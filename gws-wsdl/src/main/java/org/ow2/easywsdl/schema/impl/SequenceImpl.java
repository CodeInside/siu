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

import org.ow2.easywsdl.schema.api.Element;
import org.ow2.easywsdl.schema.api.Sequence;
import org.ow2.easywsdl.schema.api.abstractElmt.AbstractElementImpl;
import org.ow2.easywsdl.schema.api.abstractElmt.AbstractSchemaElementImpl;
import org.ow2.easywsdl.schema.api.abstractElmt.AbstractSequenceImpl;
import org.ow2.easywsdl.schema.org.w3._2001.xmlschema.ExplicitGroup;
import org.ow2.easywsdl.schema.org.w3._2001.xmlschema.LocalElement;
import org.ow2.easywsdl.schema.org.w3._2001.xmlschema.ObjectFactory;

/**
 * @author Nicolas Salatge - eBM WebSourcing
 */
public class SequenceImpl extends AbstractSequenceImpl<ExplicitGroup, Element> implements Sequence {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public SequenceImpl(ExplicitGroup model, AbstractSchemaElementImpl parent) {
		super(model, parent);

		// get elements
		for (Object item : this.model.getParticle()) {
			if (item instanceof JAXBElement) {
				if (((JAXBElement) item).getValue() instanceof org.ow2.easywsdl.schema.org.w3._2001.xmlschema.Element) {
					this.elements
							.add(new ElementImpl(
									(org.ow2.easywsdl.schema.org.w3._2001.xmlschema.Element) ((JAXBElement) item)
											.getValue(), this));

				}
			}
			// TODO: finish to analyze
		}

	}

	public Element createElement() {
		return new ElementImpl(new LocalElement(), this);
	}

	@Override
	public void addElement(Element elmt) {
		super.addElement(elmt);
		ObjectFactory factory = new ObjectFactory();
		JAXBElement<LocalElement> element = factory
				.createGroupElement((LocalElement) ((AbstractElementImpl) elmt)
						.getModel());
		this.model.getParticle().add(element);
	}


}
