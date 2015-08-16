package com.demien.weather.ua.parser;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.demien.weather.ua.domain.SearchResult;

public class WUASearchParser extends BaseParser {

	public WUASearchParser(String parseData) {
		super(parseData);
	}
	
	public List<SearchResult> parse() throws Exception {
		List<SearchResult> result=new ArrayList<SearchResult>();
		
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory
				.newInstance();
		builderFactory.setNamespaceAware(true);
		DocumentBuilder builder = builderFactory.newDocumentBuilder();
		// Document document = builder.parse(data);
		InputSource source = new InputSource(new StringReader(parseData));
		//Document document = builder	.parse("http://xml.weather.co.ua/1.2/city/?search=Хмель");
		Document document = builder.parse(source);

		XPath xpath = XPathFactory.newInstance().newXPath();
		Node rootN=(Node) xpath.evaluate("/city",
				document, XPathConstants.NODE);
		
		NodeList cityNL=rootN.getChildNodes();
		for (int i=0; i<cityNL.getLength(); i++) {
			SearchResult searchResult=new SearchResult();
			Node cityN=cityNL.item(i);
			if (cityN.getLocalName()!=null ) {
				NamedNodeMap cityNNM=cityN.getAttributes();
				String id=getNodeAttribute(cityNNM, "id");
				searchResult.setCityId(id);
				// city params
				NodeList paramsNL=cityN.getChildNodes();
				for (int j=0; j<paramsNL.getLength(); j++) {
					Node paramN=paramsNL.item(j);
					if  (paramN!=null && paramN.getLocalName()!=null) {
						String name=paramN.getLocalName();
						if (name.equals("name")) {
							searchResult.setCityName(paramN.getTextContent());
						}
						if (name.equals("name_en")) {
							searchResult.setCityNameEng(paramN.getTextContent());
						}
						if (name.equals("region")) {
							searchResult.setRegion(paramN.getTextContent());
						}
						if (name.equals("country")) {
							searchResult.setCountry(paramN.getTextContent());
						}
					}
				}
				result.add(searchResult);
			}
		}
		
		return result;
	}
	

}
