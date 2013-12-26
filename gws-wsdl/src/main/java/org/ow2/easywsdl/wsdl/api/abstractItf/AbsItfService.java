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

import java.util.List;

import javax.xml.namespace.QName;

import org.ow2.easywsdl.wsdl.api.WSDLElement;
import org.ow2.easywsdl.wsdl.api.WSDLException;


/**
 * This interface represents a service, which groups related endpoints to
 * provide some functionality.
 * 
 * @author Nicolas Salatge - eBM WebSourcing
 */
@SuppressWarnings("unchecked")
public interface AbsItfService<I extends AbsItfInterfaceType, E extends AbsItfEndpoint> extends
        WSDLElement {
    /**
     * Set the name of this service.
     * 
     * @param name
     *            the desired name
     */
    void setQName(QName name);

    /**
     * Get the name of this service.
     * 
     * @return the service name
     */
    QName getQName();
    
    /**
     * create the specified endpoint.
	 *
     * @return the corresponding endpoint, or null if there wasn't any matching
     *         endpoint
     */
    E createEndpoint();

    /**
     * Add a endpoint to this service.
     * 
     * @param endpoint
     *            the endpoint to be added
     */
    void addEndpoint(E endpoint);

    /**
     * Get the specified endpoint.
     * 
     * @param name
     *            the name of the desired endpoint.
     * @return the corresponding endpoint, or null if there wasn't any matching
     *         endpoint
     */
    E getEndpoint(String name);

    /**
     * Remove the specified endpoint.
     * 
     * @param name
     *            the name of the endpoint to be removed.
     * @return the endpoint which was removed.
     */
    E removeEndpoint(String name);

    /**
     * Get all the endpoints defined here.
     */
    List<E> getEndpoints();

    /**
     * <p>
     * Get interface.
     * </p>
     * <p>
     * In case of WSDL 2.0, the returned interface is defined by the XPath
     * expression '/description/service@interface'.
     * </p>
     * <p>
     * In case of WSDL 1.1, according to <a
     * href="http://www.w3.org/TR/wsdl#_services">WSDL 1.1 specifications</a>,
     * if all service endpoints share the same port type, the returned interface
     * is defined by the endpoints port type, otherwise an exception
     * {@link WSDLException} is thrown.
     * </p>
     */
    I getInterface() throws WSDLException;
    
    
    void setInterface(I itf);
}
