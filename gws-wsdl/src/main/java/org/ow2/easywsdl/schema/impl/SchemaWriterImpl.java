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
package org.ow2.easywsdl.schema.impl;

import java.io.OutputStream;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.ow2.easywsdl.u.NotImplementedException;
import org.ow2.easywsdl.schema.api.Schema;
import org.ow2.easywsdl.schema.api.SchemaException;
import org.ow2.easywsdl.schema.api.extensions.NamespaceMapperImpl;
import org.w3c.dom.Document;

/**
 * @author Nicolas Salatge - eBM WebSourcing
 */
public class SchemaWriterImpl implements org.ow2.easywsdl.schema.api.SchemaWriter {


	
	private DocumentBuilderFactory builder = null;

	
    private static SchemaJAXBContext jaxbcontext = null;
	private static SchemaException contextException = null;
	
	static {
		try {
			jaxbcontext = SchemaJAXBContext.getInstance();
		} catch (final SchemaException e) {
			contextException = e;
		}
	}
	
	public static SchemaJAXBContext getJaxbContext() throws SchemaException {
		if(contextException != null) {
			throw contextException;
		}
		return jaxbcontext;
	}
	
	/*
	 * Private object initializations
	 */
	public SchemaWriterImpl() throws SchemaException {
		getJaxbContext();

		builder = DocumentBuilderFactory.newInstance();
		builder.setNamespaceAware(true);
	}

	@SuppressWarnings("unchecked")
	public Document convertSchema2DOMElement(final org.ow2.easywsdl.schema.org.w3._2001.xmlschema.Schema schemaDescriptor, NamespaceMapperImpl namespaceMapper) throws SchemaException {
		Document doc = null;
		try {
			doc = builder.newDocumentBuilder().newDocument();

			// TODO : Check if it is a Thread safe method
			final JAXBElement element = new JAXBElement(new QName(Constants.SCHEMA_NAMESPACE, Constants.SCHEMA_ROOT_TAG), schemaDescriptor.getClass(), schemaDescriptor);

			Marshaller marshaller = getJaxbContext().getJaxbContext().createMarshaller();

			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			// marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper",
			// namespaceMapper);
			marshaller.marshal(element, doc);
			
		} catch (final JAXBException ex) {
			throw new SchemaException("Failed to build XML binding from SchemaImpl descriptor Java classes", ex);
		} catch (final ParserConfigurationException ex) {
			throw new SchemaException("Failed to build XML binding from SchemaImpl descriptor Java classes", ex);

		}
		return doc;
	}



	public Document getDocument(final Schema schemaDef) throws SchemaException {
		Document doc = null;
		if ((schemaDef != null) && (schemaDef instanceof org.ow2.easywsdl.schema.impl.SchemaImpl)) {
			try {
				doc = this.convertSchema2DOMElement(((org.ow2.easywsdl.schema.impl.SchemaImpl) schemaDef).getModel(), schemaDef.getAllNamespaces());
				if (schemaDef.getDocumentURI() != null) {
					doc.setDocumentURI(schemaDef.getDocumentURI().toString());
				}
			} catch (final SchemaException e) {
				throw new SchemaException("Can not write wsdl description", e);
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

	public void writeSchema(final Schema schemaDef, OutputStream output) throws SchemaException {
		if ((schemaDef != null) && (schemaDef instanceof org.ow2.easywsdl.schema.impl.SchemaImpl)) {
			try {

				org.ow2.easywsdl.schema.org.w3._2001.xmlschema.Schema schemaDescriptor = ((org.ow2.easywsdl.schema.impl.SchemaImpl) schemaDef).getModel();
				final JAXBElement element = new JAXBElement(new QName(Constants.SCHEMA_NAMESPACE, Constants.SCHEMA_ROOT_TAG), schemaDescriptor.getClass(), schemaDescriptor);
				Marshaller marshaller = getJaxbContext().getJaxbContext().createMarshaller();

				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

				// NamespaceMapperImpl namespaceMapper = schemaDef.getAllNamespaces();
				// marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper",
				// namespaceMapper);
				marshaller.marshal(element, output);

			} catch (final JAXBException e) {
				throw new SchemaException("Failed to build XML binding from Agreement descriptor Java classes", e);
			}
		}
	}
}
