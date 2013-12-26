package org.activiti.cdi;

import org.activiti.engine.impl.javax.el.ELContext;
import org.activiti.engine.impl.javax.el.ELResolver;

import java.beans.FeatureDescriptor;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

final public class VerboseELResolver extends ELResolver {

    final private Logger logger = Logger.getLogger(getClass().getName());
    final private Level level;

    public VerboseELResolver(Level level) {
        this.level = level;
    }

    @Override
    public void setValue(ELContext context, Object base, Object property, Object value) {
        logger.log(level, "SET base:" + base + " property:" + property + " value:" + value);
    }

    @Override
    public boolean isReadOnly(ELContext context, Object base, Object property) {
        return false;
    }

    @Override
    public Object getValue(ELContext context, Object base, Object property) {
        logger.log(level, "GET base:" + base + " property:" + property);
        return null;
    }

    @Override
    public Class<?> getType(ELContext context, Object base, Object property) {
        logger.log(level, "TYPE base:" + base + " property:" + property);
        return null;
    }

    @Override
    public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {
        logger.log(level, "FD base:" + base);
        return null;
    }

    @Override
    public Class<?> getCommonPropertyType(ELContext context, Object base) {
        logger.log(level, "TYPE base:" + base);
        return null;
    }
}