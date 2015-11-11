package com.crime.ruianextractor;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ExtractUzemniJednotky extends DefaultHandler
{
	private final HashMap<String, ExtractUzemniJednotkaHandler> mHandlers;
	private ExtractUzemniJednotkaHandler mCurrentHandler;
	private boolean mIgnoreContent;
	private String mIgnoredElementQName;
	private int mIgnoreDepth = 0;
	private int mCurrentDepth = 0;
	private final String DATA_QNAME = "vf:Data";
	private boolean mInData;
	
	public ExtractUzemniJednotky(final String configFilePath, final String outputDirectoryPath) throws FileNotFoundException
	{
		this.mHandlers = new HashMap<String, ExtractUzemniJednotkaHandler>();
		
		BufferedReader reader = new BufferedReader(new FileReader(Paths.get(configFilePath).toFile()));
		String line;
		try
		{
			while((line = reader.readLine()) != null)
			{
				String[] lineTokens = line.split(" ");
				
				String elementQName = lineTokens[0];
				String elementOutputFilePath = lineTokens[1];
				boolean append = Boolean.valueOf(lineTokens[2]);
				this.mHandlers.put(
						elementQName,
						new ExtractUzemniJednotkaHandler(
							elementQName,
							Arrays.copyOfRange(lineTokens, 3, lineTokens.length),
							outputDirectoryPath,
							elementOutputFilePath,
							append));
			}
		}
		catch(Exception e1)
		{
			e1.printStackTrace();
		}
		finally
		{
			try
			{
				if(reader != null)
					reader.close();
			}
			catch(Exception e2)
			{
				e2.printStackTrace();
			}
		}
	}
	
	@Override
	public void startDocument() throws SAXException {
		
		for(ExtractUzemniJednotkaHandler handler : this.mHandlers.values())
		{
			handler.startDocument();
		}
	}
	
	public void startElement(
	  String namespaceURI,
	  String localName, // simple name
	  String qName, // qualified name
	  Attributes attrs) throws SAXException {
		
		if(!this.mInData)
		{
			if(this.mInData = qName.equalsIgnoreCase(DATA_QNAME))
			{
				this.mCurrentDepth = 0;
				this.mIgnoreContent = false;
			}
			
			return;
		}
		
		if(this.mIgnoreContent)
		{
			++this.mCurrentDepth;
			return;
		}
		
		++this.mCurrentDepth;
		
		if(this.mCurrentHandler == null)
		{
			this.mCurrentHandler = this.mHandlers.get(qName);
//			if(this.mCurrentHandler != null)
//			{
//				System.err.printf("Found handler for %s\n", qName);
//			}
		}
		
		if(this.mCurrentHandler != null)
		{
			this.mCurrentHandler.startElement(namespaceURI, localName, qName, attrs);
		}
		else
		{
			this.mIgnoreContent = true;
			this.mIgnoredElementQName = qName;
			this.mIgnoreDepth = this.mCurrentDepth;
			//System.err.printf("Did not find handler for %s\n. Ignoring its content at depth %d", qName, mIgnoreDepth);
		}
	}

	public void endElement(String uri, String localName, String qName) throws SAXException
	{
		if(!this.mInData)
			return;
		
		if(this.mIgnoreContent)
		{
			//System.err.printf("Ignoring the end element of %s\n", qName);
			if(this.mCurrentDepth == this.mIgnoreDepth && qName.equalsIgnoreCase(this.mIgnoredElementQName))
			{
				//System.err.printf("But ending the ignoration, since the element being ignored ends at depth %d\n", mIgnoreDepth);
				this.mIgnoreContent = false;
			}
			
			--this.mCurrentDepth;
			
			return;
		}
		
		if(this.mInData = !qName.equalsIgnoreCase(DATA_QNAME))
		{
			if(this.mCurrentHandler.endElementAndReleaseOwnership(uri, localName, qName))
			{
				//System.err.printf("Setting current handler to null.\n", qName);
				this.mCurrentHandler = null;
			}
			
			--this.mCurrentDepth;
		}
	}

	public void characters(char buf[], int offset, int len) throws SAXException
	{
		if(this.mIgnoreContent || !this.mInData)
			return;
		
		if(this.mCurrentHandler != null)
		{
			this.mCurrentHandler.characters(buf, offset, len);
		}
	}
	
	@Override
	public void endDocument() throws SAXException {
		
		for(ExtractUzemniJednotkaHandler handler : this.mHandlers.values())
		{
			handler.endDocument();
		}
	}
  }
