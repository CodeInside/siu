/**
 * easyWSDL - easyWSDL toolbox Platform.
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
package org.ow2.easywsdl.wsdl.impl.wsdl20;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;

import org.ow2.easywsdl.schema.api.absItf.AbsItfSchema;
import org.ow2.easywsdl.schema.util.EasyXMLFilter;
import org.ow2.easywsdl.wsdl.api.Description;
import org.ow2.easywsdl.wsdl.api.WSDLException;
import org.ow2.easywsdl.wsdl.api.WSDLImportException;
import org.ow2.easywsdl.wsdl.api.abstractElmt.AbstractWSDLReaderImpl;
import org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfDescription;
import org.ow2.easywsdl.wsdl.org.w3.ns.wsdl.DescriptionType;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * @author Nicolas Salatge - eBM WebSourcing
 */
public class WSDLReaderImpl extends AbstractWSDLReaderImpl implements org.ow2.easywsdl.wsdl.api.WSDLReader {

	private static final Logger LOG = Logger.getLogger(WSDLReaderImpl.class.getName());


	

	/*
	 * Private object initializations
	 */
	public WSDLReaderImpl() throws WSDLException {
		super();
		WSDLJAXBContext.getInstance();
	}






	/**
	 * {@inheritDoc}
	 */
	public Description read(final URL wsdlURL) throws WSDLException, IOException, URISyntaxException {
		try {
			InputSource inputSource = new InputSource(wsdlURL.openStream());
			inputSource.setSystemId(wsdlURL.toString());

			return this.read(inputSource);
		} catch (final MalformedURLException e) {
			throw new RuntimeException("The provided well-formed URL has been detected as malformed !!");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Description read(final InputSource inputSource) throws WSDLException, MalformedURLException, URISyntaxException {
		final String systemId = inputSource.getSystemId();
		if (systemId != null ) {
			this.setDocumentBaseURI(new URI(inputSource.getSystemId()));
		}
		return this.read(inputSource, null, null, true);
	}

	/**
	 * {@inheritDoc}
	 */
	public Description read(final Document doc) throws WSDLException, URISyntaxException {
		try {
			// The DOM Document needs to be converted into an InputStource
			final ByteArrayOutputStream baos = new ByteArrayOutputStream();
			final StreamResult streamResult = new StreamResult(baos);
			// FIXME: The Transformer creation is not thread-safe
			final Transformer transformer = TransformerFactory.newInstance()
			.newTransformer();
			transformer.transform(new DOMSource(doc), streamResult);
			baos.flush();
			baos.close();

			final InputSource documentInputSource = new InputSource(
					new ByteArrayInputStream(baos.toByteArray()));
			documentInputSource.setSystemId(doc.getBaseURI());

			return this.read(documentInputSource);

		} catch (final TransformerConfigurationException e) {
			throw new WSDLException(e);
		} catch (final TransformerFactoryConfigurationError e) {
			throw new WSDLException(e);
		} catch (final TransformerException e) {
			throw new WSDLException(e);
		} catch (final IOException e) {
			throw new WSDLException(e);
		}

	}

	/**
	 * {@inheritDoc}
	 */
	public Description read(InputSource inputSource, Map<URI, AbsItfDescription> descriptionImports, Map<URI, AbsItfSchema> schemaImports, boolean deleteImports) throws WSDLException, MalformedURLException, URISyntaxException {
		final String systemId = inputSource.getSystemId();
		URI systemIdURI;
		if (systemId != null ) {
			systemIdURI = new URI(systemId);
			this.setDocumentBaseURI(systemIdURI);
		} else {
			systemIdURI = new File(".").toURI();
		}

		Description desc = null;
		if(deleteImports){
			this.getImportList().clear();
		}
		try {
			Unmarshaller unmarshaller = WSDLJAXBContext.getInstance().getJaxbContext().createUnmarshaller();
			LOG.fine("Loading " + systemIdURI);
			XMLReader xmlReader = XMLReaderFactory.createXMLReader();
			EasyXMLFilter filter = new EasyXMLFilter(xmlReader);
			SAXSource saxSource = new SAXSource(filter, inputSource);

			// TODO use SAX validation instead of JAXB validation
			// turn off the JAXB provider's default validation mechanism to
			// avoid duplicate validation
			// SchemaReaderImpl.getUnMarshaller().setValidating( false );

			final JAXBElement<DescriptionType> wsdlBinding = unmarshaller.unmarshal(saxSource, DescriptionType.class);

			DescriptionType def = wsdlBinding.getValue();

			desc = new org.ow2.easywsdl.wsdl.impl.wsdl20.DescriptionImpl(systemIdURI, def, filter.getNamespaceMapper(), filter.getSchemaLocator(), this.getFeatures(), descriptionImports, schemaImports, this);

		} catch (JAXBException e) {
			throw new WSDLException("Can not get wsdl at: " + systemIdURI.toString(), e);
		} catch (SAXException e) {
			throw new WSDLException("Can not get wsdl at: " + systemIdURI.toString(), e);
		} catch (WSDLImportException e) {
			throw new WSDLException("Can not get wsdl at: " + systemIdURI.toString(), e);
		} catch (NumberFormatException e) {
			throw new WSDLException("Can not get wsdl at: " + systemIdURI.toString() + " WSDL too large !", e);
		}

		return desc;
	}

	public Description read(InputSource source,
			Map<URI, AbsItfDescription> descriptionImports,
			Map<URI, AbsItfSchema> schemaImports) throws WSDLException,
			MalformedURLException, URISyntaxException {
		return this.read(source, descriptionImports, schemaImports, true);
	}

	/*public Description readWSDL(Document doc, Map<URI, AbsItfDescription> descriptionImports, Map<URI, AbsItfSchema> schemaImports) throws WSDLException {
		Description desc = null;
		try {
			URI uri = null;
			if (doc.getDocumentURI() != null) {
				uri = new URI(doc.getDocumentURI());
			} else {
				uri = new URI(".");
			}

			WSDLVersionConstants version = Util.getVersionInDocument(doc);

			desc = this.readWSDL(uri, version, SourceHelper.convertDOMSource2InputSource(new DOMSource(doc)), descriptionImports, schemaImports);
		} catch (URISyntaxException e) {
			throw new WSDLException(e);
		} catch (XmlException e) {
			throw new WSDLException(e);
		}
		return desc;
	}*/

}
