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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.ow2.easywsdl.schema.api.SchemaException;
import org.ow2.easywsdl.schema.api.SchemaReader;
import org.ow2.easywsdl.schema.api.absItf.AbsItfSchema;
import org.ow2.easywsdl.schema.impl.SchemaReaderImpl;
import org.ow2.easywsdl.schema.util.URILocationResolver;
import org.ow2.easywsdl.wsdl.api.Description;
import org.ow2.easywsdl.wsdl.api.WSDLException;
import org.ow2.easywsdl.wsdl.api.WSDLReader;
import org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfDescription;
import org.xml.sax.InputSource;


/**
 * @author Nicolas Salatge - eBM WebSourcing
 */
public abstract class AbstractWSDLReaderImpl implements WSDLReader {

	protected static final Logger LOG = Logger.getLogger(AbstractWSDLReaderImpl.class.getName());

	private Map<FeatureConstants, Object> features = new HashMap<FeatureConstants, Object>();

	private Map<String, AbsItfDescription> importList = new HashMap<String, AbsItfDescription>();

	private SchemaReader schemaReader = null; 

	private final URILocationResolver uriLocationResolver = new URILocationResolver();

	private URI documentBaseURI;
    
    public AbstractWSDLReaderImpl() throws WSDLException {
		try {
			this.schemaReader = new SchemaReaderImpl();
			this.features.put(FeatureConstants.VERBOSE, false);
			this.features.put(FeatureConstants.IMPORT_DOCUMENTS, true);
		} catch (SchemaException e) {
			throw new WSDLException(e);
		}
	}
	
	public SchemaReader getSchemaReader() {
		return schemaReader;
	}
	
	public void setDocumentBaseURI(final URI documentBaseURI) {
	    this.documentBaseURI = documentBaseURI;
	    if (this.schemaReader != null) {
	        this.schemaReader.setDocumentBaseURI(documentBaseURI);
	    }
	}

	public final void setFeature(final FeatureConstants name, final Object value)
	throws WSDLException {
		this.features.put(name, value);
		AbstractWSDLReaderImpl.LOG.finest("set proterty: " + name + " - value = " + value);
	}

	public final Object getFeature(final FeatureConstants name) {
		return this.features.get(name);
	}

	public final Map<FeatureConstants, Object> getFeatures() {
		return this.features;
	}

	public final void setFeatures(final Map<FeatureConstants, Object> features) {
		this.features = features;
	}

	public Map<String, AbsItfDescription> getImportList() {
		return importList;
	}

	public void setImportList(Map<String, AbsItfDescription> importLists) {
		this.importList = importLists;
	}
	
	/**
	 * Read an WSDL part provided by an {@link InputSource}, description
     * imports/includes and schema imports/includes provided by
     * <code>descriptionImports</code> and <code>schemaImports</code> are not
     * read.
     * 
     * @throws WSDLException
     * @throws MalformedURLException
     *             The {@link InputSource} systemId is a malformed URL.
     * @throws URISyntaxException
     *             The {@link InputSource} systemId is an URL not formatted
     *             strictly according to to RFC2396 and cannot be converted to a
     *             URI.
	 * @param deleteImport 
	 * 				Clear import list
	 */
	public abstract Description read(final InputSource source, final Map<URI, AbsItfDescription> descriptionImports, final Map<URI, AbsItfSchema> schemaImports, final boolean deleteImports) throws WSDLException, MalformedURLException, URISyntaxException;

    /**
     * Read an external WSDL URI according to a base URI.
     * 
     * @throws WSDLException
     * @throws MalformedURLException
     *             The URL based on the external WSDL URI and the current base
     *             URI is a malformed URL.
     * @throws URISyntaxException
     *             The URL based on the external WSDL URI and the current base
     *             URI is an URL not formatted strictly according to to RFC2396
     *             and cannot be converted to a URI.
     */
    protected Description readExternalPart(final URI externalURI, final URI documentBaseURI, final Map<URI, AbsItfDescription> descriptionImports,
            final Map<URI, AbsItfSchema> schemaImports, final boolean deleteImports)
            throws WSDLException, MalformedURLException, URISyntaxException {
        
        InputSource inputSource = null;
		try {
			inputSource = new InputSource(this.uriLocationResolver.resolve(documentBaseURI, externalURI).openStream());
			inputSource.setSystemId(this.uriLocationResolver.resolve(documentBaseURI, externalURI).toString());
		} catch (IOException e) {
			throw new WSDLException(e);
		}
        return this.read(inputSource, descriptionImports, schemaImports, deleteImports);
    }

}
