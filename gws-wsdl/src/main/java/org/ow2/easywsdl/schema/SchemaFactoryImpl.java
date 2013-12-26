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
package org.ow2.easywsdl.schema;

import java.util.Map;

import org.ow2.easywsdl.schema.api.Schema;
import org.ow2.easywsdl.schema.api.SchemaException;
import org.ow2.easywsdl.schema.api.SchemaReader;
import org.ow2.easywsdl.schema.api.SchemaWriter;
import org.ow2.easywsdl.schema.api.SchemaReader.FeatureConstants;
import org.ow2.easywsdl.schema.impl.SchemaImpl;
import org.ow2.easywsdl.schema.impl.SchemaReaderImpl;


/**
 * This class is a concrete implementation of the abstract class SchemaFactory.
 * Some ideas used here have been shamelessly copied from the wonderful JAXP and
 * Xerces work.
 *
 * @author Nicolas Salatge - eBM WebSourcing
 */
public class SchemaFactoryImpl extends SchemaFactory {

    /**
     * Create a new instance of a Definition, with an instance of a
     * PopulatedExtensionRegistry as its ExtensionRegistry.
     * @throws SchemaException 
     *
     * @see com.ibm.wsdl.extensions.PopulatedExtensionRegistry
     */
    @Override
    public Schema newSchema() throws SchemaException {
        return new SchemaImpl();
    }

    /**
     * Create a new instance of a SchemaReaderImpl.
     * @throws SchemaException 
     */
    @Override
    public SchemaReader newSchemaReader() throws SchemaException {
        return new org.ow2.easywsdl.schema.impl.SchemaReaderImpl();
    }

    /**
     * Create a new instance of a SchemaReaderImpl.
     * @throws SchemaException 
     */
    @Override
    public SchemaReader newSchemaReader(final Map<FeatureConstants, Object> features) throws SchemaException {
        final SchemaReader reader = this.newSchemaReader();
        ((SchemaReaderImpl) reader).setFeatures(features);
        return reader;
    }

    /**
     * Create a new instance of a SchemaWriterImpl.
     * @throws SchemaException 
     */
    @Override
    public SchemaWriter newSchemaWriter() throws SchemaException {
        return new org.ow2.easywsdl.schema.impl.SchemaWriterImpl();
    }

}
