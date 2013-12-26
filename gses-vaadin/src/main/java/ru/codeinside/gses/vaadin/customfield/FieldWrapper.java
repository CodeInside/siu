/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.vaadin.customfield;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;




import com.vaadin.data.Property;
import com.vaadin.data.Property.ReadOnlyStatusChangeListener;
import com.vaadin.data.Property.ReadOnlyStatusChangeNotifier;
import com.vaadin.data.Validatable;
import com.vaadin.data.Validator;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.terminal.CompositeErrorMessage;
import com.vaadin.terminal.ErrorMessage;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Field;

public abstract class FieldWrapper<PC> extends CustomComponent implements
        Field, ReadOnlyStatusChangeNotifier, ReadOnlyStatusChangeListener {

    /**
     * Property converter that delegates conversions back to the containing
     * class instance.
     */
    protected class DefaultPropertyConverter extends
            PropertyConverter<PC, Object> {

        public DefaultPropertyConverter(Class<? extends PC> propertyClass) {
            super(propertyClass);
        }

        @Override
        public Object format(PC value) {
            return FieldWrapper.this.format(value);
        }

        @Override
        public PC parse(Object formattedValue) throws ConversionException {
            return FieldWrapper.this.parse(formattedValue);
        }
    }

    /**
     * The {@link Field} to which most functionality is delegated.
     */
    private Field wrappedField;

    /**
     * Property value converter or null if none is used.
     */
    private PropertyConverter<PC, ? extends Object> converter;

    /**
     * The property used, either a {@link PropertyConverter} or the wrapped
     * field.
     */
    private Property property;

    /**
     * Type of the data for the underlying property.
     */
    private Class<? extends PC> propertyType;

    /**
     * The tab order number of this field.
     */
    private int tabIndex = 0;

    /**
     * Create a custom field wrapping a {@link Field}.
     * 
     * Subclass constructors calling this constructor must create and set the
     * layout.
     * 
     * When this constructor is used, value conversions are delegated to the
     * methods {@link #format(PC)} and {@link #parse(Object)}.
     * 
     * @param wrappedField
     * @param propertyType
     */
    protected FieldWrapper(Field wrappedField, Class<? extends PC> propertyType) {
        this.wrappedField = wrappedField;
        this.propertyType = propertyType;
        converter = new DefaultPropertyConverter(propertyType);
        converter.setPropertyDataSource(wrappedField.getPropertyDataSource());
        wrappedField.setPropertyDataSource(converter);
        property = converter;
    }

    /**
     * Create a custom field wrapping a {@link Field}.
     * 
     * Subclass constructors calling this constructor must create and set the
     * layout.
     * 
     * When this constructor is used, value conversions are delegated to the
     * methods {@link #format(PC)} and {@link #parse(Object)}.
     * 
     * @param wrappedField
     * @param propertyType
     * @param layout
     *            composition root layout, which already contains the wrapped
     *            field
     */
    protected FieldWrapper(Field wrappedField,
            Class<? extends PC> propertyType, ComponentContainer layout) {
        this(wrappedField, propertyType);
        setCompositionRoot(layout);
    }

    /**
     * Create a custom field wrapping a {@link Field} with a user-defined
     * {@link PropertyConverter}.
     * 
     * Subclass constructors calling this constructor must create and set the
     * layout.
     * 
     * When this constructor is used, the methods {@link #format(PC)} and
     * {@link #parse(Object)} are never called.
     * 
     * @param wrappedField
     * @param propertyConverter
     *            or null to bypass the use of a property converter
     * @param propertyType
     */
    protected FieldWrapper(Field wrappedField,
            PropertyConverter<PC, ? extends Object> converter,
            Class<? extends PC> propertyType) {
        this.wrappedField = wrappedField;
        this.converter = converter;
        this.propertyType = propertyType;
        if (converter != null) {
            converter.setPropertyDataSource(wrappedField
                    .getPropertyDataSource());
            wrappedField.setPropertyDataSource(converter);
            property = converter;
        } else {
            property = wrappedField;
        }
    }

    /**
     * Create a custom field wrapping a {@link Field} with a user-defined
     * {@link PropertyConverter}.
     * 
     * Subclass constructors calling this constructor must create and set the
     * layout.
     * 
     * When this constructor is used, the methods {@link #format(PC)} and
     * {@link #parse(Object)} are never called.
     * 
     * @param wrappedField
     * @param propertyConverter
     *            or null to bypass the use of a property converter
     * @param propertyType
     * @param layout
     *            composition root layout, which already contains the wrapped
     *            field
     */
    protected FieldWrapper(Field wrappedField,
            PropertyConverter<PC, ? extends Object> converter,
            Class<? extends PC> propertyType, ComponentContainer layout) {
        this(wrappedField, converter, propertyType);
        setCompositionRoot(layout);
    }

    /**
     * Returns the wrapped field to which operations are delegated.
     * 
     * @return
     */
    protected Field getWrappedField() {
        return wrappedField;
    }

    /**
     * Returns the property converter performing value conversions etc.
     * 
     * By default, if no property converter is given, a
     * {@link DefaultPropertyConverter} is created, but the user can explicitly
     * specify null as the converter when calling the constructor.
     * 
     * @return property converter or null if none
     */
    protected PropertyConverter<PC, ? extends Object> getConverter() {
        return converter;
    }

    /**
     * Returns the property to which operations are delegated first if it
     * supports them. Currently, this is either the {@link PropertyConverter}
     * used or the wrapped {@link Field}.
     * 
     * This method is for internal use only.
     * 
     * @return property, not null
     */
    protected Property getProperty() {
        return property;
    }

    /**
     * Convert an underlying property value to a field value to display.
     * 
     * The default conversion uses toString(), override or specify another
     * converter to modify behavior.
     */
    protected Object format(PC value) {
        return value != null ? value.toString() : null;
    }

    /**
     * Convert a field value to an underlying property value.
     * 
     * The default is no conversion, override or specify another converter to
     * modify behavior.
     */
    protected PC parse(Object formattedValue) throws ConversionException {
        return (PC) formattedValue;
    }

    public Class<? extends PC> getType() {
        return propertyType;
    }

    @Override
    public void paintContent(PaintTarget target) throws PaintException {

        // The tab ordering number
        if (tabIndex != 0) {
            target.addAttribute("tabindex", tabIndex);
        }

        // If the field is modified, but not committed, set modified attribute
        if (isModified()) {
            target.addAttribute("modified", true);
        }

        // Adds the required attribute
        if (!isReadOnly() && isRequired()) {
            target.addAttribute("required", true);
        }

        // Hide the error indicator if needed
        if (isRequired() && isEmpty() && getComponentError() == null
                && getErrorMessage() != null) {
            target.addAttribute("hideErrors", true);
        }
        super.paintContent(target);
    }

    /**
     * Is the field empty?
     * 
     * In general, "empty" state is same as null. If the wrapped field is an
     * {@link AbstractSelect} in multiselect mode, also an empty
     * {@link Collection} is considered to be empty.
     * 
     * Override if custom functionality is needed. This method should always
     * return "true" for null values.
     */
    protected boolean isEmpty() {
        // getValue() also handles read-through mode
        Object value = getValue();
        return (value == null)
                || ((wrappedField instanceof AbstractSelect)
                        && ((AbstractSelect) wrappedField).isMultiSelect()
                        && (value instanceof Collection) && ((Collection) value)
                        .isEmpty());
    }

    @Override
    public void focus() {
        super.focus();
    }

    public boolean isInvalidCommitted() {
        return wrappedField.isInvalidCommitted();
    }

    public void setInvalidCommitted(boolean isCommitted) {
        wrappedField.setInvalidCommitted(isCommitted);
    }

    public void commit() throws SourceException, InvalidValueException {
        wrappedField.commit();
    }

    public void discard() throws SourceException {
        wrappedField.discard();
    }

    public boolean isWriteThrough() {
        return wrappedField.isWriteThrough();
    }

    public void setWriteThrough(boolean writeThrough) throws SourceException,
            InvalidValueException {
        wrappedField.setWriteThrough(writeThrough);
    }

    public boolean isReadThrough() {
        return wrappedField.isReadThrough();
    }

    public void setReadThrough(boolean readThrough) throws SourceException {
        wrappedField.setReadThrough(readThrough);
    }

    public boolean isModified() {
        return wrappedField.isModified();
    }

    public void addValidator(Validator validator) {
        if (property instanceof Validatable) {
            ((Validatable) property).addValidator(validator);
            requestRepaint();
        } else {
            wrappedField.addValidator(validator);
        }
    }

    public void removeValidator(Validator validator) {
        if (property instanceof Validatable) {
            ((Validatable) property).removeValidator(validator);
            requestRepaint();
        } else {
            wrappedField.removeValidator(validator);
        }
    }

    public Collection<Validator> getValidators() {
        if (property instanceof Validatable) {
            return ((Validatable) property).getValidators();
        } else {
            return wrappedField.getValidators();
        }
    }

    public boolean isValid() {
        if (property instanceof Validatable) {
            if (isEmpty()) {
                if (isRequired()) {
                    return false;
                } else {
                    return true;
                }
            }

            if (converter != null) {
                return converter.isValid(getValue());
            } else {
                return ((Validatable) getProperty()).isValid();
            }
        } else {
            return wrappedField.isValid();
        }
    }

    public void validate() throws InvalidValueException {
        if (property instanceof Validatable) {
            if (isEmpty()) {
                if (isRequired()) {
                    throw new Validator.EmptyValueException(getRequiredError());
                } else {
                    return;
                }
            }

            if (converter != null) {
                converter.validate(getValue());
            } else {
                ((Validatable) property).validate();
            }
        } else {
            wrappedField.validate();
        }
    }

    /**
     * Returns the error message of the component, the wrapped field and the
     * validation of this field (if it has a converter or other custom
     * property). The error messages are combined if necessary.
     * 
     * Note that the method {@link #validate()} of this component is not called
     * if there is no custom property/converter. This is to avoid duplicate
     * error messages - override this method to change the behavior if
     * necessary.
     * 
     * Note also that {@link AbstractComponent#setComponentError()} is not
     * overridden, and setting the error message for this component does not
     * affect the error message of the wrapped field. Override the
     * setComponentError() method to modify this behavior.
     * 
     * If overriding this method, see
     * {@link #getAbstractComponentErrorMessage()},
     * {@link #getValidationError()} and
     * {@link #combineErrorMessages(ErrorMessage[])}.
     */
    @Override
    public ErrorMessage getErrorMessage() {
        ErrorMessage superError = getAbstractComponentErrorMessage();

        // this is needed to get buffered source exceptions
        ErrorMessage fieldError = null;
        if (wrappedField instanceof AbstractComponent) {
            fieldError = ((AbstractComponent) wrappedField).getErrorMessage();
        }

        // should do this always, but that could lead to duplicate errors with
        // wrappedField
        ErrorMessage validationError = null;
        if (property instanceof Validatable && property != wrappedField) {
            validationError = getValidationError();
        }

        return combineErrorMessages(new ErrorMessage[] { superError,
                validationError, fieldError });
    }

    /**
     * Returns the error message of this component, without taking the wrapped
     * field into account. This is sometimes needed when overriding the behavior
     * of {@link #getErrorMessage()}.
     * 
     * @return error message of this component, ignoring the wrapped field
     */
    protected ErrorMessage getAbstractComponentErrorMessage() {
        return super.getErrorMessage();
    }

    /**
     * Perform validation of the field and return the validation error found, if
     * any.
     * 
     * @return
     */
    protected ErrorMessage getValidationError() {
        try {
            validate();
        } catch (Validator.InvalidValueException e) {
            if (!e.isInvisible()) {
                return e;
            }
        }
        return null;
    }

    /**
     * Combine multiple {@link ErrorMessage} instances into a single message,
     * using {@link CompositeErrorMessage} if necessary.
     * 
     * Any input {@link CompositeErrorMessage} instances are flattened and null
     * messages filtered out, and empty input results in the return value null.
     * 
     * @param errorMessages
     *            non-null array of error messages (may contain null)
     * @return
     */
    protected ErrorMessage combineErrorMessages(ErrorMessage[] errorMessages) {
        // combine error messages from all sources
        List<ErrorMessage> errors = new ArrayList<ErrorMessage>();

        if (errorMessages.length == 1 && errorMessages[0] != null) {
            return errorMessages[0];
        }

        for (ErrorMessage errorMessage : errorMessages) {
            if (errorMessage instanceof CompositeErrorMessage) {
                // flatten the hierarchy of composite errors
                Iterator<ErrorMessage> it = ((CompositeErrorMessage) errorMessage)
                        .iterator();
                while (it.hasNext()) {
                    // never null for CompositeErrorMessage
                    errors.add(it.next());
                }
            } else if (errorMessage != null) {
                errors.add(errorMessage);
            }
        }

        if (errors.isEmpty()) {
            return null;
        } else if (errors.size() == 1) {
            return errors.get(0);
        } else {
            return new CompositeErrorMessage(errors);
        }
    }

    public boolean isInvalidAllowed() {
        if (property instanceof Validatable) {
            return ((Validatable) property).isInvalidAllowed();
        } else {
            return wrappedField.isInvalidAllowed();
        }
    }

    public void setInvalidAllowed(boolean invalidValueAllowed)
            throws UnsupportedOperationException {
        if (property instanceof Validatable) {
            ((Validatable) property).setInvalidAllowed(invalidValueAllowed);
        } else {
            wrappedField.setInvalidAllowed(invalidValueAllowed);
        }
    }

    public PC getValue() {
        if (!isReadThrough() || isModified()) {
            // return internal value (converted)
            Object internalValue = getWrappedField().getValue();
            if (converter != null) {
                return ((PropertyConverter<PC, Object>) converter)
                        .parse(internalValue);
            } else if (internalValue != null
                    && getType().isAssignableFrom(internalValue.getClass())) {
                return (PC) internalValue;
            } else {
                return null;
            }
        } else {
            // return property value
            if (converter != null) {
                return (PC) converter.getPropertyDataSource().getValue();
            } else {
                return (PC) property.getValue();
            }
        }
    }

    public void setValue(Object newValue) throws ReadOnlyException,
            ConversionException {
        if (converter != null) {
            converter.getPropertyDataSource().setValue(newValue);
        } else {
            property.setValue(newValue);
        }
    }

    public void addListener(ValueChangeListener listener) {
        // if possible, listener for the original datasource values, not the
        // wrapped field converted values
        if (converter != null
                && converter.getPropertyDataSource() instanceof Property.ValueChangeNotifier) {
            ((Property.ValueChangeNotifier) converter.getPropertyDataSource())
                    .addListener(listener);
        } else if (property instanceof Property.ValueChangeNotifier) {
            ((Property.ValueChangeNotifier) property).addListener(listener);
        } else {
            wrappedField.addListener(listener);
        }
    }

    public void removeListener(ValueChangeListener listener) {
        // see addListener()
        if (converter != null
                && converter.getPropertyDataSource() instanceof Property.ValueChangeNotifier) {
            ((Property.ValueChangeNotifier) converter.getPropertyDataSource())
                    .removeListener(listener);
        } else if (property instanceof Property.ValueChangeNotifier) {
            ((Property.ValueChangeNotifier) property).removeListener(listener);
        } else {
            wrappedField.removeListener(listener);
        }
    }

    public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
        // this should also work if property is a PropertyConverter
        if (property instanceof Property.ValueChangeListener) {
            ((Property.ValueChangeListener) property).valueChange(event);
        } else {
            wrappedField.valueChange(event);
        }
    }

    public void addListener(Property.ReadOnlyStatusChangeListener listener) {
        if (property instanceof ReadOnlyStatusChangeNotifier) {
            ((ReadOnlyStatusChangeNotifier) property).addListener(listener);
        }
    }

    public void removeListener(Property.ReadOnlyStatusChangeListener listener) {
        if (property instanceof ReadOnlyStatusChangeNotifier) {
            ((ReadOnlyStatusChangeNotifier) property).removeListener(listener);
        }
    }

    public void readOnlyStatusChange(Property.ReadOnlyStatusChangeEvent event) {
        if (property instanceof ReadOnlyStatusChangeListener) {
            ((ReadOnlyStatusChangeListener) property)
                    .readOnlyStatusChange(event);
        }
    }

    public void setPropertyDataSource(Property newDataSource) {
        if (converter != null) {
            // note that assuming property == converter in this case
            converter.setPropertyDataSource(newDataSource);
        } else {
            wrappedField.setPropertyDataSource(newDataSource);
        }
    }

    /**
     * Returns the property data source for the field.
     * 
     * Note that this method for {@link FieldWrapper} always returns the
     * property converter, or the property data source of the wrapped field if
     * there is no converter.
     */
    public Property getPropertyDataSource() {
        if (converter != null) {
            return converter;
        } else {
            return wrappedField.getPropertyDataSource();
        }
    }

    public int getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;

    }

    public boolean isRequired() {
        if (property instanceof Field) {
            return ((Field) property).isRequired();
        } else {
            return wrappedField.isRequired();
        }
    }

    public void setRequired(boolean required) {
        if (property instanceof Field) {
            ((Field) property).setRequired(required);
        } else {
            wrappedField.setRequired(required);
        }
    }

    public void setRequiredError(String requiredMessage) {
        if (property instanceof Field) {
            ((Field) property).setRequiredError(requiredMessage);
        } else {
            wrappedField.setRequiredError(requiredMessage);
        }
    }

    public String getRequiredError() {
        if (property instanceof Field) {
            return ((Field) property).getRequiredError();
        } else {
            return wrappedField.getRequiredError();
        }
    }

    @Override
    public boolean isReadOnly() {
        return super.isReadOnly() || property.isReadOnly();
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        property.setReadOnly(readOnly);
        super.setReadOnly(readOnly);
    }

    @Override
    public void setImmediate(boolean immediate) {
        super.setImmediate(immediate);
        if (wrappedField instanceof AbstractComponent) {
            ((AbstractComponent) wrappedField).setImmediate(immediate);
        }
    }
}
