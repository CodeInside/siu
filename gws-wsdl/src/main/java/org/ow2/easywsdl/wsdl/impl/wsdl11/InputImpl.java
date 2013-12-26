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

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.ow2.easywsdl.schema.api.Element;
import org.ow2.easywsdl.wsdl.api.Part;
import org.ow2.easywsdl.wsdl.api.WSDLException;
import org.ow2.easywsdl.wsdl.api.abstractElmt.AbstractDescriptionImpl;
import org.ow2.easywsdl.wsdl.api.abstractElmt.AbstractInterfaceTypeImpl;
import org.ow2.easywsdl.wsdl.api.abstractElmt.AbstractOperationImpl;
import org.ow2.easywsdl.wsdl.api.abstractElmt.AbstractParamImpl;
import org.ow2.easywsdl.wsdl.api.abstractElmt.AbstractWSDLElementImpl;
import org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.TDefinitions;
import org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.TMessage;
import org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.TParam;
import org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.TPart;

/**
 * @author Nicolas Salatge - eBM WebSourcing
 */
public class InputImpl extends AbstractParamImpl<TParam> implements
org.ow2.easywsdl.wsdl.api.Input {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private TMessage correspondingMessage = null;

	private List<Part> parts = new ArrayList<Part>();

	@SuppressWarnings("unchecked")
	public InputImpl(final TParam param, final OperationImpl operationImpl) {
		super(param, operationImpl);
		this.operation = operationImpl;

		// get the documentation
		this.documentation = new org.ow2.easywsdl.wsdl.impl.wsdl11.DocumentationImpl(
				this.model.getDocumentation(), this);

		findCorrespondingMessage();
	}

	private void findCorrespondingMessage() {
		// get corresponding messages
		final DescriptionImpl desc = (org.ow2.easywsdl.wsdl.impl.wsdl11.DescriptionImpl) ((AbstractInterfaceTypeImpl) ((AbstractOperationImpl) this.operation)
				.getInterface()).getDescription();
		for (final MessageImpl msg : desc.getMessages()) {
			if ((this.getMessageName() != null)&&(msg.getQName().getLocalPart().equals(this.getMessageName().getLocalPart()))) {
				this.correspondingMessage = (TMessage) ((AbstractWSDLElementImpl)msg).getModel();
			}
		}

		// get parts
		if(this.correspondingMessage != null) {
			this.parts = new ArrayList<Part>();
			for (final TPart part : this.correspondingMessage.getPart()) {
				this.parts.add(new org.ow2.easywsdl.wsdl.impl.wsdl11.PartImpl(part, this));
			}
		}
	}

	public QName getMessageName() {
		return this.model.getMessage();
	}

	public void setMessageName(final QName name) {
		// TODO: Delete unused message
		
		this.model.setMessage(name);
	
		// create corresponding message
		AbstractDescriptionImpl desc = (AbstractDescriptionImpl) ((AbstractInterfaceTypeImpl)((AbstractOperationImpl)this.getOperation()).getInterface()).getDescription();
		this.correspondingMessage = new TMessage();
		this.correspondingMessage.setName(name.getLocalPart());
		((TDefinitions)desc.getModel()).getAnyTopLevelOptionalElement().add(this.correspondingMessage);
	}

	@Override
	public Element getElement() {
		Element res = null;
		if(this.correspondingMessage == null) {
			this.findCorrespondingMessage();
		}
		if (this.parts.size() == 1) {
			res = this.parts.get(0).getElement();
		}
		return res;
	}

	public List<Part> getParts() {
		if(this.correspondingMessage == null) {
			this.findCorrespondingMessage();
		}
		return this.parts;
	}

	public void setElement(final Element element) throws WSDLException {
		if(this.correspondingMessage == null) {
			this.findCorrespondingMessage();
		}
		if(this.correspondingMessage == null) {
			throw new WSDLException("No message is associated to this input");
		}
		
		this.elementName = element.getQName();
		if(this.correspondingMessage.getPart().size() == 0) {
			// create part
			TPart part = new TPart();
			part.setName(element.getQName().getLocalPart());
			part.setElement(element.getQName());

			// add part to list of parts
			this.parts.add(new PartImpl(part, this));
			
			// add part in model
			this.correspondingMessage.getPart().add(part);
			
		} else {
			// set part
			TPart part = this.correspondingMessage.getPart().get(0);
			part.setName(element.getQName().getLocalPart());
			part.setElement(element.getQName());
		}
	}

	public String getName() {
		return this.model.getName();
	}

	public void setName(final String name) {
		this.model.setName(name);
	}

	public Part getPart(final String name) {
		Part res = null;
		if(this.correspondingMessage == null) {
			this.findCorrespondingMessage();
		}
		if (this.parts != null) {
			for (final Part p : this.parts) {
				if (p.getPartQName().getLocalPart().equals(name)) {
					res = p;
					break;
				}
			}
		}
		return res;
	}

}
