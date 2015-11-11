package com.crime.ruianextractor;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;

public class Main
{
	public static void main(String args[]) throws FileNotFoundException
	{
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setValidating(false);
		
		//System.out.printf("Factory class name: %s\n", factory.getClass().getName());
		// set namespace aware?
		String xmlFilePath = args[0];
		String outputDirectoryPath = args[1];
		String configFilePath = args[2];
		
		BufferedReader reader =
				new BufferedReader(
						new InputStreamReader(
								new FileInputStream(xmlFilePath),
								StandardCharsets.UTF_8)
						);
		
		try {
			SAXParser saxParser = factory.newSAXParser();
			//System.out.printf("SAX parser class name: %s\n", saxParser.getClass().getName());
			saxParser.parse(
					new InputSource(reader),
					new ExtractUzemniJednotky(configFilePath, outputDirectoryPath));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if(reader != null)
				{
					reader.close();
				}
			}
			catch(IOException expception)
			{
				expception.printStackTrace();
			}
		}
	}
}
