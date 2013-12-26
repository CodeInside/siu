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
package org.ow2.easywsdl.wsdl.api.abstractItf;

import org.ow2.easywsdl.wsdl.api.WSDLElement;
import org.ow2.easywsdl.wsdl.api.WSDLException;
import org.ow2.easywsdl.wsdl.api.binding.wsdl11.http.HTTPBinding4Wsdl11;
import org.ow2.easywsdl.wsdl.api.binding.wsdl11.mime.MIMEBinding4Wsdl11;
import org.ow2.easywsdl.wsdl.api.binding.wsdl11.soap.soap11.SOAP11Binding4Wsdl11;
import org.ow2.easywsdl.wsdl.api.binding.wsdl11.soap.soap12.SOAP12Binding4Wsdl11;
import org.ow2.easywsdl.wsdl.api.binding.wsdl20.http.HTTPBinding4Wsdl20;
import org.ow2.easywsdl.wsdl.api.binding.wsdl20.soap.SOAPBinding4Wsdl20;

/**
 * This interface represents an input message, and contains the name of the
 * input and the message itself.
 *
 * @author Nicolas Salatge - eBM WebSourcing
 */
public abstract interface AbsItfBindingParam extends WSDLElement {

    /**
     * Set the name of this input.
     *
     * @param name
     *            the desired name
     * @throws WSDLException
     */
    void setName(String name);

    /**
     * Get the name of this input.
     *
     * @return the input message name
     */
    String getName();

    SOAP11Binding4Wsdl11 createSOAP11Binding4Wsdl11();

    SOAP11Binding4Wsdl11 getSOAP11Binding4Wsdl11();

    void setSOAP11Binding4Wsdl11(SOAP11Binding4Wsdl11 soap11binding);

    SOAP12Binding4Wsdl11 getSOAP12Binding4Wsdl11();

    HTTPBinding4Wsdl11 getHTTPBinding4Wsdl11();

    MIMEBinding4Wsdl11 getMIMEBinding4Wsdl11();

    SOAPBinding4Wsdl20 createSOAP12Binding4Wsdl20();

    SOAPBinding4Wsdl20 getSOAP12Binding4Wsdl20();

    void setSOAP12Binding4Wsdl20(SOAPBinding4Wsdl20 soap12binding);

    HTTPBinding4Wsdl20 getHTTPBinding4Wsdl20();

    String getHttpContentEncoding();
}
