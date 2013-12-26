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
package org.ow2.easywsdl.schema.api.abstractElmt;

import java.util.ArrayList;
import java.util.List;

import org.ow2.easywsdl.schema.api.absItf.AbsItfAll;
import org.ow2.easywsdl.schema.api.absItf.AbsItfAttribute;
import org.ow2.easywsdl.schema.api.absItf.AbsItfChoice;
import org.ow2.easywsdl.schema.api.absItf.AbsItfComplexContent;
import org.ow2.easywsdl.schema.api.absItf.AbsItfComplexType;
import org.ow2.easywsdl.schema.api.absItf.AbsItfSequence;
import org.ow2.easywsdl.schema.api.absItf.AbsItfSimpleContent;
import org.ow2.easywsdl.schema.org.w3._2001.xmlschema.Annotated;

/**
 * @author Nicolas Salatge - eBM WebSourcing
 */
@SuppressWarnings("unchecked")
public abstract class AbstractComplexTypeImpl<E extends Annotated, V, A extends AbsItfAttribute, S extends AbsItfSequence, All extends AbsItfAll, Ch extends AbsItfChoice, CC extends AbsItfComplexContent, SC extends AbsItfSimpleContent>
        extends AbstractTypeImpl<E, V> implements AbsItfComplexType<A, S, All, Ch, CC, SC> {

    /**
	 *
	 */
    private static final long serialVersionUID = 1L;

    private S sequence;
    
    private All all;
    
    private Ch choice;

    private CC complexContent;

    private SC simpleContent;

    private List<A> attributes;

    
    public AbstractComplexTypeImpl(final E model, final AbstractSchemaElementImpl parent) {
        super(model, parent);
    }

    public S getSequence() {
    	return this.sequence;
    }
    
    public boolean hasSequence() {
    	return (this.sequence != null);
    }

    public void setSequence(S sequence) {
    	this.sequence = sequence;
    }
    
    public All getAll() {
    	return this.all;
    }

    public boolean hasAll() {
    	return (this.all != null);
    }
    
    public void setAll(All all) {
    	this.all = all;
    }

    public Ch getChoice() {
    	return this.choice;
    }
    
    public boolean hasChoice() {
    	return (this.choice != null);
    }

    public void setChoice(Ch choice) {
    	this.choice = choice;
    }
    
    public CC getComplexContent() {
    	return this.complexContent;
    }

    public boolean hasComplexContent() {
    	return (this.complexContent != null);
    }
    
    public void setComplexContent(CC complexContent) {
    	this.complexContent = complexContent;
    }
    
    public SC getSimpleContent() {
    	return this.simpleContent;
    }

    public boolean hasSimpleContent() {
    	return (this.simpleContent != null);
    }
    
    public void setSimpleContent(SC simpleContent) {
    	this.simpleContent = simpleContent;
    }
    
    

    /*
     * Attributes methods
     */

    /*
     * (non-Javadoc)
     *
     * @see org.ow2.easywsdl.schema.api.Schema#getAttributes()
     */
    public List<A> getAttributes() {
        if (this.attributes == null) {
            this.attributes = new ArrayList<A>();
        }
        return this.attributes;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.ow2.easywsdl.schema.api.Schema#getAttribute(javax.xml.namespace
     * .QName)
     */
    public A getAttribute(final String element) {
        A res = null;
        if (this.attributes == null) {
            this.attributes = new ArrayList<A>();
        }
        for (final A e : this.attributes) {
            if (e.getName().equals(element)) {
                res = e;
                break;
            }
        }
        return res;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.ow2.easywsdl.schema.api.Schema#addAttribute(com.ebmwebsourcing
     * .commons.schema.api.Attribute)
     */
    public void addAttribute(final A elmt) {
        if (this.attributes == null) {
            this.attributes = new ArrayList<A>();
        }
        this.attributes.add(elmt);
    }
}
