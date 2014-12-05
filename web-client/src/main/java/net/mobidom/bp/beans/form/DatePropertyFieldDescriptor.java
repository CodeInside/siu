package net.mobidom.bp.beans.form;

import java.util.Date;

import com.vaadin.ui.Field;
import com.vaadin.ui.PopupDateField;

public class DatePropertyFieldDescriptor extends PropertyFieldDescriptor<Date> {

  public DatePropertyFieldDescriptor(String id, String caption, Date value) {
    super(id, caption, value, Date.class);
  }

  @Override
  public Field getField() {
    PopupDateField field = new PopupDateField(getProperty());
    field.setCaption(caption);
    field.setImmediate(true);
    return field;
  }

  @Override
  public Date getValue() {
    return (Date) getProperty().getValue();
  }

}