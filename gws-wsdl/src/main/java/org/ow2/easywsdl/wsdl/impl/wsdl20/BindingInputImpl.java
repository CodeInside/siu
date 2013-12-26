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
import java.util.logging.Logger;

import javax.xml.namespace.QName;

import org.ow2.easywsdl.u.NotImplementedException;
import org.ow2.easywsdl.schema.api.XmlException;
import org.ow2.easywsdl.wsdl.api.abstractElmt.AbstractBindingParamImpl;
import org.ow2.easywsdl.wsdl.api.binding.wsdl11.soap.soap11.SOAP11Binding4Wsdl11;
import org.ow2.easywsdl.wsdl.api.binding.wsdl20.soap.SOAPBinding4Wsdl20;
import org.ow2.easywsdl.wsdl.org.w3.ns.wsdl.BindingOperationMessageType;
import org.w3c.dom.Element;

/**
 * @author Nicolas Salatge - eBM WebSourcing
 */
public class BindingInputImpl extends AbstractBindingParamImpl<BindingOperationMessageType>
        implements org.ow2.easywsdl.wsdl.api.BindingInput {

    /**
	 *
	 */
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = Logger.getLogger(BindingInputImpl.class.getName());


    @SuppressWarnings("unchecked")
    public BindingInputImpl(final BindingOperationMessageType param,
            final BindingOperationImpl bindingOperationImpl) {
        super(param, bindingOperationImpl);
        this.bindingOperation = bindingOperationImpl;

        // get the documentation
        this.documentation = new org.ow2.easywsdl.wsdl.impl.wsdl20.DocumentationImpl(
                this.model.getDocumentation(), this);

        // get the binding protocol
        this.bindingProtocol = AbstractBindingParamImpl.extractBindingProtocol((List) this.model
                .getAny(), this);
    }

    public String getName() {
        return this.model.getMessageLabel();
    }

    public void setName(final String name) {
        this.model.setMessageLabel(name);
    }

    public String getHttpContentEncoding() {
        return this.model
                .getOtherAttributes()
                .get(
                        new QName(
                                org.ow2.easywsdl.wsdl.api.Binding.BindingConstants.HTTP_BINDING4WSDL20
                                        .value().toString(), "contentEncoding"));
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

	public void setSOAP11Binding4Wsdl11(SOAP11Binding4Wsdl11 soap11binding) {
		LOG.warning("This binding does not exist in wsdl 2.0");
	}

	public void setSOAP12Binding4Wsdl20(SOAPBinding4Wsdl20 soap12binding) {
		throw new NotImplementedException();
	}

	public SOAP11Binding4Wsdl11 createSOAP11Binding4Wsdl11() {
		LOG.warning("This binding does not exist in wsdl 2.0");
		return null;
	}

	public SOAPBinding4Wsdl20 createSOAP12Binding4Wsdl20() {
		throw new NotImplementedException();
	}
}
