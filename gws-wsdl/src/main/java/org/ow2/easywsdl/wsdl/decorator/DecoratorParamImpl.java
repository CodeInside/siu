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
package org.ow2.easywsdl.wsdl.decorator;

import java.util.List;

import javax.xml.namespace.QName;

import org.ow2.easywsdl.schema.api.Element;
import org.ow2.easywsdl.wsdl.api.Part;
import org.ow2.easywsdl.wsdl.api.WSDLException;
import org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfParam;


/**
 * @author Nicolas Salatge - eBM WebSourcing
 */
public abstract class DecoratorParamImpl<P extends AbsItfParam> extends Decorator<P> {

    /**
	 *
	 */
    private static final long serialVersionUID = 1L;
    
    public DecoratorParamImpl(final AbsItfParam param) throws WSDLException {
        this.internalObject = (P)param;
    }

    public Element getElement() {
        return this.internalObject.getElement();
    }

    public QName getMessageName() {
        return this.internalObject.getMessageName();
    }

    public String getName() {
        return this.internalObject.getName();
    }

    public Part getPart(final String arg0) {
        return this.internalObject.getPart(arg0);
    }

    public List<Part> getParts() {
        return this.internalObject.getParts();
    }

    public void setElement(final Element arg0) throws WSDLException {
        this.internalObject.setElement(arg0);
    }

    public void setMessageName(final QName arg0) {
        this.internalObject.setMessageName(arg0);
    }

    public void setName(final String arg0) throws WSDLException {
        this.internalObject.setName(arg0);
    }
}
