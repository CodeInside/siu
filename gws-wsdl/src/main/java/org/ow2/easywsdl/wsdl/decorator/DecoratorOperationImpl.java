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
package org.ow2.easywsdl.wsdl.decorator;

import java.util.List;

import javax.xml.namespace.QName;

import org.ow2.easywsdl.wsdl.api.WSDLException;
import org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfFault;
import org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfInput;
import org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfOperation;
import org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfOutput;
import org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfOperation.MEPPatternConstants;


/**
 * @author Nicolas Salatge - eBM WebSourcing
 */
public abstract class DecoratorOperationImpl<In extends AbsItfInput, Out extends AbsItfOutput, F extends AbsItfFault> 
extends Decorator<AbsItfOperation<In, Out, F>> {

    /**
	 *
	 */
    private static final long serialVersionUID = 1L;

    public DecoratorOperationImpl(final AbsItfOperation<In, Out, F> operation) throws WSDLException {
        this.internalObject = operation;
    }

    public void addFault(final F arg0) {
        this.internalObject.addFault(arg0);
    }

    public F getFault(final String arg0) {
        return this.internalObject.getFault(arg0);
    }

    public F getFaultByElementName(final QName arg0) {
        return this.internalObject.getFaultByElementName(arg0);
    }

    public List<F> getFaults() {
        return this.internalObject.getFaults();
    }

    public In getInput() {
        return this.internalObject.getInput();
    }

    public Out getOutput() {
        return this.internalObject.getOutput();
    }

    public List<String> getParameterOrdering() {
        return this.internalObject.getParameterOrdering();
    }

    public MEPPatternConstants getPattern() {
        return this.internalObject.getPattern();
    }

    public QName getQName() {
        return this.internalObject.getQName();
    }

    public String getSignature() {
        return this.internalObject.getSignature();
    }

    public F removeFault(final String arg0) {
        return this.internalObject.removeFault(arg0);
    }

    public F removeFaultByElementName(final QName arg0) {
        return this.internalObject.getFaultByElementName(arg0);
    }

    public void setInput(final In arg0) {
        this.internalObject.setInput(arg0);
    }

    public void setOutput(final Out arg0) {
        this.internalObject.setOutput(arg0);
    }

    public void setParameterOrdering(final List<String> arg0) throws WSDLException {
        this.internalObject.setParameterOrdering(arg0);
    }

    public void setPattern(final MEPPatternConstants arg0) throws WSDLException {
        this.internalObject.setPattern(arg0);
    }

    public void setQName(final QName arg0) {
        this.internalObject.setQName(arg0);
    }

	public F createFault() {
		return this.internalObject.createFault();
	}

	public In createInput() {
		return this.internalObject.createInput();
	}

	public Out createOutput() {
		return this.internalObject.createOutput();
	}

}
