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

import javax.xml.namespace.QName;

import org.ow2.easywsdl.u.NotImplementedException;
import org.ow2.easywsdl.schema.api.Type;
import org.ow2.easywsdl.wsdl.api.WSDLElement;
import org.ow2.easywsdl.wsdl.api.abstractElmt.AbstractInterfaceTypeImpl;
import org.ow2.easywsdl.wsdl.api.abstractElmt.AbstractOperationImpl;
import org.ow2.easywsdl.wsdl.api.abstractElmt.AbstractParamImpl;
import org.ow2.easywsdl.wsdl.api.abstractElmt.AbstractPartImpl;
import org.ow2.easywsdl.wsdl.api.abstractElmt.AbstractWSDLElementImpl;
import org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.TPart;

/**
 * @author Nicolas Salatge - eBM WebSourcing
 */
public class PartImpl extends AbstractPartImpl<TPart> implements
        org.ow2.easywsdl.wsdl.api.Part {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;


    /**
     * 
     * @param part
     * @param elmt
     * @param param
     */
    public PartImpl(final TPart part, final WSDLElement parent) {
        super(part, (AbstractWSDLElementImpl) parent);

        this.elementName = this.model.getElement();
        this.typeName = this.model.getType();
    }

    @SuppressWarnings("unchecked")
    public QName getPartQName() {
    	String tns = null;
    	if(parent instanceof AbstractParamImpl) {
    		tns = ((AbstractInterfaceTypeImpl) ((AbstractOperationImpl) ((AbstractParamImpl) this.parent)
                    .getOperation()).getInterface()).getDescription().getTargetNamespace();
    	}
    	if(parent instanceof MessageImpl) {
    		tns = ((MessageImpl)parent).getDescription().getTargetNamespace();
    	}
    	
    	
        return new QName(
                tns,
                this.model.getName());
    }

    public void setPartQName(final QName name) {
        this.model.setName(name.getLocalPart());
    }

    public void setElement(final org.ow2.easywsdl.schema.api.Element element) {
        throw new NotImplementedException();
    }

    public void setType(final Type type) {
        throw new NotImplementedException();
    }

}
