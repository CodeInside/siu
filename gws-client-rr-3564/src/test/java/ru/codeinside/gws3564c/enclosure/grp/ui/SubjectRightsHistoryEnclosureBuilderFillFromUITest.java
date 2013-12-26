/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws3564c.enclosure.grp.ui;

import org.junit.Before;
import org.junit.Test;
import ru.codeinside.gws.api.ExchangeContext;
import ru.codeinside.gws3564c.DummyContext;
import ru.codeinside.gws3564c.enclosure.grp.SubjectRightsHistoryEnclosureBuilder;
import ru.codeinside.gws3564c.enclosure.grp.TestUtils;

public class SubjectRightsHistoryEnclosureBuilderFillFromUITest {

  private ExchangeContext context;
  private SubjectRightsHistoryEnclosureBuilder enclosureBuilder;

  @Before
  public void setUp() throws Exception {
    context = new DummyContext();
    enclosureBuilder = new SubjectRightsHistoryEnclosureBuilder(context);
  }

  @Test
  public void testWeCanReproduceSampleRequest() throws Exception {
    fillContext();

    String guid = "7b983700-651d-43d8-bdb4-26dd683df535";
    String xml = enclosureBuilder.createEnclosure(guid);
    //System.out.println(xml);
  }

  @Test
  public void testTestLegalPersonFillData() throws Exception {
    fillContextForLegalPerson();
    String xml = enclosureBuilder.createEnclosure( "7b983700-651d-43d8-bdb4-26dd683df535");
    System.out.println(xml);
  }

  private void fillContextForLegalPerson() {
    context.setVariable("ownerTemporaryLocationCLADR", "");
    //context.setVariable("declLegalPersonRegAgency", "налоговая");
    context.setVariable("declLegalPersonDocumentSeries", "1231");
    context.setVariable("ownerPersonPDocumentIssueOrgan", "УВД");
    context.setVariable("ownerLegalPersonINN", "1212121221");
    context.setVariable("ownerGovernanceName", "Пенсионный фонд");
    context.setVariable("ownerTemporaryLocationDistrictName", "");
    context.setVariable("ownerPersonFIOSurname", "Иванов");
    context.setVariable("agentTemporaryLocationLocationOther", "");
    context.setVariable("smevInitialRegDate", TestUtils.getDateValue("2013-07-23"));
    context.setVariable("freePayment", 1L);
    context.setVariable("ownerTemporaryLocationRegion", "40");
    context.setVariable("declPersonSNILS", "112-233-332 33");
    context.setVariable("declLegalPersonDocumentName", "свидетельство");
    context.setVariable("ownerTemporaryLocationCityName", "Городище");
    context.setVariable("agentTemporaryLocationLocalityType", null);
    context.setVariable("ownerLegalPersonDocumentCode", "008002000000");
    context.setVariable("ownerLegalPersonName", "Заготовочная контора 1");
    context.setVariable("agentTemporaryLocationLocalityName", "");
    context.setVariable("freePaymentADocumentNumber_1", "1");
    context.setVariable("ownerTemporaryLocationDistrictType", null);
    context.setVariable("ownerTemporaryLocationOKATO", "");
    context.setVariable("Legal", "Юридические лица");
    context.setVariable("declLegalPersonPhone", "12121212");
    context.setVariable("agentTemporaryLocationPostalCode", "");
    context.setVariable("ownerTemporaryLocationLocationLevel1Value", "1");
    context.setVariable("ownerLegalPersonDocumentIssueOrgan", "dsdsddsd");
    context.setVariable("ownerTemporaryLocationLocationLevel3Value", "");
    context.setVariable("ownerPersonSex", "M");
    context.setVariable("okato", "60000000000");
    context.setVariable("declLegalPersonDocumentNumber", "212122");
    context.setVariable("declLegalPersonContactInfo", "212121212");
    context.setVariable("declPersonEmail", "test@test.com");
    context.setVariable("declLegalPersonRegDate", TestUtils.getDateValue("2013-07-23"));
    //context.setVariable("declLegalPersonKPP", "12343434");
    //context.setVariable("ownerLegalPersonOPF", "809000000039");
    context.setVariable("declPersonFIOFirst", "Иван");
    context.setVariable("ownerTemporaryLocationLocationOther", "иное");
    context.setVariable("ownerTemporaryLocationDCity", "г");
    context.setVariable("freePaymentADocumentOriginalQuantitySheet_1", "1");
    context.setVariable("isPaymentFree", true);
    context.setVariable("procedureId", "301");
    context.setVariable("declPersonSex", "M");
    //context.setVariable("ownerLegalPersonRegAgency", "Налоговая");
    context.setVariable("territory", 1L);
    context.setVariable("territoryCode_1", "01");
    context.setVariable("ownerLegalPersonDocumentDate", TestUtils.getDateValue("2013-07-23"));
    context.setVariable("agentTemporaryLocationRegion", "40");
    context.setVariable("declPersonPDocumentSeries", "1222");
    context.setVariable("freePaymentADocumentDate_1", TestUtils.getDateValue("2013-07-23"));
    context.setVariable("declPersonPDocumentIssueOrgan", "УВД");
    context.setVariable("agentTemporaryLocationLocationApartmentValue", "1");
    context.setVariable("ownerLegalPersonRegDate", TestUtils.getDateValue("2013-07-23"));
    context.setVariable("agentTemporaryLocationSovietVillageName", "");
    context.setVariable("ownerPersonSNILS", "112-233-332 33");
    context.setVariable("agentTemporaryLocationUrbanDistictType", null);
    context.setVariable("ownerLegalPersonDocumentDesc", "12121221");
    context.setVariable("agentPhone", "123121");
    context.setVariable("agentFIOFirst", "Антон");
    context.setVariable("declPersonPhone", "12212212221");
    context.setVariable("agentTemporaryLocationLocationApartmentType", "кв");
    context.setVariable("payment", null);
    context.setVariable("ownerTemporaryLocationLocalityName", "");
    context.setVariable("agentTemporaryLocationLocationLevel2Type", null);
    context.setVariable("smevTest", "Первичный запрос");
    context.setVariable("agentSNILS", "112-233-332 33");
    context.setVariable("Individuals", "Индивидуальные предпринематели");
    context.setVariable("ownerLegalPersonDocumentName", "Свидетельство");
    context.setVariable("declPersonMarriageStatus", "женат");
    context.setVariable("procedureTypeName", "0");
    context.setVariable("agentPDocumentSeries", "1221");
    context.setVariable("declKind", "357007000000");
    context.setVariable("request_interval_start", null);
    context.setVariable("declGovernancePhone", "12342323");
    context.setVariable("enclosure_request_type", "SUBJECT_RIGHTS_HISTORY");
    context.setVariable("objectKind", "BUILDING");
    context.setVariable("agentPDocumentDate", TestUtils.getDateValue("2013-07-23"));
    //context.setVariable("ownerLegalPersonKPP", "1221211212");
    context.setVariable("declLegalPersonEmail", "tesst@rerer.com");
    context.setVariable("ownerTemporaryLocationLocationLevel2Type", null);
    context.setVariable("declLegalPersonDocumentDate", TestUtils.getDateValue("2013-07-23"));
    context.setVariable("declLegalPersonINN", "12343434");
    context.setVariable("smevInitialRegNumber", "137519339796253077");
    context.setVariable("declPersonPDocumentCode", "008001001000");
    context.setVariable("declPersonPDocumentNumber", "121212");
    context.setVariable("agentTemporaryLocationLocationNote", "");
    context.setVariable("agentTemporaryLocationOKATO", "");
    context.setVariable("agentTemporaryLocationCLADR", "");
    context.setVariable("freePaymentADocumentDesc_1", "нет");
    //context.setVariable("declLegalPersonDocumentDesc", "нет");
    context.setVariable("freePaymentADocumentIssue_1", "УВД");
    //context.setVariable("ownerLegalPersonContactInfo", "");
    context.setVariable("declLegalPersonDocumentIssueOrgan", "Налоговая");
    context.setVariable("ownerTemporaryLocationPostalCode", "");
    context.setVariable("agentTemporaryLocationLocationLevel3Value", "");
    context.setVariable("declGovernanceContactInfo", "");
    context.setVariable("ownerLegalPersonDocumentNumber", "11133331");
    context.setVariable("ownerTemporaryLocationLocalityType", null);
    context.setVariable("agentTemporaryLocationDCity", "г");
    context.setVariable("ownerGovernanceCode", "007001001001");
    context.setVariable("freePaymentADocumentCode_1", "008001000000");
    context.setVariable("ownerLegalPersonOGRN", "121212122");
    context.setVariable("agentTemporaryLocationDStreets", "ул");
    context.setVariable("ownerTemporaryLocationSovietVillageType", null);
    context.setVariable("declGovernanceCode", "007001001001");
    context.setVariable("agentTemporaryLocationUrbanDistictName", "");
    context.setVariable("ownerPersonFIOPatronymic", "Иванович");
    context.setVariable("agentFIOPatronymic", "Антонович");
    context.setVariable("ownerTemporaryLocationDStreets", "ул");
    context.setVariable("ownerTemporaryLocationLocationLevel1Type", "д");
    context.setVariable("declGovernanceName", "Пенсионный фонд");
    context.setVariable("ownerLegalPersonDocumentSeries", "1111");
    context.setVariable("serviceId", "3");
    context.setVariable("declPersonPDocumentDate", null);
    context.setVariable("agentTemporaryLocationStreetName", "Южная");
    context.setVariable("agentTemporaryLocationDistrictType", null);
    context.setVariable("declarantLogin", "test");
    context.setVariable("ownerTemporaryLocationUrbanDistictType", null);
    context.setVariable("ownerTemporaryLocationLocationApartmentType", "кв");
    context.setVariable("ownerPersonDateBirth", null);
    context.setVariable("agentTemporaryLocationLocationLevel2Value", "");
    context.setVariable("agentPDocumentCode", "008001001000");
    context.setVariable("oktmo", "01234567");
    context.setVariable("declLegalPersonName", "Точка");
    context.setVariable("declLegalPersonOGRN", "1212122");
    //context.setVariable("ownerLegalPersonPhone", "21212121212");
    context.setVariable("agentTemporaryLocationLocationLevel1Value", "1");
    //context.setVariable("ownerLegalPersonEmail", "test@ewew");
    context.setVariable("ownerPersonPDocumentCode", "008001001000");
    context.setVariable("agentTemporaryLocationCityName", "Калуга");
    context.setVariable("agentEmail", "reeer@erere.com");
    context.setVariable("freePaymentADocumentOriginalQuantity_1", "1");
    context.setVariable("agentTemporaryLocationLocationLevel1Type", "д");
    context.setVariable("freePaymentADocumentName_1", "справка");
    context.setVariable("agentPDocumentNumber", "121212");
    context.setVariable("agentTemporaryLocationDistrictName", "");
    context.setVariable("agentPDocumentIssueOrgan", "УВД");
    context.setVariable("declLegalPersonDocumentCode", "008002000000");
    context.setVariable("declarantType", "ORGANISATION");
    context.setVariable("request_interval_end", TestUtils.getDateValue("2013-07-23"));
    //context.setVariable("declLegalPersonOPF", "809000000039");
    context.setVariable("objectOtherKind", null);
    context.setVariable("ownerPersonPDocumentDate", null);
    context.setVariable("ownerTemporaryLocationLocationNote", "описание адреа");
    context.setVariable("buildingKind", "APARTMENTS");
    context.setVariable("agentFIOSurname", "Антонов");
    context.setVariable("ownerPersonPDocumentSeries", "1111");
    context.setVariable("ownerPersonPDocumentNumber", "123123");
    context.setVariable("ownerType", "ORGANISATION");
    context.setVariable("ownerTemporaryLocationStreetName", "Додож");
    context.setVariable("objectRoomKind", null);
    context.setVariable("ownerTemporaryLocationLocationLevel2Value", "");
    context.setVariable("ownerTemporaryLocationSovietVillageName", "");
    context.setVariable("ownerTemporaryLocationLocationApartmentValue", "1");
    context.setVariable("requestType", "558102100000");
    context.setVariable("Physical", "Физические лица");
    context.setVariable("agentTemporaryLocationLocationLevel3Type", null);
    context.setVariable("agentKind", "356002000000");
    context.setVariable("applied", 1l);
    context.setVariable("ownerTemporaryLocationUrbanDistictName", "");
    context.setVariable("agentTemporaryLocationSovietVillageType", null);
    context.setVariable("ownerPersonFIOFirst", "Иван");
    context.setVariable("declPersonFIOSurname", "Иванов");
    context.setVariable("declPersonFIOPatronymic", "Иванович");
    context.setVariable("ownerTemporaryLocationLocationLevel3Type", null);
    context.setVariable("appliedADocumentDate_1", TestUtils.getDateValue("2012-07-24")); // string
    context.setVariable("appliedADocumentOriginalQuantity_1", 1L); // string
    context.setVariable("appliedADocumentOriginalQuantitySheet_1", 1L); // string

    context.setVariable("appliedADocumentName_1",
                        "Запрос о предоставлении сведений, содержащихся в Едином государственном реестре прав на недвижимое имущество и сделок с ним");
    context.setVariable("appliedADocumentCode_1", "558102100000"); // string
    context.setVariable("appliedADocumentNumber_1", "26-0-1-21/4001/2011-166"); // string
  }

  private void fillContext() {
    context.setVariable("ownerTemporaryLocationCLADR", "");
   // context.setVariable("declLegalPersonRegAgency", "налоговая");
    context.setVariable("declLegalPersonDocumentSeries", "1231");
    context.setVariable("ownerPersonPDocumentIssueOrgan", "УВД");
    context.setVariable("ownerLegalPersonINN", "1212121221");
    context.setVariable("ownerGovernanceName", "Пенсионный фонд");
    context.setVariable("ownerTemporaryLocationDistrictName", "");
    context.setVariable("ownerPersonFIOSurname", "Иванов");
    context.setVariable("agentTemporaryLocationLocationOther", "");
    context.setVariable("smevInitialRegDate", TestUtils.getDateValue("2013-07-23"));
    context.setVariable("freePayment", 1L);
    context.setVariable("ownerTemporaryLocationRegion", "40");
    context.setVariable("declPersonSNILS", "112-233-332 33");
    context.setVariable("declLegalPersonDocumentName", "свидетельство");
    context.setVariable("ownerTemporaryLocationCityName", "Городище");
    context.setVariable("agentTemporaryLocationLocalityType", null);
    context.setVariable("ownerLegalPersonDocumentCode", "008002000000");
    context.setVariable("ownerLegalPersonName", "Заготовочная контора 1");
    context.setVariable("agentTemporaryLocationLocalityName", "");
    context.setVariable("freePaymentADocumentNumber_1", "1");
    context.setVariable("ownerTemporaryLocationDistrictType", null);
    context.setVariable("ownerTemporaryLocationOKATO", "");
    context.setVariable("Legal", "Юридические лица");
    context.setVariable("declLegalPersonPhone", "12121212");
    context.setVariable("agentTemporaryLocationPostalCode", "");
    context.setVariable("ownerTemporaryLocationLocationLevel1Value", "1");
    context.setVariable("ownerLegalPersonDocumentIssueOrgan", "dsdsddsd");
    context.setVariable("ownerTemporaryLocationLocationLevel3Value", "");
    context.setVariable("ownerPersonSex", "M");
    context.setVariable("okato", "60000000000");
    context.setVariable("declLegalPersonDocumentNumber", "212122");
    context.setVariable("declLegalPersonContactInfo", "212121212");
    context.setVariable("declPersonEmail", "test@test.com");
    context.setVariable("declLegalPersonRegDate", null);
   // context.setVariable("declLegalPersonKPP", "12343434");
    //context.setVariable("ownerLegalPersonOPF", "809000000039");
    context.setVariable("declPersonFIOFirst", "Иван");
    context.setVariable("ownerTemporaryLocationLocationOther", "иное");
    context.setVariable("ownerTemporaryLocationDCity", "г");
    context.setVariable("freePaymentADocumentOriginalQuantitySheet_1", 1L);
    context.setVariable("isPaymentFree", true);
    context.setVariable("procedureId", "301");
    context.setVariable("declPersonSex", "M");
    //context.setVariable("ownerLegalPersonRegAgency", "Налоговая");
    context.setVariable("territory", 1L);
    context.setVariable("territoryCode_1", "40");
    context.setVariable("ownerLegalPersonDocumentDate", null);
    context.setVariable("agentTemporaryLocationRegion", "40");
    context.setVariable("declPersonPDocumentSeries", "1222");
    context.setVariable("freePaymentADocumentDate_1", TestUtils.getDateValue("2013-07-23"));
    context.setVariable("declPersonPDocumentIssueOrgan", "УВД");
    context.setVariable("agentTemporaryLocationLocationApartmentValue", "1");
    context.setVariable("ownerLegalPersonRegDate", null);
    context.setVariable("agentTemporaryLocationSovietVillageName", "");
    context.setVariable("ownerPersonSNILS", "112-233-332 33");
    context.setVariable("agentTemporaryLocationUrbanDistictType", null);
    context.setVariable("ownerLegalPersonDocumentDesc", "12121221");
    context.setVariable("agentPhone", "123121");
    context.setVariable("agentFIOFirst", "Антон");
    context.setVariable("declPersonPhone", "12212212221");
    context.setVariable("agentTemporaryLocationLocationApartmentType", "кв");
    context.setVariable("payment", null);
    context.setVariable("ownerTemporaryLocationLocalityName", "");
    context.setVariable("agentTemporaryLocationLocationLevel2Type", null);
    context.setVariable("smevTest", "Первичный запрос");
    context.setVariable("agentSNILS", "112-233-332 33");
    context.setVariable("Individuals", "Индивидуальные предпринематели");
    context.setVariable("ownerLegalPersonDocumentName", "Свидетельство");
    context.setVariable("declPersonMarriageStatus", "женат");
    context.setVariable("procedureTypeName", "0");
    context.setVariable("agentPDocumentSeries", "1221");
    context.setVariable("declKind", "357007000000");
    context.setVariable("request_interval_start", TestUtils.getDateValue("2013-07-23"));
    context.setVariable("declGovernancePhone", "12342323");
    context.setVariable("enclosure_request_type", "SUBJECT_RIGHTS_HISTORY");
    context.setVariable("objectKind", "ROOM");
    context.setVariable("agentPDocumentDate",TestUtils.getDateValue("2013-07-23"));
    context.setVariable("ownerLegalPersonKPP", "1221211212");
    context.setVariable("declLegalPersonEmail", "tesst@rerer.com");
    context.setVariable("ownerTemporaryLocationLocationLevel2Type", null);
    context.setVariable("declLegalPersonDocumentDate", null);
    context.setVariable("declLegalPersonINN", "12343434");
    context.setVariable("smevInitialRegNumber", "137518949711569753");
    context.setVariable("declPersonPDocumentCode", "008001001000");
    context.setVariable("declPersonPDocumentNumber", "121212");
    context.setVariable("agentTemporaryLocationLocationNote", "");
    context.setVariable("agentTemporaryLocationOKATO", "");
    context.setVariable("agentTemporaryLocationCLADR", "");
    context.setVariable("freePaymentADocumentDesc_1", "нет");
    //context.setVariable("declLegalPersonDocumentDesc", "нет");
    context.setVariable("freePaymentADocumentIssue_1", "УВД");
    //context.setVariable("ownerLegalPersonContactInfo", "");
    context.setVariable("declLegalPersonDocumentIssueOrgan", "Налоговая");
    context.setVariable("ownerTemporaryLocationPostalCode", "");
    context.setVariable("agentTemporaryLocationLocationLevel3Value", "");
    context.setVariable("declGovernanceContactInfo", "");
    context.setVariable("ownerLegalPersonDocumentNumber", "11133331");
    context.setVariable("ownerTemporaryLocationLocalityType", null);
    context.setVariable("agentTemporaryLocationDCity", "г");
    context.setVariable("ownerGovernanceCode", "007001001001");
    context.setVariable("freePaymentADocumentCode_1", "008001000000");
    context.setVariable("ownerLegalPersonOGRN", "121212122");
    context.setVariable("agentTemporaryLocationDStreets", "ул");
    context.setVariable("ownerTemporaryLocationSovietVillageType", null);
    context.setVariable("declGovernanceCode", "007001001001");
    context.setVariable("agentTemporaryLocationUrbanDistictName", "");
    context.setVariable("ownerPersonFIOPatronymic", "Иванович");
    context.setVariable("agentFIOPatronymic", "Антонович");
    context.setVariable("ownerTemporaryLocationDStreets", "ул");
    context.setVariable("ownerTemporaryLocationLocationLevel1Type", "д");
    context.setVariable("declGovernanceName", "Пенсионный фонд");
    context.setVariable("ownerLegalPersonDocumentSeries", "1111");
    context.setVariable("serviceId", "3");
    context.setVariable("declPersonPDocumentDate", null);
    context.setVariable("agentTemporaryLocationStreetName", "Южная");
    context.setVariable("agentTemporaryLocationDistrictType", null);
    context.setVariable("declarantLogin", "test");
    context.setVariable("ownerTemporaryLocationUrbanDistictType", null);
    context.setVariable("ownerTemporaryLocationLocationApartmentType", "кв");
    context.setVariable("ownerPersonDateBirth", null);
    context.setVariable("agentTemporaryLocationLocationLevel2Value", "");
    context.setVariable("agentPDocumentCode", "008001001000");
    context.setVariable("oktmo", "01234567");
    context.setVariable("declLegalPersonName", "Точка");
    context.setVariable("declLegalPersonOGRN", "1212122");
    context.setVariable("ownerLegalPersonPhone", "21212121212");
    context.setVariable("agentTemporaryLocationLocationLevel1Value", "1");
    context.setVariable("ownerLegalPersonEmail", "test@ewew");
    context.setVariable("ownerPersonPDocumentCode", "008001001000");
    context.setVariable("agentTemporaryLocationCityName", "Калуга");
    context.setVariable("agentEmail", "reeer@erere.com");
    context.setVariable("freePaymentADocumentOriginalQuantity_1", 1L);
    context.setVariable("agentTemporaryLocationLocationLevel1Type", "д");
    context.setVariable("freePaymentADocumentName_1", "справка");
    context.setVariable("agentPDocumentNumber", "121212");
    context.setVariable("agentTemporaryLocationDistrictName", "");
    context.setVariable("agentPDocumentIssueOrgan", "УВД");
    context.setVariable("declLegalPersonDocumentCode", "008002000000");
    context.setVariable("declarantType", "GOVERNANCE");
   // context.setVariable("ownerGovernanceContactInfo", "12342323");
    context.setVariable("request_interval_end", TestUtils.getDateValue("2013-07-23"));
    //context.setVariable("declLegalPersonOPF", "809000000039");
    context.setVariable("objectOtherKind", null);
    context.setVariable("ownerPersonPDocumentDate", null);
    context.setVariable("ownerTemporaryLocationLocationNote", "описание адреа");
    context.setVariable("buildingKind", null);
    context.setVariable("agentFIOSurname", "Антонов");
    context.setVariable("ownerPersonPDocumentSeries", "1111");
    context.setVariable("ownerPersonPDocumentNumber", "123123");
    context.setVariable("ownerType", "GOVERNANCE");
    context.setVariable("ownerTemporaryLocationStreetName", "Додож");
    context.setVariable("objectRoomKind", "FLAT");
    context.setVariable("ownerTemporaryLocationLocationLevel2Value", "");
    context.setVariable("ownerTemporaryLocationSovietVillageName", "");
    context.setVariable("ownerTemporaryLocationLocationApartmentValue", "1");
    context.setVariable("requestType", "558102100000");
    context.setVariable("Physical", "Физические лица");
    context.setVariable("agentTemporaryLocationLocationLevel3Type", null);
    context.setVariable("agentKind", "356002000000");
    context.setVariable("applied", 1L);
    context.setVariable("ownerTemporaryLocationUrbanDistictName", "");
    context.setVariable("agentTemporaryLocationSovietVillageType", null);
    context.setVariable("ownerPersonFIOFirst", "Иван");
    context.setVariable("declPersonFIOSurname", "Иванов");
    context.setVariable("declPersonFIOPatronymic", "Иванович");
    context.setVariable("ownerTemporaryLocationLocationLevel3Type", null);
    context.setVariable("appliedADocumentDate_1", TestUtils.getDateValue("2012-07-24")); // string
    context.setVariable("appliedADocumentOriginalQuantity_1", 1L); // string
    context.setVariable("appliedADocumentOriginalQuantitySheet_1", 1L); // string

    context.setVariable("appliedADocumentName_1",
                        "Запрос о предоставлении сведений, содержащихся в Едином государственном реестре прав на недвижимое имущество и сделок с ним");
    context.setVariable("appliedADocumentCode_1", "558102100000"); // string
    context.setVariable("appliedADocumentNumber_1", "26-0-1-21/4001/2011-166"); // string

  }
}
