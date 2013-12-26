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

import org.ow2.easywsdl.wsdl.api.WSDLException;
import org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfBinding;
import org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfBindingOperation;
import org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfInterfaceType;
import org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfBinding.BindingConstants;
import org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfBinding.StyleConstant;


/**
 * @author Nicolas Salatge - eBM WebSourcing
 */
public abstract class DecoratorBindingImpl<I extends AbsItfInterfaceType, BO extends AbsItfBindingOperation> 
extends Decorator<AbsItfBinding<I, BO>> {

    /**
	 *
	 */
    private static final long serialVersionUID = 1L;

    public DecoratorBindingImpl(final AbsItfBinding<I, BO> binding) throws WSDLException {
        this.internalObject = binding;
    }

    public void addBindingOperation(final BO arg0) {
        this.internalObject.addBindingOperation(arg0);
    }

    public BO getBindingOperation(final String arg0) {
        return this.internalObject.getBindingOperation(arg0);
    }

    public List<BO> getBindingOperations() {
        return this.internalObject.getBindingOperations();
    }

    public String getHttpContentEncodingDefault() {
        return this.internalObject.getHttpContentEncodingDefault();
    }

    public String getHttpDefaultMethod() {
        return this.internalObject.getHttpDefaultMethod();
    }

    public String getHttpQueryParameterSeparatorDefault() {
        return this.internalObject.getHttpQueryParameterSeparatorDefault();
    }

    public I getInterface() {
        return this.internalObject.getInterface();
    }

    public QName getQName() {
        return this.internalObject.getQName();
    }

    public StyleConstant getStyle() {
        return this.internalObject.getStyle();
    }

    public String getTransportProtocol() {
        return this.internalObject.getTransportProtocol();
    }

    public BindingConstants getTypeOfBinding() {
        return this.internalObject.getTypeOfBinding();
    }

    public String getVersion() {
        return this.internalObject.getVersion();
    }

    public boolean isHttpCookies() {
        return this.internalObject.isHttpCookies();
    }

    public BO removeBindingOperation(final String arg0) {
        return this.internalObject.removeBindingOperation(arg0);
    }

    public void setInterface(final I arg0) {
        this.internalObject.setInterface(arg0);
    }

    public void setQName(final QName arg0) {
        this.internalObject.setQName(arg0);
    }

    public void setTransportProtocol(String transportProtocol) {
        this.internalObject.setTransportProtocol(null);
    }

	public BO createBindingOperation() {
		return this.internalObject.createBindingOperation();
	}
}
