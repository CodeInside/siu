package net.mobidom.bp.beans.form;

import com.vaadin.ui.Field;
import com.vaadin.ui.TextField;

public class TextPropertyFieldDescriptor extends PropertyFieldDescriptor<String> {

  public TextPropertyFieldDescriptor(String id, String caption, String value) {
    super(id, caption, value, String.class);
  }

  public TextPropertyFieldDescriptor(String id, String caption, String value, boolean notRequired) {
    super(id, caption, value, String.class, notRequired);
  }

  @Override
  public Field getField() {
    TextField textField = new TextField(caption);
    textField.setPropertyDataSource(getProperty());
    textField.setImmediate(true);
    textField.setRequired(true);
    textField.setNullSettingAllowed(true);
    textField.setNullRepresentation(null);
    textField.setWidth("250px");
    return textField;
  }

}