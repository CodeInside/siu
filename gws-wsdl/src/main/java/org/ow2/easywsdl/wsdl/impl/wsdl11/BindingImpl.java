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
import org.ow2.easywsdl.wsdl.api.BindingOperation;
import org.ow2.easywsdl.wsdl.api.InterfaceType;
import org.ow2.easywsdl.wsdl.api.WSDLElement;
import org.ow2.easywsdl.wsdl.api.WSDLException;
import org.ow2.easywsdl.wsdl.api.abstractElmt.AbstractBindingImpl;
import org.ow2.easywsdl.wsdl.api.abstractElmt.AbstractWSDLElementImpl;
import org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.TBinding;
import org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.TBindingOperation;
import org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.TDefinitions;
import org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.soap.ObjectFactory;
import org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.soap.TStyleChoice;
import org.w3c.dom.Element;

/**
 * @author Nicolas Salatge - eBM WebSourcing
 */
public class BindingImpl extends AbstractBindingImpl<TBinding, InterfaceType, BindingOperation> implements org.ow2.easywsdl.wsdl.api.Binding {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ObjectFactory soap11BindingFactory = new ObjectFactory();

	public BindingImpl(final TBinding binding, final DescriptionImpl desc) {
		super(binding, desc);
		this.desc = desc;

		// get the documentation
		this.documentation = new org.ow2.easywsdl.wsdl.impl.wsdl11.DocumentationImpl(this.model.getDocumentation(), this);

		// get the interface
		final QName itfName = this.model.getType();
		this.itf = (InterfaceType) this.desc.getInterface(itfName);

		// get the binding operations
		for (final TBindingOperation tbo : this.model.getOperation()) {
			final BindingOperation bo = new org.ow2.easywsdl.wsdl.impl.wsdl11.BindingOperationImpl(tbo, this);
			this.bindingOperations.add(bo);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void addBindingOperation(final BindingOperation bindingOperation) {
		super.addBindingOperation(bindingOperation);
		this.model.getOperation().add((TBindingOperation) ((AbstractWSDLElementImpl) bindingOperation).getModel());
	}

	public QName getQName() {
		return new QName(this.desc.getTargetNamespace(), this.model.getName());
	}

	public BindingOperation removeBindingOperation(final String name) {
		throw new NotImplementedException();
	}

	public void setInterface(final InterfaceType interfaceType) {
		this.model.setType(interfaceType.getQName());
		this.itf = interfaceType;
	}

	public void setQName(final QName name) {
		this.model.setName(name.getLocalPart());
	}

	@SuppressWarnings("unchecked")
	public String getTransportProtocol() {
		String protocol = null;
		for (final Object element : this.model.getAny()) {
			if (element instanceof JAXBElement) {
				if (((JAXBElement) element).getValue() instanceof org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.soap.TBinding) {
					protocol = ((org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.soap.TBinding) ((JAXBElement) element).getValue()).getTransport();
					break;
				}
				if (((JAXBElement) element).getValue() instanceof org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.soap12.TBinding) {
					protocol = ((org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.soap12.TBinding) ((JAXBElement) element).getValue()).getTransport();
					break;
				}
				if (((JAXBElement) element).getValue() instanceof org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.http.BindingType) {
					protocol = ((org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.http.BindingType) ((JAXBElement) element).getValue()).getVerb();
					break;
				}
			}
		}
		return protocol;
	}

	public void setTransportProtocol(String transportProtocol) {
		boolean find = false;
		for (final Object element : this.model.getAny()) {
			if (element instanceof JAXBElement) {
				if (((JAXBElement) element).getValue() instanceof org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.soap.TBinding) {
					((org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.soap.TBinding) ((JAXBElement) element).getValue()).setTransport(transportProtocol);
					find = true;
					break;
				}
				if (((JAXBElement) element).getValue() instanceof org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.soap12.TBinding) {
					((org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.soap12.TBinding) ((JAXBElement) element).getValue()).setTransport(transportProtocol);
					find = true;
					break;
				}
				if (((JAXBElement) element).getValue() instanceof org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.http.BindingType) {
					((org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.http.BindingType) ((JAXBElement) element).getValue()).setVerb(transportProtocol);
					find = true;
					break;
				}
			}
		}

		if (!find) {
			// create soap 1.1 transport in document mode by default
			org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.soap.TBinding binding = new org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.soap.TBinding();
			binding.setTransport(transportProtocol);
			binding.setStyle(TStyleChoice.DOCUMENT);

			// add to model
			this.model.getAny().add(this.soap11BindingFactory.createBinding(binding));
		}
	}

	@SuppressWarnings("unchecked")
	public StyleConstant getStyle() {
		StyleConstant style = null;
		for (final Object element : this.model.getAny()) {
			if (((JAXBElement) element).getValue() instanceof org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.soap.TBinding) {
				if (((org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.soap.TBinding) ((JAXBElement) element).getValue()).getStyle() != null) {
					style = StyleConstant.valueOf(((org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.soap.TBinding) ((JAXBElement) element).getValue()).getStyle().value().toUpperCase());
				}
				break;
			}
			if (((JAXBElement) element).getValue() instanceof org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.soap12.TBinding) {
				if (((org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.soap12.TBinding) ((JAXBElement) element).getValue()).getStyle() != null) {
					style = StyleConstant.valueOf(((org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.soap12.TBinding) ((JAXBElement) element).getValue()).getStyle().value().toUpperCase());
				}
				break;
			}
		}
		return style;
	}

	@SuppressWarnings("unchecked")
	public BindingConstants getTypeOfBinding() {
		BindingConstants res = null;
		for (final Object element : this.model.getAny()) {
			if (((JAXBElement) element).getValue() instanceof org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.soap.TBinding) {
				res = BindingConstants.SOAP11_BINDING4WSDL11;
				break;
			}
			if (((JAXBElement) element).getValue() instanceof org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.soap12.TBinding) {
				res = BindingConstants.SOAP12_BINDING4WSDL11;
				break;
			}
			if (((JAXBElement) element).getValue() instanceof org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.http.BindingType) {
				res = BindingConstants.HTTP11_BINDING4WSDL11;
				break;
			}
		}
		return res;
	}

	public String getVersion() {
		String res = null;
		if (this.getTypeOfBinding().equals(BindingConstants.SOAP11_BINDING4WSDL11)) {
			res = "1.1";
		} else if (this.getTypeOfBinding().equals(BindingConstants.SOAP12_BINDING4WSDL11)) {
			res = "1.2";
		} else if (this.getTypeOfBinding().equals(BindingConstants.HTTP11_BINDING4WSDL11)) {
			res = "1.1";
		}
		return res;
	}

	public String getHttpContentEncodingDefault() {
		return null;
	}

	public String getHttpDefaultMethod() {
		return null;
	}

	public String getHttpQueryParameterSeparatorDefault() {
		return null;
	}

	public boolean isHttpCookies() {
		return false;
	}

	public BindingOperation createBindingOperation() {
		return new BindingOperationImpl(new TBindingOperation(), this);
	}

	public static TBinding replaceDOMElementByTBinding(final WSDLElement parent, final Element childToReplace, WSDLReaderImpl reader) throws WSDLException {
		TBinding res = null;
		try {
			if ((childToReplace != null) && ((childToReplace.getLocalName().equals("binding")))) {
				JAXBElement<TBinding> jaxbElement;

				Unmarshaller unmarshaller = WSDLJAXBContext.getInstance().getJaxbContext().createUnmarshaller();

				jaxbElement = unmarshaller.unmarshal(childToReplace, TBinding.class);

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
