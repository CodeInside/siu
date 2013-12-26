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

import org.ow2.easywsdl.wsdl.api.Part;
import org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfFault;
import org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfInput;
import org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfInterfaceType;
import org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfOperation;
import org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfOutput;


/**
 * @author Nicolas Salatge - eBM WebSourcing
 */
public abstract class AbstractOperationImpl<E, In extends AbsItfInput, Out extends AbsItfOutput, F extends AbsItfFault>
        extends AbstractWSDLElementImpl<E> implements AbsItfOperation<In, Out, F> {



    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
     * the parent interface
     */
    @SuppressWarnings("unchecked")
    protected AbsItfInterfaceType itf;

    /**
     * the input
     */
    protected In input;

    /**
     * the output
     */
    protected Out output;

    /**
     * the faults
     */
    protected List<F> faults = new ArrayList<F>();

    public AbstractOperationImpl(E model, AbstractWSDLElementImpl parent) {
        super(model, parent);
    }
    
    /**
     * @return the interface
     */
    @SuppressWarnings("unchecked")
    public AbsItfInterfaceType getInterface() {
        return this.itf;
    }

    /**
     * @return the input
     */
    public In getInput() {
        return this.input;
    }

    /**
     * @param input
     *            the input to set
     */
    public void setInput(final In input) {
        this.input = input;
    }

    /**
     * @return the output
     */
    public Out getOutput() {
        return this.output;
    }

    /**
     * @param output
     *            the output to set
     */
    public void setOutput(final Out output) {
        this.output = output;
    }

    /**
     * @return the fault
     */
    public List<F> getFaults() {
        if (this.faults == null) {
            this.faults = new ArrayList<F>();
        }
        return this.faults;
    }

    /**
     * @param fault
     *            the fault to set
     */
    public void setFaults(final List<F> fault) {
        this.faults = fault;
    }

    public F getFault(final String name) {
        F res = null;
        for (final F f : this.faults) {
            if (f.getMessageName().getLocalPart().equals(name)) {
                res = f;
                break;
            }
        }
        return res;
    }

    public String getSignature() {
        String res = null;
        
        // get the output
        String returnS = "void";
        if (this.getOutput() != null) {
            if (this.getOutput().getElement() != null) {
                if (this.getOutput().getElement().getType().getQName() != null) {
                    returnS = this.getOutput().getElement().getType().getQName().toString();
                } else {
                    returnS = this.getOutput().getElement().getQName().toString();
                }
            } else if (this.getOutput().getParts() != null) {
                if (this.getOutput().getParts().size() == 1) {
                    if (this.getOutput().getParts().get(0).getElement() != null) {
                        if (this.getOutput().getParts().get(0).getElement().getType().getQName() != null) {
                            returnS = this.getOutput().getParts().get(0).getElement().getType()
                                    .getQName().toString();
                        } else {
                            returnS = this.getOutput().getParts().get(0).getElement().getQName()
                                    .toString();
                        }
                    } else if (this.getOutput().getParts().get(0).getType() != null) {
                        returnS = this.getOutput().getParts().get(0).getType().getQName()
                                .toString();
                    }
                } else {
                    returnS = "{";
                    StringBuffer buf = new StringBuffer();

                    for (final Part part : this.getOutput().getParts()) {
                        if (part.getElement() != null) {
                            if (part.getElement().getType().getQName() != null) {
                                buf.append(part.getElement().getType().getQName().toString() + " ;");
                            } else {
                            	buf.append(part.getType().getQName().toString() + " ;");
                            }
                        } else if (part.getType() != null) {
                        	buf.append(part.getType().getQName().toString() + " ;");
                        }
                    }
                    returnS = buf.toString() + "}";
                    returnS = returnS.replace(";}", "}");
                }
            }
        }

        // get the operation name
        final String operation = this.getQName().toString();

        // get the input parameters
        String params = "(";
        if (this.getInput() != null) {
            if (this.getInput().getElement() != null) {
                if (this.getInput().getElement().getType().getQName() != null) {
                    params = params + this.getInput().getElement().getType().getQName().toString()
                            + " " + this.getInput().getElement().getQName().toString();
                } else {
                    params = params + this.getInput().getElement().getQName().toString();
                }
            } else if (this.getInput().getParts() != null) {
                for (final Part part : this.getInput().getParts()) {
                    if (part.getElement() != null) {
                        if (part.getElement().getType().getQName() != null) {
                            params = params + part.getElement().getType().getQName().toString()
                                    + " " + part.getElement().getQName().toString() + " ,";
                        } else {
                            params = params + part.getElement().getQName().toString() + " ,";
                        }
                    } else if (part.getType() != null) {
                        params = params + part.getType().getQName().toString() + " "
                                + part.getPartQName() + " ,";
                    }
                }
            }
        }
        params = params + ")";
        params = params.replace(",)", ")");

        // create signature
        res = returnS + " " + operation + params;
        return res;
    }
}
