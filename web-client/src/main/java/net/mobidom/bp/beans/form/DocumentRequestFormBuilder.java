package net.mobidom.bp.beans.form;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.mobidom.bp.beans.request.DocumentRequest;
import net.mobidom.bp.beans.types.ТипДокумента;

public class DocumentRequestFormBuilder {

  private static class FormBuilderParam {
    public DocumentRequest documentRequest;
    public boolean readonly;
  }

  private static interface FormBuilder {
    DocumentRequestForm createForm(FormBuilderParam param);
  }

  private static Map<ТипДокумента, FormBuilder> BUILDERS = new HashMap<ТипДокумента, FormBuilder>() {
    private static final long serialVersionUID = -3927184894757663135L;

    {
      put(ТипДокумента.НДФЛ_2, new FormBuilder() {

        @Override
        public DocumentRequestForm createForm(FormBuilderParam param) {

          List<PropertyFieldDescriptor<?>> propertyDescriptors = new ArrayList<PropertyFieldDescriptor<?>>();

          propertyDescriptors.add(new TextPropertyFieldDescriptor("ВерсФорм", "Версия формата", "4.01"));
          propertyDescriptors.add(new TextPropertyFieldDescriptor("ИдЗапрос", "Идентификатор запроса", "2012"));
          propertyDescriptors.add(new DatePropertyFieldDescriptor("ОтчетГод", "Отчетный налоговый период", null, "yyyy"));

          IdCaptionItem defaultValue = new IdCaptionItem("1",
              "Запрос сведений о количестве представленных справок о доходах физических лиц по форме 2-НДФЛ");

          List<IdCaptionItem> types = new ArrayList<IdCaptionItem>();
          types.add(defaultValue);
          types.add(new IdCaptionItem("2", "Запрос сведений о доходах ФЛ по справкам 2-НДФЛ"));
          propertyDescriptors.add(new BeanItemSelectPropertyFieldDescriptor<IdCaptionItem>("ТипЗапроса", "Тип запроса", defaultValue,
              IdCaptionItem.class, "caption", types, IdCaptionItem.EXTRACTOR));

          propertyDescriptors.add(new TextPropertyFieldDescriptor("Фамилия", "Фамилия", "ЕВСЕЕВА"));
          propertyDescriptors.add(new TextPropertyFieldDescriptor("Имя", "Имя", "НАТАЛЬЯ"));
          propertyDescriptors.add(new TextPropertyFieldDescriptor("Отчество", "Отчество", "ВЛАДИМИРОВНА"));
          propertyDescriptors.add(new TextPropertyFieldDescriptor("Снилс", "СНИЛС", "12345678901234"));

          return new DocumentRequestForm(param.documentRequest, propertyDescriptors, param.readonly);

        }
      });

      put(ТипДокумента.ДАННЫЕ_ЛИЦЕВОГО_СЧЕТА_ЗАСТРАХОВАННОГО_ЛИЦА, new FormBuilder() {

        @Override
        public DocumentRequestForm createForm(FormBuilderParam param) {
          List<PropertyFieldDescriptor<?>> propertyDescriptors = new ArrayList<PropertyFieldDescriptor<?>>();
          propertyDescriptors.add(new TextPropertyFieldDescriptor("snils_number", "СНИЛС", "027-733-198 62"));
          return new DocumentRequestForm(param.documentRequest, propertyDescriptors, param.readonly);
        }
      });

      put(ТипДокумента.СНИЛС, new FormBuilder() {

        @Override
        public DocumentRequestForm createForm(FormBuilderParam param) {

          List<PropertyFieldDescriptor<?>> propertyDescriptors = new ArrayList<PropertyFieldDescriptor<?>>();
          propertyDescriptors.add(new TextPropertyFieldDescriptor("surname", "Фамилия", "Петина"));
          propertyDescriptors.add(new TextPropertyFieldDescriptor("name", "Имя", "Елена"));
          propertyDescriptors.add(new TextPropertyFieldDescriptor("patronymic", "Отчество", "Владимировна"));

          try {
            propertyDescriptors.add(new DatePropertyFieldDescriptor("birthdate", "Дата рождения", new SimpleDateFormat("dd-MM-yyyy")
                .parse("12-09-1966"), "dd/MM/yyyy"));
          } catch (Exception e) {
            propertyDescriptors.add(new DatePropertyFieldDescriptor("birthdate", "Дата рождения", new Date(), "dd/MM/yyyy"));
          }

          propertyDescriptors.add(new BeanItemSelectPropertyFieldDescriptor<IdCaptionItem>("gender", "Пол", IdCaptionItem.GENDER_ITEMS
              .get(0), IdCaptionItem.class, "caption", IdCaptionItem.GENDER_ITEMS, IdCaptionItem.EXTRACTOR));

          return new DocumentRequestForm(param.documentRequest, propertyDescriptors, param.readonly);
        }
      });

      put(ТипДокумента.ТРАНСКРИБИРОВАНИЕ_ФИГ, new FormBuilder() {

        @Override
        public DocumentRequestForm createForm(FormBuilderParam param) {

          List<PropertyFieldDescriptor<?>> propertyDescriptors = new ArrayList<PropertyFieldDescriptor<?>>();
          propertyDescriptors.add(new TextPropertyFieldDescriptor("LAT_SURNAME", "Фамилия(лат.)", "LABBEE"));
          propertyDescriptors.add(new TextPropertyFieldDescriptor("LAT_FIRSTNAME", "Имя Отчество(лат.)", "GABRIELLE CECILIA"));

          // TODO webdom загрузить список государств с кодами - посмотреть в
          // справочниках
          IdCaptionItem defaultValue = new IdCaptionItem("USA", "Соединенные Штаты Америки");
          List<IdCaptionItem> countries = new ArrayList<IdCaptionItem>();
          countries.add(defaultValue);
          countries.add(new IdCaptionItem("RUS", "Российская Федерация"));
          countries.add(new IdCaptionItem("UK", "Объединенное Королевство"));

          propertyDescriptors.add(new BeanItemSelectPropertyFieldDescriptor<IdCaptionItem>("COUNTRY_CODE", "Страна", defaultValue,
              IdCaptionItem.class, "caption", countries, IdCaptionItem.EXTRACTOR));

          return new DocumentRequestForm(param.documentRequest, propertyDescriptors, param.readonly);
        }
      });

      put(ТипДокумента.ИНН, new FormBuilder() {

        @Override
        public DocumentRequestForm createForm(FormBuilderParam param) {
          List<PropertyFieldDescriptor<?>> propertyDescriptors = new ArrayList<PropertyFieldDescriptor<?>>();
          propertyDescriptors.add(new TextPropertyFieldDescriptor("Имя", "Имя", "ПЕТР"));
          propertyDescriptors.add(new TextPropertyFieldDescriptor("Фамилия", "Фамилия", "ЧАХЛОВ"));
          propertyDescriptors.add(new TextPropertyFieldDescriptor("Отчество", "Отчество", "АЛЕКСЕЕВИЧ"));
          try {
            propertyDescriptors.add(new DatePropertyFieldDescriptor("ДатаРожд", "Дата рождения", new SimpleDateFormat("dd.MM.yyyy")
                .parse("12.07.1954"), "dd.MM.yyyy"));
          } catch (ParseException e) {
            propertyDescriptors.add(new DatePropertyFieldDescriptor("ДатаРожд", "Дата рождения", new Date(), "dd.MM.yyyy"));
          }

          return new DocumentRequestForm(param.documentRequest, propertyDescriptors, param.readonly);
        }
      });

      put(ТипДокумента.СВЕДЕНИЯ_О_ЗАРАБОТНОЙ_ПЛАТЕ_ИНЫХ_ВЫПЛАТАХ_И_ВОЗНАГРАЖДЕНИЯХ_ЗАСТРАХОВАННОГО_ЛИЦА, new FormBuilder() {

        @Override
        public DocumentRequestForm createForm(FormBuilderParam param) {

          List<PropertyFieldDescriptor<?>> propertyDescriptors = new ArrayList<PropertyFieldDescriptor<?>>();
          propertyDescriptors.add(new TextPropertyFieldDescriptor("FIRST_NAME", "Имя", "ЕЛЕНА"));
          propertyDescriptors.add(new TextPropertyFieldDescriptor("SECOND_NAME", "Фамилия", "ПЕТИНА"));
          propertyDescriptors.add(new TextPropertyFieldDescriptor("PATRONYMIC", "Отчество", "ВЛАДИМИРОВНА"));
          propertyDescriptors.add(new TextPropertyFieldDescriptor("SNILS", "СНИЛС", "027-733-198 62"));
          propertyDescriptors.add(new TextPropertyFieldDescriptor("YEARS", "Год", "2010;2011"));

          return new DocumentRequestForm(param.documentRequest, propertyDescriptors, param.readonly);
        }
      });

      put(ТипДокумента.ВЫПИСКА_ИЗ_ЕГРИП_ПОЛНАЯ, new FormBuilder() {

        @Override
        public DocumentRequestForm createForm(FormBuilderParam param) {

          List<PropertyFieldDescriptor<?>> propertyDescriptors = new ArrayList<PropertyFieldDescriptor<?>>();
          propertyDescriptors.add(new TextPropertyFieldDescriptor("ИНН", "ИНН", "", true));
          propertyDescriptors.add(new TextPropertyFieldDescriptor("ОГРНИП", "ОГРНИП", "304301728800074"));

          return new DocumentRequestForm(param.documentRequest, propertyDescriptors, param.readonly);
        }
      });
      put(ТипДокумента.ВЫПИСКА_ИЗ_ЕГРЮЛ_ПОЛНАЯ, new FormBuilder() {

        @Override
        public DocumentRequestForm createForm(FormBuilderParam param) {
          List<PropertyFieldDescriptor<?>> propertyDescriptors = new ArrayList<PropertyFieldDescriptor<?>>();
          propertyDescriptors.add(new TextPropertyFieldDescriptor("ИННЮЛ", "ИННЮЛ", "", true));
          propertyDescriptors.add(new TextPropertyFieldDescriptor("ОГРН", "ОГРН", "1023302752021"));

          return new DocumentRequestForm(param.documentRequest, propertyDescriptors, param.readonly);
        }
      });

    }
  };

  public static Set<ТипДокумента> getAvailableTypes() {
    return BUILDERS.keySet();
  }

  public static DocumentRequestForm createForm(DocumentRequest documentRequest, boolean readonly) {

    ТипДокумента type = documentRequest.getType();
    FormBuilder builder = BUILDERS.get(type);
    if (builder == null) {
      return null;
    }

    FormBuilderParam param = new FormBuilderParam();
    param.documentRequest = documentRequest;
    param.readonly = readonly;
    DocumentRequestForm requestForm = builder.createForm(param);
    return requestForm;
  }
}