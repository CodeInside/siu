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
package org.ow2.easywsdl.wsdl.api.abstractElmt;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfBinding;
import org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfBindingFault;
import org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfBindingInput;
import org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfBindingOperation;
import org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfBindingOutput;
import org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfInterfaceType;
import org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfOperation;


/**
 * @author Nicolas Salatge - eBM WebSourcing
 */
@SuppressWarnings("unchecked")
public abstract class AbstractBindingOperationImpl<E, O extends AbsItfOperation, BIn extends AbsItfBindingInput, BOut extends AbsItfBindingOutput, BF extends AbsItfBindingFault>
        extends AbstractWSDLElementImpl<E> implements AbsItfBindingOperation<O, BIn, BOut, BF> {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
     * the parent binding
     */
    protected AbsItfBinding binding;

    /**
     * the input
     */
    protected BIn input;

    /**
     * the output
     */
    protected BOut output;

    /**
     * the faults
     */
    protected List<BF> faults = new ArrayList<BF>();
    
    public AbstractBindingOperationImpl(E model, AbstractWSDLElementImpl parent) {
        super(model, parent);
    }

    /**
     * @return the binding
     */
    public AbsItfBinding getBinding() {
        return this.binding;
    }

    public O getOperation() {
    	AbsItfInterfaceType interfaceType = this.binding.getInterface();
    	QName name = new QName(interfaceType.getQName().getNamespaceURI(), this.getQName().getLocalPart());
        return (O) this.binding.getInterface().getOperation(name);
    }

    /**
     * @return the input
     */
    public BIn getInput() {
        return this.input;
    }

    /**
     * @param input
     *            the input to set
     */
    public void setInput(final BIn input) {
        this.input = input;
    }

    /**
     * @return the output
     */
    public BOut getOutput() {
        return this.output;
    }

    /**
     * @param output
     *            the output to set
     */
    public void setOutput(final BOut output) {
        this.output = output;
    }

    /**
     * @return the fault
     */
    public List<BF> getFaults() {
        if (this.faults == null) {
            this.faults = new ArrayList<BF>();
        }
        return this.faults;
    }

    /**
     * @param fault
     *            the fault to set
     */
    public void setFaults(final List<BF> fault) {
        this.faults = fault;
    }

    public BF getFault(final String name) {
        BF res = null;
        for (final BF f : this.faults) {
            if (f.getName().equals(name)) {
                res = f;
                break;
            }
        }
        return res;
    }

}
