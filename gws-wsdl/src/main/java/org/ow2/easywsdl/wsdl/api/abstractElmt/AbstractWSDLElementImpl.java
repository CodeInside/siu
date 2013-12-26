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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.namespace.QName;

import org.ow2.easywsdl.u.NotImplementedException;
import org.ow2.easywsdl.schema.api.Documentation;
import org.ow2.easywsdl.schema.api.XmlException;
import org.ow2.easywsdl.schema.api.abstractElmt.AbstractXMLElementImpl;
import org.ow2.easywsdl.wsdl.api.WSDLElement;
import org.ow2.easywsdl.wsdl.api.WSDLException;
import org.ow2.easywsdl.wsdl.org.w3.ns.wsdl.DocumentedType;
import org.ow2.easywsdl.wsdl.org.w3.ns.wsdl.ExtensibleDocumentedType;
import org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.TDocumented;
import org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.TExtensibleAttributesDocumented;
import org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.TExtensibleDocumented;
import org.w3c.dom.Element;

/**
 * Abstract super class for all WSDL Elements, providing some basic
 * common functionality.
 */

/**
 * @author Nicolas Salatge - eBM WebSourcing
 */
public abstract class AbstractWSDLElementImpl<E> extends AbstractXMLElementImpl<E> implements WSDLElement {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private static final Logger LOG = Logger.getLogger(AbstractWSDLElementImpl.class.getName());

    public AbstractWSDLElementImpl() {
    	super();
    }

    public AbstractWSDLElementImpl(E model, AbstractWSDLElementImpl parent) {
        super(model, parent);
    }

    /**
     * Set the documentation for this document.
     *
     * @param docEl
     *            the documentation element
     */
    @SuppressWarnings("unchecked")
    public void setDocumentation(final Documentation doc) {
        this.documentation = doc;

        if (this.model instanceof TDocumented) {
            ((TDocumented) this.model)
                    .setDocumentation(((org.ow2.easywsdl.wsdl.impl.wsdl11.DocumentationImpl) this.documentation)
                            .getModel());
        } else if (this.model instanceof List) {
            ((List<DocumentedType>) this.model)
                    .add((DocumentedType) ((org.ow2.easywsdl.wsdl.impl.wsdl20.DocumentationImpl) this.documentation)
                            .getModel());
        }
    }

    /**
     * Get the documentation.
     *
     * @return the documentation element
     */
    public Documentation getDocumentation() {
        return this.documentation;
    }

    /**
     * Create the documentation element.
     *
     * @return the documentation element
     */
    public Documentation createDocumentation() {
        Documentation doc = null;
        if (this.getClass().getPackage().getName().equals(
                org.ow2.easywsdl.wsdl.impl.wsdl11.Constants.WSDL11_PACKAGE)) {
            doc = new org.ow2.easywsdl.wsdl.impl.wsdl11.DocumentationImpl();
        } else if (this.getClass().getPackage().getName().equals(
                org.ow2.easywsdl.wsdl.impl.wsdl20.Constants.WSDL20_PACKAGE)) {
            doc = new org.ow2.easywsdl.wsdl.impl.wsdl20.DocumentationImpl();
        } else {
            LOG.info("Impossible to create a documentation on this element");
        }
        return doc;
    }

    /**
     * Get all the extensibility elements defined here.
     *
     * @throws WSDLException
     */
    public List<Element> getOtherElements() throws XmlException {
        final List<Element> res = new ArrayList<Element>();

		if (this.model instanceof TExtensibleDocumented) {
			List<Object> any = null;
			any = ((TExtensibleDocumented) this.model).getAny();
			for (final Object anyItem : any) {
				if (anyItem instanceof Element) {
					Element xmlElmt = (Element)anyItem;
					res.add(xmlElmt);
				}
			}
		}
        return res;
    }
    
    public void addOtherElements(Element elmt) {
    	throw new NotImplementedException();
    }

    /**
     * Get the map containing all the attributes defined on this element. The
     * keys are the qnames of the attributes.
     *
     * @return a map containing all the attributes defined on this element
     * @throws WSDLException
     *
     */
    public Map<QName, String> getOtherAttributes() throws XmlException {
        Map<QName, String> res = new HashMap<QName, String>();

        if (this.model instanceof TExtensibleAttributesDocumented) {
            res = ((TExtensibleAttributesDocumented) this.model).getOtherAttributes();
        } else if (this.model instanceof ExtensibleDocumentedType) {
            res = ((ExtensibleDocumentedType) this.model).getOtherAttributes();
        }
        return res;
    }

    @Override
    public String toString() {
    	String res = null;
    	if(this.model != null) {
    		res = this.model.toString();
    	}
        return res;
    }

    public E getModel() {
        return this.model;
    }
}
