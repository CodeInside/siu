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

import java.util.List;

import javax.xml.namespace.QName;

import org.ow2.easywsdl.schema.api.All;
import org.ow2.easywsdl.schema.api.Attribute;
import org.ow2.easywsdl.schema.api.Choice;
import org.ow2.easywsdl.schema.api.ComplexContent;
import org.ow2.easywsdl.schema.api.ComplexType;
import org.ow2.easywsdl.schema.api.Element;
import org.ow2.easywsdl.schema.api.Sequence;
import org.ow2.easywsdl.schema.api.SimpleContent;
import org.ow2.easywsdl.schema.api.abstractElmt.AbstractComplexTypeImpl;
import org.ow2.easywsdl.schema.api.abstractElmt.AbstractSchemaElementImpl;
import org.ow2.easywsdl.schema.org.w3._2001.xmlschema.Annotated;
import org.ow2.easywsdl.schema.org.w3._2001.xmlschema.ExplicitGroup;

/**
 * @author Nicolas Salatge - eBM WebSourcing
 */
public class ComplexTypeImpl
        extends
        AbstractComplexTypeImpl<org.ow2.easywsdl.schema.org.w3._2001.xmlschema.ComplexType, List<String>, Attribute, Sequence, All, Choice, ComplexContent, SimpleContent>
        implements ComplexType {

	/**
	 *
	 */
    private static final long serialVersionUID = 1L;

    public ComplexTypeImpl(
            final org.ow2.easywsdl.schema.org.w3._2001.xmlschema.ComplexType model,
            final AbstractSchemaElementImpl parent) {
        super(model, parent);

        // get attributes associated to the elements
		for (final Annotated attr : model.getAttributeOrAttributeGroup()) {
			if (attr instanceof org.ow2.easywsdl.schema.org.w3._2001.xmlschema.Attribute) {
				final Attribute a = new org.ow2.easywsdl.schema.impl.AttributeImpl(
						(org.ow2.easywsdl.schema.org.w3._2001.xmlschema.Attribute) attr,
						this);
				this.addAttribute(a);
			}
		}

        // get the sequence
        if(this.model.getSequence() != null) {
        	this.setSequence(new SequenceImpl(this.model.getSequence(), this));
        }
        
        // get the all
        if(this.model.getAll() != null) {
        	this.setAll(new AllImpl(this.model.getAll(), this));
        }
        
        // get the choice
        if(this.model.getChoice() != null) {
        	this.setChoice(new ChoiceImpl(this.model.getChoice(), this));
        }
        
        // get the complex content
        if(this.model.getComplexContent() != null) {
        	this.setComplexContent(new ComplexContentImpl(this.model.getComplexContent(), this));
        }
        
        // get the simple content
        if(this.model.getSimpleContent() != null) {
        	this.setSimpleContent(new SimpleContentImpl(this.model.getSimpleContent(), this));
        }
    }

    public QName getQName() {
        QName res = null;
        if ((this.getModel()).getName() != null) {
            res = new QName(this.parent.getSchema().getTargetNamespace(), (this.getModel()).getName());
        }
        return res;
    }

    public void setQName(QName name) {
    	this.model.setName(name.getLocalPart());
    }    

    @Override
	public void setSequence(Sequence sequence) {
		super.setSequence(sequence);
		this.model.setSequence((ExplicitGroup) ((AbstractSchemaElementImpl)this.getSequence()).getModel());
	}


	public Sequence createSequence() {
		return new SequenceImpl(new ExplicitGroup(), this);
	}

    
	public All createAll() {
		return new AllImpl(new org.ow2.easywsdl.schema.org.w3._2001.xmlschema.All(), this);
	}

    @Override
	public void setChoice(Choice choice) {
		super.setChoice(choice);
		this.model.setChoice((ExplicitGroup) ((AbstractSchemaElementImpl)this.getChoice()).getModel());
	}


	public Choice createChoice() {
		return new ChoiceImpl(new ExplicitGroup(), this);
	}
	
	@Override
	public void setComplexContent(ComplexContent complexContent) {
		super.setComplexContent(complexContent);
		this.model.setComplexContent((org.ow2.easywsdl.schema.org.w3._2001.xmlschema.ComplexContent) ((AbstractSchemaElementImpl)this.getComplexContent()).getModel());
	}

	public ComplexContent createComplexContent() {
		return new ComplexContentImpl(new org.ow2.easywsdl.schema.org.w3._2001.xmlschema.ComplexContent(), this);
	}

	@Override
	public void setSimpleContent(SimpleContent simpleContent) {
		super.setSimpleContent(simpleContent);
		this.model.setSimpleContent((org.ow2.easywsdl.schema.org.w3._2001.xmlschema.SimpleContent) ((AbstractSchemaElementImpl)this.getSimpleContent()).getModel());
	}

	public SimpleContent createSimpleContent() {
		return new SimpleContentImpl(new org.ow2.easywsdl.schema.org.w3._2001.xmlschema.SimpleContent(), this);
	}

	public void findReferencedElementIfExist() {
		if(this.getSequence() != null) {
			for(Element elmt: this.getSequence().getElements()) {
				((ElementImpl)elmt).findReferencedElementIfExist();
			}
		}
		if(this.getAll() != null) {
			for(Element elmt: this.getAll().getElements()) {
				((ElementImpl)elmt).findReferencedElementIfExist();
			}
		}
		if(this.getChoice() != null) {
			for(Element elmt: this.getChoice().getElements()) {
				((ElementImpl)elmt).findReferencedElementIfExist();
			}
		}
	}



}
