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
package org.ow2.easywsdl.wsdl;

import java.util.Map;

import org.ow2.easywsdl.wsdl.api.Description;
import org.ow2.easywsdl.wsdl.api.WSDLException;
import org.ow2.easywsdl.wsdl.api.WSDLReader;
import org.ow2.easywsdl.wsdl.api.WSDLWriter;
import org.ow2.easywsdl.wsdl.api.WSDLReader.FeatureConstants;
import org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfDescription.WSDLVersionConstants;


/**
 * This abstract class defines a factory API that enables applications to obtain
 * a WSDLFactory capable of producing new Definitions, new WSDLReaders, and new
 * WSDLWriters.
 *
 * Some ideas used here have been shamelessly copied from the wonderful JAXP and
 * Xerces work.
 *
 * @author Nicolas Salatge - eBM WebSourcing
 */
public abstract class WSDLFactory {

    /**
     * Get a new instance of a WSDLFactory. This method follows (almost) the
     * same basic sequence of steps that JAXP follows to determine the
     * fully-qualified class name of the class which implements WSDLFactory.
     * <p>
     * The steps in order are:
     * <ol>
     * <li>Check the property file
     * META-INF/services/javax.wsdl.factory.WSDLFactory.</li>
     * <li>Check the javax.wsdl.factory.WSDLFactory system property.</li>
     * <li>Check the lib/wsdl.properties file in the JRE directory. The key will
     * have the same name as the above system property.</li>
     * <li>Use the default class name provided by the implementation.</li>
     * </ol>
     * <p>
     * Once an instance of a WSDLFactory is obtained, invoke newDefinition(),
     * newWSDLReader(), or newWSDLWriter(), to create the desired instances.
     */
    public static WSDLFactory newInstance() throws WSDLException {
        return new WSDLFactoryImpl();
    }

    /**
     * Create a new instance of a Definition.
     * @throws WSDLException
     */
    public abstract Description newDescription(WSDLVersionConstants version) throws WSDLException;

    /**
     * Create a new instance of a WSDLReaderImpl.
     */
    public abstract WSDLReader newWSDLReader() throws WSDLException;
    
    /**
     * Create a new instance of a WSDLReaderImpl.
     */
    public abstract WSDLReader newWSDLReader(Map<FeatureConstants, Object> features) throws WSDLException;

    /**
     * Create a new instance of a WSDLWriterImpl.
     * @throws WSDLException 
     */
    public abstract WSDLWriter newWSDLWriter() throws WSDLException;


}
