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
package org.ow2.easywsdl.wsdl.api.abstractElmt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.ow2.easywsdl.schema.api.Schema;
import org.ow2.easywsdl.schema.api.absItf.AbsItfImport;
import org.ow2.easywsdl.schema.api.absItf.AbsItfSchema;
import org.ow2.easywsdl.schema.api.abstractElmt.AbstractSchemaImpl;
import org.ow2.easywsdl.schema.impl.Constants;
import org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfDescription;
import org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfTypes;


/**
 * @author Nicolas Salatge - eBM WebSourcing
 */
@SuppressWarnings("unchecked")
public abstract class AbstractTypesImpl<E, S extends Schema, Impt extends AbsItfImport> extends
AbstractWSDLElementImpl<E> implements AbsItfTypes<S, Impt> {



	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * the desc
	 */
	protected AbsItfDescription desc;

	/**
	 * the list of schemas
	 */
	protected List<S> schemas = new ArrayList<S>();

	/**
	 * the list of imported schemas
	 */
	protected List<Impt> importedSchemas = new ArrayList<Impt>();

	public AbstractTypesImpl(E model, AbstractWSDLElementImpl parent) {
		super(model, parent);
	}

	protected void addImportedSchemasInAllList() {
		for (final Impt impt : this.importedSchemas) {
			if (impt.getSchema() != null) {
				this.schemas.add((S) impt.getSchema());
			}
		}
	}

	public List<S> getSchemas() {
		if (this.schemas == null) {
			this.schemas = new ArrayList<S>();
		}
		return this.schemas;
	}

	public List<Impt> getImportedSchemas() {
		if (this.importedSchemas == null) {
			this.importedSchemas = new ArrayList<Impt>();
		}
		return this.importedSchemas;
	}

	protected void setAllNamespacesInAllSchemas() {
		for (final Schema schema : this.schemas) {
			final Map<String, String> ns = ((org.ow2.easywsdl.schema.api.extensions.NamespaceMapperImpl) this.desc
					.getNamespaces()).getNamespaces();
			for (final Entry<String, String> entry : ns.entrySet()) {
				((org.ow2.easywsdl.schema.api.extensions.NamespaceMapperImpl) schema
						.getAllNamespaces()).addNamespace(entry.getKey(), entry.getValue());
			}
		}
	}

	@Override
	public String toString() {
		return this.getClass().getCanonicalName();
	}
	
	public void addSchema(S schema) {
		this.schemas.add(schema);
	}
	
	protected void reloadAllListInAllSchema() {
		List<AbsItfSchema> alls = new ArrayList<AbsItfSchema>();
		for(AbsItfSchema s: this.schemas) {
			if(!alls.contains(s)) {
				alls.add(s);
			}
			for(AbsItfImport impt: (List<AbsItfImport>) s.getImports()) {
				if(impt.getSchema() != null) {
					if(!alls.contains(impt.getSchema())) {
						alls.add(impt.getSchema());
					}
				}
			}
		}	
		
		
		for(AbsItfSchema s: alls) {
		    ((AbstractSchemaImpl)s).addImportElementsInAllList();
			((AbstractSchemaImpl)s).addIncludeElementsInAllList();
		}
	}
	
	
	protected void setSchemaInAllImport() {
		boolean found = false;
		for(AbsItfSchema s: this.schemas) {
			for(AbsItfImport impt: (List<AbsItfImport>) s.getImports()) {
				if(impt.getSchema() == null) {
					impt.setSchema(findSchema(impt.getNamespaceURI()));
					found = true;
				}
			}
		}
		
		if(found) {
			this.reloadAllListInAllSchema();
		}
	}

	protected AbsItfSchema findSchema(String namespaceURI) {
		AbsItfSchema res = null;
		if(Constants.SCHEMA_NAMESPACE.equals(namespaceURI)){
				res = (AbsItfSchema)org.ow2.easywsdl.schema.SchemaFactory.getDefaultSchema();
		} else {
			for(AbsItfSchema s: this.schemas) {
				if(s != null && s.getTargetNamespace() != null && s.getTargetNamespace().equals(namespaceURI)) {
					res = s;
					break;
				}
			}
		}
		return res;
	}
}
