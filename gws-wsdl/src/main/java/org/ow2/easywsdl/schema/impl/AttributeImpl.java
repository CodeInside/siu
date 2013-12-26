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

import org.ow2.easywsdl.u.NotImplementedException;
import org.ow2.easywsdl.schema.SchemaFactory;
import org.ow2.easywsdl.schema.api.Attribute;
import org.ow2.easywsdl.schema.api.Type;
import org.ow2.easywsdl.schema.api.absItf.AbsItfType;
import org.ow2.easywsdl.schema.api.abstractElmt.AbstractAttributeImpl;
import org.ow2.easywsdl.schema.api.abstractElmt.AbstractSchemaElementImpl;


/**
* @author Nicolas Salatge - eBM WebSourcing
*/
public class AttributeImpl extends
        AbstractAttributeImpl<org.ow2.easywsdl.schema.org.w3._2001.xmlschema.Attribute, Type>
        implements Attribute {

    /**
	 *
	 */
    private static final long serialVersionUID = 1L;

    /**
     *
     * @param model
     * @param parent
     */
    public AttributeImpl(
            final org.ow2.easywsdl.schema.org.w3._2001.xmlschema.Attribute model,
            final AbstractSchemaElementImpl parent) {
        super(model, parent);

        if (SchemaFactory.getDefaultSchema() != null) {
            // type define in parent
            for (final Object obj : parent.getSchema().getTypes()) {
                if (obj instanceof AbsItfType) {
                    final AbsItfType t = (AbsItfType) obj;
                    if (t.getQName().equals(this.model.getType())) {
                        this.type = (Type) t;
                        break;
                    }
                }
            }

            // type define in default parent
            if (this.type == null) {
                if (SchemaFactory.getDefaultSchema().getTypes() != null) {
                    for (final Object obj : SchemaFactory.getDefaultSchema().getTypes()) {
                        final AbsItfType t = (AbsItfType) obj;
                        if (t.getQName().equals(this.model.getType())) {
                            this.type = (Type) t;
                            break;
                        }
                    }
                }
            }
        }

        // if referenced attribute
        if (this.model.getRef() != null) {
            this.referencedAttribute = this.parent.getSchema().getAttribute(this.model.getRef());
        }

    }

    public String getName() {
        String name = null;
        if (this.model.getName() != null) {
            name = this.model.getName();
        } else if (this.referencedAttribute != null) {
            name = this.referencedAttribute.getName();
        }
        return name;
    }

    public String getNamespaceUri() {
        String ns = null;
        if (this.model.getName() != null) {
            ns = this.parent.getSchema().getTargetNamespace();
        } else if (this.referencedAttribute != null) {
            ns = this.referencedAttribute.getNamespaceUri();
        }
        return ns;
    }

    public String getValue() {
        throw new NotImplementedException();
    }

	public Use getUse() {
		Use res = Use.OPTIONAL;
		if(this.model.getUse() != null) {
			res = Use.value(this.model.getUse());
		}
		return res;
	}

}
