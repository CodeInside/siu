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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.ow2.easywsdl.u.NotImplementedException;
import org.ow2.easywsdl.schema.api.XmlException;
import org.ow2.easywsdl.schema.api.absItf.AbsItfSchema;
import org.ow2.easywsdl.schema.api.extensions.NamespaceMapperImpl;
import org.ow2.easywsdl.schema.api.extensions.SchemaLocatorImpl;
import org.ow2.easywsdl.wsdl.api.Binding;
import org.ow2.easywsdl.wsdl.api.BindingFault;
import org.ow2.easywsdl.wsdl.api.BindingInput;
import org.ow2.easywsdl.wsdl.api.BindingOperation;
import org.ow2.easywsdl.wsdl.api.BindingOutput;
import org.ow2.easywsdl.wsdl.api.Description;
import org.ow2.easywsdl.wsdl.api.Endpoint;
import org.ow2.easywsdl.wsdl.api.Fault;
import org.ow2.easywsdl.wsdl.api.Import;
import org.ow2.easywsdl.wsdl.api.Include;
import org.ow2.easywsdl.wsdl.api.InterfaceType;
import org.ow2.easywsdl.wsdl.api.Operation;
import org.ow2.easywsdl.wsdl.api.Service;
import org.ow2.easywsdl.wsdl.api.Types;
import org.ow2.easywsdl.wsdl.api.WSDLException;
import org.ow2.easywsdl.wsdl.api.WSDLImportException;
import org.ow2.easywsdl.wsdl.api.WSDLReader.FeatureConstants;
import org.ow2.easywsdl.wsdl.api.abstractElmt.AbstractDescriptionImpl;
import org.ow2.easywsdl.wsdl.api.abstractElmt.AbstractInterfaceTypeImpl;
import org.ow2.easywsdl.wsdl.api.abstractElmt.AbstractWSDLElementImpl;
import org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfDescription;
import org.ow2.easywsdl.wsdl.api.binding.BindingProtocol.SOAPMEPConstants;
import org.ow2.easywsdl.wsdl.org.w3.ns.wsdl.BindingType;
import org.ow2.easywsdl.wsdl.org.w3.ns.wsdl.DescriptionType;
import org.ow2.easywsdl.wsdl.org.w3.ns.wsdl.ImportType;
import org.ow2.easywsdl.wsdl.org.w3.ns.wsdl.IncludeType;
import org.ow2.easywsdl.wsdl.org.w3.ns.wsdl.ObjectFactory;
import org.ow2.easywsdl.wsdl.org.w3.ns.wsdl.ServiceType;
import org.ow2.easywsdl.wsdl.org.w3.ns.wsdl.TypesType;
import org.w3c.dom.Element;

/**
 * @author Nicolas Salatge - eBM WebSourcing
 */
public class DescriptionImpl extends AbstractDescriptionImpl<DescriptionType, Service, Endpoint, Binding, InterfaceType, Include, Import, Types> implements Description {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOG = Logger.getLogger(DescriptionImpl.class.getName());

	private ObjectFactory factory = new ObjectFactory();
	
	private WSDLReaderImpl reader = null;

	@SuppressWarnings("unchecked")
	public DescriptionImpl(final URI baseURI, final DescriptionType description, final NamespaceMapperImpl namespaceMapper, final SchemaLocatorImpl schemaLocator, final Map<FeatureConstants, Object> features, Map<URI, AbsItfDescription> descriptionImports, Map<URI, AbsItfSchema> schemaImports, WSDLReaderImpl reader)
			throws WSDLException, WSDLImportException {
		super(baseURI, description, namespaceMapper, schemaLocator, features);

		// get the documentation
		this.documentation = new org.ow2.easywsdl.wsdl.impl.wsdl20.DocumentationImpl(this.model.getDocumentation(), this);

		// get the reader
		if(reader == null) {
			this.reader = new WSDLReaderImpl();
		} else {
			this.reader = reader;
		}
		
		boolean find = false;
		for (final Object element : this.model.getImportOrIncludeOrTypes()) {

			if (element instanceof JAXBElement) {
				final Object part = ((JAXBElement) element).getValue();

				// get imports
				if (part instanceof ImportType) {
					final Import impt = new org.ow2.easywsdl.wsdl.impl.wsdl20.ImportImpl((ImportType) part, this, descriptionImports, schemaImports, this.documentURI, this.reader);
					this.imports.add(impt);
					find = true;
				}
			}
		}
		if (!find) {
			Iterator<Object> it = this.model.getImportOrIncludeOrTypes().iterator();
			while (it.hasNext()) {
				Object part = it.next();

				// get import
				if (part instanceof Element) {
					if ((((Element) part).getLocalName().equals("import")) && (((Element) part).getNamespaceURI().equals(Constants.WSDL_20_NAMESPACE))) {
						ImportType tImpt = ImportImpl.replaceDOMElementByImportType(this, (Element) part, this.reader);
						it = this.model.getImportOrIncludeOrTypes().iterator();
						final Import impt = new org.ow2.easywsdl.wsdl.impl.wsdl20.ImportImpl((ImportType) tImpt, this, descriptionImports, schemaImports, this.documentURI, this.reader);
						this.imports.add(impt);
					}
				}
			}
		}

		find = false;
		for (final Object element : this.model.getImportOrIncludeOrTypes()) {

			if (element instanceof JAXBElement) {
				final Object part = ((JAXBElement) element).getValue();
				// get includes
				if (part instanceof IncludeType) {
					final Include incl = new org.ow2.easywsdl.wsdl.impl.wsdl20.IncludeImpl((IncludeType) part, this, descriptionImports, schemaImports, this.documentURI, this.reader);
					this.includes.add(incl);
					find = true;
				}
			}
		}
		if (!find) {
			Iterator<Object> it = this.model.getImportOrIncludeOrTypes().iterator();
			while (it.hasNext()) {
				Object part = it.next();

				// get import
				if (part instanceof Element) {
					if ((((Element) part).getLocalName().equals("include")) && (((Element) part).getNamespaceURI().equals(Constants.WSDL_20_NAMESPACE))) {
						if(reader == null) {
							reader = new WSDLReaderImpl();
						}
						IncludeType tIncl = IncludeImpl.replaceDOMElementByIncludeType(this, (Element) part, reader);
						it = this.model.getImportOrIncludeOrTypes().iterator();
						final Include incl = new org.ow2.easywsdl.wsdl.impl.wsdl20.IncludeImpl((IncludeType) tIncl, this, descriptionImports, schemaImports, this.documentURI, this.reader);
						this.includes.add(incl);
					}
				}
			}
		}

		this.addImportElementsInAllList();
		this.addIncludeElementsInAllList();

		find = false;
		for (final Object element : this.model.getImportOrIncludeOrTypes()) {

			if (element instanceof JAXBElement) {
				final Object part = ((JAXBElement) element).getValue();
				// get types
				if (part instanceof org.ow2.easywsdl.wsdl.org.w3.ns.wsdl.TypesType) {
					this.types = new org.ow2.easywsdl.wsdl.impl.wsdl20.TypesImpl((org.ow2.easywsdl.wsdl.org.w3.ns.wsdl.TypesType) part, this, schemaImports, this.reader);
					find = true;
				}
			}
		}
		if (!find) {
			Iterator<Object> it = this.model.getImportOrIncludeOrTypes().iterator();
			while (it.hasNext()) {
				Object part = it.next();

				// get import
				if (part instanceof Element) {
					if ((((Element) part).getLocalName().equals("types")) && (((Element) part).getNamespaceURI().equals(Constants.WSDL_20_NAMESPACE))) {
						if(reader == null) {
							reader = new WSDLReaderImpl();
						}
						TypesType tTypes = TypesImpl.replaceDOMElementByTypesType(this, (Element) part, reader);
						it = this.model.getImportOrIncludeOrTypes().iterator();
						this.types = new org.ow2.easywsdl.wsdl.impl.wsdl20.TypesImpl((org.ow2.easywsdl.wsdl.org.w3.ns.wsdl.TypesType) tTypes, this, schemaImports, this.reader);
					}
				}
			}
		}

		find = false;
		for (final Object element : this.model.getImportOrIncludeOrTypes()) {

			if (element instanceof JAXBElement) {
				final Object part = ((JAXBElement) element).getValue();
				// get interfaces
				if (part instanceof org.ow2.easywsdl.wsdl.org.w3.ns.wsdl.InterfaceType) {
					final InterfaceType itf = new org.ow2.easywsdl.wsdl.impl.wsdl20.InterfaceTypeImpl((org.ow2.easywsdl.wsdl.org.w3.ns.wsdl.InterfaceType) part, this);
					this.interfaces.add(itf);
					find = true;
				}
			}
		}
		if (!find) {
			Iterator<Object> it = this.model.getImportOrIncludeOrTypes().iterator();
			while (it.hasNext()) {
				Object part = it.next();

				// get import
				if (part instanceof Element) {
					if ((((Element) part).getLocalName().equals("interface")) && (((Element) part).getNamespaceURI().equals(Constants.WSDL_20_NAMESPACE))) {
						if(reader == null) {
							reader = new WSDLReaderImpl();
						}
						InterfaceType tItf = InterfaceTypeImpl.replaceDOMElementByInterfaceType(this, (Element) part, reader);
						it = this.model.getImportOrIncludeOrTypes().iterator();
						final InterfaceType itf = new org.ow2.easywsdl.wsdl.impl.wsdl20.InterfaceTypeImpl((org.ow2.easywsdl.wsdl.org.w3.ns.wsdl.InterfaceType) tItf, this);
						this.interfaces.add(itf);
					}
				}
			}
		}

		find = false;
		for (final Object element : this.model.getImportOrIncludeOrTypes()) {

			if (element instanceof JAXBElement) {
				final Object part = ((JAXBElement) element).getValue();
				// get bindings
				if (part instanceof BindingType) {
					final Binding b = new org.ow2.easywsdl.wsdl.impl.wsdl20.BindingImpl((BindingType) part, this);
					this.bindings.add(b);
					find = true;
				}
			}
		}
		if (!find) {
			Iterator<Object> it = this.model.getImportOrIncludeOrTypes().iterator();
			while (it.hasNext()) {
				Object part = it.next();

				// get import
				if (part instanceof Element) {
					if ((((Element) part).getLocalName().equals("binding")) && (((Element) part).getNamespaceURI().equals(Constants.WSDL_20_NAMESPACE))) {
						if(reader == null) {
							reader = new WSDLReaderImpl();
						}
						BindingType tBinding = BindingImpl.replaceDOMElementByBindingType(this, (Element) part, reader);
						it = this.model.getImportOrIncludeOrTypes().iterator();
						final Binding b = new org.ow2.easywsdl.wsdl.impl.wsdl20.BindingImpl((BindingType) tBinding, this);
						this.bindings.add(b);
					}
				}
			}
		}

		find = false;
		for (final Object element : this.model.getImportOrIncludeOrTypes()) {

			if (element instanceof JAXBElement) {
				final Object part = ((JAXBElement) element).getValue();
				// get services
				if (part instanceof ServiceType) {
					final Service s = new org.ow2.easywsdl.wsdl.impl.wsdl20.ServiceImpl((ServiceType) part, this);
					this.services.add(s);
					find = true;
				}
			}
		}
		if (!find) {
			Iterator<Object> it = this.model.getImportOrIncludeOrTypes().iterator();
			while (it.hasNext()) {
				Object part = it.next();

				// get import
				if (part instanceof Element) {
					if ((((Element) part).getLocalName().equals("service")) && (((Element) part).getNamespaceURI().equals(Constants.WSDL_20_NAMESPACE))) {
						if(reader == null) {
							reader = new WSDLReaderImpl();
						}
						ServiceType tService = ServiceImpl.replaceDOMElementByServiceType(this, (Element) part, reader);
						it = this.model.getImportOrIncludeOrTypes().iterator();
						final Service s = new org.ow2.easywsdl.wsdl.impl.wsdl20.ServiceImpl((ServiceType) tService, this);
						this.services.add(s);
					}
				}
			}
		}
	}

	public DescriptionImpl() throws WSDLException {
		try {
			this.model = new DescriptionType();
			this.documentURI = new URI(".");

			// get the documentation
			this.documentation = new org.ow2.easywsdl.wsdl.impl.wsdl20.DocumentationImpl(this.model.getDocumentation(), this);
		} catch (URISyntaxException e) {
			throw new WSDLException(e);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void addBinding(final Binding binding) {
		JAXBElement<BindingType> b = factory.createBinding((BindingType) ((AbstractWSDLElementImpl) binding).getModel());
		super.addBinding(binding);
		this.model.getImportOrIncludeOrTypes().add(b);
	}

	@Override
	public void addImport(final Import importDef) {
		JAXBElement<ImportType> impt = factory.createImport((ImportType) ((AbstractWSDLElementImpl) importDef).getModel());
		super.addImport(importDef);
		this.model.getImportOrIncludeOrTypes().add(impt);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void addInterface(final InterfaceType interfaceType) {
		JAXBElement<org.ow2.easywsdl.wsdl.org.w3.ns.wsdl.InterfaceType> itf = factory.createInterface(((org.ow2.easywsdl.wsdl.org.w3.ns.wsdl.InterfaceType) ((AbstractWSDLElementImpl) interfaceType).getModel()));
		super.addInterface(interfaceType);
		this.model.getImportOrIncludeOrTypes().add(itf);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void addService(final Service service) {
		JAXBElement<ServiceType> s = factory.createService((ServiceType) ((AbstractWSDLElementImpl) service).getModel());
		super.addService(service);
		this.model.getImportOrIncludeOrTypes().add(s);
	}

	public Binding createBinding() {
		return new BindingImpl(new BindingType(), this);
	}

	public Import createImport() throws WSDLException, WSDLImportException {
		return new ImportImpl(new ImportType(), this, null, null, this.documentURI, this.reader);
	}

	public InterfaceType createInterface() {
		return new InterfaceTypeImpl(new org.ow2.easywsdl.wsdl.org.w3.ns.wsdl.InterfaceType(), this);
	}

	public Service createService() {
		return new ServiceImpl(new ServiceType(), this);
	}

	@Override
	public void setTypes(Types types) {
		super.setTypes(types);
		JAXBElement<org.ow2.easywsdl.wsdl.org.w3.ns.wsdl.TypesType> jaxbTypes = factory.createTypes(((org.ow2.easywsdl.wsdl.org.w3.ns.wsdl.TypesType) ((AbstractWSDLElementImpl) types).getModel()));
		this.model.getImportOrIncludeOrTypes().add(jaxbTypes);
	}

	public Types createTypes() {
		Types res = null;
		try {
			res = new TypesImpl(new TypesType(), this, null, this.reader);
		} catch (WSDLException e) {
			// Do nothing
			e.printStackTrace();
		}
		return res;
	}

	public QName getQName() throws WSDLException {
		return null;
	}

	public String getTargetNamespace() {
		return this.model.getTargetNamespace();
	}

	public Binding removeBinding(final QName name) {
		throw new NotImplementedException();
	}

	public Import removeImport(final Import importDef) {
		throw new NotImplementedException();
	}

	public String removeNamespace(final String prefix) {
		throw new NotImplementedException();
	}

	public InterfaceType removeInterface(final QName name) {
		throw new NotImplementedException();
	}

	public Service removeService(final QName name) {
		throw new NotImplementedException();
	}

	public void setQName(final QName name) throws WSDLException {
		LOG.warning("Do nothing: No name in description");
	}

	public void setTargetNamespace(final String targetNamespace) {
		this.model.setTargetNamespace(targetNamespace);
	}

	public Include removeInclude(final Include includeDef) {
		throw new NotImplementedException();
	}

	public WSDLVersionConstants getVersion() {
		return WSDLVersionConstants.WSDL20;
	}

	@Override
	public List<Element> getOtherElements() throws XmlException {
		final List<Element> res = super.getOtherElements();

		for (final Object item : this.model.getImportOrIncludeOrTypes()) {
			if (item instanceof Element) {
				res.add((Element) item);
			}
		}

		return res;
	}

	public Binding createDefaultSoapBinding(String bindingName, Endpoint endpoint, InterfaceType itf) {
		// create binding
		Binding binding = (Binding) ((AbstractInterfaceTypeImpl) itf).getDescription().createBinding();
		binding.setQName(new QName(((AbstractInterfaceTypeImpl) itf).getDescription().getTargetNamespace(), bindingName));
		binding.setInterface(itf);
		binding.setTransportProtocol(Constants.HTTP_WWW_W3_ORG_2003_05_SOAP_BINDINGS_HTTP);

		for (Operation operation : itf.getOperations()) {
			BindingOperation bindingOperation = binding.createBindingOperation();
			bindingOperation.setQName(operation.getQName());

			if (operation.getOutput() != null) {
				bindingOperation.setMEP(SOAPMEPConstants.REQUEST_RESPONSE);
			} else {
				bindingOperation.setMEP(SOAPMEPConstants.ONE_WAY);
			}
			bindingOperation.setSoapAction(operation.getQName().getNamespaceURI()
					+ (operation.getQName().getNamespaceURI().endsWith(
							"/") ? "" : "/")
					+ operation.getQName().getLocalPart());

			// input
			if (operation.getInput() != null) {
				BindingInput binput = bindingOperation.createInput();
				bindingOperation.setInput(binput);

			}

			// output
			if (operation.getOutput() != null) {
				BindingOutput boutput = bindingOperation.createOutput();
				bindingOperation.setOutput(boutput);

			}

			// fault
			if (operation.getFaults() != null) {
				for (Fault faultop : operation.getFaults()) {
					BindingFault bfault = bindingOperation.createFault();
					bindingOperation.addFault(bfault);
					bfault.setRef(faultop.getElement().getQName());
				}
			}

			binding.addBindingOperation(bindingOperation);
		}

		// set the binding to endpoint
		endpoint.setBinding(binding);

		return binding;
	}

	@Override
	public void addOtherElements(Element elmt) {
		this.model.getImportOrIncludeOrTypes().add(elmt);
	}
}
