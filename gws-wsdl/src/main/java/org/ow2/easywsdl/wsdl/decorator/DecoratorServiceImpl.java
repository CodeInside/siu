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
import org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfEndpoint;
import org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfInterfaceType;
import org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfService;


/**
 * @author Nicolas Salatge - eBM WebSourcing
 */
@SuppressWarnings("unchecked")
public abstract class DecoratorServiceImpl<I extends AbsItfInterfaceType, E extends AbsItfEndpoint>  extends Decorator<AbsItfService<I, E>> {

    /**
	 *
	 */
    private static final long serialVersionUID = 1L;

    public DecoratorServiceImpl(final AbsItfService<I, E> service) throws WSDLException {
        this.internalObject = service;
    }

    public void addEndpoint(final E arg0) {
        this.internalObject.addEndpoint(arg0);
    }

    public E getEndpoint(final String arg0) {
        return this.internalObject.getEndpoint(arg0);
    }

    public List<E> getEndpoints() {
        return this.internalObject.getEndpoints();
    }

    public I getInterface() throws WSDLException {
        return this.internalObject.getInterface();
    }

    public QName getQName() {
        return this.internalObject.getQName();
    }

    public E removeEndpoint(final String arg0) {
        return this.internalObject.removeEndpoint(arg0);
    }

    public void setQName(final QName arg0) {
        this.internalObject.setQName(arg0);
    }

	public E createEndpoint() {
		return this.internalObject.createEndpoint();
	}

	public void setInterface(I itf) {
		this.internalObject.setInterface(itf);
	}

}
