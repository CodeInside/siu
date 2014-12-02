package net.mobidom.bp.beans.form;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.mobidom.bp.beans.request.DocumentRequest;
import net.mobidom.bp.beans.types.ТипДокумента;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.ui.Component;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;

public class FromDocumentRequestBuilder {

  public static abstract class PropertyField<T> {

    protected String caption;
    protected T value;
    protected Class<?> type;

    protected Field field;

    public abstract Field getField();

    public T getValue() {
      return value;
    }

    public ObjectProperty<T> getProperty() {
      return new ObjectProperty(value, type);
    }
  }

  public static class TextFieldProperty extends PropertyField<String> {

    public TextFieldProperty(String caption, String value) {
      this.caption = caption;
      this.value = value;
      this.type = String.class;
    }

    @Override
    public Field getField() {
      ObjectProperty<String> property = new ObjectProperty<String>(value);
      TextField textField = new TextField(caption, property);
      textField.setWidth("250px");
      return textField;
    }

  }

  public static class DatePropertyField extends PropertyField<Date> {

    public DatePropertyField(String caption, Date value) {
      this.caption = caption;
      this.value = value;
      this.type = Date.class;
    }

    @Override
    public Field getField() {
      ObjectProperty<Date> property = new ObjectProperty<Date>(value);
      PopupDateField field = new PopupDateField(property);
      field.setCaption(caption);
      return field;
    }

    @Override
    public Date getValue() {
      return (Date) field.getValue();
    }

  }

  public static class NativeSelectPropertyField extends PropertyField<String> {

    protected List<String> selectValues;

    public NativeSelectPropertyField(String caption, String value, List<String> selectValues) {
      this.caption = caption;
      this.value = value;
      this.type = String.class;
      this.selectValues = selectValues;
    }

    @Override
    public Field getField() {
      NativeSelect select = new NativeSelect(caption, selectValues);
      select.setValue(value);
      return select;
    }

    @Override
    public String getValue() {
      return String.valueOf(field.getValue());
    }
  }

  public static RequestForm createForm(final DocumentRequest documentRequest) {

    RequestForm requestForm = new RequestForm();
    if (documentRequest.getType() == ТипДокумента.НДФЛ_2) {
      requestForm.documentRequest = documentRequest;
      requestForm.form = create2NDFLForm(documentRequest);
    }

    // TODO webdom
    return requestForm;
  }

  static Form create2NDFLForm(final DocumentRequest documentRequest) {
    Form form = new Form();
    form.setCaption("2-НДФЛ");

    Map<String, PropertyField<?>> properties = new HashMap<String, PropertyField<?>>();
    properties.put("surname", new TextFieldProperty("Фамилия", null));
    properties.put("name", new TextFieldProperty("Имя", null));
    properties.put("patronymic", new TextFieldProperty("Отчество", null));

    List<String> cities = Arrays.asList("МСК", "СПб", "ННовгород");
    properties.put("city", new NativeSelectPropertyField("Город", "СПб", cities));

    form.setItemDataSource(new DocumentRequestItemSource(properties));

    form.setFormFieldFactory(new DocumentRequestFormFieldFactory());

    return form;
  }

  public static class DocumentRequestFormFieldFactory extends DefaultFieldFactory {
    private static final long serialVersionUID = 6834191594989190315L;

    @Override
    public Field createField(Item item, Object propertyId, Component uiContext) {
      DocumentRequestItemSource source = (DocumentRequestItemSource) item;
      return source.getPropertyField(propertyId).getField();
    }

  }

  public static class DocumentRequestItemSource implements Item {
    private static final long serialVersionUID = 5462543676001043907L;
    private Map<String, PropertyField<?>> properties;

    public DocumentRequestItemSource(Map<String, PropertyField<?>> properties) {
      this.properties = properties;
    }

    @Override
    public Property getItemProperty(Object id) {
      PropertyField<?> propertyField = properties.get(id);
      ObjectProperty<?> property = propertyField.getProperty();
      return property;
    }

    public PropertyField<?> getPropertyField(Object id) {
      return properties.get(id);
    }

    @Override
    public Collection<?> getItemPropertyIds() {
      return properties.keySet();
    }

    @Override
    public boolean addItemProperty(Object id, Property property) throws UnsupportedOperationException {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeItemProperty(Object id) throws UnsupportedOperationException {
      throw new UnsupportedOperationException();
    }

  }
}