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

import java.util.List;
import java.util.logging.Logger;

import javax.xml.namespace.QName;

import org.ow2.easywsdl.schema.api.Element;
import org.ow2.easywsdl.schema.api.XmlException;
import org.ow2.easywsdl.wsdl.api.Part;
import org.ow2.easywsdl.wsdl.api.WSDLException;
import org.ow2.easywsdl.wsdl.api.abstractElmt.AbstractInterfaceTypeImpl;
import org.ow2.easywsdl.wsdl.api.abstractElmt.AbstractOperationImpl;
import org.ow2.easywsdl.wsdl.api.abstractElmt.AbstractParamImpl;
import org.ow2.easywsdl.wsdl.org.w3.ns.wsdl.MessageRefType;
import org.ow2.easywsdl.wsdl.util.Util;

/**
 * @author Nicolas Salatge - eBM WebSourcing
 */
public class OutputImpl extends AbstractParamImpl<MessageRefType> implements
org.ow2.easywsdl.wsdl.api.Output {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOG = Logger.getLogger(OutputImpl.class.getName());

	@SuppressWarnings("unchecked")
	public OutputImpl(final MessageRefType param, final OperationImpl operationImpl) {
		super(param, operationImpl);
		this.operation = operationImpl;

		// get the documentation
		this.documentation = new org.ow2.easywsdl.wsdl.impl.wsdl20.DocumentationImpl(
				this.model.getDocumentation(), this);

		// get element name
		if(param.getElement() != null) {
			String ns = null;
			QName elmt = QName.valueOf(param.getElement());
			if(elmt.getNamespaceURI() != null && elmt.getNamespaceURI().trim().length() > 0) {
				String prefix = null;
				if(elmt.getPrefix() == null || elmt.getPrefix().length() > 0) {
					prefix = ((AbstractInterfaceTypeImpl) ((AbstractOperationImpl) operationImpl)
							.getInterface()).getDescription().getNamespaces().getPrefix(elmt.getNamespaceURI());

				} else {
					prefix = elmt.getPrefix();
				}
				this.elementName = new QName(elmt.getNamespaceURI(), elmt.getLocalPart(), prefix);
			} else {
				ns = ((AbstractInterfaceTypeImpl) ((AbstractOperationImpl) operationImpl)
						.getInterface()).getDescription().getNamespaces().getNamespaceURI(
								Util.getPrefix(param.getElement()));
				this.elementName = new QName(ns, Util.getLocalPartWithoutPrefix(param.getElement()));
			}
		}
	}

	@SuppressWarnings("unchecked")
	public QName getMessageName() {
		QName res = null;
		if (this.model.getMessageLabel() != null) {
			res = new QName(((AbstractInterfaceTypeImpl) ((AbstractOperationImpl) this
					.getOperation()).getInterface()).getDescription().getTargetNamespace(),
					this.model.getMessageLabel());
		} else {
			res = new QName(((AbstractInterfaceTypeImpl) ((AbstractOperationImpl) this
					.getOperation()).getInterface()).getDescription().getTargetNamespace(), "Out");
		}
		return res;
	}

	public void setMessageName(final QName name) {
		this.model.setMessageLabel(name.getLocalPart());
	}

	public List<Part> getParts() {
		return null;
	}

	public void setElement(final Element element) {
		this.elementName = element.getQName();
		String elmt = null;
		String prefix = null;
		if(element.getQName().getPrefix() == null || element.getQName().getPrefix().trim().length() == 0) {
			prefix = ((AbstractInterfaceTypeImpl) ((AbstractOperationImpl) this.operation)
					.getInterface()).getDescription().getNamespaces().getPrefix(element.getQName().getNamespaceURI());
		} else {
			prefix = element.getQName().getPrefix();
		}

		if(prefix != null) {
			elmt = prefix + ":" + element.getQName().getLocalPart();
		} else {
			elmt = element.getQName().getLocalPart();
		}

		this.model.setElement(elmt);
	}

	public String getName() {
		return null;
	}

	public void setName(final String name) throws WSDLException {
		LOG.warning("Do nothing: No name in output in wsdl 2.0");
	}

	public Part getPart(final String name) {
		return null;
	}

	@Override
	public List<org.w3c.dom.Element> getOtherElements() throws XmlException {
		final List<org.w3c.dom.Element> res = super.getOtherElements();

		for (final Object item : this.model.getAny()) {
			if (item instanceof org.w3c.dom.Element) {
				res.add((org.w3c.dom.Element) item);
			}
		}

		return res;
	}
}
