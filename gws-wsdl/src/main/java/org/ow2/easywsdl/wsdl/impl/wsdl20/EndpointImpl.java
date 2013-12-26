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
package org.ow2.easywsdl.wsdl.impl.wsdl20;

import java.util.List;

import javax.xml.namespace.QName;

import org.ow2.easywsdl.schema.api.XmlException;
import org.ow2.easywsdl.wsdl.api.Binding;
import org.ow2.easywsdl.wsdl.api.Service;
import org.ow2.easywsdl.wsdl.api.abstractElmt.AbstractEndpointImpl;
import org.ow2.easywsdl.wsdl.api.abstractElmt.AbstractWSDLElementImpl;
import org.ow2.easywsdl.wsdl.org.w3.ns.wsdl.EndpointType;
import org.w3c.dom.Element;

/**
 * @author Nicolas Salatge - eBM WebSourcing
 */
public class EndpointImpl extends AbstractEndpointImpl<EndpointType, Service, Binding> implements
        org.ow2.easywsdl.wsdl.api.Endpoint {

    /**
	 *
	 */
    private static final long serialVersionUID = 1L;



    public EndpointImpl(final EndpointType endpoint, final Service service) {
        super(endpoint,  (AbstractWSDLElementImpl)service);
        this.service = service;

        // get the documentation
        this.documentation = new org.ow2.easywsdl.wsdl.impl.wsdl20.DocumentationImpl(
                this.model.getDocumentation(), this);
    }

    public Binding getBinding() {
        final QName bindingName = this.model.getBinding();
        return (Binding) ((ServiceImpl)this.service).getDescription().getBinding(bindingName);
    }

    public String getName() {
        return this.model.getName();
    }

    public String getAddress() {
        return this.model.getAddress();
    }

    public void setBinding(final Binding binding) {
        this.model.setBinding(binding.getQName());
    }

    public void setName(final String name) {
        this.model.setName(name);
    }

    public void setAddress(final String address) {
        this.model.setAddress(address);
    }

    public String getHttpAuthenticationRealm() {
        return this.model
                .getOtherAttributes()
                .get(
                        new QName(
                                org.ow2.easywsdl.wsdl.api.Binding.BindingConstants.HTTP_BINDING4WSDL20
                                        .value().toString(), "authenticationScheme"));
    }

    public String getHttpAuthenticationScheme() {
        return this.model
                .getOtherAttributes()
                .get(
                        new QName(
                                org.ow2.easywsdl.wsdl.api.Binding.BindingConstants.HTTP_BINDING4WSDL20
                                        .value().toString(), "authenticationRealm"));
    }

    @Override
    public List<Element> getOtherElements() throws XmlException {
        final List<Element> res = super.getOtherElements();

        for (final Object item : this.model.getAny()) {
            if (item instanceof Element) {
                res.add((Element) item);
            }
        }

        return res;
    }


}
