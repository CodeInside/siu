package net.mobidom.bp.beans.form;

import java.util.Date;

import com.vaadin.ui.Field;
import com.vaadin.ui.PopupDateField;

public class DatePropertyFieldDescriptor extends PropertyFieldDescriptor<Date> {

  protected String dateFormat;

  public DatePropertyFieldDescriptor(String id, String caption, Date value) {
    super(id, caption, value, Date.class);
  }

  public DatePropertyFieldDescriptor(String id, String caption, Date value, String dateFormat) {
    super(id, caption, value, Date.class);
    this.dateFormat = dateFormat;
  }

  @Override
  public Field getField() {
    PopupDateField field = new PopupDateField(getProperty());
    field.setCaption(caption);
    field.setImmediate(true);
    field.setRequired(true);
    if (dateFormat != null && !dateFormat.isEmpty()) {
      field.setDateFormat(dateFormat);
    }
    return field;
  }

  @Override
  public Date getValue() {
    return (Date) getProperty().getValue();
  }

}