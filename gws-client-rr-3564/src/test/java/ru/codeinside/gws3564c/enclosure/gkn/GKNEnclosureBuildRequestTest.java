/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws3564c.enclosure.gkn;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.codeinside.gws.api.ExchangeContext;
import ru.codeinside.gws3564c.DummyContext;
import ru.codeinside.gws3564c.EnclosureBuilderFactory;
import ru.codeinside.gws3564c.enclosure.EnclosureRequestBuilder;
import ru.gkn.DInhabitedLocalities;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Date;


public class GKNEnclosureBuildRequestTest {


  @Before
  public void setUp() throws Exception {
  }

  /**
   * Получение кадастрового паспорта
   */
  @Test
  public void testGetCadastralPassportEnclosure() throws Exception {
    testCreateEnclosure(createGetCadastralPassportContext(), "/enclosure/gkn/request_cadastral_passport.xml");
  }
  /**
   * О запросе кадастровой справки о кадастровой стоимости земельного участка
   */
  @Test
  public void testCreateEnclosureForParcelValueRequest() throws Exception {
    testCreateEnclosure(createContextParcelValueRequest(), "/enclosure/gkn/cadastral_reference.xml");
  }

  /**
   * О земельном участке в виде кадастрового паспорта объекта недвижимости
   * @throws Exception
   */
  @Test
  public void testCadastralPassportOnParcel() throws Exception {
    testCreateEnclosure(createContextForCadastralPassportOnParsel(), "/enclosure/gkn/Cadastral_Passport_On_Parcel.xml");

  }

  /**
   * о кадастровой выписки об объекте недвижимости
   * @throws Exception
   */
  @Test
  public void testCadastralPassportReferenceOnRealtyObject() throws Exception {
    testCreateEnclosure(createContextForCadastralReferenceOnRealtyObject(), "/enclosure/gkn/reference_from_cadastral_passport.xml");
  }

  /**
   * Запрос кадастрового плана территории
   * @throws Exception
   */
  @Test
  public void testCreateRequestOnCadastralPlanOfParcel() throws Exception {
    testCreateEnclosure(createContextForCadastralPlanParcel(), "/enclosure/gkn/cadastral_plan_parcel.xml");
  }

  private ExchangeContext createContextForCadastralPlanParcel() throws ParseException {
    ExchangeContext ctx = new DummyContext();
    ctx.setVariable("smevTest", "Первичный запрос");
    ctx.setVariable("okato", "60000000000"); // string
    ctx.setVariable("oktmo", "01234567"); // string
    ctx.setVariable("requestType", "558102100000"); // string
    ctx.setVariable("enclosure_request_type", "PARCEL_CADASTRAL_PLAN"); // string
    ctx.setVariable("declarantType", "GOVERNANCE"); // string
    ctx.setVariable("declKind", "357007000000"); // string
    ctx.setVariable("declGovernanceCode", "007001001001"); // string
    ctx.setVariable("declGovernanceName", "Пенсионный фонд"); // string
    ctx.setVariable("declGovernanceEmail", "test@test"); // string
    //ctx.setVariable("declGovernanceContactInfo", "неизвестно"); // string
    //ctx.setVariable("declGovernancePhone", "445544"); // string
    ctx.setVariable("declLocationRegion", "40"); // string
    ctx.setVariable("declLocationCityName", "Калуга"); // string
    ctx.setVariable("declLocationDCity", "г"); // string
    ctx.setVariable("declLocationStreetName", "Ленина"); // string
    ctx.setVariable("declLocationDStreets", "ул"); // string
    ctx.setVariable("declLocationLocationLevel1Type", "д"); // string
    ctx.setVariable("declLocationLocationLevel1Value", "5"); // string
    ctx.setVariable("declLocationLocationApartmentType", "кв"); // string
    ctx.setVariable("declLocationLocationApartmentValue", "32"); // string
    ctx.setVariable("declLocationLocationLocationOther", "Иное"); // string
    ctx.setVariable("declLocationLocationLocationNote", "Неформализованное описание"); // string
    ctx.setVariable("agentFIOFirst", "Антон"); // string
    ctx.setVariable("agentFIOSurname", "Антонов"); // string
    ctx.setVariable("agentFIOPatronymic", "Антонович"); // string
    ctx.setVariable("agentPDocumentCode", "008001001000"); // string
    ctx.setVariable("agentPDocumentSeries", "2222"); // string
    ctx.setVariable("agentPDocumentNumber", "222222"); // string
    ctx.setVariable("agentPDocumentDate", getDateValue("2010-10-10")); // string
    ctx.setVariable("agentPDocumentIssueOrgan", "УВД,Код подразделен"); // string
    ctx.setVariable("agentPostalCode", "113563"); // string
    ctx.setVariable("agentRegion", "40"); // string
    ctx.setVariable("agentCityName", "Калуга"); // string
    ctx.setVariable("agentDCity", "г"); // string
    ctx.setVariable("agentStreetName", "Советская"); // string
    ctx.setVariable("agentDStreets", "ул"); // string
    ctx.setVariable("agentLocationLevel1Type", "д"); // string
    ctx.setVariable("agentLocationLevel1Value", "10"); // string
    ctx.setVariable("agentLocationLevel2Type", "корп"); // string
    ctx.setVariable("agentLocationLevel2Value", "1"); // string
    ctx.setVariable("agentLocationLevel3Type", "литера"); // string
    ctx.setVariable("agentLocationLevel3Value", "а"); // string
    ctx.setVariable("agentLocationApartmentType", "к"); // string
    ctx.setVariable("agentLocationApartmentValue", "1"); // string
    ctx.setVariable("agentLocationOther", "Иное"); // string
    ctx.setVariable("agentLocationNote", "Неформализованное описание"); // string
    ctx.setVariable("agentEmail", "test@test.ru"); // string
    ctx.setVariable("agentPhone", "213-23-12"); // string
    // ctx.setVariable("agentContactInfo", "Адрес для почтовой корреспонденции"); // string
    ctx.setVariable("agentSNILS", "233-234-434 34"); // string
    ctx.setVariable("agentKind", "356003000000"); // string
    ctx.setVariable("applied", 1l); // string
    ctx.setVariable("appliedADocumentNumber_1", "26-0-1-21/4001/2011-162"); // string
    ctx.setVariable("appliedADocumentDate_1", getDateValue("2012-07-23")); // string
    ctx.setVariable("appliedADocumentOriginalQuantity_1", 1L); // string
    ctx.setVariable("appliedADocumentOriginalQuantitySheet_1", 1L); // string
    ctx.setVariable("appliedADocumentName_1", "Запрос о предоставлении сведений, внесенных в государственный кадастр недвижимости"); // string
    ctx.setVariable("appliedADocumentCode_1", "558101010000"); // string
    ctx.setVariable("cadastralPassportObjKind", "002001001000"); // string
    ctx.setVariable("kptCadastralNumber", "40:19:090402"); // string
    ctx.setVariable("payment", 0l);
    return ctx;
  }

  private ExchangeContext createContextForCadastralReferenceOnRealtyObject() throws ParseException {
    ExchangeContext ctx = new DummyContext();
    ctx.setVariable("smevTest", "Первичный запрос");
    ctx.setVariable("okato", "60000000000"); // string
    ctx.setVariable("oktmo", "01234567"); // string
    ctx.setVariable("requestType", "558102100000"); // string
    ctx.setVariable("enclosure_request_type", "CADASTRAL_EXTRACT"); // string
    ctx.setVariable("declarantType", "GOVERNANCE"); // string
    ctx.setVariable("declKind", "357007000000"); // string
    ctx.setVariable("declGovernanceCode", "007001001001"); // string
    ctx.setVariable("declGovernanceName", "Пенсионный фонд"); // string
    ctx.setVariable("declGovernanceEmail", "test@test"); // string
    ctx.setVariable("declLocationRegion", "40"); // string
    ctx.setVariable("declLocationCityName", "Калуга"); // string
    ctx.setVariable("declLocationDCity", "г"); // string
    ctx.setVariable("declLocationStreetName", "Ленина"); // string
    ctx.setVariable("declLocationDStreets", "ул"); // string
    ctx.setVariable("declLocationLocationLevel1Type", "д"); // string
    ctx.setVariable("declLocationLocationLevel1Value", "5"); // string
    ctx.setVariable("declLocationLocationApartmentType", "кв"); // string
    ctx.setVariable("declLocationLocationApartmentValue", "32"); // string
    ctx.setVariable("declLocationLocationLocationOther", "Иное"); // string
    ctx.setVariable("declLocationLocationLocationNote", "Неформализованное описание"); // string
    ctx.setVariable("agentFIOFirst", "Антон"); // string
    ctx.setVariable("agentFIOSurname", "Антонов"); // string
    ctx.setVariable("agentFIOPatronymic", "Антонович"); // string
    ctx.setVariable("agentPDocumentCode", "008001001000"); // string
    ctx.setVariable("agentPDocumentSeries", "2222"); // string
    ctx.setVariable("agentPDocumentNumber", "222222"); // string
    ctx.setVariable("agentPDocumentDate", getDateValue("2010-10-10")); // string
    ctx.setVariable("agentPDocumentIssueOrgan", "УВД,Код подразделен"); // string
    ctx.setVariable("agentRegion", "40"); // string
    ctx.setVariable("agentCityName", "Калуга"); // string
    ctx.setVariable("agentDCity", "г"); // string
    ctx.setVariable("agentStreetName", "Ленина"); // string
    ctx.setVariable("agentDStreets", "ул"); // string
    ctx.setVariable("agentLocationLevel1Type", "д"); // string
    ctx.setVariable("agentLocationLevel1Value", "5"); // string
    ctx.setVariable("agentLocationApartmentType", "к"); // string
    ctx.setVariable("agentLocationApartmentValue", "32"); // string
    ctx.setVariable("agentLocationOther", "Иное"); // string
    ctx.setVariable("agentLocationNote", "Неформализованное описание"); // string
    ctx.setVariable("agentEmail", "test@test.ru"); // string
    ctx.setVariable("agentPhone", "213-23-12"); // string
    ctx.setVariable("agentSNILS", "233-234-434 34"); // string
    ctx.setVariable("agentKind", "356003000000"); // string
    ctx.setVariable("applied", 1l); // string
    ctx.setVariable("appliedADocumentNumber_1", "26-0-1-21/4001/2011-163"); // string
    ctx.setVariable("appliedADocumentDate_1", getDateValue("2012-07-23")); // string
    ctx.setVariable("appliedADocumentOriginalQuantity_1", 1L); // string
    ctx.setVariable("appliedADocumentOriginalQuantitySheet_1", 1L); // string
    ctx.setVariable("appliedADocumentName_1", "Запрос о предоставлении сведений, внесенных в государственный кадастр недвижимости"); // string
    ctx.setVariable("appliedADocumentCode_1", "558101010000"); // string
    ctx.setVariable("cadastralPassportObjKind", "002001001000"); // string
    ctx.setVariable("cadastralPassportRegion", "40"); // string
    ctx.setVariable("cadastralPassportCityName", "Калуга"); // string
    ctx.setVariable("cadastralPassportDCity", "г"); // string
    ctx.setVariable("cadastralPassportStreetName", "Цветочная"); // string
    ctx.setVariable("cadastralPassportDStreets", "ул"); // string
    ctx.setVariable("cadastralPassportLocationLevel1Type", "д"); // string
    ctx.setVariable("cadastralPassportLocationLevel1Value", "12"); // string
    ctx.setVariable("cadastralPassportLocationApartmentType", "к"); // string
    ctx.setVariable("cadastralPassportLocationApartmentValue", "1"); // string

    ctx.setVariable("KVObjKind", "002001001000"); // string
    ctx.setVariable("KV1", true); // boolean
    ctx.setVariable("KV2", true); // boolean
    ctx.setVariable("KV3", true); // boolean
    ctx.setVariable("KV4", true); // boolean
    ctx.setVariable("KV5", true); // boolean
    ctx.setVariable("KV6", true); // boolean
    ctx.setVariable("payment", 0l);
    return ctx;
  }

  private ExchangeContext createContextForCadastralPassportOnParsel() throws ParseException {
    ExchangeContext ctx = new DummyContext();
    ctx.setVariable("smevTest", "Первичный запрос");
    ctx.setVariable("okato", "60000000000"); // string
    ctx.setVariable("oktmo", "01234567"); // string
    ctx.setVariable("requestType", "558102100000"); // string
    ctx.setVariable("enclosure_request_type", "PARCEL_CADASTRAL_PASSPORT"); // string
    ctx.setVariable("declarantType", "GOVERNANCE"); // string
    ctx.setVariable("declKind", "357007000000"); // string
    ctx.setVariable("declGovernanceCode", "007001001001"); // string
    ctx.setVariable("declGovernanceName", "Пенсионный фонд"); // string
    ctx.setVariable("declGovernanceEmail", "test@test"); // string
    ctx.setVariable("declLocationRegion", "40"); // string
    ctx.setVariable("declLocationCityName", "Калуга"); // string
    ctx.setVariable("declLocationDCity", "г"); // string
    ctx.setVariable("declLocationStreetName", "Ленина"); // string
    ctx.setVariable("declLocationDStreets", "ул"); // string
    ctx.setVariable("declLocationLocationLevel1Type", "д"); // string
    ctx.setVariable("declLocationLocationLevel1Value", "5"); // string
    ctx.setVariable("declLocationLocationApartmentType", "кв"); // string
    ctx.setVariable("declLocationLocationApartmentValue", "32"); // string
    ctx.setVariable("declLocationLocationLocationOther", "Иное"); // string
    ctx.setVariable("declLocationLocationLocationNote", "Неформализованное описание"); // string
    ctx.setVariable("agentFIOFirst", "Антон"); // string
    ctx.setVariable("agentFIOSurname", "Антонов"); // string
    ctx.setVariable("agentFIOPatronymic", "Антонович"); // string
    ctx.setVariable("agentPDocumentCode", "008001001000"); // string
    ctx.setVariable("agentPDocumentSeries", "2222"); // string
    ctx.setVariable("agentPDocumentNumber", "222222"); // string
    ctx.setVariable("agentPDocumentDate", getDateValue("2010-10-10")); // string
    ctx.setVariable("agentPDocumentIssueOrgan", "УВД,Код подразделен"); // string
    ctx.setVariable("agentPostalCode", "113563"); // string
    ctx.setVariable("agentRegion", "40"); // string
    ctx.setVariable("agentCityName", "Калуга"); // string
    ctx.setVariable("agentDCity", "г"); // string
    ctx.setVariable("agentStreetName", "Советская"); // string
    ctx.setVariable("agentDStreets", "ул"); // string
    ctx.setVariable("agentLocationLevel1Type", "д"); // string
    ctx.setVariable("agentLocationLevel1Value", "10"); // string
    ctx.setVariable("agentLocationLevel2Type", "корп"); // string
    ctx.setVariable("agentLocationLevel2Value", "1"); // string
    ctx.setVariable("agentLocationLevel3Type", "литера"); // string
    ctx.setVariable("agentLocationLevel3Value", "а"); // string
    ctx.setVariable("agentLocationApartmentType", "к"); // string
    ctx.setVariable("agentLocationApartmentValue", "1"); // string
    ctx.setVariable("agentLocationOther", "Иное"); // string
    ctx.setVariable("agentLocationNote", "Неформализованное описание"); // string
    ctx.setVariable("agentEmail", "test@test.ru"); // string
    ctx.setVariable("agentPhone", "213-23-12"); // string
    ctx.setVariable("agentSNILS", "233-234-434 34"); // string
    ctx.setVariable("agentKind", "356003000000"); // string
    ctx.setVariable("applied", 1l); // string
    ctx.setVariable("appliedADocumentNumber_1", "26-0-1-21/4001/2011-162"); // string
    ctx.setVariable("appliedADocumentDate_1", getDateValue("2012-07-23")); // string
    ctx.setVariable("appliedADocumentOriginalQuantity_1", 1L); // string
    ctx.setVariable("appliedADocumentOriginalQuantitySheet_1", 1L); // string
    ctx.setVariable("appliedADocumentName_1", "Запрос о предоставлении сведений, внесенных в государственный кадастр недвижимости"); // string
    ctx.setVariable("appliedADocumentCode_1", "558101010000"); // string
    ctx.setVariable("cadastralPassportObjKind", "002001001000"); // string
    ctx.setVariable("cadastralPassport", "558102100000"); // string
    ctx.setVariable("cadastralPassportRegion", "40"); // string
    ctx.setVariable("cadastralPassportCityName", "Калуга"); // string
    ctx.setVariable("cadastralPassportDCity", "г"); // string
    ctx.setVariable("cadastralPassportStreetName", "Брежнева"); // string
    ctx.setVariable("cadastralPassportDStreets", "ул"); // string
    ctx.setVariable("cadastralPassportLocationLevel1Value", "23"); // string
    ctx.setVariable("cadastralPassportLocationLevel1Type", "д"); // string
    ctx.setVariable("cadastralPassportLocationApartmentValue", "3"); // string
    ctx.setVariable("cadastralPassportLocationApartmentType", "кв"); // string
    ctx.setVariable("payment", 0l);
    return ctx;
  }

  private ExchangeContext createContextParcelValueRequest() throws ParseException {
    ExchangeContext ctx = new DummyContext();
    ctx.setVariable("smevTest", "Первичный запрос");
    ctx.setVariable("okato", "60000000000"); // string
    ctx.setVariable("oktmo", "01234567"); // string
    ctx.setVariable("requestType", "558102100000"); // string
    ctx.setVariable("enclosure_request_type", "PARCEL_VALUE"); // string
    ctx.setVariable("declarantType", "GOVERNANCE"); // string
    ctx.setVariable("declKind", "357007000000"); // string
    ctx.setVariable("declGovernanceCode", "007001001001"); // string
    ctx.setVariable("declGovernanceName", "Пенсионный фонд"); // string
    ctx.setVariable("declGovernanceEmail", "test@test"); // string
    ctx.setVariable("declLocationRegion", "40"); // string
    ctx.setVariable("declLocationCityName", "Калуга"); // string
    ctx.setVariable("declLocationDCity", "г"); // string
    ctx.setVariable("declLocationStreetName", "Ленина"); // string
    ctx.setVariable("declLocationDStreets", "ул"); // string
    ctx.setVariable("declLocationLocationLevel1Type", "д"); // string
    ctx.setVariable("declLocationLocationLevel1Value", "5"); // string
    ctx.setVariable("declLocationLocationApartmentType", "кв"); // string
    ctx.setVariable("declLocationLocationApartmentValue", "32"); // string
    ctx.setVariable("declLocationLocationLocationOther", "Иное"); // string
    ctx.setVariable("declLocationLocationLocationNote", "Неформализованное описание"); // string
    ctx.setVariable("agentFIOFirst", "Антон"); // string
    ctx.setVariable("agentFIOSurname", "Антонов"); // string
    ctx.setVariable("agentFIOPatronymic", "Антонович"); // string
    ctx.setVariable("agentPDocumentCode", "008001001000"); // string
    ctx.setVariable("agentPDocumentSeries", "2222"); // string
    ctx.setVariable("agentPDocumentNumber", "222222"); // string
    ctx.setVariable("agentPDocumentDate", getDateValue("2010-10-10")); // string
    ctx.setVariable("agentPDocumentIssueOrgan", "УВД,Код подразделен"); // string
    ctx.setVariable("agentPostalCode", "121213"); // string
    ctx.setVariable("agentRegion", "40"); // string
    ctx.setVariable("agentCityName", "Калуга"); // string
    ctx.setVariable("agentDCity", "г"); // string
    ctx.setVariable("agentStreetName", "Коммунистическая"); // string
    ctx.setVariable("agentDStreets", "ул"); // string
    ctx.setVariable("agentLocationLevel1Type", "д"); // string
    ctx.setVariable("agentLocationLevel1Value", "23"); // string
    ctx.setVariable("agentLocationLevel2Type", "корп"); // string
    ctx.setVariable("agentLocationLevel2Value", "2"); // string
    ctx.setVariable("agentLocationLevel3Type", "литера"); // string
    ctx.setVariable("agentLocationLevel3Value", "в"); // string
    ctx.setVariable("agentLocationApartmentType", "к"); // string
    ctx.setVariable("agentLocationApartmentValue", "233"); // string
    ctx.setVariable("agentLocationOther", "Иное"); // string
    ctx.setVariable("agentLocationNote", "Неформализованное описание"); // string
    ctx.setVariable("agentEmail", "test@test.ru"); // string
    ctx.setVariable("agentPhone", "213-23-12"); // string
    ctx.setVariable("agentSNILS", "233-234-434 34"); // string
    ctx.setVariable("agentKind", "356003000000"); // string
    ctx.setVariable("applied", 1L); // string
    ctx.setVariable("appliedADocumentNumber_1", "26-0-1-21/4001/2011-172"); // string
    ctx.setVariable("appliedADocumentDate_1", getDateValue("2012-07-24")); // string
    ctx.setVariable("appliedADocumentOriginalQuantity_1", 1L); // string
    ctx.setVariable("appliedADocumentOriginalQuantitySheet_1", 1L); // string
    ctx.setVariable("appliedADocumentName_1", "Запрос о предоставлении сведений, внесенных в государственный кадастр недвижимости"); // string
    ctx.setVariable("appliedADocumentCode_1", "558101010000"); // string
    ctx.setVariable("cadastralPassportRegion", "40"); // string
    ctx.setVariable("cadastralPassportCityName", "Калуга"); // string
    ctx.setVariable("cadastralPassportDCity", "г"); // string
    ctx.setVariable("cadastralPassportStreetName", "Лесная"); // string
    ctx.setVariable("cadastralPassportDStreets", "ул"); // string
    ctx.setVariable("cadastralPassportLocationLevel1Type", "д"); // string
    ctx.setVariable("cadastralPassportLocationLevel1Value", "12"); // string
    ctx.setVariable("cadastralPassportLocationApartmentType", "к"); // string
    ctx.setVariable("cadastralPassportLocationApartmentValue", "1"); // string
    ctx.setVariable("cadastralPassportEmail", "test@test"); // string
    ctx.setVariable("cadastralPassportPhone", "213-23-12"); // string
    ctx.setVariable("payment", 0l);
    return ctx;
  }

  private void testCreateEnclosure(ExchangeContext resultEnclosure, String fileWithExpectedEnclosure) throws IOException {
    InputStream is = this.getClass().getResourceAsStream(fileWithExpectedEnclosure);
    String expectedContent = IOUtils.toString(is);
    EnclosureRequestBuilder enclosureBuilder = EnclosureBuilderFactory.createEnclosureBuilder(resultEnclosure);
    String id = "7b983700-651d-43d8-bdb4-26dd683df535";
    Assert.assertEquals(expectedContent, enclosureBuilder.createEnclosure(id));
  }

  @Test
  public void testXMLEnum() throws Exception {
    Assert.assertNotNull(DInhabitedLocalities.fromValue("дп"));
  }

  private ExchangeContext createGetCadastralPassportContext() throws ParseException {
    ExchangeContext ctx = new DummyContext();
    ctx.setVariable("smevTest", "Первичный запрос");
    ctx.setVariable("okato", "60000000000"); // string
    ctx.setVariable("oktmo", "01234567"); // string
    ctx.setVariable("requestType", "558102100000"); // string
    ctx.setVariable("enclosure_request_type", "CADASTRAL_PASSPORT"); // string
    ctx.setVariable("declarantType", "GOVERNANCE"); // string
    ctx.setVariable("declKind", "357007000000"); // string
    ctx.setVariable("declGovernanceCode", "007001001001"); // string
    ctx.setVariable("declGovernanceName", "Пенсионный фонд"); // string
    ctx.setVariable("declGovernanceEmail", "test@test"); // string
    ctx.setVariable("declLocationRegion", "40"); // string
    ctx.setVariable("declLocationCityName", "Калуга"); // string
    ctx.setVariable("declLocationDCity", "г"); // string
    ctx.setVariable("declLocationStreetName", "Ленина"); // string
    ctx.setVariable("declLocationDStreets", "ул"); // string
    ctx.setVariable("declLocationLocationLevel1Type", "д"); // string
    ctx.setVariable("declLocationLocationLevel1Value", "5"); // string
    ctx.setVariable("declLocationLocationApartmentType", "кв"); // string
    ctx.setVariable("declLocationLocationApartmentValue", "32"); // string
    ctx.setVariable("declLocationLocationLocationOther", "Иное"); // string
    ctx.setVariable("declLocationLocationLocationNote", "Неформализованное описание"); // string
    ctx.setVariable("agentFIOFirst", "Антон"); // string
    ctx.setVariable("agentFIOSurname", "Антонов"); // string
    ctx.setVariable("agentFIOPatronymic", "Антонович"); // string
    ctx.setVariable("agentPDocumentCode", "008001001000"); // string
    ctx.setVariable("agentPDocumentSeries", "2222"); // string
    ctx.setVariable("agentPDocumentNumber", "222222"); // string
    ctx.setVariable("agentPDocumentDate", getDateValue("2010-10-10")); // string
    ctx.setVariable("agentPDocumentIssueOrgan", "УВД,Код подразделен"); // string
    ctx.setVariable("agentPostalCode", "113563"); // string
    ctx.setVariable("agentRegion", "40"); // string
    ctx.setVariable("agentCityName", "Калуга"); // string
    ctx.setVariable("agentDCity", "г"); // string
    ctx.setVariable("agentStreetName", "Советская"); // string
    ctx.setVariable("agentDStreets", "ул"); // string
    ctx.setVariable("agentLocationLevel1Type", "д"); // string
    ctx.setVariable("agentLocationLevel1Value", "10"); // string
    ctx.setVariable("agentLocationLevel2Type", "корп"); // string
    ctx.setVariable("agentLocationLevel2Value", "1"); // string
    ctx.setVariable("agentLocationLevel3Type", "литера"); // string
    ctx.setVariable("agentLocationLevel3Value", "а"); // string
    ctx.setVariable("agentLocationApartmentType", "к"); // string
    ctx.setVariable("agentLocationApartmentValue", "1"); // string
    ctx.setVariable("agentLocationOther", "Иное"); // string
    ctx.setVariable("agentLocationNote", "Неформализованное описание"); // string
    ctx.setVariable("agentEmail", "test@test.ru"); // string
    ctx.setVariable("agentPhone", "213-23-12"); // string
    ctx.setVariable("agentSNILS", "233-234-434 34"); // string
    ctx.setVariable("agentKind", "356003000000"); // string
    ctx.setVariable("applied", 1l);
    ctx.setVariable("payment", 0l);
    ctx.setVariable("appliedADocumentNumber_1", "26-0-1-21/4001/2011-162"); // string
    ctx.setVariable("appliedADocumentDate_1", getDateValue("2012-07-23")); // string
    ctx.setVariable("appliedADocumentOriginalQuantity_1", 1L); // string
    ctx.setVariable("appliedADocumentOriginalQuantitySheet_1", 1L); // string
    ctx.setVariable("appliedADocumentName_1", "Запрос о предоставлении сведений, внесенных в государственный кадастр недвижимости"); // string
    ctx.setVariable("appliedADocumentCode_1", "558101010000"); // string
    ctx.setVariable("cadastralPassportObjKind", "002001001000"); // string
    ctx.setVariable("cadastralPassport", "558102100000"); // string
    ctx.setVariable("cadastralPassportRegion", "40"); // string
    ctx.setVariable("cadastralPassportCityName", "Калуга"); // string
    ctx.setVariable("cadastralPassportDCity", "г"); // string
    ctx.setVariable("cadastralPassportStreetName", "Брежнева"); // string
    ctx.setVariable("cadastralPassportDStreets", "ул"); // string
    ctx.setVariable("cadastralPassportLocationLevel1Value", "23"); // string
    ctx.setVariable("cadastralPassportLocationLevel1Type", "д"); // string
    ctx.setVariable("cadastralPassportLocationApartmentValue", "3"); // string
    ctx.setVariable("cadastralPassportLocationApartmentType", "кв"); // string
    return ctx;
  }

  private Date getDateValue(String dateStr) throws ParseException {
    return DateUtils.parseDate(dateStr, new String[]{"yyyy-MM-dd"});
  }


}
