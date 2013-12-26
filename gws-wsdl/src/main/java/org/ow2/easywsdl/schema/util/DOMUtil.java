package org.ow2.easywsdl.schema.util;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DOMUtil {

	public static Element getFirstElement(Element parent) {
		Element res = null;
		NodeList list = parent.getChildNodes();
		for(int i = 0; i < list.getLength(); i++) {
			if(list.item(i).getNodeType() == Node.ELEMENT_NODE) {
				res = (Element) list.item(i);
				break;
			}
		}
		return res;
	}
}
