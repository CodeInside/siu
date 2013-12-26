/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.vaadin.customfield;

import com.vaadin.data.*;
import com.vaadin.data.Validator.InvalidValueException;

import java.util.*;

@SuppressWarnings("serial")
public abstract class PropertyConverter<PC, FC> implements Property,
        Property.ValueChangeNotifier, Property.ValueChangeListener,
        Property.ReadOnlyStatusChangeListener,
        Property.ReadOnlyStatusChangeNotifier, Property.Viewer, Validatable {

    /** The list of validators. Def null */
    private List<Validator> validators;

    /** Are invalid values allowed in fields ? */
    private boolean invalidAllowed = true;

    /** Internal list of registered value change listeners. */
    private final LinkedList<ValueChangeListener> valueChangeListeners
		    = new LinkedList<ValueChangeListener>();

    /** Internal list of registered read-only status change listeners. */
    private final LinkedList<Property.ReadOnlyStatusChangeListener> readOnlyStatusChangeListeners
		    = new LinkedList<ReadOnlyStatusChangeListener>();

    /** Datasource that stores the actual value. */
    private Property dataSource;

    private Class<? extends PC> propertyClass;


    /**
     * Construct a new {@code PropertyConverter} that is not connected to any
     * data source. Call {@link #setPropertyDataSource(Property)} later on to
     * attach it to a property.
     */
    protected PropertyConverter(Class<? extends PC> propertyClass) {
        this.propertyClass = propertyClass;
    }//new

    /**
     * Construct a new formatter that is connected to given data source. Calls
     * {@link #format(Object)} which can be a problem if the formatter has not
     * yet been initialized.
     * 
     * @param propertyDataSource
     *            to connect this property to.
     */
    public PropertyConverter(Property propertyDataSource) {
        setPropertyDataSource(propertyDataSource);
        propertyClass = (Class<? extends PC>) propertyDataSource.getType();
    }//new


    /**
     * Gets the current data source of the formatter, if any.
     * 
     * @return the current data source as a Property, or <code>null</code> if
     *         none defined.
     */
    public Property getPropertyDataSource() {
        return dataSource;
    }

    /**
     * Sets the specified Property as the data source for the formatter.
     * 
     * <p>
     * Remember that new data sources getValue() must return objects that are
     * compatible with parse() and format() methods.
     * </p>
     * 
     * @param newDataSource
     *            the new data source Property.
     */
    public void setPropertyDataSource(Property newDataSource) {

        boolean readOnly = false;
        String prevValue = null;

        if (dataSource != null) {
            if (dataSource instanceof Property.ValueChangeNotifier) {
                ((Property.ValueChangeNotifier) dataSource)
                        .removeListener(this);
            }
            if (dataSource instanceof Property.ReadOnlyStatusChangeNotifier) {
                ((Property.ReadOnlyStatusChangeNotifier) dataSource)
                        .removeListener(this);
            }
            readOnly = isReadOnly();
            prevValue = toString();
        }

        dataSource = newDataSource;

        if (dataSource != null) {
            if (dataSource instanceof Property.ValueChangeNotifier) {
                ((Property.ValueChangeNotifier) dataSource).addListener(this);
            }
            if (dataSource instanceof Property.ReadOnlyStatusChangeNotifier) {
                ((Property.ReadOnlyStatusChangeNotifier) dataSource)
                        .addListener(this);
            }
        }

        if (isReadOnly() != readOnly) {
            fireReadOnlyStatusChange();
        }
        String newVal = toString();
        if ((prevValue == null && newVal != null)
                || (prevValue != null && !prevValue.equals(newVal))) {
            fireValueChange();
        }
    }//setPropertyDataSource

    public Class<? extends PC> getType() {
        return propertyClass;
    }

    /**
     * Get the formatted value.
     * 
     * @return If the datasource returns null, this is null. Otherwise this is
     *         given by format().
     */
    public Object getValue() {
        if (dataSource == null) {
            return null;
        }
        return format((PC) dataSource.getValue());
    }

    /**
     * Get the formatted value.
     * 
     * @return If the datasource returns null, this is null. Otherwise this is a
     *         String based on the result of format().
     */
    public String toString() {
        PC value = dataSource == null ? null : (PC) dataSource.getValue();
        if (value == null) {
            return null;
        }
        FC formattedValue = format(value);
        return String.valueOf(formattedValue);
    }

    /**
     * Reflects the read-only status of the datasource. If there is no data
     * source, returns false.
     */
    public boolean isReadOnly() {
        return dataSource != null && dataSource.isReadOnly();
    }

    /**
     * This method must be implemented to format the values received from a data
     * source for use in a field.
     * 
     * @see #parse(Object)
     * 
     * @param propertyValue
     *            Value object from the datasource. This is null or of a type
     *            compatible with getType() of the datasource.
     * @return
     */
    public abstract FC format(PC propertyValue);

    /**
     * Parse a value from a field and convert it to format compatible with
     * datasource.
     * 
     * The method is required to assure that parse(format(x)) equals x.
     * 
     * @param fieldValue
     *            field value to convert
     * @return value compatible with datasource
     * @throws ConversionException
     */
    public abstract PC parse(FC fieldValue) throws ConversionException;

    /**
     * Sets the Property's read-only mode to the specified status.
     * 
     * @param newStatus
     *            the new read-only status of the Property.
     */
    public void setReadOnly(boolean newStatus) {
        if (dataSource != null) {
            dataSource.setReadOnly(newStatus);
        }
    }

    public void setValue(Object newValue) throws ReadOnlyException,
            ConversionException {
        if (dataSource == null) {
            return;
        }
        try {
            // null is just an ordinary value
            PC convertedValue = parse((FC) newValue);
            dataSource.setValue(convertedValue);
            if (convertedValue == null ? getValue() != null : !convertedValue
                    .equals(getValue())) {
                fireValueChange();
            }
        } catch (ConversionException e) {
	        throw e;//just re-throw as is
        } catch (Exception e) {
          throw new ConversionException(e);
        }
    }//setValue

    // Value change and read-only status listeners and notifications

    /**
     * An <code>Event</code> object specifying the ObjectProperty whose value
     * has changed.
     */
    private static class ValueChangeEvent extends java.util.EventObject implements
            Property.ValueChangeEvent {

        /**
         * Constructs a new value change event for this object.
         *
         * @param source
         *            the source object of the event.
         */
        protected ValueChangeEvent(PropertyConverter<?,?> source) {
            super(source);
        }//new

        public Property getProperty() {
            return (Property) getSource();
        }
    }//ValueChangeEvent

    /**
     * An <code>Event</code> object specifying the Property whose read-only
     * status has been changed.
     */
    private static class ReadOnlyStatusChangeEvent extends java.util.EventObject
            implements Property.ReadOnlyStatusChangeEvent {

        /**
         * Constructs a new read-only status change event for this object.
         * 
         * @param source
         *            source object of the event
         */
        protected ReadOnlyStatusChangeEvent(PropertyConverter<?,?> source) {
            super(source);
        }//new

        public Property getProperty() {
            return (Property) getSource();
        }
    }//ReadOnlyStatusChangeEvent

    /**
     * Removes a previously registered value change listener.
     * 
     * @param listener
     *            the listener to be removed.
     */
    public void removeListener(Property.ValueChangeListener listener) {
        valueChangeListeners.remove(listener);
    }

    /**
     * Registers a new value change listener for this ObjectProperty.
     * 
     * @param listener
     *            the new Listener to be registered
     */
    public void addListener(Property.ValueChangeListener listener) {
        valueChangeListeners.add(listener);
    }

    /**
     * Registers a new read-only status change listener for this Property.
     * 
     * @param listener
     *            the new Listener to be registered
     */
    public void addListener(Property.ReadOnlyStatusChangeListener listener) {
        readOnlyStatusChangeListeners.add(listener);
    }

    /**
     * Removes a previously registered read-only status change listener.
     * 
     * @param listener
     *            the listener to be removed.
     */
    public void removeListener(Property.ReadOnlyStatusChangeListener listener) {
        readOnlyStatusChangeListeners.remove(listener);
    }

    /**
     * Sends a value change event to all registered listeners.
     */
    protected void fireValueChange() {
        final ValueChangeListener[] listeners = valueChangeListeners
                .toArray(new ValueChangeListener[valueChangeListeners.size()]);
        final Property.ValueChangeEvent event = new ValueChangeEvent(this);
        for (ValueChangeListener listener : listeners) {
            listener.valueChange(event);
        }
    }

    /**
     * Sends a read only status change event to all registered listeners.
     */
    protected void fireReadOnlyStatusChange() {
        final ReadOnlyStatusChangeListener[] listeners = readOnlyStatusChangeListeners
                .toArray(new ReadOnlyStatusChangeListener[readOnlyStatusChangeListeners
                        .size()]);
        final Property.ReadOnlyStatusChangeEvent event = new ReadOnlyStatusChangeEvent(
                this);
        for (ReadOnlyStatusChangeListener listener : listeners) {
            listener.readOnlyStatusChange(event);
        }
    }

    /**
     * Listens for changes in the datasource.
     * 
     * This should not be called directly.
     */
    public void valueChange(Property.ValueChangeEvent event) {
        fireValueChange();
    }

    /**
     * Listens for changes in the datasource.
     * 
     * This should not be called directly.
     */
    public void readOnlyStatusChange(Property.ReadOnlyStatusChangeEvent event) {
        fireReadOnlyStatusChange();
    }

    // Validatable

    public void addValidator(Validator validator) {
        if (validators == null) {
            validators = new LinkedList<Validator>();
        }
        validators.add(validator);
    }

    public void removeValidator(Validator validator) {
        if (validators != null) {
            validators.remove(validator);
        }
    }

    public Collection<Validator> getValidators() {
        if (validators == null || validators.isEmpty()) {
            return Collections.emptySet();//caller friendly
        }
        return Collections.unmodifiableCollection(validators);
    }

    @SuppressWarnings("unchecked")
    public boolean isValid() {
        if (validators == null || dataSource == null) {
            return true;
        }

        final Object value = getPropertyDataSource().getValue();

        if (value == null || getType().isAssignableFrom(value.getClass())) {
            return isValid((PC) value);
        } else {
            return false;
        }
    }

    public boolean isValid(PC value) {
        if (validators == null) {
            return true;
        }

        for (Validator validator : validators) {
            if (!validator.isValid(value)) {
                return false;
            }
        }

        return true;
    }

    @SuppressWarnings("unchecked")
    public void validate() throws Validator.InvalidValueException {
        if (validators == null || dataSource == null) {
            return;
        }
        final Object value = getPropertyDataSource().getValue();
        if (value == null || getType().isAssignableFrom(value.getClass())) {
            validate((PC) value);
        }
    }

    public void validate(PC value) throws Validator.InvalidValueException {
        // If there is no validator, there can not be any errors
        if (validators == null) {
            return;
        }

        // Initialize temps
        List<InvalidValueException> errors = new ArrayList<InvalidValueException>();
        // validate the underlying value, not the formatted value

        // Gets all the validation errors
				for (Validator validator : validators) {
					try {
						validator.validate(value);
					} catch (Validator.InvalidValueException e) {
						errors.add(e);
					}
				}

        // If there were no error
        if (errors.isEmpty()) {
            return;
        }

        // If only one error occurred, throw it forwards
        if (errors.size() == 1) {
            throw errors.get(0);
        }

        // Creates composite validation exception
	      final Validator.InvalidValueException[] exceptions =
			    errors.toArray(new Validator.InvalidValueException[errors.size()]);

        throw new Validator.InvalidValueException(null, exceptions);
    }

    public boolean isInvalidAllowed() {
        return invalidAllowed;
    }

    public void setInvalidAllowed(boolean invalidAllowed) {
        this.invalidAllowed = invalidAllowed;
    }

}
