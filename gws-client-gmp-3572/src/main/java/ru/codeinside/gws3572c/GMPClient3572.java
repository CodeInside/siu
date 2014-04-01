/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws3572c;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import ru.codeinside.gws.api.*;
import ru.roskazna.xsd.bill.Bill;
import ru.roskazna.xsd.budgetindex.BudgetIndex;
import ru.roskazna.xsd.charge.ChargeType;
import ru.roskazna.xsd.common.AddressType;
import ru.roskazna.xsd.common.ContactInfoType;
import ru.roskazna.xsd.doacknowledgmentrequest.DoAcknowledgmentRequestType;
import ru.roskazna.xsd.doacknowledgmentresponse.DoAcknowledgmentResponseType;
import ru.roskazna.xsd.exportpaymentsresponse.ExportPaymentsResponse;
import ru.roskazna.xsd.exportquittanceresponse.ExportQuittanceResponse;
import ru.roskazna.xsd.organization.Account;
import ru.roskazna.xsd.organization.Bank;
import ru.roskazna.xsd.organization.Organization;
import ru.roskazna.xsd.paymentinfo.PaymentIdentificationDataType;
import ru.roskazna.xsd.paymentinfo.PaymentInfoType;
import ru.roskazna.xsd.paymentinfo.PaymentType;
import ru.roskazna.xsd.pgu_chargesresponse.ExportChargesResponse;
import ru.roskazna.xsd.pgu_datarequest.DataRequest;
import ru.roskazna.xsd.pgu_importrequest.ImportRequest;
import ru.roskazna.xsd.postblock.PostBlock;
import ru.roskazna.xsd.ticket.Ticket;
import ru.rosrazna.xsd.smevunifoservice.ExportData;
import ru.rosrazna.xsd.smevunifoservice.ExportDataResponse;
import ru.rosrazna.xsd.smevunifoservice.ImportData;
import ru.rosrazna.xsd.smevunifoservice.ImportDataResponse;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.math.BigInteger;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GMPClient3572 implements Client {


  public static final String IMPORT_CHARGE_OPERATION = "importCharge";
  public static final String EXPORT_OPERATION = "exportData";
  public static final String IMPORT_PAYMENT_OPERATION = "importPayment";
  public static final String DO_ACKNOWLEDGMENT_OPERATION = "DoAcknowledgmentRequest";
  protected CryptoProvider cryptoProvider;

  public static final String SMEV_INITIAL_REG_NUMBER = "smevInitialRegNumber";
  private static final String SMEV_INITIAL_REG_DATE = "smevInitialRegDate";
  private Logger logger = Logger.getLogger(getClass().getName());
  private static Map<String, String> PERSON_DOCUMENT_ID_MAP;
  private static Map<String, String> EXPORT_ENTITY_SCHEMA_MAP;
  static {
    PERSON_DOCUMENT_ID_MAP = new HashMap<String, String>();
    PERSON_DOCUMENT_ID_MAP.put("01", "payerPersonDocumentID2");
    PERSON_DOCUMENT_ID_MAP.put("02", "payerPersonDocumentID3");
    PERSON_DOCUMENT_ID_MAP.put("03", "payerPersonDocumentID4");
    PERSON_DOCUMENT_ID_MAP.put("04", "payerPersonDocumentID5");
    PERSON_DOCUMENT_ID_MAP.put("05", "payerPersonDocumentID6");
    PERSON_DOCUMENT_ID_MAP.put("06", "payerPersonDocumentID7");
    PERSON_DOCUMENT_ID_MAP.put("07", "payerPersonDocumentID8");
    PERSON_DOCUMENT_ID_MAP.put("08", "payerPersonDocumentID9");
    PERSON_DOCUMENT_ID_MAP.put("09", "payerPersonDocumentID10");
    PERSON_DOCUMENT_ID_MAP.put("10", "payerPersonDocumentID11");
    PERSON_DOCUMENT_ID_MAP.put("11", "payerPersonDocumentID12");
    PERSON_DOCUMENT_ID_MAP.put("12", "payerPersonDocumentID13");
    PERSON_DOCUMENT_ID_MAP.put("13", "payerPersonDocumentID14");
    PERSON_DOCUMENT_ID_MAP.put("21", "payerPersonDocumentID15");
    PERSON_DOCUMENT_ID_MAP.put("22", "payerPersonDocumentID16");
    PERSON_DOCUMENT_ID_MAP.put("23", "payerPersonDocumentID17");
    PERSON_DOCUMENT_ID_MAP.put("24", "payerPersonDocumentID18");

    EXPORT_ENTITY_SCHEMA_MAP = new HashMap<String, String>();
    EXPORT_ENTITY_SCHEMA_MAP.put("QUITTANCE", "http://roskazna.ru/xsd/ExportQuittanceResponse");
    EXPORT_ENTITY_SCHEMA_MAP.put("CHARGE", "http://roskazna.ru/xsd/PGU_ChargesResponse");
    EXPORT_ENTITY_SCHEMA_MAP.put("CHARGESTATUS", "http://roskazna.ru/xsd/PGU_ChargesResponse");
    EXPORT_ENTITY_SCHEMA_MAP.put("CHARGENOTFULLMATCHED", "http://roskazna.ru/xsd/PGU_ChargesResponse");
    EXPORT_ENTITY_SCHEMA_MAP.put("PAYMENT", "http://roskazna.ru/xsd/ExportPaymentsResponse");
    EXPORT_ENTITY_SCHEMA_MAP.put("PAYMENTUNMATCHED", "http://roskazna.ru/xsd/ExportPaymentsResponse");
    EXPORT_ENTITY_SCHEMA_MAP.put("PAYMENTMODIFIED", "http://roskazna.ru/xsd/ExportPaymentsResponse");
  }

  public Revision getRevision() {
    return ru.codeinside.gws.api.Revision.rev111111;
  }

  public URL getWsdlUrl() {
    return getClass().getClassLoader().getResource("gws3572/SmevUnifoService.wsdl");
  }

  public ClientRequest createClientRequest(ExchangeContext ctx) {
    final String originRequestId = (String) ctx.getVariable("smevOriginRequestId");


    final Packet packet = new Packet();
    packet.recipient = new InfoSystem("RKZN35001", "Казначейство России");
    InfoSystem pnzr8201 = new InfoSystem("8201", "Комплексная система предоставления государственных и муниципальных услуг Пензенской области");
    packet.sender = packet.originator = pnzr8201;

    packet.typeCode = Packet.Type.EXECUTION;

    packet.date = new Date();
    packet.exchangeType = "6"; // 1 - Запрос на оказание услуги

    packet.originRequestIdRef = originRequestId;
    packet.testMsg = (String) ctx.getVariable("smevTest");

    final ClientRequest request = new ClientRequest();

    BigInteger caseNumber = generateCaseNumber();
    packet.caseNumber = caseNumber.toString();
    ctx.setVariable(SMEV_INITIAL_REG_NUMBER, caseNumber);
    ctx.setVariable(SMEV_INITIAL_REG_DATE, new Date());
    request.packet = packet;
    request.action = new QName("http://roskazna.ru/SmevUnifoService/", "UnifoTransferMsg");
    packet.status = Packet.Status.REQUEST;
    try {
      String typeOperation = getStringFromContext(ctx, "operationType", "");
      if (IMPORT_CHARGE_OPERATION.equals(typeOperation)) {
        request.appData = createAppDataForImportChargeOperation(ctx);
      } else if (EXPORT_OPERATION.equals(typeOperation)) {
        request.appData = createAppDataForExportOperation(ctx);
      } else if (IMPORT_PAYMENT_OPERATION.equals(typeOperation)) {
        request.appData = createAppDataForImportPaymentOperation(ctx);
      } else if (DO_ACKNOWLEDGMENT_OPERATION.equals(typeOperation)) {
        request.appData = createAcknowledgmentRequest(ctx);
      } else {
        throw new IllegalArgumentException("Тип операции не верен");
      }
    } catch (Exception e) {
      logger.log(Level.SEVERE, e.getMessage(), e);
      throw new RuntimeException(e.getMessage(), e);
    }

    return request;
  }

  private String createAcknowledgmentRequest(ExchangeContext ctx) {
    final DoAcknowledgmentRequestType acknowledgmentRequest = new DoAcknowledgmentRequestType();
    String postBlockSenderIdentifier = getStringFromContext(ctx, "postBlockSenderIdentifier", "");
    final PostBlock postBlock = createPostBlock(ctx, postBlockSenderIdentifier);
    acknowledgmentRequest.setPostBlock(postBlock);
    acknowledgmentRequest.setSupplierBillID(getStringFromContext(ctx, "SupplierBillID", ""));
    long countPayments = (Long)ctx .getVariable("paymentBlock");
    if (countPayments > 0l){
      acknowledgmentRequest.setPayments(new DoAcknowledgmentRequestType.Payments());
      for (long paymentIdx = 0; paymentIdx < countPayments; paymentIdx ++){
        String paymentId = getStringFromContext(ctx, "paymentSystemId_" + (paymentIdx + 1), "");
        acknowledgmentRequest.getPayments().getPaymentSystemIdentifier().add(paymentId);
      }
    }
    return new XmlTypes(DoAcknowledgmentRequestType.class).toXml(acknowledgmentRequest);
  }

  private String createAppDataForImportPaymentOperation(ExchangeContext ctx) throws Exception {
    ImportData importData = new ImportData();
    final ImportRequest importRequest = new ImportRequest();
    importData.setImportRequest(importRequest);
    String postBlockSenderIdentifier = (String) ctx.getVariable("postBlockSenderIdentifier");
    final PostBlock postBlock = createPostBlock(ctx, postBlockSenderIdentifier);
    importRequest.setPostBlock(postBlock);
    PaymentInfoType paymentInfo = new PaymentInfoType();
    importRequest.setFinalPayment(paymentInfo);

    paymentInfo.setNarrative(getStringFromContext(ctx, "paymentNarrative", ""));
    paymentInfo.setSupplierBillID(getStringFromContext(ctx, "paymentSupplierBillID", ""));
    paymentInfo.setAmount(toLong((String) ctx.getVariable("paymentAmount")));
    paymentInfo.setPaymentDate(XmlTypes.date(formatDate((Date) ctx.getVariable("paymentDate"), "dd.MM.yyyy")));
    paymentInfo.setChangeStatus(getStringFromContext(ctx, "paymentChangeStatus", "1"));
    paymentInfo.setPayeeINN(getStringFromContext(ctx, "paymentPayeeINN", ""));
    paymentInfo.setPayeeKPP(getStringFromContext(ctx, "paymentPayeeKPP", ""));
    paymentInfo.setKBK(getStringFromContext(ctx, "paymentKBK", ""));
    paymentInfo.setOKATO(getStringFromContext(ctx, "paymentOKATO", ""));
    paymentInfo.setPayerPA(getStringFromContext(ctx, "paymentPA", ""));

    Account payeeBankAcc = new Account();
    paymentInfo.setPayeeBankAcc(payeeBankAcc);
    payeeBankAcc.setAccount(getStringFromContext(ctx, "paymentPayeeAccount", ""));
    payeeBankAcc.setKind(getStringFromContext(ctx, "paymentPayeeBankAccKind", "!"));
    Bank payeeBank = new Bank();
    payeeBankAcc.setBank(payeeBank);
    payeeBank.setName(getStringFromContext(ctx, "paymentPayeeBankName", ""));
    payeeBank.setCorrespondentBankAccount(getStringFromContext(ctx, "paymentCorrespondentBankAccount", ""));
    payeeBank.setBIK(getStringFromContext(ctx, "paymentPayeeBankBIK", ""));

    PaymentIdentificationDataType paymentIdentificationData = new PaymentIdentificationDataType();
    paymentInfo.setPaymentIdentificationData(paymentIdentificationData);
    Bank bank = new Bank();
    paymentIdentificationData.setBank(bank);
    bank.setBIK(getStringFromContext(ctx, "paymentBIK", ""));
    bank.setName(getStringFromContext(ctx, "paymentBankName", ""));
    paymentIdentificationData.setSystemIdentifier(buildSystemIdentifierForImportPaymentOperation(ctx));

    fillPaymentAdditionalData(paymentInfo, ctx);
    final BudgetIndex budgetIndex = new BudgetIndex();
    paymentInfo.setBudgetIndex(budgetIndex);
    budgetIndex.setStatus(getStringFromContext(ctx, "Status", "0" )); //Статус плательщика
    budgetIndex.setPaymentType(getStringFromContext(ctx, "PaymentType", "0" )); // тип платежа
    budgetIndex.setPurpose(getStringFromContext(ctx, "Purpose", "0" )); // основание платежа 2 символа максимум
    budgetIndex.setTaxPeriod(getStringFromContext(ctx, "TaxPeriod", "0" )); // налоговый период до 10 символов
    budgetIndex.setTaxDocNumber(getStringFromContext(ctx, "TaxDocNumber", "0" )); // Показатель номера документа
    budgetIndex.setTaxDocDate(getStringFromContext(ctx, "TaxDocDate", "0" ));   // Показатель даты документа

    paymentInfo.setPayerIdentifier(buildUnifiedPayerID(ctx, ""));

    String importDataStr = new XmlTypes(ImportData.class).toXml(importData);
    return cryptoProvider.signElement(importDataStr, "FinalPayment", null, true, true, false);
  }

  private String buildSystemIdentifierForImportPaymentOperation(ExchangeContext ctx) {
    String bik = getStringFromContext(ctx, "paymentBIK", "");
    Long branchId = (Long)ctx.getVariable( "paymentBankBranch");
    Date paymentDate = (Date)ctx.getVariable("paymentDate");
    if (ctx.getVariable("paymentId") == null) {
        throw new RuntimeException("Уникальный номер платежа внутри отделения в теч. дня должен быть заполнен");
    }
    Long paymentId = (Long)ctx.getVariable("paymentId");

    String branchPart = leftPad(branchId.toString(), 6, '0');
    String datePaymentPart = new SimpleDateFormat("yyMMdd").format(paymentDate);
    String paymentIdPart = leftPad(paymentId.toString(), 10, '0');
    return "1" + bik + branchPart + datePaymentPart + paymentIdPart;
  }

   String leftPad(String sourceString, int expectedLength, char fillChar) {
    String strForPad = "";
    if (sourceString.length() < expectedLength) {
      for(int idx = 0; idx < expectedLength - sourceString.length(); idx++){
        strForPad += fillChar;
      }
    }
    return strForPad + sourceString;
  }

  private void fillPaymentAdditionalData(PaymentInfoType paymentInfo, ExchangeContext ctx) {
    Long valuesCount = (Long)ctx.getVariable("payeeAddDataBlock");
    for (long idx = 0; idx < valuesCount; idx++) {
      PaymentType.AdditionalData additionalData = new PaymentType.AdditionalData();
      additionalData.setName(getStringFromContext(ctx, "payeeAddDataKey_"+ (idx +1), "" ));
      additionalData.setValue(getStringFromContext(ctx, "payeeAddDataValue_" + (idx +1), "" ));
      paymentInfo.getAdditionalData().add(additionalData);
    }
  }

  String createAppDataForExportOperation(ExchangeContext ctx) {
    ExportData exportData = new ExportData();
    DataRequest dataRequest = new DataRequest();
    Date startDate = (Date) ctx.getVariable("startDate");
    if (startDate != null) {
      dataRequest.setStartDate(XmlTypes.dateTimeAndZeroMilliseconds(formatDate(startDate, "dd.MM.yyyy HH:mm:ss")));
    }

    Date endDate = (Date) ctx.getVariable("endDate");
    if (endDate != null) {
      dataRequest.setEndDate(XmlTypes.dateTimeAndZeroMilliseconds(formatDate(endDate, "dd.MM.yyyy HH:mm:ss")));
    }
    exportData.setDataRequest(dataRequest);
    dataRequest.setKind((String) ctx.getVariable("exportRequestType"));
    String postBlockSenderIdentifier = (String) ctx.getVariable("postBlockSenderIdentifier");
    final PostBlock postBlock = createPostBlock(ctx, postBlockSenderIdentifier);
    dataRequest.setPostBlock(postBlock);
    fillSupplierBillIds(ctx, dataRequest);
    fillApplicationIds(ctx, dataRequest);
    fillUnifiedPayerIds(ctx, dataRequest);
    return new XmlTypes(ExportData.class).toXml(exportData);
  }

  private void fillUnifiedPayerIds(ExchangeContext ctx, DataRequest dataRequest) {
    if (ctx.getVariable("payerIdBlock") == null) return;
    Long countPayers = (Long) ctx.getVariable("payerIdBlock");
    if (countPayers > 0) {
      DataRequest.Payers payers = new DataRequest.Payers();
      dataRequest.setPayers(payers);

      for (int idx = 0; idx < countPayers; idx++) {
        payers.getPayerIdentifier().add(buildUnifiedPayerID(ctx, "_" + (idx + 1)));
      }
    }
  }

  private void fillApplicationIds(ExchangeContext ctx, DataRequest dataRequest) {
    if (ctx.getVariable("ApplicationIdBlock") == null) return;
    Long countApplicationIds = (Long) ctx.getVariable("ApplicationIdBlock");
    if (countApplicationIds > 0) {
      DataRequest.ApplicationIDs applicationIDs = new DataRequest.ApplicationIDs();
      dataRequest.setApplicationIDs(applicationIDs);

      for (int idx = 0; idx < countApplicationIds; idx++) {
        Object supplierBillID = ctx.getVariable("ApplicationId_" + (idx + 1));
        if (supplierBillID != null)
          applicationIDs.getApplicationID().add((String) supplierBillID);
      }
    }
  }

  private void fillSupplierBillIds(ExchangeContext ctx, DataRequest dataRequest) {
    if (ctx.getVariable("SupplierBillIDBlock") == null) return;
    Long countSupplierBillIds = (Long) ctx.getVariable("SupplierBillIDBlock");
    if (countSupplierBillIds > 0) {
      DataRequest.SupplierBillIDs supplierBillIDs = new DataRequest.SupplierBillIDs();
      dataRequest.setSupplierBillIDs(supplierBillIDs);

      for (int idx = 0; idx < countSupplierBillIds; idx++) {
        Object supplierBillID = ctx.getVariable("SupplierBillID_" + (idx + 1));
        if (supplierBillID != null)
          supplierBillIDs.getSupplierBillID().add((String) supplierBillID);
      }
    }
  }

  String createAppDataForImportChargeOperation(ExchangeContext ctx) throws Exception {
    ImportData importData = new ImportData();
    final ImportRequest importRequest = new ImportRequest();
    String postBlockSenderIdentifier = (String) ctx.getVariable("postBlockSenderIdentifier");
    final PostBlock postBlock = createPostBlock(ctx, postBlockSenderIdentifier);
    importRequest.setPostBlock(postBlock);
    final ChargeType charge = new ChargeType();
    importRequest.setCharge(charge);

    String ordinalNumber = (String) ctx.getVariable("ordinalNumber");
    if (ordinalNumber == null || "".equals(ordinalNumber)) {
      ordinalNumber = formatDate(new Date(), "ddMMyyHHmmss");
    }

    String supplierBillID = UINGenerator.generateChargeUIN(postBlockSenderIdentifier, ordinalNumber);
    ctx.setVariable("supplierBillID", supplierBillID);
    charge.setSupplierBillID(supplierBillID); // Уникальный идентификатор счёта в ИСП
    charge.setBillDate(XmlTypes.date(formatDate((Date) ctx.getVariable("chargeBillDate"), "dd.MM.yyyy"))); //Дата выставления счёта

    final Organization supplierOrgInfo = new Organization();
    supplierOrgInfo.setName((String) ctx.getVariable("supplierOrgInfoName"));
    supplierOrgInfo.setINN((String) ctx.getVariable("supplierOrgInfoINN"));
    supplierOrgInfo.setKPP((String) ctx.getVariable("supplierOrgInfoKPP"));
    final Account account = new Account();
    account.setAccount((String) ctx.getVariable("accountAccount"));
    account.setKind((String) ctx.getVariable("accountKind"));
    final Bank bank = new Bank();
    bank.setBIK((String) ctx.getVariable("bankBIK"));
    bank.setName((String) ctx.getVariable("bankName"));
    account.setBank(bank);
    supplierOrgInfo.setAccount(account);
    charge.setSupplierOrgInfo(supplierOrgInfo);
    charge.setBillFor((String) ctx.getVariable("chargeBillFor"));
    charge.setTotalAmount(toLong((String) ctx.getVariable("chargeTotalAmount")));
    charge.setChangeStatus((String) ctx.getVariable("chargeChangeStatus")); /* Статус счёта 1 - новый  2 - изменение  3 - аннулирование */
    charge.setTreasureBranch((String) ctx.getVariable("chargeTreasureBranch")); // Орган ФК, на счёт которого должны поступать средства плательщика
    charge.setKBK((String) ctx.getVariable("chargeKBK"));
    charge.setOKATO((String) ctx.getVariable("chargeOKATO"));


    final BudgetIndex budgetIndex = new BudgetIndex();
    budgetIndex.setStatus((String) ctx.getVariable("budgetIndexStatus")); //Статус плательщика
    budgetIndex.setPaymentType((String) ctx.getVariable("budgetPaymentType")); // тип платежа
    budgetIndex.setPurpose((String) ctx.getVariable("budgetPurpose")); // основание платежа 2 символа максимум
    budgetIndex.setTaxPeriod((String) ctx.getVariable("budgetTaxPeriod")); // налоговый период до 10 символов
    budgetIndex.setTaxDocNumber((String) ctx.getVariable("budgetTaxDocNumber")); // Показатель номера документа
    budgetIndex.setTaxDocDate((String) ctx.getVariable("budgetTaxDocDate"));   // Показатель даты документа
    charge.setBudgetIndex(budgetIndex);
    if ("".equals(getStringFromContext(ctx, "chargeApplicationID", ""))) {
      String applicationID = String.valueOf(System.currentTimeMillis());
      ctx.setVariable("chargeApplicationID", applicationID);
      charge.setApplicationID(applicationID);
    } else {
      charge.setApplicationID((String) ctx.getVariable("chargeApplicationID"));
    }

    String unifiedPayerIdentifier = buildUnifiedPayerID(ctx, "");
    ctx.setVariable("unifiedPayerIdentifier", unifiedPayerIdentifier);
    charge.setUnifiedPayerIdentifier(unifiedPayerIdentifier); //  единый идентификатор плательщика
    importData.setImportRequest(importRequest);
    String importDataStr = new XmlTypes(ImportData.class).toXml(importData);
    return cryptoProvider.signElement(importDataStr, "Charge", null, true, true, false);
  }

  private PostBlock createPostBlock(ExchangeContext ctx, String postBlockSenderIdentifier) {
    final PostBlock postBlock = new PostBlock();
    String postBlockID = ctx.getVariable("postBlockId") == null ? UUID.randomUUID().toString() : (String) ctx.getVariable("postBlockId");
    ctx.setVariable("postBlockId", postBlockID);
    postBlock.setID(postBlockID);  // идентификатор запроса
    postBlock.setSenderIdentifier(postBlockSenderIdentifier);
    Date postBlockTimeStamp = ctx.getVariable("postBlockTimeStamp") != null ? (Date) ctx.getVariable("postBlockTimeStamp") : new Date();
    postBlock.setTimeStamp(XmlTypes.dateTimeAndZeroMilliseconds(formatDate(postBlockTimeStamp, "dd.MM.yyyy HH:mm:ss")));   //дата составления запроса
    return postBlock;
  }

  String buildUnifiedPayerID(ExchangeContext ctx, String suffix) {
    String payerType = getStringFromContext(ctx, "payerType" + suffix, "");
    String citizenship = getStringFromContext(ctx, "payerPersonCitizenshipID" + suffix, "");
    if ("1".equals(payerType)) { // ФЛ СНИЛС
      String snilsNumber = getStringFromContext(ctx, "payerPersonDocumentID1" + suffix, "");
      if (!snilsNumber.matches("\\d{11}")) throw new IllegalArgumentException("СНИЛС должен состоять из 11 цифр");
      return "1" + snilsNumber;
    }
    if ("2".equals(payerType)) { // ЮЛ резидент
      String legalPersonKPP = getStringFromContext(ctx, "payerLegalKPP" + suffix, "");
      String legalPersonINN = getStringFromContext(ctx, "payerLegalINN" + suffix, "");
      if (!legalPersonINN.matches("\\d{10}")) throw new IllegalArgumentException("ИНН должен состоять из 10 цифр");
      if (!legalPersonKPP.matches("\\d{9}")) throw new IllegalArgumentException("КПП должен состоять из 9 цифр");
      return "2" + legalPersonINN + legalPersonKPP;
    }
    if ("3".equals(payerType)) { // ЮЛ не резидент
      String legalPersonKPP = getStringFromContext(ctx, "payerLegalNonResidentKPP" + suffix, "");
      String legalPersonINN = getStringFromContext(ctx, "payerLegalNonResidentINN" + suffix, "");
      if (!legalPersonINN.matches("\\d{5}")) throw new IllegalArgumentException("КИО должен состоять из 5 цифр");
      if (!legalPersonKPP.matches("\\d{9}")) throw new IllegalArgumentException("КПП должен состоять из 9 цифр");
      return "3" + legalPersonINN + legalPersonKPP;
    }
    if (Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "21", "22", "23", "24").contains(payerType)) {
      String personDocumentID = getPersonDocumentId(ctx, suffix, payerType);
      if (personDocumentID.length() < 20) {
        personDocumentID = padding(20 - personDocumentID.length(), '0').concat(personDocumentID);
        if (Arrays.asList("21", "22", "23", "24").contains(payerType)) {
          return payerType + personDocumentID + "643";
        } else {
          if (!citizenship.matches("\\d{3}"))
            throw new IllegalArgumentException("Неверно указан код страны, должно быть три цифры");
          return payerType + personDocumentID + citizenship;
        }
      }
    }
    throw new IllegalArgumentException("Неверный тип плательщика " + payerType);
  }

  private String getPersonDocumentId(ExchangeContext ctx, String suffix, String payerType) {
    String documentVarName = PERSON_DOCUMENT_ID_MAP.get(payerType);
    documentVarName = documentVarName + suffix;
    return getStringFromContext(ctx, documentVarName, "").replace("-", "").toUpperCase();
  }

  private String padding(int repeat, char padChar) throws IndexOutOfBoundsException {
    if (repeat < 0) {
      throw new IndexOutOfBoundsException("Cannot pad a negative amount: " + repeat);
    }
    final char[] buf = new char[repeat];
    for (int i = 0; i < buf.length; i++) {
      buf[i] = padChar;
    }
    return new String(buf);
  }

  private String getStringFromContext(ExchangeContext ctx, String varName, String defaultString) {
    return ctx.getVariable(varName) == null ? defaultString : (String) ctx.getVariable(varName);
  }

  private String formatDate(Date dateForFormat, String format) {
    return new SimpleDateFormat(format).format(dateForFormat);
  }

  private long toLong(String valueForConvert) {
    Double dblValue = Double.parseDouble(valueForConvert.replace(',', '.'));
    return new Double(dblValue * 100).longValue();
  }


  private BigInteger generateCaseNumber() {
    BigInteger value = BigInteger.valueOf(new Date().getTime());
    value = value.multiply(BigInteger.valueOf(100000));
    long randomValue = Math.round(Math.random() * 100000.0);
    return value.add(BigInteger.valueOf(randomValue));
  }

  public void processClientResponse(ClientResponse response, ExchangeContext context) {
    try {
      context.setVariable("smevPool", true); // сервис синхронный
      if (response.verifyResult.error != null) {
        context.setVariable("smevError", response.verifyResult.error);
        context.setVariable("responseSuccess", false);
        context.setVariable("smevPool", false);
        context.setVariable("requestProcessResultErrorDescription", response.verifyResult.error);
      } else if (new QName("http://roskazna.ru/SmevUnifoService/", "UnifoTransferMsg").equals(response.action)) {
        processResponseWithFinalData(response, context);
      } else {
        context.setVariable("smevError", "Неизвестный ответ " + response.action);
      }
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }

  private void processResponseWithFinalData(ClientResponse response, ExchangeContext context) throws Exception {
    String typeOperation = getStringFromContext(context, "operationType", "");
    if (IMPORT_CHARGE_OPERATION.equals(typeOperation)) {
      processResponseOnImportChargeRequest(response, context);
    } else if (EXPORT_OPERATION.equals(typeOperation)) {
      processResponseOnExport(response, context);
    } else if (IMPORT_PAYMENT_OPERATION.equals(typeOperation)) {
      processResponseOnImportPayment(response, context);
    } else if (DO_ACKNOWLEDGMENT_OPERATION.equals(typeOperation)){
      processResponseOnAcknowledgmentOperation(response, context);
    } else {
      throw new IllegalArgumentException("Тип операции не верен");
    }

  }

  private void processResponseOnAcknowledgmentOperation(ClientResponse response, ExchangeContext context) {
    context.setVariable("smevPool", false);
    DoAcknowledgmentResponseType acknowledgmentResponse = (DoAcknowledgmentResponseType) new XmlTypes(DoAcknowledgmentResponseType.class).toBean(response.appData.getFirstChild());
    if (acknowledgmentResponse.getRequestProcessResult() != null) {
      context.setVariable("responseSuccess", false);
      String errorCode = acknowledgmentResponse.getRequestProcessResult().getErrorCode();
      processInternalErrorService(context, errorCode);
      context.setVariable("requestProcessResultErrorCode", errorCode);
      context.setVariable("requestProcessResultErrorDescription", acknowledgmentResponse.getRequestProcessResult().getErrorDescription());
    } else {
      context.setVariable("responseSuccess", true);
      fillAcknowledgmentQuinttancesToContext(context, acknowledgmentResponse.getQuittances().getQuittance());
    }
  }

  private void fillAcknowledgmentQuinttancesToContext(ExchangeContext context, List<DoAcknowledgmentResponseType.Quittances.Quittance> quittanceList) {
    context.setVariable("quittance", (long) quittanceList.size());
    //context.setVariable("-quittance", "");
    for (int idx = 0; idx < quittanceList.size(); idx++) {
      int blockIndex = idx + 1;
      DoAcknowledgmentResponseType.Quittances.Quittance quittance = quittanceList.get(idx);
      context.setVariable("quittanceSupplierBillID_" + blockIndex, quittance.getSupplierBillID());
      context.setVariable("quittancePayerIdentifier_" + blockIndex, quittance.getPayerIdentifier());
      context.setVariable("quittanceBalance_" + blockIndex, quittance.getBalance());
      context.setVariable("quittanceBillStatus_" + blockIndex, quittance.getBillStatus());
      PaymentIdentificationDataType paymentIdentificationData = quittance.getPaymentIdentificationData();
      if (paymentIdentificationData != null) {
        context.setVariable("quittanceSystemIdentifier_" + blockIndex, paymentIdentificationData.getSystemIdentifier());
        context.setVariable("quittanceBankBIK_" + blockIndex, paymentIdentificationData.getBank().getBIK());
        context.setVariable("quittanceBankName_" + blockIndex, paymentIdentificationData.getBank().getName());
        context.setVariable("quittanceBankCorrAccount_" + blockIndex, paymentIdentificationData.getBank().getCorrespondentBankAccount());
        context.setVariable("quittanceBankSWIFT_" + blockIndex, paymentIdentificationData.getBank().getSWIFT());
      } else {
        PaymentIdentificationDataType incomeRowIdentificationData = quittance.getIncomeRowIdentificationData();
        if (incomeRowIdentificationData != null) {
          context.setVariable("quittanceUFKName_" + blockIndex, incomeRowIdentificationData.getUFK());
        }
      }
      context.setVariable("quittanceApplicationId_" + blockIndex, quittance.getApplicationID());
      context.setVariable("payeeINN_" + blockIndex, quittance.getPayeeINN());
      context.setVariable("payeeKPP_" + blockIndex, quittance.getPayeeKPP());
      context.setVariable("KBK_" + blockIndex, quittance.getKBK());
      context.setVariable("OKATO_" + blockIndex, quittance.getOKATO());
    }
  }

  private void processResponseOnImportPayment(ClientResponse response, ExchangeContext context) {
    processResponseOnImportChargeRequest(response, context); // структура данных совпадает. Должно работать
  }

  private void processResponseOnExport(ClientResponse response, ExchangeContext context) throws Exception {
    context.setVariable("smevPool", false);
    String exportType = getStringFromContext(context, "exportRequestType", "");
    addAdditionalXmlSchema(response, exportType);
    ExportDataResponse exportDataResponse = (ExportDataResponse) new XmlTypes(ExportDataResponse.class).toBean(response.appData.getFirstChild());
    if (exportDataResponse.getResponseTemplate().getRequestProcessResult() != null) {
      context.setVariable("responseSuccess", false);
      String errorCode = exportDataResponse.getResponseTemplate().getRequestProcessResult().getErrorCode();
      processInternalErrorService(context, errorCode);
      context.setVariable("requestProcessResultErrorCode", errorCode);
      context.setVariable("requestProcessResultErrorDescription", exportDataResponse.getResponseTemplate().getRequestProcessResult().getErrorDescription());
    } else {
      context.setVariable("responseSuccess", true);
      if ("QUITTANCE".equals(exportType)) {
        processExportQuittanceResponse(context, exportDataResponse);
      } else if (Arrays.asList("CHARGE", "CHARGESTATUS", "CHARGENOTFULLMATCHED").contains(exportType)) {
        processExportChargeResponse(context, exportDataResponse);
      } else if (Arrays.asList("PAYMENT", "PAYMENTMODIFIED", "PAYMENTUNMATCHED").contains(exportType)) {
        processExportPaymentResponse(context, exportDataResponse);
      } else {
        throw new IllegalArgumentException("Unknown export type " + exportType);
      }
    }
  }

  private void processExportPaymentResponse(ExchangeContext ctx, ExportDataResponse exportDataResponse) throws Exception {
    ExportPaymentsResponse exportResponse = (ExportPaymentsResponse) exportDataResponse.getResponseTemplate();
    if (exportResponse.getPayments() != null) {
      List<ExportPaymentsResponse.Payments.PaymentInfo> paymentInfoList = exportResponse.getPayments().getPaymentInfo();
      ctx.setVariable("paymentBlock", (long) paymentInfoList.size());
      int blockIndex =  0;
      for (ExportPaymentsResponse.Payments.PaymentInfo paymentInfo : paymentInfoList){
         blockIndex ++;
        PaymentInfoType paymentType = (PaymentInfoType) parseEntity(paymentInfo.getPaymentData(), "http://roskazna.ru/xsd/PaymentInfo", "FinalPayment", PaymentInfoType.class);
        ctx.setVariable("paymentNarrative" + "_" + blockIndex, paymentType.getNarrative());
        ctx.setVariable("paymentSupplierBillID" + "_" + blockIndex, paymentType.getSupplierBillID());
        ctx.setVariable("paymentAmount" + "_" + blockIndex, paymentType.getAmount());
        ctx.setVariable("paymentDate" + "_" + blockIndex, paymentType.getPaymentDate().toGregorianCalendar().getTime());
        ctx.setVariable("paymentChangeStatus" + "_" + blockIndex, paymentType.getChangeStatus());
        ctx.setVariable("paymentPayeeINN" + "_" + blockIndex, paymentType.getPayeeINN());
        ctx.setVariable("paymentPayeeKPP" + "_" + blockIndex, paymentType.getPayeeKPP());
        ctx.setVariable("paymentKBK" + "_" + blockIndex, paymentType.getKBK());
        ctx.setVariable("paymentOKATO" + "_" + blockIndex, paymentType.getOKATO());
        ctx.setVariable("paymentPA" + "_" + blockIndex, paymentType.getPayerPA());
        if (paymentType.getPayeeBankAcc() != null) {
          ctx.setVariable("paymentPayeeAccount" + "_" + blockIndex, paymentType.getPayeeBankAcc().getAccount());
          ctx.setVariable("paymentPayeeBankAccKind" + "_" + blockIndex, paymentType.getPayeeBankAcc().getKind());
          ctx.setVariable("paymentPayeeBankName" + "_" + blockIndex, paymentType.getPayeeBankAcc().getBank().getName());
          ctx.setVariable("paymentCorrespondentBankAccount" + "_" + blockIndex, paymentType.getPayeeBankAcc().getBank().getCorrespondentBankAccount());
          ctx.setVariable("paymentPayeeBankBIK" + "_" + blockIndex, paymentType.getPayeeBankAcc().getBank().getBIK());
        }
        ctx.setVariable("Status" + "_" + blockIndex, paymentType.getBudgetIndex().getStatus());
        ctx.setVariable("PaymentType" + "_" + blockIndex, paymentType.getBudgetIndex().getPaymentType());
        ctx.setVariable("Purpose" + "_" + blockIndex, paymentType.getBudgetIndex().getPurpose());
        ctx.setVariable("TaxPeriod" + "_" + blockIndex, paymentType.getBudgetIndex().getTaxPeriod());
        ctx.setVariable("TaxDocNumber" + "_" + blockIndex, paymentType.getBudgetIndex().getTaxDocNumber());
        ctx.setVariable("TaxDocDate" + "_" + blockIndex, paymentType.getBudgetIndex().getTaxDocDate());
        ctx.setVariable("paymentSystemId" + "_" + blockIndex, paymentType.getPaymentIdentificationData().getSystemIdentifier());
        ctx.setVariable("paymentBIK" + "_" + blockIndex, paymentType.getPaymentIdentificationData().getBank().getBIK());
        ctx.setVariable("paymentBankName" + "_" + blockIndex, paymentType.getPaymentIdentificationData().getBank().getName());
        ctx.setVariable("payerId" + "_" + blockIndex, paymentType.getPayerIdentifier());
      }
    } else {
      ctx.setVariable("paymentBlock", 0l);
    }
  }



  private void processExportChargeResponse(ExchangeContext context, ExportDataResponse exportDataResponse) throws Exception {
    ExportChargesResponse exportResponse = (ExportChargesResponse) exportDataResponse.getResponseTemplate();
    if (exportResponse.getCharges() != null) {
      List<ExportChargesResponse.Charges.ChargeInfo> charges = exportResponse.getCharges().getChargeInfo();
      context.setVariable("chargeBlock", (long) charges.size());
      for (int idx = 0; idx < charges.size(); idx++) {
        int blockIndex = idx + 1;
        ExportChargesResponse.Charges.ChargeInfo chargeInfo = charges.get(idx);
        context.setVariable("amountToPay_" + blockIndex, chargeInfo.getAmountToPay());
        context.setVariable("quittanceWithPaymentStatus_" + blockIndex, chargeInfo.getQuittanceWithPaymentStatus());
        context.setVariable("quittanceWithIncomeStatus_" + blockIndex, chargeInfo.getQuittanceWithIncomeStatus());
        if (chargeInfo.getIsRevoked() != null) {
          context.setVariable("isRevoked_" + blockIndex, chargeInfo.getIsRevoked().isValue());
          if (chargeInfo.getIsRevoked().getDate() != null) {
            context.setVariable("revokedDate_" + blockIndex, chargeInfo.getIsRevoked().getDate().toGregorianCalendar().getTime());
          } else {
            context.setVariable("revokedDate_" + blockIndex, null);
          }
        } else {
          context.setVariable("revokedDate_" + blockIndex, null);
          context.setVariable("isRevoked_" + blockIndex, false);
        }
        if (chargeInfo.getChargeData() != null) {
          ChargeType charge = (ChargeType) parseEntity(chargeInfo.getChargeData(), "http://roskazna.ru/xsd/Charge", "Charge", ChargeType.class);
          addChargeToContext(charge, context, blockIndex);
        }
      }
    } else {
      context.setVariable("chargeBlock", 0l);
    }
  }

  private void addChargeToContext(ChargeType charge, ExchangeContext context, int blockIndex) {
    context.setVariable("SupplierBillID_" + blockIndex, charge.getSupplierBillID());
    context.setVariable("UnifiedPayerId_" + blockIndex, charge.getUnifiedPayerIdentifier());
    context.setVariable("BillDate_" + blockIndex, charge.getBillDate().toGregorianCalendar().getTime());
    context.setVariable("SupplierOrgInfoName_" + blockIndex, charge.getSupplierOrgInfo().getName());
    context.setVariable("SupplierOrgInfoINN_" + blockIndex, charge.getSupplierOrgInfo().getINN());
    context.setVariable("SupplierOrgInfoKPP_" + blockIndex, charge.getSupplierOrgInfo().getKPP());
    context.setVariable("SupplierOrgAccountKind_" + blockIndex, charge.getSupplierOrgInfo().getAccount().getKind());
    context.setVariable("SupplierOrgAccountNumber_" + blockIndex, charge.getSupplierOrgInfo().getAccount().getAccount());
    context.setVariable("SupplierOrgAccountBankName_" + blockIndex, charge.getSupplierOrgInfo().getAccount().getBank().getName());
    context.setVariable("SupplierOrgAccountBankBIK_" + blockIndex, charge.getSupplierOrgInfo().getAccount().getBank().getBIK());
    context.setVariable("ChargeChangeStatus_" + blockIndex, charge.getChangeStatus());
    context.setVariable("ChargeKBK_" + blockIndex, charge.getKBK());
    context.setVariable("ChargeOKATO_" + blockIndex, charge.getOKATO());
    context.setVariable("ChargeTreasureBranch_" + blockIndex, charge.getTreasureBranch());
    context.setVariable("budgetStatus_" + blockIndex, charge.getBudgetIndex().getStatus());
    context.setVariable("budgetPurpose_" + blockIndex, charge.getBudgetIndex().getPurpose());
    context.setVariable("budgetPaymentType_" + blockIndex, charge.getBudgetIndex().getPaymentType());
    context.setVariable("budgetTaxPeriod_" + blockIndex, charge.getBudgetIndex().getTaxPeriod());
    context.setVariable("budgetTaxDocNumber_" + blockIndex, charge.getBudgetIndex().getTaxDocNumber());
    context.setVariable("budgetTaxDocDate_" + blockIndex, charge.getBudgetIndex().getTaxDocDate());
    context.setVariable("TotalAmount_" + blockIndex, charge.getTotalAmount());
    if (charge.getAdditionalData() != null) {
      context.setVariable("additionalData_" + blockIndex, (long)charge.getAdditionalData().size());
      int additionalDataIdx = 0;
      for (Bill.AdditionalData additionalData : charge.getAdditionalData()) {
        additionalDataIdx++;
        context.setVariable("additionalDataKey_" + blockIndex + "_" + additionalDataIdx, additionalData.getName());
        context.setVariable("additionalDataValue_" + blockIndex + "_" + additionalDataIdx, additionalData.getValue());
      }
    }else {
      context.setVariable("additionalData_" + blockIndex, 0l);
    }
    if (charge.getSupplierOrgInfo ().getAddresses() != null ) {
      int addressIdx = 0;
      context.setVariable("SupplierOrgAddress_" + blockIndex, (long)charge.getSupplierOrgInfo ().getAddresses().getAddress().size());
      for (AddressType address : charge.getSupplierOrgInfo().getAddresses().getAddress()) {
        addressIdx++;
        context.setVariable("addressType_" + blockIndex + "_" + addressIdx, address.getKind());
        context.setVariable("addressView_" + blockIndex + "_" + addressIdx, address.getView());
      }
    }  else {
      context.setVariable("SupplierOrgAddress_" + blockIndex, 0l);
    }

    if (charge.getSupplierOrgInfo().getContacts() != null) {
      int contactIdx = 0;
      context.setVariable("SupplierOrgContact_" + blockIndex, (long) charge.getSupplierOrgInfo().getContacts().getContact().size());
      for (ContactInfoType contact : charge.getSupplierOrgInfo().getContacts().getContact()) {
        contactIdx++;
        context.setVariable("contactType_" + blockIndex + "_" + contactIdx, contact.getKind());
        context.setVariable("contactValue_" + blockIndex + "_" + contactIdx, contact.getValue());
        context.setVariable("contactComment_" + blockIndex + "_" + contactIdx, contact.getComment());
      }
    } else {
      context.setVariable("SupplierOrgContact_" + blockIndex, 0l);
    }
  }

  private Object parseEntity(byte[] chargeData, String namespaceURI, String qualifiedName, Class typeClass) throws Exception {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    dbf.setNamespaceAware(true);
    DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
    InputSource is = new InputSource(new StringReader(new String(chargeData, "UTF-8")));
    Document doc = documentBuilder.parse(is);
    if ("".equals(doc.getDocumentElement().getNamespaceURI()) || doc.getDocumentElement().getNamespaceURI() == null){
      Node renamedElement = doc.renameNode(doc.getDocumentElement(), namespaceURI, qualifiedName);
      return  new XmlTypes(typeClass).toBean(renamedElement);
    } else {
      return  new XmlTypes(typeClass).toBean(doc.getDocumentElement());
    }
  }

  private void processExportQuittanceResponse(ExchangeContext context, ExportDataResponse exportDataResponse) {
    ExportQuittanceResponse exportQuittanceResponse = (ExportQuittanceResponse) exportDataResponse.getResponseTemplate();
    List<ExportQuittanceResponse.Quittances.Quittance> quittanceList = exportQuittanceResponse.getQuittances() != null ? exportQuittanceResponse.getQuittances().getQuittance() : Arrays.<ExportQuittanceResponse.Quittances.Quittance>asList();
    context.setVariable("quittance", (long) quittanceList.size());
    //context.setVariable("-quittance", "");
    for (int idx = 0; idx < quittanceList.size(); idx++) {
      int blockIndex = idx + 1;
      ExportQuittanceResponse.Quittances.Quittance quittance = quittanceList.get(idx);
      context.setVariable("quittanceIsRevoked_" + blockIndex, quittance.isIsRevoked());
      context.setVariable("quittanceSupplierBillID_" + blockIndex, quittance.getSupplierBillID());
      context.setVariable("quittancePayerIdentifier_" + blockIndex, quittance.getPayerIdentifier());
      context.setVariable("quittanceBalance_" + blockIndex, quittance.getBalance());
      context.setVariable("quittanceBillStatus_" + blockIndex, quittance.getBillStatus());
      PaymentIdentificationDataType paymentIdentificationData = quittance.getPaymentIdentificationData();
      if (paymentIdentificationData != null) {
        context.setVariable("quittanceSystemIdentifier_" + blockIndex, paymentIdentificationData.getSystemIdentifier());
        context.setVariable("quittanceBankBIK_" + blockIndex, paymentIdentificationData.getBank().getBIK());
        context.setVariable("quittanceBankName_" + blockIndex, paymentIdentificationData.getBank().getName());
        context.setVariable("quittanceBankCorrAccount_" + blockIndex, paymentIdentificationData.getBank().getCorrespondentBankAccount());
        context.setVariable("quittanceBankSWIFT_" + blockIndex, paymentIdentificationData.getBank().getSWIFT());
      } else {
        PaymentIdentificationDataType incomeRowIdentificationData = quittance.getIncomeRowIdentificationData();
        if (incomeRowIdentificationData != null) {
          context.setVariable("quittanceUFKName_" + blockIndex, incomeRowIdentificationData.getUFK());
        }
      }
      context.setVariable("quittanceApplicationId_" + blockIndex, quittance.getApplicationID());
      context.setVariable("payeeINN_" + blockIndex, quittance.getPayeeINN());
      context.setVariable("payeeKPP_" + blockIndex, quittance.getPayeeKPP());
      context.setVariable("KBK_" + blockIndex, quittance.getKBK());
      context.setVariable("OKATO_" + blockIndex, quittance.getOKATO());
    }
  }

  private void addAdditionalXmlSchema(ClientResponse response, String requestedEntity) {
    if (response.appData.getElementsByTagNameNS(null, "ResponseTemplate").getLength() > 0) {
      Node responseTemplateNode = response.appData.getElementsByTagNameNS("", "ResponseTemplate").item(0);
      Node typeAttr = responseTemplateNode.getAttributes().getNamedItemNS("http://www.w3.org/2001/XMLSchema-instance", "type");
      String attributeValue = typeAttr.getNodeValue();
      String schemaURI = EXPORT_ENTITY_SCHEMA_MAP.get(requestedEntity);
      response.appData.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + attributeValue.split(":")[0], schemaURI);
    }
  }

  private void processResponseOnImportChargeRequest(ClientResponse response, ExchangeContext context) {
    ImportDataResponse importDataResponse = (ImportDataResponse) new XmlTypes(ImportDataResponse.class).toBean(response.appData.getFirstChild());
    if (importDataResponse == null) throw new IllegalStateException("Can't parse import data response");
    Ticket ticket = importDataResponse.getTicket();
    context.setVariable("ticketPostBlockID", ticket.getPostBlock().getID());
    context.setVariable("ticketPostBlockSenderIdentifier", ticket.getPostBlock().getSenderIdentifier());
    context.setVariable("ticketPostBlockTimeStamp", ticket.getPostBlock().getTimeStamp().toGregorianCalendar().getTime());
    if (ticket.getRequestProcessResult() != null) {
      String errorCode = ticket.getRequestProcessResult().getErrorCode();
      context.setVariable("requestProcessResultErrorCode", errorCode);
      context.setVariable("requestProcessResultErrorDescription", ticket.getRequestProcessResult().getErrorDescription());
      context.setVariable("responseSuccess", false);
      context.setVariable("smevPool", false);
      processInternalErrorService(context, errorCode);
    } else {
      context.setVariable("responseSuccess", true);
      context.setVariable("smevPool", false);
    }
  }

  private void processInternalErrorService(ExchangeContext context, String errorCode) {
    if ("EBPP5000".equals(errorCode)) {
      int countRetries = context.getVariableNames().contains("countRetries") ? (Integer) context.getVariable("countRetries") : 0;
      countRetries++;
      context.setVariable("countRetries", countRetries);
      context.setVariable("smevTimeOut", calcTimeOut(countRetries));
      context.setVariable("smevPool", countRetries < 5);
    }
  }

  private String calcTimeOut(int countRetries) {
    if (countRetries == 1) return "PT30S";
    else return "PT600S";
  }


  public void bindCryptoProvider(CryptoProvider cryptoProvider) {
    this.cryptoProvider = cryptoProvider;
  }

  public void unbindCryptoProvider(CryptoProvider cryptoProvider) {
    this.cryptoProvider = null;
  }
}
