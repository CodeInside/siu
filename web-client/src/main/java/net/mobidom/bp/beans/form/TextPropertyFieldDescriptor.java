package net.mobidom.bp.beans.form;

import com.vaadin.ui.Field;
import com.vaadin.ui.TextField;

public class TextPropertyFieldDescriptor extends PropertyFieldDescriptor<String> {

  public TextPropertyFieldDescriptor(String id, String caption, String value) {
    super(id, caption, value, String.class);
  }

  @Override
  public Field getField() {
    TextField textField = new TextField(caption);
    textField.setPropertyDataSource(getProperty());
    textField.setImmediate(true);
    textField.setRequired(true);
    textField.setNullRepresentation("");
    textField.setWidth("250px");
    return textField;
  }

}