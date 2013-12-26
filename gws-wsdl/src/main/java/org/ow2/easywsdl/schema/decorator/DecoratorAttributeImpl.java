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
package org.ow2.easywsdl.schema.decorator;

import java.lang.reflect.InvocationTargetException;

import org.ow2.easywsdl.schema.api.SchemaException;
import org.ow2.easywsdl.schema.api.absItf.AbsItfAttribute;
import org.ow2.easywsdl.schema.api.absItf.AbsItfComplexType;
import org.ow2.easywsdl.schema.api.absItf.AbsItfSimpleType;
import org.ow2.easywsdl.schema.api.absItf.AbsItfType;
import org.ow2.easywsdl.schema.api.absItf.AbsItfAttribute.Use;


/**
 * @author Nicolas Salatge - eBM WebSourcing
 */
public abstract class DecoratorAttributeImpl<T extends AbsItfType> extends Decorator<AbsItfAttribute<T>> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private T type;

    public DecoratorAttributeImpl(final AbsItfAttribute<T> attribute, Class<? extends T> tSImpl, Class<? extends T> tCImpl) throws SchemaException {
        this.internalObject = attribute;
        try {
        	if(attribute.getType() instanceof AbsItfSimpleType) {
        		this.type = (T) tSImpl.getConstructors()[0].newInstance(this.internalObject.getType());
        	} else if(attribute.getType() instanceof AbsItfComplexType){
        		this.type = (T) tCImpl.getConstructors()[0].newInstance(this.internalObject.getType());
        	} else {
        		this.type = (T) this.internalObject.getType();
        	}
		} catch (IllegalArgumentException e) {
			throw new SchemaException(e);
		} catch (SecurityException e) {
			throw new SchemaException(e);
		} catch (InstantiationException e) {
			throw new SchemaException(e);
		} catch (IllegalAccessException e) {
			throw new SchemaException(e);
		} catch (InvocationTargetException e) {
			throw new SchemaException(e);
		}
    }
    
    public  T getType() {
        return type;
    }

    
	public void setType(T type) {
		this.type = type;
		if(type instanceof Decorator) {
			this.internalObject.setType((T) ((Decorator)type).getInternalObject());
		} else {
			this.internalObject.setType(type);
		}
	}

    public String getName() {
        return this.internalObject.getName();
    }

    public String getNamespaceUri() {
        return this.internalObject.getNamespaceUri();
    }

    public String getValue() {
        return this.internalObject.getValue();
    }

	public Use getUse() {
		return this.internalObject.getUse();
	}

}
