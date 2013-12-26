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
package org.ow2.easywsdl.wsdl.api.abstractItf;

import java.net.URI;

import org.ow2.easywsdl.wsdl.api.WSDLElement;

/**
 * This interface represents an include, and may contain a reference to the
 * included definition.
 * 
 * @author Nicolas Salatge - eBM WebSourcing
 */
@SuppressWarnings("unchecked")
public interface AbsItfInclude<D extends AbsItfDescription> extends WSDLElement {

    /**
     * Set the location URI of this import.
     * 
     * @param locationURI
     *            the desired location URI
     */
    void setLocationURI(URI locationURI);

    /**
     * Get the location URI of this import.
     * 
     * @return the location {@link URI} of this import or
     *         <code>null</code> if undefined.
     */
    URI getLocationURI();
    
    /**
     * This property can be used to hang a referenced Definition, and the
     * top-level Definition (i.e. the one with the &lt;import&gt;) will use this
     * Definition when resolving referenced WSDL parts. This would need to be
     * made into a generic reference to handle other types of referenced
     * documents.
     */
    void setDescription(D description);

    /**
     * This property can be used to hang a referenced Definition, and the
     * top-level Definition (i.e. the one with the &lt;import&gt;) will use this
     * Definition when resolving referenced WSDL parts. This would need to be
     * made into a generic reference to handle other types of referenced
     * documents.
     */
    D getDescription();
}
