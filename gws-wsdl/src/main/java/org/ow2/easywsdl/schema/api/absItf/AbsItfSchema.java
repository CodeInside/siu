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
package org.ow2.easywsdl.schema.api.absItf;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.xml.namespace.QName;

import org.ow2.easywsdl.schema.api.SchemaElement;
import org.ow2.easywsdl.schema.api.SchemaException;
import org.ow2.easywsdl.schema.api.extensions.NamespaceMapperImpl;
import org.ow2.easywsdl.schema.org.w3._2001.xmlschema.FormChoice;

/**
 * @author Nicolas Salatge - eBM WebSourcing
 */
public interface AbsItfSchema<T extends AbsItfType, E extends AbsItfElement, A extends AbsItfAttribute, Incl extends AbsItfInclude, Impt extends AbsItfImport> extends SchemaElement {

	/*
	 * method for types
	 */

	/**
	 * @return
	 */
	List<T> getTypes();

	void addType(T absItfType);

	T getType(QName type);

	T createComplexType();

	T createSimpleType();
	

	/*
	 * Method for elements
	 */
	List<E> getElements();

	void addElement(E elmt);

	E getElement(QName element);

	E createElement();
	
	List<E> findElementsInAllSchema(QName element);

	/*
	 * Method for attributes
	 */
	List<A> getAttributes();

	void addAttribute(A attr);

	A getAttribute(QName attr);

	A createAttribute();

	/*
	 * method for namespace
	 */
	String getTargetNamespace();

	void setTargetNamespace(String tns);

	NamespaceMapperImpl getAllNamespaces();

	/**
	 * Add an import to this parent.
	 * 
	 * @param importDef
	 *            the import to be added
	 */
	void addImport(Impt importDef);

	/**
	 * Create an import to this parent.
	 * 
	 * @param importDef
	 *            the import to be added
	 * @throws SchemaException
	 *             if an imported element can't be retrieved
	 */
	Impt createImport() throws SchemaException;

	/**
	 * Remove an import from this parent.
	 * 
	 * @param importDef
	 *            the import to be removed
	 * @return the removed ImportImpl
	 */
	Impt removeImport(Impt importDef);

	/**
	 * Get the list of imports for the specified namespaceURI.
	 * 
	 * @param namespaceURI
	 *            the namespaceURI associated with the desired imports.
	 * @return a list of the corresponding imports, or null if there weren't any matching imports
	 */
	List<Impt> getImports(String namespaceURI);

	/**
	 * Get a map of lists containing all the imports defined here. The map's keys are the namespaceURIs, and the map's values are lists. There is one list for each namespaceURI for which imports have been defined.
	 */
	List<Impt> getImports();

	/**
	 * Add an include to this parent.
	 * 
	 * @param includeDef
	 *            the include to be added
	 * @throws SchemaException
	 *             if an included element can't be retrieved
	 */
	void addInclude(Incl includeDef) throws SchemaException;

	/**
	 * Create an include to this parent.
	 * 
	 * @param includeDef
	 *            the include to be added
	 * @throws SchemaException
	 *             if an included element can't be retrieved
	 */
	Incl createInclude() throws SchemaException;

	/**
	 * Remove an include from this parent.
	 * 
	 * @param includeDef
	 *            the include to be removed
	 * @return the removed include
	 * @throws SchemaException
	 */
	Incl removeInclude(Incl includeDef) throws SchemaException;

    /**
     * Get the list of includes for the specified locationURI.
     * 
     * @param locationURI
     *            the locationURI associated with the desired includes.
     * @return a list of the corresponding includes, or null if there weren't
     *         any matching includes
     * @throws URISyntaxException
     *             The schemaLocation definition of an include of the schema is
     *             invalid.
     */
	// FIXME: Is it possible to have several include for one locationURI ?
    List<Incl> getIncludes(URI locationURI) throws URISyntaxException;

	/**
	 * Get a map of lists containing all the includes defined here. The map's keys are the namespaceURIs, and the map's values are lists. There is one list for each locationURI for which imports have been defined.
	 */
	List<Incl> getIncludes();

	/**
	 * Set the document base URI of this definition. Can be used to represent the origin of the Definition, and can be exploited when resolving relative URIs (e.g. in &lt;import&gt;s).
	 * 
	 * @param documentBaseURI
	 *            the document base URI of this definition
	 */
	void setDocumentURI(URI documentBaseURI);

	URI getDocumentURI();

	FormChoice getElementFormDefault();
	
	void setElementFormDefault(FormChoice form);

	FormChoice getAttributeFormDefault();
	
	void setAttributeFormDefault(FormChoice form);

	String getLang();

	String getVersion();

	List<String> getBlockDefault();

	List<String> getFinalDefault();

}
