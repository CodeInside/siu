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
import ru.grp.*;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static ru.grp.RequestGRP.Request.RequiredData.RequiredDataIncapacity.IncapacityOwner;

public class ExtractDataAboutIncapacityOwnerBuilderTest {
  private ExchangeContext context;
  private ExtractDataAboutIncapacityOwnerBuilder enclosureBuilder;

  @Before
  public void setUp() throws Exception {
    context = new DummyContext();
    enclosureBuilder = new ExtractDataAboutIncapacityOwnerBuilder(context);
  }


  @Test
  public void testFillPersonSingleOwner() throws Exception {
    fillContext();
    context.setVariable("incapacityOwner" , 1L);
    RequestGRP request = enclosureBuilder.createRequest( "id");
    List<IncapacityOwner> owners = request.getRequest()
                                 .getRequiredData()
                                 .getRequiredDataIncapacity()
                                 .getIncapacityOwner();
    assertNotNull(owners);
    assertEquals(1, owners.size());
    final TPersonOwner personOwner = owners.get(0);
    assertEquals("Иванов", personOwner.getFIO().getSurname());
    assertEquals("Иван", personOwner.getFIO().getFirst());
    assertEquals("Иванович", personOwner.getFIO().getPatronymic());
    assertEquals(TestUtils.getDateValue("2012-07-12"), personOwner.getDateBirth().toGregorianCalendar().getTime());
    assertEquals("birthLocation", personOwner.getPlaceBirth());
    assertEquals("112-233-332 33", personOwner.getSNILS());
    assertEquals("contactInfo", personOwner.getContactInfo());
    assertEquals("citizenShip", personOwner.getCitizenship());
    assertEquals("familyStatus", personOwner.getFamilyStatus());
    assertEquals("Phone", personOwner.getPhone());
    assertEquals("E-mail", personOwner.getEMail());
    assertNotNull(personOwner.getLocationTemporary());
    assertNotNull(personOwner.getDocument());
  }

  @Test
  public void testFillSeveralIncapacityOwners() throws Exception {
    fillContext();
    context.setVariable("incapacityOwner" , 2L);
    RequestGRP request = enclosureBuilder.createRequest( "id");
    List<IncapacityOwner> owners = request.getRequest()
                                          .getRequiredData()
                                          .getRequiredDataIncapacity()
                                          .getIncapacityOwner();
    assertEquals(2, owners.size());
    TPersonOwner personOwner = owners.get(0);
    assertEquals("Иванов", personOwner.getFIO().getSurname());
    personOwner = owners.get(1);
    assertEquals("Иванов", personOwner.getFIO().getSurname());
  }

  private void fillContext() {
    //owner _ 1
    context.setVariable("ownerPersonFIOSurname_1", "Иванов");
    context.setVariable("ownerPersonFIOFirst_1", "Иван");
    context.setVariable("ownerPersonFIOPatronymic_1", "Иванович");
    context.setVariable("ownerPersonDateBirth_1", TestUtils.getDateValue("2012-07-12"));
    context.setVariable("ownerPersonPlaceBirth_1", "birthLocation");
    context.setVariable("ownerPersonEmail_1", "E-mail");
    context.setVariable("ownerPersonPhone_1", "Phone");
    context.setVariable("ownerPersonCitizenShip_1", "citizenShip");
    context.setVariable("ownerPersonFamilyStatus_1", "familyStatus");


    context.setVariable("ownerPersonSNILS_1", "112-233-332 33");
    context.setVariable("ownerPersonContactInfo_1", "contactInfo");

    context.setVariable("ownerTemporaryLocationOKATO_1", "ownerTemporaryLocationOKATO"); //тип string
    context.setVariable("ownerTemporaryLocationCLADR_1", "ownerTemporaryLocationCLADR"); //тип string
    context.setVariable("ownerTemporaryLocationPostalCode_1", "ownerTemporaryLocationPostalCode"); //тип string
    context.setVariable("ownerTemporaryLocationRegion_1", "ownerTemporaryLocationRegion"); //тип string
    context.setVariable("ownerTemporaryLocationDistrictName_1", "ownerTemporaryLocationDistrictName"); //тип string
    context.setVariable("ownerTemporaryLocationDistrictType_1", "р-н"); //тип enum

    context.setVariable("ownerTemporaryLocationCityName_1", "ownerTemporaryLocationCityName"); //тип string
    context.setVariable("ownerTemporaryLocationDCity_1", "г"); //тип string
    context.setVariable("ownerTemporaryLocationUrbanDistictName_1",
                        "ownerTemporaryLocationUrbanDistictName"); //тип string
    context.setVariable("ownerTemporaryLocationUrbanDistictType_1", "р-н"); //тип string

    context.setVariable("ownerTemporaryLocationSovietVillageName_1",
                        "ownerTemporaryLocationSovietVillageName"); //тип string
    context.setVariable("ownerTemporaryLocationSovietVillageType_1", "волость"); //тип string

    context.setVariable("ownerTemporaryLocationLocalityName_1", "ownerTemporaryLocationLocalityName"); //тип string
    context.setVariable("ownerTemporaryLocationLocalityType_1", "аал"); //тип string

    context.setVariable("ownerTemporaryLocationStreetName_1", "ownerTemporaryLocationStreetName"); //тип string
    context.setVariable("ownerTemporaryLocationDStreets_1", "аллея"); //тип enum

    context.setVariable("ownerTemporaryLocationLocationLevel1Type_1", "д"); //тип enum

    context.setVariable("ownerTemporaryLocationLocationLevel1Value_1",
                        "ownerTemporaryLocationLocationLevel1Value"); //тип string
    context.setVariable("ownerTemporaryLocationLocationLevel2Type_1", "корп"); //тип enum

    context.setVariable("ownerTemporaryLocationLocationLevel2Value_1",
                        "ownerTemporaryLocationLocationLevel2Value"); //тип string
    context.setVariable("ownerTemporaryLocationLocationLevel3Type_1", "блок"); //тип enum

    context.setVariable("ownerTemporaryLocationLocationLevel3Value_1",
                        "ownerTemporaryLocationLocationLevel3Value"); //тип string
    context.setVariable("ownerTemporaryLocationLocationApartmentType_1", "кв"); //тип enum

    context.setVariable("ownerTemporaryLocationLocationApartmentValue_1",
                        "ownerTemporaryLocationLocationApartmentValue"); //тип string
    context.setVariable("ownerTemporaryLocationLocationOther_1", "ownerTemporaryLocationLocationOther"); //тип string
    context.setVariable("ownerTemporaryLocationLocationNote_1", "ownerTemporaryLocationLocationNote"); //тип string

    context.setVariable("ownerPDocumentCode_1", "ownerPDocumentCode"); //тип enum

    context.setVariable("ownerPDocumentSeries_1", "ownerPDocumentSeries"); //тип string
    context.setVariable("ownerPDocumentNumber_1", "ownerPDocumentNumber"); //тип string
    context.setVariable("ownerPDocumentDate_1", TestUtils.getDateValue("2001-11-01")); //тип date
    context.setVariable("ownerPDocumentIssueOrgan_1", "ownerPDocumentIssueOrgan"); //тип string
    context.setVariable("ownerPDocumentDesc_1", "ownerPDocumentDesc"); //тип string

    context.setVariable("ownerPersonFIOSurname_2", "Иванов");
    context.setVariable("ownerPersonFIOFirst_2", "Иван");
    context.setVariable("ownerPersonFIOPatronymic_2", "Иванович");
    context.setVariable("ownerPersonDateBirth_2", TestUtils.getDateValue("2012-07-12"));
    context.setVariable("ownerPersonPlaceBirth_2", "birthLocation");
    context.setVariable("ownerPersonEmail_2", "E-mail");
    context.setVariable("ownerPersonPhone_2", "Phone");
    context.setVariable("ownerPersonCitizenShip_2", "citizenShip");
    context.setVariable("ownerPersonFamilyStatus_2", "familyStatus");


    context.setVariable("ownerPersonSNILS_2", "112-233-332 33");
    context.setVariable("ownerPersonContactInfo_2", "contactInfo");

    context.setVariable("ownerTemporaryLocationOKATO_2", "ownerTemporaryLocationOKATO"); //тип string
    context.setVariable("ownerTemporaryLocationCLADR_2", "ownerTemporaryLocationCLADR"); //тип string
    context.setVariable("ownerTemporaryLocationPostalCode_2", "ownerTemporaryLocationPostalCode"); //тип string
    context.setVariable("ownerTemporaryLocationRegion_2", "ownerTemporaryLocationRegion"); //тип string
    context.setVariable("ownerTemporaryLocationDistrictName_2", "ownerTemporaryLocationDistrictName"); //тип string
    context.setVariable("ownerTemporaryLocationDistrictType_2", "р-н"); //тип enum

    context.setVariable("ownerTemporaryLocationCityName_2", "ownerTemporaryLocationCityName"); //тип string
    context.setVariable("ownerTemporaryLocationDCity_2", "г"); //тип string
    context.setVariable("ownerTemporaryLocationUrbanDistictName_2",
                        "ownerTemporaryLocationUrbanDistictName"); //тип string
    context.setVariable("ownerTemporaryLocationUrbanDistictType_2", "р-н"); //тип string

    context.setVariable("ownerTemporaryLocationSovietVillageName_2",
                        "ownerTemporaryLocationSovietVillageName"); //тип string
    context.setVariable("ownerTemporaryLocationSovietVillageType_2", "волость"); //тип string

    context.setVariable("ownerTemporaryLocationLocalityName_2", "ownerTemporaryLocationLocalityName"); //тип string
    context.setVariable("ownerTemporaryLocationLocalityType_2", "аал"); //тип string

    context.setVariable("ownerTemporaryLocationStreetName_2", "ownerTemporaryLocationStreetName"); //тип string
    context.setVariable("ownerTemporaryLocationDStreets_2", "аллея"); //тип enum

    context.setVariable("ownerTemporaryLocationLocationLevel1Type_2", "д"); //тип enum

    context.setVariable("ownerTemporaryLocationLocationLevel1Value_2",
                        "ownerTemporaryLocationLocationLevel1Value"); //тип string
    context.setVariable("ownerTemporaryLocationLocationLevel2Type_2", "корп"); //тип enum

    context.setVariable("ownerTemporaryLocationLocationLevel2Value_2",
                        "ownerTemporaryLocationLocationLevel2Value"); //тип string
    context.setVariable("ownerTemporaryLocationLocationLevel3Type_2", "блок"); //тип enum

    context.setVariable("ownerTemporaryLocationLocationLevel3Value_2",
                        "ownerTemporaryLocationLocationLevel3Value"); //тип string
    context.setVariable("ownerTemporaryLocationLocationApartmentType_2", "кв"); //тип enum

    context.setVariable("ownerTemporaryLocationLocationApartmentValue_2",
                        "ownerTemporaryLocationLocationApartmentValue"); //тип string
    context.setVariable("ownerTemporaryLocationLocationOther_2", "ownerTemporaryLocationLocationOther"); //тип string
    context.setVariable("ownerTemporaryLocationLocationNote_2", "ownerTemporaryLocationLocationNote"); //тип string

    context.setVariable("ownerPDocumentCode_2", "ownerPDocumentCode"); //тип enum

    context.setVariable("ownerPDocumentSeries_2", "ownerPDocumentSeries"); //тип string
    context.setVariable("ownerPDocumentNumber_2", "ownerPDocumentNumber"); //тип string
    context.setVariable("ownerPDocumentDate_2", TestUtils.getDateValue("2001-11-01")); //тип date
    context.setVariable("ownerPDocumentIssueOrgan_2", "ownerPDocumentIssueOrgan"); //тип string
    context.setVariable("ownerPDocumentDesc_2", "ownerPDocumentDesc"); //тип string
    //Territory
    context.setVariable("territoryRegion", "40");     //req Attr


    //declarant
    context.setVariable("declarantType", "GOVERNANCE");
    context.setVariable("declKind", "357007000000");      //Required
    context.setVariable("declGovernanceName", "Пенсионный фонд");
    context.setVariable("declGovernanceCode", "007001001001");
    context.setVariable("declGovernanceEmail", "test@test.ru");

    //AppliedDocument
    context.setVariable("applied", 1l);
    context.setVariable("appliedADocumentCode_1", "558102100000");
    context.setVariable("appliedADocumentName_1",
                        "Запрос о предоставлении сведений, содержащихся в Едином государственном реестре прав на недвижимое имущество и сделок с ним");
    context.setVariable("appliedADocumentNumber_1", "26-0-1-21/4001/2011-166");
    context.setVariable("appliedADocumentDate_1", TestUtils.getDateValue("2012-07-24"));
    context.setVariable("appliedADocumentOriginalQuantity_1", 1L);
    context.setVariable("appliedADocumentOriginalQuantitySheet_1", 1L);
    context.setVariable("appliedADocumentCopyQuantity_1", 1L);
    context.setVariable("appliedADocumentCopyQuantitySheet_1", 1L);
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
  }


  @Test
  public void testRealtyRightsByOwner() throws Exception {
    createContextForGetIncapacityOwner();
    testCreateEnclosure("/enclosure/grp/incapacity_owner.xml", "7b983700-651d-43d8-bdb4-26dd683df535");
  }


  private void createContextForGetIncapacityOwner() throws ParseException {
    context.setVariable("incapacityOwner" , 1L);
    context.setVariable("smevTest", "Первичный запрос");
    context.setVariable("okato", "60000000000"); // string
    context.setVariable("oktmo", "01234567"); // string
    context.setVariable("requestType", "558102100000"); // string
    context.setVariable("enclosure_request_type", "SUBJECT_RIGHTS"); // string
    context.setVariable("declarantType", "GOVERNANCE"); // string
    context.setVariable("ownerPersonFIOSurname_1", "Иванов");
    context.setVariable("ownerPersonFIOFirst_1", "Иван");
    context.setVariable("ownerPersonFIOPatronymic_1", "Иваныч");
    context.setVariable("ownerPersonSNILS_1", "112-233-332 33");
    context.setVariable("ownerPersonDateBirth_1", TestUtils.getDateValue("2012-07-12"));

    context.setVariable("ownerPersonPDocumentCode_1", "008001001000"); //тип string
    context.setVariable("ownerPersonPDocumentSeries_1", "1111"); //тип string
    context.setVariable("ownerPersonPDocumentNumber_1", "222222"); //тип string
    context.setVariable("ownerPersonPDocumentDate_1", TestUtils.getDateValue("2001-01-01")); //тип date
    context.setVariable("ownerPersonPDocumentIssueOrgan_1", "ОВД Калуга, 2321"); //тип string
    context.setVariable("ownerTemporaryLocationRegion_1", "40"); //тип string
    context.setVariable("ownerTemporaryLocationCityName_1", "Калуга"); //тип string
    context.setVariable("ownerTemporaryLocationDCity_1", "г"); //тип string
    context.setVariable("ownerTemporaryLocationStreetName_1", "Ленина"); //тип string
    context.setVariable("ownerTemporaryLocationDStreets_1", "уч-к"); //тип enum
    context.setVariable("ownerTemporaryLocationLocationLevel1Type_1", "д"); //тип enum
    context.setVariable("ownerTemporaryLocationLocationLevel1Value_1", "32"); //тип string
    context.setVariable("ownerTemporaryLocationLocationLevel2Type_1", "корп"); //тип enum
    context.setVariable("ownerTemporaryLocationLocationLevel2Value_1", "2"); //тип string
    context.setVariable("ownerTemporaryLocationLocationLevel3Type_1", "литера"); //тип enum

    context.setVariable("ownerTemporaryLocationLocationLevel3Value_1", "а"); //тип string
    context.setVariable("ownerTemporaryLocationLocationApartmentType_1", "кв"); //тип enum

    context.setVariable("ownerTemporaryLocationLocationApartmentValue_1", "32"); //тип string
    context.setVariable("ownerTemporaryLocationLocationOther_1", "Иное"); //тип string
    context.setVariable("ownerTemporaryLocationLocationNote_1", "Неформализованное описнаие");



    context.setVariable("applied", 1l);
    context.setVariable("territory", 2L);
    context.setVariable("territory_1", "01");
    context.setVariable("territory_2", "02");

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
  }

  private void testCreateEnclosure(String fileWithExpectedEnclosure, String guid) throws IOException {
    String xml = enclosureBuilder.createEnclosure( guid);
    InputStream is = this.getClass().getResourceAsStream(fileWithExpectedEnclosure);
    String expectedContent = IOUtils.toString(is, "UTF-8");
    Assert.assertEquals(expectedContent, xml);
  }
}
