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
package org.ow2.easywsdl.wsdl.impl.wsdl11;

import java.io.StringWriter;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.ow2.easywsdl.u.NotImplementedException;
import org.ow2.easywsdl.wsdl.api.Description;
import org.ow2.easywsdl.wsdl.api.WSDLException;
import org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.TDefinitions;
import org.ow2.easywsdl.wsdl.util.CustomPrefixMapper;
import org.ow2.easywsdl.wsdl.util.Util;
import org.w3c.dom.Document;

/**
 * @author Nicolas Salatge - eBM WebSourcing
 */
public class WSDLWriterImpl implements org.ow2.easywsdl.wsdl.api.WSDLWriter {

	/**
	 * The JAXB unique marshaller.
	 */

	private DocumentBuilderFactory builder = null;


	
	/*
	 * Private object initializations
	 */
	public WSDLWriterImpl() throws WSDLException {
		WSDLJAXBContext.getInstance();
		// marshaller.setSchema(schema);

		builder = DocumentBuilderFactory.newInstance();
		builder.setNamespaceAware(true);
	}



	/*
	 * Method used to set predefined namespace prefixes.
	 */
	public void useCustomNamespacesPrefixes(String[] customPrefixes) throws WSDLException {

		// try {
		new CustomPrefixMapper(customPrefixes);
		// this.marshaller.setProperty(
		// "com.sun.xml.bind.namespacePrefixMapper", mapper);
		// } catch (PropertyException e) {
		// throw new WSDLException(e);
		// }
	}

	/*
	 * Method used to set normalized namespace prefixes.
	 */
	public void useNormalizedNamespacesPrefixes() throws WSDLException {

		// try {
		new CustomPrefixMapper();
		// this.marshaller.setProperty(
		// "com.sun.xml.bind.namespacePrefixMapper", mapper);
		// } catch (PropertyException e) {
		// throw new WSDLException(e);
		// }
	}

	@SuppressWarnings("unchecked")
	// TODO: change visibility to private
	public Document convertWSDL11Definition2DOMElement(final TDefinitions wsdlDescriptor, String schemaLocation) throws WSDLException {
		Document doc = null;
		try {
			// FIXME: Following instruction is not thread-safe
			doc = builder.newDocumentBuilder().newDocument();

			// TODO : Check if it is a Thread safe method
			final JAXBElement element = new JAXBElement(new QName(Constants.WSDL_11_NAMESPACE, Constants.WSDL11_ROOT_TAG), wsdlDescriptor.getClass(), wsdlDescriptor);

			Marshaller marshaller = WSDLJAXBContext.getInstance().getJaxbContext().createMarshaller();
			if (schemaLocation != null) {
				marshaller.setProperty("jaxb.schemaLocation", schemaLocation);
			}
			marshaller.marshal(element, doc);

		} catch (final JAXBException ex) {
			throw new WSDLException("Failed to build XML binding from WSDL descriptor Java classes", ex);
		} catch (final ParserConfigurationException ex) {
			throw new WSDLException("Failed to build XML binding from WSDL descriptor Java classes", ex);

		}
		return doc;
	}

	@SuppressWarnings("unchecked")
	// TODO: change visibility to private
	public String convertWSDL11Definition2String(final TDefinitions wsdlDescriptor, String schemaLocation) throws WSDLException {

		try {
			final StringWriter stringWriter = new StringWriter();
			// TODO : Check if it is a Thread safe method

			final JAXBElement element = new JAXBElement(new QName(Constants.WSDL_11_NAMESPACE, Constants.WSDL11_ROOT_TAG), wsdlDescriptor.getClass(), wsdlDescriptor);

			Marshaller marshaller = WSDLJAXBContext.getInstance().getJaxbContext().createMarshaller();
			if (schemaLocation != null) {
				marshaller.setProperty("jaxb.schemaLocation", schemaLocation);
			}
			marshaller.marshal(element, stringWriter);

			return stringWriter.toString();
		} catch (final JAXBException e) {
			throw new WSDLException("Failed to build XML binding from Agreement descriptor Java classes", e);
		}
	}


	public Document getDocument(final Description wsdlDef) throws WSDLException {
		Document doc = null;
		if ((wsdlDef != null) && (wsdlDef instanceof org.ow2.easywsdl.wsdl.impl.wsdl11.DescriptionImpl)) {
			try {
				String schemaLocation = Util.convertSchemaLocationIntoString(wsdlDef);
				doc = this.convertWSDL11Definition2DOMElement(((org.ow2.easywsdl.wsdl.impl.wsdl11.DescriptionImpl) wsdlDef).getModel(), schemaLocation);
				if (wsdlDef.getDocumentBaseURI() != null) {
					doc.setDocumentURI(wsdlDef.getDocumentBaseURI().toString());
				} else {
					doc.setDocumentURI(".");
				}
			} catch (final WSDLException e) {
				throw new WSDLException("Can not write wsdl description", e);
			} 
		}
		return doc;
	}

	public boolean getFeature(final String name) throws IllegalArgumentException {
		throw new NotImplementedException();
	}

	public void setFeature(final String name, final boolean value) throws IllegalArgumentException {
		throw new NotImplementedException();
	}

	public String writeWSDL(final Description wsdlDef) throws WSDLException {
		String res = null;
		if ((wsdlDef != null) && (wsdlDef instanceof org.ow2.easywsdl.wsdl.impl.wsdl11.DescriptionImpl)) {
			try {
				String schemaLocation = Util.convertSchemaLocationIntoString(wsdlDef);

				res = this.convertWSDL11Definition2String(((org.ow2.easywsdl.wsdl.impl.wsdl11.DescriptionImpl) wsdlDef).getModel(), schemaLocation);
			} catch (final WSDLException e) {
				throw new WSDLException("Can not write wsdl description", e);
			} 
		}
		return res;
	}

}
