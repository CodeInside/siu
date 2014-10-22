/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws3564c.enclosure.grp;


import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.codeinside.gws.api.ExchangeContext;
import ru.codeinside.gws3564c.DummyContext;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;

public class SubjectRightsHistoryEnclosureBuilderTest {
  private ExchangeContext context;
  private SubjectRightsHistoryEnclosureBuilder enclosureBuilder;

  @Before
  public void setUp() throws Exception {
    context = new DummyContext();
    enclosureBuilder = new SubjectRightsHistoryEnclosureBuilder(context);
  }
  @Test
  public void testWeCanReproduceSampleRequest() throws Exception {
    createContextForGetRealtyRightsByOwner();
    context.setVariable("request_interval_start", TestUtils.getDateValue("2000-07-01"));
    context.setVariable("request_interval_end", TestUtils.getDateValue("2012-07-12"));
    context.setVariable("objectKind", "ALL");
    testCreateEnclosure("/enclosure/grp/subject_rights_history.xml", "7b983700-651d-43d8-bdb4-26dd683df535");
  }

  private void testCreateEnclosure(String fileWithExpectedEnclosure, String guid) throws IOException {
    String xml = enclosureBuilder.createEnclosure(guid);
    InputStream is = this.getClass().getResourceAsStream(fileWithExpectedEnclosure);
    String expectedContent = IOUtils.toString(is, "UTF-8");
    Assert.assertEquals(expectedContent, xml);
  }

  private void createContextForGetRealtyRightsByOwner() throws ParseException {
    context.setVariable("smevTest", "Первичный запрос");
    context.setVariable("okato", "60000000000"); // string
    context.setVariable("oktmo", "01234567"); // string
    context.setVariable("requestType", "558102100000"); // string
    context.setVariable("enclosure_request_type", "SUBJECT_RIGHTS"); // string
    context.setVariable("declarantType", "GOVERNANCE"); // string
    context.setVariable("ownerPersonFIOSurname", "Иванов");
    context.setVariable("ownerPersonFIOFirst", "Иван");
    context.setVariable("ownerPersonFIOPatronymic", "Иваныч");
    context.setVariable("ownerPersonSNILS", "112-233-332 33");
    context.setVariable("ownerPersonDateBirth", TestUtils.getDateValue("2012-07-12"));

    context.setVariable("ownerPersonPDocumentCode", "008001001000"); //тип string
    context.setVariable("ownerPersonPDocumentSeries", "1111"); //тип string
    context.setVariable("ownerPersonPDocumentNumber", "222222"); //тип string
    context.setVariable("ownerPersonPDocumentDate", TestUtils.getDateValue("2001-01-01")); //тип date
    context.setVariable("ownerPersonPDocumentIssueOrgan", "ОВД Калуга, 2321"); //тип string
    context.setVariable("ownerTemporaryLocationRegion", "40"); //тип string
    context.setVariable("ownerTemporaryLocationCityName", "Калуга"); //тип string
    context.setVariable("ownerTemporaryLocationDCity", "г"); //тип string
    context.setVariable("ownerTemporaryLocationStreetName", "Ленина"); //тип string
    context.setVariable("ownerTemporaryLocationDStreets", "уч-к"); //тип enum
    context.setVariable("ownerTemporaryLocationLocationLevel1Type", "д"); //тип enum
    context.setVariable("ownerTemporaryLocationLocationLevel1Value", "32"); //тип string
    context.setVariable("ownerTemporaryLocationLocationLevel2Type", "корп"); //тип enum
    context.setVariable("ownerTemporaryLocationLocationLevel2Value", "2"); //тип string
    context.setVariable("ownerTemporaryLocationLocationLevel3Type", "литера"); //тип enum

    context.setVariable("ownerTemporaryLocationLocationLevel3Value", "а"); //тип string
    context.setVariable("ownerTemporaryLocationLocationApartmentType", "кв"); //тип enum

    context.setVariable("ownerTemporaryLocationLocationApartmentValue", "32"); //тип string
    context.setVariable("ownerTemporaryLocationLocationOther", "Иное"); //тип string
    context.setVariable("ownerTemporaryLocationLocationNote", "Неформализованное описнаие");



    context.setVariable("applied", 1l);
    context.setVariable("territory", 1L);
    context.setVariable("territoryCode_1", "01");


    context.setVariable("payment", 0l);
    context.setVariable("appliedADocumentDate_1", TestUtils.getDateValue("2012-07-24")); // string
    context.setVariable("appliedADocumentOriginalQuantity_1", 1L); // string
    context.setVariable("appliedADocumentOriginalQuantitySheet_1", 1L); // string

    context.setVariable("appliedADocumentName_1",
                        "Запрос о предоставлении сведений, содержащихся в Едином государственном реестре прав на недвижимое имущество и сделок с ним");
    context.setVariable("appliedADocumentCode_1", "558102100000"); // string
    context.setVariable("appliedADocumentNumber_1", "26-0-1-21/4001/2011-166"); // string

    context.setVariable("declarantType", "GOVERNANCE");
    context.setVariable("declKind", "357007000000");      //Required
    context.setVariable("declGovernanceName", "Пенсионный фонд");
    context.setVariable("declGovernanceCode", "007001001001");
    context.setVariable("declGovernanceEmail", "test@test");

    context.setVariable("agentFIOSurname", "Антонов");
    context.setVariable("agentFIOFirst", "Антон");
    context.setVariable("agentFIOPatronymic", "Антонович");
    context.setVariable("agentSNILS", "233-234-434 34");
    context.setVariable("agentKind", "356003000000");
    context.setVariable("agentEmail", "test@test.ru");
    context.setVariable("agentPhone", "213-23-12");
    context.setVariable("agentContactInfo", "Адрес для почтовой корреспонденции");

    context.setVariable("agentPDocumentCode", "008001001000"); //тип string
    context.setVariable("agentPDocumentSeries", "2222"); //тип string
    context.setVariable("agentPDocumentNumber", "222222"); //тип string
    context.setVariable("agentPDocumentDate", TestUtils.getDateValue("2010-10-10")); //тип date
    context.setVariable("agentPDocumentIssueOrgan", "УВД, Код подразделен"); //тип string
    context.setVariable("agentTemporaryLocationRegion", "40"); //тип string
    context.setVariable("agentTemporaryLocationPostalCode", "113563"); //тип string
    context.setVariable("agentTemporaryLocationCityName", "Калуга"); //тип string
    context.setVariable("agentTemporaryLocationDCity", "г"); //тип string
    context.setVariable("agentTemporaryLocationStreetName", "Советская"); //тип string
    context.setVariable("agentTemporaryLocationDStreets", "ул"); //тип enum
    context.setVariable("agentTemporaryLocationLocationLevel1Type", "д"); //тип enum
    context.setVariable("agentTemporaryLocationLocationLevel1Value", "10"); //тип string
    context.setVariable("agentTemporaryLocationLocationLevel2Type", "корп"); //тип enum
    context.setVariable("agentTemporaryLocationLocationLevel2Value", "1"); //тип string
    context.setVariable("agentTemporaryLocationLocationLevel3Type", "литера"); //тип enum

    context.setVariable("agentTemporaryLocationLocationLevel3Value", "а"); //тип string
    context.setVariable("agentTemporaryLocationLocationApartmentType", "к"); //тип enum

    context.setVariable("agentTemporaryLocationLocationApartmentValue", "1"); //тип string
    context.setVariable("agentTemporaryLocationLocationOther", "Иное"); //тип string
    context.setVariable("agentTemporaryLocationLocationNote", "Неформализованное описание");


    //ReasonFreeDocument
    context.setVariable("freePaymentADocumentCode_1", "555005000000");
    context.setVariable("freePaymentADocumentName_1",
                        "Документ, подтверждающий право заявителя на безвозмездное получение сведений");
    context.setVariable("freePaymentADocumentNumber_1", "");
    context.setVariable("freePaymentADocumentDate_1", TestUtils.getDateValue("2012-07-24"));
    context.setVariable("freePaymentADocumentOriginalQuantity_1", 1L);
    context.setVariable("freePaymentADocumentOriginalQuantitySheet_1", 1L);
    context.setVariable("isPaymentFree", true);
    context.setVariable("freePayment", 1L);
    context.setVariable("ownerType", "PERSON");
  }

  @Test
  public void testCreateRequestByFormFieldsFromProcess() throws Exception {
    fillContextByProcessDefinition();
    String enclosure = enclosureBuilder.createEnclosure("7b983700-651d-43d8-bdb4-26dd683df535");
    System.out.println(enclosure);
  }

  private void fillContextByProcessDefinition(){
    context.setVariable("okato", "60000000000"); // ОКАТО
    context.setVariable("oktmo", "01234567"); // ОКТМО
    context.setVariable("requestType", "558102100000"); // Тип запроса
    context.setVariable("enclosure_request_type", "SUBJECT_RIGHTS_HISTORY"); // Тип запроса
    context.setVariable("declarantType", "GOVERNANCE"); // Тип Декларанта
    context.setVariable("declKind", "357007000000"); // Категория подателя запроса
// Начало данных для гос органа
    context.setVariable("declGovernanceCode", "007001001001"); // Тип
    context.setVariable("declGovernanceName", "Пенсионный фонд"); // Наименование
    context.setVariable("declGovernanceEmail", "test@test"); // Email
    context.setVariable("declGovernancePhone", "12342323"); // Телефон
    context.setVariable("declGovernanceContactInfo", "contact info"); // Информация для контакта
// Конец данных для гос органа
// Начало данных для юр лица
    context.setVariable("declLegalPersonName", ""); // Наименование организации
    context.setVariable("declLegalPersonOPF", ""); // Организацонно-правовая форма
    context.setVariable("declLegalPersonKPP", ""); // КПП
    context.setVariable("declLegalPersonINN", ""); // ИНН
    context.setVariable("declLegalPersonOGRN", ""); // Код ОГРН
    context.setVariable("declLegalPersonEmail", ""); // Email
    context.setVariable("declLegalPersonPhone", ""); // Номер телефона
    context.setVariable("declLegalPersonContactInfo", ""); // Доп. информация о контакте
    context.setVariable("declLegalPersonRegDate", ""); // Дата регистрации
    context.setVariable("declLegalPersonRegAgency", ""); // Орган регистрации
    context.setVariable("declLegalPersonDocumentCode", ""); // Документ юр лица. Вид документа
    context.setVariable("declLegalPersonDocumentName", ""); // Документ юр лица. Наименование
    context.setVariable("declLegalPersonDocumentSeries", ""); // Документ юр лица. Серия
    context.setVariable("declLegalPersonDocumentNumber", ""); // Документ юр лица. Номер
    context.setVariable("declLegalPersonDocumentDate", ""); // Документ юр лица. Дата выдачи
    context.setVariable("declLegalPersonDocumentIssueOrgan", ""); // Документ юр лица. Наименование органа выдавшего документ
    context.setVariable("declLegalPersonDocumentDesc", ""); // Документ юр лица. Заметки
// Конец данных для юр лица
// Начало данных для физ. лица
    context.setVariable("declPersonFIOSurname", ""); // Фамилия заявителя
    context.setVariable("declPersonFIOFirst", ""); // Имя заявителя
    context.setVariable("declPersonFIOPatronymic", ""); // Отчество заявителя
    context.setVariable("declPersonEmail", ""); // Email заявителя
    context.setVariable("declPersonPhone", ""); // Телефон заявителя
    context.setVariable("declPersonSNILS", ""); // СНИЛС заявителя
    context.setVariable("declPersonBirthDate", ""); // Дата рождения заявителя
    context.setVariable("declPersonBirthLocation", ""); // Место рождения заявителя
    context.setVariable("declPersonSex", ""); // Пол заявителя
    context.setVariable("declPersonMarriageStatus", ""); // Семейное положение заявителя
    context.setVariable("declPersonPDocumentCode", ""); // Код документа
    context.setVariable("declPersonPDocumentSeries", ""); // Серия документа
    context.setVariable("declPersonPDocumentNumber", ""); // Номер документа
    context.setVariable("declPersonPDocumentDate", ""); // Дата документа
    context.setVariable("declPersonPDocumentIssueOrgan", ""); // Орган выдавший документ
// Конец данных для физ. лица

    //context.setVariable("declLocationOKATO", ""); // Адрес заявителя (ОКАТО)
   // context.setVariable("declLocationCLADR", ""); // Адрес заявителя (КЛАДР)
    //context.setVariable("declLocationPostalCode", "4343443"); // Адрес заявителя (Почтовый индекс)
   // context.setVariable("declLocationRegion", "40"); // Адрес заявителя (Код региона)
   // context.setVariable("declLocationDistrictName", "нзавние района"); // Адрес заявителя (Район)
   // context.setVariable("declLocationDistrictType", "р-н"); // Адрес заявителя (Тип района)
  //  context.setVariable("declLocationCityName", "Калуга"); // Адрес заявителя (Наименование муниципального
  // образования)
  //  context.setVariable("declLocationDCity", "г"); // Адрес заявителя (Тип муниципального образования)
  //  context.setVariable("declLocationUrbanDistictName", "железнодорожный"); // Адрес заявителя (Наименование
    // городского района)
  //  context.setVariable("declLocationUrbanDistictType", "р-н"); // Адрес заявителя (Тип городского района)
  //  context.setVariable("declLocationSovietVillageName", "VillageName"); // Адрес заявителя (Наименование сельсовета)
  //  context.setVariable("declLocationSovietVillageType", "волость"); // Адрес заявителя (Тип сельсовета)
  //  context.setVariable("declLocationLocalityName", "населенного"); // Адрес заявителя (Наименование населенного
  // пункта)
    //context.setVariable("declLocationLocalityType", "аал"); // Адрес заявителя (Тип населенного пункта)
   // context.setVariable("declLocationStreetName", "Южная"); // Адрес заявителя (Название улицы)
   // context.setVariable("declLocationDStreets", "аллея"); // Адрес заявителя (Тип улицы)
  //  context.setVariable("declLocationLocationLevel1Type", "д"); // Адрес заявителя (Тип адресного элемента 1-го
  // уровня)
   // context.setVariable("declLocationLocationLevel1Value", "1"); // Адрес заявителя (Значение адресного элемента 1-го
    // уровня)
   // context.setVariable("declLocationLocationLevel2Type", "корп"); // Адрес заявителя (Тип адресного элемента 2-го
   // уровня)
   // context.setVariable("declLocationLocationLevel2Value", "2"); // Адрес заявителя (Значение адресного элемента 2-го
    // уровня)
   // context.setVariable("declLocationLocationLevel3Type", "блок"); // Адрес заявителя (Тип адресного элемента 3-го
   // уровня)
  //  context.setVariable("declLocationLocationLevel3Value", "3"); // Адрес заявителя (Значение адресного элемента 3-го
    // уровня)
   // context.setVariable("declLocationLocationApartmentType", "к"); // Адрес заявителя (Тип адресного уровня квартиры)
   // context.setVariable("declLocationLocationApartmentValue", "4"); // Адрес заявителя (Значение адресного уровня
    // квартиры)
   // context.setVariable("declLocationLocationOther", "иное адрес заявителя"); // Адрес заявителя (Иное)
   // context.setVariable("declLocationLocationNote", "замечания"); // Адрес заявителя (Неформализованное описание)
    context.setVariable("agentFIOSurname", "фамилия представителя"); // Фамилия представителя
    context.setVariable("agentFIOFirst", "имя пред"); // Имя представителя
    context.setVariable("agentFIOPatronymic", "отчество предст"); // Отчество представителя
    context.setVariable("agentPDocumentCode", "008001000000"); // Код документа
    context.setVariable("agentPDocumentSeries", "2222"); // Серия документа
    context.setVariable("agentPDocumentNumber", "222222"); // Номер документа
    context.setVariable("agentPDocumentDate", TestUtils.getDateValue("2009-12-01")); // Дата документа
    context.setVariable("agentPDocumentIssueOrgan", "увд"); // Орган выдавший документ
    context.setVariable("agentEmail", "agentEmail"); // Email представителя
    context.setVariable("agentPhone", "agentPhone"); // Телефон представителя
    context.setVariable("agentSNILS", "112-233-332 33"); // СНИЛС представителя
    context.setVariable("agentKind", "356001000000"); // Тип представителя
    context.setVariable("agentTemporaryLocationOKATO", "agentOKATO"); // Адрес представителя (Адрес представителя (ОКАТО))
    context.setVariable("agentTemporaryLocationCLADR", "agentCLADR"); // Адрес представителя (КЛАДР)
    context.setVariable("agentTemporaryLocationPostalCode", "123456"); // Адрес представителя (Адрес представителя
    // (Почтовый
    // индекс))
    context.setVariable("agentTemporaryLocationRegion", "40"); // Адрес представителя (Код региона)
    context.setVariable("agentTemporaryLocationDistrictName", "agentDistrictName"); // Адрес представителя (Район)
    context.setVariable("agentTemporaryLocationDistrictType", "р-н"); // Адрес представителя (Тип района)
    context.setVariable("agentTemporaryLocationCityName", "agentCityName"); // Адрес представителя (Наименование муниципального образования)
    context.setVariable("agentTemporaryLocationDCity", "г"); // Адрес представителя (Тип муниципального образования)
    context.setVariable("agentTemporaryLocationUrbanDistictName", "agentUrbanDistictName"); // Адрес представителя (Наименование городского района)
    context.setVariable("agentTemporaryLocationUrbanDistictType", "р-н"); // Адрес представителя (Тип городского района)
    context.setVariable("agentTemporaryLocationSovietVillageName", "agentSovietVillageName"); // Адрес представителя (Наименование сельсовета)
    context.setVariable("agentTemporaryLocationSovietVillageType", "с/с"); // Адрес представителя (Тип сельсовета)
    context.setVariable("agentTemporaryLocationLocalityName", "agentLocalityName"); // Адрес представителя (Наименование населенного пункта)
    context.setVariable("agentTemporaryLocationLocalityType", "аул"); // Адрес представителя (Тип населенного пункта)
    context.setVariable("agentTemporaryLocationDStreets", "заезд"); // Адрес представителя (Тип улицы)
    context.setVariable("agentTemporaryLocationLocationLevel1Value", "agentTemporaryLocationLevel1Value"); // Адрес представителя (Значение адресного элемента 1-го уровня)
    context.setVariable("agentTemporaryLocationLocationLevel2Type", "д"); // Адрес представителя (Тип адресного элемента 2-го уровня)
    context.setVariable("agentTemporaryLocationLocationLevel2Value", "1"); // Адрес представителя (Значение адресного элемента 2-го
    // уровня)
    context.setVariable("agentTemporaryLocationLocationLevel3Type", "блок"); // Адрес представителя (Тип адресного элемента
    // 3-го уровня)
    context.setVariable("agentTemporaryLocationLocationLevel3Value", "agentTemporaryLocationLevel3Value"); // Адрес представителя (Значение адресного элемента 3-го уровня)
    context.setVariable("agentTemporaryLocationLocationApartmentType", "к"); // Адрес представителя (Тип адресного уровня квартиры)
    context.setVariable("agentTemporaryLocationLocationApartmentValue", "2"); // Адрес представителя (Значение адресного уровня
    // квартиры)
    context.setVariable("agentTemporaryLocationLocationOther", "agentTemporaryLocationOther"); // Адрес представителя (Иное)
    context.setVariable("agentTemporaryLocationLocationNote", "agentTemporaryLocationNote"); // Адрес представителя (Неформализованное описание)
    context.setVariable("applied", 1L); // Приложенные документы
    context.setVariable("appliedADocumentCode_1", "008001003000"); // Тип представленного документа
    context.setVariable("appliedADocumentName_1", "appliedADocumentName"); // Наименование представленного документа
    context.setVariable("appliedADocumentNumber_1", "1231312"); // Номер представленного документа
    context.setVariable("appliedADocumentDate_1", TestUtils.getDateValue("2003-04-15")); // Дата выдачи документа
    context.setVariable("appliedADocumentIssue_1", "appliedADocumentIssue"); // Орган выдавший представленный документ
    context.setVariable("appliedADocumentDesc_1", "appliedADocumentDesc"); // Особые отметки для представленного документ
    context.setVariable("appliedADocumentOriginalQuantity_1", 1l); // Количество оригиналов
    context.setVariable("appliedADocumentOriginalQuantitySheet_1", 1l); // Количество листов оригиналов
    //context.setVariable("appliedADocumentCopyQuantity_1", 1l); // Количество копий документов
    //context.setVariable("appliedADocumentCopyQuantitySheet_1", 1l); // Количество листов копий

    context.setVariable("isPaymentFree" , true); // Есть права на получение безвозмезной услоги

    context.setVariable("payment", 0l); // Документы оплаты
    context.setVariable("paymentDoc_Type", "paymentDoc_Type"); // Вид платежного документа
    context.setVariable("paymentNumber", "paymentNumber"); // Номер
    context.setVariable("paymentBIC", "paymentBIC"); // БИК
    context.setVariable("paymentBank_Name", "paymentBank_Name"); // Наименование банка
    context.setVariable("paymentOKATO", "paymentOKATO"); // ОКАТО организации, к которой относится платёж
    context.setVariable("paymentSettlement_Account", "paymentSettlement_Account"); // Расчётный счёт
    context.setVariable("paymentDate", "paymentDate"); // Дата
    context.setVariable("paymentFIOPayer", "paymentFIOPayer"); // ФИО плательщика
    context.setVariable("paymentSum", "paymentSum"); // Сумма в копейках
    context.setVariable("paymentADocumentOriginalQuantity", "paymentADocumentOriginalQuantity"); // Количество документов оригиналов
    context.setVariable("paymentADocumentOriginalQuantitySheet", "paymentADocumentOriginalQuantitySheet"); // Количество листов оригиналов
    context.setVariable("paymentADocumentCopyQuantity", "paymentADocumentCopyQuantity"); // Количество документов копий
    context.setVariable("paymentADocumentCopyQuantitySheet", "paymentADocumentCopyQuantitySheet"); // Количество листов документов-копий

    context.setVariable("freePayment", 1l); // Документ-основание для получения безвозмезной услуги
    context.setVariable("freePaymentADocumentCode_1", "558206010000"); // Тип представленного документа
    context.setVariable("freePaymentADocumentNumber_1", "123131"); // Номер представленного документа
    context.setVariable("freePaymentADocumentDate_1", TestUtils.getDateValue("2002-12-01")); // Дата выдачи документа
    context.setVariable("freePaymentADocumentIssue_1", "freePaymentADocumentIssue"); // Орган выдавший представленный
    // документ
    context.setVariable("freePaymentADocumentDesc_1", "freePaymentADocumentDesc"); // Особые отметки для
    // представленного документ
    context.setVariable("freePaymentADocumentOriginalQuantity_1", 1l); // Количество оригиналов
    context.setVariable("freePaymentADocumentOriginalQuantitySheet_1", 2L); // Количество листов оригиналов
 //   context.setVariable("freePaymentADocumentCopyQuantity_1", 1l); // Количество копий документов
 //   context.setVariable("freePaymentADocumentCopyQuantitySheet_1", 2L); // Количество листов копий

    context.setVariable("ownerType", "PERSON"); // Тип владельца имущества
// Данные владельца физ-лица 
    context.setVariable("ownerPersonFIOSurname", "ownerPersonFIOSurname"); // Фамилия владельца
    context.setVariable("ownerPersonFIOFirst", "ownerPersonFIOFirst"); // Имя владельца
    context.setVariable("ownerPersonFIOPatronymic", "ownerPersonFIOPatronymic"); // Отчество владельца
   // context.setVariable("ownerPersonEmail", "ownerPersonEmail"); // Email владельца
   // context.setVariable("ownerPersonPhone", "ownerPersonPhone"); // Телефон владельца
    context.setVariable("ownerPersonSNILS", "112-233-332 33"); // СНИЛС владельца
    context.setVariable("ownerPersonDateBirth", TestUtils.getDateValue("2002-12-01")); // Дата рождения владельца
  //  context.setVariable("ownerPersonPlaceBirth", "ownerPersonPlaceBirth"); // Место рождения владельца
    context.setVariable("ownerPersonSex", "M"); // Пол владельца
    //context.setVariable("ownerPersonFamilyStatus", "женат"); // Семейное положение владельца
    context.setVariable("ownerPersonPDocumentCode", "008001000000"); // Код документа
    context.setVariable("ownerPersonPDocumentSeries", "1122"); // Серия документа
    context.setVariable("ownerPersonPDocumentNumber", "122222"); // Номер документа
    context.setVariable("ownerPersonPDocumentDate", TestUtils.getDateValue("2002-12-01")); // Дата документа
    context.setVariable("ownerPersonPDocumentIssueOrgan", "ownerPersonPDocumentIssueOrgan"); // Орган выдавший документ
// Конец данных владельца физ-лица  
// Данные владельца гос органа 
    context.setVariable("ownerGovernanceCode", "ownerGovernanceCode"); // Тип
    context.setVariable("ownerGovernanceName", "ownerGovernanceName"); // Наименование
    context.setVariable("ownerGovernanceEmail", "ownerGovernanceEmail"); // Email
    context.setVariable("ownerGovernancePhone", "ownerGovernancePhone"); // Телефон
    context.setVariable("ownerGovernanceContactInfo", "ownerGovernanceContactInfo"); // Информация для контакта
// Конец данных владельца гос органа 
// Данные владельца юр лица 
    context.setVariable("ownerLegalPersonName", "ownerLegalPersonName"); // Наименование организации
    context.setVariable("ownerLegalPersonOPF", "ownerLegalPersonOPF"); // Организацонно-правовая форма
    context.setVariable("ownerLegalPersonKPP", "ownerLegalPersonKPP"); // КПП
    context.setVariable("ownerLegalPersonINN", "ownerLegalPersonINN"); // ИНН
    context.setVariable("ownerLegalPersonOGRN", "ownerLegalPersonOGRN"); // Код ОГРН
    context.setVariable("ownerLegalPersonEmail", "ownerLegalPersonEmail"); // Email
    context.setVariable("ownerLegalPersonPhone", "ownerLegalPersonPhone"); // Номер телефона
    context.setVariable("ownerLegalPersonContactInfo", "ownerLegalPersonContactInfo"); // Доп. информация о контакте
    context.setVariable("ownerLegalPersonRegDate", "ownerLegalPersonRegDate"); // Дата регистрации
    context.setVariable("ownerLegalPersonRegAgency", "ownerLegalPersonRegAgency"); // Орган регистрации
    context.setVariable("ownerLegalPersonDocumentCode", "ownerLegalPersonDocumentCode"); // Документ юр лица. Вид документа
    context.setVariable("ownerLegalPersonDocumentName", "ownerLegalPersonDocumentName"); // Документ юр лица. Наименование
    context.setVariable("ownerLegalPersonDocumentSeries", "ownerLegalPersonDocumentSeries"); // Документ юр лица. Серия
    context.setVariable("ownerLegalPersonDocumentNumber", "ownerLegalPersonDocumentNumber"); // Документ юр лица. Номер
    context.setVariable("ownerLegalPersonDocumentDate", "ownerLegalPersonDocumentDate"); // Документ юр лица. Дата выдачи
    context.setVariable("ownerLegalPersonDocumentIssueOrgan", "ownerLegalPersonDocumentIssueOrgan"); // Документ юр лица. Наименование органа выдавшего документ
    context.setVariable("ownerLegalPersonDocumentDesc", "ownerLegalPersonDocumentDesc"); // Документ юр лица. Заметки
// Конец данных владельца юр лица 
// Адрес владельца 
    context.setVariable("ownerTemporaryLocationOKATO", ""); // Адрес владельца (ОКАТО)
    context.setVariable("ownerTemporaryLocationCLADR", ""); // Адрес владельца (КЛАДР)
    context.setVariable("ownerTemporaryLocationPostalCode", "123312"); // Адрес владельца (Почтовый индекс)
    context.setVariable("ownerTemporaryLocationRegion", "40"); // Адрес владельца (Код региона)
    context.setVariable("ownerTemporaryLocationDistrictName", "ownerTemporaryLocationDistrictName"); // Адрес владельца (Район)
    context.setVariable("ownerTemporaryLocationDistrictType", "р-н"); // Адрес владельца (Тип района)
    context.setVariable("ownerTemporaryLocationCityName", "ownerTemporaryLocationCityName"); // Адрес владельца (Наименование муниципального образования)
    context.setVariable("ownerTemporaryLocationDCity", "пгт"); // Адрес владельца (Тип муниципального образования)
    context.setVariable("ownerTemporaryLocationUrbanDistictName", "ownerTemporaryLocationUrbanDistictName"); // Адрес владельца (Наименование городского района)
    context.setVariable("ownerTemporaryLocationUrbanDistictType", "р-н"); // Адрес владельца (Тип городского района)
    context.setVariable("ownerTemporaryLocationSovietVillageName", "ownerTemporaryLocationSovietVillageName"); // Адрес владельца (Наименование сельсовета)
    context.setVariable("ownerTemporaryLocationSovietVillageType", "волость"); // Адрес владельца (Тип сельсовета)
    context.setVariable("ownerTemporaryLocationLocalityName", "ownerTemporaryLocationLocalityName"); // Адрес владельца (Наименование населенного пункта)
    context.setVariable("ownerTemporaryLocationLocalityType", "высел"); // Адрес владельца (Тип населенного пункта)
    context.setVariable("ownerTemporaryLocationStreetName", "ownerTemporaryLocationStreetName"); // Адрес владельца (Название улицы)
    context.setVariable("ownerTemporaryLocationDStreets", "жт"); // Адрес владельца (Тип улицы)
    context.setVariable("ownerTemporaryLocationLocationLevel1Type", "д"); // Адрес владельца (Тип адресного элемента 1-го уровня)
    context.setVariable("ownerTemporaryLocationLocationLevel1Value", "2"); // Адрес владельца (Значение адресного элемента
    // 1-го уровня)
    context.setVariable("ownerTemporaryLocationLocationLevel2Type", "д"); // Адрес владельца (Тип адресного элемента 2-го уровня)
    context.setVariable("ownerTemporaryLocationLocationLevel2Value", "1"); // Адрес владельца (Значение адресного элемента
    // 2-го уровня)
    context.setVariable("ownerTemporaryLocationLocationLevel3Type", "блок"); // Адрес владельца (Тип адресного элемента 3-го уровня)
    context.setVariable("ownerTemporaryLocationLocationLevel3Value", "4"); // Адрес владельца (Значение адресного элемента
    // 3-го уровня)
    context.setVariable("ownerTemporaryLocationLocationApartmentType", "кв"); // Адрес владельца (Тип адресного уровня квартиры)
    context.setVariable("ownerTemporaryLocationLocationApartmentValue", "3"); // Адрес владельца (Значение адресного уровня
    // квартиры)
    context.setVariable("ownerTemporaryLocationLocationOther", "ownerTemporaryLocationLocationOther"); // Адрес владельца (Иное)
    context.setVariable("ownerTemporaryLocationLocationNote", "ownerTemporaryLocationLocationNote"); // Адрес владельца (Неформализованное описание)
// дополнительные параметры запроса 
// За период 
    context.setVariable("request_interval_start", TestUtils.getDateValue("2002-12-01"));  // Начало периода, за который необходимо предоставить сведения "date"
    context.setVariable("request_interval_end", TestUtils.getDateValue("2007-12-01"));  // Конец периода,
    // за который необходимо предоставить сведения "date"
    context.setVariable("objectKind", "ROOM"); // Вид объекта недвижимости
    context.setVariable("objectOtherKind", "objectOtherKind"); // Вид объекта недвижимости
    context.setVariable("buildingKind", "buildingKind"); // Вид строения
    context.setVariable("objectRoomKind", "ROOM"); // Вид помещения
    context.setVariable("territory", 1L); // Выписка о правах субъекта  на объекты недвижимости территории"
    context.setVariable("territoryCode_1", "01"); // "Код региона">
  }
}
