package net.mobidom.bp.beans.form;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.mobidom.bp.beans.request.DocumentRequest;
import net.mobidom.bp.beans.types.ТипДокумента;

import com.vaadin.data.Property;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.ui.Field;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.Select;
import com.vaadin.ui.TextField;

public class DocumentRequestFormBuilder {

  @SuppressWarnings("rawtypes")
  public static abstract class PropertyFieldDescriptor<T> {

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
    public T getValue() {
      return (T) property.getValue();
    }

    public abstract Field getField();

    protected Property getProperty() {
      return property;
    }
  }

  public static class TextPropertyFieldDescriptor extends PropertyFieldDescriptor<String> {

    public TextPropertyFieldDescriptor(String id, String caption, String value) {
      super(id, caption, value, String.class);
    }

    @Override
    public Field getField() {
      TextField textField = new TextField(caption);
      textField.setPropertyDataSource(getProperty());
      textField.setImmediate(true);
      textField.setWidth("250px");
      return textField;
    }

  }

  public static class DatePropertyFieldDescriptor extends PropertyFieldDescriptor<Date> {

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

  public static class SelectPropertyFieldDescriptor extends PropertyFieldDescriptor<String> {

    protected List<String> selectValues;

    public SelectPropertyFieldDescriptor(String id, String caption, String value, List<String> selectValues) {
      super(id, caption, value, String.class);
      this.selectValues = selectValues;
    }

    @Override
    public Field getField() {
      Select select = new Select(caption, selectValues);
      select.setPropertyDataSource(getProperty());
      select.setImmediate(true);
      return select;
    }

    @Override
    public String getValue() {
      return String.valueOf(getProperty().getValue());
    }
  }

  public static DocumentRequestForm createForm(final DocumentRequest documentRequest) {

    DocumentRequestForm requestForm = null;
    ТипДокумента типДокумента = documentRequest.getType();
    if (типДокумента == ТипДокумента.НДФЛ_2) {
      requestForm = create2NDFLForm(documentRequest);

    } else if (типДокумента == ТипДокумента.ДАННЫЕ_ЛИЦЕВОГО_СЧЕТА_ЗАСТРАХОВАННОГО_ЛИЦА) {

      List<PropertyFieldDescriptor<?>> propertyDescriptors = new ArrayList<DocumentRequestFormBuilder.PropertyFieldDescriptor<?>>();
      propertyDescriptors.add(new TextPropertyFieldDescriptor("snils_number", "СНИЛС", ""));
      requestForm = new DocumentRequestForm(documentRequest, propertyDescriptors);

    } else if (типДокумента == ТипДокумента.ТРАНСКРИБИРОВАНИЕ_ФИГ) {

      List<PropertyFieldDescriptor<?>> propertyDescriptors = new ArrayList<DocumentRequestFormBuilder.PropertyFieldDescriptor<?>>();
      propertyDescriptors.add(new TextPropertyFieldDescriptor("LAT_SURNAME", "Фамилия(лат.)", ""));
      propertyDescriptors.add(new TextPropertyFieldDescriptor("LAT_FIRSTNAME", "Имя Отчество(лат.)", ""));

      // TODO webdom загрузить список государств с кодами
      Map<String, String> countries = new HashMap<String, String>();
      countries.put("USA", "Соединенные Штаты Америки");
      countries.put("RUS", "Российская Федерация");
      countries.put("UK", "Объединенное Королевство");

      propertyDescriptors.add(new SelectPropertyFieldDescriptor("COUNTRY_CODE", "Государство", null, new ArrayList<String>(countries.keySet())));
      requestForm = new DocumentRequestForm(documentRequest, propertyDescriptors);
    }

    // TODO webdom
    return requestForm;
  }

  static DocumentRequestForm create2NDFLForm(final DocumentRequest documentRequest) {

    List<PropertyFieldDescriptor<?>> propertyDescriptors = new ArrayList<PropertyFieldDescriptor<?>>();
    propertyDescriptors.add(new TextPropertyFieldDescriptor("surname", "Фамилия", null));
    propertyDescriptors.add(new TextPropertyFieldDescriptor("name", "Имя", null));
    propertyDescriptors.add(new TextPropertyFieldDescriptor("patronymic", "Отчество", null));

    List<String> cities = Arrays.asList("МСК", "СПб", "Нск");
    propertyDescriptors.add(new SelectPropertyFieldDescriptor("city", "Город", null, cities));

    return new DocumentRequestForm(documentRequest, propertyDescriptors);
  }

}