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

package org.ow2.easywsdl.wsdl.impl.wsdl11;

import java.util.List;
import java.util.logging.Logger;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.ow2.easywsdl.u.NotImplementedException;
import org.ow2.easywsdl.wsdl.api.Fault;
import org.ow2.easywsdl.wsdl.api.Input;
import org.ow2.easywsdl.wsdl.api.Operation;
import org.ow2.easywsdl.wsdl.api.Output;
import org.ow2.easywsdl.wsdl.api.WSDLException;
import org.ow2.easywsdl.wsdl.api.abstractElmt.AbstractInterfaceTypeImpl;
import org.ow2.easywsdl.wsdl.api.abstractElmt.AbstractOperationImpl;
import org.ow2.easywsdl.wsdl.api.abstractElmt.AbstractWSDLElementImpl;
import org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.ObjectFactory;
import org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.TFault;
import org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.TOperation;
import org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.TParam;

/**
 * @author Nicolas Salatge - eBM WebSourcing
 */
public class OperationImpl extends
		AbstractOperationImpl<TOperation, Input, Output, Fault> implements
		Operation {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOG = Logger.getLogger(OperationImpl.class.getName());

	private ObjectFactory objectFactory = new ObjectFactory();

	public OperationImpl(final TOperation op, final InterfaceTypeImpl itf) {
		super(op, itf);
		this.itf = itf;

		// get the documentation
		this.documentation = new org.ow2.easywsdl.wsdl.impl.wsdl11.DocumentationImpl(
				this.model.getDocumentation(), this);

		for (final JAXBElement item : this.model.getRest()) {

			Object value = item.getValue();

			// get input
			if (isInput(item, value)) {
				this.input = new org.ow2.easywsdl.wsdl.impl.wsdl11.InputImpl(
						(TParam) value, this);
			}

			// get output
			if (isOutput(item, value)) {
				this.output = new org.ow2.easywsdl.wsdl.impl.wsdl11.OutputImpl(
						(TParam) value, this);
			}

			// get fault
			if (isFault(value)) {
				this.faults
						.add(new org.ow2.easywsdl.wsdl.impl.wsdl11.FaultImpl(
								(TFault) value, this));
			}
		}

	}

	private boolean isFault(Object value) {
		return value instanceof TFault;
	}

	private boolean isOutput(final JAXBElement item, Object value) {
		return value instanceof TParam
				&& item.getName().equals(
						new QName("http://schemas.xmlsoap.org/wsdl/",
								"output"));
	}

	private boolean isInput(final JAXBElement item, Object value) {
		return value instanceof TParam
				&& item.getName().equals(
						new QName("http://schemas.xmlsoap.org/wsdl/",
								"input"));
	}

	public void addFault(final Fault fault) {
		JAXBElement<TFault> faultElem = objectFactory
				.createTOperationFault((TFault) ((AbstractWSDLElementImpl) fault)
						.getModel());
		super.getFaults().add(fault);
		this.model.getRest().add(faultElem);
	}

	@Override
	public void setInput(Input input) {
		if (this.input == null) {
			// set input
			JAXBElement<TParam> inputElem = objectFactory
					.createTOperationInput((TParam) ((AbstractWSDLElementImpl) input)
							.getModel());
			this.model.getRest().add(inputElem);
		} else {
			// change input
			for (final JAXBElement item : this.model.getRest()) {
				Object value = item.getValue();
				if (isInput(item, value)) {
					item.setValue((TParam) ((AbstractWSDLElementImpl) input)
							.getModel());
					break;
				}
			}
		}
		super.setInput(input);

	}

	@Override
	public void setOutput(Output output) {
		if (output == null) {
			// delete the internal output
			this.output = null;
			for (final JAXBElement item : this.model.getRest()) {
				Object value = item.getValue();
				if (isOutput(item, value)) {
					item.setValue(null);
					break;
				}
			}
		} else {
			if (this.output == null) {
				// create
				JAXBElement<TParam> outputElem = objectFactory
						.createTOperationOutput((TParam) ((AbstractWSDLElementImpl) output)
								.getModel());
				this.model.getRest().add(outputElem);
			} else {
				// change output
				for (final JAXBElement item : this.model.getRest()) {
					Object value = item.getValue();
					if (isOutput(item, value)) {
						item
								.setValue((TParam) ((AbstractWSDLElementImpl) output)
										.getModel());
						break;
					}
				}
			}
		}
		super.setOutput(output);
	}

	@SuppressWarnings("unchecked")
	public QName getQName() {
		return new QName(((AbstractInterfaceTypeImpl) this.getInterface())
				.getDescription().getTargetNamespace(), this.model.getName());
	}

	public List<String> getParameterOrdering() {
		List<String> res = this.model.getParameterOrder();
		if ((res != null) && (res.size() == 0)) {
			res = null;
		}
		return res;
	}

	public Fault removeFault(final String name) {
		throw new NotImplementedException();
	}

	public void setQName(final QName name) {
		this.model.setName(name.getLocalPart());
	}

	public void setParameterOrdering(final List<String> parameterOrder) {
		throw new NotImplementedException();
	}

	public MEPPatternConstants getPattern() {
		MEPPatternConstants pattern = null;
		for (final JAXBElement item : this.model.getRest()) {
			Object value = item.getValue();
			if (isOutput(item, value)) {
				pattern = MEPPatternConstants.IN_OUT;
			} else if (isInput(item, value) && (pattern == null)) {
				pattern = MEPPatternConstants.IN_ONLY;
			}
		}
		return pattern;
	}

	public void setPattern(final MEPPatternConstants pattern)
			throws WSDLException {
		if ((pattern.equals(MEPPatternConstants.IN_ONLY))
				|| (pattern.equals(MEPPatternConstants.ROBUST_IN_ONLY))) {
			this.setOutput(null);
		} else {
			LOG
					.warning("Do nothing: pattern attribute do not exist in wsdl 1.1");
		}
	}

	public Fault getFaultByElementName(final QName name) {
		Fault res = null;
		for (final Fault f : this.faults) {
			if ((f.getElement() != null)
					&& (f.getElement().getQName().equals(name))) {
				res = f;
				break;
			}
		}
		return res;
	}

	public Fault removeFaultByElementName(final QName name) {
		throw new NotImplementedException();
	}

	public Fault createFault() {
		return new FaultImpl(new TFault(), this);
	}

	public Input createInput() {
		return new InputImpl(new TParam(), this);
	}

	public Output createOutput() {
		return new OutputImpl(new TParam(), this);
	}

}
