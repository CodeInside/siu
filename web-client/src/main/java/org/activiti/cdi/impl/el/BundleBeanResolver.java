package org.activiti.cdi.impl.el;

import java.beans.FeatureDescriptor;
import java.util.Iterator;

import org.activiti.engine.impl.javax.el.ELContext;
import org.activiti.engine.impl.javax.el.ELResolver;

import ru.codeinside.gses.vaadin.beans.GsesBean;
import ru.codeinside.gses.webui.Configurator;

public class BundleBeanResolver extends ELResolver {

	@Override
	public Class<?> getCommonPropertyType(ELContext context, Object base) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<?> getType(ELContext context, Object base, Object property) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getValue(ELContext context, Object base, Object property) {
		System.out.println("getValue " + context + " " + base + " " + property + " = ");
		for (GsesBean gsesBean : Configurator.getGsesBeans()) {
			System.out.println("gsesBean " + gsesBean.name());
			if (gsesBean.name().equals(property.toString())) {
				context.setPropertyResolved(true);
				return gsesBean;
			}
		}
		return null;
	}

	@Override
	public boolean isReadOnly(ELContext context, Object base, Object property) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setValue(ELContext context, Object base, Object property, Object value) {
		// TODO Auto-generated method stub

	}

}
