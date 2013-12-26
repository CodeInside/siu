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
package org.ow2.easywsdl.schema.api;

import java.io.OutputStream;

import org.w3c.dom.Document;

/**
 * This interface describes a collection of methods that allow a SchemaImpl
 * model to be written to a writer in an XML format that follows the SchemaImpl
 * parent.
 * 
 * @author Nicolas Salatge - eBM WebSourcing
 */
public interface SchemaWriter {
    /**
     * Sets the specified feature to the specified value.
     * <p>
     * There are no minimum features that must be supported.
     * <p>
     * All feature names must be fully-qualified, Java package style. All names
     * starting with javax.wsdl. are reserved for features defined by the
     * JSchema specification. It is recommended that implementation- specific
     * features be fully-qualified to match the package name of that
     * implementation. For example: com.abc.featureName
     * 
     * @param name
     *            the name of the feature to be set.
     * @param value
     *            the value to set the feature to.
     * @throws IllegalArgumentException
     *             if the feature name is not recognized.
     * @see #getFeature(String)
     */
    void setFeature(String name, boolean value) throws IllegalArgumentException;

    /**
     * Gets the value of the specified feature.
     * 
     * @param name
     *            the name of the feature to get the value of.
     * @return the value of the feature.
     * @throws IllegalArgumentException
     *             if the feature name is not recognized.
     * @see #setFeature(String, boolean)
     */
    boolean getFeature(String name) throws IllegalArgumentException;

    /**
     * Return a document generated from the specified SchemaImpl model.
     */
    Document getDocument(Schema schemaDef) throws SchemaException;
    
	void writeSchema(final Schema schemaDef, OutputStream output) throws SchemaException;

}
