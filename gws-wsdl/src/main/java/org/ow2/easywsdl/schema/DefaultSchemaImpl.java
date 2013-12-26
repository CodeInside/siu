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
package org.ow2.easywsdl.schema;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.namespace.QName;

import org.ow2.easywsdl.schema.api.Documentation;
import org.ow2.easywsdl.schema.api.SchemaException;
import org.ow2.easywsdl.schema.api.Type;
import org.ow2.easywsdl.schema.api.XmlException;
import org.ow2.easywsdl.schema.api.absItf.AbsItfAttribute;
import org.ow2.easywsdl.schema.api.absItf.AbsItfElement;
import org.ow2.easywsdl.schema.api.absItf.AbsItfImport;
import org.ow2.easywsdl.schema.api.absItf.AbsItfInclude;
import org.ow2.easywsdl.schema.api.absItf.AbsItfSchema;
import org.ow2.easywsdl.schema.api.absItf.AbsItfType;
import org.ow2.easywsdl.schema.api.extensions.NamespaceMapperImpl;
import org.ow2.easywsdl.schema.impl.Constants;
import org.ow2.easywsdl.schema.impl.SchemaJAXBContext;
import org.ow2.easywsdl.schema.org.w3._2001.xmlschema.FormChoice;

/**
 * @author Nicolas Salatge - eBM WebSourcing
 */
public final class DefaultSchemaImpl implements DefaultSchema {

	private static final long serialVersionUID = 1L;

	private static final Logger LOG = Logger.getLogger(DefaultSchemaImpl.class.getName());

	private static AbsItfSchema defaultSchema = null;
	
	private final static DefaultSchemaImpl INSTANCE;

	static {
		final URL schemaUrl = SchemaJAXBContext.class.getResource("/" + Constants.XSD_SCHEMA_WITHOUT_DTD);
		try {
			defaultSchema = new org.ow2.easywsdl.schema.impl.SchemaReaderImpl()
					.read(schemaUrl);
		} catch (final XmlException e) {
			e.printStackTrace();
			DefaultSchemaImpl.LOG.warning("Error to read default parent => " + e.getMessage());
		} catch (final URISyntaxException e) {
			e.printStackTrace();
			DefaultSchemaImpl.LOG.warning("Error to read default parent => " + e.getMessage());
		} catch (final IOException e) {
		    e.printStackTrace();
            DefaultSchemaImpl.LOG.warning("I/O error reading " + schemaUrl.toString() + " : " + e.getMessage());
		}
		
		INSTANCE = new DefaultSchemaImpl();
	}

	private DefaultSchemaImpl() {

	}

	public static DefaultSchema getInstance() {
		return DefaultSchemaImpl.INSTANCE;
	}

	public Type getTypeInt() {
		return getType(new QName(Constants.SCHEMA_NAMESPACE, "int"));
	}

	public Type getTypeString() {
		return getType(new QName(Constants.SCHEMA_NAMESPACE, "string"));
	}
	
	public Type getTypeDateTime() {
		return getType(new QName(Constants.SCHEMA_NAMESPACE, "dateTime"));
	}
	
	public Type getTypeBoolean() {
		return getType(new QName(Constants.SCHEMA_NAMESPACE, "boolean"));
	}

	@SuppressWarnings("unchecked")
	public List<Type> getTypes() {
		List<Type> res = null;
		if(DefaultSchemaImpl.defaultSchema != null) {
			res = DefaultSchemaImpl.defaultSchema.getTypes();
		}
		return res;
	}

	public Type getType(QName type) {
		Type res = null;
		for(Type t: this.getTypes()) {
			if((t.getQName().getNamespaceURI().equals(type.getNamespaceURI()))
					&&(t.getQName().getLocalPart().equals(type.getLocalPart()))) {
				res = t;
				break;
			}
		}
		return res;
	}

	public Type getTypeDouble() {
		return getType(new QName(Constants.SCHEMA_NAMESPACE, "double"));
	}

	public Type getTypeDuration() {
		return getType(new QName(Constants.SCHEMA_NAMESPACE, "duration"));
	}

	public Type getTypeFloat() {
		return getType(new QName(Constants.SCHEMA_NAMESPACE, "float"));
	}

	public Type getTypeInteger() {
		return getType(new QName(Constants.SCHEMA_NAMESPACE, "integer"));
	}

	public Type getTypeLong() {
		return getType(new QName(Constants.SCHEMA_NAMESPACE, "long"));
	}

	public Type getTypeShort() {
		return getType(new QName(Constants.SCHEMA_NAMESPACE, "short"));
	}

	public void addAttribute(AbsItfAttribute attr) {
		this.defaultSchema.addAttribute(attr);
	}

	public void addElement(AbsItfElement elmt) {
		this.defaultSchema.addElement(elmt);
	}

	public void addImport(AbsItfImport importDef) {
		this.defaultSchema.addImport(importDef);
	}

	public void addInclude(AbsItfInclude includeDef) throws SchemaException {
		this.defaultSchema.addInclude(includeDef);
	}

	public void addType(AbsItfType absItfType) {
		this.defaultSchema.addType(absItfType);
	}

	public AbsItfAttribute createAttribute() {
		return this.defaultSchema.createAttribute();
	}

	public AbsItfType createComplexType() {
		return this.defaultSchema.createComplexType();
	}

	public AbsItfElement createElement() {
		return this.defaultSchema.createElement();
	}

	public AbsItfImport createImport() throws SchemaException {
		return this.defaultSchema.createImport();
	}

	public AbsItfInclude createInclude() throws SchemaException {
		return this.defaultSchema.createInclude();
	}

	public AbsItfType createSimpleType() {
		return this.defaultSchema.createSimpleType();
	}

	public List<AbsItfElement> findElementsInAllSchema(QName element) {
		return this.defaultSchema.findElementsInAllSchema(element);
	}

	public NamespaceMapperImpl getAllNamespaces() {
		return this.defaultSchema.getAllNamespaces();
	}

	public AbsItfAttribute getAttribute(QName attr) {
		return this.defaultSchema.getAttribute(attr);
	}

	public List<AbsItfAttribute> getAttributes() {
		return this.defaultSchema.getAttributes();
	}

	public List<String> getBlockDefault() {
		return this.defaultSchema.getBlockDefault();
	}

	public URI getDocumentURI() {
		return this.defaultSchema.getDocumentURI();
	}

	public AbsItfElement getElement(QName element) {
		return this.defaultSchema.getElement(element);
	}

	public List<AbsItfElement> getElements() {
		return this.defaultSchema.getElements();
	}

	public List<String> getFinalDefault() {
		return this.defaultSchema.getFinalDefault();
	}

	public List<AbsItfImport> getImports(String namespaceURI) {
		return this.defaultSchema.getImports(namespaceURI);
	}

	public List<AbsItfImport> getImports() {
		return this.defaultSchema.getImports();
	}

	public List<AbsItfInclude> getIncludes(URI locationURI) throws URISyntaxException {
		return this.defaultSchema.getIncludes(locationURI);
	}

	public List<AbsItfInclude> getIncludes() {
		return this.defaultSchema.getIncludes();
	}

	public String getLang() {
		return this.defaultSchema.getLang();
	}

	public String getTargetNamespace() {
		return this.defaultSchema.getTargetNamespace();
	}

	public String getVersion() {
		return this.defaultSchema.getVersion();
	}

	public AbsItfImport removeImport(AbsItfImport importDef) {
		return this.defaultSchema.removeImport(importDef);
	}

	public AbsItfInclude removeInclude(AbsItfInclude includeDef) throws SchemaException {
		return this.defaultSchema.removeInclude(includeDef);
	}

	public void setDocumentURI(URI documentBaseURI) {
		this.defaultSchema.setDocumentURI(documentBaseURI);
	}

	public void setTargetNamespace(String tns) {
		this.defaultSchema.setTargetNamespace(tns);
	}

	public Map<QName, String> getOtherAttributes() throws XmlException {
		return this.defaultSchema.getOtherAttributes();
	}

	public void setDocumentation(Documentation doc) {
		this.defaultSchema.setDocumentation(doc);
	}

	public FormChoice getAttributeFormDefault() {
		return this.defaultSchema.getAttributeFormDefault();
	}

	public FormChoice getElementFormDefault() {
		return this.defaultSchema.getElementFormDefault();
	}

	public void setAttributeFormDefault(FormChoice form) {
		this.defaultSchema.setAttributeFormDefault(form);
	}

	public void setElementFormDefault(FormChoice form) {
		this.defaultSchema.setElementFormDefault(form);
	}

	public Documentation createDocumentation() {
		return this.defaultSchema.createDocumentation();
	}

	public Documentation getDocumentation() {
		return this.defaultSchema.getDocumentation();
	}


}
