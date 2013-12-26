/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.jar.Attributes;
import java.util.jar.JarInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class JarParseUtils {

  public static boolean isOsgiComponent(JarInputStream jarStream){
	Attributes mainAttributes = jarStream.getManifest().getMainAttributes();
	String serviceComponent = mainAttributes.getValue("Service-Component");
	String exportPackage = mainAttributes.getValue("Export-Package");
	String importPackage = mainAttributes.getValue("Import-Package");
		
	return StringUtils.isNotEmpty(serviceComponent) && StringUtils.isNotEmpty(exportPackage) && StringUtils.isNotEmpty(importPackage);
  }
   
  public static Node getChildForNlByNames(NodeList nl, String... names) {
	Node result = null;
	NodeList childNodes = nl;
	for(String name : names){
		result = getChildForNlByName(childNodes, name);		
		childNodes = result.getChildNodes();
		if(childNodes == null){
			return null;
		}
	}
	return result;
  }
 	
  private static Node getChildForNlByName(NodeList nl, String name) {	
	for(int i = 0 ; i <= nl.getLength(); i++){
   	  if(name.equals(nl.item(i).getLocalName())){
   		return nl.item(i);
   	  }
    }
	return null;
  }

  public static Document readXml(InputStream is) throws SAXException, IOException, ParserConfigurationException {
     DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

     dbf.setValidating(false);
     dbf.setIgnoringComments(false);
     dbf.setIgnoringElementContentWhitespace(true);
     dbf.setNamespaceAware(true);
     // dbf.setCoalescing(true);
     // dbf.setExpandEntityReferences(true);

     DocumentBuilder db = dbf.newDocumentBuilder();
     db.setEntityResolver(new NullResolver());

     // db.setErrorHandler( new MyErrorHandler());

     return db.parse(is);
  }
}
class NullResolver implements EntityResolver {
  public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
	return new InputSource(new StringReader(""));
  }
}