package org.ow2.easywsdl.wsdl.util;

import java.io.IOException;

import org.ow2.easywsdl.wsdl.api.WSDLException;
import org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfDescription.WSDLVersionConstants;
import org.w3c.dom.Document;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public class WSDLVersionDetector extends DefaultHandler {

	WSDLVersionConstants version = null;

	public WSDLVersionConstants getVersion() {
		return version;
	}

	@Override
	public void startElement(String uri, String localName, String name,
			Attributes attributes) throws SAXException {
		if (localName
				.equals(org.ow2.easywsdl.wsdl.impl.wsdl20.Constants.WSDL20_ROOT_TAG)
				&& uri
						.equals(org.ow2.easywsdl.wsdl.impl.wsdl20.Constants.WSDL_20_NAMESPACE)) {
			this.version = WSDLVersionConstants.WSDL20;
		}
		if ((localName
				.equals(org.ow2.easywsdl.wsdl.impl.wsdl11.Constants.WSDL11_ROOT_TAG))
				&& (uri
						.equals(org.ow2.easywsdl.wsdl.impl.wsdl11.Constants.WSDL_11_NAMESPACE))) {
			this.version = WSDLVersionConstants.WSDL11;
		}

		if (version != null) {
			throw new SAXException("Cool! We found the WSDL version");
		}
		super.startElement(uri, localName, name, attributes);
	}

	public static WSDLVersionConstants getVersion(InputSource input) throws WSDLException {
		WSDLVersionDetector versionDetector = new WSDLVersionDetector();
		try {
			final XMLReader xmlReader = XMLReaderFactory.createXMLReader();
			xmlReader.setContentHandler(versionDetector);
			xmlReader.parse(input);
		} catch (SAXException e) {
			// NOP We expected that
		} catch (IOException e) {
			throw new WSDLException("Unable to get WSDL version.", e);
		}
		return versionDetector.getVersion();
	}
    
    public static WSDLVersionConstants getVersion(Document doc) {

        final String localPartWithoutPrefix = Util.getLocalPartWithoutPrefix(doc.getDocumentElement().getNodeName());
        WSDLVersionConstants version = null;
        
        if (localPartWithoutPrefix
                .equals(org.ow2.easywsdl.wsdl.impl.wsdl20.Constants.WSDL20_ROOT_TAG)) {
            version = WSDLVersionConstants.WSDL20;
        }
        if (localPartWithoutPrefix
                .equals(org.ow2.easywsdl.wsdl.impl.wsdl11.Constants.WSDL11_ROOT_TAG)) {
            version = WSDLVersionConstants.WSDL11;
        }
        return version;
    }

}
