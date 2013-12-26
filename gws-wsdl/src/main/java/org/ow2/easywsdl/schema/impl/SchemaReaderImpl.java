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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;

import org.ow2.easywsdl.schema.api.Schema;
import org.ow2.easywsdl.schema.api.SchemaException;
import org.ow2.easywsdl.schema.api.absItf.AbsItfSchema;
import org.ow2.easywsdl.schema.api.abstractElmt.AbstractSchemaReader;
import org.ow2.easywsdl.schema.util.EasyXMLFilter;
import org.ow2.easywsdl.schema.util.URILocationResolver;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * @author Nicolas Salatge - eBM WebSourcing
 * @author Gael Blondelle - EBM WebSourcing
 */
public class SchemaReaderImpl extends AbstractSchemaReader implements org.ow2.easywsdl.schema.api.SchemaReader {

	private static final Logger LOG = Logger.getLogger(SchemaReaderImpl.class.getName());



	private Map<FeatureConstants, Object> features = new HashMap<FeatureConstants, Object>();

	private Map<URI, AbsItfSchema> importList = new HashMap<URI, AbsItfSchema>();

    private final URILocationResolver schemaLocationResolver = new URILocationResolver();
    
    private URI documentBaseURI;

    private static SchemaJAXBContext jaxbcontext = null;
	private static SchemaException contextException = null;
	
	static {
		try {
			jaxbcontext = SchemaJAXBContext.getInstance();
		} catch (final SchemaException e) {
			contextException = e;
		}
	}
	
	public static SchemaJAXBContext getJaxbContext() throws SchemaException {
		if(contextException != null) {
			throw contextException;
		}
		return jaxbcontext;
	}
    
    
	/*
	 * Private object initializations
	 */
	public SchemaReaderImpl() throws SchemaException {
		getJaxbContext();
		this.features.put(FeatureConstants.VERBOSE, false);
		this.features.put(FeatureConstants.IMPORT_DOCUMENTS, true);
	}




	
	/**
     * {@inheritDoc}
     */
    public void setDocumentBaseURI(final URI documentBaseURI) {
	    this.documentBaseURI = documentBaseURI;
	}

    /**
     * {@inheritDoc}
     */
    public URI getDocumentBaseURI() {
        return this.documentBaseURI;
    }

	/**
	 * {@inheritDoc}
	 */
	public Schema read(final URL schemaURL) throws SchemaException, URISyntaxException, IOException {
	    try {
	    	InputStream stream = schemaURL.openStream();
            final InputSource inputSource = new InputSource(stream);
            inputSource.setSystemId(schemaURL.toString());
            return this.read(inputSource);
        } catch (final MalformedURLException e) {
            throw new SchemaException("The provided well-formed URL has been detected as malformed !!", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public Schema read(final InputSource inputSource)
            throws SchemaException, URISyntaxException, MalformedURLException {
    	if(inputSource.getSystemId() != null) {
    		this.documentBaseURI = new URI(inputSource.getSystemId());
    	} else {
    		this.documentBaseURI = new URI(".");
    	}
        return this.readSchema(inputSource, null, true);
    }
    
    /**
     * {@inheritDoc}
     */
    public Schema read(final Document doc) throws SchemaException {
        
        try {
            // The DOM Document needs to be converted into an InputStream
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            final StreamResult streamResult = new StreamResult(baos);
            // FIXME: The Transformer creation is not thread-safe
            final Transformer transformer = TransformerFactory.newInstance()
                    .newTransformer();
            transformer.transform(new DOMSource(doc), streamResult);
            baos.flush();
            baos.close();

            final InputSource documentInputSource = new InputSource(
                    new ByteArrayInputStream(baos.toByteArray()));
            documentInputSource.setSystemId(doc.getBaseURI());
            
            return this.read(documentInputSource);
        } catch (final TransformerConfigurationException e) {
            throw new SchemaException(e);
        } catch (final TransformerFactoryConfigurationError e) {
            throw new SchemaException(e);
        } catch (final TransformerException e) {
            throw new SchemaException(e);
        } catch (final IOException e) {
            throw new SchemaException(e);
        } catch (final URISyntaxException e) {
            throw new SchemaException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
	protected Schema readExternalPart(final URI schemaLocationURI, final URI baseURI, Map<URI, AbsItfSchema> imports, boolean deleteImports) throws SchemaException, MalformedURLException, URISyntaxException {
        
    	InputSource inputSource = null;
		try {
			inputSource = new InputSource(this.schemaLocationResolver.resolve(baseURI, schemaLocationURI).openStream());
			inputSource.setSystemId(this.schemaLocationResolver.resolve(baseURI, schemaLocationURI).toString());
		} catch (IOException e) {
			throw new SchemaException(e);
		}
		return this.readSchema(inputSource, imports, deleteImports);
	}

	/**
     * @throws MalformedURLException
     *             The {@link InputSource} systemId is a malformed URL.
     * @throws URISyntaxException
     *             The {@link InputSource} systemId is an URL not formatted
     *             strictly according to to RFC2396 and cannot be converted to a
     *             URI.
     */
    private Schema readSchema(final InputSource source, Map<URI, AbsItfSchema> imports, boolean deleteImports) throws SchemaException, MalformedURLException, URISyntaxException {
	    if(deleteImports){
			this.getImportList().clear();
		}
		try {
	        final String systemId = source.getSystemId();
	        URI systemIdURI;
	        if (systemId != null ) {
	            systemIdURI = new URI(systemId);
	            this.setDocumentBaseURI(systemIdURI);
	        } else {
	            systemIdURI = new File(".").toURI();
	        }
			final XMLReader xmlReader = XMLReaderFactory.createXMLReader();
			final EasyXMLFilter filter = new EasyXMLFilter(xmlReader);
			final SAXSource saxSource = new SAXSource(filter, source);

			// TODO use SAX validation instead of JAXB validation
			// turn off the JAXB provider's default validation mechanism to
			// avoid duplicate validation
			// SchemaReaderImpl.getUnMarshaller().setValidating( false );
			LOG.fine("Loading " + systemIdURI);
			
			
			// unmarshal
			final JAXBElement<org.ow2.easywsdl.schema.org.w3._2001.xmlschema.Schema> schemaBinding = getJaxbContext().getJaxbContext().createUnmarshaller().unmarshal(saxSource, org.ow2.easywsdl.schema.org.w3._2001.xmlschema.Schema.class);

			final org.ow2.easywsdl.schema.org.w3._2001.xmlschema.Schema def = schemaBinding.getValue();

			return new org.ow2.easywsdl.schema.impl.SchemaImpl(systemIdURI, def, filter.getNamespaceMapper(), filter.getSchemaLocator(), this.getFeatures(), imports, this);
		} catch (SAXException e) {
			throw new SchemaException("Can not get schema: ", e);
		} catch (JAXBException e) {
			throw new SchemaException("Can not get schema: ", e);
		}
	}

	public void setFeature(final FeatureConstants name, final Object value)
	    throws IllegalArgumentException {
		this.features.put(name, value);
		SchemaReaderImpl.LOG.finest("set features: name = " + name + " - value = " + value);
	}

	public Object getFeature(final FeatureConstants name) throws IllegalArgumentException {
		return this.features.get(name);
	}

	public Map<FeatureConstants, Object> getFeatures() {
		return this.features;
	}

	public void setFeatures(final Map<FeatureConstants, Object> features) {
		this.features = features;
	}

	@Override
	protected Map<URI, AbsItfSchema> getImportList() {
		return importList;
	}

	protected void setImportList(Map<URI, AbsItfSchema> importLists) {
		this.importList = importLists;
	}
}
