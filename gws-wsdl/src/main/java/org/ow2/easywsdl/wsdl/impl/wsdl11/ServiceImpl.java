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

import java.util.logging.Logger;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;

import org.ow2.easywsdl.u.NotImplementedException;
import org.ow2.easywsdl.wsdl.api.Endpoint;
import org.ow2.easywsdl.wsdl.api.InterfaceType;
import org.ow2.easywsdl.wsdl.api.Service;
import org.ow2.easywsdl.wsdl.api.WSDLElement;
import org.ow2.easywsdl.wsdl.api.WSDLException;
import org.ow2.easywsdl.wsdl.api.abstractElmt.AbstractServiceImpl;
import org.ow2.easywsdl.wsdl.api.abstractElmt.AbstractWSDLElementImpl;
import org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.TDefinitions;
import org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.TPort;
import org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.TService;
import org.w3c.dom.Element;

/**
 * @author Nicolas Salatge - eBM WebSourcing
 */
public class ServiceImpl extends AbstractServiceImpl<TService, InterfaceType, Endpoint> implements Service {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOG = Logger.getLogger(ServiceImpl.class.getName());

	public ServiceImpl(final TService service, final DescriptionImpl desc) {
		super(service, desc);
		this.desc = desc;

		// get the documentation
		this.documentation = new org.ow2.easywsdl.wsdl.impl.wsdl11.DocumentationImpl(this.model.getDocumentation(), this);

		// get the endpoint
		for (final TPort port : this.model.getPort()) {
			final Endpoint e = new org.ow2.easywsdl.wsdl.impl.wsdl11.EndpointImpl(port, this);
			this.endpoints.add(e);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void addEndpoint(final Endpoint endpoint) {
		super.addEndpoint(endpoint);
		this.model.getPort().add((TPort) ((AbstractWSDLElementImpl) endpoint).getModel());
	}

	public QName getQName() {
		QName serviceName = null;
		serviceName = new QName(this.desc.getTargetNamespace(), this.model.getName());
		return serviceName;
	}

	public Endpoint removeEndpoint(final String name) {
		throw new NotImplementedException();
	}

	public void setQName(final QName name) {
		this.model.setName(name.getLocalPart());
	}

	/**
     * {@inheritDoc}
     */
    public InterfaceType getInterface() throws WSDLException {
		InterfaceType res = null;
		InterfaceType item = null;
		for (final Endpoint ep : this.getEndpoints()) {
			item = ep.getBinding().getInterface();
			if (item != null) {
			    if (res == null) {
			        res = item;
			    } else if (res != item) {
			        throw new WSDLException(
			                "WSDL 1.1: The endpoints of this service do not implement the same interface");
			    }
                // else 'res' is the same instance as 'item', so the current
                // endpoint implements the same interface as the previous
                // ones.
			}
		}
		return res;
	}

	public Endpoint createEndpoint() {
		return new EndpointImpl(new TPort(), this);
	}

	public void setInterface(InterfaceType itf) {
		LOG.warning("Operation setInterface not supported");
	}

	public static TService replaceDOMElementByTService(final WSDLElement parent, final Element childToReplace, WSDLReaderImpl reader) throws WSDLException {
		TService res = null;
		try {
			if ((childToReplace != null) && ((childToReplace.getLocalName().equals("service")))) {
				JAXBElement<TService> jaxbElement;

				Unmarshaller unmarshaller = WSDLJAXBContext.getInstance().getJaxbContext().createUnmarshaller();

				jaxbElement = unmarshaller.unmarshal(childToReplace, TService.class);

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
