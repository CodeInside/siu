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

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;

import org.ow2.easywsdl.u.NotImplementedException;
import org.ow2.easywsdl.wsdl.api.Operation;
import org.ow2.easywsdl.wsdl.api.WSDLElement;
import org.ow2.easywsdl.wsdl.api.WSDLException;
import org.ow2.easywsdl.wsdl.api.abstractElmt.AbstractInterfaceTypeImpl;
import org.ow2.easywsdl.wsdl.api.abstractElmt.AbstractWSDLElementImpl;
import org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.TDefinitions;
import org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.TOperation;
import org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.TPortType;
import org.w3c.dom.Element;

/**
 * @author Nicolas Salatge - eBM WebSourcing
 */
public class InterfaceTypeImpl extends AbstractInterfaceTypeImpl<TPortType, Operation> implements org.ow2.easywsdl.wsdl.api.InterfaceType {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InterfaceTypeImpl(final TPortType itf, final DescriptionImpl desc) {
		super(itf, desc);
		this.desc = desc;

		// get the documentation
		this.documentation = new org.ow2.easywsdl.wsdl.impl.wsdl11.DocumentationImpl(this.model.getDocumentation(), this);

		// get the operation
		for (final TOperation top : this.model.getOperation()) {
			final Operation op = new org.ow2.easywsdl.wsdl.impl.wsdl11.OperationImpl(top, this);
			this.operations.add(op);
		}

	}

	public QName getQName() {
		return new QName(this.getDescription().getTargetNamespace(), this.model.getName());
	}

	@Override
	@SuppressWarnings("unchecked")
	public void addOperation(final Operation op) {
		super.addOperation(op);
		this.model.getOperation().add((TOperation) ((AbstractWSDLElementImpl) op).getModel());
	}

	public Operation removeOperation(final String name) {
		throw new NotImplementedException();
	}

	public void setQName(final QName name) {
		this.model.setName(name.getLocalPart());
	}

	public Operation createOperation() {
		return new OperationImpl(new TOperation(), this);
	}

	public static TPortType replaceDOMElementByTPortType(final WSDLElement parent, final Element childToReplace, WSDLReaderImpl reader) throws WSDLException {
		TPortType res = null;
		try {
			if ((childToReplace != null) && ((childToReplace.getLocalName().equals("portType")))) {
				JAXBElement<TPortType> jaxbElement;

				Unmarshaller unmarshaller = WSDLJAXBContext.getInstance().getJaxbContext().createUnmarshaller();

				jaxbElement = unmarshaller.unmarshal(childToReplace, TPortType.class);

				// change element by jaxb element
				((TDefinitions) ((AbstractWSDLElementImpl) parent).getModel()).getAny().remove(childToReplace);
				((TDefinitions) ((AbstractWSDLElementImpl) parent).getModel()).getAnyTopLevelOptionalElement().add(jaxbElement.getValue());

				// get element
				res = jaxbElement.getValue();
			}
		} catch (JAXBException e) {
			throw new WSDLException(e);
		}
		return res;
	}
}
