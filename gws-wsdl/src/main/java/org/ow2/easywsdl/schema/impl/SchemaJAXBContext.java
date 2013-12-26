/**
 * easySchema - easyWSDL toolbox Platform.
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
package org.ow2.easywsdl.schema.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.validation.SchemaFactory;

import org.ow2.easywsdl.schema.api.SchemaException;


/**
 * @author Nicolas Salatge - eBM WebSourcing
 */
public final class SchemaJAXBContext {

	
	private final List<Class> defaultObjectFactories = new ArrayList<Class>(Arrays.asList(new Class[] {
			org.ow2.easywsdl.schema.org.w3._2001.xmlschema.ObjectFactory.class}));

	
	private List<Class> currentObjectFactories = new ArrayList<Class>(defaultObjectFactories);
	
	/**
     * The JAXB context
     */
    private JAXBContext jaxbContext;
    
    private static SchemaJAXBContext instance = null;
    private static SchemaException fail = null;
    
    static {
    	try {
    		instance = new SchemaJAXBContext();
    	} catch (SchemaException e) {
    		fail = e;
    	}
    }
    
    
    /**
     * Private object initializations
     */
    private SchemaJAXBContext() throws SchemaException {
        SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        // The EndpointReference parent resource is in the Jar where the class
        // is
        // loaded
        try {
        	this.jaxbContext = JAXBContext
			.newInstance(currentObjectFactories.toArray(new Class[currentObjectFactories.size()]));

        } catch (final JAXBException e) {
            throw new SchemaException(e);
        }

    }
    
	public static SchemaJAXBContext getInstance() throws SchemaException {
		if(fail == null) {
			return instance;
		} else {
			throw fail;
		}
	}



	public synchronized void addOtherObjectFactory(List<Class> addedObjectFactories) throws SchemaException {
		if(addedObjectFactories != null) {
			currentObjectFactories.addAll(addedObjectFactories);

			try {
				//factory.newSchema(new StreamSource[] { new StreamSource(schemaUrl11.openStream()) });

				this.jaxbContext = JAXBContext
				.newInstance(currentObjectFactories.toArray(new Class[currentObjectFactories.size()]));

			} catch (final JAXBException e) {
				throw new SchemaException(e);
			}
		}
	}


	/**
	 * @return the jaxbContext
	 */
	public synchronized JAXBContext getJaxbContext() {
		return this.jaxbContext;
	}
}
