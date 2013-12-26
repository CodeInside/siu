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

package org.ow2.easywsdl.wsdl.impl.generic;

import javax.xml.bind.JAXBContext;

import org.ow2.easywsdl.schema.api.SchemaException;
import org.ow2.easywsdl.schema.impl.SchemaJAXBContext;
import org.ow2.easywsdl.wsdl.api.WSDLException;


/**
 * @author Nicolas Salatge - eBM WebSourcing
 */
public final class WSDLJAXBContext {


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

	public static WSDLJAXBContext getInstance() throws WSDLException {
		if(fail == null) {
			return instance;
		} else {
			throw fail;
		}
	}

	/**
	 * Private object initializations
	 */
	private WSDLJAXBContext() throws WSDLException {
		try {
			SchemaJAXBContext.getInstance().addOtherObjectFactory(org.ow2.easywsdl.wsdl.impl.wsdl11.WSDLJAXBContext.getDefaultObjectFactories());
			SchemaJAXBContext.getInstance().addOtherObjectFactory(org.ow2.easywsdl.wsdl.impl.wsdl20.WSDLJAXBContext.getDefaultObjectFactories());
			SchemaJAXBContext.getInstance().getJaxbContext();
		} catch (SchemaException e) {
			throw new WSDLException(e);
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
