package net.mobidom.bp.beans.form;

import java.io.Serializable;

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
  protected boolean notRequired;

  @SuppressWarnings("unchecked")
  public PropertyFieldDescriptor(String id, String caption, T value, Class<?> type) {
    this.id = id;
    this.caption = caption;
    this.value = value;
    this.type = type;

    this.property = new ObjectProperty(value, type);
  }
  
  @SuppressWarnings("unchecked")
  public PropertyFieldDescriptor(String id, String caption, T value, Class<?> type, boolean notRequired) {
    this.id = id;
    this.caption = caption;
    this.value = value;
    this.type = type;
    this.notRequired = notRequired;

    this.property = new ObjectProperty(value, type);
  }

  @SuppressWarnings("unchecked")
  public Serializable getValue() {
	  Object result = (T) property.getValue(); 
    return (Serializable)result;
  }

  public abstract Field getField();

  protected Property getProperty() {
    return property;
  }

  public String getId() {
    return id;
  }
  

  @SuppressWarnings("unchecked")
  public void setValue(Serializable object) {
    this.value = (T) object;
    this.property.setValue(object);
  }

}