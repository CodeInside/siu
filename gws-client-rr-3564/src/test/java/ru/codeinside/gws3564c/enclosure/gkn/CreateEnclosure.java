/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws3564c.enclosure.gkn;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Before;
import org.junit.Test;
import ru.codeinside.gws3564c.DummyContext;
import ru.gkn.*;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.Date;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Тестируем создаение вложений
 */
public class CreateEnclosure {
  private Date currentDate;

  @Before
  public void setUp() throws Exception {
    currentDate = DateUtils.parseDate("01.02.2001", new String[]{"dd.MM.yyyy"});

  }

  /**
   * Проверка заполенения данных физ лица
   *
   * @throws Exception
   */
  @Test
  public void testEnclosureForCadastralPassportOwnerIsPerson() throws Exception {

    EncRealtyCadastralPassport encBuilder = new EncRealtyCadastralPassport(createContext("PERSON"));
    RequestGKN enclosure = encBuilder.buildGKNObject("id");
    TPerson person = enclosure.getRequest().getDeclarant().getPerson();
    assertNotNull(person);
    assertEquals("declPersonFIOSurname", person.getFIO().getSurname());
    assertEquals("declPersonFIOFirst", person.getFIO().getFirst());
    assertEquals("declPersonFIOPatronymic", person.getFIO().getPatronymic());
    assertEquals("declPersonEmail", person.getEMail());
    assertEquals("declPersonPhone", person.getPhone());
    assertEquals("declPersonSNILS", person.getSNILS());
    assertEquals(getDateValue("1929-22-11"), person.getDateBirth().toGregorianCalendar().getTime());
    assertEquals("declPersonBirthLocation", person.getPlaceBirth());
    assertEquals(SSex.F, person.getSex());
    assertEquals("declPersonMarriageStatus", person.getFamilyStatus());
    assertEquals("declPersonPDocumentCode", person.getDocument().getCodeDocument());
    assertEquals("declPersonPDocumentSeries", person.getDocument().getSeries());
    assertEquals("declPersonPDocumentNumber", person.getDocument().getNumber());
    assertEquals(getDateValue("2001-11-01"), person.getDocument().getDate().toGregorianCalendar().getTime());
    assertEquals("declPersonPDocumentIssueOrgan", person.getDocument().getIssueOrgan());
    assertNotNull(person.getAgent());
  }

  /**
   * Проверка заполения данных юр лица
   *
   * @throws Exception
   */
  @Test
  public void testBuildEnclosureForOrganization() throws Exception {

    EncRealtyCadastralPassport encBuilder = new EncRealtyCadastralPassport(createContext("ORGANISATION"));
    RequestGKN enclosure = encBuilder.buildGKNObject("id");
    TOrganization organization = enclosure.getRequest().getDeclarant().getOrganization();
    assertNotNull(organization);
    assertEquals("declLegalPersonName", organization.getName());
    assertEquals("declLegalPersonOPF", organization.getCodeOPF());
    assertEquals("declLegalPersonKPP", organization.getCodeCPP());
    assertEquals("declLegalPersonINN", organization.getINN());
    assertEquals("declLegalPersonOGRN", organization.getCodeOGRN());
    assertEquals("declLegalPersonEmail", organization.getEMail());
    assertEquals("declLegalPersonPhone", organization.getPhone());
    assertEquals("declLegalPersonContactInfo", organization.getContactInfo());
    assertEquals(getDateValue("2001-01-01"), organization.getRegDate().toGregorianCalendar().getTime());
    assertEquals("declLegalPersonRegAgency", organization.getRegistrationAgency());
    assertEquals("declLegalPersonDocumentCode", organization.getDocument().getCodeDocument());
    assertEquals("declLegalPersonDocumentName", organization.getDocument().getName());
    assertEquals("declLegalPersonDocumentSeries", organization.getDocument().getSeries());
    assertEquals("declLegalPersonDocumentNumber", organization.getDocument().getNumber());
    assertEquals(getDateValue("2001-09-01"), organization.getDocument().getDate().toGregorianCalendar().getTime());
    assertEquals("declLegalPersonDocumentIssueOrgan", organization.getDocument().getIssueOrgan());
    assertEquals("declLegalPersonDocumentDesc", organization.getDocument().getDesc());
    assertNotNull(organization.getAgent());
  }

  /**
   * Проверка заполнения паспортного блока и
   *
   * @throws Exception
   */
  @Test
  public void testFillRequiredData() throws Exception {

    EncRealtyCadastralPassport encBuilder = new EncRealtyCadastralPassport(createContext("ORGANISATION"));
    RequestGKN enclosure = encBuilder.buildGKNObject("id");
    RequestGKN.Request.RequiredData.CadastralPassport cadastralPassport = enclosure.getRequest().getRequiredData().getCadastralPassport();
    assertNotNull(cadastralPassport);
    assertEquals("cadastralPassportCLADR", cadastralPassport.getObject().getLocation().getCodeKLADR()); //cadastralPassportCLADR
    assertEquals("cadastralPassportOKATO", cadastralPassport.getObject().getLocation().getCodeOKATO()); //cadastralPassportOKATO
    assertEquals("cadastralPassportPostalCode", cadastralPassport.getObject().getLocation().getPostalCode()); //cadastralPassportPostalCode
    assertEquals("cadastralPassportRegion", cadastralPassport.getObject().getLocation().getRegion()); //cadastralPassportRegion
    assertEquals("cadastralPassportDistrictName", cadastralPassport.getObject().getLocation().getDistrict().getName()); //cadastralPassportDistrictName
    assertEquals("р-н", cadastralPassport.getObject().getLocation().getDistrict().getType().value()); //cadastralPassportDistrictType
    assertEquals("cadastralPassportCityName", cadastralPassport.getObject().getLocation().getCity().getName()); //cadastralPassportCityName
    assertEquals("г", cadastralPassport.getObject().getLocation().getCity().getType().value()); //cadastralPassportDCity
    assertEquals("cadastralPassportUrbanDistictName", cadastralPassport.getObject().getLocation().getUrbanDistrict().getName()); //cadastralPassportUrbanDistictName
    assertEquals("р-н", cadastralPassport.getObject().getLocation().getUrbanDistrict().getType().value()); //cadastralPassportUrbanDistictType
    assertEquals("cadastralPassportSovietVillageName", cadastralPassport.getObject().getLocation().getSovietVillage().getName()); //cadastralPassportSovietVillageName
    assertEquals("волость", cadastralPassport.getObject().getLocation().getSovietVillage().getType().value()); //cadastralPassportSovietVillageType
    assertEquals("cadastralPassportLocalityName", cadastralPassport.getObject().getLocation().getLocality().getName()); //cadastralPassportLocalityName
    assertEquals("аал", cadastralPassport.getObject().getLocation().getLocality().getType().value()); //cadastralPassportLocalityType
    assertEquals("cadastralPassportStreetName", cadastralPassport.getObject().getLocation().getStreet().getName()); //cadastralPassportStreetName
    assertEquals("аллея", cadastralPassport.getObject().getLocation().getStreet().getType().value()); //cadastralPassportDStreets
    assertEquals("д", cadastralPassport.getObject().getLocation().getLevel1().getType().value()); //cadastralPassportLocationLevel1Type
    assertEquals("cadastralPassportLocationLevel1Value", cadastralPassport.getObject().getLocation().getLevel1().getValue()); //cadastralPassportLocationLevel1Value
    assertEquals("корп", cadastralPassport.getObject().getLocation().getLevel2().getType().value()); //cadastralPassportLocationLevel2Type
    assertEquals("cadastralPassportLocationLevel2Value", cadastralPassport.getObject().getLocation().getLevel2().getValue()); //cadastralPassportLocationLevel2Value
    assertEquals("блок", cadastralPassport.getObject().getLocation().getLevel3().getType().value()); //cadastralPassportLocationLevel3Type
    assertEquals("cadastralPassportLocationLevel3Value", cadastralPassport.getObject().getLocation().getLevel3().getValue()); //cadastralPassportLocationLevel3Value
    assertEquals("кв", cadastralPassport.getObject().getLocation().getApartment().getType().value()); //cadastralPassportLocationApartmentType
    assertEquals("cadastralPassportLocationApartmentValue", cadastralPassport.getObject().getLocation().getApartment().getValue()); //cadastralPassportLocationApartmentValue
    assertEquals("cadastralPassportLocationOther", cadastralPassport.getObject().getLocation().getOther()); //cadastralPassportLocationOther
    assertEquals("cadastralPassportLocationNote", cadastralPassport.getObject().getLocation().getNote()); //cadastralPassportLocationNote
    assertEquals("cadastralPassportObjKind", cadastralPassport.getObject().getObjKind()); //cadastralPassportLocationNote
    assertEquals("cadastralPassportNumber", cadastralPassport.getObject().getCadastralNumber());
  }

  @Test
  public void testFillAppliedDocument() throws Exception {
    EncRealtyCadastralPassport encBuilder = new EncRealtyCadastralPassport(createContext("ORGANISATION"));
    RequestGKN enclosure = encBuilder.buildGKNObject("id");
    RequestGKN.Request.AppliedDocuments appliedDocuments = enclosure.getRequest().getAppliedDocuments();
    assertNotNull(appliedDocuments);
    assertEquals(2, appliedDocuments.getAppliedDocument().size());
    TAppliedDocument documentFirst = appliedDocuments.getAppliedDocument().get(0);
    assertEquals("appliedACodeDocument_1", documentFirst.getCodeDocument()); //appliedACodeDocument
    assertEquals("appliedADocumentName_1", documentFirst.getName()); //appliedADocumentName
    assertEquals("appliedADocumentNumber_1", documentFirst.getNumber()); //appliedADocumentNumber
    assertEquals(currentDate, documentFirst.getDate().toGregorianCalendar().getTime()); //тип date
    assertEquals("appliedAIssue_1", documentFirst.getIssueOrgan()); //appliedAIssue
    assertEquals("appliedADesc_1", documentFirst.getDesc()); //appliedADesc
    assertEquals(BigInteger.valueOf(1l), documentFirst.getQuantity().getOriginal().getQuantity()); //тип string
    assertEquals(BigInteger.valueOf(2L), documentFirst.getQuantity().getOriginal().getQuantitySheet());
    assertEquals(BigInteger.valueOf(3L), documentFirst.getQuantity().getCopy().getQuantity()); //тип string
    assertEquals(BigInteger.valueOf(4L), documentFirst.getQuantity().getCopy().getQuantitySheet());

    TAppliedDocument documentSecond = appliedDocuments.getAppliedDocument().get(1);
    assertEquals("appliedACodeDocument_2", documentSecond.getCodeDocument()); //appliedACodeDocument
    assertEquals("appliedADocumentName_2", documentSecond.getName()); //appliedADocumentName
    assertEquals("appliedADocumentNumber_2", documentSecond.getNumber()); //appliedADocumentNumber
    assertEquals(currentDate, documentSecond.getDate().toGregorianCalendar().getTime()); //тип date
    assertEquals("appliedAIssue_2", documentSecond.getIssueOrgan()); //appliedAIssue
    assertEquals("appliedADesc_2", documentSecond.getDesc()); //appliedADesc
    assertEquals(BigInteger.valueOf(5L), documentSecond.getQuantity().getOriginal().getQuantity()); //тип string
    assertEquals(BigInteger.valueOf(6L), documentSecond.getQuantity().getOriginal().getQuantitySheet());
    assertEquals(BigInteger.valueOf(7L), documentSecond.getQuantity().getCopy().getQuantity()); //тип string
    assertEquals(BigInteger.valueOf(8L), documentSecond.getQuantity().getCopy().getQuantitySheet());
  }

  @Test
  public void testFillPaymentDocuments() throws Exception {

    EncRealtyCadastralPassport encBuilder = new EncRealtyCadastralPassport(createContext("ORGANISATION"));
    RequestGKN enclosure = encBuilder.buildGKNObject("id");
    RequestGKN.Request.PaymentDocuments paymentDocuments = enclosure.getRequest().getPaymentDocuments();
    assertNotNull(paymentDocuments);
    assertEquals(2, paymentDocuments.getPaymentDocument().size());
    checkPaymentDocument(paymentDocuments.getPaymentDocument().get(0));
    checkPaymentDocument(paymentDocuments.getPaymentDocument().get(1));
  }

  private void checkPaymentDocument(TPayDocument firstDocument) {
    assertEquals("Вид платежного документа", firstDocument.getDocType()); // paymentDoc_Type;
    assertEquals("Номер", firstDocument.getNumber()); // paymentNumber;
    assertEquals("БИК", firstDocument.getBIC()); // paymentBIC;
    assertEquals("Наименование банка", firstDocument.getBankName()); // paymentBank_Name;
    assertEquals("ОКАТО организации, к которой относится платёж", firstDocument.getOKATO()); // paymentOKATO;
    assertEquals("Расчётный счёт", firstDocument.getSettlementAccount()); // paymentSettlement_Account;
    assertEquals(currentDate, firstDocument.getDate().toGregorianCalendar().getTime());
    assertEquals("ФИО плательщика", firstDocument.getFIOPayment()); // paymentFIOPayer;
    assertEquals(100l, firstDocument.getSum().longValue());
    assertEquals(1L, firstDocument.getQuantity().getOriginal().getQuantity().longValue());
    assertEquals(2L, firstDocument.getQuantity().getOriginal().getQuantitySheet().longValue());
    assertEquals(3L, firstDocument.getQuantity().getCopy().getQuantity().longValue());
    assertEquals(4l, firstDocument.getQuantity().getCopy().getQuantitySheet().longValue());
  }

  private DummyContext createContext(String personType) throws ParseException {
    DummyContext context = new DummyContext();
    context.setVariable("okato", "okato"); //тип string
    context.setVariable("oktmo", "oktmo"); //тип string
    context.setVariable("requestType", "requestType"); //тип string
    context.setVariable("enclosure_request_type", "enclosure_request_type"); //тип string
    context.setVariable("declarantType", personType); //тип enum

    context.setVariable("declKind", "declKind"); //тип enum default="357007000000" required="true"

//Начало данных для гос органа
    context.setVariable("declGovernanceCode", "declGovernanceCode"); //тип enum default="007001001001" required="true">
    context.setVariable("declGovernanceName", "declGovernanceName"); //тип string
    context.setVariable("declGovernanceEmail", "declGovernanceEmail"); //тип string
    context.setVariable("declGovernancePhone", "declGovernancePhone"); //тип string
// Конец данных для гос органа-->
// Начало данных для юр лица-->
    context.setVariable("declLegalPersonName", "declLegalPersonName"); //тип string
    context.setVariable("declLegalPersonOPF", "declLegalPersonOPF"); //тип enum

    context.setVariable("declLegalPersonKPP", "declLegalPersonKPP"); //тип string
    context.setVariable("declLegalPersonINN", "declLegalPersonINN"); //тип string
    context.setVariable("declLegalPersonOGRN", "declLegalPersonOGRN"); //тип string
    context.setVariable("declLegalPersonEmail", "declLegalPersonEmail"); //тип string
    context.setVariable("declLegalPersonPhone", "declLegalPersonPhone"); //тип string
    context.setVariable("declLegalPersonContactInfo", "declLegalPersonContactInfo"); //тип string
    context.setVariable("declLegalPersonRegDate", getDateValue("2001-01-01")); //тип date
    context.setVariable("declLegalPersonRegAgency", "declLegalPersonRegAgency"); //тип string
    context.setVariable("declLegalPersonDocumentCode", "declLegalPersonDocumentCode"); //тип enum

    context.setVariable("declLegalPersonDocumentName", "declLegalPersonDocumentName"); //тип string
    context.setVariable("declLegalPersonDocumentSeries", "declLegalPersonDocumentSeries"); //тип string
    context.setVariable("declLegalPersonDocumentNumber", "declLegalPersonDocumentNumber"); //тип string
    context.setVariable("declLegalPersonDocumentDate", getDateValue("2001-09-01")); //тип date
    context.setVariable("declLegalPersonDocumentIssueOrgan", "declLegalPersonDocumentIssueOrgan"); //тип string
    context.setVariable("declLegalPersonDocumentDesc", "declLegalPersonDocumentDesc"); //тип string
    // Конец данных для юр лица
    // Начало данных для физ. лица
    context.setVariable("declPersonFIOSurname", "declPersonFIOSurname"); //тип string
    context.setVariable("declPersonFIOFirst", "declPersonFIOFirst"); //тип string
    context.setVariable("declPersonFIOPatronymic", "declPersonFIOPatronymic"); //тип string
    context.setVariable("declPersonEmail", "declPersonEmail"); //тип string
    context.setVariable("declPersonPhone", "declPersonPhone"); //тип string
    context.setVariable("declPersonSNILS", "declPersonSNILS"); //тип string>
    context.setVariable("declPersonBirthDate", getDateValue("1929-22-11")); //тип string>
    context.setVariable("declPersonBirthLocation", "declPersonBirthLocation"); //тип string>
    context.setVariable("declPersonSex", "F"); //тип enum>

    context.setVariable("declPersonMarriageStatus", "declPersonMarriageStatus"); //тип enum>

    context.setVariable("declPersonPDocumentCode", "declPersonPDocumentCode"); //тип enum>

    context.setVariable("declPersonPDocumentSeries", "declPersonPDocumentSeries"); //тип string
    context.setVariable("declPersonPDocumentNumber", "declPersonPDocumentNumber"); //тип string
    context.setVariable("declPersonPDocumentDate", getDateValue("2001-11-01")); //тип date
    context.setVariable("declPersonPDocumentIssueOrgan", "declPersonPDocumentIssueOrgan"); //тип string
    // Конец данных для физ. лица
    context.setVariable("declLocationOKATO", "declLocationOKATO"); //тип string>
    context.setVariable("declLocationCLADR", "declLocationCLADR"); //тип string>
    context.setVariable("declLocationPostalCode", "declLocationPostalCode"); //тип string
    context.setVariable("declLocationRegion", "declLocationRegion"); //тип string
    context.setVariable("declLocationDistrictName", "declLocationDistrictName"); //тип string
    context.setVariable("declLocationDistrictType", "р-н"); //тип enum

    context.setVariable("declLocationCityName", "declLocationCityName"); //тип string
    context.setVariable("declLocationDCity", "declLocationDCity"); //тип string
    context.setVariable("declLocationUrbanDistictName", "declLocationUrbanDistictName"); //тип string
    context.setVariable("declLocationUrbanDistictType", "declLocationUrbanDistictType"); //тип string

    context.setVariable("declLocationSovietVillageName", "declLocationSovietVillageName"); //тип string
    context.setVariable("declLocationSovietVillageType", "declLocationSovietVillageType"); //тип string

    context.setVariable("declLocationLocalityName", "declLocationLocalityName"); //тип string
    context.setVariable("declLocationLocalityType", "declLocationLocalityType"); //тип string

    context.setVariable("declLocationStreetName", "declLocationStreetName"); //тип string
    context.setVariable("declLocationDStreets", "аллея"); //тип enum

    context.setVariable("declLocationLocationLevel1Type", "д"); //тип enum

    context.setVariable("declLocationLocationLevel1Value", "declLocationLocationLevel1Value"); //тип string
    context.setVariable("declLocationLocationLevel2Type", "declLocationLocationLevel2Type"); //тип enum

    context.setVariable("declLocationLocationLevel2Value", "declLocationLocationLevel2Value"); //тип string
    context.setVariable("declLocationLocationLevel3Type", "блок"); //тип enum

    context.setVariable("declLocationLocationLevel3Value", "declLocationLocationLevel3Value"); //тип string
    context.setVariable("declLocationLocationApartmentType", "declLocationLocationApartmentType"); //тип enum

    context.setVariable("declLocationLocationApartmentValue", "declLocationLocationApartmentValue"); //тип string
    context.setVariable("declLocationLocationOther", "declLocationLocationOther"); //тип string
    context.setVariable("declLocationLocationNote", "declLocationLocationNote"); //тип string
    context.setVariable("agentFIOSurname", "agentFIOSurname"); //тип string
    context.setVariable("agentFIOFirst", "agentFIOFirst"); //тип string
    context.setVariable("agentFIOPatronymic", "agentFIOPatronymic"); //тип string
    context.setVariable("agentPDocumentCode", "agentPDocumentCode"); //тип enum

    context.setVariable("agentPDocumentSeries", "agentPDocumentSeries"); //тип string
    context.setVariable("agentPDocumentNumber", "agentPDocumentNumber"); //тип string
    context.setVariable("agentPDocumentDate", getDateValue("2001-11-01")); //тип date
    context.setVariable("agentPDocumentIssueOrgan", "agentPDocumentIssueOrgan"); //тип string
    context.setVariable("agentEmail", "agentEmail"); //тип string
    context.setVariable("agentPhone", "agentPhone"); //тип string
    context.setVariable("agentSNILS", "agentSNILS"); //тип string
    context.setVariable("agentKind", "agentKind"); //тип enum

    context.setVariable("agentOKATO", "agentOKATO"); //тип string
    context.setVariable("agentCLADR", "agentCLADR"); //тип string
    context.setVariable("agentPostalCode", "agentPostalCode"); //тип string
    context.setVariable("agentRegion", "agentRegion"); //тип string
    context.setVariable("agentDistrictName", "agentDistrictName"); //тип string
    context.setVariable("agentDistrictType", "р-н"); //тип enum

    context.setVariable("agentCityName", "agentCityName"); //тип string
    context.setVariable("agentDCity", "г"); //тип string
    context.setVariable("agentUrbanDistictName", "agentUrbanDistictName"); //тип string
    context.setVariable("agentUrbanDistictType", "р-н"); //тип string

    context.setVariable("agentSovietVillageName", "agentSovietVillageName"); //тип string
    context.setVariable("agentSovietVillageType", "с/с"); //тип string

    context.setVariable("agentLocalityName", "agentLocalityName"); //тип string
    context.setVariable("agentLocalityType", "аал"); //тип string

    context.setVariable("agentStreetName", "agentStreetName"); //тип string
    context.setVariable("agentDStreets", "аллея"); //тип enum

    context.setVariable("agentLocationLevel1Type", "д"); //тип enum

    context.setVariable("agentLocationLevel1Value", "agentLocationLevel1Value"); //тип string
    context.setVariable("agentLocationLevel2Type", "д"); //тип enum

    context.setVariable("agentLocationLevel2Value", "agentLocationLevel2Value"); //тип string
    context.setVariable("agentLocationLevel3Type", "блок"); //тип enum

    context.setVariable("agentLocationLevel3Value", "agentLocationLevel3Value"); //тип string
    context.setVariable("agentLocationApartmentType", "к"); //тип enum

    context.setVariable("agentLocationApartmentValue", "agentLocationApartmentValue"); //тип string
    context.setVariable("agentLocationOther", "agentLocationOther"); //тип string
    context.setVariable("agentLocationNote", "agentLocationNote"); //тип string
    context.setVariable("cadastralPassportObjKind", "cadastralPassportObjKind"); //тип enum required="true"

    context.setVariable("cadastralPassportOKATO", "cadastralPassportOKATO"); //тип string
    context.setVariable("cadastralPassportCLADR", "cadastralPassportCLADR"); //тип string
    context.setVariable("cadastralPassportPostalCode", "cadastralPassportPostalCode"); //тип string
    context.setVariable("cadastralPassportRegion", "cadastralPassportRegion"); //тип string
    context.setVariable("cadastralPassportDistrictName", "cadastralPassportDistrictName"); //тип string
    context.setVariable("cadastralPassportDistrictType", "р-н"); //тип enum

    context.setVariable("cadastralPassportCityName", "cadastralPassportCityName"); //тип string
    context.setVariable("cadastralPassportDCity", "г"); //тип string
    context.setVariable("cadastralPassportUrbanDistictName", "cadastralPassportUrbanDistictName"); //тип string
    context.setVariable("cadastralPassportUrbanDistictType", "р-н"); //тип string

    context.setVariable("cadastralPassportSovietVillageName", "cadastralPassportSovietVillageName"); //тип string
    context.setVariable("cadastralPassportSovietVillageType", "волость"); //тип string

    context.setVariable("cadastralPassportLocalityName", "cadastralPassportLocalityName"); //тип string
    context.setVariable("cadastralPassportLocalityType", "аал"); //тип string

    context.setVariable("cadastralPassportStreetName", "cadastralPassportStreetName"); //тип string
    context.setVariable("cadastralPassportDStreets", "аллея"); //тип enum

    context.setVariable("cadastralPassportLocationLevel1Type", "д"); //тип enum

    context.setVariable("cadastralPassportLocationLevel1Value", "cadastralPassportLocationLevel1Value"); //тип string
    context.setVariable("cadastralPassportLocationLevel2Type", "корп"); //тип enum

    context.setVariable("cadastralPassportLocationLevel2Value", "cadastralPassportLocationLevel2Value"); //тип string
    context.setVariable("cadastralPassportLocationLevel3Type", "блок"); //тип enum

    context.setVariable("cadastralPassportLocationLevel3Value", "cadastralPassportLocationLevel3Value"); //тип string
    context.setVariable("cadastralPassportLocationApartmentType", "кв"); //тип enum

    context.setVariable("cadastralPassportLocationApartmentValue", "cadastralPassportLocationApartmentValue"); //тип string
    context.setVariable("cadastralPassportLocationOther", "cadastralPassportLocationOther"); //тип string
    context.setVariable("cadastralPassportLocationNote", "cadastralPassportLocationNote"); //тип string

    context.setVariable("cadastralPassportNumber", "cadastralPassportNumber"); //тип string

    context.setVariable("applied", 2L); //тип enum
    context.setVariable("appliedADocumentCode_1", "appliedACodeDocument_1"); //тип enum
    context.setVariable("appliedADocumentName_1", "appliedADocumentName_1"); //тип string
    context.setVariable("appliedADocumentNumber_1", "appliedADocumentNumber_1"); //тип string
    context.setVariable("appliedADocumentDate_1", currentDate); //тип date
    context.setVariable("appliedAIssue_1", "appliedAIssue_1"); //тип string
    context.setVariable("appliedADesc_1", "appliedADesc_1"); //тип string
    context.setVariable("appliedADocumentOriginalQuantity_1", 1L); //тип string
    context.setVariable("appliedADocumentOriginalQuantitySheet_1", 2L); //тип string
    context.setVariable("appliedADocumentCopyQuantity_1", 3L); //тип string
    context.setVariable("appliedADocumentCopyQuantitySheet_1", 4L); //тип string

    context.setVariable("appliedADocumentCode_2", "appliedACodeDocument_2"); //тип enum
    context.setVariable("appliedADocumentName_2", "appliedADocumentName_2"); //тип string
    context.setVariable("appliedADocumentNumber_2", "appliedADocumentNumber_2"); //тип string
    context.setVariable("appliedADocumentDate_2", currentDate); //тип date
    context.setVariable("appliedAIssue_2", "appliedAIssue_2"); //тип string
    context.setVariable("appliedADesc_2", "appliedADesc_2"); //тип string
    context.setVariable("appliedADocumentOriginalQuantity_2", 5L); //тип string
    context.setVariable("appliedADocumentOriginalQuantitySheet_2", 6L); //тип string
    context.setVariable("appliedADocumentCopyQuantity_2", 7L); //тип string
    context.setVariable("appliedADocumentCopyQuantitySheet_2", 8L); //тип string

    context.setVariable("payment", 2L);
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
    return context;
  }

  private Date getDateValue(String dateStr) throws ParseException {
    return DateUtils.parseDate(dateStr, new String[]{"yyyy-MM-dd"});
  }
}
