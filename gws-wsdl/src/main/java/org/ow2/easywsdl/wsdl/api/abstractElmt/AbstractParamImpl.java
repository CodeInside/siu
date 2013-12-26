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

import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;

import org.ow2.easywsdl.schema.api.Element;
import org.ow2.easywsdl.schema.api.Schema;
import org.ow2.easywsdl.schema.api.absItf.AbsItfSchema;
import org.ow2.easywsdl.wsdl.api.Types;
import org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfOperation;
import org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfParam;


/**
 * @author Nicolas Salatge - eBM WebSourcing
 */
public abstract class AbstractParamImpl<E> extends AbstractWSDLElementImpl<E> implements
        AbsItfParam {


    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
     * the parent operation
     */
    @SuppressWarnings("unchecked")
    protected AbsItfOperation operation;

    /**
     * the element name
     */
    protected QName elementName;
    
    public AbstractParamImpl(E model, AbstractWSDLElementImpl parent) {
        super(model, parent);
    }

    /**
     * @return the interface
     */
    @SuppressWarnings("unchecked")
    public AbsItfOperation getOperation() {
        return this.operation;
    }

    @SuppressWarnings("unchecked")
    public Element getElement() {
        Element res = null;
        final Types types = (Types) ((AbstractInterfaceTypeImpl) ((AbstractOperationImpl) this.operation)
                .getInterface()).getDescription().getTypes();
        if (this.elementName != null) {
        	List<Schema> schemas = types.getSchemas();
        	Iterator it = schemas.iterator();
        	while(it.hasNext()){
        		AbsItfSchema temp = (AbsItfSchema)it.next();
        		res = (Element) temp.getElement(this.elementName);
        		if(res != null){
        			break;
        		}
        	}
        }
        return res;
    }

}
