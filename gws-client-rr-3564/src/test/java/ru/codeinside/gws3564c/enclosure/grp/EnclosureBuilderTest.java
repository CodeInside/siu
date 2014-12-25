/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws3564c.enclosure.grp;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Before;
import org.junit.Test;
import ru.codeinside.gws.api.ExchangeContext;
import ru.codeinside.gws3564c.DummyContext;
import ru.grp.*;

import java.math.BigInteger;
import java.util.Date;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static ru.grp.RequestGRP.Request.Payment.PaymentDocuments;
import static ru.grp.RequestGRP.Request.Payment.ReasonFreeDocuments;


public class EnclosureBuilderTest {
  private Date currentDate;
  private ExchangeContext context;
  private EnclosureGRPBuilder enclosureGRPBuilder;

  @Before
  public void setUp() throws Exception {
    currentDate = DateUtils.parseDate("01.02.2001", new String[]{"dd.MM.yyyy"});
    context = new DummyContext();
    enclosureGRPBuilder = new SubjectRightsEnclosureBuilder(context);
  }

  @Test
  public void testFillLocation() throws Exception {
    EnclosureGRPBuilder enclosureGRPBuilder = new SubjectRightsEnclosureBuilder(context);
    context.setVariable("ownerTemporaryLocationOKATO", "ownerTemporaryLocationOKATO");
    context.setVariable("ownerTemporaryLocationCLADR", "ownerTemporaryLocationCLADR");
    context.setVariable("ownerTemporaryLocationPostalCode", "ownerTemporaryLocationPostalCode");
    context.setVariable("ownerTemporaryLocationRegion", "ownerTemporaryLocationRegion");
    context.setVariable("ownerTemporaryLocationDistrictName", "ownerTemporaryLocationDistrictName");
    context.setVariable("ownerTemporaryLocationDistrictType", "р-н");

    context.setVariable("ownerTemporaryLocationCityName", "ownerTemporaryLocationCityName");
    context.setVariable("ownerTemporaryLocationDCity", "г");
    context.setVariable("ownerTemporaryLocationUrbanDistictName", "ownerTemporaryLocationUrbanDistictName");
    context.setVariable("ownerTemporaryLocationUrbanDistictType", "р-н");

    context.setVariable("ownerTemporaryLocationSovietVillageName", "ownerTemporaryLocationSovietVillageName");
    context.setVariable("ownerTemporaryLocationSovietVillageType", "волость");

    context.setVariable("ownerTemporaryLocationLocalityName", "ownerTemporaryLocationLocalityName");
    context.setVariable("ownerTemporaryLocationLocalityType", "аал");

    context.setVariable("ownerTemporaryLocationStreetName", "ownerTemporaryLocationStreetName");
    context.setVariable("ownerTemporaryLocationDStreets", "аллея");

    context.setVariable("ownerTemporaryLocationLocationLevel1Type", "д");

    context.setVariable("ownerTemporaryLocationLocationLevel1Value", "ownerTemporaryLocationLocationLevel1Value");
    context.setVariable("ownerTemporaryLocationLocationLevel2Type", "корп");

    context.setVariable("ownerTemporaryLocationLocationLevel2Value", "ownerTemporaryLocationLocationLevel2Value");
    context.setVariable("ownerTemporaryLocationLocationLevel3Type", "блок");

    context.setVariable("ownerTemporaryLocationLocationLevel3Value", "ownerTemporaryLocationLocationLevel3Value");
    context.setVariable("ownerTemporaryLocationLocationApartmentType", "кв");

    context.setVariable("ownerTemporaryLocationLocationApartmentValue", "ownerTemporaryLocationLocationApartmentValue");
    context.setVariable("ownerTemporaryLocationLocationOther", "ownerTemporaryLocationLocationOther");
    context.setVariable("ownerTemporaryLocationLocationNote", "ownerTemporaryLocationLocationNote");
    PAddressV1 location = enclosureGRPBuilder.createLocation("ownerTemporaryLocation", "");
    assertNotNull(location);
    assertEquals("ownerTemporaryLocationCLADR", location.getCodeKLADR());
    assertEquals("ownerTemporaryLocationOKATO", location.getCodeOKATO());
    assertEquals("ownerTemporaryLocationPostalCode", location.getPostalCode());
    assertEquals("ownerTemporaryLocationRegion", location.getRegion());
    assertEquals("ownerTemporaryLocationDistrictName", location.getDistrict().getName());
    assertEquals("р-н", location.getDistrict().getType().value());
    assertEquals("ownerTemporaryLocationCityName", location.getCity().getName());
    assertEquals("г", location.getCity().getType().value());
    assertEquals("ownerTemporaryLocationUrbanDistictName", location.getUrbanDistrict().getName());
    assertEquals("р-н", location.getUrbanDistrict().getType().value());
    assertEquals("ownerTemporaryLocationSovietVillageName", location.getSovietVillage().getName());
    assertEquals("волость", location.getSovietVillage().getType().value());
    assertEquals("ownerTemporaryLocationLocalityName", location.getLocality().getName());
    assertEquals("аал", location.getLocality().getType().value());
    assertEquals("ownerTemporaryLocationStreetName", location.getStreet().getName());
    assertEquals("аллея", location.getStreet().getType().value());
    assertEquals("д", location.getLevel1().getType().value());
    assertEquals("ownerTemporaryLocationLocationLevel1Value", location.getLevel1().getValue());
    assertEquals("корп", location.getLevel2().getType().value());
    assertEquals("ownerTemporaryLocationLocationLevel2Value", location.getLevel2().getValue());
    assertEquals("блок", location.getLevel3().getType().value());
    assertEquals("ownerTemporaryLocationLocationLevel3Value", location.getLevel3().getValue());
    assertEquals("кв", location.getApartment().getType().value());
    assertEquals("ownerTemporaryLocationLocationApartmentValue", location.getApartment().getValue());
    assertEquals("ownerTemporaryLocationLocationOther", location.getOther());
    assertEquals("ownerTemporaryLocationLocationNote", location.getNote());
  }

  @Test
  public void testOwnerDocument() throws Exception {
    EnclosureGRPBuilder enclosureGRPBuilder = new SubjectRightsEnclosureBuilder(context);
    context.setVariable("ownerPDocumentCode", "ownerPDocumentCode");

    context.setVariable("ownerPDocumentSeries", "ownerPDocumentSeries");
    context.setVariable("ownerPDocumentNumber", "ownerPDocumentNumber");
    context.setVariable("ownerPDocumentDate", TestUtils.getDateValue("2001-11-01")); //тип date
    context.setVariable("ownerPDocumentIssueOrgan", "ownerPDocumentIssueOrgan");
    context.setVariable("ownerPDocumentDesc", "ownerPDocumentDesc");
    PIdentityPersonDocumentV1 document = enclosureGRPBuilder.formPDocument("owner", "");
    assertNotNull(document);
    assertEquals("ownerPDocumentSeries", document.getSeries());
    assertEquals("ownerPDocumentNumber", document.getNumber());
    assertEquals(TestUtils.getDateValue("2001-11-01"), document.getDate().toGregorianCalendar().getTime());
    assertEquals("ownerPDocumentIssueOrgan", document.getIssueOrgan());
    assertEquals("ownerPDocumentCode", document.getCodeDocument());
    assertEquals("ownerPDocumentDesc", document.getDesc());
  }

  @Test
  public void testGovernanceData() throws Exception {
    context.setVariable("declarantType", "GOVERNANCE");
    context.setVariable("declKind", "357007000000");      //Required
    context.setVariable("declGovernanceName", "Пенсионный фонд");
    context.setVariable("declGovernanceCode", "007001001001");
    context.setVariable("declGovernanceEmail", "test@test.ru");
    context.setVariable("declGovernancePhone", "phone");
    context.setVariable("declGovernanceContactInfo", "contactInfo");
    context.setVariable("declLocationLocationNote", "gov location note");
    context.setVariable("agentDocumentACodeDocument", "558102100000");
    context.setVariable("agentDocumentADocumentName",
                        "Запрос о предоставлении сведений, содержащихся в Едином государственном реестре прав на недвижимое имущество и сделок с ним");
    context.setVariable("agentDocumentADocumentNumber", "26-0-1-21/4001/2011-166");
    context.setVariable("agentDocumentADocumentDate", TestUtils.getDateValue("2012-07-24"));
    context.setVariable("agentDocumentADocumentOriginalQuantity", 1L);
    context.setVariable("agentDocumentADocumentOriginalQuantitySheet", 1L);
    context.setVariable("agentDocumentADocumentCopyQuantity", 1L);
    context.setVariable("agentDocumentADocumentCopyQuantitySheet", 1L);
    EnclosureGRPBuilder enclosureGRPBuilder = new SubjectRightsEnclosureBuilder(context);
    TDeclarant declarant = enclosureGRPBuilder.formDeclarant();
    TGovernanceDeclarant governance = declarant.getGovernance();
    assertNotNull(governance);
    assertEquals("007001001001", governance.getGovernanceCode());
    assertEquals("Пенсионный фонд", governance.getName());
    assertEquals("test@test.ru", governance.getEMail());
    assertEquals("phone", governance.getPhone());
    assertEquals("contactInfo", governance.getContactInfo());
    assertNotNull(governance.getAgent());
    PAddressV1 location = governance.getLocation();
    assertNotNull(location);
    assertEquals("gov location note", location.getNote());
    assertEquals("357007000000", declarant.getDeclarantKind());
  }

  @Test
  public void testFillAgent() throws Exception {
    context.setVariable("declarantType", "GOVERNANCE");
    context.setVariable("declKind", "357007000000");      //Required
    context.setVariable("declGovernanceName", "Пенсионный фонд");
    context.setVariable("declGovernanceCode", "007001001001");
    context.setVariable("declGovernanceEmail", "test@test.ru");
    context.setVariable("declGovernancePhone", "phone");
    context.setVariable("declGovernanceContactInfo", "contactInfo");
    context.setVariable("declLocationLocationNote", "gov location note");
    context.setVariable("agentKind", "agentKind");
    context.setVariable("agentEmail", "agentEmail");
    context.setVariable("agentPhone", "agentPhone");
    context.setVariable("agentContactInfo", "agentContactInfo");
    context.setVariable("agentIdDbEgrp", "IdDbEgrp");
    context.setVariable("agentFIOSurname", "Иванов");
    context.setVariable("agentFIOFirst", "Иван");
    context.setVariable("agentFIOPatronymic", "Иванович");
    context.setVariable("agentDateBirth", TestUtils.getDateValue("2012-07-12"));
    context.setVariable("agentPlaceBirth", "birthLocation");
    context.setVariable("agentCitizenShip", "citizenShip");
    context.setVariable("agentFamilyStatus", "familyStatus");
    context.setVariable("agentSNILS", "112-233-332 33");

    context.setVariable("agentDocumentADocumentCode", "558102100000");
    context.setVariable("agentDocumentADocumentDesc", "desc");
    context.setVariable("agentDocumentADocumentIssue", "issue");
    context.setVariable("agentDocumentADocumentName",
                        "Запрос о предоставлении сведений, содержащихся в Едином государственном реестре прав на недвижимое имущество и сделок с ним");
    context.setVariable("agentDocumentADocumentNumber", "26-0-1-21/4001/2011-166");
    context.setVariable("agentDocumentADocumentDate", TestUtils.getDateValue("2012-07-24"));
    context.setVariable("agentDocumentADocumentNumberReg", "numberReg");
    context.setVariable("agentDocumentADocumentRegister", "register");
    context.setVariable("agentDocumentADocumentSeries", "series");
    context.setVariable("agentDocumentADocumentOriginalQuantity", 1L);
    context.setVariable("agentDocumentADocumentOriginalQuantitySheet", 1L);
    context.setVariable("agentDocumentADocumentCopyQuantity", 1L);
    context.setVariable("agentDocumentADocumentCopyQuantitySheet", 1L);

    EnclosureGRPBuilder enclosureGRPBuilder = new SubjectRightsEnclosureBuilder(context);
    TDeclarant declarant = enclosureGRPBuilder.formDeclarant();
    TGovernanceDeclarant governance = declarant.getGovernance();
    TAgent agent = governance.getAgent();
    assertNotNull(agent);
    assertEquals("agentKind", agent.getAgentKind());
    assertEquals("IdDbEgrp", agent.getIdDbEgrp());
    assertEquals("agentEmail", agent.getEMail());
    assertEquals("agentPhone", agent.getPhone());
    assertEquals("agentContactInfo", agent.getContactInfo());
    assertNotNull(agent.getDocument());
    assertEquals("Иванов", agent.getFIO().getSurname());
    assertEquals("Иван", agent.getFIO().getFirst());
    assertEquals("Иванович", agent.getFIO().getPatronymic());
    assertEquals(TestUtils.getDateValue("2012-07-12"), agent.getDateBirth().toGregorianCalendar().getTime());
    assertEquals("birthLocation", agent.getPlaceBirth());
    assertEquals("112-233-332 33", agent.getSNILS());
    assertEquals("citizenShip", agent.getCitizenship());
    assertEquals("familyStatus", agent.getFamilyStatus());

    final TAppliedDocument appliedDocument = agent.getAttorneyDocument();
    assertNotNull(appliedDocument);
    assertEquals("Запрос о предоставлении сведений, содержащихся в Едином государственном реестре прав на недвижимое имущество и сделок с ним", appliedDocument.getName());
    assertEquals("558102100000", appliedDocument.getCodeDocument());
    assertEquals("issue", appliedDocument.getIssueOrgan());
    assertEquals("26-0-1-21/4001/2011-166", appliedDocument.getNumber());
    assertEquals("numberReg", appliedDocument.getNumberReg());
    assertEquals("register", appliedDocument.getRegister());
    assertEquals("series", appliedDocument.getSeries());
    assertEquals(1L, appliedDocument.getQuantity().getCopy().getQuantity().longValue());
    assertEquals(1l, appliedDocument.getQuantity().getCopy().getQuantitySheet().longValue());
    assertEquals(1l, appliedDocument.getQuantity().getOriginal().getQuantitySheet().longValue());
    assertEquals(1l, appliedDocument.getQuantity().getOriginal().getQuantitySheet().longValue());
    assertEquals("desc", appliedDocument.getDesc());
  }

  @Test
  public void testDeclarantPerson() throws Exception {
    context.setVariable("declarantType", "PERSON");
    context.setVariable("declPersonFIOSurname", "Иванов");
    context.setVariable("declPersonFIOFirst", "Иван");
    context.setVariable("declPersonFIOPatronymic", "Иванович");
    context.setVariable("agentKind", "agentKind");
    context.setVariable("agentDocumentACodeDocument", "558102100000");
    context.setVariable("agentDocumentADocumentName",
                        "Запрос о предоставлении сведений, содержащихся в Едином государственном реестре прав на недвижимое имущество и сделок с ним");
    context.setVariable("agentDocumentADocumentNumber", "26-0-1-21/4001/2011-166");
    context.setVariable("agentDocumentADocumentDate", TestUtils.getDateValue("2012-07-24"));
    context.setVariable("agentDocumentADocumentOriginalQuantity", 1L);
    context.setVariable("agentDocumentADocumentOriginalQuantitySheet", 1L);
    context.setVariable("agentDocumentADocumentCopyQuantity", 1L);
    context.setVariable("agentDocumentADocumentCopyQuantitySheet", 1L);
    EnclosureGRPBuilder enclosureGRPBuilder = new SubjectRightsEnclosureBuilder(context);
    TDeclarant declarant = enclosureGRPBuilder.formDeclarant();
    TDeclarant.Person person = declarant.getPerson();
    assertNotNull(person);
    assertEquals("Иванов", person.getFIO().getSurname());
    assertEquals("Иван", person.getFIO().getFirst());
    assertEquals("Иванович", person.getFIO().getPatronymic());
    assertNotNull(person.getAgent());
    assertEquals("agentKind", person.getAgent().getAgentKind());
  }

  @Test
  public void testDeclarantOrganization() throws Exception {
    context.setVariable("declarantType", "ORGANISATION");
    context.setVariable("declLegalPersonName", "declLegalPersonName");
    context.setVariable("declLegalPersonOPF", "declLegalPersonOPF");

    context.setVariable("declLegalPersonKPP", "declLegalPersonKPP");
    context.setVariable("declLegalPersonINN", "declLegalPersonINN");
    context.setVariable("declLegalPersonOGRN", "declLegalPersonOGRN");
    context.setVariable("declLegalPersonEmail", "declLegalPersonEmail");
    context.setVariable("declLegalPersonPhone", "declLegalPersonPhone");
    context.setVariable("declLegalPersonContactInfo", "declLegalPersonContactInfo");
    context.setVariable("declLegalPersonRegDate", TestUtils.getDateValue("2001-01-01")); //тип date
    context.setVariable("declLegalPersonRegAgency", "declLegalPersonRegAgency");
    context.setVariable("declLegalPersonDocumentCode", "declLegalPersonDocumentCode");

    context.setVariable("declLegalPersonDocumentName", "declLegalPersonDocumentName");
    context.setVariable("declLegalPersonDocumentSeries", "declLegalPersonDocumentSeries");
    context.setVariable("declLegalPersonDocumentNumber", "declLegalPersonDocumentNumber");
    context.setVariable("declLegalPersonDocumentDate", TestUtils.getDateValue("2001-09-01")); //тип date
    context.setVariable("declLegalPersonDocumentIssueOrgan", "declLegalPersonDocumentIssueOrgan");
    context.setVariable("declLegalPersonDocumentDesc", "declLegalPersonDocumentDesc");
    context.setVariable("agentKind", "agentKind");
    context.setVariable("agentDocumentACodeDocument", "558102100000");
    context.setVariable("agentDocumentADocumentName",
                        "Запрос о предоставлении сведений, содержащихся в Едином государственном реестре прав на недвижимое имущество и сделок с ним");
    context.setVariable("agentDocumentADocumentNumber", "26-0-1-21/4001/2011-166");
    context.setVariable("agentDocumentADocumentDate", TestUtils.getDateValue("2012-07-24"));
    context.setVariable("agentDocumentADocumentOriginalQuantity", 1L);
    context.setVariable("agentDocumentADocumentOriginalQuantitySheet", 1L);
    context.setVariable("agentDocumentADocumentCopyQuantity", 1L);
    context.setVariable("agentDocumentADocumentCopyQuantitySheet", 1L);
    EnclosureGRPBuilder enclosureGRPBuilder = new SubjectRightsEnclosureBuilder(context);
    TDeclarant declarant = enclosureGRPBuilder.formDeclarant();
    TOrganizationDeclarant organization = declarant.getOrganization();
    assertNotNull(organization);
    assertNotNull(organization.getAgent());
    assertEquals("agentKind", organization.getAgent().getAgentKind());

    assertEquals("declLegalPersonName", organization.getName());
    assertEquals("declLegalPersonOPF", organization.getCodeOPF());
    assertEquals("declLegalPersonKPP", organization.getCodeCPP());
    assertEquals("declLegalPersonINN", organization.getINN());
    assertEquals("declLegalPersonOGRN", organization.getCodeOGRN());
    assertEquals("declLegalPersonEmail", organization.getEMail());
    assertEquals("declLegalPersonPhone", organization.getPhone());
    assertEquals("declLegalPersonContactInfo", organization.getContactInfo());
    assertEquals(TestUtils.getDateValue("2001-01-01"), organization.getRegDate().toGregorianCalendar().getTime());
    assertEquals("declLegalPersonRegAgency", organization.getRegistrationAgency());

    assertEquals("declLegalPersonDocumentCode", organization.getDocument().getCodeDocument());
    assertEquals("declLegalPersonDocumentName", organization.getDocument().getName());
    assertEquals("declLegalPersonDocumentSeries", organization.getDocument().getSeries());
    assertEquals("declLegalPersonDocumentNumber", organization.getDocument().getNumber());
    assertEquals(TestUtils.getDateValue("2001-09-01"), organization.getDocument().getDate().toGregorianCalendar().getTime());
    assertEquals("declLegalPersonDocumentIssueOrgan", organization.getDocument().getIssueOrgan());
    assertEquals("declLegalPersonDocumentDesc", organization.getDocument().getDesc());
  }

  @Test
  public void testFillFreePaymentWithoutDocuments() throws Exception {
    context.setVariable("declarantType", "ORGANISATION");
    context.setVariable("isPaymentFree", true);
    context.setVariable("freePayment", 0L);
    context.setVariable("payment", 0L);

    EnclosureGRPBuilder enclosureGRPBuilder = new SubjectRightsEnclosureBuilder(context);
    RequestGRP.Request.Payment payment = enclosureGRPBuilder.formPayment();

    assertNotNull(payment);
    assertTrue(payment.isFree());
  }

  @Test
  public void testFillFreePaymentDocuments() throws Exception {
    context.setVariable("declarantType", "ORGANISATION");
    context.setVariable("isPaymentFree", true);
    context.setVariable("freePayment", 2L);
    context.setVariable("payment", 0L);
    context.setVariable("freePaymentADocumentCode_1", "freePaymentACodeDocument_1");
    context.setVariable("freePaymentADocumentName_1", "freePaymentADocumentName_1");
    context.setVariable("freePaymentADocumentNumber_1", "freePaymentADocumentNumber_1");
    context.setVariable("freePaymentADocumentDate_1", currentDate);
    context.setVariable("freePaymentADocumentIssue_1", "freePaymentAIssue_1");
    context.setVariable("freePaymentADocumentDesc_1", "freePaymentADesc_1");
    context.setVariable("freePaymentADocumentOriginalQuantity_1", 1L);
    context.setVariable("freePaymentADocumentOriginalQuantitySheet_1", 2L);
    context.setVariable("freePaymentADocumentCopyQuantity_1", 3L);
    context.setVariable("freePaymentADocumentCopyQuantitySheet_1", 4L);

    context.setVariable("freePaymentADocumentCode_2", "freePaymentACodeDocument_2");
    context.setVariable("freePaymentADocumentName_2", "freePaymentADocumentName_2");
    context.setVariable("freePaymentADocumentNumber_2", "freePaymentADocumentNumber_2");
    context.setVariable("freePaymentADocumentDate_2", currentDate); //тип date
    context.setVariable("freePaymentADocumentIssue_2", "freePaymentAIssue_2");
    context.setVariable("freePaymentADocumentDesc_2", "freePaymentADesc_2");
    context.setVariable("freePaymentADocumentOriginalQuantity_2", 5L);
    context.setVariable("freePaymentADocumentOriginalQuantitySheet_2", 6L);
    context.setVariable("freePaymentADocumentCopyQuantity_2", 7L);
    context.setVariable("freePaymentADocumentCopyQuantitySheet_2", 8L);


    EnclosureGRPBuilder enclosureGRPBuilder = new SubjectRightsEnclosureBuilder(context);
    RequestGRP.Request.Payment payment = enclosureGRPBuilder.formPayment();

    assertNotNull(payment);
    ReasonFreeDocuments documents = payment.getReasonFreeDocuments();
    assertNotNull(documents);
    assertEquals(2, documents.getReasonFreeDocument().size());
    TAppliedDocument documentFirst = documents.getReasonFreeDocument().get(0);
    assertEquals("freePaymentACodeDocument_1", documentFirst.getCodeDocument());
    assertEquals("freePaymentADocumentName_1", documentFirst.getName());
    assertEquals("freePaymentADocumentNumber_1", documentFirst.getNumber());
    assertEquals(currentDate, documentFirst.getDate().toGregorianCalendar().getTime());
    assertEquals("freePaymentAIssue_1", documentFirst.getIssueOrgan());
    assertEquals("freePaymentADesc_1", documentFirst.getDesc());
    assertEquals(BigInteger.valueOf(1l), documentFirst.getQuantity().getOriginal().getQuantity());
    assertEquals(BigInteger.valueOf(2L), documentFirst.getQuantity().getOriginal().getQuantitySheet());
    assertEquals(BigInteger.valueOf(3L), documentFirst.getQuantity().getCopy().getQuantity());
    assertEquals(BigInteger.valueOf(4L), documentFirst.getQuantity().getCopy().getQuantitySheet());

    TAppliedDocument secondDocument = documents.getReasonFreeDocument().get(1);
    assertEquals("freePaymentACodeDocument_2", secondDocument.getCodeDocument());
    assertEquals("freePaymentADocumentName_2", secondDocument.getName());
    assertEquals("freePaymentADocumentNumber_2", secondDocument.getNumber());
    assertEquals(currentDate, secondDocument.getDate().toGregorianCalendar().getTime());
    assertEquals("freePaymentAIssue_2", secondDocument.getIssueOrgan());
    assertEquals("freePaymentADesc_2", secondDocument.getDesc());
    TQuantity secondDocumentQuantity = secondDocument.getQuantity();
    assertEquals(BigInteger.valueOf(5l), secondDocumentQuantity.getOriginal().getQuantity());
    assertEquals(BigInteger.valueOf(6L), secondDocumentQuantity.getOriginal().getQuantitySheet());
    assertEquals(BigInteger.valueOf(7L), secondDocumentQuantity.getCopy().getQuantity());
    assertEquals(BigInteger.valueOf(8L), secondDocumentQuantity.getCopy().getQuantitySheet());
  }

  @Test
  public void testPaymentDocument() throws Exception {
    context.setVariable("declarantType", "ORGANISATION");
    context.setVariable("isPaymentFree", false);
    context.setVariable("paymentDocument", 2L);
    context.setVariable("payment", 2L);
    context.setVariable("freePayment", 0L);
    context.setVariable("paymentDoc_Type_1", "Вид платежного документа");
    context.setVariable("paymentNumber_1", "Номер");
    context.setVariable("paymentBIC_1", "БИК");
    context.setVariable("paymentBank_Name_1", "Наименование банка");
    context.setVariable("paymentOKATO_1", "ОКАТО организации, к которой относится платёж");
    context.setVariable("paymentSettlement_Account_1", "Расчётный счёт");
    context.setVariable("paymentDate_1", currentDate);
    context.setVariable("paymentFIOPayer_1", "ФИО плательщика");
    context.setVariable("paymentSum_1", 100l);
    context.setVariable("paymentADocumentOriginalQuantity_1", 1L);
    context.setVariable("paymentADocumentOriginalQuantitySheet_1", 2L);
    context.setVariable("paymentADocumentCopyQuantity_1", 3L);
    context.setVariable("paymentADocumentCopyQuantitySheet_1", 4l);
    context.setVariable("paymentDoc_Type_2", "Вид платежного документа");
    context.setVariable("paymentNumber_2", "Номер");
    context.setVariable("paymentBIC_2", "БИК");
    context.setVariable("paymentBank_Name_2", "Наименование банка");
    context.setVariable("paymentOKATO_2", "ОКАТО организации, к которой относится платёж");
    context.setVariable("paymentSettlement_Account_2", "Расчётный счёт");
    context.setVariable("paymentDate_2", currentDate);
    context.setVariable("paymentFIOPayer_2", "ФИО плательщика");
    context.setVariable("paymentSum_2", 100l);
    context.setVariable("paymentADocumentOriginalQuantity_2", 1L);
    context.setVariable("paymentADocumentOriginalQuantitySheet_2", 2L);
    context.setVariable("paymentADocumentCopyQuantity_2", 3L);
    context.setVariable("paymentADocumentCopyQuantitySheet_2", 4l);

    RequestGRP.Request.Payment payment = enclosureGRPBuilder.formPayment();

    assertNotNull(payment);
    PaymentDocuments paymentDocuments = payment.getPaymentDocuments();
    assertNotNull(paymentDocuments);
    assertEquals(2, paymentDocuments.getPaymentDocument().size());
    checkPaymentDocument(paymentDocuments.getPaymentDocument().get(0));
    checkPaymentDocument(paymentDocuments.getPaymentDocument().get(1));
  }


  private void checkPaymentDocument(TPayDocument doc) {
    assertEquals("Вид платежного документа", doc.getDocType()); // paymentDoc_Type;
    assertEquals("Номер", doc.getNumber()); // paymentNumber;
    assertEquals("БИК", doc.getBIC()); // paymentBIC;
    assertEquals("Наименование банка", doc.getBankName()); // paymentBank_Name;
    assertEquals("ОКАТО организации, к которой относится платёж", doc.getOKATO()); // paymentOKATO;
    assertEquals("Расчётный счёт", doc.getSettlementAccount()); // paymentSettlement_Account;
    assertEquals(currentDate, doc.getDate().toGregorianCalendar().getTime());
    assertEquals("ФИО плательщика", doc.getFIOPayment()); // paymentFIOPayer;
    assertEquals(100l, doc.getSum().longValue());
    assertEquals(1L, doc.getQuantity().getOriginal().getQuantity().longValue());
    assertEquals(2L, doc.getQuantity().getOriginal().getQuantitySheet().longValue());
    assertEquals(3L, doc.getQuantity().getCopy().getQuantity().longValue());
    assertEquals(4l, doc.getQuantity().getCopy().getQuantitySheet().longValue());
  }


}
