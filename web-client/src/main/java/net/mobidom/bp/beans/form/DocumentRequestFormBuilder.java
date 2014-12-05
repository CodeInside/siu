package net.mobidom.bp.beans.form;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import net.mobidom.bp.beans.request.DocumentRequest;
import net.mobidom.bp.beans.types.ТипДокумента;

public class DocumentRequestFormBuilder {

  public static DocumentRequestForm createForm(final DocumentRequest documentRequest) {

    DocumentRequestForm requestForm = null;
    ТипДокумента типДокумента = documentRequest.getType();
    if (типДокумента == ТипДокумента.НДФЛ_2) {
      requestForm = create2NDFLForm(documentRequest);

    } else if (типДокумента == ТипДокумента.ДАННЫЕ_ЛИЦЕВОГО_СЧЕТА_ЗАСТРАХОВАННОГО_ЛИЦА) {

      List<PropertyFieldDescriptor<?>> propertyDescriptors = new ArrayList<PropertyFieldDescriptor<?>>();
      propertyDescriptors.add(new TextPropertyFieldDescriptor("snils_number", "СНИЛС", ""));
      requestForm = new DocumentRequestForm(documentRequest, propertyDescriptors);

    } else if (типДокумента == ТипДокумента.СНИЛС) {

      List<PropertyFieldDescriptor<?>> propertyDescriptors = new ArrayList<PropertyFieldDescriptor<?>>();
      propertyDescriptors.add(new TextPropertyFieldDescriptor("surname", "Фамилия", ""));
      propertyDescriptors.add(new TextPropertyFieldDescriptor("name", "Имя", ""));
      propertyDescriptors.add(new TextPropertyFieldDescriptor("patronymic", "Отчество", ""));

      propertyDescriptors.add(new DatePropertyFieldDescriptor("birthdate", "Дата рождения", new Date()));

      propertyDescriptors.add(new BeanItemSelectPropertyFieldDescriptor<IdCaptionItem>("gender", "Пол", null, IdCaptionItem.class, "caption", IdCaptionItem.GENDER_ITEMS,
          IdCaptionItem.EXTRACTOR));

      requestForm = new DocumentRequestForm(documentRequest, propertyDescriptors);

    } else if (типДокумента == ТипДокумента.ТРАНСКРИБИРОВАНИЕ_ФИГ) {

      List<PropertyFieldDescriptor<?>> propertyDescriptors = new ArrayList<PropertyFieldDescriptor<?>>();
      propertyDescriptors.add(new TextPropertyFieldDescriptor("LAT_SURNAME", "Фамилия(лат.)", ""));
      propertyDescriptors.add(new TextPropertyFieldDescriptor("LAT_FIRSTNAME", "Имя Отчество(лат.)", ""));

      // TODO webdom загрузить список государств с кодами - посмотреть в
      // справочниках
      List<IdCaptionItem> countries = new ArrayList<IdCaptionItem>();
      countries.add(new IdCaptionItem("USA", "Соединенные Штаты Америки"));
      countries.add(new IdCaptionItem("RUS", "Российская Федерация"));
      countries.add(new IdCaptionItem("UK", "Объединенное Королевство"));

      propertyDescriptors.add(new BeanItemSelectPropertyFieldDescriptor<IdCaptionItem>("COUNTRY_CODE", "Страна", null, IdCaptionItem.class, "caption", countries,
          IdCaptionItem.EXTRACTOR));

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