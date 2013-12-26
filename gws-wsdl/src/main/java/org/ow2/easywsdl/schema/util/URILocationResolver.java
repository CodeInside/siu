/**
 * easySchema - easyWSDL toolbox Platform.
 * Copyright (c) 2009,  eBM Websourcing
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

package org.ow2.easywsdl.schema.util;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

/**
 * URI resolver used to resolve URI as URL, based on a base URI, to load
 * external document.
 * 
 * @author Christophe DENEUX - Capgemini Sud
 */
public final class URILocationResolver {

    /**
     * <p>
     * Resolve the <code>uri</code> according to the <code>baseURI</code>.
     * </p>
     * <p>
     * If <code>baseURI</code> is <code>null</code>, the return value is the
     * <code>uri</code> converted into {@link URL} using {@link URI#toURL()}.
     * Otherwise the return value is the result of {@link URL#URL(URL, String)}.
     * </p>
     * 
     * @param baseURI
     *            The base {@link URI}.
     * @param uri
     *            The {@link URI} to resolve.
     * @return The resolved URI as an {@link URL}.
     * @throws MalformedURLException
     *             The resolved URI is not a well-formed URL.
     */
    public URL resolve(final URI baseURI, final URI uri) throws MalformedURLException {
        if (baseURI == null) {
            // No base URI defined, we return the uri as URL
            return uri.toURL();
        } else {
            // We try to resolve the uri according to the baseURI
            // Note: Usage of URL(URL, String) is needed to handle correctly
            // baseURI based on a JAR URL
            return new URL(baseURI.toURL(), uri.toString());
        }
    }
}
