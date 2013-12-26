/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws3564c.enclosure.gkn;

import ru.codeinside.gws.api.ExchangeContext;
import ru.codeinside.gws3564c.DeclarantType;
import ru.codeinside.gws3564c.context.TypedContext;
import ru.codeinside.gws3564c.enclosure.EnclosureRequestBuilder;
import ru.gkn.*;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.StringWriter;
import java.math.BigInteger;
import java.net.URL;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class RequestEnclosureBuilder implements EnclosureRequestBuilder {
  private final Logger logger = Logger.getLogger(getClass().getName());
  protected TypedContext ctx;

  protected RequestEnclosureBuilder(ExchangeContext ctx) {
    this.ctx = new TypedContext(ctx);
  }

  RequestGKN createGKNRequest(String id) {
    final RequestGKN requestGKN = new RequestGKN();
    requestGKN.setEDocument(eDocumentGKN(id));
    final RequestGKN.Request request = new RequestGKN.Request();
    //Declarant
    request.setDeclarant(formDeclarant());
    //RequiredData
    final RequestGKN.Request.RequiredData requiredData = createRequiredData();
    request.setRequiredData(requiredData);
    //Delivery
    TDelivery delivery = new TDelivery();
    delivery.setWebService(true);
    request.setDelivery(delivery);
    //AppliedDocuments
    createAppliedDocuments(request);
    createPaymentDocuments(request);
    request.setMunicipalService(new RequestGKN.Request.MunicipalService());
    requestGKN.setRequest(request);
    return requestGKN;
  }

  RequestGKN.EDocument eDocumentGKN(String id) {
    final RequestGKN.EDocument eDocument = new RequestGKN.EDocument();
    eDocument.setVersion("1.03");
    eDocument.setGUID(id);
    return eDocument;
  }

  String gknToString(RequestGKN requestGKN) {
    try {
      SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
      URL resource = getClass().getClassLoader().getResource("rr_xsd/RequestGKN_V01/STD_RequestGKN.xsd");
      final Schema schema = sf.newSchema(resource);
      JAXBContext jc = JAXBContext.newInstance(RequestGKN.class);
      final Marshaller marshaller = jc.createMarshaller();
      final StringWriter stringWriter = new StringWriter();
      marshaller.setSchema(schema);
      marshaller.marshal(requestGKN, stringWriter);
      return stringWriter.toString();
    } catch (Exception e) {
      printContextVars();
      throw new RuntimeException(e);
    }
  }

  private void printContextVars() {
    for (String varName : ctx.getVariableNames()) {
      logger.log(Level.SEVERE, String.format("%s => %s", varName, ctx.getVariable(varName)));
    }
  }

  TOwner formDeclarant() {
    final TOwner declarant = new TOwner();
    DeclarantType declarantType = DeclarantType.valueOf(ctx.getStrFromContext("declarantType"));
    switch (declarantType) {
      case GOVERNANCE: {
        final TGovernance governance = new TGovernance();
        governance.setEMail(ctx.getStrFromContext("declGovernanceEmail"));
        governance.setName(ctx.getStrFromContext("declGovernanceName"));
        governance.setGovernanceCode(ctx.getStrFromContext("declGovernanceCode"));
        governance.setPhone(ctx.getStrFromContext("declGovernancePhone"));
        governance.setContactInfo(ctx.getStrFromContext("declGovernanceContactInfo"));
        governance.setLocation(setLocation("declLocation"));
        governance.setAgent(formAgent());
        declarant.setGovernance(governance);
        break;

      }
      case PERSON: {
        TPerson person = formPerson("declPerson");
        declarant.setPerson(person);
        person.setAgent(formAgent());
        break;
      }

      case ORGANISATION:
        final TOrganization value = formOrganisation("declLegalPerson");
        value.setAgent(formAgent());
        declarant.setOrganization(value);
        break;
    }
    declarant.setDeclarantKind(ctx.getStrFromContext("declKind"));
    return declarant;
  }

  TAgent formAgent() {
    final TAgent agent = new TAgent();
    agent.setFIO(fio("agent"));
    agent.setDocument(formPDocument("agent"));
    agent.setLocation(setLocation("agent"));
    agent.setEMail(ctx.getStrFromContext("agentEmail"));
    agent.setPhone(ctx.getStrFromContext("agentPhone"));
    agent.setContactInfo(ctx.getStrFromContext("agentContactInfo"));
    agent.setSNILS(ctx.getStrFromContext("agentSNILS"));
    agent.setAgentKind(ctx.getStrFromContext("agentKind"));
    return agent;

  }

  TPerson formPerson(String type) {
    final TPerson person = new TPerson();

    person.setFIO(fio(type));    //required
    person.setCitizenship(ctx.getStrFromContext(type + "Citizenship"));
    person.setContactInfo(ctx.getStrFromContext(type + "ContactInfo"));
    person.setDocument(formPDocument(type));
    person.setDateBirth(date(ctx.getDateFromContext(type + "BirthDate")));
    person.setEMail(ctx.getStrFromContext(type + "Email"));
    person.setSNILS(ctx.getStrFromContext(type + "SNILS"));
    person.setPhone(ctx.getStrFromContext(type + "Phone"));
    person.setPlaceBirth(ctx.getStrFromContext(type + "BirthLocation"));
    if (ctx.isStringVariableHasValue(type + "Sex")){
      person.setSex(SSex.fromValue(ctx.getStrFromContext(type + "Sex")));
    }
    if (ctx.isStringVariableHasValue(type + "MarriageStatus")){
      person.setFamilyStatus(ctx.getStrFromContext(type + "MarriageStatus"));
    }
    return person;
  }

  PIdentityPersonDocumentV1 formPDocument(final String type) {
    final PIdentityPersonDocumentV1 pIdentityPersonDocument = new PIdentityPersonDocumentV1();
    pIdentityPersonDocument.setCodeDocument(ctx.getStrFromContext(type + "PDocumentCode"));
    pIdentityPersonDocument.setSeries(ctx.getStrFromContext(type + "PDocumentSeries"));
    pIdentityPersonDocument.setNumber(ctx.getStrFromContext(type + "PDocumentNumber"));
    pIdentityPersonDocument.setDesc(ctx.getStrFromContext(type + "PDocumentDesc"));
    XMLGregorianCalendar date = date(ctx.getDateFromContext(type + "PDocumentDate"));

    pIdentityPersonDocument.setDate(date);
    pIdentityPersonDocument.setIssueOrgan(ctx.getStrFromContext(type + "PDocumentIssueOrgan"));
    return pIdentityPersonDocument;
  }

  TFIO fio(final String type) {
    final TFIO tFIO = new TFIO();
    tFIO.setSurname(ctx.getStrFromContext(type + "FIOSurname"));
    tFIO.setFirst(ctx.getStrFromContext(type + "FIOFirst"));
    tFIO.setPatronymic(ctx.getStrFromContext(type + "FIOPatronymic"));
    return tFIO;
  }

  TOrganization formOrganisation(String type) {
    TOrganization result = new TOrganization();
    result.setName(ctx.getStrFromContext(type + "Name"));
    result.setCodeOPF(ctx.getStrFromContext(type + "OPF"));
    result.setCodeCPP(ctx.getStrFromContext(type + "KPP"));
    result.setINN(ctx.getStrFromContext(type + "INN"));
    result.setCodeOGRN(ctx.getStrFromContext(type + "OGRN"));
    result.setEMail(ctx.getStrFromContext(type + "Email"));
    result.setPhone(ctx.getStrFromContext(type + "Phone"));

    result.setContactInfo(ctx.getStrFromContext(type + "ContactInfo"));
    result.setRegDate(date(ctx.getDateFromContext(type + "RegDate")));
    result.setRegistrationAgency(ctx.getStrFromContext(type + "RegAgency"));
    result.setDocument(formOrganizationDocument("declLegalPersonDocument"));
    return result;
  }

  PIdentityOrganizationDocumentV1 formOrganizationDocument(String prefix) {
    if (!ctx.hasAtLeastOneVariableWithPrefix(prefix)) return null;
    PIdentityOrganizationDocumentV1 result = new PIdentityOrganizationDocumentV1();
    result.setCodeDocument(ctx.getStrFromContext(prefix + "Code"));
    result.setName(ctx.getStrFromContext(prefix + "Name"));
    result.setSeries(ctx.getStrFromContext(prefix + "Series"));
    result.setNumber(ctx.getStrFromContext(prefix + "Number"));
    result.setDate(date(ctx.getDateFromContext(prefix + "Date")));
    result.setIssueOrgan(ctx.getStrFromContext(prefix + "IssueOrgan"));
    result.setDesc(ctx.getStrFromContext(prefix + "Desc"));
    return result;
  }

  XMLGregorianCalendar date(Date date) {
    if (date == null) {
      return null;
    }
    try {
      final GregorianCalendar calendar = new GregorianCalendar();
      calendar.setTime(date);
      final XMLGregorianCalendar xml = DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
      xml.setMillisecond(DatatypeConstants.FIELD_UNDEFINED);
      xml.setSecond(DatatypeConstants.FIELD_UNDEFINED);
      xml.setMinute(DatatypeConstants.FIELD_UNDEFINED);
      xml.setHour(DatatypeConstants.FIELD_UNDEFINED);
      xml.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
      return xml;
    } catch (final DatatypeConfigurationException e) {
      throw new RuntimeException(e);
    }
  }

  PAddressV1 setLocation(final String type) {
    final PAddressV1 pAddressV1 = new PAddressV1();
    if (ctx.isStringVariableHasValue(type + "PostalCode")) {
      pAddressV1.setPostalCode(ctx.getStrFromContext(type + "PostalCode"));
    }
    pAddressV1.setRegion(ctx.getStrFromContext(type + "Region"));

    if (ctx.isStringVariableHasValue(type + "OKATO")) {
      pAddressV1.setCodeOKATO(ctx.getStrFromContext(type + "OKATO"));
    }
    if (ctx.isStringVariableHasValue(type + "CLADR")) {
      pAddressV1.setCodeKLADR(ctx.getStrFromContext(type + "CLADR"));
    }

    if (ctx.isStringVariableHasValue(type + "DistrictName")) {
      final TDistrict district = new TDistrict();
      district.setName(ctx.getStrFromContext(type + "DistrictName"));
      district.setType(DDistrict.fromValue(ctx.getStrFromContext(type + "DistrictType")));
      pAddressV1.setDistrict(district);
    }

    if (ctx.isStringVariableHasValue(type + "CityName")) {
      final TCity tCity = new TCity();
      tCity.setName(ctx.getStrFromContext(type + "CityName"));
      tCity.setType(DCity.fromValue(ctx.getStrFromContext(type + "DCity")));
      pAddressV1.setCity(tCity);
    }
    if (ctx.isStringVariableHasValue(type + "UrbanDistictName")) {
      final TUrbanDistrict tCity = new TUrbanDistrict();
      tCity.setName(ctx.getStrFromContext(type + "UrbanDistictName"));
      tCity.setType(DUrbanDistrict.fromValue(ctx.getStrFromContext(type + "UrbanDistictType")));
      pAddressV1.setUrbanDistrict(tCity);
    }
    if (ctx.isStringVariableHasValue(type + "SovietVillageName")) {
      final TSovietVillage sovietVillage = new TSovietVillage();
      sovietVillage.setName(ctx.getStrFromContext(type + "SovietVillageName"));
      sovietVillage.setType(DSovietVillage.fromValue(ctx.getStrFromContext(type + "SovietVillageType")));
      pAddressV1.setSovietVillage(sovietVillage);
    }
    if (ctx.isStringVariableHasValue(type + "LocalityName")) {
      TLocality value = new TLocality();
      value.setName(ctx.getStrFromContext(type + "LocalityName"));
      value.setType(DInhabitedLocalities.fromValue(ctx.getStrFromContext(type + "LocalityType")));
      pAddressV1.setLocality(value);
    }
    if (ctx.isStringVariableHasValue(type + "StreetName")) {
      final TStreet tStreet = new TStreet();
      tStreet.setName(ctx.getStrFromContext(type + "StreetName"));
      tStreet.setType(DStreets.fromValue(ctx.getStrFromContext(type + "DStreets")));
      pAddressV1.setStreet(tStreet);
    }
    if (ctx.isStringVariableHasValue(type + "LocationLevel1Type")) {
      final TLevel1 tLevel1 = new TLevel1();
      tLevel1.setValue(ctx.getStrFromContext(type + "LocationLevel1Value"));
      tLevel1.setType(DLocationLevel1Type.fromValue(ctx.getStrFromContext(type + "LocationLevel1Type")));
      pAddressV1.setLevel1(tLevel1);
    }
    if (ctx.isStringVariableHasValue(type + "LocationLevel2Type")) {
      final TLevel2 tLevel2 = new TLevel2();
      tLevel2.setValue(ctx.getStrFromContext(type + "LocationLevel2Value"));
      tLevel2.setType(DLocationLevel2Type.fromValue(ctx.getStrFromContext(type + "LocationLevel2Type")));
      pAddressV1.setLevel2(tLevel2);
    }
    if (ctx.isStringVariableHasValue(type + "LocationLevel3Type")) {
      final TLevel3 tLevel3 = new TLevel3();
      tLevel3.setValue(ctx.getStrFromContext(type + "LocationLevel3Value"));
      tLevel3.setType(DLocationLevel3Type.fromValue(ctx.getStrFromContext(type + "LocationLevel3Type")));
      pAddressV1.setLevel3(tLevel3);
    }
    if (ctx.isStringVariableHasValue(type + "LocationApartmentType")) {
      final TApartment tApartment = new TApartment();
      tApartment.setValue(ctx.getStrFromContext(type + "LocationApartmentValue"));    //required
      tApartment.setType(DApartmentType.fromValue(ctx.getStrFromContext(type + "LocationApartmentType")));
      pAddressV1.setApartment(tApartment);
    }
    if (ctx.isStringVariableHasValue(type + "LocationOther")) {
      pAddressV1.setOther(ctx.getStrFromContext(type + "LocationOther"));
    }
    if (ctx.isStringVariableHasValue(type + "LocationNote")) {
      pAddressV1.setNote(ctx.getStrFromContext(type + "LocationNote"));
    }
    return pAddressV1;
  }

  private void createPaymentDocuments(RequestGKN.Request request) {
    long countPaymentDocuments = ctx.getLongFromContext("payment");
    if (countPaymentDocuments > 0) {
      final RequestGKN.Request.PaymentDocuments paymentsDocuments = new RequestGKN.Request.PaymentDocuments();
      for (int idx = 1; idx <= countPaymentDocuments; idx++) {
        TPayDocument appliedDocument = formPaymentDocument("payment", "_" + idx);
        paymentsDocuments.getPaymentDocument().add(appliedDocument);
      }
      request.setPaymentDocuments(paymentsDocuments);
    }
  }

  private TPayDocument formPaymentDocument(String prefix, String suffix) {
    TPayDocument payDocument = new TPayDocument();
    payDocument.setDocType(ctx.getStrFromContext(prefix + "Doc_Type" + suffix));
    payDocument.setNumber(ctx.getStrFromContext(prefix + "Number" + suffix));
    payDocument.setBIC(ctx.getStrFromContext(prefix + "BIC" + suffix));
    payDocument.setBankName(ctx.getStrFromContext(prefix + "Bank_Name" + suffix));
    payDocument.setOKATO(ctx.getStrFromContext(prefix + "OKATO" + suffix));
    payDocument.setSettlementAccount(ctx.getStrFromContext(prefix + "Settlement_Account" + suffix));
    payDocument.setSum(BigInteger.valueOf(ctx.getLongFromContext(prefix + "Sum" + suffix)));
    payDocument.setDate(date(ctx.getDateFromContext(prefix + "Date" + suffix)));
    payDocument.setFIOPayment(ctx.getStrFromContext(prefix + "FIOPayer" + suffix));
    payDocument.setQuantity(formQuantity(prefix, suffix));
    return payDocument;
  }

  private TQuantity formQuantity(String prefix, String suffix) {
    final TQuantity tQuantity = new TQuantity();           //required
    if (ctx.getLongFromContext(prefix + "ADocumentOriginalQuantity" + suffix) != null) {
      final TQuantityAttribute tQuantityOriginal = new TQuantityAttribute();
      tQuantityOriginal.setQuantity(ctx.getBigIntFromContext(prefix + "ADocumentOriginalQuantity" + suffix));
      tQuantityOriginal.setQuantitySheet(ctx.getBigIntFromContext(prefix + "ADocumentOriginalQuantitySheet" + suffix));
      tQuantity.setOriginal(tQuantityOriginal);
    }
    if (ctx.getVariable(prefix + "ADocumentCopyQuantity" + suffix) != null) {
      final TQuantityAttribute tQuantityCopy = new TQuantityAttribute();
      tQuantityCopy.setQuantity(ctx.getBigIntFromContext(prefix + "ADocumentCopyQuantity" + suffix));
      tQuantityCopy.setQuantitySheet(ctx.getBigIntFromContext(prefix + "ADocumentCopyQuantitySheet" + suffix));
      tQuantity.setCopy(tQuantityCopy);
    }
    return tQuantity;
  }

  private void createAppliedDocuments(RequestGKN.Request request) {
    long countAppliedDocuments = ctx.getLongFromContext("applied");
    if (countAppliedDocuments > 0) {
      final RequestGKN.Request.AppliedDocuments appliedDocuments = new RequestGKN.Request.AppliedDocuments();
      for (int idx = 1; idx <= countAppliedDocuments; idx++) {
        TAppliedDocument appliedDocument = formAppliedDocument("applied", "_" + idx);
        appliedDocuments.getAppliedDocument().add(appliedDocument);
      }
      request.setAppliedDocuments(appliedDocuments);
    }
  }

  TAppliedDocument formAppliedDocument(final String prefix, final String suffix) {
    final TAppliedDocument tAppliedDocument = new TAppliedDocument();
    tAppliedDocument.setCodeDocument(ctx.getStrFromContext(prefix + "ADocumentCode" + suffix));
    tAppliedDocument.setName(ctx.getStrFromContext(prefix + "ADocumentName" + suffix));
    tAppliedDocument.setNumber(ctx.getStrFromContext(prefix + "ADocumentNumber" + suffix));
    tAppliedDocument.setIssueOrgan(ctx.getStrFromContext(prefix + "AIssue" + suffix));
    tAppliedDocument.setDesc(ctx.getStrFromContext(prefix + "ADesc" + suffix));
    XMLGregorianCalendar appliedDocumentDate = date(ctx.getDateFromContext(prefix + "ADocumentDate" + suffix));
    tAppliedDocument.setDate(appliedDocumentDate);
       /* final TImages tImages = new TImages();
        final TImage tImage = new TImage();
        tImage.setName(getStringFromContext( ctx, type + "ADocumentImageName"));
        tImage.setURL(getStringFromContext( ctx, type + "ADocumentImageURL"));
        tImages.getImage().add(tImage);
        tAppliedDocument.setImages(tImages);

        */
    final TQuantity tQuantity = formQuantity(prefix, suffix);

    tAppliedDocument.setQuantity(tQuantity);

    return tAppliedDocument;
  }

  protected abstract RequestGKN.Request.RequiredData createRequiredData();

  RequestGKN buildGKNObject(String id) {
    return createGKNRequest(id);
  }

  public String createEnclosure(String id) {
    RequestGKN requestGKN = buildGKNObject(id);
    return gknToString(requestGKN);
  }
}
