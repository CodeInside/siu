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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.ow2.easywsdl.u.NotImplementedException;
import org.ow2.easywsdl.schema.api.XmlException;
import org.ow2.easywsdl.wsdl.api.Fault;
import org.ow2.easywsdl.wsdl.api.Input;
import org.ow2.easywsdl.wsdl.api.Output;
import org.ow2.easywsdl.wsdl.api.WSDLException;
import org.ow2.easywsdl.wsdl.api.abstractElmt.AbstractInterfaceTypeImpl;
import org.ow2.easywsdl.wsdl.api.abstractElmt.AbstractOperationImpl;
import org.ow2.easywsdl.wsdl.api.abstractElmt.AbstractWSDLElementImpl;
import org.ow2.easywsdl.wsdl.org.w3.ns.wsdl.InterfaceOperationType;
import org.ow2.easywsdl.wsdl.org.w3.ns.wsdl.MessageRefFaultType;
import org.ow2.easywsdl.wsdl.org.w3.ns.wsdl.MessageRefType;
import org.ow2.easywsdl.wsdl.org.w3.ns.wsdl.ObjectFactory;
import org.w3c.dom.Element;

/**
 * @author Nicolas Salatge - eBM WebSourcing
 */
public class OperationImpl extends
		AbstractOperationImpl<InterfaceOperationType, Input, Output, Fault>
		implements org.ow2.easywsdl.wsdl.api.Operation {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private ObjectFactory factory = new ObjectFactory();

	private static final Logger LOG = Logger.getLogger(OperationImpl.class.getName());

	public OperationImpl(final InterfaceOperationType op,
			final InterfaceTypeImpl itf) {
		super(op, itf);
		this.itf = itf;

		// get the documentation
		this.documentation = new org.ow2.easywsdl.wsdl.impl.wsdl20.DocumentationImpl(
				this.model.getDocumentation(), this);

		for (final Object item : this.model.getInputOrOutputOrInfault()) {
			if (item instanceof JAXBElement) {
				Object value = ((JAXBElement) item).getValue();

				// get input
				if (isInput((JAXBElement) item, value)) {
					this.input = new org.ow2.easywsdl.wsdl.impl.wsdl20.InputImpl(
							(MessageRefType) value, this);
				}

				// get output
				if (isOutput((JAXBElement) item, value)) {
					this.output = new org.ow2.easywsdl.wsdl.impl.wsdl20.OutputImpl(
							(MessageRefType) value, this);
				}

				// get fault
				if (isInFault((JAXBElement) item, value)) {
					this.faults
							.add(new org.ow2.easywsdl.wsdl.impl.wsdl20.FaultImpl(
									(MessageRefFaultType) value, this));
				}

				// get fault
				if (isOutFault((JAXBElement) item, value)) {
					this.faults
							.add(new org.ow2.easywsdl.wsdl.impl.wsdl20.FaultImpl(
									(MessageRefFaultType) value, this));
				}
			}
		}
	}

	private boolean isInFault(final JAXBElement item, Object value) {
		return value instanceof MessageRefFaultType
				&& item.getName().equals(
						new QName("http://www.w3.org/ns/wsdl", "infault"));
	}

	private boolean isOutFault(final JAXBElement item, Object value) {
		return value instanceof MessageRefFaultType
				&& item.getName().equals(
						new QName("http://www.w3.org/ns/wsdl", "outfault"));
	}

	private boolean isOutput(final JAXBElement item, Object value) {
		return value instanceof MessageRefType
				&& item.getName().equals(
						new QName("http://www.w3.org/ns/wsdl", "output"));
	}

	private boolean isInput(final JAXBElement item, Object value) {
		return value instanceof MessageRefType
				&& item.getName().equals(
						new QName("http://www.w3.org/ns/wsdl", "input"));
	}

	public void addFault(final Fault fault) {
		JAXBElement<MessageRefFaultType> jaxbFault = factory
				.createInterfaceOperationTypeOutfault(((MessageRefFaultType) ((AbstractWSDLElementImpl) fault)
						.getModel()));
		super.getFaults().add(fault);
		this.model.getInputOrOutputOrInfault().add(jaxbFault);
	}

	@SuppressWarnings("unchecked")
	public QName getQName() {
		return new QName(((AbstractInterfaceTypeImpl) this.getInterface())
				.getDescription().getTargetNamespace(), this.model.getName());
	}

	public List<String> getParameterOrdering() {
		return null;
	}

	@Override
	public void setInput(Input input) {
		if (this.input == null) {
			// set input
			JAXBElement<MessageRefType> inputElem = factory
					.createInterfaceOperationTypeInput((MessageRefType) ((AbstractWSDLElementImpl) input)
							.getModel());
			this.model.getInputOrOutputOrInfault().add(inputElem);
		} else {
			// change input
			for (final Object item : this.model.getInputOrOutputOrInfault()) {
				if (item instanceof JAXBElement) {
					Object value = ((JAXBElement) item).getValue();
					if (isInput((JAXBElement) item, value)) {
						((JAXBElement) item)
								.setValue((MessageRefType) ((AbstractWSDLElementImpl) input)
										.getModel());
						break;
					}
				}
			}
		}
		super.setInput(input);

	}

	@Override
	public void setOutput(Output output) {
		if (this.output == null) {
			JAXBElement<MessageRefType> outputElem = factory
					.createInterfaceOperationTypeOutput((MessageRefType) ((AbstractWSDLElementImpl) output)
							.getModel());
			this.model.getInputOrOutputOrInfault().add(outputElem);
		} else {
			// change output
			for (final Object item : this.model.getInputOrOutputOrInfault()) {
				if (item instanceof JAXBElement) {
					Object value = ((JAXBElement) item).getValue();
					if (isOutput((JAXBElement) item, value)) {
						((JAXBElement) item)
								.setValue((MessageRefType) ((AbstractWSDLElementImpl) output)
										.getModel());
						break;
					}
				}
			}
		}
		super.setOutput(output);
	}

	public Fault removeFault(final String name) {
		throw new NotImplementedException();
	}

	public void setQName(final QName name) {
		this.model.setName(name.getLocalPart());
	}

	public void setParameterOrdering(final List<String> parameterOrder)
			throws WSDLException {
		LOG.warning("Do nothing: parameterOrdering not exist in wsdl 2.0");
	}

	public MEPPatternConstants getPattern() {
		MEPPatternConstants pattern = null;
		try {
			pattern = MEPPatternConstants.valueOf(new URI(this.model
					.getPattern()));
		} catch (final URISyntaxException e) {
			pattern = null;
		}
		return pattern;
	}

	public void setPattern(final MEPPatternConstants pattern) {
		this.model.setPattern(pattern.value().toString());
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

	@Override
	public List<Element> getOtherElements() throws XmlException {
		final List<Element> res = super.getOtherElements();

		for (final Object item : this.model.getInputOrOutputOrInfault()) {
			if (item instanceof Element) {
				res.add((Element) item);
			}
		}

		return res;
	}

	public Fault createFault() {
		return new FaultImpl(new MessageRefFaultType(), this);
	}

	public Input createInput() {
		return new InputImpl(new MessageRefType(), this);
	}

	public Output createOutput() {
		return new OutputImpl(new MessageRefType(), this);
	}
}
