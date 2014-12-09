package net.mobidom.bp.beans.form;

import java.util.List;

import com.vaadin.ui.Field;
import com.vaadin.ui.Select;

public class SelectPropertyFieldDescriptor extends PropertyFieldDescriptor<String> {

  protected List<String> selectValues;

  public SelectPropertyFieldDescriptor(String id, String caption, String value, List<String> selectValues) {
    super(id, caption, value, String.class);
    this.selectValues = selectValues;
  }

  @Override
  public Field getField() {
    Select select = new Select(caption, selectValues);
    select.setPropertyDataSource(getProperty());
    select.setRequired(true);
    select.setImmediate(true);
    select.setNullSelectionAllowed(false);
    return select;
  }

  @Override
  public String getValue() {
    return String.valueOf(getProperty().getValue());
  }
}