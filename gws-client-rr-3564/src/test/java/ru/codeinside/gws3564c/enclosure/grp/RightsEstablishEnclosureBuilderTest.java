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

public class RightsEstablishEnclosureBuilderTest {
  private ExchangeContext context;
  private RightsEstablishEnclosureBuilder enclosureBuilder;

  @Before
  public void setUp() throws Exception {
    context = new DummyContext();
    enclosureBuilder = new RightsEstablishEnclosureBuilder(context);
  }

  @Test
  public void testWeCanReproduceSampleRequest() throws Exception {
    fillContext();
    context.setVariable("objects", 1l);
    context.setVariable("objectCadastralNumber_1", "");
    context.setVariable("objectAreaValue_1", "100.00");
    context.setVariable("objectAreaUnit_1", "055");
    context.setVariable("objectLocationOKATO_1", "");
    context.setVariable("objectKind_1", "ROOM");
    context.setVariable("objectRoomKind_1", "NONDOMESTIC");
    testCreateEnclosure("/enclosure/grp/right_establish.xml", "beb8a145-c0a2-4f2e-b318-381a3cc4049e");
  }

  private void testCreateEnclosure(String fileWithExpectedEnclosure, String guid) throws IOException {
    String xml = enclosureBuilder.createEnclosure( guid);
    InputStream is = this.getClass().getResourceAsStream(fileWithExpectedEnclosure);
    String expectedContent = IOUtils.toString(is);
    Assert.assertEquals(expectedContent, xml);
  }

  private void fillContext() {
    //declarant
    context.setVariable("declarantType", "GOVERNANCE");
    context.setVariable("declKind", "357007000000");      //Required
    context.setVariable("declGovernanceName", "Пенсионный фонд");
    context.setVariable("declGovernanceCode", "007001001001");
    context.setVariable("declGovernanceEmail", "test@test");

    //AppliedDocument
    context.setVariable("applied", 1l);
    context.setVariable("appliedADocumentCode_1", "558102100000");
    context.setVariable("appliedADocumentName_1",
                        "Запрос о предоставлении сведений, содержащихся в Едином государственном реестре прав на недвижимое имущество и сделок с ним");
    context.setVariable("appliedADocumentNumber_1", "26-0-1-21/4001/2011-167");
    context.setVariable("appliedADocumentDate_1", TestUtils.getDateValue("2012-07-24"));
    context.setVariable("appliedADocumentOriginalQuantity_1", 1L);
    context.setVariable("appliedADocumentOriginalQuantitySheet_1", 1L);
    context.setVariable("appliedADocumentCopyQuantity_1", 0L);
    context.setVariable("appliedADocumentCopyQuantitySheet_1", 0L);
    //ReasonFreeDocument
    context.setVariable("freeACodeDocument", "555005000000");
    context.setVariable("freeADocumentName",
                        "Запрос о предоставлении сведений, содержащихся в Едином государственном реестре прав на недвижимое имущество и сделок с ним");
    context.setVariable("freeADocumentNumber", "");
    context.setVariable("freeADocumentDate", TestUtils.getDateValue("2012-07-24"));
    context.setVariable("freeADocumentOriginalQuantity", "1");
    context.setVariable("freeADocumentOriginalQuantitySheet", "1");
    context.setVariable("isPaymentFree", false);
    context.setVariable("freePayment", 1L);
    context.setVariable("payment", 0L);
    context.setVariable("territory", 0L);
    context.setVariable("smevTest", "Первичный запрос");

    // object location
    context.setVariable("objectLocationOKATO_1", ""); //тип string
    context.setVariable("objectLocationCLADR_1", ""); //тип string
    context.setVariable("objectLocationPostalCode_1", ""); //тип string
    context.setVariable("objectLocationRegion_1", "40"); //тип string
    context.setVariable("objectLocationDistrictName_1", ""); //тип string
    context.setVariable("objectLocationDistrictType_1", "р-н"); //тип enum

    context.setVariable("objectLocationCityName_1", "Калуга"); //тип string
    context.setVariable("objectLocationDCity_1", "г"); //тип string
    context.setVariable("objectLocationUrbanDistictName_1", ""); //тип string
    context.setVariable("objectLocationUrbanDistictType_1", ""); //тип string

    context.setVariable("objectLocationSovietVillageName_1", ""); //тип string
    context.setVariable("objectLocationSovietVillageType_1", ""); //тип string

    context.setVariable("objectLocationLocalityName_1", ""); //тип string
    context.setVariable("objectLocationLocalityType_1", ""); //тип string

    context.setVariable("objectLocationStreetName_1", "Зеленая"); //тип string
    context.setVariable("objectLocationDStreets_1", "ул"); //тип enum

    context.setVariable("objectLocationLocationLevel1Type_1", "д"); //тип enum

    context.setVariable("objectLocationLocationLevel1Value_1", "23"); //тип string

    context.setVariable("objectLocationLocationLevel2Type_1", ""); //тип enum
    context.setVariable("objectLocationLocationLevel2Value_1", ""); //тип string

    context.setVariable("objectLocationLocationLevel3Type_1", ""); //тип enum
    context.setVariable("objectLocationLocationLevel3Value_1", ""); //тип string
    context.setVariable("objectLocationLocationApartmentType_1", "кв"); //тип enum

    context.setVariable("objectLocationLocationApartmentValue_1", "2"); //тип string

    context.setVariable("objectLocationLocationOther_1", ""); //тип string
    context.setVariable("objectLocationLocationNote_1", ""); //тип string

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
    context.setVariable("agentTemporaryLocationPostalCode", ""); //тип string
    context.setVariable("agentTemporaryLocationCityName", "Калуга"); //тип string
    context.setVariable("agentTemporaryLocationDCity", "г"); //тип string
    context.setVariable("agentTemporaryLocationStreetName", "Коммунистическая"); //тип string
    context.setVariable("agentTemporaryLocationDStreets", "ул"); //тип enum
    context.setVariable("agentTemporaryLocationLocationLevel1Type", "д"); //тип enum
    context.setVariable("agentTemporaryLocationLocationLevel1Value", "23"); //тип string
    context.setVariable("agentTemporaryLocationLocationLevel2Type", "корп"); //тип enum
    context.setVariable("agentTemporaryLocationLocationLevel2Value", "2"); //тип string
    context.setVariable("agentTemporaryLocationLocationLevel3Type", "литера"); //тип enum

    context.setVariable("agentTemporaryLocationLocationLevel3Value", "в"); //тип string
    context.setVariable("agentTemporaryLocationLocationApartmentType", "к"); //тип enum

    context.setVariable("agentTemporaryLocationLocationApartmentValue", "233"); //тип string
    context.setVariable("agentTemporaryLocationLocationOther", "Иное"); //тип string
    context.setVariable("agentTemporaryLocationLocationNote", "Неформализованное описание");


    //ReasonFreeDocument
    context.setVariable("freePaymentADocumentCode_1", "555005000000");
    context.setVariable("freePaymentADocumentName_1",
                        "Документ, подтверждающий право заявителя на безвозмездное получение сведений");
    context.setVariable("freePaymentADocumentNumber_1", "");
    context.setVariable("freePaymentADocumentDate_1", TestUtils.getDateValue("2012-07-23"));
    context.setVariable("freePaymentADocumentOriginalQuantity_1", 1L);
    context.setVariable("freePaymentADocumentOriginalQuantitySheet_1", 1L);
    context.setVariable("isPaymentFree", true);
    context.setVariable("freePayment", 1L);
  }
}
