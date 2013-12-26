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
import java.util.Map;
import java.util.logging.Logger;

import org.ow2.easywsdl.schema.api.absItf.AbsItfSchema;
import org.ow2.easywsdl.schema.api.abstractElmt.AbstractSchemaElementImpl;
import org.ow2.easywsdl.schema.impl.Constants;
import org.ow2.easywsdl.wsdl.api.WSDLException;
import org.ow2.easywsdl.wsdl.api.WSDLImportException;
import org.ow2.easywsdl.wsdl.api.WSDLReader.FeatureConstants;
import org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfDescription;
import org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfInclude;

/**
 * @author Nicolas Salatge - eBM WebSourcing
 */
@SuppressWarnings("unchecked")
public abstract class AbstractIncludeImpl<E, D extends AbsItfDescription> extends AbstractWSDLElementImpl<E> implements AbsItfInclude<D> {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOG = Logger.getLogger(AbstractIncludeImpl.class.getName());

	/**
	 * the desc
	 */
	protected D desc;


	/**
	 * Default constructor
	 * 
	 * @param model
	 *            the model
	 * @param parent
	 *            the parent description
	 * @throws WSDLException
	 * @throws WSDLImportException
	 */
	public AbstractIncludeImpl(final E model, final D parent, Map<URI, AbsItfDescription> descriptionImports, Map<URI, AbsItfSchema> schemaImports, final URI baseURI, AbstractWSDLReaderImpl reader) throws WSDLException, WSDLImportException {
		super(model, (AbstractWSDLElementImpl) parent);

		this.parent = (AbstractSchemaElementImpl) parent;

		final URI location = this.getLocationURI();

		if (this.parent != null) {
			if (!((AbstractDescriptionImpl) this.parent).getFeatures().isEmpty()) {
				if ((((AbstractDescriptionImpl) this.parent).getFeatures().get(FeatureConstants.IMPORT_DOCUMENTS) != null) && ((Boolean) ((AbstractDescriptionImpl) this.parent).getFeatures().get(FeatureConstants.IMPORT_DOCUMENTS))) {
					this.retrieveInclude(location, baseURI, descriptionImports, schemaImports, reader);
				} else if(descriptionImports != null && descriptionImports.get(location) != null) {
					this.desc = (D) descriptionImports.get(location);
				}
			} else if(descriptionImports != null && descriptionImports.get(location) != null) {
				this.desc = (D) descriptionImports.get(location);
			}
		}

		if ((this.parent != null) && (this.desc != null)) {
			if(this.desc instanceof AbstractDescriptionImpl) {
				((AbstractDescriptionImpl) this.desc).setFeatures(reader.getFeatures());
			} 
		}
	}

	private void retrieveInclude(final URI originalWsdlLocation, final URI baseURI, final Map<URI, AbsItfDescription> descriptionImports, final Map<URI, AbsItfSchema> schemaImports, final AbstractWSDLReaderImpl reader) throws WSDLException, WSDLImportException {

		if (originalWsdlLocation != null) {

			try {
				URI wsdlLocation = null;

				// Check for well known schemas
				if (Constants.XML_URI.equals(originalWsdlLocation)) {
					wsdlLocation = this.getClass().getClassLoader().getResource(Constants.XML_XSD).toURI().normalize();
				} else {
					wsdlLocation = originalWsdlLocation;
				}

				String locationNorm = wsdlLocation.normalize().toString();

				// Try to identify a cyclic import loop

				
				//if(!wsdlLocation.toString().startsWith("xpath")) {
					if(reader.getImportList().containsKey(locationNorm)) {
						LOG.warning("This import is already include : " + locationNorm + ". This probably mean there's a cyclic import");
						this.desc = (D) reader.getImportList().get(locationNorm);
					} else {	
						if (descriptionImports != null && descriptionImports.containsKey(wsdlLocation)) {
							this.desc = (D) descriptionImports.get(wsdlLocation);
						} else {
							reader.getImportList().put(locationNorm, null);
							// Create schema reader
							this.desc = (D)reader.readExternalPart(wsdlLocation, baseURI, descriptionImports, schemaImports, false);
							/*if(reader instanceof org.ow2.easywsdl.wsdl.impl.wsdl11.WSDLReaderImpl) {
							this.desc = (D) (reader).readWSDL(schemaLocation, descriptionImports, schemaImports, false);
						} else if (reader instanceof org.ow2.easywsdl.wsdl.impl.wsdl20.WSDLReaderImpl) {
							this.desc = (D) (reader).readWSDL(schemaLocation, descriptionImports, schemaImports, false);
						} else {
							throw new WSDLException("Reader unknowed");
						}*/
						}
						reader.getImportList().put(locationNorm, this.desc);
					}
				//}
			} catch (final Exception e) {
				// do nothing
				// the document is not included in imported document
				throw new WSDLImportException("the imported document cannot be imported at: " + originalWsdlLocation, e);
			}
		}
	}

	/**
	 * @return the desc
	 */
	public D getDescription() {
		return this.desc;
	}

	public void setDescription(final D d) {
		this.desc = d;
	}

	/**
	 * @return the parent desc
	 */
	public D getParentDescription() {
		return (D) this.parent;
	}

	public void setParentDescription(final D p) {
		this.parent = (AbstractSchemaElementImpl) p;
	}

}
