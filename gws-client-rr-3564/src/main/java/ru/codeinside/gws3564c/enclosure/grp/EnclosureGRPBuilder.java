/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws3564c.enclosure.grp;

import ru.codeinside.gws.api.ExchangeContext;
import ru.codeinside.gws3564c.DeclarantType;
import ru.codeinside.gws3564c.context.TypedContext;
import ru.codeinside.gws3564c.enclosure.EnclosureRequestBuilder;
import ru.grp.*;

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
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


import static ru.grp.RequestGRP.Request.Payment.PaymentDocuments;

/**
 * Строит вложение к запросам в рег палату
 */
public abstract class EnclosureGRPBuilder implements EnclosureRequestBuilder {
  public static final String SCHEMA_LOCATION = "rr_xsd/RequestGRP_V01/STD_RequestGRP.xsd";
  private final Logger logger = Logger.getLogger(getClass().getName());
  protected TypedContext ctx;

  public EnclosureGRPBuilder(ExchangeContext ctx) {
    this.ctx = new TypedContext(ctx);
  }

  RequestGRP.EDocument eDocument(String id) {
    final RequestGRP.EDocument eDocument = new RequestGRP.EDocument();
    eDocument.setVersion("1.16");
    eDocument.setGUID(id);
    return eDocument;
  }

  RequestGRP createRequest(String id) {
    final RequestGRP requestGRP = new RequestGRP();
    requestGRP.setEDocument(eDocument(id));
    final RequestGRP.Request request = new RequestGRP.Request();
    request.setRequiredData(createRequiredData());
    request.setDeclarant(formDeclarant());
    request.setPayment(formPayment());
    request.setDelivery(formDelivery());
    final RequestGRP.Request.AppliedDocuments appliedDocuments = new RequestGRP.Request.AppliedDocuments();
    fillAppliedDocument(appliedDocuments);
    request.setAppliedDocuments(appliedDocuments);
    request.setMunicipalService(new RequestGRP.Request.MunicipalService());
    requestGRP.setRequest(request);
    return requestGRP;
  }

  TDelivery formDelivery() {
    final TDelivery tDelivery = new TDelivery();
    tDelivery.setWebService(true);
    return tDelivery;
  }

  protected abstract RequestGRP.Request.RequiredData createRequiredData();

  RequestGRP.Request.Payment formPayment() {
    final RequestGRP.Request.Payment payment = new RequestGRP.Request.Payment();
    long countFreeDocuments = ctx.getLongFromContext("freePayment");
    long countPaymentDocuments = ctx.getLongFromContext("payment");
    boolean isPaymentFree = getBoolFromContext("isPaymentFree");
    if (isPaymentFree && countFreeDocuments > 0L) {
      RequestGRP.Request.Payment.ReasonFreeDocuments reasonFreeDocuments = new RequestGRP.Request.Payment.ReasonFreeDocuments();
      List<TAppliedDocument> result = getAppliedDocuments(countFreeDocuments, "freePayment");
      reasonFreeDocuments.getReasonFreeDocument().addAll(result);
      payment.setReasonFreeDocuments(reasonFreeDocuments);
    }
    if (!isPaymentFree && countPaymentDocuments > 0l) {
      PaymentDocuments documents = new PaymentDocuments();
      payment.setPaymentDocuments(documents);
      documents.getPaymentDocument().addAll(getPaymentDocuments(countPaymentDocuments, "payment"));
    }
    return payment;
  }

  private List<TPayDocument> getPaymentDocuments(long countPaymentDocuments, String prefix) {
    final LinkedList<TPayDocument> result = new LinkedList<TPayDocument>();
    for (int idx = 1; idx <= countPaymentDocuments; idx++) {
      String suffix = "_" + idx;
      result.add(fillPaymentDocument(prefix, suffix));
    }
    return result;
  }

  private TPayDocument fillPaymentDocument(String prefix, String suffix) {
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

  private List<TAppliedDocument> getAppliedDocuments(long countFreeDocuments, String prefix) {
    List<TAppliedDocument> result = new LinkedList<TAppliedDocument>();
    for (int idx = 1; idx <= countFreeDocuments; idx++) {
      String suffix = "_" + idx;
      final TAppliedDocument document = formAppliedDocument(prefix, suffix);
      result.add(document);
    }
    return result;
  }

  private boolean getBoolFromContext(String varName) {
    return (Boolean) ctx.getVariable(varName);
  }

  TAppliedDocument formAppliedDocument(final String prefix, String suffix) {
    final String entityName = "ADocument";
    if (!ctx.hasAtLeastOneVariableWithPrefix(prefix + entityName)) return null;
    final TAppliedDocument tAppliedDocument = new TAppliedDocument();
    tAppliedDocument.setCodeDocument(ctx.getStrFromContext(buildVarName(prefix, suffix, entityName + "Code")));
    tAppliedDocument.setName(ctx.getStrFromContext(buildVarName(prefix, suffix, entityName + "Name")));
    tAppliedDocument.setNumber(ctx.getStrFromContext(buildVarName(prefix, suffix, entityName + "Number")));
    tAppliedDocument.setIssueOrgan(ctx.getStrFromContext(buildVarName(prefix, suffix, entityName + "Issue")));
    tAppliedDocument.setDesc(ctx.getStrFromContext(buildVarName(prefix, suffix, entityName + "Desc")));
    tAppliedDocument.setNumberReg(ctx.getStrFromContext(buildVarName(prefix, suffix, entityName + "NumberReg")));
    tAppliedDocument.setRegister(ctx.getStrFromContext(buildVarName(prefix, suffix, entityName + "Register")));
    tAppliedDocument.setSeries(ctx.getStrFromContext(buildVarName(prefix, suffix, entityName + "Series")));
    XMLGregorianCalendar appliedDocumentDate = date(ctx.getDateFromContext(buildVarName(prefix,
                                                                                    suffix,
                                                                                    entityName + "Date")));
    tAppliedDocument.setDate(appliedDocumentDate);
    final TQuantity tQuantity = formQuantity(prefix, suffix);
    tAppliedDocument.setQuantity(tQuantity);
    return tAppliedDocument;
  }

  private static String buildVarName(String prefix, String suffix, String varName) {
    return prefix + varName + suffix;
  }

  private TQuantity formQuantity(String prefix, String suffix) {
    final Long originalQuantity = ctx.getLongFromContext(prefix +
        "ADocumentOriginalQuantity" + suffix);
    final Long copyQuantity = ctx.getLongFromContext(prefix +
        "ADocumentCopyQuantity" + suffix);
    if (originalQuantity == 0 && copyQuantity == 0) return null;
    TQuantity quantity = new TQuantity();
    if (originalQuantity > 0) {
      TQuantityAttribute original = new TQuantityAttribute();
      original.setQuantity(BigInteger.valueOf(originalQuantity));
      original.setQuantitySheet(BigInteger.valueOf(ctx.getLongFromContext(prefix +
                                                                          "ADocumentOriginalQuantitySheet" + suffix)));
      quantity.setOriginal(original);
    }

    if (copyQuantity > 0) {
      TQuantityAttribute copy = new TQuantityAttribute();
      copy.setQuantity(BigInteger.valueOf(copyQuantity));
      copy.setQuantitySheet(BigInteger.valueOf(ctx.getLongFromContext(prefix +
                                                                      "ADocumentCopyQuantitySheet" + suffix)));
      quantity.setCopy(copy);
    }
    return quantity;
  }

  private void fillAppliedDocument(RequestGRP.Request.AppliedDocuments appliedDocuments) {
    long countAppliedDocuments = ctx.getLongFromContext("applied");
    if (countAppliedDocuments > 0) {
      appliedDocuments.getAppliedDocument().addAll(getAppliedDocuments(countAppliedDocuments, "applied"));
    }
  }

  public String createEnclosure(String id) {
    final RequestGRP requestGRP = createRequest(id);
    return grpToString(requestGRP);
  }

  String grpToString(RequestGRP requestGRP) {
    try {
      SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
      URL resource = getClass().getClassLoader().getResource(SCHEMA_LOCATION);
      final Schema schema = sf.newSchema(resource);
      JAXBContext jc = JAXBContext.newInstance(RequestGRP.class);
      final Marshaller marshaller = jc.createMarshaller();
      final StringWriter stringWriter = new StringWriter();
      marshaller.setSchema(schema);
      marshaller.marshal(requestGRP, stringWriter);
      return stringWriter.toString();
    } catch (Exception e) {
      logger.log(Level.SEVERE, e.getMessage(), e);
      printContextVars();
      throw new RuntimeException(e);
    }
  }

  private void printContextVars() {
    for (String varName : ctx.getVariableNames()){
      logger.log(Level.SEVERE, String.format("%s => %s", varName, ctx.getVariable(varName)));
    }
  }

  PAddressV1 createLocation(final String prefix, String suffix) {
    if (!ctx.hasAtLeastOneVariableWithPrefix(prefix)) return null;
    final PAddressV1 pAddressV1 = new PAddressV1();
    if (ctx.isStringVariableHasValue(prefix + "PostalCode" + suffix)) {
      pAddressV1.setPostalCode(ctx.getStrFromContext(prefix + "PostalCode" + suffix));
    }
    pAddressV1.setRegion(ctx.getStrFromContext(prefix + "Region" + suffix));

    if (ctx.isStringVariableHasValue(prefix + "OKATO" + suffix)) {
      pAddressV1.setCodeOKATO(ctx.getStrFromContext(prefix + "OKATO" + suffix));
    }
    if (ctx.isStringVariableHasValue(prefix + "CLADR" + suffix)) {
      pAddressV1.setCodeKLADR(ctx.getStrFromContext(prefix + "CLADR" + suffix));
    }

    if (ctx.isStringVariableHasValue(prefix + "DistrictName" + suffix)) {
      final TDistrict district = new TDistrict();
      district.setName(ctx.getStrFromContext(prefix + "DistrictName" + suffix));
      district.setType(DDistrict.fromValue(ctx.getStrFromContext(prefix + "DistrictType" + suffix)));
      pAddressV1.setDistrict(district);
    }

    if (ctx.isStringVariableHasValue(prefix + "CityName" + suffix)) {
      final TCity tCity = new TCity();
      tCity.setName(ctx.getStrFromContext(prefix + "CityName" + suffix));
      tCity.setType(DCity.fromValue(ctx.getStrFromContext(prefix + "DCity" + suffix)));
      pAddressV1.setCity(tCity);
    }
    if (ctx.isStringVariableHasValue(prefix + "UrbanDistictName" + suffix)) {
      final TUrbanDistrict tCity = new TUrbanDistrict();
      tCity.setName(ctx.getStrFromContext(prefix + "UrbanDistictName" + suffix));
      tCity.setType(DUrbanDistrict.fromValue(ctx.getStrFromContext(prefix + "UrbanDistictType" + suffix)));
      pAddressV1.setUrbanDistrict(tCity);
    }
    if (ctx.isStringVariableHasValue(prefix + "SovietVillageName" + suffix)) {
      final TSovietVillage sovietVillage = new TSovietVillage();
      sovietVillage.setName(ctx.getStrFromContext(prefix + "SovietVillageName" + suffix));
      sovietVillage.setType(DSovietVillage.fromValue(ctx.getStrFromContext(prefix + "SovietVillageType" + suffix)));
      pAddressV1.setSovietVillage(sovietVillage);
    }
    if (ctx.isStringVariableHasValue(prefix + "LocalityName" + suffix)) {
      TLocality value = new TLocality();
      value.setName(ctx.getStrFromContext(prefix + "LocalityName" + suffix));
      value.setType(DInhabitedLocalities.fromValue(ctx.getStrFromContext(prefix + "LocalityType" + suffix)));
      pAddressV1.setLocality(value);
    }
    if (ctx.isStringVariableHasValue(prefix + "StreetName" + suffix)) {
      final TStreet tStreet = new TStreet();
      tStreet.setName(ctx.getStrFromContext(prefix + "StreetName" + suffix));
      tStreet.setType(DStreets.fromValue(ctx.getStrFromContext(prefix + "DStreets" + suffix)));
      pAddressV1.setStreet(tStreet);
    }
    if (ctx.isStringVariableHasValue(prefix + "LocationLevel1Type" + suffix)) {
      final TLevel1 tLevel1 = new TLevel1();
      tLevel1.setValue(ctx.getStrFromContext(prefix + "LocationLevel1Value" + suffix));
      tLevel1.setType(DLocationLevel1Type.fromValue(ctx.getStrFromContext(prefix + "LocationLevel1Type" + suffix)));
      pAddressV1.setLevel1(tLevel1);
    }
    if (ctx.isStringVariableHasValue(prefix + "LocationLevel2Type" + suffix)) {
      final TLevel2 tLevel2 = new TLevel2();
      tLevel2.setValue(ctx.getStrFromContext(prefix + "LocationLevel2Value" + suffix));
      tLevel2.setType(DLocationLevel2Type.fromValue(ctx.getStrFromContext(prefix + "LocationLevel2Type" + suffix)));
      pAddressV1.setLevel2(tLevel2);
    }
    if (ctx.isStringVariableHasValue(prefix + "LocationLevel3Type" + suffix)) {
      final TLevel3 tLevel3 = new TLevel3();
      tLevel3.setValue(ctx.getStrFromContext(prefix + "LocationLevel3Value" + suffix));
      tLevel3.setType(DLocationLevel3Type.fromValue(ctx.getStrFromContext(prefix + "LocationLevel3Type" + suffix)));
      pAddressV1.setLevel3(tLevel3);
    }
    if (ctx.isStringVariableHasValue(prefix + "LocationApartmentType" + suffix)) {
      final TApartment tApartment = new TApartment();
      tApartment.setValue(ctx.getStrFromContext(prefix + "LocationApartmentValue" + suffix));    //required
      tApartment.setType(DApartmentType.fromValue(ctx.getStrFromContext(prefix + "LocationApartmentType" + suffix)));
      pAddressV1.setApartment(tApartment);
    }
    if (ctx.isStringVariableHasValue(prefix + "LocationOther" + suffix)) {
      pAddressV1.setOther(ctx.getStrFromContext(prefix + "LocationOther" + suffix));
    }
    if (ctx.isStringVariableHasValue(prefix + "LocationNote" + suffix)) {
      pAddressV1.setNote(ctx.getStrFromContext(prefix + "LocationNote" + suffix));
    }
    return pAddressV1;
  }

  protected void fillPersonOwner(TPersonOwner result, String prefix, String suffix) {
    fillPPersonV1(prefix, result, suffix);
    result.setLocationTemporary(createLocation("ownerTemporaryLocation", suffix));
    result.setLocation(createLocation("ownerLocation", suffix));
    result.setLocationPermanent(createLocation("ownerPermanentLocation", suffix));
  }

  private void fillPPersonV1(String prefix, OriginalPPersonV1 result, String suffix) {
    result.setFIO(fio(prefix, suffix));
    result.setDateBirth(date(ctx.getDateFromContext(prefix + "DateBirth" + suffix)));
    result.setPlaceBirth(ctx.getStrFromContext(prefix + "PlaceBirth" + suffix));
    result.setSNILS(ctx.getStrFromContext(prefix + "SNILS" + suffix));
    result.setContactInfo(ctx.getStrFromContext(prefix + "ContactInfo" + suffix));
    result.setCitizenship(ctx.getStrFromContext(prefix + "CitizenShip" + suffix));
    result.setFamilyStatus(ctx.getStrFromContext(prefix + "FamilyStatus" + suffix));
    result.setPhone(ctx.getStrFromContext(prefix + "Phone" + suffix));
    result.setEMail(ctx.getStrFromContext(prefix + "Email" + suffix));
    result.setDocument(formPDocument(prefix, suffix));
    result.setLocationTemporary(createLocation(prefix + "TemporaryLocation", suffix));
    result.setLocationPermanent(createLocation(prefix + "PermanentLocation", suffix));
    result.setLocation(createLocation(prefix + "Location", suffix));
    result.setDocument(formPDocument(prefix, suffix));
  }


  TFIO fio(final String type, String suffix) {
    final TFIO tFIO = new TFIO();
    tFIO.setSurname(ctx.getStrFromContext(type + "FIOSurname" + suffix));
    tFIO.setFirst(ctx.getStrFromContext(type + "FIOFirst" + suffix));
    tFIO.setPatronymic(ctx.getStrFromContext(type + "FIOPatronymic" + suffix));
    return tFIO;
  }

  PIdentityPersonDocumentV1 formPDocument(final String prefix, String suffix) {
    final PIdentityPersonDocumentV1 pIdentityPersonDocument = new PIdentityPersonDocumentV1();
    pIdentityPersonDocument.setCodeDocument(ctx.getStrFromContext(prefix + "PDocumentCode" + suffix));
    pIdentityPersonDocument.setSeries(ctx.getStrFromContext(prefix + "PDocumentSeries" + suffix));
    pIdentityPersonDocument.setNumber(ctx.getStrFromContext(prefix + "PDocumentNumber" + suffix));
    pIdentityPersonDocument.setDesc(ctx.getStrFromContext(prefix + "PDocumentDesc" + suffix));
    XMLGregorianCalendar date = date(ctx.getDateFromContext(prefix + "PDocumentDate" + suffix));

    pIdentityPersonDocument.setDate(date);
    pIdentityPersonDocument.setIssueOrgan(ctx.getStrFromContext(prefix + "PDocumentIssueOrgan" + suffix));
    return pIdentityPersonDocument;
  }

  TDeclarant formDeclarant() {
    final TDeclarant declarant = new TDeclarant();
    DeclarantType declarantType = DeclarantType.valueOf(ctx.getStrFromContext("declarantType"));
    switch (declarantType) {
      case GOVERNANCE: {
        final TGovernanceDeclarant governance = new TGovernanceDeclarant();
        governance.setEMail(ctx.getStrFromContext("declGovernanceEmail"));
        governance.setName(ctx.getStrFromContext("declGovernanceName"));
        governance.setGovernanceCode(ctx.getStrFromContext("declGovernanceCode"));
        governance.setPhone(ctx.getStrFromContext("declGovernancePhone"));
        governance.setContactInfo(ctx.getStrFromContext("declGovernanceContactInfo"));
        governance.setLocation(createLocation("declLocation", ""));
        governance.setAgent(formAgent());
        declarant.setGovernance(governance);
        break;

      }
      case PERSON: {
        TDeclarant.Person person = formPerson("declPerson");
        declarant.setPerson(person);
        person.setAgent(formAgent());
        break;
      }

      case ORGANISATION:
        final TOrganizationDeclarant value = new TOrganizationDeclarant();
        formOrganisation("declLegalPerson", value);
        value.setAgent(formAgent());
        declarant.setOrganization(value);
        break;
    }
    declarant.setDeclarantKind(ctx.getStrFromContext("declKind"));
    return declarant;
  }

  private TDeclarant.Person formPerson(String prefix) {
    final TDeclarant.Person person = new TDeclarant.Person();
    fillPPersonV1(prefix, person, "");
    return person;
  }

  TAgent formAgent() {
    final TAgent agent = new TAgent();
    fillPPersonV1("agent", agent, "");
    agent.setAgentKind(ctx.getStrFromContext("agentKind"));
    agent.setIdDbEgrp(ctx.getStrFromContext("agentIdDbEgrp"));
    agent.setAttorneyDocument(formAppliedDocument("agentDocument", ""));
    return agent;
  }

  void formOrganisation(String type, POrganizationV1 organization) {
    organization.setName(ctx.getStrFromContext(type + "Name"));
    organization.setCodeOPF(ctx.getStrFromContext(type + "OPF"));
    organization.setCodeCPP(ctx.getStrFromContext(type + "KPP"));
    organization.setINN(ctx.getStrFromContext(type + "INN"));
    organization.setCodeOGRN(ctx.getStrFromContext(type + "OGRN"));
    organization.setEMail(ctx.getStrFromContext(type + "Email"));
    organization.setPhone(ctx.getStrFromContext(type + "Phone"));
    organization.setContactInfo(ctx.getStrFromContext(type + "ContactInfo"));
    organization.setRegDate(date(ctx.getDateFromContext(type + "RegDate")));
    organization.setRegistrationAgency(ctx.getStrFromContext(type + "RegAgency"));
    organization.setDocument(formOrganizationDocument("declLegalPersonDocument"));
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

  private PIdentityOrganizationDocumentV1 formOrganizationDocument(String type) {
    PIdentityOrganizationDocumentV1 result = new PIdentityOrganizationDocumentV1();
    result.setCodeDocument(ctx.getStrFromContext(type + "Code"));
    result.setName(ctx.getStrFromContext(type + "Name"));
    result.setSeries(ctx.getStrFromContext(type + "Series"));
    result.setNumber(ctx.getStrFromContext(type + "Number"));
    result.setDate(date(ctx.getDateFromContext(type + "Date")));
    result.setIssueOrgan(ctx.getStrFromContext(type + "IssueOrgan"));
    result.setDesc(ctx.getStrFromContext(type + "Desc"));
    return result;
  }
  protected void fillRoomDataFromContext(TExtractRoom room, String suffix) {
    RoomKind roomKind = RoomKind.valueOf(ctx.getStrFromContext("objectRoomKind" + suffix));
    switch (roomKind) {
      case ROOM:
        room.setIsRoom(true);
        break;
      case NONDOMESTIC:
        room.setIsNondomestic(true);
        break;
      case FLAT:
        room.setIsFlat(true);
        break;
    }
  }

  enum RoomKind {
    FLAT, NONDOMESTIC, ROOM
  }

  protected void fillBuildingFromContext(TExtractBuilding buildingRequired, String suffix) {
    BuildingKind type = BuildingKind.valueOf(ctx.getStrFromContext("buildingKind" + suffix));
    switch (type) {
      case NONDOMESTIC:
        buildingRequired.setIsNondomestic(true);
        break;
      case LIVING:
        buildingRequired.setIsLiving(true);
        break;
      case APARTMENTS:
        buildingRequired.setIsApartments(true);
        break;
    }
  }

  enum BuildingKind {
    NONDOMESTIC, LIVING, APARTMENTS
  }

}
