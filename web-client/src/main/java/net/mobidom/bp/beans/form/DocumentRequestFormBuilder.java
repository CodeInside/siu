package net.mobidom.bp.beans.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
    public Object getValue() {
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

  @SuppressWarnings("unchecked")
  public static class BeanItemSelectPropertyFieldDescriptor<T> extends PropertyFieldDescriptor<T> {

    public static interface ResultExtractor<T> {
      Object getValue(T value);
    }

    protected List<T> selectValues;
    protected String itemCaptionPropName;
    protected ResultExtractor<T> extractor;

    public BeanItemSelectPropertyFieldDescriptor(String id, String caption, T value, Class<T> type, String itemCaptionPropName, List<T> selectValues, ResultExtractor<T> extractor) {
      super(id, caption, value, type);
      this.itemCaptionPropName = itemCaptionPropName;
      this.selectValues = selectValues;
      this.extractor = extractor;
    }

    @Override
    public Field getField() {
      Select select = new Select(caption, selectValues);
      select.setImmediate(true);
      select.setItemCaptionMode(Select.ITEM_CAPTION_MODE_PROPERTY);
      select.setPropertyDataSource(getProperty());
      select.setItemCaptionPropertyId(itemCaptionPropName);

      return select;
    }

    @Override
    public Object getValue() {
      if (extractor == null) {
        return getProperty().getValue();
      }

      return extractor.getValue((T) getProperty().getValue());
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

  public static class CountryItem implements Serializable {
    private static final long serialVersionUID = 3502384982955303591L;

    private String id;
    private String name;

    public CountryItem() {
    }

    public CountryItem(String id, String name) {
      super();
      this.id = id;
      this.name = name;
    }

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
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
      List<CountryItem> countries = new ArrayList<CountryItem>();
      countries.add(new CountryItem("USA", "Соединенные Штаты Америки"));
      countries.add(new CountryItem("RUS", "Российская Федерация"));
      countries.add(new CountryItem("UK", "Объединенное Королевство"));

      propertyDescriptors.add(new BeanItemSelectPropertyFieldDescriptor<CountryItem>("COUNTRY_CODE", "Страна", null, CountryItem.class, "name", countries,
          new BeanItemSelectPropertyFieldDescriptor.ResultExtractor<CountryItem>() {

            @Override
            public Object getValue(CountryItem value) {
              return value.getId();
            }
          }));

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