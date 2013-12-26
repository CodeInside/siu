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
import ru.codeinside.gws3564c.enclosure.grp.DocumentContentEnclosureBuilder;
import ru.codeinside.gws3564c.enclosure.grp.SubjectRightsHistoryEnclosureBuilder;
import ru.codeinside.gws3564c.enclosure.grp.TestUtils;

import java.util.Date;

public class DocumentContentBuilderFillFromUITest {

  private ExchangeContext context;
  private DocumentContentEnclosureBuilder enclosureBuilder;

  @Before
  public void setUp() throws Exception {
    context = new DummyContext();
    enclosureBuilder = new DocumentContentEnclosureBuilder(context);
  }

  @Test
  public void testWeCanReproduceSampleRequest() throws Exception {
    fillContext();

    String guid = "7b983700-651d-43d8-bdb4-26dd683df535";
    String xml = enclosureBuilder.createEnclosure(guid);
    System.out.println(xml);
  }


  private void fillContext() {
    context.setVariable("declLegalPersonDocumentSeries", "1231");
    context.setVariable("objectLocationLocationLevel1Type_1", "д");
    context.setVariable("agentTemporaryLocationLocationOther", "");
    context.setVariable("smevInitialRegDate", TestUtils.getDateValue("2012-08-02"));
    context.setVariable("freePayment", "1");
    context.setVariable("objectLocationStreetName_1", "Южная");
    context.setVariable("declLegalPersonDocumentName", "свидетельство");
    context.setVariable("declPersonSNILS", "112-233-332 33");
    context.setVariable("agentTemporaryLocationLocalityType", null);
    context.setVariable("objectLocationRegion_1", "40");
    context.setVariable("objects", "1");
    context.setVariable("objectLocationSovietVillageName_1", "");
    context.setVariable("agentTemporaryLocationLocalityName", "");
    context.setVariable("freePaymentADocumentNumber_1", "1");
    context.setVariable("objectLocationLocationLevel1Value_1", "1");
    context.setVariable("appliedADocumentNumber_1", "12212212");
    context.setVariable("objectLocationLocationLevel2Type_1", null);
    context.setVariable("objectLocationCLADR_1", "");
    context.setVariable("Legal", "Юридические лица");
    context.setVariable("declLegalPersonPhone", "12121212");
    context.setVariable("agentTemporaryLocationPostalCode", "");
    context.setVariable("okato", "60000000000");
    context.setVariable("declLegalPersonDocumentNumber", "212122");
    context.setVariable("declLegalPersonContactInfo", "212121212");
    context.setVariable("declPersonEmail", "test@test.com");
    context.setVariable("declLegalPersonRegDate", null);
    context.setVariable("declPersonFIOFirst", "Иван");
    context.setVariable("objectLocationLocationLevel3Value_1", "");
    context.setVariable("objectLocationDCity_1", "г");
    context.setVariable("freePaymentADocumentOriginalQuantitySheet_1", "1");
    context.setVariable("isPaymentFree", true);
    context.setVariable("procedureId", "251");
    context.setVariable("declPersonSex", "M");
    context.setVariable("appliedADocumentIssue_1", "УВД");
    context.setVariable("agentTemporaryLocationRegion", "40");
    context.setVariable("declPersonPDocumentSeries", "1222");
    context.setVariable("freePaymentADocumentDate_1", TestUtils.getDateValue("2012-08-02"));
    context.setVariable("declPersonPDocumentIssueOrgan", "УВД");
    context.setVariable("agentTemporaryLocationLocationApartmentValue", "1");
    context.setVariable("agentTemporaryLocationSovietVillageName", "");
    context.setVariable("agentTemporaryLocationUrbanDistictType", null);
    context.setVariable("agentPhone", "123121");
    context.setVariable("agentFIOFirst", "Антон");
    context.setVariable("objectLocationUrbanDistictType_1", null);
    context.setVariable("declPersonPhone", "12212212221");
    context.setVariable("objectCadastralNumber_1", "122:212:2121212:1212212");
    context.setVariable("payment", "0");
    context.setVariable("agentTemporaryLocationLocationApartmentType", "кв");
    context.setVariable("objectAreaUnit_1", "055");
    context.setVariable("agentTemporaryLocationLocationLevel2Type", null);
    context.setVariable("smevTest", "Первичный запрос");
    context.setVariable("agentSNILS", "112-233-332 33");
    context.setVariable("Individuals", "Индивидуальные предпринематели");
    context.setVariable("declPersonMarriageStatus", "женат");
    context.setVariable("procedureTypeName", "0");
    context.setVariable("objectLocationOKATO_1", null);
    context.setVariable("agentPDocumentSeries", "1221");
    context.setVariable("declKind", "357007000000");
    context.setVariable("objectRoomKind_1", "ROOM");
    context.setVariable("declGovernancePhone", "12342323");
    context.setVariable("enclosure_request_type", "CONTENT_DOCUMENT_EXTRACT");
    context.setVariable("agentPDocumentDate", TestUtils.getDateValue("2012-08-02"));
    context.setVariable("declLegalPersonEmail", "tesst@rerer.com");
    context.setVariable("appliedADocumentOriginalQuantity_1", "1");
    context.setVariable("declLegalPersonDocumentDate", null);
    context.setVariable("objectAreaValue_1", "00000000012.00");
    context.setVariable("declLegalPersonINN", "12343434");
    context.setVariable("smevInitialRegNumber", "137542138571477400");
    context.setVariable("objectLocationLocationNote_1", "");
    context.setVariable("declPersonPDocumentCode", "008001001000");
    context.setVariable("declPersonPDocumentNumber", "121212");
    context.setVariable("agentTemporaryLocationLocationNote", "");
    context.setVariable("agentTemporaryLocationOKATO", null);
    context.setVariable("agentTemporaryLocationCLADR", "");
    context.setVariable("freePaymentADocumentDesc_1", "нет");
    context.setVariable("freePaymentADocumentIssue_1", "УВД");
    context.setVariable("declLegalPersonDocumentIssueOrgan", "Налоговая");
    context.setVariable("appliedADocumentCode_1", "008001001000");
    context.setVariable("agentTemporaryLocationLocationLevel3Value", "");
    context.setVariable("declGovernanceContactInfo", "");
    context.setVariable("agentTemporaryLocationDCity", "г");
    context.setVariable("freePaymentADocumentCode_1", "008001000000");
    context.setVariable("objectLocationLocationApartmentValue_1", "1");
    context.setVariable("agentTemporaryLocationDStreets", "ул");
    context.setVariable("objectKind_1", "ROOM");
    context.setVariable("parcels", null);
    context.setVariable("declGovernanceCode", "007001001001");
    context.setVariable("agentTemporaryLocationUrbanDistictName", "");
    context.setVariable("objectLocationLocationLevel3Type_1", null);
    context.setVariable("objectLocationCityName_1", "Калуга");
    context.setVariable("agentFIOPatronymic", "Антонович");
    context.setVariable("objectLocationDStreets_1", "ул");
    context.setVariable("declGovernanceName", "Пенсионный фонд");
    context.setVariable("serviceId", "3");
    context.setVariable("declPersonPDocumentDate", null);
    context.setVariable("objectLocationLocationApartmentType_1", "кв");
    context.setVariable("agentTemporaryLocationStreetName", "Южная");
    context.setVariable("agentTemporaryLocationDistrictType", null);
    context.setVariable("objectLocationPostalCode_1", "");
    context.setVariable("appliedADocumentDate_1", TestUtils.getDateValue("2012-08-02") );
    context.setVariable("declarantLogin", "test");
    context.setVariable("objectLocationDistrictType_1", null);
    context.setVariable("agentTemporaryLocationLocationLevel2Value", "");
    context.setVariable("agentPDocumentCode", "008001001000");
    context.setVariable("objectLocationDistrictName_1", "");
    context.setVariable("oktmo", "01234567");
    context.setVariable("declLegalPersonName", "Точка");
    context.setVariable("objectLocationLocationOther_1", "");
    context.setVariable("declLegalPersonOGRN", "1212122");
    context.setVariable("objectLocationUrbanDistictName_1", "");
    context.setVariable("objectLocationSovietVillageType_1", null);
    context.setVariable("agentTemporaryLocationLocationLevel1Value", "1");
    context.setVariable("objectLocationLocalityType_1", null);
    context.setVariable("objectOtherKind_1", null);
    context.setVariable("agentTemporaryLocationCityName", "Калуга");
    context.setVariable("objectLocationLocationLevel2Value_1", "");
    context.setVariable("agentEmail", "reeer@erere.com");
    context.setVariable("freePaymentADocumentOriginalQuantity_1", "1");
    context.setVariable("agentTemporaryLocationLocationLevel1Type", "д");
    context.setVariable("freePaymentADocumentName_1", "справка");
    context.setVariable("agentPDocumentNumber", "121212");
    context.setVariable("agentTemporaryLocationDistrictName", "");
    context.setVariable("agentPDocumentIssueOrgan", "УВД");
    context.setVariable("declLegalPersonDocumentCode", "008002000000");
    context.setVariable("declarantType", "GOVERNANCE");
    context.setVariable("appliedADocumentDesc_1", "нет");
    context.setVariable("agentFIOSurname", "Антонов");
    context.setVariable("requestType", "558102100000");
    context.setVariable("objectLocationLocalityName_1", "");
    context.setVariable("Physical", "Физические лица");
    context.setVariable("agentTemporaryLocationLocationLevel3Type", null);
    context.setVariable("agentKind", "356002000000");
    context.setVariable("appliedADocumentOriginalQuantitySheet_1", "1");
    context.setVariable("buildingKind_1", null);
    context.setVariable("applied", "1");
    context.setVariable("appliedADocumentName_1", "Паспорт гражданина РФ");
    context.setVariable("agentTemporaryLocationSovietVillageType", null);
    context.setVariable("declPersonFIOSurname", "Иванов");
    context.setVariable("declPersonFIOPatronymic", "Иванович");
    context.setVariable("contentDocumentPDocumentCode", "008002099000");
    context.setVariable("contentDocumentPDocumentSeries", "1111");
    context.setVariable("contentDocumentPDocumentNumber", "1111111");
    context.setVariable("contentDocumentPDocumentDate", new Date());
    context.setVariable("contentDocumentPDocumentIssueOrgan", "налоговая");
    context.setVariable("contentDocumentPDocumentName", "наименование документа");


  }
}
