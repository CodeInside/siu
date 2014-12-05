package net.mobidom.bp.beans.form;

import com.vaadin.data.Property;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.ui.Field;

@SuppressWarnings("rawtypes")
public abstract class PropertyFieldDescriptor<T> {

  protected String id;
  protected String caption;
  protected T value;
  protected Class<?> type;
  protected Property property;

  @SuppressWarnings("unchecked")
  public PropertyFieldDescriptor(String id, String caption, T value, Class<?> type) {
    this.id = id;
    this.caption = caption;
    this.value = value;
    this.type = type;

    this.property = new ObjectProperty(value, type);
  }

  @SuppressWarnings("unchecked")
  public Object getValue() {
    return (T) property.getValue();
  }

  public abstract Field getField();

  protected Property getProperty() {
    return property;
  }
}