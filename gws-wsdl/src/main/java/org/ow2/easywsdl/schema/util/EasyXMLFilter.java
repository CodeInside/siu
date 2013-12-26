package org.ow2.easywsdl.schema.util;

import java.util.StringTokenizer;

import org.ow2.easywsdl.schema.api.extensions.NamespaceMapperImpl;
import org.ow2.easywsdl.schema.api.extensions.SchemaLocatorImpl;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLFilterImpl;

public class EasyXMLFilter extends XMLFilterImpl {

	private boolean first=true;
	
	private SchemaLocatorImpl schemaLocator = new SchemaLocatorImpl();
	
	private NamespaceMapperImpl namespaceContext = new NamespaceMapperImpl();
	
	public EasyXMLFilter(XMLReader parent) {
		super(parent);
	}

	@Override
	public void startElement(String uri, String localName, String name,
			Attributes atts) throws SAXException {
		if (first){
			String schemaLocation = atts.getValue("http://www.w3.org/2001/XMLSchema-instance", "schemaLocation");
			if (schemaLocation!=null){
				for (StringTokenizer stringTokenizer = new StringTokenizer(schemaLocation); stringTokenizer.hasMoreTokens(); ){
					String schemaUri = stringTokenizer.nextToken();
					if (stringTokenizer.hasMoreTokens()){
						String location = stringTokenizer.nextToken();
						schemaLocator.addSchemaLocation(schemaUri,location);
					}
				}
			}
			first = false;
		}
		super.startElement(uri, localName, name, atts);
	}

	@Override
	public void startPrefixMapping(String prefix, String uri)
			throws SAXException {
//		if (!"".equals(prefix)){
			namespaceContext.addNamespace(prefix, uri);
//		}
		super.startPrefixMapping(prefix, uri);
	}
	
	
	@Override
	public void startDocument() throws SAXException {
		first = true;
		super.startDocument();
	}

	public SchemaLocatorImpl getSchemaLocator() {
		return schemaLocator;
	}

	public NamespaceMapperImpl getNamespaceMapper() {
		return namespaceContext;
	}

}
