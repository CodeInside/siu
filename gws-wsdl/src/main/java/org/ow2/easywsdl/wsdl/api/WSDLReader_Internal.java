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
package org.ow2.easywsdl.wsdl.api;



public interface WSDLReader_Internal {

	//Description readWSDL(final URI wsdlURI, Map<URI, AbsItfDescription> descriptionImports, Map<URI, AbsItfSchema> schemaImports) throws WSDLException;
	
	//Description readWSDL(final Document doc, Map<URI, AbsItfDescription> descriptionImports, Map<URI, AbsItfSchema> schemaImports) throws WSDLException;

	//Description readWSDL(final URI wsdlURI, WSDLVersionConstants version, InputSource source) throws WSDLException;

	//Description readWSDL(final URI wsdlURI, WSDLVersionConstants version, InputSource source, Map<URI, AbsItfDescription> descriptionImports, Map<URI, AbsItfSchema> schemaImports) throws WSDLException;
	
	/**
     * Read an external WSDL URI according to the current base URI of the
     * reader.
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
    /*Description readExternalPart(final URI schemaURI, final Map<URI, AbsItfDescription> descriptionImports,
            final Map<URI, AbsItfSchema> schemaImports, final boolean deleteImports)
            throws WSDLException, MalformedURLException, URISyntaxException;

    protected abstract Map<URI, AbsItfSchema> getImportList();*/
}
