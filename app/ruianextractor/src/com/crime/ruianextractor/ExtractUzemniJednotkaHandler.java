package com.crime.ruianextractor;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ExtractUzemniJednotkaHandler extends DefaultHandler {
	private final String mElementName;
	private final PrintWriter mOutputFileStream;
	private final boolean mAppend;
	private int mDepthInElement = -1;
	private final HashSet<String> mIgnoredSubElements;
	private boolean mInIgnoredElement;

	public ExtractUzemniJednotkaHandler(String elementName,
			String[] ignoredSubElements, String outputDirectoryPath,
			String outputFileName, boolean appendOutput)
			throws FileNotFoundException, UnsupportedEncodingException {
		this.mElementName = elementName;
		this.mAppend = appendOutput;
		this.mOutputFileStream = new PrintWriter(new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(Paths.get(
						outputDirectoryPath, outputFileName).toFile(),
						appendOutput), StandardCharsets.UTF_8)), true);

		this.mIgnoredSubElements = new HashSet<String>(
				Arrays.asList(ignoredSubElements));
		this.mInIgnoredElement = false;
	}

	@Override
	public void startDocument() throws SAXException {

		// this.mOutputFileStream.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		this.mInIgnoredElement = false;

		if (!this.mAppend) {
			this.mOutputFileStream
					.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
			this.mOutputFileStream
					.println("<root xsi:schemaLocation = \"urn:cz:isvs:ruian:schemas:VymennyFormatTypy:v1 ../ruian/xsd/vymenny_format/VymennyFormatTypy.xsd\" xmlns:gml = \"http://www.opengis.net/gml/3.2\" xmlns:xlink = \"http://www.w3.org/1999/xlink\" xmlns:xsi = \"http://www.w3.org/2001/XMLSchema-instance\" xmlns:ami = \"urn:cz:isvs:ruian:schemas:AdrMistoIntTypy:v1\" xmlns:base = \"urn:cz:isvs:ruian:schemas:BaseTypy:v1\" xmlns:coi = \"urn:cz:isvs:ruian:schemas:CastObceIntTypy:v1\" xmlns:com = \"urn:cz:isvs:ruian:schemas:CommonTypy:v1\" xmlns:kui = \"urn:cz:isvs:ruian:schemas:KatUzIntTypy:v1\" xmlns:kri = \"urn:cz:isvs:ruian:schemas:KrajIntTypy:v1\" xmlns:mci = \"urn:cz:isvs:ruian:schemas:MomcIntTypy:v1\" xmlns:mpi = \"urn:cz:isvs:ruian:schemas:MopIntTypy:v1\" xmlns:obi = \"urn:cz:isvs:ruian:schemas:ObecIntTypy:v1\" xmlns:oki = \"urn:cz:isvs:ruian:schemas:OkresIntTypy:v1\" xmlns:opi = \"urn:cz:isvs:ruian:schemas:OrpIntTypy:v1\" xmlns:pai = \"urn:cz:isvs:ruian:schemas:ParcelaIntTypy:v1\" xmlns:pui = \"urn:cz:isvs:ruian:schemas:PouIntTypy:v1\" xmlns:rsi = \"urn:cz:isvs:ruian:schemas:RegSouIntiTypy:v1\" xmlns:spi = \"urn:cz:isvs:ruian:schemas:SpravObvIntTypy:v1\" xmlns:sti = \"urn:cz:isvs:ruian:schemas:StatIntTypy:v1\" xmlns:soi = \"urn:cz:isvs:ruian:schemas:StavObjIntTypy:v1\" xmlns:uli = \"urn:cz:isvs:ruian:schemas:UliceIntTypy:v1\" xmlns:vci = \"urn:cz:isvs:ruian:schemas:VuscIntTypy:v1\" xmlns:vf = \"urn:cz:isvs:ruian:schemas:VymennyFormatTypy:v1\" xmlns:zji = \"urn:cz:isvs:ruian:schemas:ZsjIntTypy:v1\" xmlns:voi = \"urn:cz:isvs:ruian:schemas:VOIntTypy:v1\">");
		}
	}

	@Override
	public void startElement(String uri, String localName,
			String qName, Attributes attributes) {

		if (this.mDepthInElement >= 0 && !this.mInIgnoredElement) {
			if (!this.mIgnoredSubElements.contains(qName)) {
				++this.mDepthInElement;
				this.writeStartElement(qName, attributes);
			} else {
				this.mInIgnoredElement = true;
			}
		} else if (this.mDepthInElement < 0
				&& qName.equalsIgnoreCase(this.mElementName)) {
			this.mDepthInElement = 0;
		}
	}

	public boolean endElementAndReleaseOwnership(String uri, String localName,
			String qName) {

		if (this.mDepthInElement >= 0) {
			if (this.mInIgnoredElement) {
				if (this.mIgnoredSubElements.contains(qName)) {
					this.mInIgnoredElement = false;
				}
			} else if (--this.mDepthInElement >= 0) {
				this.writeEndElement(qName);
			}
			else
			{
				return true;
			}
		}
		
		return false;
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {

		if (this.mDepthInElement >= 0 && !this.mInIgnoredElement)
			this.writeCharacters(ch, start, length);
	}

	@Override
	public void endDocument() throws SAXException {

		if (!this.mAppend) {
			this.mOutputFileStream.println("</root>");
		}
	}

	private void writeStartElement(String qName, Attributes attributes) {

		// System.out.printf("<%s", qName);
		// this.mOutputFileStream.print(this.mDepthInElement);
		this.mOutputFileStream.printf("<%s", qName);

		for (int i = 0; i < attributes.getLength(); i++) {
			this.mOutputFileStream.printf(" %s = \"%s\"",
					attributes.getQName(i), attributes.getValue(i));
		}

		this.mOutputFileStream.print(">");
		this.mOutputFileStream.println();
	}

	private void writeEndElement(String qName) {

		// this.mOutputFileStream.print(this.mDepthInElement);
		// System.out.printf("</%s>", qName);
		this.mOutputFileStream.printf("</%s>", qName);
		this.mOutputFileStream.println();
	}

	private void writeCharacters(char[] ch, int start, int length) {

		// this.mOutputFileStream.printf("%d ", this.mDepthInElement);

		this.mOutputFileStream.print(new String(ch, start, length));
	}

	@Override
	protected void finalize() throws Throwable {
		// this.mOutputFileStream.flush();
		this.mOutputFileStream.close();
		// TODO Auto-generated method stub
		super.finalize();
	}
}
