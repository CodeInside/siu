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
package org.ow2.easywsdl.schema.api.abstractElmt;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.logging.Logger;

import org.ow2.easywsdl.schema.api.SchemaException;
import org.ow2.easywsdl.schema.api.SchemaReader.FeatureConstants;
import org.ow2.easywsdl.schema.api.absItf.AbsItfInclude;
import org.ow2.easywsdl.schema.api.absItf.AbsItfSchema;
import org.ow2.easywsdl.schema.impl.Constants;

/**
 * @author Nicolas Salatge - eBM WebSourcing
 */
public abstract class AbstractIncludeImpl<E, S extends AbsItfSchema> extends AbstractSchemaElementImpl<E> implements AbsItfInclude<S> {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOG = Logger.getLogger(AbstractIncludeImpl.class.getName());

	/**
	 * the parent
	 */
	protected S schema;

    /**
     * Default constructor
     * 
     * @param model
     *            the model
     * @param parent
     *            the parent description
     */
	public AbstractIncludeImpl(final E model, final Map<FeatureConstants, Object> features, final Map<URI, AbsItfSchema> imports, final URI baseURI, final AbstractSchemaReader reader) throws SchemaException {
		super(model, null);

		final URI schemaLocation = this.getLocationURI();

		if (features != null) {
			final Object obj = features.get(FeatureConstants.IMPORT_DOCUMENTS);
			if ((Boolean) obj) {
				this.retrieveInclude(schemaLocation, features, imports, baseURI, reader);
			}
		}

		if (this.schema != null) {
			((AbstractSchemaImpl) this.schema).setFeatures(features);
		}

	}

	/**
	 * Default constructor
	 * 
	 * @param model
	 *            the model
	 * @param parent
	 *            the parent description
	 */
	public AbstractIncludeImpl(final E model, final AbstractSchemaElementImpl parent, final Map<URI, AbsItfSchema> imports, final URI baseURI, final AbstractSchemaReader reader) throws SchemaException {
		super(model, parent);

		URI schemaLocation = this.getLocationURI();

		if (this.parent != null) {
			if (((AbstractSchemaImpl) this.parent).getFeatures() != null) {
				if ((((AbstractSchemaImpl) this.parent).getFeatures().get(FeatureConstants.IMPORT_DOCUMENTS) != null) && ((Boolean) ((AbstractSchemaImpl) this.parent).getFeatures().get(FeatureConstants.IMPORT_DOCUMENTS))) {
					this.retrieveInclude(schemaLocation, ((AbstractSchemaImpl) this.parent).getFeatures(), imports, baseURI,  reader);
				} else if(imports != null && !imports.isEmpty() && imports.get(schemaLocation) != null) {
					this.schema =  (S) imports.get(schemaLocation);
				}
			} else if(imports != null && imports.get(schemaLocation) != null) {
				this.schema =  (S) imports.get(schemaLocation);
			}
		}

		if ((parent != null) && (this.schema != null)) {
			((AbstractSchemaImpl) this.schema).setFeatures(((AbstractSchemaImpl) this.parent).getFeatures());
		}
	}

	private void retrieveInclude(final URI originalSchemaLocation, final Map<FeatureConstants, Object> features, final Map<URI, AbsItfSchema> imports, final URI baseURI, final AbstractSchemaReader reader) throws SchemaException {

		if (originalSchemaLocation != null) {
			try {
				URI schemaLocation = null;

				// Check for well known schemas
				if (Constants.XML_URI.equals(originalSchemaLocation)) {
					schemaLocation = this.getClass().getClassLoader().getResource(Constants.XML_XSD).toURI().normalize();
				} else {
					schemaLocation = originalSchemaLocation;
				}

				// Try to identify a cyclic import loop
				if(reader.getImportList().containsKey(schemaLocation)) {
					LOG.warning("This import is already include : " + schemaLocation.toString() + ". This probably mean there's a cyclic import");
					this.schema = (S) reader.getImportList().get(schemaLocation);
				} else {
					reader.getImportList().put(schemaLocation, null);

					if (imports != null && imports.containsKey(schemaLocation)) {
						// Search in imports Map
						this.schema = (S) imports.get(schemaLocation);
					} else {
						// Create schema reader
						reader.setFeatures(features);
						this.schema = (S) reader.readExternalPart(schemaLocation, baseURI, imports, false);
					}

				}
				reader.getImportList().put(schemaLocation, this.schema);
			} catch (final SchemaException e) {
				throw new SchemaException("the imported document cannot be import at: " + originalSchemaLocation.toString(), e);
			} catch (final URISyntaxException e) {
				throw new SchemaException("the imported document cannot be import at: " + originalSchemaLocation.toString() + " because the URI is invalid", e);
			} catch (final MalformedURLException e) {
                throw new SchemaException("the imported document cannot be import at: " + originalSchemaLocation.toString() + " because the URI is invalid", e);
            }
		}
	}

	/**
	 * @return the parent
	 */
	public S getSchema() {
		return this.schema;
	}

	public void setSchema(final S schema) {
		this.schema = schema;
	}

	/**
	 * @return the parent parent
	 */
	public S getParentSchema() {
		return (S) this.parent.getSchema();
	}

}
