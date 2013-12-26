/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws3456c;

import ru.codeinside.gws.api.*;
import ru.tower.mvd.clients.common.requestid.HeaderType;
import ru.tower.mvd.clients.giac.request.*;
import ru.tower.mvd.clients.giac.response.message.ResponseInfoType;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import java.math.BigInteger;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MvdClient3456 implements Client {
    public static final String SMEV_REG_TIME = "smevRegTime";
    public static final String SMEV_REG_NUMBER = "smevRegNumber";
    public static final String SMEV_INITIAL_REG_NUMBER = "smevInitialRegNumber";
    private static final String SMEV_INITIAL_REG_DATE = "smevInitialRegDate";
    private Logger logger = Logger.getLogger(getClass().getName());

    public Revision getRevision() {
        return ru.codeinside.gws.api.Revision.rev111111;
    }

    public URL getWsdlUrl() {
        return getClass().getClassLoader().getResource("gws3456/GIACService.wsdl");
    }

    public ClientRequest createClientRequest(ExchangeContext ctx) {
        final String originRequestId = (String) ctx.getVariable("smevOriginRequestId");
        final String requestId = (String) ctx.getVariable("smevRequestId");
        final Boolean smevPool = (Boolean) ctx.getVariable("smevPool");

        final Packet packet = new Packet();
        packet.recipient = new InfoSystem("MVDR01001", "МВД России");
        packet.typeCode = Packet.Type.SERVICE;
        packet.date = new Date();
        packet.exchangeType = "2"; // 1 - Запрос на оказание услуги
        packet.originRequestIdRef = originRequestId;
        packet.testMsg = (String) ctx.getVariable("smevTest");

        packet.serviceCode = "12345678901";
        final ClientRequest request = new ClientRequest();

        if (Boolean.TRUE == smevPool) {
            packet.status = Packet.Status.PING;
            packet.requestIdRef = requestId;
            request.packet = packet;
            request.action = new QName("http://tower.ru/mvd/clients/giac", "getGIACResponse");
            request.appData = createAppDataForGetResponseOperation(ctx);
        } else {
            BigInteger caseNumber = generateCaseNumber();
            packet.caseNumber = caseNumber.toString();
            ctx.setVariable(SMEV_INITIAL_REG_NUMBER, caseNumber);
            ctx.setVariable(SMEV_INITIAL_REG_DATE, new Date());
            request.packet = packet;
            request.action = new QName("http://tower.ru/mvd/clients/giac", "sendGIACRequest");
            packet.status = Packet.Status.REQUEST;
            request.appData = createAppDataForSendRequestOperation(ctx);
        }
        return request;
    }

    private String createAppDataForGetResponseOperation(ExchangeContext ctx) {
        ru.tower.mvd.clients.common.requestid.Message requestIdMessage = new ru.tower.mvd.clients.common.requestid.Message();
        HeaderType header = new HeaderType();
        HeaderType.RegNumber regNumber = new HeaderType.RegNumber();
        regNumber.setValue((BigInteger) ctx.getVariable(SMEV_REG_NUMBER));
        regNumber.setRegtime((XMLGregorianCalendar) ctx.getVariable(SMEV_REG_TIME));
        header.setRegNumber(regNumber);
        header.setMsgType("REQUEST_ID");
        header.setMsgVid((String) ctx.getVariable("MsgVid"));
        header.setFromFoivId("PNZR01581");
        header.setFromFoivName("Комплексная система предоставления государственных и муниципальных услуг Пензенской области");
        header.setFromSystemId("PNZR01581");
        header.setFromSystem("Комплексная система предоставления государственных и муниципальных услуг Пензенской области");
        header.setToFoivId("MVDR01001");
        header.setToFoivName("МВД России");
        header.setToSystem("ИС ГИАЦ");
        header.setToSystemId("6");
        header.setVersion("1.1");
        header.setReason((String) ctx.getVariable("Reason"));


        ru.tower.mvd.clients.common.requestid.BaseHeaderType.InitialRegNumber initialRegNumber = new ru.tower.mvd.clients.common.requestid.BaseHeaderType.InitialRegNumber();
        initialRegNumber.setValue((BigInteger) ctx.getVariable(SMEV_INITIAL_REG_NUMBER));
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime((Date) ctx.getVariable(SMEV_INITIAL_REG_DATE));
        try {
            initialRegNumber.setRegtime(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar));
        } catch (DatatypeConfigurationException err) {
            logger.log(Level.SEVERE, err.getMessage(), err);
        }


        header.setInitialRegNumber(initialRegNumber);

        ru.tower.mvd.clients.common.requestid.BaseHeaderType.Service service = new ru.tower.mvd.clients.common.requestid.BaseHeaderType.Service();
        service.setCode(1); // В документации предписано указывать только это значение
        service.setName("conviction_investigation"); // В документации предписано указывать только это значение
        service.setValue("Сведения о наличии (отсутствии) судимости и (или) факта уголовного преследования либо о прекращении уголовного преследования, сведения о нахождении в розыске");// В
        header.setService(service);

        ru.tower.mvd.clients.common.requestid.BaseHeaderType.Originator originator = new ru.tower.mvd.clients.common.requestid.BaseHeaderType.Originator();
        originator.setCode("PNZR01581"/*(String) ctx.getVariable("OriginatorCode")*/);
        originator.setFio((String) ctx.getVariable("OriginatorFio"));
        originator.setName("Комплексная система предоставления государственных и муниципальных услуг Пензенской области"/*(String) ctx.getVariable("OriginatorName")*/);
        originator.setRegion((String) ctx.getVariable("OriginatorRegion"));
        header.setOriginator(originator);

        requestIdMessage.setHeader(header);

        return new XmlTypes(ru.tower.mvd.clients.common.requestid.Message.class).toXml(requestIdMessage);
    }

    private XMLGregorianCalendar convertToGregorianCalendar(Date time) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        try {
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
        } catch (DatatypeConfigurationException err) {
            logger.log(Level.SEVERE, err.getMessage(), err);
        }
        return null;
    }

    private BigInteger generateCaseNumber() {
        BigInteger value = BigInteger.valueOf(new Date().getTime());
        value = value.multiply(BigInteger.valueOf(100000));
        long randomValue = Math.round(Math.random() * 100000.0);
        return value.add(BigInteger.valueOf(randomValue));
    }

    private String createAppDataForSendRequestOperation(ExchangeContext ctx) {
        BaseHeaderType header = new BaseHeaderType();
        header.setMsgType("REQUEST");
        header.setMsgVid((String) ctx.getVariable("MsgVid"));
        header.setFromFoivId("PNZR01581");
        header.setFromFoivName("Комплексная система предоставления государственных и муниципальных услуг Пензенской области");
        header.setFromSystemId("PNZR01581");
        header.setFromSystem("Комплексная система предоставления государственных и муниципальных услуг Пензенской области");
        header.setToFoivId("MVDR01001");
        header.setToFoivName("МВД России");
        header.setToSystem("ИС ГИАЦ");
        header.setToSystemId("6");
        header.setVersion("1.1");
        header.setReason((String) ctx.getVariable("Reason"));

        BaseHeaderType.InitialRegNumber regNumber = new BaseHeaderType.InitialRegNumber();
        regNumber.setValue((BigInteger) ctx.getVariable(SMEV_INITIAL_REG_NUMBER));
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime((Date) ctx.getVariable(SMEV_INITIAL_REG_DATE));
        try {
            regNumber.setRegtime(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar));
        } catch (DatatypeConfigurationException err) {
            logger.log(Level.SEVERE, err.getMessage(), err);
        }
        header.setInitialRegNumber(regNumber);

        BaseHeaderType.Service service = new BaseHeaderType.Service();
        service.setCode(1); // В документации предписано указывать только это значение
        service.setName("conviction_investigation"); // В документации предписано указывать только это значение
        service.setValue("Сведения о наличии (отсутствии) судимости и (или) факта уголовного преследования либо о прекращении уголовного преследования, сведения о нахождении в розыске");// В
        // значение
        header.setService(service);

        BaseHeaderType.Originator originator = new BaseHeaderType.Originator();
        originator.setCode("PNZR01581"/*(String) ctx.getVariable("OriginatorCode")*/);
        originator.setFio((String) ctx.getVariable("OriginatorFio"));
        originator.setName("Комплексная система предоставления государственных и муниципальных услуг Пензенской области"/*(String) ctx.getVariable("OriginatorName")*/);
        originator.setRegion((String) ctx.getVariable("OriginatorRegion"));
        header.setOriginator(originator);

        GIACPrivatePersonType privatePerson = new GIACPrivatePersonType();
        privatePerson.setDateOfBirth(formatDate((Date) ctx.getVariable("DateOfBirth"), "dd.MM.yyyy"));
        privatePerson.setFirstName((String) ctx.getVariable("FirstName"));
        privatePerson.setFathersName((String) ctx.getVariable("FathersName"));
        privatePerson.setSecName((String) ctx.getVariable("SecName"));
        privatePerson.setSNILS((String) ctx.getVariable("SNILS"));

        PlaceOfBirthType placeOfBirth = new PlaceOfBirthType();
        placeOfBirth.setCode((String) ctx.getVariable("BirthRegionCode"));
        placeOfBirth.setValue((String) ctx.getVariable("PlaceOfBirth"));
        privatePerson.setPlaceOfBirth(placeOfBirth);

        GIACPrivatePersonType.Address address = new GIACPrivatePersonType.Address();
        address.setRegion((String) ctx.getVariable("Region"));
        address.setRegistrationPlace((String) ctx.getVariable("RegistrationPlace"));
        address.setTypeRegistration((String) ctx.getVariable("TypeRegistration"));

        privatePerson.setAddress(address);

        DocumentType document = new DocumentType();
        document.setPrivatePerson(privatePerson);

        Message message = new Message();
        message.setHeader(header);
        message.setDocument(document);

        return new XmlTypes(Message.class).toXml(message);
    }

    private String formatDate(Date dateValue, String pattern) {
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(dateValue);
    }

    public void processClientResponse(ClientResponse response, ExchangeContext context) {
        if (response.verifyResult.error != null) {
            context.setVariable("smevPool", false);
            context.setVariable("smevError", response.verifyResult.error);
        } else if (new QName("http://smev.gosuslugi.ru/rev111111", "ResponseID").equals(response.action)) {
            processResponseOfPingOperation(response, context);
        } else if (new QName("http://smev.gosuslugi.ru/rev111111", "ResponseGIAC").equals(response.action)) {
            proceccResponseWithFinalData(response, context);
        } else {
            context.setVariable("smevPool", false);
            context.setVariable("smevError", "Неизвестный ответ " + response.action);
        }
    }

    private void proceccResponseWithFinalData(ClientResponse response, ExchangeContext context) {
        // разбираем конечный ответ
        ru.tower.mvd.clients.giac.response.AppDataType appData = XmlTypes.elementToBean(response.appData, ru.tower.mvd.clients.giac.response.AppDataType.class);
        ResponseInfoType.Subdivision subdivision = appData.getMessage().getResponseInfo().getSubdivision();
        context.setVariable("codeDivision", subdivision.getCode());
        context.setVariable("nameDivision", subdivision.getName());
        context.setVariable("infoFound", subdivision.getFound());
        String result = "";
        if (subdivision.getResult() != null) {
            result = new String(subdivision.getResult());
        }
        context.setVariable("infoTxt", result);
        context.setVariable("smevPool", false);
    }

    private void processResponseOfPingOperation(ClientResponse response, ExchangeContext context) {
        Boolean pooled = (Boolean) context.getVariable("smevPool");
        if (response.packet.status == Packet.Status.ACCEPT) {
            context.setVariable("smevPool", true);
            if (Boolean.TRUE != pooled) {
                // разбираем ответ от первичного запроса
                context.setVariable("smevRequestId", response.packet.requestIdRef);
                context.setVariable("smevOriginRequestId", response.packet.originRequestIdRef);
                // получаем regNumber и regDate они понадобятся для отправки повторного запроса
                ru.tower.mvd.clients.common.responseid.AppDataType appData = XmlTypes.elementToBean(response.appData, ru.tower.mvd.clients.common.responseid.AppDataType.class);
                ru.tower.mvd.clients.common.responseid.message.HeaderType.RegNumber regNumber = appData.getMessage().getHeader().getRegNumber();
                context.setVariable(SMEV_REG_NUMBER, regNumber.getValue());
                context.setVariable(SMEV_REG_TIME, regNumber.getRegtime());
            } else {
                context.setVariable("smevPool", false);
                ru.tower.mvd.clients.common.responseid.AppDataType appData = XmlTypes.elementToBean(response.appData, ru.tower.mvd.clients.common.responseid.AppDataType.class);
                context.setVariable("smevError", "Описание ошибки " + appData.getMessage().getAcknowledgement().getResult());
            }
        }
    }
}
