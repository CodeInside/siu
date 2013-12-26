/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws3457c;


import org.xml.sax.SAXException;
import ru.codeinside.gws.api.*;
import ru.socit.pfr.service.data.Properties;
import ru.socit.pfr.service.data.Property;
import ru.socit.pfr.service.data.Type;
import ru.tower.mvd.clients.pf.request.BaseHeaderType;
import ru.tower.mvd.clients.pf.request.Message;
import ru.tower.mvd.clients.pf.requestid.HeaderType;
import ru.tower.mvd.response.addpayment.ResponseAdditionalPaymentRequest;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MvdClient3457 implements Client {
    public static final String SMEV_REQUEST_ID = "smevQRYNMB";
    public static final String INITIAL_REG_NUMBER = "INITIAL_REG_NUMBER";
    private static final String INITIAL_REG_DATE = "INITIAL_REG_DATE";
    private Logger logger = Logger.getLogger(getClass().getName());

    @Override
    public Revision getRevision() {
        return ru.codeinside.gws.api.Revision.rev111111; // todo chek it!!!
    }

    @Override
    public URL getWsdlUrl() {
        return getClass().getClassLoader().getResource("gws3457/PFService.wsdl");
    }

    @Override
    public ClientRequest createClientRequest(ExchangeContext ctx) {
        final String originRequestId = (String) ctx.getVariable("smevOriginRequestId");
        final String requestId = (String) ctx.getVariable("smevRequestId");
        final Boolean smevPool = (Boolean) ctx.getVariable("smevPool");

        final Packet packet = new Packet();
        packet.recipient = new InfoSystem("MVDR01001", "МВД России");
        packet.typeCode = Packet.Type.SERVICE;
        packet.date = new Date();
        packet.exchangeType = "2";
        packet.originRequestIdRef = originRequestId;
        packet.testMsg = (String) ctx.getVariable("smevTest");
        packet.serviceCode = "12345678901";
        final ClientRequest request = new ClientRequest();
        request.packet = packet;
        if (Boolean.TRUE == smevPool) {
            packet.status = Packet.Status.PING;
            packet.requestIdRef = requestId;
            request.action = new QName("http://tower.ru/mvd/clients/pf", "getPFResponse");
            request.appData = createAppDataForPing(ctx);
            BigInteger caseNumber = (BigInteger) ctx.getVariable(INITIAL_REG_NUMBER);
            packet.caseNumber = caseNumber.toString();
        } else {
            BigInteger caseNumber = generateCaseNumber();
            ctx.setVariable(INITIAL_REG_NUMBER, caseNumber);
            ctx.setVariable(INITIAL_REG_DATE, new Date());
            packet.status = Packet.Status.REQUEST;
            request.appData = createAppDataForFirstRequest(ctx);
            request.action = new QName("http://tower.ru/mvd/clients/pf", "sendPFRequest");
            packet.caseNumber = caseNumber.toString();
        }
        return request;
    }

    private String createAppDataForPing(ExchangeContext ctx) {
        ru.tower.mvd.clients.pf.requestid.Message message = new ru.tower.mvd.clients.pf.requestid.Message();
        message.setType(Type.REQUEST);
        HeaderType header = new HeaderType();
        header.setMsgType("REQUEST_ID");
        String msgVid = (String) ctx.getVariable("MsgVid");
        header.setMsgVid(msgVid);
        header.setFromFoivId("PNZR01581");
        header.setFromFoivName("Комплексная система предоставления государственных и муниципальных услуг Пензенской области");
        header.setFromSystemId("PNZR01581");
        header.setFromSystem("Комплексная система предоставления государственных и муниципальных услуг Пензенской области");
        header.setToFoivId("MVDR01001");
        header.setToFoivName("МВД России");
        header.setToSystem("АИС МВД"); // нет ничего в документации
        header.setToSystemId("8"); // нет ничего в документации
        header.setReason("12345678908");

        ru.tower.mvd.clients.pf.requestid.BaseHeaderType.Service service = new ru.tower.mvd.clients.pf.requestid.BaseHeaderType.Service();
        service.setCode(8); // В документации предписано указывать только это значение
        service.setName("Pension"); // В документации предписано указывать только это значение
        service.setValue("Сведения о пенсиях и выплатах");
        header.setService(service);

        ru.tower.mvd.clients.pf.requestid.BaseHeaderType.InitialRegNumber initialRegNumber = new ru.tower.mvd.clients.pf.requestid.BaseHeaderType.InitialRegNumber();
        initialRegNumber.setValue((BigInteger) ctx.getVariable(INITIAL_REG_NUMBER));
        initialRegNumber.setRegtime(getXMLDate((Date) ctx.getVariable(INITIAL_REG_DATE)));
        header.setInitialRegNumber(initialRegNumber);

        ru.tower.mvd.clients.pf.requestid.BaseHeaderType.Originator originator = new ru.tower.mvd.clients.pf.requestid.BaseHeaderType.Originator();
        originator.setCode("PNZR01581"/*(String) ctx.getVariable("OriginatorCode")*/);
        originator.setFio((String) ctx.getVariable("OriginatorFio"));
        originator.setName("Комплексная система предоставления государственных и муниципальных услуг Пензенской области"/*(String) ctx.getVariable("OriginatorName")*/);
        originator.setRegion((String) ctx.getVariable("OriginatorRegion")); // Принимает значение первых 3 цифр из справочника КЛАДР
        header.setOriginator(originator);

        HeaderType.RegNumber regNumber = new HeaderType.RegNumber();

        String smevRequestId = (String) ctx.getVariable(SMEV_REQUEST_ID);
        regNumber.setValue(new BigInteger(smevRequestId));

        regNumber.setRegtime(getXMLDate((Date) ctx.getVariable(INITIAL_REG_DATE)));
        header.setRegNumber(regNumber);

        message.setHeader(header);

        Properties properties = new Properties();
        properties.getProperty().add(createProperty("QRYNMB", smevRequestId));
        message.setProperties(properties);
        message.setType(Type.REQUEST);

        return new XmlTypes(ru.tower.mvd.clients.pf.requestid.Message.class).toXml(message);
    }

    private String createAppDataForFirstRequest(ExchangeContext ctx) {
        BaseHeaderType header = new BaseHeaderType();
        header.setMsgType("REQUEST");
        String msgVid = (String) ctx.getVariable("MsgVid");
        header.setMsgVid(msgVid);
        header.setFromFoivId("PNZR01581");
        header.setFromFoivName("Комплексная система предоставления государственных и муниципальных услуг Пензенской области");
        header.setFromSystemId("PNZR01581");
        header.setFromSystem("Комплексная система предоставления государственных и муниципальных услуг Пензенской области");
        header.setToFoivId("MVDR01001");
        header.setToFoivName("МВД России");
        header.setToSystem("АИС МВД"); // нет ничего в документации
        header.setToSystemId("8"); // нет ничего в документации
        header.setReason("12345678908");
        BaseHeaderType.InitialRegNumber initialRegNumber = new BaseHeaderType.InitialRegNumber();
        initialRegNumber.setValue((BigInteger) ctx.getVariable(INITIAL_REG_NUMBER));
        initialRegNumber.setRegtime(getXMLDate((Date) ctx.getVariable(INITIAL_REG_DATE)));
        header.setInitialRegNumber(initialRegNumber);

        BaseHeaderType.Service service = new BaseHeaderType.Service();
        service.setCode(8); // В документации предписано указывать только это значение
        service.setName("Pension"); // В документации предписано указывать только это значение
        service.setValue("Сведения о пенсиях и выплатах");
        header.setService(service);

        BaseHeaderType.Originator originator = new BaseHeaderType.Originator();
        originator.setCode("PNZR01581"/*(String) ctx.getVariable("OriginatorCode")*/);
        originator.setFio((String) ctx.getVariable("OriginatorFio"));
        originator.setName("Комплексная система предоставления государственных и муниципальных услуг Пензенской области"/*(String) ctx.getVariable("OriginatorName")*/);
        originator.setRegion((String) ctx.getVariable("OriginatorRegion")); // Принимает значение первых 3 цифр из справочника КЛАДР
        header.setOriginator(originator);

        Message message = new ru.tower.mvd.clients.pf.request.Message();
        message.setHeader(header);

        Properties properties = new Properties();
        properties.getProperty().add(createProperty("TYPE_QUERY", (String) ctx.getVariable("typeQuery")));
        properties.getProperty().add(createProperty("SECOND_NAME", (String) ctx.getVariable("secondName"))); // фамилия
        properties.getProperty().add(createProperty("FIRST_NAME", (String) ctx.getVariable("firstName"))); // имя
        properties.getProperty().add(createProperty("PATRONYMIC", (String) ctx.getVariable("patronymic"))); // отчество
        properties.getProperty().add(createProperty("SNILS", (String) ctx.getVariable("snils"))); // СНИЛС NNN-NNN-NNN NN
        properties.getProperty().add(createProperty("DPTCOD", (String) ctx.getVariable("dptcod"))); // код региона куда отправляется запрос из справочника документа
        properties.getProperty().add(createProperty("DATE_OF_BIRTH", formatDate((Date) ctx.getVariable("DateOfBirth"), "dd.MM.yyyy")));
        properties.getProperty().add(createProperty("DOCUMENT_NAME", "ПЕНСИОННОЕ УДОСТОВЕРЕНИЕ")); // так указано в документации
        properties.getProperty().add(createProperty("DOCUMENT_NUMBER", (String) ctx.getVariable("documentNumber"))); // номер документа

        if (Arrays.asList("pension1", "pension3").contains(msgVid)) {
            properties.getProperty().add(createProperty("PENSION_DATE", formatDate((Date) ctx.getVariable("requestMomentDate"), "dd.MM.yyyy"))); // Дата, на которую запрашиваются сведения.
        }
        message.setProperties(properties);
        message.setType(Type.REQUEST);
        return new XmlTypes(Message.class).toXml(message);
    }

    private XMLGregorianCalendar getXMLDate(Date date) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(date);
        try {
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
        } catch (DatatypeConfigurationException err) {
            logger.log(Level.SEVERE, err.getMessage(), err);
        }
        return null;
    }

    private String formatDate(Date dateValue, String pattern) {
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(dateValue);
    }

    private Property createProperty(String key, String value) {
        Property property = new Property();
        property.setPropertyName(key);
        property.setPropertyValue(value);
        return property;
    }

    @Override
    public void processClientResponse(ClientResponse response, ExchangeContext context) {
        Boolean pooled = (Boolean) context.getVariable("smevPool");
        if (response.verifyResult.error != null) {
            context.setVariable("smevPool", false);
            context.setVariable("smevError", response.verifyResult.error);
        } else {
            QName responseID = new QName("http://smev.gosuslugi.ru/rev111111", "ResponseID");
            QName responsePF = new QName("http://smev.gosuslugi.ru/rev111111", "ResponsePF");
            if ((responseID.equals(response.action))) {
                if (response.packet.status == Packet.Status.ACCEPT) {
                    context.setVariable("smevPool", true);
                    if (Boolean.TRUE != pooled) {
                        context.setVariable("smevRequestId", response.packet.requestIdRef);
                        context.setVariable("smevOriginRequestId", response.packet.originRequestIdRef);
                        ru.tower.mvd.clients.pf.responseid.AppDataType appData = XmlTypes.elementToBean(response.appData, ru.tower.mvd.clients.pf.responseid.AppDataType.class);
                        ru.tower.mvd.clients.pf.responseid.message.Message message = appData.getMessage();
                        String orderNumber = findPropertyByName(message.getProperties(), "QRYNMB");
                        context.setVariable(SMEV_REQUEST_ID, orderNumber);
                    } else {
                        context.setVariable("smevPool", false);
                        ru.tower.mvd.clients.pf.responseid.AppDataType appData = XmlTypes.elementToBean(response.appData, ru.tower.mvd.clients.pf.responseid.AppDataType.class);
                        ru.tower.mvd.clients.pf.responseid.message.Message message = appData.getMessage();
                        context.setVariable("smevError", "Ошибка " + findPropertyByName(message.getProperties(), "STATUSDESCR"));
                    }
                }
            }
            if ((responsePF.equals(response.action))) {
                if (response.packet.status == Packet.Status.RESULT) {
                    context.setVariable("smevPool", false);
                    ru.tower.mvd.clients.pf.response.AppDataType appData = XmlTypes.elementToBean(response.appData, ru.tower.mvd.clients.pf.response.AppDataType.class);
                    ru.tower.mvd.clients.pf.response.message.Message message = appData.getMessage();
                    if (message.getType() == Type.RESPONSE) {
                        String xmlData = new String(message.getFilePFR());
                        context.setVariable("info", xmlData);
                        context.setVariable("status", Boolean.TRUE);
                        processXMLData(xmlData, context);
                    } else {
                        context.setVariable("status", Boolean.FALSE);
                        context.setVariable("info", findPropertyByName(message.getProperties(), "TYPE_RESPONSE"));
                    }
                }
            }
        }
    }

    private void processXMLData(String xmlData, ExchangeContext context) {
        try {
            final ResponseAdditionalPaymentRequest responseAdditionalPaymentRequest = UnmarshallHelper.parseAdditionaPaymentResult(xmlData);
            context.setVariable("dataExist", responseAdditionalPaymentRequest.getНаличиеДанных());
            context.setVariable("stoppingPaymentDate", responseAdditionalPaymentRequest.getПрекращениеВыплат().getДата());
            context.setVariable("stoppingPaymentOrg", responseAdditionalPaymentRequest.getПрекращениеВыплат().getОрганизация());
            context.setVariable("stoppingPaymentReason", responseAdditionalPaymentRequest.getПрекращениеВыплат().getОснование());
            context.setVariable("responseDataOrgName", responseAdditionalPaymentRequest.getПодразделение().getНаименование());
            context.setVariable("responseDataOrgRegionCode", responseAdditionalPaymentRequest.getПодразделение().getРегион());
            context.setVariable("paymentTrudPension", findPayment(responseAdditionalPaymentRequest.getВсеВыплаты().getВыплата(), "ТРУДОВАЯ_ПЕНСИЯ"));
            context.setVariable("paymentGosPension", findPayment(responseAdditionalPaymentRequest.getВсеВыплаты().getВыплата(), "ГОСУДАРСТВЕННАЯ_ПЕНСИЯ"));
            context.setVariable("paymentEDV", findPayment(responseAdditionalPaymentRequest.getВсеВыплаты().getВыплата(), "ЕДВ"));
            context.setVariable("paymentDemo", findPayment(responseAdditionalPaymentRequest.getВсеВыплаты().getВыплата(), "ДЕМО"));
            context.setVariable("paymentEDVHiroes", findPayment(responseAdditionalPaymentRequest.getВсеВыплаты().getВыплата(), "ЕДВ_ГЕРОЯМ"));
            context.setVariable("paymentDPM", findPayment(responseAdditionalPaymentRequest.getВсеВыплаты().getВыплата(), "ДПМ"));
        } catch (JAXBException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (SAXException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    private String findPayment(List<ResponseAdditionalPaymentRequest.ВсеВыплаты.Выплата> paymentList, String paymentType) {
        for (ResponseAdditionalPaymentRequest.ВсеВыплаты.Выплата payment : paymentList) {
            if (paymentType.equals(payment.getВидВыплаты())){
                NumberFormat format = DecimalFormat.getInstance();
                format.setMaximumFractionDigits(2);
                return format.format(payment.getСуммаВыплаты());
            }
        }
        return "0.00";
    }


    private String findPropertyByName(Properties properties, String propertyKey) {
        for (Property property : properties.getProperty()) {
            if (property.getPropertyName().equals(propertyKey)) {
                return property.getPropertyValue();
            }
        }
        return null;
    }

    private BigInteger generateCaseNumber() {
        BigInteger value = BigInteger.valueOf(new Date().getTime());
        value = value.multiply(BigInteger.valueOf(100000));
        long randomValue = Math.round(Math.random() * 100000.0);
        return value.add(BigInteger.valueOf(randomValue));
    }
}
