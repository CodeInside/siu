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

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;

import org.ow2.easywsdl.wsdl.api.Description;
import org.ow2.easywsdl.wsdl.api.Part;
import org.ow2.easywsdl.wsdl.api.WSDLElement;
import org.ow2.easywsdl.wsdl.api.WSDLException;
import org.ow2.easywsdl.wsdl.api.abstractElmt.AbstractWSDLElementImpl;
import org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.TDefinitions;
import org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.TMessage;
import org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.TPart;
import org.w3c.dom.Element;

public class MessageImpl extends AbstractWSDLElementImpl<TMessage> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Description description;

	private List<Part> parts = new ArrayList<Part>();

	public MessageImpl(TMessage msg, Description parent) {
		super(msg, (AbstractWSDLElementImpl) parent);
		this.description = parent;

		if(this.model.getPart() != null) {
			for (TPart part : this.model.getPart()) {
				parts.add(new PartImpl(part, this));
			}
		}
	}

	public List<Part> getParts() {
		return this.parts;
	}

	public Part getPart(QName partName) {
		Part res = null;
		for (Part part : this.parts) {
			if ((part.getPartQName().getLocalPart().equals(partName.getLocalPart())) && (part.getPartQName().getNamespaceURI().equals(partName.getNamespaceURI()))) {
				res = part;
				break;
			}
		}
		return res;
	}

	public QName getQName() {
		return new QName(this.description.getTargetNamespace(), this.model.getName());
	}

	public Description getDescription() {
		return description;
	}

	public static TMessage replaceDOMElementByTMessage(final WSDLElement parent, final Element childToReplace, WSDLReaderImpl reader) throws WSDLException {
		TMessage res = null;
		try {
			if ((childToReplace != null) && ((childToReplace.getLocalName().equals("message")))) {
				JAXBElement<TMessage> jaxbElement;

				Unmarshaller unmarshaller = WSDLJAXBContext.getInstance().getJaxbContext().createUnmarshaller();

				jaxbElement = unmarshaller.unmarshal(childToReplace, TMessage.class);

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
