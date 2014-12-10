package net.mobidom.bp.beans.form;

import java.io.Serializable;
import java.util.List;

import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Field;
import com.vaadin.ui.Select;

@SuppressWarnings("unchecked")
public class BeanItemSelectPropertyFieldDescriptor<T> extends PropertyFieldDescriptor<T> {

  public static interface ResultExtractor<T> {
    Object getValue(T value);
  }

  protected BeanItemContainer<T> selectValues;
  protected String itemCaptionPropName;
  protected BeanItemSelectPropertyFieldDescriptor.ResultExtractor<T> extractor;

  public BeanItemSelectPropertyFieldDescriptor(String id, String caption, T value, Class<T> type, String itemCaptionPropName,
      List<T> selectValues, BeanItemSelectPropertyFieldDescriptor.ResultExtractor<T> extractor) {
    super(id, caption, value, type);
    this.itemCaptionPropName = itemCaptionPropName;
    this.selectValues = new BeanItemContainer<T>(type);
    for (T bean : selectValues) {
      this.selectValues.addBean(bean);
    }

    this.extractor = extractor;
  }

  @Override
  public Field getField() {
    Select select = new Select(caption, selectValues);
    select.setImmediate(true);
    select.setRequired(true);
    select.setNullSelectionAllowed(false);
    select.setItemCaptionMode(Select.ITEM_CAPTION_MODE_PROPERTY);
    select.setPropertyDataSource(getProperty());
    select.setItemCaptionPropertyId(itemCaptionPropName);

    return select;
  }

  @Override
  public void setValue(Serializable object) {

    T selected = null;
    for (T id : selectValues.getItemIds()) {
      BeanItem<T> item = selectValues.getItem(id);
      if (item.getBean().equals(object)) {
        selected = item.getBean();
        break;
      }
    }
    super.setValue((Serializable)selected);
  }

  @Override
  public Serializable getValue() {
    if (extractor == null) {
      return (Serializable)getProperty().getValue();
    }

    return (Serializable)(extractor.getValue((T) getProperty().getValue()));
  }
}