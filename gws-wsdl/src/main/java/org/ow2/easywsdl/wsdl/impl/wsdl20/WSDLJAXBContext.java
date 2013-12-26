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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.JAXBContext;

import org.ow2.easywsdl.schema.api.SchemaException;
import org.ow2.easywsdl.schema.impl.SchemaJAXBContext;
import org.ow2.easywsdl.wsdl.api.WSDLException;


/**
 * @author Nicolas Salatge - eBM WebSourcing
 */
public final class WSDLJAXBContext {



	private static List<Class> defaultObjectFactories = new ArrayList<Class>(Arrays.asList(new Class[] {
			org.ow2.easywsdl.wsdl.org.w3.ns.wsdl.ObjectFactory.class,
			org.ow2.easywsdl.wsdl.org.w3.ns.wsdl.http.ObjectFactory.class,
			org.ow2.easywsdl.wsdl.org.w3.ns.wsdl.rpc.ObjectFactory.class,
			org.ow2.easywsdl.wsdl.org.w3.ns.wsdl.soap.ObjectFactory.class,
			org.ow2.easywsdl.schema.org.w3._2001.xmlschema.ObjectFactory.class}));

	public static List<Class> getDefaultObjectFactories() {
		return defaultObjectFactories;
	}

	/**
     * The JAXB context
     */
    private static WSDLJAXBContext instance = null;
    private static WSDLException fail = null;
    
    static {
    	try {
    		instance = new WSDLJAXBContext();
    	} catch (WSDLException e) {
    		fail = e;
    	}
    }

	/**
	 * Private object initializations
	 */
	private WSDLJAXBContext() throws WSDLException {
		try {
			SchemaJAXBContext.getInstance().addOtherObjectFactory(defaultObjectFactories);
	    	SchemaJAXBContext.getInstance().getJaxbContext();
		} catch (final SchemaException e) {
			throw new WSDLException(e);
		}
	}


	public static WSDLJAXBContext getInstance() throws WSDLException {
		if(fail == null) {
			return instance;
		} else {
			throw fail;
		}
	}



	/**
	 * @return the jaxbContext
	 * @throws WSDLException 
	 */
	public JAXBContext getJaxbContext() throws WSDLException {
		try {
			return SchemaJAXBContext.getInstance().getJaxbContext();
		} catch (SchemaException e) {
			throw new WSDLException(e);
		}
	}
}
