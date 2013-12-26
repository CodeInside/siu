/**
 * easySchema - SOA Tools Platform.
 * Copyright (c) 2008 EBM Websourcing, http://www.ebmwebsourcing.com/
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * -------------------------------------------------------------------------
 * $id.java
 * -------------------------------------------------------------------------
 */
package org.ow2.easywsdl.schema.api.abstractElmt;

import org.ow2.easywsdl.schema.api.absItf.AbsItfAttribute;
import org.ow2.easywsdl.schema.api.absItf.AbsItfRestriction;
import org.ow2.easywsdl.schema.api.absItf.AbsItfSimpleType;
import org.ow2.easywsdl.schema.org.w3._2001.xmlschema.Annotated;

/**
 * @author Nicolas Salatge - eBM WebSourcing
 */
@SuppressWarnings("unchecked")
public abstract class AbstractSimpleTypeImpl<E extends Annotated, V, A extends AbsItfAttribute, R extends AbsItfRestriction>
        extends AbstractTypeImpl<E, V> implements AbsItfSimpleType<A, R> {

    /**
	 *
	 */
    private static final long serialVersionUID = 1L;

    protected R restriction = null;
    
    public AbstractSimpleTypeImpl(final E model, final AbstractSchemaElementImpl parent) {
        super(model, parent);
    }
    
    public R getRestriction(){
    	return this.restriction;
    }

    public void setRestriction(R restriction){
    	this.restriction = restriction;
    }
}
