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
package org.ow2.easywsdl.wsdl.decorator;

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
import org.ow2.easywsdl.schema.api.absItf.AbsItfNotation;
import org.ow2.easywsdl.schema.api.absItf.AbsItfRedefine;
import org.ow2.easywsdl.schema.api.absItf.AbsItfSchema;
import org.ow2.easywsdl.schema.api.absItf.AbsItfSimpleType;
import org.ow2.easywsdl.schema.api.absItf.AbsItfType;
import org.ow2.easywsdl.schema.decorator.AbstractSchemaConverter;
import org.ow2.easywsdl.schema.decorator.DecoratorAnnotationImpl;
import org.ow2.easywsdl.schema.decorator.DecoratorAttributeGroupImpl;
import org.ow2.easywsdl.schema.decorator.DecoratorAttributeImpl;
import org.ow2.easywsdl.schema.decorator.DecoratorComplexTypeImpl;
import org.ow2.easywsdl.schema.decorator.DecoratorElementImpl;
import org.ow2.easywsdl.schema.decorator.DecoratorGroupImpl;
import org.ow2.easywsdl.schema.decorator.DecoratorNotationImpl;
import org.ow2.easywsdl.schema.decorator.DecoratorRedefineImpl;
import org.ow2.easywsdl.schema.decorator.DecoratorSchemaImpl;
import org.ow2.easywsdl.schema.decorator.DecoratorSimpleTypeImpl;
import org.ow2.easywsdl.schema.decorator.DecoratorTypeImpl;
import org.ow2.easywsdl.wsdl.api.WSDLException;
import org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfBinding;
import org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfBindingFault;
import org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfBindingInput;
import org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfBindingOperation;
import org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfBindingOutput;
import org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfDescription;
import org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfEndpoint;
import org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfFault;
import org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfImport;
import org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfInclude;
import org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfInput;
import org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfInterfaceType;
import org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfOperation;
import org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfOutput;
import org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfService;
import org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfTypes;


/**
 * @author Nicolas Salatge - eBM WebSourcing
 */
@SuppressWarnings("unchecked")
public abstract class AbstractWSDLConverter<
// WSDL generics
DNew extends AbsItfDescription, DImpl extends DecoratorDescriptionImpl, SNew extends AbsItfService, SImpl extends DecoratorServiceImpl, BNew extends AbsItfBinding, BImpl extends DecoratorBindingImpl, INew extends AbsItfInterfaceType, IImpl extends DecoratorInterfaceTypeImpl, InclNew extends AbsItfInclude, InclImpl extends DecoratorIncludeImpl, ImptNew extends AbsItfImport, ImptImpl extends DecoratorImportImpl, TNew extends AbsItfTypes, TImpl extends DecoratorTypesImpl, ENew extends AbsItfEndpoint, EImpl extends DecoratorEndpointImpl, ONew extends AbsItfOperation, OImpl extends DecoratorOperationImpl, InNew extends AbsItfInput, InImpl extends DecoratorInputImpl, OutNew extends AbsItfOutput, OutImpl extends DecoratorOutputImpl, FNew extends AbsItfFault, FImpl extends DecoratorFaultImpl, BONew extends AbsItfBindingOperation, BOImpl extends DecoratorBindingOperationImpl, BInNew extends AbsItfBindingInput, BInImpl extends DecoratorBindingInputImpl, BOutNew extends AbsItfBindingOutput, BOutImpl extends DecoratorBindingOutputImpl, BFNew extends AbsItfBindingFault, BFImpl extends DecoratorBindingFaultImpl,
// Schema generics
SchNew extends AbsItfSchema, SchImpl extends DecoratorSchemaImpl, AnnNew extends AbsItfAnnotation, AnnImpl extends DecoratorAnnotationImpl, AttGNew extends AbsItfAttributeGroup, AttGImpl extends DecoratorAttributeGroupImpl, AttNew extends AbsItfAttribute, AttImpl extends DecoratorAttributeImpl, CTNew extends AbsItfComplexType, CTImpl extends DecoratorComplexTypeImpl, ElmtNew extends AbsItfElement, ElmtImpl extends DecoratorElementImpl, GNew extends AbsItfGroup, GImpl extends DecoratorGroupImpl, ImptSchNew extends org.ow2.easywsdl.schema.api.absItf.AbsItfImport, ImptSchImpl extends org.ow2.easywsdl.schema.decorator.DecoratorImportImpl, InclSchNew extends org.ow2.easywsdl.schema.api.absItf.AbsItfInclude, InclSchImpl extends org.ow2.easywsdl.schema.decorator.DecoratorIncludeImpl, NotNew extends AbsItfNotation, NotImpl extends DecoratorNotationImpl, RedNew extends AbsItfRedefine, RedImpl extends DecoratorRedefineImpl, STNew extends AbsItfSimpleType, STImpl extends DecoratorSimpleTypeImpl, TSchNew extends AbsItfType, TSchImpl extends DecoratorTypeImpl> {

	protected AbstractSchemaConverter<SchNew, SchImpl, AnnNew, AnnImpl, AttGNew, AttGImpl, AttNew, AttImpl, CTNew, CTImpl, ElmtNew, ElmtImpl, GNew, GImpl, ImptSchNew, ImptSchImpl, InclSchNew, InclSchImpl, NotNew, NotImpl, RedNew, RedImpl, STNew, STImpl, TSchNew, TSchImpl> schemaConverter;

	public AbstractWSDLConverter() {
	}

	public void setSchemaConverter(
			final AbstractSchemaConverter<SchNew, SchImpl, AnnNew, AnnImpl, AttGNew, AttGImpl, AttNew, AttImpl, CTNew, CTImpl, ElmtNew, ElmtImpl, GNew, GImpl, ImptSchNew, ImptSchImpl, InclSchNew, InclSchImpl, NotNew, NotImpl, RedNew, RedImpl, STNew, STImpl, TSchNew, TSchImpl> schemaConverter) {
		this.schemaConverter = schemaConverter;
	}

	public abstract DNew convertDescription(AbsItfDescription desc) throws WSDLException;


	protected DNew convertDescription(final AbsItfDescription odlDesc, final Class<DImpl> dimpl,
			final Class<SImpl> simpl, final Class<BImpl> bimpl, final Class<IImpl> iimpl, final Class<InclImpl> inclimpl,
			final Class<ImptImpl> imptimpl, final Class<TImpl> timpl, final Class<EImpl> eimpl, final Class<OImpl> oimpl,
			final Class<InImpl> inimpl, final Class<OutImpl> outimpl, final Class<FImpl> fimpl, final Class<BOImpl> boimpl,
			final Class<BInImpl> binimpl, final Class<BOutImpl> boutimpl, final Class<BFImpl> bfimpl)
	throws WSDLException {
		DNew newDesc = null;
		try {
			// create new description
			GenericDescriptionConverter<DNew, DImpl> dc = new GenericDescriptionConverter();
			newDesc = dc.convertDescription(odlDesc, dimpl);


			// add services
			this.convertServices(odlDesc.getServices(), simpl, newDesc);
			for (final Object service : newDesc.getServices()) {
				if (service instanceof AbsItfService) {
					this.convertInternalElmtsInService((AbsItfService) service, eimpl);
				} else {
					throw new WSDLException("Incorrect service in the services list: " + service);
				}
			}

			// add bindings
			this.convertBindings(odlDesc.getBindings(), bimpl, newDesc);
			for (final Object binding : newDesc.getBindings()) {
				if (binding instanceof AbsItfBinding) {
					this.convertInternalElmtsInBinding((AbsItfBinding) binding, boimpl, binimpl,
							boutimpl, bfimpl);
				} else {
					throw new WSDLException("Incorrect binding in the bindings list: " + binding);
				}
			}

			// interfaces
			this.convertInterfaces(odlDesc.getInterfaces(), iimpl, newDesc);
			for (final Object itf : newDesc.getInterfaces()) {
				if (itf instanceof AbsItfInterfaceType) {
					this.convertInternalElmtsInInterface((AbsItfInterfaceType) itf, oimpl, inimpl,
							outimpl, fimpl);
				} else {
					throw new WSDLException("Incorrect interface in the interfaces list: " + itf);
				}
			}

			// include
			this.convertIncludes(odlDesc.getIncludes(), inclimpl, newDesc);

			// import
			this.convertImports(odlDesc.getImports(), imptimpl, newDesc);

			// types
			newDesc.setTypes(this.convertTypes(odlDesc.getTypes(), timpl));
			
			if (this.schemaConverter != null) {
				// schemas
				final List<SchNew> newSchemas = new ArrayList<SchNew>();
				for (final AbsItfSchema s : (List<AbsItfSchema>) odlDesc.getTypes().getSchemas()) {
					newSchemas.add(this.schemaConverter.convertSchema(s));
				}
				newDesc.getTypes().getSchemas().clear();
				newDesc.getTypes().getSchemas().addAll(newSchemas);

				// imported schemas
				final List<ImptSchNew> newImportedSchemas = new ArrayList<ImptSchNew>();
				for (final org.ow2.easywsdl.schema.api.absItf.AbsItfImport impt : (List<org.ow2.easywsdl.schema.api.absItf.AbsItfImport>) newDesc
						.getTypes().getImportedSchemas()) {
					newImportedSchemas.add(this.schemaConverter.convertImport(impt));
				}
				newDesc.getTypes().getImportedSchemas().clear();
				newDesc.getTypes().getImportedSchemas().addAll(newImportedSchemas);
			} else {
				throw new WSDLException("the schema converter is not setted");
			}
			

		} catch (final IllegalArgumentException e) {
			throw new WSDLException(e);
		} catch (final SchemaException e) {
			throw new WSDLException(e);
		}
		return newDesc;
	}

	private void convertInternalElmtsInService(final AbsItfService service, final Class<EImpl> eimpl)
	throws WSDLException {
		// endpoints
		this.convertEndpoints(service.getEndpoints(), eimpl);
	}

	private void convertInternalElmtsInBinding(final AbsItfBinding binding, final Class<BOImpl> boimpl,
			final Class<BInImpl> binimpl, final Class<BOutImpl> boutimpl, final Class<BFImpl> bfimpl)
	throws WSDLException {
		// binding operations
		this.convertBindingOperations(binding.getBindingOperations(), boimpl);
		for (final Object bo : binding.getBindingOperations()) {
			if (bo instanceof AbsItfBindingOperation) {
				this.convertInternalElmtsInBindingOperation((AbsItfBindingOperation) bo, binimpl,
						boutimpl, bfimpl);
			} else {
				throw new WSDLException(
						"Incorrect bindingOperation in the bindingOperations list: " + bo);
			}
		}
	}

	private void convertInternalElmtsInInterface(final AbsItfInterfaceType itf, final Class<OImpl> oimpl,
			final Class<InImpl> inimpl, final Class<OutImpl> outimpl, final Class<FImpl> fimpl) throws WSDLException {
		// operations
		this.convertOperations(itf.getOperations(), oimpl);
		for (final Object o : itf.getOperations()) {
			if (o instanceof AbsItfOperation) {
				this.convertInternalElmtsInOperation((AbsItfOperation) o, inimpl, outimpl, fimpl);
			} else {
				throw new WSDLException("Incorrect operation in the operations list: " + o);
			}
		}

	}

	private void convertInternalElmtsInBindingOperation(final AbsItfBindingOperation bo,
			final Class<BInImpl> binimpl, final Class<BOutImpl> boutimpl, final Class<BFImpl> bfimpl)
	throws WSDLException {
		// bindingInputs
		if(bo.getInput() != null) {
			bo.setInput(this.convertBindingInput(bo.getInput(), binimpl));
		}
		// bindingOutputs
		if(bo.getOutput() != null) {
			bo.setOutput(this.convertBindingOutput(bo.getOutput(), boutimpl));
		}

		// bindingFaults
		this.convertBindingFaults(bo.getFaults(), bfimpl);
	}

	private void convertInternalElmtsInOperation(final AbsItfOperation o, final Class<InImpl> inimpl,
			final Class<OutImpl> outimpl, final Class<FImpl> fimpl) throws WSDLException {
		// Inputs
		if(o.getInput() != null) {
			o.setInput(this.convertInput(o.getInput(), inimpl));
		}
		// Outputs
		if(o.getOutput() != null) {
			o.setOutput(this.convertOutput(o.getOutput(), outimpl));
		}
		// Faults
		this.convertFaults(o.getFaults(), fimpl);
	}

	/**
	 * Convert all wsdl element
	 */

	protected SNew convertService(final AbsItfService oldService, final Class<SImpl> simpl)
	throws WSDLException {
		SNew newService = null;
		try {
			final Constructor c = simpl.getConstructors()[0];
			newService = (SNew) c.newInstance(oldService);
		} catch (final IllegalArgumentException e) {
			throw new WSDLException(e);
		} catch (final InstantiationException e) {
			throw new WSDLException(e);
		} catch (final IllegalAccessException e) {
			throw new WSDLException(e);
		} catch (final InvocationTargetException e) {
			throw new WSDLException(e);
		}
		return newService;
	}

	protected void convertServices(final List<AbsItfService> services, final Class<SImpl> simpl, final DNew newDesc)
	throws WSDLException {
		if (services != null) {
			final List<SNew> servs = new ArrayList<SNew>();
			final Iterator<AbsItfService> it = services.iterator();
			AbsItfService oldService = null;
			SNew newService = null;
			while (it.hasNext()) {
				oldService = it.next();

				newService = this.convertService(oldService, simpl);

				servs.add(newService);
			}
			newDesc.getServices().clear();
			newDesc.getServices().addAll(servs);
		}
	}

	protected BNew convertBinding(final AbsItfBinding oldBinding, final Class<BImpl> bimpl)
	throws WSDLException {
		BNew newB = null;
		try {
			final Constructor c = bimpl.getConstructors()[0];
			newB = (BNew) c.newInstance(oldBinding);
		} catch (final IllegalArgumentException e) {
			throw new WSDLException(e);
		} catch (final InstantiationException e) {
			throw new WSDLException(e);
		} catch (final IllegalAccessException e) {
			throw new WSDLException(e);
		} catch (final InvocationTargetException e) {
			throw new WSDLException(e);
		}
		return newB;
	}

	protected void convertBindings(final List<AbsItfBinding> bindings, final Class<BImpl> bimpl, final DNew newDesc)
	throws WSDLException {
		if (bindings != null) {
			final List<BNew> binds = new ArrayList<BNew>();
			final Iterator<AbsItfBinding> it = bindings.iterator();
			AbsItfBinding oldBinding = null;
			BNew newBinding = null;
			while (it.hasNext()) {
				oldBinding = it.next();

				newBinding = this.convertBinding(oldBinding, bimpl);

				binds.add(newBinding);
			}
			newDesc.getBindings().clear();
			newDesc.getBindings().addAll(binds);
		}
	}

	protected INew convertInterface(final AbsItfInterfaceType oldItf, final Class<IImpl> iimpl)
	throws WSDLException {
		INew newItf = null;
		try {
			final Constructor c = iimpl.getConstructors()[0];
			newItf = (INew) c.newInstance(oldItf);
		} catch (final IllegalArgumentException e) {
			throw new WSDLException(e);
		} catch (final InstantiationException e) {
			throw new WSDLException(e);
		} catch (final IllegalAccessException e) {
			throw new WSDLException(e);
		} catch (final InvocationTargetException e) {
			throw new WSDLException(e);
		}
		return newItf;
	}

	protected void convertInterfaces(final List<AbsItfInterfaceType> interfaces, final Class<IImpl> iimpl,
			final DNew newDesc) throws WSDLException {
		if (interfaces != null) {
			final List<INew> itfs = new ArrayList<INew>();
			final Iterator<AbsItfInterfaceType> it = interfaces.iterator();
			AbsItfInterfaceType oldItf = null;
			INew newItf = null;
			while (it.hasNext()) {
				oldItf = it.next();

				newItf = this.convertInterface(oldItf, iimpl);

				itfs.add(newItf);
			}
			newDesc.getInterfaces().clear();
			newDesc.getInterfaces().addAll(itfs);
		}
	}

	protected InclNew convertInclude(final AbsItfInclude oldInclude, final Class<InclImpl> inclimpl)
	throws WSDLException {
		InclNew newIncl = null;
		try {
			final Constructor c = inclimpl.getConstructors()[0];
			newIncl = (InclNew) c.newInstance(oldInclude);
		} catch (final IllegalArgumentException e) {
			throw new WSDLException(e);
		} catch (final InstantiationException e) {
			throw new WSDLException(e);
		} catch (final IllegalAccessException e) {
			throw new WSDLException(e);
		} catch (final InvocationTargetException e) {
			throw new WSDLException(e);
		}
		return newIncl;
	}

	protected void convertIncludes(final List<AbsItfInclude> includes, final Class<InclImpl> inclimpl,
			final DNew newDesc) throws WSDLException {
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

	protected ImptNew convertImport(final AbsItfImport oldImpt, final Class<ImptImpl> imptimpl)
	throws WSDLException {
		ImptNew newImpt = null;
		try {
			final Constructor c = imptimpl.getConstructors()[0];
			newImpt = (ImptNew) c.newInstance(oldImpt);
		} catch (final IllegalArgumentException e) {
			throw new WSDLException(e);
		} catch (final InstantiationException e) {
			throw new WSDLException(e);
		} catch (final IllegalAccessException e) {
			throw new WSDLException(e);
		} catch (final InvocationTargetException e) {
			throw new WSDLException(e);
		}
		return newImpt;
	}

	protected void convertImports(final List<AbsItfImport> imports, final Class<ImptImpl> imptimpl, final DNew newDesc)
	throws WSDLException {
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

	protected TNew convertTypes(final AbsItfTypes oldTypes, final Class<TImpl> typesimpl) throws WSDLException {
		TNew newTypes = null;
		try {
			final Constructor c = typesimpl.getConstructors()[0];
			newTypes = (TNew) c.newInstance(oldTypes);
		} catch (final IllegalArgumentException e) {
			throw new WSDLException(e);
		} catch (final InstantiationException e) {
			throw new WSDLException(e);
		} catch (final IllegalAccessException e) {
			throw new WSDLException(e);
		} catch (final InvocationTargetException e) {
			throw new WSDLException(e);
		}
		return newTypes;
	}

	protected ENew convertEndpoint(final AbsItfEndpoint oldEp, final Class<EImpl> eimpl) throws WSDLException {
		ENew newItf = null;
		try {
			final Constructor c = eimpl.getConstructors()[0];
			newItf = (ENew) c.newInstance(oldEp);
		} catch (final IllegalArgumentException e) {
			throw new WSDLException(e);
		} catch (final InstantiationException e) {
			throw new WSDLException(e);
		} catch (final IllegalAccessException e) {
			throw new WSDLException(e);
		} catch (final InvocationTargetException e) {
			throw new WSDLException(e);
		}
		return newItf;
	}

	protected void convertEndpoints(final List<AbsItfEndpoint> endpoints, final Class<EImpl> eimpl)
	throws WSDLException {
		if (endpoints != null) {
			final List<ENew> eps = new ArrayList<ENew>();
			final Iterator<AbsItfEndpoint> it = endpoints.iterator();
			AbsItfEndpoint oldEp = null;
			ENew newEp = null;
			while (it.hasNext()) {
				oldEp = it.next();

				newEp = this.convertEndpoint(oldEp, eimpl);

				eps.add(newEp);
			}
			endpoints.clear();
			endpoints.addAll(eps);
		}
	}

	private ONew convertOperation(final AbsItfOperation oldOp, final Class<OImpl> oimpl) throws WSDLException {
		ONew newOp = null;
		try {
			final Constructor c = oimpl.getConstructors()[0];
			newOp = (ONew) c.newInstance(oldOp);
		} catch (final IllegalArgumentException e) {
			throw new WSDLException(e);
		} catch (final InstantiationException e) {
			throw new WSDLException(e);
		} catch (final IllegalAccessException e) {
			throw new WSDLException(e);
		} catch (final InvocationTargetException e) {
			throw new WSDLException(e);
		}
		return newOp;
	}

	private void convertOperations(final List<AbsItfOperation> operations, final Class<OImpl> oimpl)
	throws WSDLException {
		if (operations != null) {
			final List<ONew> ops = new ArrayList<ONew>();
			final Iterator<AbsItfOperation> it = operations.iterator();
			AbsItfOperation oldOp = null;
			ONew newOp = null;
			while (it.hasNext()) {
				oldOp = it.next();

				newOp = this.convertOperation(oldOp, oimpl);

				ops.add(newOp);
			}
			operations.clear();
			operations.addAll(ops);
		}
	}

	private BONew convertBindingOperation(final AbsItfBindingOperation oldBOp, final Class<BOImpl> boimpl)
	throws WSDLException {
		BONew newBOp = null;
		try {
			final Constructor c = boimpl.getConstructors()[0];
			newBOp = (BONew) c.newInstance(oldBOp);
		} catch (final IllegalArgumentException e) {
			throw new WSDLException(e);
		} catch (final InstantiationException e) {
			throw new WSDLException(e);
		} catch (final IllegalAccessException e) {
			throw new WSDLException(e);
		} catch (final InvocationTargetException e) {
			throw new WSDLException(e);
		}
		return newBOp;
	}

	private void convertBindingOperations(final List<AbsItfBindingOperation> bindingOperations,
			final Class<BOImpl> boimpl) throws WSDLException {
		if (bindingOperations != null) {
			final List<BONew> bops = new ArrayList<BONew>();
			final Iterator<AbsItfBindingOperation> it = bindingOperations.iterator();
			AbsItfBindingOperation oldBOp = null;
			BONew newBOp = null;
			while (it.hasNext()) {
				oldBOp = it.next();

				newBOp = this.convertBindingOperation(oldBOp, boimpl);

				bops.add(newBOp);
			}
			bindingOperations.clear();
			bindingOperations.addAll(bops);
		}
	}

	protected BInNew convertBindingInput(final AbsItfBindingInput oldBindingInput, final Class<BInImpl> binimpl)
	throws WSDLException {
		BInNew newBindingInput = null;
		try {
			final Constructor c = binimpl.getConstructors()[0];
			newBindingInput = (BInNew) c.newInstance(oldBindingInput);
		} catch (final IllegalArgumentException e) {
			throw new WSDLException(e);
		} catch (final InstantiationException e) {
			throw new WSDLException(e);
		} catch (final IllegalAccessException e) {
			throw new WSDLException(e);
		} catch (final InvocationTargetException e) {
			throw new WSDLException(e);
		}
		return newBindingInput;
	}

	protected BOutNew convertBindingOutput(final AbsItfBindingOutput oldBindingOutput,
			final Class<BOutImpl> boutimpl) throws WSDLException {
		BOutNew newBindingOutput = null;
		try {
			final Constructor c = boutimpl.getConstructors()[0];
			newBindingOutput = (BOutNew) c.newInstance(oldBindingOutput);
		} catch (final IllegalArgumentException e) {
			throw new WSDLException(e);
		} catch (final InstantiationException e) {
			throw new WSDLException(e);
		} catch (final IllegalAccessException e) {
			throw new WSDLException(e);
		} catch (final InvocationTargetException e) {
			throw new WSDLException(e);
		}
		return newBindingOutput;
	}

	private BFNew convertBindingFault(final AbsItfBindingFault oldBF, final Class<BFImpl> bfimpl)
	throws WSDLException {
		BFNew newBF = null;
		try {
			final Constructor c = bfimpl.getConstructors()[0];
			newBF = (BFNew) c.newInstance(oldBF);
		} catch (final IllegalArgumentException e) {
			throw new WSDLException(e);
		} catch (final InstantiationException e) {
			throw new WSDLException(e);
		} catch (final IllegalAccessException e) {
			throw new WSDLException(e);
		} catch (final InvocationTargetException e) {
			throw new WSDLException(e);
		}
		return newBF;
	}

	private void convertBindingFaults(final List<AbsItfBindingFault> bindingFaults, final Class<BFImpl> bfimpl)
	throws WSDLException {
		if (bindingFaults != null) {
			final List<BFNew> bfs = new ArrayList<BFNew>();
			final Iterator<AbsItfBindingFault> it = bindingFaults.iterator();
			AbsItfBindingFault oldBF = null;
			BFNew newBF = null;
			while (it.hasNext()) {
				oldBF = it.next();

				newBF = this.convertBindingFault(oldBF, bfimpl);

				bfs.add(newBF);
			}
			bindingFaults.clear();
			bindingFaults.addAll(bfs);
		}
	}

	protected InNew convertInput(final AbsItfInput oldInput, final Class<InImpl> inimpl) throws WSDLException {
		InNew newInput = null;
		try {
			final Constructor c = inimpl.getConstructors()[0];
			newInput = (InNew) c.newInstance(oldInput);
		} catch (final IllegalArgumentException e) {
			throw new WSDLException(e);
		} catch (final InstantiationException e) {
			throw new WSDLException(e);
		} catch (final IllegalAccessException e) {
			throw new WSDLException(e);
		} catch (final InvocationTargetException e) {
			throw new WSDLException(e);
		}
		return newInput;
	}

	protected OutNew convertOutput(final AbsItfOutput oldOutput, final Class<OutImpl> outimpl)
	throws WSDLException {
		OutNew newOutput = null;
		try {
			final Constructor c = outimpl.getConstructors()[0];
			newOutput = (OutNew) c.newInstance(oldOutput);
		} catch (final IllegalArgumentException e) {
			throw new WSDLException(e);
		} catch (final InstantiationException e) {
			throw new WSDLException(e);
		} catch (final IllegalAccessException e) {
			throw new WSDLException(e);
		} catch (final InvocationTargetException e) {
			throw new WSDLException(e);
		}
		return newOutput;
	}

	private FNew convertFault(final AbsItfFault oldF, final Class<FImpl> fimpl) throws WSDLException {
		FNew newF = null;
		try {
			final Constructor c = fimpl.getConstructors()[0];
			newF = (FNew) c.newInstance(oldF);
		} catch (final IllegalArgumentException e) {
			throw new WSDLException(e);
		} catch (final InstantiationException e) {
			throw new WSDLException(e);
		} catch (final IllegalAccessException e) {
			throw new WSDLException(e);
		} catch (final InvocationTargetException e) {
			throw new WSDLException(e);
		}
		return newF;
	}

	private void convertFaults(final List<AbsItfFault> faults, final Class<FImpl> fimpl) throws WSDLException {
		if (faults != null) {
			final List<FNew> fs = new ArrayList<FNew>();
			final Iterator<AbsItfFault> it = faults.iterator();
			AbsItfFault oldF = null;
			FNew newF = null;
			while (it.hasNext()) {
				oldF = it.next();

				newF = this.convertFault(oldF, fimpl);

				fs.add(newF);
			}
			faults.clear();
			faults.addAll(fs);
		}
	}
}
