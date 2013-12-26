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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.ow2.easywsdl.u.NotImplementedException;
import org.ow2.easywsdl.schema.SchemaFactory;
import org.ow2.easywsdl.schema.api.Import;
import org.ow2.easywsdl.schema.api.Schema;
import org.ow2.easywsdl.schema.api.SchemaException;
import org.ow2.easywsdl.schema.api.SchemaReader;
import org.ow2.easywsdl.schema.api.SchemaReader.FeatureConstants;
import org.ow2.easywsdl.schema.api.absItf.AbsItfSchema;
import org.ow2.easywsdl.schema.api.abstractElmt.AbstractSchemaElementImpl;
import org.ow2.easywsdl.schema.api.abstractElmt.AbstractSchemaImpl;
import org.ow2.easywsdl.schema.api.abstractElmt.AbstractSchemaReader;
import org.ow2.easywsdl.schema.impl.SchemaReaderImpl;
import org.ow2.easywsdl.wsdl.api.Types;
import org.ow2.easywsdl.wsdl.api.WSDLElement;
import org.ow2.easywsdl.wsdl.api.WSDLException;
import org.ow2.easywsdl.wsdl.api.abstractElmt.AbstractDescriptionImpl;
import org.ow2.easywsdl.wsdl.api.abstractElmt.AbstractTypesImpl;
import org.ow2.easywsdl.wsdl.api.abstractElmt.AbstractWSDLElementImpl;
import org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.TDefinitions;
import org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.TTypes;
import org.ow2.easywsdl.wsdl.util.Util;
import org.w3c.dom.Element;

/**
 * @author Nicolas Salatge - eBM WebSourcing
 */
public class TypesImpl extends AbstractTypesImpl<TTypes, Schema, Import> implements Types {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Schema soap11encTypesSchema = null;

	private static Schema soap12encTypesSchema = null;

	static {
		// load default soap type
		try {
			SchemaReader schemaReader = SchemaFactory.newInstance().newSchemaReader();
			soap11encTypesSchema = schemaReader.read(DescriptionImpl.class.getResource("/schema/wsdl/wsdl11/extensions/soapenc11.xsd"));
			soap12encTypesSchema = schemaReader.read(DescriptionImpl.class.getResource("/schema/wsdl/wsdl11/extensions/soapenc12.xsd"));
		} catch (SchemaException e) {
			throw new RuntimeException("SOAP 11 or SOAP 12 schema is missing in resources", e);
		} catch (URISyntaxException e) {
			throw new RuntimeException("SOAP 11 or SOAP 12 schema is missing in resources", e);
		} catch (final IOException e) {
			throw new RuntimeException("SOAP 11 or SOAP 12 schema is missing in resources", e);
		}

	}

	public TypesImpl(final TTypes types, final DescriptionImpl desc, final Map<URI, AbsItfSchema> imports, WSDLReaderImpl reader) throws WSDLException {
		super(types, desc);
		this.desc = desc;

		// get the documentation
		this.documentation = new org.ow2.easywsdl.wsdl.impl.wsdl11.DocumentationImpl(this.model.getDocumentation(), this);

		final List<org.ow2.easywsdl.schema.org.w3._2001.xmlschema.Schema> scs = new ArrayList<org.ow2.easywsdl.schema.org.w3._2001.xmlschema.Schema>();
		for (final Object item : this.model.getAny()) {

			if (item instanceof org.ow2.easywsdl.schema.org.w3._2001.xmlschema.Schema) {
				scs.add((org.ow2.easywsdl.schema.org.w3._2001.xmlschema.Schema) item);
			}

			if (item instanceof org.ow2.easywsdl.schema.org.w3._2001.xmlschema.Import) {
				org.ow2.easywsdl.schema.api.Import impt = null;
				try {
					// TODO : use the documentURI
					impt = new org.ow2.easywsdl.schema.impl.ImportImpl((org.ow2.easywsdl.schema.org.w3._2001.xmlschema.Import) item, Util.convertWSDLFeatures2SchemaFeature(this.desc), imports, this.desc.getDocumentBaseURI(), (AbstractSchemaReader) reader.getSchemaReader());
				} catch (SchemaException e) {
					throw new WSDLException(e);
				} catch (final URISyntaxException e) {
					// TODO: Perhaps can we log a warning about this exception without throwing it ? 
					throw new WSDLException(e);
				}
				this.importedSchemas.add(impt);
			}
		}

		this.addImportedSchemasInAllList();

		final Map<FeatureConstants, Object> features = Util.convertWSDLFeatures2SchemaFeature(this.desc);

		try {

			for (final org.ow2.easywsdl.schema.org.w3._2001.xmlschema.Schema schema : scs) {

				try {
					Schema schemaImpt = new org.ow2.easywsdl.schema.impl.SchemaImpl(this.desc.getDocumentBaseURI(), schema, this.desc.getNamespaces(), ((AbstractDescriptionImpl) this.desc).getSchemaLocator(), features, imports, (SchemaReaderImpl) reader.getSchemaReader());
	

					this.schemas.add(schemaImpt);
				} catch (final URISyntaxException e) {
					// TODO: Perhaps can we log a warning about this exception without throwing it ? 
					throw new WSDLException(e);
				}

			}
		} catch (final SchemaException e) {
			throw new WSDLException(e);
		}

		this.setAllNamespacesInAllSchemas();

		this.setSchemaInAllImport();
	}



	public Schema createSchema() {
		Schema schema = null;
		try {
			schema = SchemaFactory.newInstance().newSchema();
			((AbstractSchemaImpl) schema).setParent(this);
		} catch (SchemaException e) {
			// Do nothing
			e.printStackTrace();
		}
		return schema;
	}

	@Override
	public void addSchema(Schema schema) {
		this.model.getAny().add(((AbstractSchemaElementImpl) schema).getModel());
		super.addSchema(schema);
	}

	public Schema removeSchema() {
		throw new NotImplementedException();
	}

	@Override
	public List<Schema> getSchemas() {
		return super.getSchemas();
	}

	public static TTypes replaceDOMElementByTTypes(final WSDLElement parent, final Element childToReplace, WSDLReaderImpl reader) throws WSDLException {
		TTypes res = null;
		try {
			if ((childToReplace != null) && ((childToReplace.getLocalName().equals("types")))) {
				JAXBElement<TTypes> jaxbElement;

				Unmarshaller unmarshaller = WSDLJAXBContext.getInstance().getJaxbContext().createUnmarshaller();

				jaxbElement = unmarshaller.unmarshal(childToReplace, TTypes.class);

				// change element by jaxb element
				((TDefinitions) ((AbstractWSDLElementImpl) parent).getModel()).getAny().remove(childToReplace);
				((TDefinitions) ((AbstractWSDLElementImpl) parent).getModel()).getAnyTopLevelOptionalElement().add(jaxbElement.getValue());

				// get element
				res = jaxbElement.getValue();
			}
		} catch (JAXBException e) {
			throw new WSDLException(e);
		}
		return res;
	}

	public static Schema getSoap11encTypesSchema() {
		return soap11encTypesSchema;
	}

	public static Schema getSoap12encTypesSchema() {
		return soap12encTypesSchema;
	}
}
