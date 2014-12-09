package net.mobidom.bp.beans.form;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.mobidom.bp.beans.request.DocumentRequest;
import net.mobidom.bp.beans.types.ТипДокумента;

public class DocumentRequestFormBuilder {

  public static DocumentRequestForm createForm(final DocumentRequest documentRequest, boolean readonly) {

    DocumentRequestForm requestForm = null;
    ТипДокумента типДокумента = documentRequest.getType();

    if (типДокумента == ТипДокумента.НДФЛ_2) {

      List<PropertyFieldDescriptor<?>> propertyDescriptors = new ArrayList<PropertyFieldDescriptor<?>>();

      propertyDescriptors.add(new TextPropertyFieldDescriptor("ВерсФорм", "Версия формата", "4.01"));
      propertyDescriptors.add(new TextPropertyFieldDescriptor("ИдЗапрос", "Идентификатор запроса", "2012"));
      propertyDescriptors.add(new DatePropertyFieldDescriptor("ОтчетГод", "Отчетный налоговый период", null, "yyyy"));

      List<IdCaptionItem> types = new ArrayList<IdCaptionItem>();
      types.add(new IdCaptionItem("1", "Запрос сведений о количестве представленных справок о доходах физических лиц по форме 2-НДФЛ"));
      types.add(new IdCaptionItem("2", "Запрос сведений о доходах ФЛ по справкам 2-НДФЛ"));
      propertyDescriptors.add(new BeanItemSelectPropertyFieldDescriptor<IdCaptionItem>("ТипЗапроса", "Тип запроса", null, IdCaptionItem.class, "caption", types,
          IdCaptionItem.EXTRACTOR));

      propertyDescriptors.add(new TextPropertyFieldDescriptor("Фамилия", "Фамилия", "ЕВСЕЕВА"));
      propertyDescriptors.add(new TextPropertyFieldDescriptor("Имя", "Имя", "НАТАЛЬЯ"));
      propertyDescriptors.add(new TextPropertyFieldDescriptor("Отчество", "Отчество", "ВЛАДИМИРОВНА"));
      propertyDescriptors.add(new TextPropertyFieldDescriptor("Снилс", "СНИЛС", "12345678901234"));

      requestForm = new DocumentRequestForm(documentRequest, propertyDescriptors, readonly);

    } else if (типДокумента == ТипДокумента.ДАННЫЕ_ЛИЦЕВОГО_СЧЕТА_ЗАСТРАХОВАННОГО_ЛИЦА) {

      List<PropertyFieldDescriptor<?>> propertyDescriptors = new ArrayList<PropertyFieldDescriptor<?>>();
      propertyDescriptors.add(new TextPropertyFieldDescriptor("snils_number", "СНИЛС", ""));
      requestForm = new DocumentRequestForm(documentRequest, propertyDescriptors, readonly);

    } else if (типДокумента == ТипДокумента.СНИЛС) {

      List<PropertyFieldDescriptor<?>> propertyDescriptors = new ArrayList<PropertyFieldDescriptor<?>>();
      propertyDescriptors.add(new TextPropertyFieldDescriptor("surname", "Фамилия", ""));
      propertyDescriptors.add(new TextPropertyFieldDescriptor("name", "Имя", ""));
      propertyDescriptors.add(new TextPropertyFieldDescriptor("patronymic", "Отчество", ""));

      propertyDescriptors.add(new DatePropertyFieldDescriptor("birthdate", "Дата рождения", new Date(), "dd/MM/yyyy"));

      propertyDescriptors.add(new BeanItemSelectPropertyFieldDescriptor<IdCaptionItem>("gender", "Пол", null, IdCaptionItem.class, "caption", IdCaptionItem.GENDER_ITEMS,
          IdCaptionItem.EXTRACTOR));

      requestForm = new DocumentRequestForm(documentRequest, propertyDescriptors, readonly);

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

      requestForm = new DocumentRequestForm(documentRequest, propertyDescriptors, readonly);
    }

    // TODO webdom
    return requestForm;
  }
}