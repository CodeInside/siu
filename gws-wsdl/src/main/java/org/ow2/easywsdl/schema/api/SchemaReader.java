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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/**
 * This interface describes a collection of methods that enable conversion of a
 * SchemaImpl document (in XML, following the SchemaImpl parent described in the
 * SchemaImpl specification) into a SchemaImpl model.
 * 
 * @author Nicolas Salatge - eBM WebSourcing
 */
public interface SchemaReader {

    /**
     * Constants for the Message Exchange Patterns.
     * 
     */
    public enum FeatureConstants {
        VERBOSE("org.ow2.easywsdl.schema.verbose"), IMPORT_DOCUMENTS(
                "org.ow2.easywsdl.schema.importDocuments");

        private final String value;

        /**
         * Creates a new instance of {@link FeatureConstants}
         * 
         * @param value
         */
        private FeatureConstants(final String value) {
            this.value = value;
        }

        /**
         * 
         * @return
         */
        public String value() {
            return this.value;
        }

        /**
         * Please use this equals method instead of using :<code>
         * value().equals(value)
         * </code> which is
         * almost 10 times slower...
         * 
         * @param value
         * @return
         */
        public boolean equals(final String val) {
            return this.toString().equals(val);
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Enum#toString()
         */
        @Override
        public String toString() {
            return this.value;
        }
    }

    /**
     * Sets the specified feature to the specified value.
     * <p>
     * The minimum features that must be supported are:
     * <p>
     * <table border=1>
     * <tr>
     * <th>Name</th>
     * <th>Description</th>
     * <th>Default Value</th>
     * </tr>
     * <tr>
     * <td><center>org.ow2.easywsdl.schema.verbose</center></td>
     * <td>If set to true, status messages will be displayed.</td>
     * <td><center>type: boolean - default value: false</center></td>
     * </tr>
     * <tr>
     * <td><center>org.ow2.easywsdl.schema.importDocuments</center></td>
     * <td>If set to true, imported WSDL documents will be retrieved and
     * processed.</td>
     * <td><center>type: boolean - default value: true</center></td>
     * </tr>
     * <tr>
     * <td>
     * <center>org.ow2.easywsdl.schema.pathDirectoryOfImportLocations
     * </center></td>
     * <td>If the location is set, imported WSDL documents will be retrieved at
     * this location (Set the importDocuments Features at true).</td>
     * <td><center>type: String</center></td>
     * </tr>
     * </table>
     * <p>
     * All feature names must be fully-qualified, Java package style. All names
     * starting with com.ebmwebsourcing. are reserved for features defined by
     * the specification. It is recommended that implementation- specific
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
    void setFeature(FeatureConstants name, Object value) throws IllegalArgumentException;

    /**
     * Set the specified features. See
     * {@link #setFeature(FeatureConstants, Object)} for more information on
     * available features.
     * 
     * @param features
     *            Set of features to set.
     */
    void setFeatures(final Map<FeatureConstants, Object> features);

    /**
     * Gets the value of the specified feature.
     * 
     * @param name
     *            the name of the feature to get the value of.
     * @return the value of feature
     * @throws IllegalArgumentException
     *             if the feature name is not recognized.
     * @see #setFeature(String, boolean)
     */
    Object getFeature(FeatureConstants name);

    /**
     * Gets all features.
     * 
     * @return the features
     * @see #setFeature(String, boolean)
     */
    Map<FeatureConstants, Object> getFeatures();

    /**
     * <p>
     * Read the XMLSchema definition available at the location identified by the
     * specified URL, and bind it into a {@link Schema} object.
     * </p>
     * <p>
     * <b>Note</b>: all relative URIs are resolved according to the specified
     * URL.
     * </p>
     * 
     * @param schemaURL
     *            an URL pointing to a XMLSchema definition.
     * @return the {@link Schema} definition.
     * @throws SchemaException
     *             An error occurs during the parsing or the binding of the
     *             XMLSchema
     * @throws URISyntaxException
     *             If the URL is not formatted strictly according to to RFC2396
     *             and cannot be converted to a URI.
     * @throws IOException
     *             An I/O error occurs openning the URL stream.
     */
	Schema read(final URL schemaURL) throws SchemaException, URISyntaxException, IOException;

	/**
     * <p>
     * Read the XMLSchema definition accessible via the specified DOM
     * {@link Document}, and bind it into a {@link Schema} object.
     * </p>
     * <p>
     * <b>Note</b>: To be able to resolve relative URIs, the {@link Document}
     * base URI must be set.
     * </p>
     * 
     * @param document
     *            a DOM {@link Document} pointing to a XMLSchema definition.
     * @return the {@link Schema} definition.
     * @throws SchemaException
     *             An error occurs during the parsing or the binding of the
     *             XMLSchema
     */
	Schema read(final Document document) throws SchemaException;

    /**
     * <p>
     * Read the XMLSchema definition accessible via the specified
     * {@link InputSource}, and bind it into a {@link Schema} object.
     * </p>
     * <p>
     * <b>Note</b>: To be able to resolve relative URIs, the {@link InputSource}
     * system identifier must be set.
     * </p>
     * 
     * @param inputSource
     *            an {@link InputSource} pointing to a XMLSchema definition.
     * @return the {@link Schema} definition.
     * @throws SchemaException
     *             An error occurs during the parsing or the binding of the
     *             XMLSchema
     * @throws URISyntaxException
     *             If the system identifier URL is not formatted strictly
     *             according to to RFC2396 and cannot be converted to a URI.
     * @throws MalformedURLException
     *             The system identifier URL is not a well-formed URL
     */
    Schema read(final InputSource inputSource)
            throws SchemaException, URISyntaxException, MalformedURLException;

    /**
     * Set the document base URI of the reader to be able to resolve imported
     * parts.
     * 
     * @param documentBaseURI
     */
    void setDocumentBaseURI(final URI documentBaseURI);

    /**
     * Get the document base URI of the reader.
     * 
     * @return The document base URI.
     */
    URI getDocumentBaseURI();
}
