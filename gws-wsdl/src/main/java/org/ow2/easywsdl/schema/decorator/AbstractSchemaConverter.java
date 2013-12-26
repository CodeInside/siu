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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.ow2.easywsdl.schema.api.SchemaException;
import org.ow2.easywsdl.schema.api.absItf.AbsItfAnnotation;
import org.ow2.easywsdl.schema.api.absItf.AbsItfAttribute;
import org.ow2.easywsdl.schema.api.absItf.AbsItfAttributeGroup;
import org.ow2.easywsdl.schema.api.absItf.AbsItfComplexType;
import org.ow2.easywsdl.schema.api.absItf.AbsItfElement;
import org.ow2.easywsdl.schema.api.absItf.AbsItfGroup;
import org.ow2.easywsdl.schema.api.absItf.AbsItfImport;
import org.ow2.easywsdl.schema.api.absItf.AbsItfInclude;
import org.ow2.easywsdl.schema.api.absItf.AbsItfNotation;
import org.ow2.easywsdl.schema.api.absItf.AbsItfRedefine;
import org.ow2.easywsdl.schema.api.absItf.AbsItfSchema;
import org.ow2.easywsdl.schema.api.absItf.AbsItfSimpleType;
import org.ow2.easywsdl.schema.api.absItf.AbsItfType;


/**
 * @author Nicolas Salatge - eBM WebSourcing
 */
public abstract class AbstractSchemaConverter<SNew extends AbsItfSchema, SImpl extends DecoratorSchemaImpl, AnnNew extends AbsItfAnnotation, AnnImpl extends DecoratorAnnotationImpl, AttGNew extends AbsItfAttributeGroup, AttGImpl extends DecoratorAttributeGroupImpl, AttNew extends AbsItfAttribute, AttImpl extends DecoratorAttributeImpl, CTNew extends AbsItfType, CTImpl extends DecoratorComplexTypeImpl, ENew extends AbsItfElement, EImpl extends DecoratorElementImpl, GNew extends AbsItfGroup, GImpl extends DecoratorGroupImpl, ImptNew extends AbsItfImport, ImptImpl extends DecoratorImportImpl, InclNew extends AbsItfInclude, InclImpl extends DecoratorIncludeImpl, NotNew extends AbsItfNotation, NotImpl extends DecoratorNotationImpl, RedNew extends AbsItfRedefine, RedImpl extends DecoratorRedefineImpl, STNew extends AbsItfType, STImpl extends DecoratorSimpleTypeImpl, TNew extends AbsItfType, TImpl extends DecoratorTypeImpl> {

    public AbstractSchemaConverter() {

    }

    public abstract SNew convertSchema(AbsItfSchema desc) throws SchemaException;

    public abstract ImptNew convertImport(AbsItfImport desc) throws SchemaException;

    @SuppressWarnings("unchecked")
    protected SNew convertSchema(final AbsItfSchema odlDesc, final Class<SImpl> simpl, final Class<AnnImpl> annImpl,
            final Class<AttGImpl> attGImpl, final Class<AttImpl> attImpl, final Class<CTImpl> cTImpl,
            final Class<EImpl> eImpl, final Class<GImpl> gImpl, final Class<ImptImpl> imptImpl,
            final Class<InclImpl> inclImpl, final Class<NotImpl> notImpl, final Class<RedImpl> redImpl,
            final Class<STImpl> sTImpl, final Class<TImpl> tImpl) throws SchemaException {
        SNew newDesc = null;
        try {
            // create new parent
            final Constructor c = simpl.getConstructors()[0];
            newDesc = (SNew) c.newInstance(odlDesc);

            // add elements
            this.convertElements(odlDesc.getElements(), eImpl, newDesc);

            // add attribute
            this.convertAttributes(odlDesc.getAttributes(), attImpl, newDesc);

            // add type
            this.convertTypes(odlDesc.getTypes(), tImpl, sTImpl, cTImpl, newDesc);

            // include
            this.convertIncludes(odlDesc.getIncludes(), inclImpl, newDesc);

            // import
            this.convertImports(odlDesc.getImports(), imptImpl, newDesc);

            // TODO: redefine and others parent element

        } catch (final IllegalArgumentException e) {
            throw new SchemaException(e);
        } catch (final InstantiationException e) {
            throw new SchemaException(e);
        } catch (final IllegalAccessException e) {
            throw new SchemaException(e);
        } catch (final InvocationTargetException e) {
            throw new SchemaException(e);
        }
        return newDesc;
    }


    /**
     * Convert all wsdl element
     */

    protected ENew convertElement(final AbsItfElement oldElement, final Class<EImpl> eimpl)
            throws SchemaException {
        ENew newE = null;
        try {
            final Constructor c = eimpl.getConstructors()[0];
            newE = (ENew) c.newInstance(oldElement);
        } catch (final IllegalArgumentException e) {
            throw new SchemaException(e);
        } catch (final InstantiationException e) {
            throw new SchemaException(e);
        } catch (final IllegalAccessException e) {
            throw new SchemaException(e);
        } catch (final InvocationTargetException e) {
            throw new SchemaException(e);
        }
        return newE;
    }

    protected void convertElements(final List<AbsItfElement> elements, final Class<EImpl> eimpl, final SNew newDesc)
            throws SchemaException {
        if (elements != null) {
            final List<ENew> elmts = new ArrayList<ENew>();
            final Iterator<AbsItfElement> it = elements.iterator();
            AbsItfElement oldElement = null;
            ENew newElement = null;
            while (it.hasNext()) {
                oldElement = it.next();

                newElement = this.convertElement(oldElement, eimpl);

                elmts.add(newElement);
            }
            newDesc.getElements().clear();
            newDesc.getElements().addAll(elmts);
        }
    }

    protected AttNew convertAttribute(final AbsItfAttribute oldAttribute, final Class<AttImpl> attImpl)
            throws SchemaException {
        AttNew newAtt = null;
        try {
            final Constructor c = attImpl.getConstructors()[0];
            newAtt = (AttNew) c.newInstance(oldAttribute);
        } catch (final IllegalArgumentException e) {
            throw new SchemaException(e);
        } catch (final InstantiationException e) {
            throw new SchemaException(e);
        } catch (final IllegalAccessException e) {
            throw new SchemaException(e);
        } catch (final InvocationTargetException e) {
            throw new SchemaException(e);
        }
        return newAtt;
    }

    protected void convertAttributes(final List<AbsItfAttribute> attributes, final Class<AttImpl> attImpl,
            final SNew newDesc) throws SchemaException {
        if (attributes != null) {
            final List<AttNew> atts = new ArrayList<AttNew>();
            final Iterator<AbsItfAttribute> it = attributes.iterator();
            AbsItfAttribute oldAttribute = null;
            AttNew newAttribute = null;
            while (it.hasNext()) {
                oldAttribute = it.next();

                newAttribute = this.convertAttribute(oldAttribute, attImpl);

                atts.add(newAttribute);
            }
            newDesc.getAttributes().clear();
            newDesc.getAttributes().addAll(atts);
        }
    }

    protected STNew convertSimpleType(final AbsItfSimpleType oldSt, final Class<STImpl> sTImpl)
            throws SchemaException {
        STNew newSt = null;
        try {
            final Constructor c = sTImpl.getConstructors()[0];
            newSt = (STNew) c.newInstance(oldSt);
        } catch (final IllegalArgumentException e) {
            throw new SchemaException(e);
        } catch (final InstantiationException e) {
            throw new SchemaException(e);
        } catch (final IllegalAccessException e) {
            throw new SchemaException(e);
        } catch (final InvocationTargetException e) {
            throw new SchemaException(e);
        }
        return newSt;
    }

    protected CTNew convertComplexType(final AbsItfComplexType oldCt, final Class<CTImpl> cTImpl)
            throws SchemaException {
        CTNew newCt = null;
        try {
            final Constructor c = cTImpl.getConstructors()[0];
            newCt = (CTNew) c.newInstance(oldCt);
        } catch (final IllegalArgumentException e) {
            throw new SchemaException(e);
        } catch (final InstantiationException e) {
            throw new SchemaException(e);
        } catch (final IllegalAccessException e) {
            throw new SchemaException(e);
        } catch (final InvocationTargetException e) {
            throw new SchemaException(e);
        }
        return newCt;
    }

    protected void convertTypes(final List<AbsItfType> types, final Class<TImpl> tImpl, final Class<STImpl> sTImpl,
            final Class<CTImpl> cTImpl, final SNew newDesc) throws SchemaException {
        if (types != null) {
            final List<TNew> ts = new ArrayList<TNew>();
            final Iterator<AbsItfType> it = types.iterator();
            AbsItfType oldT = null;
            TNew newT = null;
            while (it.hasNext()) {
                oldT = it.next();
                if (oldT instanceof AbsItfSimpleType) {
                    newT = (TNew) this.convertSimpleType((AbsItfSimpleType) oldT, sTImpl);
                } else if (oldT instanceof AbsItfComplexType) {
                    newT = (TNew) this.convertComplexType((AbsItfComplexType) oldT, cTImpl);
                } else {
                    throw new SchemaException("this type is unknown: " + newT);
                }
                ts.add(newT);
            }
            newDesc.getTypes().clear();
            newDesc.getTypes().addAll(ts);
        }
    }

    protected InclNew convertInclude(final AbsItfInclude oldInclude, final Class<InclImpl> inclimpl)
            throws SchemaException {
        InclNew newIncl = null;
        try {
            final Constructor c = inclimpl.getConstructors()[0];
            newIncl = (InclNew) c.newInstance(oldInclude);
        } catch (final IllegalArgumentException e) {
            throw new SchemaException(e);
        } catch (final InstantiationException e) {
            throw new SchemaException(e);
        } catch (final IllegalAccessException e) {
            throw new SchemaException(e);
        } catch (final InvocationTargetException e) {
            throw new SchemaException(e);
        }
        return newIncl;
    }

    protected void convertIncludes(final List<AbsItfInclude> includes, final Class<InclImpl> inclimpl,
            final SNew newDesc) throws SchemaException {
        if (includes != null) {
            final List<InclNew> incls = new ArrayList<InclNew>();
            final Iterator<AbsItfInclude> it = includes.iterator();
            AbsItfInclude oldIncl = null;
            InclNew newIncl = null;
            while (it.hasNext()) {
                oldIncl = it.next();

                newIncl = this.convertInclude(oldIncl, inclimpl);

                incls.add(newIncl);
            }
            newDesc.getIncludes().clear();
            newDesc.getIncludes().addAll(incls);
        }
    }

    public ImptNew convertImport(final AbsItfImport oldImpt, final Class<ImptImpl> imptimpl)
            throws SchemaException {
        ImptNew newImpt = null;
        try {
            final Constructor c = imptimpl.getConstructors()[0];
            newImpt = (ImptNew) c.newInstance(oldImpt);
        } catch (final IllegalArgumentException e) {
            throw new SchemaException(e);
        } catch (final InstantiationException e) {
            throw new SchemaException(e);
        } catch (final IllegalAccessException e) {
            throw new SchemaException(e);
        } catch (final InvocationTargetException e) {
            throw new SchemaException(e);
        }
        return newImpt;
    }

    protected void convertImports(final List<AbsItfImport> imports, final Class<ImptImpl> imptimpl, final SNew newDesc)
            throws SchemaException {
        if (imports != null) {
            final List<ImptNew> impts = new ArrayList<ImptNew>();
            final Iterator<AbsItfImport> it = imports.iterator();
            AbsItfImport oldImpt = null;
            ImptNew newImpt = null;
            while (it.hasNext()) {
                oldImpt = it.next();

                newImpt = this.convertImport(oldImpt, imptimpl);

                impts.add(newImpt);
            }
            newDesc.getImports().clear();
            newDesc.getImports().addAll(impts);
        }
    }

}
