/**
 * easySchema - easyWSDL toolbox Platform.
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
package org.ow2.easywsdl.schema.impl;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.ow2.easywsdl.schema.api.Enumeration;
import org.ow2.easywsdl.schema.api.Restriction;
import org.ow2.easywsdl.schema.api.abstractElmt.AbstractEnumerationImpl;
import org.ow2.easywsdl.schema.api.abstractElmt.AbstractRestrictionImpl;
import org.ow2.easywsdl.schema.api.abstractElmt.AbstractSchemaElementImpl;
import org.ow2.easywsdl.schema.org.w3._2001.xmlschema.NoFixedFacet;
import org.ow2.easywsdl.schema.org.w3._2001.xmlschema.ObjectFactory;

/**
 * @author Nicolas Boissel-Dallier - eBM WebSourcing
 */
public class RestrictionImpl 
	extends AbstractRestrictionImpl<org.ow2.easywsdl.schema.org.w3._2001.xmlschema.Restriction, Enumeration> 
	implements Restriction {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public RestrictionImpl(org.ow2.easywsdl.schema.org.w3._2001.xmlschema.Restriction model, AbstractSchemaElementImpl parent) {
		super(model, parent);

		// get elements
		for (Object item : this.model.getFacets()) {
			if (item instanceof JAXBElement) {
				// Enumeration management
				if (((JAXBElement) item).getValue() instanceof NoFixedFacet && ((JAXBElement) item).getName().equals(
						new QName("http://www.w3.org/2001/XMLSchema", "enumeration"))) {
					this.enums.add(new EnumerationImpl((NoFixedFacet)((JAXBElement) item).getValue(), this));
				}
				// TODO: finish to parse (Pattern, Length...)
			}
		}
	}

	public Enumeration createEnumeration() {
		return new EnumerationImpl(new NoFixedFacet(), this);
	}

	@Override
	public void addEnumeration(Enumeration enumeration) {
		super.addEnumeration(enumeration);
		ObjectFactory factory = new ObjectFactory();
		JAXBElement<NoFixedFacet> enumElmt = factory
				.createEnumeration((NoFixedFacet)((AbstractEnumerationImpl) enumeration).getModel());
		this.model.getFacets().add(enumElmt);
	}

	public QName getBase() {
		return this.model.getBase();
	}

	public void setBase(QName base) {
		this.model.setBase(base);
	}

}
