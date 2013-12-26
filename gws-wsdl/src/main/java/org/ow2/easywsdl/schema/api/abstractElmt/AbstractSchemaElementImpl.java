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
package org.ow2.easywsdl.schema.api.abstractElmt;

import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import org.ow2.easywsdl.schema.api.Documentation;
import org.ow2.easywsdl.schema.api.SchemaElement;
import org.ow2.easywsdl.schema.api.XmlException;
import org.ow2.easywsdl.schema.api.absItf.AbsItfSchema;
import org.ow2.easywsdl.schema.org.w3._2001.xmlschema.OpenAttrs;

/**
 * @author Nicolas Salatge - eBM WebSourcing
 */
public abstract class AbstractSchemaElementImpl<E> implements SchemaElement {
    /**
	 *
	 */
    private static final long serialVersionUID = 1L;

    /**
     * the model
     */
    protected E model;

    protected AbstractSchemaElementImpl parent;

	protected Documentation documentation;


    public AbstractSchemaElementImpl() {
    	this.model = null;
        this.parent = null;
    }

    public AbstractSchemaElementImpl(E model, AbstractSchemaElementImpl parent) {
        this.model = model;
        this.parent = parent;
    }

    /**
     * Set the documentation for this document.
     *
     * @param docEl
     *            the documentation element
     */
    public void setDocumentation(final Documentation doc) {
        this.documentation = doc;

        /*
         * if(this.model instanceof org.w3._2001.xmlschema.Documentation) {
         * ((TDocumented)this.model).setDocumentation((TDocumentation)
         * ((com.ebmwebsourcing
         * .commons.wsdl.impl.wsdl11.Documentation)this.documentation
         * ).getModel()); } else if(this.model instanceof List) {
         * ((List<DocumentedType
         * >)this.model).add((org.w3._2001.xmlschema.Documentation)
         * ((com.ebmwebsourcing
         * .commons.wsdl.impl.Documentation)this.documentation).getModel()); }
         */
    }

    /**
     * Get the documentation.
     *
     * @return the documentation element
     */
    public Documentation getDocumentation() {
        return this.documentation;
    }

    /**
     * Create the documentation element.
     *
     * @return the documentation element
     */
    public Documentation createDocumentation() {
        final Documentation doc = null;
        /*
         * if(this.getClass().getPackage().getName().equals(Constants.WSDL11_PACKAGE
         * )) { doc = new
         * org.ow2.easywsdl.schema.test.impl.wsdl11.Documentation(); } else
         * if
         * (this.getClass().getPackage().getName().equals(Constants.WSDL20_PACKAGE
         * )) { doc = new
         * org.ow2.easywsdl.schema.test.impl.wsdl20.Documentation(); }
         */
        return doc;
    }

    /**
     * Get the map containing all the attributes defined on this element. The
     * keys are the qnames of the attributes.
     *
     * @return a map containing all the attributes defined on this element
     * @throws WSDLException
     *
     */
    public Map<QName, String> getOtherAttributes() throws XmlException {
        Map<QName, String> res = new HashMap<QName, String>();

        if (this.model instanceof OpenAttrs) {
            res = ((OpenAttrs) this.model).getOtherAttributes();
        }
        return res;
    }

    public E getModel() {
        return this.model;
    }

    public AbstractSchemaElementImpl getParent() {
    	return this.parent;
    }

    public AbstractSchemaElementImpl getTopParent() {
    	AbstractSchemaElementImpl top = this;
    	AbstractSchemaElementImpl current = this.parent;
    	while(current != null) {
    		top = current;
    		current = current.getParent();
    	}
    	return top;
    }
    


    public AbsItfSchema getSchema() {
    	AbsItfSchema schema = null;
    	AbstractSchemaElementImpl top = this;
    	AbstractSchemaElementImpl current = this.parent;
    	if(top instanceof AbsItfSchema) {
    		schema = (AbsItfSchema)top;
    	}
    	
    	while(current != null) {
    		top = current;
        	if(top instanceof AbsItfSchema) {
        		schema = (AbsItfSchema)top;
        	}
    		current = current.getParent();
    	}
    	return schema;
    }
    
    public void setParent(AbstractSchemaElementImpl parent) {
		this.parent = parent;
	}
    
    public boolean equals(Object o){
    	if(o instanceof AbstractSchemaElementImpl){
    		return this.model.equals(((AbstractSchemaElementImpl) o).getModel());
    	} else {
    		return false;
    	}
    }
    
    public int hashCode(){
    	return this.model.hashCode();
    }
}
