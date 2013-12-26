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

import javax.xml.namespace.QName;

import org.ow2.easywsdl.wsdl.api.binding.wsdl11.soap.SOAPBinding4Wsdl11.UseConstants;
import org.ow2.easywsdl.wsdl.api.binding.wsdl11.soap.soap11.SOAP11HeaderFault;
import org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.soap.THeader;
import org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.soap.THeaderFault;

/**
 * @author Nicolas Salatge - eBM WebSourcing
 */
public class SOAP11HeaderImpl implements
        org.ow2.easywsdl.wsdl.api.binding.wsdl11.soap.soap11.SOAP11Header {

    private final THeader header;

    private List<SOAP11HeaderFault> headerFaults = null;

    public SOAP11HeaderImpl(final THeader header) {
        this.header = header;

        if (this.header.getHeaderfault() != null) {
            this.headerFaults = new ArrayList<SOAP11HeaderFault>();
            for (final THeaderFault hf : this.header.getHeaderfault()) {
                this.headerFaults
                        .add(new org.ow2.easywsdl.wsdl.impl.wsdl11.binding.soap.soap11.SOAP11HeaderFaultImpl(
                                hf));
            }
        }
    }

    public List<String> getEncodingStyles() {
        List<String> res = null;
        if ((this.header.getEncodingStyle() != null)
                && (this.header.getEncodingStyle().size() > 0)) {
            res = new ArrayList<String>();
            for (final String item : this.header.getEncodingStyle()) {
                res.add(item);
            }
        }
        return res;
    }

    public String getNamespace() {
        return this.header.getNamespace();
    }

    public UseConstants getUse() {
        UseConstants res = null;
        if (this.header.getUse() != null) {
            res = UseConstants.valueOf(this.header.getUse().value());
        }
        return res;
    }

    public QName getMessage() {
        return this.header.getMessage();
    }

    public String getPart() {
        return this.header.getPart();
    }

    public boolean isRequired() {
        return this.header.isRequired();
    }

    public List<SOAP11HeaderFault> getHeaderFaults() {
        return this.headerFaults;
    }

    public THeader getModel() {
    	return this.header;
    }
}
