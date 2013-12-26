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
package org.ow2.easywsdl.wsdl.api.abstractElmt;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.ow2.easywsdl.schema.api.extensions.NamespaceMapperImpl;
import org.ow2.easywsdl.schema.api.extensions.SchemaLocatorImpl;
import org.ow2.easywsdl.wsdl.api.WSDLException;
import org.ow2.easywsdl.wsdl.api.WSDLReader.FeatureConstants;
import org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfBinding;
import org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfDescription;
import org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfEndpoint;
import org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfInterfaceType;
import org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfOperation;
import org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfService;

/**
 * @author Nicolas Salatge - eBM WebSourcing
 */
@SuppressWarnings("unchecked")
public abstract class AbstractDescriptionImpl<E, S extends AbsItfService<I, ? extends AbsItfEndpoint>, Ep extends AbsItfEndpoint, B extends AbsItfBinding, I extends AbsItfInterfaceType<? extends AbsItfOperation>, Incl extends org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfInclude<? extends AbsItfDescription>, Impt extends org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfImport<? extends AbsItfDescription>, T extends org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfTypes>
		extends AbstractWSDLElementImpl<E> implements AbsItfDescription<S, Ep, B, I, Incl, Impt, T> {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Features
	 */
	protected Map<FeatureConstants, Object> features = new HashMap<FeatureConstants, Object>();

	/**
	 * the namespace context
	 */
	protected NamespaceMapperImpl namespaceMapper = new NamespaceMapperImpl();

	/**
	 * list of imports
	 */
	protected List<Impt> imports = new ArrayList<Impt>();

	/**
	 * list of includes
	 */
	protected List<Incl> includes = new ArrayList<Incl>();

	/**
	 * list of services
	 */
	protected List<S> services = new ArrayList<S>();

	/**
	 * list of binding
	 */
	protected List<B> bindings = new ArrayList<B>();

	/**
	 * list of interface
	 */
	protected List<I> interfaces = new ArrayList<I>();

	/**
	 * types
	 */
	protected T types;

	/**
	 * baseURI
	 */
	protected URI documentURI;

	/**
	 * Schema Location management
	 */
	private SchemaLocatorImpl schemaLocator;

	/**
	 * @param internalObject
	 * @param doc
	 */
	public AbstractDescriptionImpl(final URI baseURI, final E model, final NamespaceMapperImpl namespaceMapper, final SchemaLocatorImpl schemaLocator, final Map<FeatureConstants, Object> features) throws WSDLException {
		super(model, null);
		this.features = features;
		this.namespaceMapper = namespaceMapper;
		this.schemaLocator = schemaLocator;
		this.documentURI = baseURI;
	}

	public AbstractDescriptionImpl() throws WSDLException {

	}

	protected void addImportElementsInAllList() {
		for (final Impt impt : this.imports) {
			if (impt.getDescription() != null) {
				this.services.addAll(impt.getDescription().getServices());
				this.bindings.addAll(impt.getDescription().getBindings());
				this.interfaces.addAll(impt.getDescription().getInterfaces());
				if (this.types != null) {
					if (impt.getDescription().getTypes() != null) {
						this.types.getSchemas().addAll(impt.getDescription().getTypes().getSchemas());
					}
				} else {
					this.types = (T) impt.getDescription().getTypes();
				}
			}
		}
	}

	protected void addIncludeElementsInAllList() {
		for (final Incl incl : this.includes) {
			if (incl.getDescription() != null) {
				this.services.addAll(incl.getDescription().getServices());
				this.bindings.addAll(incl.getDescription().getBindings());
				this.interfaces.addAll(incl.getDescription().getInterfaces());
				if (this.types != null) {
					this.types.getSchemas().addAll(incl.getDescription().getTypes().getSchemas());
				} else {
					this.types = (T) incl.getDescription().getTypes();
				}
			}
		}
	}

	/**
	 * ImportImpl operation
	 */
	public void addImport(final Impt impt) {
		if (this.imports == null) {
			this.imports = new ArrayList<Impt>();
		}
		this.imports.add(impt);
	}

	public List<Impt> getImports() {
		if (this.imports == null) {
			this.imports = new ArrayList<Impt>();
		}
		return this.imports;
	}

	public List<Impt> getImports(final String namespaceUri) {
		final List<Impt> res = new ArrayList<Impt>();
		for (final Impt impt : this.imports) {
			if (impt.getNamespaceURI().equals(namespaceUri)) {
				res.add(impt);
			}
		}
		return res;
	}

	/**
	 * IncludeImpl operation
	 * 
	 * @throws WSDLException
	 */
	public void addInclude(final Incl incl) throws WSDLException {
		if (this.includes == null) {
			this.includes = new ArrayList<Incl>();
		}
		this.includes.add(incl);
	}

	public List<Incl> getIncludes() {
		if (this.includes == null) {
			this.includes = new ArrayList<Incl>();
		}
		return this.includes;
	}

	/**
	 * {@inheritDoc}
	 */
	public Incl getInclude(final URI locationUri) {
	    
	    if (this.includes.contains(locationUri)) {
	        return this.includes.get(this.includes.indexOf(locationUri));
	    }
	    else {
	        return null;
	    }
    }

    /**
	 * ServiceImpl operation
	 */
	public void addService(final S service) {
		if (this.services == null) {
			this.services = new ArrayList<S>();
		}
		this.services.add(service);
	}

	public List<S> getServices() {
		if (this.services == null) {
			this.services = new ArrayList<S>();
		}
		return this.services;
	}

	public S getService(final QName name) {
		S res = null;
		for (final S s : this.services) {
			if (s.getQName().equals(name)) {
				res = s;
				break;
			}
		}
		return res;
	}

	/**
	 * BindingImpl operation
	 */
	public void addBinding(final B binding) {
		if (this.bindings == null) {
			this.bindings = new ArrayList<B>();
		}
		this.bindings.add(binding);
	}

	public List<B> getBindings() {
		if (this.bindings == null) {
			this.bindings = new ArrayList<B>();
		}
		return this.bindings;
	}

	public B getBinding(final QName name) {
		B res = null;
		for (final B b : this.bindings) {
			if (b.getQName().equals(name)) {
				res = b;
				break;
			}
		}
		return res;
	}

	/**
	 * Interface operation
	 */
	public void addInterface(final I itf) {
		if (this.interfaces == null) {
			this.interfaces = new ArrayList<I>();
		}
		this.interfaces.add(itf);
	}

	public List<I> getInterfaces() {
		if (this.interfaces == null) {
			this.interfaces = new ArrayList<I>();
		}
		return this.interfaces;
	}

	public I getInterface(final QName name) {
		I res = null;
		for (final I i : this.interfaces) {
			if (i.getQName().equals(name)) {
				res = i;
				break;
			}
		}
		return res;
	}

	/**
	 * @return the features
	 */
	public Map<FeatureConstants, Object> getFeatures() {
		if (this.features == null) {
			this.features = new HashMap<FeatureConstants, Object>();
		}
		return this.features;
	}

	/**
	 * @return the feature value
	 */
	public Object getFeatureValue(final FeatureConstants feature) {
		if (this.features == null) {
			this.features = new HashMap<FeatureConstants, Object>();
		}

		return this.features.get(feature);
	}

	/**
	 * @param features
	 *            the features to set
	 */
	public void setFeatures(final Map<FeatureConstants, Object> features) {
		this.features = features;
	}

	public T getTypes() {
		return this.types;
	}

	public void setTypes(final T types) {
		this.types = types;
	}

	/**
	 * methods for namespaces
	 */
	public NamespaceMapperImpl getNamespaces() {
		return this.namespaceMapper;
	}
	
	public void addNamespace(final String prefix, final String namespaceURI) {
		this.namespaceMapper.addNamespace(prefix, namespaceURI);
	}
	
	/**
     * {@inheritDoc}
     * 
     */
	public URI getDocumentBaseURI() {
        return this.documentURI;
    }
	
	/**
     * {@inheritDoc}
     * 
     */
	public void setDocumentBaseURI(final URI documentURI) {
        this.documentURI = documentURI;
    }
    
	/**
	 * @return the schemaLocations
	 */
	public Map<String, String> getSchemaLocation() {
		if (this.schemaLocator == null) {
			this.schemaLocator = new SchemaLocatorImpl();
		}
		return this.schemaLocator.getSchemaLocations();
	}

	public List<Ep> findEndpointsImplementingInterface(I itf) {
		List<Ep> endpoints = new ArrayList<Ep>();
		if (itf != null) {
			for (S service : this.getServices()) {
				for (Ep endpoint : (List<Ep>) service.getEndpoints()) {
					if ((endpoint.getBinding() != null) && (endpoint.getBinding().getInterface() != null)) {
						if ((endpoint.getBinding().getInterface().getQName().getLocalPart().equals(itf.getQName().getLocalPart())) && (endpoint.getBinding().getInterface().getQName().getNamespaceURI().equals(itf.getQName().getNamespaceURI()))) {
							if(!endpoints.contains(endpoint)) {
								endpoints.add(endpoint);
							}
						}
					}
				}
			}
		}
		return endpoints;
	}

	public SchemaLocatorImpl getSchemaLocator() {
		return schemaLocator;
	}

	@Override
	public String toString() {
		return this.documentURI.toString();
	}

}
