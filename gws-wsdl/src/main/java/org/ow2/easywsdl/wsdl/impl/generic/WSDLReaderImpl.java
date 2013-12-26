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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.ow2.easywsdl.schema.api.XmlException;
import org.ow2.easywsdl.schema.api.absItf.AbsItfSchema;
import org.ow2.easywsdl.schema.util.SourceHelper;
import org.ow2.easywsdl.wsdl.api.Description;
import org.ow2.easywsdl.wsdl.api.WSDLException;
import org.ow2.easywsdl.wsdl.api.WSDLReader;
import org.ow2.easywsdl.wsdl.api.abstractElmt.AbstractWSDLReaderImpl;
import org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfDescription;
import org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfDescription.WSDLVersionConstants;
import org.ow2.easywsdl.wsdl.util.InputStreamForker;
import org.ow2.easywsdl.wsdl.util.WSDLVersionDetector;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/**
 * @author Nicolas Salatge - eBM WebSourcing
 */
public class WSDLReaderImpl extends AbstractWSDLReaderImpl implements
        WSDLReader {


    private final static Logger LOG = Logger.getLogger(WSDLReaderImpl.class.getName());

	private final AbstractWSDLReaderImpl reader11;
	private final AbstractWSDLReaderImpl reader20;
	
	public WSDLReaderImpl() throws WSDLException {
		this.reader11 = new org.ow2.easywsdl.wsdl.impl.wsdl11.WSDLReaderImpl();
		this.reader20 = new org.ow2.easywsdl.wsdl.impl.wsdl20.WSDLReaderImpl();
	}



    /**
     * {@inheritDoc}
     * 
     */
    public Description read(final URL wsdlURL) throws WSDLException, MalformedURLException, IOException, URISyntaxException {
        try {
            InputSource inputSource = new InputSource(wsdlURL.openStream());
            inputSource.setSystemId(wsdlURL.toString());

            final WSDLVersionConstants version = WSDLVersionDetector.getVersion(inputSource);

            inputSource = new InputSource(wsdlURL.openStream());
            inputSource.setSystemId(wsdlURL.toString());

            return this.read(version, inputSource, null, null);
        } catch (final MalformedURLException e) {
            throw new WSDLException("The provided well-formed URL has been detected as malformed !!", e);
        }
    }

	public Description read(InputSource source, Map<URI, AbsItfDescription> descriptionImports, Map<URI, AbsItfSchema> schemaImports) throws WSDLException, MalformedURLException, URISyntaxException {
		return this.read(source, descriptionImports, schemaImports, true);
	}
	
    /**
     * {@inheritDoc}
     */
    public Description read(final InputSource inputSource, final Map<URI, AbsItfDescription> descriptionImports, final Map<URI, AbsItfSchema> schemaImports, final boolean deleteImports) throws WSDLException, MalformedURLException, URISyntaxException {
        
        if (inputSource.getByteStream() != null) {
        	
            final String systemId = inputSource.getSystemId();
            if (systemId != null ) {
                this.setDocumentBaseURI(new URI(systemId));
            }
            
            
            final InputStream originalInputStream = inputSource.getByteStream();
            final InputStreamForker isf = new InputStreamForker(originalInputStream);
       
            
            inputSource.setByteStream(isf.getInputStreamOne());
            
            WSDLVersionConstants version = null;
            try {
				DOMSource source = SourceHelper.convertInputSource2DOMSource(inputSource);
				version = WSDLVersionDetector.getVersion(((Document)source.getNode()));
			} catch (XmlException e) {
				throw new WSDLException(e);
			}
            
              
            inputSource.setByteStream(isf.getInputStreamTwo());
            
                       
            return this.read(version, inputSource, descriptionImports, schemaImports, deleteImports);
        }
        else {
            throw new UnsupportedOperationException("This method supports only InputSource with byte stream.");
        }

	}

    private Description read(final WSDLVersionConstants version, final InputSource source, final Map<URI, AbsItfDescription> descriptionImports, final Map<URI, AbsItfSchema> schemaImports) throws WSDLException, MalformedURLException, URISyntaxException {
    	return this.read(version, source, descriptionImports, schemaImports, true);
    }
    
    private Description read(final WSDLVersionConstants version, final InputSource source, final Map<URI, AbsItfDescription> descriptionImports, final Map<URI, AbsItfSchema> schemaImports, final boolean deleteImports) throws WSDLException, MalformedURLException, URISyntaxException {
        
        final AbstractWSDLReaderImpl reader;
        if (version == WSDLVersionConstants.WSDL11) {
            reader = reader11;
        } else if (version == WSDLVersionConstants.WSDL20) {
            reader = reader20;
        } else {
            throw new WSDLException("unknown version of wsdl");
        }

        reader.setFeatures(this.getFeatures());
        return reader.read(source, descriptionImports, schemaImports, deleteImports);
    }

	/**
     * {@inheritDoc} 
     */
    public Description read(final Document document) throws WSDLException, URISyntaxException {
        
        try {
            final WSDLVersionConstants version = WSDLVersionDetector.getVersion(document);
            
            // The DOM Document needs to be converted into an InputStource
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            final StreamResult streamResult = new StreamResult(baos);
            // FIXME: The Transformer creation is not thread-safe
            final Transformer transformer = TransformerFactory.newInstance()
                    .newTransformer();
            transformer.transform(new DOMSource(document), streamResult);
            baos.flush();
            baos.close();

            final InputSource documentInputSource = new InputSource(
                    new ByteArrayInputStream(baos.toByteArray()));
            documentInputSource.setSystemId(document.getBaseURI());
            
            return this.read(version, documentInputSource, null, null, true);
        } catch (final TransformerConfigurationException e) {
            throw new WSDLException(e);
        } catch (final TransformerFactoryConfigurationError e) {
            throw new WSDLException(e);
        } catch (final TransformerException e) {
            throw new WSDLException(e);
        } catch (final IOException e) {
            throw new WSDLException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public Description read(InputSource inputSource) throws WSDLException, MalformedURLException, URISyntaxException {
        
        return this.read(inputSource, null, null, true);

    }
}
