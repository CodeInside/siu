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
package org.ow2.easywsdl.schema.decorator;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.xml.namespace.QName;

import org.ow2.easywsdl.schema.api.SchemaException;
import org.ow2.easywsdl.schema.api.absItf.AbsItfAttribute;
import org.ow2.easywsdl.schema.api.absItf.AbsItfElement;
import org.ow2.easywsdl.schema.api.absItf.AbsItfImport;
import org.ow2.easywsdl.schema.api.absItf.AbsItfInclude;
import org.ow2.easywsdl.schema.api.absItf.AbsItfSchema;
import org.ow2.easywsdl.schema.api.absItf.AbsItfType;
import org.ow2.easywsdl.schema.api.extensions.NamespaceMapperImpl;
import org.ow2.easywsdl.schema.org.w3._2001.xmlschema.FormChoice;

/**
 * @author Nicolas Salatge - eBM WebSourcing
 */
public abstract class DecoratorSchemaImpl<T extends AbsItfType, E extends AbsItfElement, A extends AbsItfAttribute, Incl extends AbsItfInclude, Impt extends AbsItfImport>
extends Decorator<AbsItfSchema<T, E, A, Incl, Impt>> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DecoratorSchemaImpl(final AbsItfSchema<T, E, A, Incl, Impt> schema) {
		this.internalObject = schema;
	}

	public void addAttribute(final A attr) {
		this.internalObject.addAttribute(attr);
	}

	public void addElement(final E elmt) {
		this.internalObject.addElement(elmt);
	}

	public void addImport(final Impt importDef) {
		this.internalObject.addImport(importDef);
	}

	public void addInclude(final Incl includeDef) throws SchemaException {
		this.internalObject.addInclude(includeDef);
	}

	public void addType(final T absItfType) {
		this.internalObject.addType(absItfType);
	}

	public NamespaceMapperImpl getAllNamespaces() {
		return this.internalObject.getAllNamespaces();
	}

	public A getAttribute(final QName attr) {
		return this.internalObject.getAttribute(attr);
	}

	public List<A> getAttributes() {
		return this.internalObject.getAttributes();
	}

	public E getElement(final QName element) {
		return this.internalObject.getElement(element);
	}

	public List<E> getElements() {
		return this.internalObject.getElements();
	}

	public List<Impt> getImports(final String namespaceURI) {
		return this.internalObject.getImports();
	}

	public List<Impt> getImports() {
		return this.internalObject.getImports();
	}

	public List<Incl> getIncludes(final URI locationURI) throws URISyntaxException {
		return this.internalObject.getIncludes(locationURI);
	}

	public List<Incl> getIncludes() {
		return this.internalObject.getIncludes();
	}

	public String getTargetNamespace() {
		return this.internalObject.getTargetNamespace();
	}

	public T getType(final QName type) {
		return this.internalObject.getType(type);
	}

	public List<T> getTypes() {
		return this.internalObject.getTypes();
	}

	public Impt removeImport(final Impt importDef) {
		return this.internalObject.removeImport(importDef);
	}

	public Incl removeInclude(final Incl includeDef) throws SchemaException {
		return this.internalObject.removeInclude(includeDef);
	}

	public void setDocumentURI(final URI documentBaseURI) {
		this.internalObject.setDocumentURI(documentBaseURI);
	}

	public A createAttribute() {
		return this.internalObject.createAttribute();
	}

	public T createComplexType() {
		return this.internalObject.createComplexType();
	}

	public E createElement() {
		return this.internalObject.createElement();
	}

	public Impt createImport() throws SchemaException {
		return this.internalObject.createImport();
	}

	public Incl createInclude() throws SchemaException {
		return this.internalObject.createInclude();
	}

	public T createSimpleType() {
		return this.internalObject.createSimpleType();
	}

	public void setTargetNamespace(String tns) {
		this.internalObject.setTargetNamespace(tns);
	}

	public FormChoice getAttributeFormDefault() {
		return this.internalObject.getAttributeFormDefault();
	}

	public List<String> getBlockDefault() {
		return this.internalObject.getBlockDefault();
	}

	public FormChoice getElementFormDefault() {
		return this.internalObject.getElementFormDefault();
	}

	public List<String> getFinalDefault() {
		return this.internalObject.getFinalDefault();
	}

	public String getLang() {
		return this.internalObject.getLang();
	}

	public String getVersion() {
		return this.internalObject.getVersion();
	}

	public URI getDocumentURI() {
		return this.internalObject.getDocumentURI();
	}
	
	public  List<E> findElementsInAllSchema(QName element) {
		return this.internalObject.findElementsInAllSchema(element);
	}
	
	public void setAttributeFormDefault(FormChoice form) {
		this.internalObject.setAttributeFormDefault(form);
	}

	public void setElementFormDefault(FormChoice form) {
		this.internalObject.setElementFormDefault(form);
	}
}
