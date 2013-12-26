/**
 * easySchema - easyWSDL toolbox Platform.
 * Copyright (c) 2008,  eBM Websourcing
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the University of California, Berkeley nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE REGENTS AND CONTRIBUTORS ``AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE REGENTS AND CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.ow2.easywsdl.schema.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.ow2.easywsdl.schema.api.XmlException;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/**
 * @author Nicolas Salatge - eBM WebSourcing
 */
public final class SourceHelper {


	/**
	 * Creates a new instance of {@link SourceHelper}
	 * 
	 */
	private SourceHelper() {
		super();
	}


	public static InputSource convertDOMSource2InputSource(final DOMSource domSource)
	throws XmlException {
		InputSource source = null;
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			StreamResult streamResult = new StreamResult(os);
			Transformer transformer = 
				TransformerFactory.newInstance().newTransformer();
			transformer.transform(domSource, streamResult);
			os.flush();
			os.close();
			InputStream is = new java.io.ByteArrayInputStream(os.toByteArray());

			StreamSource attach = new StreamSource(is);
			source = SAXSource.sourceToInputSource(attach);
		} catch (final IOException e) {
			throw new XmlException(e);
		} catch (TransformerConfigurationException e) {
			throw new XmlException(e);
		} catch (TransformerFactoryConfigurationError e) {
			throw new XmlException(e);
		} catch (TransformerException e) {
			throw new XmlException(e);
		}
		return source;
	}

	public static DOMSource convertInputSource2DOMSource(final InputSource inputSource)
	throws XmlException {
		DOMSource res = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			DocumentBuilder db = factory.newDocumentBuilder();
			Document document = db.parse(inputSource);
			res = new DOMSource(document);
		} catch (final ParserConfigurationException e) {
			throw new XmlException(e);
		} catch (final SAXException e) {
			throw new XmlException(e);
		} catch (final IOException e) {
			throw new XmlException(e);
		}
		return res;
	}



	
}
