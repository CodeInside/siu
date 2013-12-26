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

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.ow2.easywsdl.wsdl.api.Binding;
import org.ow2.easywsdl.wsdl.api.Service;
import org.ow2.easywsdl.wsdl.api.abstractElmt.AbstractEndpointImpl;
import org.ow2.easywsdl.wsdl.api.abstractElmt.AbstractServiceImpl;
import org.ow2.easywsdl.wsdl.api.abstractElmt.AbstractWSDLElementImpl;
import org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.TPort;
import org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.soap.ObjectFactory;

/**
 * @author Nicolas Salatge - eBM WebSourcing
 */
public class EndpointImpl extends AbstractEndpointImpl<TPort, Service, Binding> implements
        org.ow2.easywsdl.wsdl.api.Endpoint {

    /**
	 *
	 */
    private static final long serialVersionUID = 1L;



    private ObjectFactory soapFactory = new ObjectFactory();

    public EndpointImpl(final TPort endpoint, final Service service) {
        super(endpoint, (AbstractWSDLElementImpl)service);
        this.service = service;

        // get the documentation
        this.documentation = new org.ow2.easywsdl.wsdl.impl.wsdl11.DocumentationImpl(
                this.model.getDocumentation(), this);

    }

    @SuppressWarnings("unchecked")
    public Binding getBinding() {
        final QName bindingName = this.model.getBinding();
        return (Binding) ((AbstractServiceImpl) this.service).getDescription().getBinding(
                bindingName);
    }

    public String getName() {
        return this.model.getName();
    }

    @SuppressWarnings("unchecked")
    public String getAddress() {
        String location = null;
        for (final Object element : this.model.getAny()) {
            if (((JAXBElement) element).getValue() instanceof org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.soap.TAddress) {
                location = ((org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.soap.TAddress) ((JAXBElement) element)
                        .getValue()).getLocation();
                break;
            }
            if (((JAXBElement) element).getValue() instanceof org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.soap12.TAddress) {
                location = ((org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.soap12.TAddress) ((JAXBElement) element)
                        .getValue()).getLocation();
                break;
            }
            if (((JAXBElement) element).getValue() instanceof org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.http.AddressType) {
                location = ((org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.http.AddressType) ((JAXBElement) element)
                        .getValue()).getLocation();
                break;
            }
        }
        return location;
    }

    public void setBinding(final Binding binding) {
        this.model.setBinding(binding.getQName());
    }

    public void setName(final String name) {
        this.model.setName(name);
    }

    @SuppressWarnings("unchecked")
    public void setAddress(final String address) {
    	boolean find = false;
        for (final Object element : this.model.getAny()) {
            if (((JAXBElement) element).getValue() instanceof org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.soap.TAddress) {
                ((org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.soap.TAddress) ((JAXBElement) element)
                        .getValue()).setLocation(address);
                find = true;
                break;
            }
            if (((JAXBElement) element).getValue() instanceof org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.soap12.TAddress) {
                ((org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.soap12.TAddress) ((JAXBElement) element)
                        .getValue()).setLocation(address);
                find = true;
                break;
            }
            if (((JAXBElement) element).getValue() instanceof org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.http.AddressType) {
                ((org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.http.AddressType) ((JAXBElement) element)
                        .getValue()).setLocation(address);
                find = true;
                break;
            }
        }

        if(!find) {
        	// create default address
        	org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.soap.TAddress soapAddress = new org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.soap.TAddress();
        	soapAddress.setLocation(address);
        	this.model.getAny().add(soapFactory.createAddress(soapAddress));
        }
    }

    public String getHttpAuthenticationRealm() {
        return null;
    }

    public String getHttpAuthenticationScheme() {
        return null;
    }

}
