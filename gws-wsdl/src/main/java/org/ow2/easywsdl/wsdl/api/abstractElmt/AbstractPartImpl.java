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
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.namespace.QName;

import org.ow2.easywsdl.schema.SchemaFactory;
import org.ow2.easywsdl.schema.api.Element;
import org.ow2.easywsdl.schema.api.Schema;
import org.ow2.easywsdl.schema.api.Type;
import org.ow2.easywsdl.schema.api.absItf.AbsItfSchema;
import org.ow2.easywsdl.wsdl.api.Part;
import org.ow2.easywsdl.wsdl.api.Types;
import org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfDescription;
import org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfImport;
import org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfParam;
import org.ow2.easywsdl.wsdl.impl.wsdl11.MessageImpl;
import org.ow2.easywsdl.wsdl.impl.wsdl11.TypesImpl;


/**
 * @author Nicolas Salatge - eBM WebSourcing
 */
public abstract class AbstractPartImpl<E> extends AbstractWSDLElementImpl<E> implements Part {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOG = Logger.getLogger(AbstractPartImpl.class.getName());


	/**
	 * the element name
	 */
	protected QName elementName;

	/**
	 * the type name
	 */
	protected QName typeName;

	public AbstractPartImpl(E model, AbstractWSDLElementImpl parent) {
		super(model, parent);
	}

	/**
	 * @return the interface
	 */
	public AbsItfParam getParam() {
		AbsItfParam param = null;
		if(parent instanceof AbstractParamImpl) {
			param = (AbsItfParam) parent;
		}
		return param;
	}

	@SuppressWarnings("unchecked")
	public Element getElement() {
		Element res = null;
		Element item = null;

		AbsItfDescription desc = null;
		if(parent instanceof AbstractParamImpl) {
			desc = ((AbstractInterfaceTypeImpl) ((AbstractOperationImpl) ((AbstractParamImpl) this.parent)
					.getOperation()).getInterface()).getDescription();
		}
		if(parent instanceof MessageImpl) {
			desc = ((MessageImpl)parent).getDescription();
		}

		//final Types types = (Types) desc.getTypes();
		if (this.elementName != null) {
			List<? extends org.ow2.easywsdl.schema.api.Element> elmts = this.findElementsInAllSchema(desc, this.elementName);
			if(elmts.size() == 1) {
				res = elmts.get(0);
			}
			if(elmts.size() > 1) {
				LOG.warning("several same elements exists: optimistic result => take the first element");
				res = elmts.get(0);
			}
		}
		return res;
	}

	private List<? extends org.ow2.easywsdl.schema.api.Element> findElementsInAllSchema(
			AbsItfDescription desc, QName element) {
		List<org.ow2.easywsdl.schema.api.Element> res = new ArrayList<org.ow2.easywsdl.schema.api.Element>();

		if(desc != null && element != null) {
			for(AbsItfSchema schema: (List<AbsItfSchema>)desc.getTypes().getSchemas()) {
				res.addAll((Collection<? extends org.ow2.easywsdl.schema.api.Element>) schema.findElementsInAllSchema(element));
				for(org.ow2.easywsdl.schema.api.absItf.AbsItfImport imptSchema: (List<org.ow2.easywsdl.schema.api.absItf.AbsItfImport>)schema.getImports()) {
					if(imptSchema.getSchema() != null) {
						res.addAll((Collection<? extends org.ow2.easywsdl.schema.api.Element>) imptSchema.getSchema().findElementsInAllSchema(element));
					} else {
						LOG.severe("No schema linked to the import with namespace: " + imptSchema.getNamespaceURI());
					}
				}
				for(org.ow2.easywsdl.schema.api.absItf.AbsItfInclude inclSchema: (List<org.ow2.easywsdl.schema.api.absItf.AbsItfInclude>)schema.getIncludes()) {
					if(inclSchema.getSchema() != null) {
						res.addAll((Collection<? extends org.ow2.easywsdl.schema.api.Element>) inclSchema.getSchema().findElementsInAllSchema(element));
					}
				}
			}

			for(AbsItfSchema schema: (List<AbsItfSchema>)desc.getTypes().getImportedSchemas()) {
				if(schema != null) {
					res.addAll((Collection<? extends org.ow2.easywsdl.schema.api.Element>) schema.findElementsInAllSchema(element));
				}
			}
			for(AbsItfImport impt: (List<AbsItfImport>)desc.getImports()) {
				if(impt != null) {
					res.addAll((Collection<? extends org.ow2.easywsdl.schema.api.Element>) this.findElementsInAllSchema((AbsItfDescription) impt.getDescription(), element));
				}
			}
		}
		return res;
	}

	@SuppressWarnings("unchecked")
	public Type getType() {
		Type res = null;
		Type item = null;
		AbsItfDescription desc = null;
		if(parent instanceof AbstractParamImpl) {
			desc = ((AbstractInterfaceTypeImpl) ((AbstractOperationImpl) ((AbstractParamImpl) this.parent)
					.getOperation()).getInterface()).getDescription();
		}
		if(parent instanceof MessageImpl) {
			desc = ((MessageImpl)parent).getDescription();
		}
		final Types types = (Types) desc.getTypes();
		if (this.typeName != null) {
			if ((types != null) && (types.getSchemas() != null)) {
				for (final Schema schema : types.getSchemas()) {
					item = schema.getType(this.typeName);
					if (item != null) {
						res = item;
						break;
					}
				}
			} 

			if(res == null) {
				item = (Type) SchemaFactory.getDefaultSchema().getType(this.typeName);
				if (item != null) {
					res = item;
				}
			}

			if(res == null) {
				item = (Type) TypesImpl.getSoap11encTypesSchema().getType(this.typeName);
				if (item != null) {
					res = item;
				}
			}

			if(res == null) {
				item = (Type) TypesImpl.getSoap12encTypesSchema().getType(this.typeName);
				if (item != null) {
					res = item;
				}
			}
		}
		return res;
	}
}
