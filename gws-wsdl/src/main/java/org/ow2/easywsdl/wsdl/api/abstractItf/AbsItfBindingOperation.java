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
import org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfBinding.StyleConstant;
import org.ow2.easywsdl.wsdl.api.binding.BindingProtocol.SOAPMEPConstants;


/**
 * This interface represents a WSDL operation binding. That is, it holds the
 * information that would be specified in the operation element contained within
 * a binding element.
 * 
 * @author Nicolas Salatge - eBM WebSourcing
 */
@SuppressWarnings("unchecked")
public interface AbsItfBindingOperation<O extends AbsItfOperation, BIn extends AbsItfBindingInput, BOut extends AbsItfBindingOutput, BF extends AbsItfBindingFault>
        extends WSDLElement {

    /**
     * Set the name of this operation binding.
     * 
     * @param name
     *            the desired name
     */
    void setQName(QName name);

    /**
     * Get the name of this operation binding.
     * 
     * @return the operation binding name
     */
    QName getQName();

    /**
     * Get the operation that this operation binding binds.
     * 
     * @return the operation that this operation binding binds
     */
    O getOperation();
    
    /**
     * create input
     */
    BIn createInput();

    /**
     * Set the input message specification for this operation.
     * 
     * @param input
     *            the new input message
     */
    void setInput(BIn input);

    /**
     * Get the input message specification for this operation.
     * 
     * @return the input message
     */
    BIn getInput();
    
    /**
     * create output
     */
    BOut createOutput();

    /**
     * Set the output message specification for this operation.
     * 
     * @param output
     *            the new output message
     */
    void setOutput(BOut output);

    /**
     * Get the output message specification for this operation.
     * 
     * @return the output message specification for the operation
     */
    BOut getOutput();
    
    /**
     * create fault
     */
    BF createFault();

    /**
     * Add a fault binding.
     * 
     * @param absItfBindingFault
     *            the new fault binding
     */
    void addFault(BF absItfBindingFault);

    /**
     * Remove a fault binding.
     * 
     * @param name
     *            the name of the fault binding to be removed
     * @return the BindingFaultImpl which was removed
     */
    BF removeFault(String name);

    /**
     * Get the specified fault binding.
     * 
     * @param name
     *            the name of the desired fault binding.
     * @return the corresponding fault binding, or null if there wasn't any
     *         matching fault binding
     */
    BF getFault(String name);

    /**
     * Get all the fault bindings associated with this operation binding.
     * 
     * @return names of fault bindings
     */
    List<BF> getFaults();

    /**
     * get the mep
     */
    SOAPMEPConstants getMEP();

    /**
     * set the mep
     * 
     * @param mep
     *            the mep
     */
    void setMEP(SOAPMEPConstants mep);

    /**
     * get style
     */
    StyleConstant getStyle();

    /**
     * get the soap action
     * 
     * @return
     */
    String getSoapAction();
    
    /**
     * set soap action
     */
    void setSoapAction(String action);

    /*
     * http attribute for WSDL 2.0
     */

    /**
     * get the http location
     * 
     * @return
     */
    String getHttpLocation();

    String getHttpMethod();

    String getHttpInputSerialization();

    String getHttpOutputSerialization();

    String getHttpFaultSerialization();

    String getHttpQueryParameterSeparator();

    String getHttpContentEncodingDefault();

    boolean isHttpIgnoreUncited();

}
