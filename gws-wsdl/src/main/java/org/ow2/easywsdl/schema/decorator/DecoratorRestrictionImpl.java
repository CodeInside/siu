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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;

import org.ow2.easywsdl.schema.api.SchemaException;
import org.ow2.easywsdl.schema.api.absItf.AbsItfEnumeration;
import org.ow2.easywsdl.schema.api.absItf.AbsItfRestriction;


/**
 * @author Nicolas Boissel-Dallier - eBM WebSourcing
 */
public abstract class DecoratorRestrictionImpl<En extends AbsItfEnumeration> extends Decorator<AbsItfRestriction> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<En> enumerations = new ArrayList<En>();
	
	public DecoratorRestrictionImpl(final AbsItfRestriction restriction, Class<? extends En> enumImpl) throws SchemaException{
		this.internalObject = restriction;
		
		List<En> oldEnums = restriction.getEnumerations();

		try {
			if(!oldEnums.isEmpty()) {
				Iterator<En> iEnum = oldEnums.iterator();
				while(iEnum.hasNext()){
					enumerations.add((En) enumImpl.getConstructors()[0].newInstance(iEnum.next()));
				}
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

	public List<En> getEnumerations(){
		return this.enumerations;
	}

	public void addEnumeration(En enumeration){
		this.enumerations.add(enumeration);
		this.internalObject.addEnumeration((AbsItfEnumeration)((Decorator)enumeration).getInternalObject());
	}

	public En createEnumeration(){
		return (En) this.internalObject.createEnumeration();
	}
	
	public QName getBase(){
		return this.internalObject.getBase();
	}
	
	public void setBase(QName base){
		this.internalObject.setBase(base);
	}
}
