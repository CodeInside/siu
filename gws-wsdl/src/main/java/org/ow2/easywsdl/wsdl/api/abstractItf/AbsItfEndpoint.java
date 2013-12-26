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

/**
 * This interface represents a port, an endpoint for the functionality described
 * by a particular port type.
 * 
 * @author Nicolas Salatge - eBM WebSourcing
 */
@SuppressWarnings("unchecked")
public interface AbsItfEndpoint<S extends AbsItfService, B extends AbsItfBinding> extends WSDLElement {
    /**
     * Set the name of this port.
     * 
     * @param name
     *            the desired name
     */
    void setName(String name);

    /**
     * Get the name of this port.
     * 
     * @return the port name
     */
    String getName();

    /**
     * Set the address of this port.
     * 
     * @param address
     *            the desired name
     */
    void setAddress(String address);

    /**
     * Get the address of this port.
     * 
     * @return the port address
     */
    String getAddress();

    /**
     * Set the binding this port should refer to.
     * 
     * @param binding
     *            the desired binding
     */
    void setBinding(B binding);

    /**
     * Get the binding this port refers to.
     * 
     * @return the binding associated with this port
     */
    B getBinding();
    
    S getService();

    /*
     * http binding attribute
     */
    String getHttpAuthenticationScheme();

    String getHttpAuthenticationRealm();
}
