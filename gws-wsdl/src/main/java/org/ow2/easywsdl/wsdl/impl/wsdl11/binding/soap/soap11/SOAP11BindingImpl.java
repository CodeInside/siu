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
package org.ow2.easywsdl.wsdl.impl.wsdl11.binding.soap.soap11;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.ow2.easywsdl.wsdl.api.WSDLException;
import org.ow2.easywsdl.wsdl.api.abstractElmt.AbstractWSDLElementImpl;
import org.ow2.easywsdl.wsdl.api.binding.wsdl11.soap.soap11.SOAP11Body;
import org.ow2.easywsdl.wsdl.api.binding.wsdl11.soap.soap11.SOAP11Fault;
import org.ow2.easywsdl.wsdl.api.binding.wsdl11.soap.soap11.SOAP11Header;
import org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.TBindingOperationFault;
import org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.TBindingOperationMessage;
import org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.soap.TBody;
import org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.soap.TFault;
import org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.soap.THeader;

/**
 * @author Nicolas Salatge - eBM WebSourcing
 */
public class SOAP11BindingImpl implements
        org.ow2.easywsdl.wsdl.api.binding.wsdl11.soap.soap11.SOAP11Binding4Wsdl11 {

    SOAP11Body soap11body = null;

    List<SOAP11Header> soap11headers = new ArrayList<SOAP11Header>();

    SOAP11Fault soap11fault = null;

    AbstractWSDLElementImpl parent = null;

    public SOAP11BindingImpl(final List<THeader> headers, final TBody body, final TFault fault, AbstractWSDLElementImpl parent) {
        this.parent = parent;
    	if (headers != null) {
            for (final THeader h : headers) {
                this.soap11headers
                        .add(new org.ow2.easywsdl.wsdl.impl.wsdl11.binding.soap.soap11.SOAP11HeaderImpl(
                                h));
            }
        }
        if (body != null) {
            this.soap11body = new org.ow2.easywsdl.wsdl.impl.wsdl11.binding.soap.soap11.SOAP11BodyImpl(
                    body, parent);
        }
        if (fault != null) {
            this.soap11fault = new org.ow2.easywsdl.wsdl.impl.wsdl11.binding.soap.soap11.SOAP11FaultImpl(
                    fault);
        }
    }

    public List<SOAP11Header> getHeaders() {
        return this.soap11headers;
    }

    public SOAP11Body getBody() {
        return this.soap11body;
    }

    public SOAP11Fault getFault() {
        return this.soap11fault;
    }

	public void setBody(SOAP11Body body) throws WSDLException {
		this.soap11body = body;

		List<Object> items = null;
		if(parent.getModel() instanceof TBindingOperationMessage) {
			items = (List) ((TBindingOperationMessage)parent.getModel()).getAny();
		} else if(parent.getModel() instanceof TBindingOperationFault) {
			items = (List) ((TBindingOperationFault)parent.getModel()).getAny();
		} else {
			throw new WSDLException("Parent unknown");
		}

		boolean find = false;
		Object value = null;
        for (final Object item : items) {
            if (item instanceof JAXBElement) {
                value = ((JAXBElement) item).getValue();
            } else {
                value = item;
            }
            if (value instanceof org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.soap.TBody) {
            	find = true;
            	break;
            }
        }

        if(!find) {
        	org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.soap.ObjectFactory factory = new org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.soap.ObjectFactory();
        	Object item = factory.createBody(((SOAP11BodyImpl)body).getModel());
        	if(parent.getModel() instanceof TBindingOperationMessage) {
    			((TBindingOperationMessage)parent.getModel()).getAny().add(item);
    		} else if(parent.getModel() instanceof TBindingOperationFault) {
    			((TBindingOperationFault)parent.getModel()).getAny().add(item);
    		}
        }
	}

	public SOAP11Body createBody() {
		return new SOAP11BodyImpl(new TBody(), parent);
	}

	public SOAP11Fault createFault() {
		return new SOAP11FaultImpl(new TFault());
	}

	public void setFault(SOAP11Fault fault) throws WSDLException {
		this.soap11fault = fault;

		List<Object> items = null;
		if(parent.getModel() instanceof TBindingOperationMessage) {
			items = (List) ((TBindingOperationMessage)parent.getModel()).getAny();
		} else if(parent.getModel() instanceof TBindingOperationFault) {
			items = (List) ((TBindingOperationFault)parent.getModel()).getAny();
		} else {
			throw new WSDLException("Parent unknown");
		}

		boolean find = false;
		Object value = null;
        for (final Object item : items) {
            if (item instanceof JAXBElement) {
                value = ((JAXBElement) item).getValue();
            } else {
                value = item;
            }
            if (value instanceof org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.soap.TFault) {
            	find = true;
            	break;
            }
        }

        if(!find) {
        	org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.soap.ObjectFactory factory = new org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.soap.ObjectFactory();
        	Object item = factory.createFault(((SOAP11FaultImpl)fault).getModel());
        	if(parent.getModel() instanceof TBindingOperationMessage) {
    			((TBindingOperationMessage)parent.getModel()).getAny().add(item);
    		} else if(parent.getModel() instanceof TBindingOperationFault) {
    			((TBindingOperationFault)parent.getModel()).getAny().add(item);
    		}
        }
	}
}
