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
package org.ow2.easywsdl.wsdl.impl.wsdl11.binding.soap.soap12;

import java.util.ArrayList;
import java.util.List;

import org.ow2.easywsdl.wsdl.api.binding.wsdl11.soap.soap12.SOAP12Body;
import org.ow2.easywsdl.wsdl.api.binding.wsdl11.soap.soap12.SOAP12Fault;
import org.ow2.easywsdl.wsdl.api.binding.wsdl11.soap.soap12.SOAP12Header;
import org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.soap12.TBody;
import org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.soap12.TFault;
import org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.soap12.THeader;

/**
 * @author Nicolas Salatge - eBM WebSourcing
 */
public class SOAP12BindingImpl implements
        org.ow2.easywsdl.wsdl.api.binding.wsdl11.soap.soap12.SOAP12Binding4Wsdl11 {

    SOAP12Body soap12body = null;

    List<SOAP12Header> soap12headers = new ArrayList<SOAP12Header>();

    SOAP12Fault soap12fault = null;

    public SOAP12BindingImpl(final List<THeader> headers, final TBody body, final TFault fault) {
        if (headers != null) {
            for (final THeader h : headers) {
                this.soap12headers
                        .add(new org.ow2.easywsdl.wsdl.impl.wsdl11.binding.soap.soap12.SOAP12HeaderImpl(
                                h));
            }
        }
        if (body != null) {
            this.soap12body = new org.ow2.easywsdl.wsdl.impl.wsdl11.binding.soap.soap12.SOAP12BodyImpl(
                    body);
        }
        if (fault != null) {
            this.soap12fault = new org.ow2.easywsdl.wsdl.impl.wsdl11.binding.soap.soap12.SOAP12FaultImpl(
                    fault);
        }
    }

    public List<SOAP12Header> getHeaders() {
        return this.soap12headers;
    }

    public SOAP12Body getBody() {
        return this.soap12body;
    }

    public SOAP12Fault getFault() {
        return this.soap12fault;
    }

}
