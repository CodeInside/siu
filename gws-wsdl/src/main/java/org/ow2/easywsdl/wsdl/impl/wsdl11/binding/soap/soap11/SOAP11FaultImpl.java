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

import org.ow2.easywsdl.wsdl.api.binding.wsdl11.soap.SOAPBinding4Wsdl11.UseConstants;
import org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.soap.TFault;
import org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.soap.UseChoice;

/**
 * @author Nicolas Salatge - eBM WebSourcing
 */
public class SOAP11FaultImpl implements
        org.ow2.easywsdl.wsdl.api.binding.wsdl11.soap.soap11.SOAP11Fault {

    private final TFault fault;

    public SOAP11FaultImpl(final TFault fault) {
        this.fault = fault;
    }

    public List<String> getEncodingStyles() {
        List<String> res = null;
        if ((this.fault.getEncodingStyle() != null)
                && (this.fault.getEncodingStyle().size() > 0)) {
            res = new ArrayList<String>();
            for (final String item : this.fault.getEncodingStyle()) {
                res.add(item);
            }
        }
        return res;
    }

    public String getName() {
        return this.fault.getName();
    }
    
    public void setName(final String name) {
    	this.fault.setName(name);
    }

    public String getNamespace() {
        return this.fault.getNamespace();
    }

    public UseConstants getUse() {
        UseConstants res = null;
        if (this.fault.getUse() != null) {
            if (this.fault.getUse().value().equals(UseConstants.ENCODED.value())) {
                res = UseConstants.ENCODED;
            } else if (this.fault.getUse().value().equals(UseConstants.LITERAL.value())) {
                res = UseConstants.LITERAL;
            }
        }
        return res;
    }

    public List<String> getParts() {
        List<String> res = null;
        if ((this.fault.getParts() != null) && (this.fault.getParts().size() > 0)) {
            res = new ArrayList<String>();
            for (final String item : this.fault.getParts()) {
                res.add(item);
            }
        }
        return res;
    }

    public Boolean getRequired() {
        return this.fault.isRequired();
    }

	public void setUse(UseConstants use) {
		if (use.value().equals(UseChoice.ENCODED.value())) {
            this.fault.setUse(UseChoice.ENCODED);
        } else if (use.equals(UseChoice.LITERAL.value())) {
        	this.fault.setUse(UseChoice.LITERAL);
        }
	}

	public TFault getModel() {
		return this.fault;
	}
}
