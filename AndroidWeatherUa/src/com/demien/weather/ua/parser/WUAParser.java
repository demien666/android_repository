package com.demien.weather.ua.parser;

import java.io.StringReader;

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

import com.demien.weather.ua.domain.Day;
import com.demien.weather.ua.domain.Report;

public class WUAParser {
	private String weatherData;
	private String parseError;

	public WUAParser(String weatherData) {
		this.weatherData = weatherData;
	}

	private String getNodeAttribute(NamedNodeMap attributes, String attribute)
			throws Exception {
		if (attributes == null)
			throw new Exception("Attribute map is null(key=" + attribute + ")");
		if (attribute == null)
			throw new Exception("Attribute key is null");
		Node node = attributes.getNamedItem(attribute);
		return node.getNodeValue();
	}

	public Report parse() throws Exception {
		// TownReportVO townReport = new TownReportVO();
		parseError = null;
		Report report=new Report();

			DocumentBuilderFactory builderFactory = DocumentBuilderFactory
					.newInstance();
			builderFactory.setNamespaceAware(true);
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			// Document document = builder.parse(data);
			InputSource source = new InputSource(new StringReader(weatherData));
			//Document document = builder.parse("http://xml.weather.co.ua/1.2/forecast/23?dayf=5&lang=ru&userid=yoursite_com");
			Document document = builder.parse(source);

			XPath xpath = XPathFactory.newInstance().newXPath();
			Node cityNameN=(Node) xpath.evaluate("/forecast/city/name",
					document, XPathConstants.NODE);
			report.setCityName(cityNameN.getTextContent());
			Node regionNameN=(Node) xpath.evaluate("/forecast/city/region/name",
					document, XPathConstants.NODE);
			if (regionNameN!=null) {
			    report.setRegionName(regionNameN.getTextContent());
			}
			Node countryNameN=(Node) xpath.evaluate("/forecast/city/country/name",
					document, XPathConstants.NODE);
			report.setCountryName(countryNameN.getTextContent());				
			
			
			// -----------------------
			// -- get days data
			// ------------------------
			Node dayNodes = (Node) xpath.evaluate("/forecast/forecast",
					document, XPathConstants.NODE);
			NodeList dayNodelist = dayNodes.getChildNodes();
			for (int i = 0; i < dayNodelist.getLength(); i++) {
				Day day = new Day();
				Node dayNode = dayNodelist.item(i);
				NamedNodeMap dayNodeAttributes = dayNode.getAttributes();
				if (dayNodeAttributes != null) {

					day.setDate(getNodeAttribute(dayNodeAttributes, "date"));
					day.setHour(getNodeAttribute(dayNodeAttributes, "hour"));

					// child nodes
					NodeList itemsNodelist = dayNode.getChildNodes();
					for (int j = 0; j < itemsNodelist.getLength(); j++) {
						Node item = itemsNodelist.item(j);
						String itemName = item.getLocalName();
						if (itemName != null) {
							// cloud
							if (itemName.equals("cloud")) {
								day.setCloud(item.getTextContent());
							}
							// pict
							if (itemName.equals("pict")) {
								day.setPict(item.getTextContent());
							}	
							// ppcp
							if (itemName.equals("ppcp")) {
								day.setPpcp(item.getTextContent());
							}	
							// wpi
							if (itemName.equals("wpi")) {
								day.setWpi(item.getTextContent());
							}								
							// temperature
							if (itemName.equals("t")) {
								NodeList childNL = item.getChildNodes();
								for (int c = 0; c < childNL.getLength(); c++) {
									Node childN = childNL.item(c);
									String childName = childN.getLocalName();
									if (childName != null) {
										if (childName.equals("min")) {
											day.settMin(childN.getTextContent());
										}
										if (childName.equals("max")) {
											day.settMax(childN.getTextContent());
										}
									}

								}
							}
							
							// pressure
							if (itemName.equals("p")) {
								NodeList childNL = item.getChildNodes();
								for (int c = 0; c < childNL.getLength(); c++) {
									Node childN = childNL.item(c);
									String childName = childN.getLocalName();
									if (childName != null) {
										if (childName.equals("min")) {
											day.setpMin(childN.getTextContent());
										}
										if (childName.equals("max")) {
											day.setpMax(childN.getTextContent());
										}
									}

								}
							}	
							// hmid
							if (itemName.equals("hmid")) {
								NodeList childNL = item.getChildNodes();
								for (int c = 0; c < childNL.getLength(); c++) {
									Node childN = childNL.item(c);
									String childName = childN.getLocalName();
									if (childName != null) {
										if (childName.equals("min")) {
											day.setHmidMin(childN.getTextContent());
										}
										if (childName.equals("max")) {
											day.setHmidMax(childN.getTextContent());
										}
									}

								}
							}							
							// wind
							if (itemName.equals("wind")) {
								NodeList childNL = item.getChildNodes();
								for (int c = 0; c < childNL.getLength(); c++) {
									Node childN = childNL.item(c);
									String childName = childN.getLocalName();
									if (childName != null) {
										if (childName.equals("min")) {
											day.setWindMin(childN.getTextContent());
										}
										if (childName.equals("max")) {
											day.setWindMax(childN.getTextContent());
										}
										if (childName.equals("rumb")) {
											day.setWrumb(childN.getTextContent());
										}
									}

								}
							}							
						}
					}
					//System.out.println(day);
					report.addDayReport(day);
				}

			}
			// NamedNodeMap attributes = townNode.getAttributes();

		//System.out.println(report);
			return report;
	}
	
	public String getParseStatus() {
		return parseError;
	}



}
