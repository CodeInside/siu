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
package org.ow2.easywsdl.wsdl.impl.generic;

import org.ow2.easywsdl.u.NotImplementedException;
import org.ow2.easywsdl.wsdl.api.Description;
import org.ow2.easywsdl.wsdl.api.WSDLException;
import org.ow2.easywsdl.wsdl.api.abstractElmt.AbstractWSDLElementImpl;
import org.ow2.easywsdl.wsdl.org.w3.ns.wsdl.DescriptionType;
import org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.TDefinitions;
import org.w3c.dom.Document;

/**
 * @author Nicolas Salatge - eBM WebSourcing
 */
public class WSDLWriterImpl implements org.ow2.easywsdl.wsdl.api.WSDLWriter {

	
	private boolean customPrefixes =false;
	private String[] customPrefixesDeclaration;
	private boolean normalizedPrefixes =false;

	private final org.ow2.easywsdl.wsdl.impl.wsdl11.WSDLWriterImpl wsdl11writer;
	
	private final org.ow2.easywsdl.wsdl.impl.wsdl20.WSDLWriterImpl wsdl20writer;
	
//	WSDLVersionConstants defaultModel = WSDLVersionConstants.WSDL20;

	public WSDLWriterImpl() throws WSDLException {
		wsdl11writer = new org.ow2.easywsdl.wsdl.impl.wsdl11.WSDLWriterImpl();
		wsdl20writer = new org.ow2.easywsdl.wsdl.impl.wsdl20.WSDLWriterImpl();
	}

	public Document getDocument(final Description wsdlDef) throws WSDLException {
		Document doc = null;
		if(wsdlDef != null) {
			Description source = wsdlDef;
			final org.ow2.easywsdl.wsdl.api.WSDLWriter writer = this
			.getConcreteWriter(source);

			doc = writer.getDocument(source);
		}
		return doc;
	}

	@SuppressWarnings("unchecked")
	private org.ow2.easywsdl.wsdl.api.WSDLWriter getConcreteWriter(
			final Description wsdlDef) throws WSDLException {
		org.ow2.easywsdl.wsdl.api.WSDLWriter writer = null;
		if ((wsdlDef != null)
				&& (((AbstractWSDLElementImpl) wsdlDef).getModel() instanceof TDefinitions)) {
			writer = this.wsdl11writer;
			if (this.customPrefixes){
				writer.useCustomNamespacesPrefixes(this.customPrefixesDeclaration);
			}else if (this.normalizedPrefixes){
				writer.useNormalizedNamespacesPrefixes();
			}
		} else if ((wsdlDef != null)
				&& (((AbstractWSDLElementImpl) wsdlDef).getModel() instanceof DescriptionType)) {
			writer = this.wsdl20writer;
		} 
//		else if ((wsdlDef != null)
//				&& (((AbstractWSDLElementImpl) wsdlDef).getModel() instanceof Element)) {
//			if(this.defaultModel.equals(WSDLVersionConstants.WSDL11)) {
//				writer = new org.ow2.easywsdl.schema.test.impl.wsdl11.WSDLWriterImpl(this.addedObjectFactories);
//			} else if(this.defaultModel.equals(WSDLVersionConstants.WSDL20)) {
//				writer = new org.ow2.easywsdl.schema.test.impl.wsdl20.WSDLWriterImpl(this.addedObjectFactories);
//			}
//		} 
	    else {
			throw new WSDLException("Unknown model");
		}
		return writer;
	}

	public boolean getFeature(final String name) throws IllegalArgumentException {
		throw new NotImplementedException();
	}

	public void setFeature(final String name, final boolean value) throws IllegalArgumentException {
		throw new NotImplementedException();
	}

	public String writeWSDL(final Description wsdlDef) throws WSDLException {
		String res = null;
		if(wsdlDef != null) {
			Description source = wsdlDef;
			final org.ow2.easywsdl.wsdl.api.WSDLWriter writer = this
			.getConcreteWriter(source);

//			if(((AbstractWSDLElementImpl) source).getModel() instanceof Element) {
//				if(this.defaultModel.equals(WSDLVersionConstants.WSDL11)) {
//					source = org.ow2.easywsdl.schema.test.impl.wsdl11.WSDLConverter.convertGenericWSDLToWSDL11((DescriptionImpl) wsdlDef);
//				} else {
//					source = org.ow2.easywsdl.schema.test.impl.wsdl20.WSDLConverter.convertGenericWSDLToWSDL20((DescriptionImpl) wsdlDef);
//				}
//			}

			res = writer.writeWSDL(source);
		}
		return res;
	}

	public void useCustomNamespacesPrefixes(String[] customPrefixes)
			throws WSDLException {
		this.customPrefixes= true;
		this.customPrefixesDeclaration= customPrefixes.clone();
		
	}

	public void useNormalizedNamespacesPrefixes() throws WSDLException {
		this.normalizedPrefixes= true;
		
	}

//	public WSDLVersionConstants getDefaultModel() {
//		return this.defaultModel;
//	}
//
//	public void setDefaultModel(WSDLVersionConstants version) throws WSDLException  {
//		if(version.equals(WSDLVersionConstants.GENERIC)) {
//			throw new WSDLException("Incorrect default model: " + version);
//		}
//
//		this.defaultModel = version;
//	}

}
