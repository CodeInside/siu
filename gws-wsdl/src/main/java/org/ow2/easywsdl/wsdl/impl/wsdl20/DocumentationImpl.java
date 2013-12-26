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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.ow2.easywsdl.schema.api.Documentation;
import org.ow2.easywsdl.wsdl.api.abstractElmt.AbstractWSDLElementImpl;
import org.ow2.easywsdl.wsdl.org.w3.ns.wsdl.DocumentationType;

/**
 * @author Nicolas Salatge - eBM WebSourcing
 */
public class DocumentationImpl extends
		AbstractWSDLElementImpl<List<DocumentationType>> implements
		Documentation {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DocumentationImpl() {
		super(new ArrayList<DocumentationType>(), null);
		final DocumentationType doc = new DocumentationType();
		this.model.add(doc);
	}

	public DocumentationImpl(final List<DocumentationType> docs,
			AbstractWSDLElementImpl parent) {
		super(docs, parent);
	}

	public String getContent() {
		final StringBuffer res = new StringBuffer();
		if (this.model != null) {
			for (final DocumentationType doc : this.model) {
				final Iterator<Object> it = doc
						.getContent().iterator();
				String text = null;
				while (it.hasNext()) {
					text = it.next().toString();
					res.append(text + "\n");
				}
			}
		}
		String result = "";
		if (res.length() > 0) {
			result = res.substring(0, res.lastIndexOf("\n"));
		}
		return result;
	}

	public void setContent(final String content) {
		this.model.clear();
		final DocumentationType doc = new DocumentationType();
		doc.getContent().add(content);
		this.model.add(doc);
	}

}
