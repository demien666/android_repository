package com.demien.weather.ua.parser;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public abstract class BaseParser {
	protected String parseError;
	protected String parseData;
	
	public BaseParser(String parseData) {
		this.parseData=parseData;
	}
	
	
	protected String getNodeAttribute(NamedNodeMap attributes, String attribute)
			throws Exception {
		if (attributes == null)
			throw new Exception("Attribute map is null(key=" + attribute + ")");
		if (attribute == null)
			throw new Exception("Attribute key is null");
		Node node = attributes.getNamedItem(attribute);
		return node.getNodeValue();
	}

}
