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
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;

import org.ow2.easywsdl.u.NotImplementedException;
import org.ow2.easywsdl.schema.api.XmlException;
import org.ow2.easywsdl.wsdl.api.BindingOperation;
import org.ow2.easywsdl.wsdl.api.InterfaceType;
import org.ow2.easywsdl.wsdl.api.WSDLElement;
import org.ow2.easywsdl.wsdl.api.WSDLException;
import org.ow2.easywsdl.wsdl.api.abstractElmt.AbstractBindingImpl;
import org.ow2.easywsdl.wsdl.api.abstractElmt.AbstractWSDLElementImpl;
import org.ow2.easywsdl.wsdl.org.w3.ns.wsdl.BindingOperationType;
import org.ow2.easywsdl.wsdl.org.w3.ns.wsdl.BindingType;
import org.ow2.easywsdl.wsdl.org.w3.ns.wsdl.DescriptionType;
import org.ow2.easywsdl.wsdl.org.w3.ns.wsdl.ObjectFactory;
import org.w3c.dom.Element;

/**
 * @author Nicolas Salatge - eBM WebSourcing
 */
public class BindingImpl extends AbstractBindingImpl<BindingType, InterfaceType, BindingOperation> implements org.ow2.easywsdl.wsdl.api.Binding {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOG = Logger.getLogger(BindingImpl.class.getName());

	private ObjectFactory factory = new ObjectFactory();

	@SuppressWarnings("unchecked")
	public BindingImpl(final BindingType binding, final DescriptionImpl desc) {
		super(binding, desc);
		this.desc = desc;

		// get the documentation
		this.documentation = new org.ow2.easywsdl.wsdl.impl.wsdl20.DocumentationImpl(this.model.getDocumentation(), this);

		// get the interface
		final QName itfName = this.model.getInterface();
		this.itf = (InterfaceType) this.desc.getInterface(itfName);

		// get the binding operation
		for (final Object element : this.model.getOperationOrFaultOrAny()) {

			if (element instanceof JAXBElement) {
				final Object part = ((JAXBElement) element).getValue();

				// get binding operation
				if (part instanceof BindingOperationType) {
					final BindingOperation bo = new org.ow2.easywsdl.wsdl.impl.wsdl20.BindingOperationImpl((BindingOperationType) part, this);
					this.bindingOperations.add(bo);
				}
			}
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void addBindingOperation(final BindingOperation bindingOperation) {
		JAXBElement<BindingOperationType> bo = factory.createBindingTypeOperation((BindingOperationType) ((AbstractWSDLElementImpl) bindingOperation).getModel());
		super.addBindingOperation(bindingOperation);
		this.model.getOperationOrFaultOrAny().add(bo);
	}

	public QName getQName() {
		return new QName(this.desc.getTargetNamespace(), this.model.getName());
	}

	public BindingOperation removeBindingOperation(final String name) {
		throw new NotImplementedException();
	}

	public void setInterface(final InterfaceType interfaceType) {
		this.model.setInterface(interfaceType.getQName());
		this.itf = interfaceType;
	}

	public void setQName(final QName name) {
		this.model.setName(name.getLocalPart());
	}

	public String getTransportProtocol() {
		String protocol = null;
		for (final Entry<QName, String> attribute : this.model.getOtherAttributes().entrySet()) {
			if ((attribute.getKey().getLocalPart().equals(Constants.SOAP_PROTOCOL)) && (attribute.getKey().getNamespaceURI().equals(org.ow2.easywsdl.wsdl.api.Binding.BindingConstants.SOAP_BINDING4WSDL20.value().toString()))) {
				protocol = attribute.getValue();
				break;
			} else if (((attribute.getKey().getLocalPart().equals(Constants.HTTP_METHOD)) || (attribute.getKey().getLocalPart().equals(Constants.HTTP_METHOD_DEFAULT))) && ((attribute.getKey().getNamespaceURI().equals(org.ow2.easywsdl.wsdl.api.Binding.BindingConstants.HTTP_BINDING4WSDL20.value().toString())))) {
				protocol = attribute.getValue();
				break;
			}
		}
		return protocol;
	}

	public void setTransportProtocol(String transportProtocol) {
		if (transportProtocol.contains("soap")) {
			this.model.setType(org.ow2.easywsdl.wsdl.api.Binding.BindingConstants.SOAP_BINDING4WSDL20.value().toString());
			this.model.getOtherAttributes().put(new QName(this.model.getType(), Constants.SOAP_PROTOCOL), transportProtocol);
		} else if (transportProtocol.contains("http")) {
			this.model.setType(org.ow2.easywsdl.wsdl.api.Binding.BindingConstants.HTTP_BINDING4WSDL20.value().toString());
			this.model.getOtherAttributes().put(new QName(this.model.getType(), Constants.HTTP_METHOD_DEFAULT), transportProtocol);
		} else {
			this.LOG.severe("unrecognized transport protocol");
		}
	}

	public StyleConstant getStyle() {
		return StyleConstant.DOCUMENT;
	}

	public BindingConstants getTypeOfBinding() {
		BindingConstants res = null;
		try {
			if (this.model.getType() != null) {
				res = BindingConstants.valueOf(new URI(this.model.getType()));
			}
		} catch (final URISyntaxException e) {
			BindingImpl.LOG.warning("The binding type is unknown");
		}
		return res;
	}

	public String getVersion() {
		String res = null;
		res = this.model.getOtherAttributes().get(new QName(org.ow2.easywsdl.wsdl.api.Binding.BindingConstants.HTTP_BINDING4WSDL20.value().toString(), "version"));
		if (res == null) {
			res = this.model.getOtherAttributes().get(new QName(org.ow2.easywsdl.wsdl.api.Binding.BindingConstants.SOAP_BINDING4WSDL20.value().toString(), "version"));
		}
		return res;
	}

	public String getHttpContentEncodingDefault() {
		return this.model.getOtherAttributes().get(new QName(org.ow2.easywsdl.wsdl.api.Binding.BindingConstants.HTTP_BINDING4WSDL20.value().toString(), "contentEncodingDefault"));
	}

	public String getHttpDefaultMethod() {
		return this.model.getOtherAttributes().get(new QName(org.ow2.easywsdl.wsdl.api.Binding.BindingConstants.HTTP_BINDING4WSDL20.value().toString(), "methodDefault"));
	}

	public String getHttpQueryParameterSeparatorDefault() {
		return this.model.getOtherAttributes().get(new QName(org.ow2.easywsdl.wsdl.api.Binding.BindingConstants.HTTP_BINDING4WSDL20.value().toString(), "queryParameterSeparatorDefault"));
	}

	public boolean isHttpCookies() {
		return Boolean.valueOf(this.model.getOtherAttributes().get(new QName(org.ow2.easywsdl.wsdl.api.Binding.BindingConstants.HTTP_BINDING4WSDL20.value().toString(), "cookies")));
	}

	@Override
	public List<Element> getOtherElements() throws XmlException {
		final List<Element> res = super.getOtherElements();

		for (final Object item : this.model.getOperationOrFaultOrAny()) {
			if (item instanceof Element) {
				res.add((Element) item);
			}
		}

		return res;
	}

	public BindingOperation createBindingOperation() {
		return new BindingOperationImpl(new BindingOperationType(), this);
	}

	public static BindingType replaceDOMElementByBindingType(final WSDLElement parent, final Element childToReplace, WSDLReaderImpl reader) throws WSDLException {
		BindingType res = null;
		try {
			if ((childToReplace != null) && ((childToReplace.getLocalName().equals("binding")) && (childToReplace.getNamespaceURI().equals(Constants.WSDL_20_NAMESPACE)))) {
				JAXBElement<BindingType> jaxbElement;

				Unmarshaller unmarshaller = WSDLJAXBContext.getInstance().getJaxbContext().createUnmarshaller();

				jaxbElement = unmarshaller.unmarshal(childToReplace, BindingType.class);

				// change element by jaxb element
				((DescriptionType) ((AbstractWSDLElementImpl) parent).getModel()).getImportOrIncludeOrTypes().remove(childToReplace);
				((DescriptionType) ((AbstractWSDLElementImpl) parent).getModel()).getImportOrIncludeOrTypes().add(jaxbElement.getValue());

				// get element
				res = jaxbElement.getValue();
			}
		} catch (JAXBException e) {
			throw new WSDLException(e);
		}
		return res;
	}
}
