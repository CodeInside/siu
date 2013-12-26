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
package org.ow2.easywsdl.schema.decorator;

import java.util.Map;

import javax.xml.namespace.QName;

import org.ow2.easywsdl.schema.api.Documentation;
import org.ow2.easywsdl.schema.api.SchemaElement;
import org.ow2.easywsdl.schema.api.XmlException;
import org.ow2.easywsdl.schema.api.abstractElmt.AbstractSchemaElementImpl;


/**
 * @author Nicolas Salatge - eBM WebSourcing
 */
public abstract class Decorator<IO extends SchemaElement> extends AbstractSchemaElementImpl{

	protected IO internalObject;
	
    public Documentation createDocumentation() {
        return this.internalObject.createDocumentation();
    }

    public Documentation getDocumentation() {
        return this.internalObject.getDocumentation();
    }

    public Map<QName, String> getOtherAttributes() throws XmlException {
        return this.internalObject.getOtherAttributes();
    }

    public void setDocumentation(final Documentation doc) {
        this.internalObject.setDocumentation(doc);
    }

	public Object getModel() {
		return ((AbstractSchemaElementImpl)this.internalObject).getModel();
	}

	public AbstractSchemaElementImpl getParent() {
		return ((AbstractSchemaElementImpl)this.internalObject).getParent();
	}

	public SchemaElement getInternalObject() {
		return this.internalObject;
	}
	
    public boolean equals(Object o){
    	return this.internalObject.equals(o);
    }
    
    public int hashCode(){
    	return this.internalObject.hashCode();
    }
}
