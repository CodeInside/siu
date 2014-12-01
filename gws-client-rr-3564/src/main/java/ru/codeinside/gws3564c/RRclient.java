/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws3564c;

import com.google.common.base.Joiner;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import ru.codeinside.gws.api.*;
import ru.codeinside.gws3564c.enclosure.EnclosureRequestBuilder;
import ru.fccland.portal.types.CreateRequestBean;
import ru.fccland.portal.types.StatusResponseBean;

import javax.xml.namespace.QName;
import java.math.BigInteger;
import java.net.URL;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;


public class RRclient implements Client {
    private final Logger logger = Logger.getLogger(getClass().getName());

    public static final String SMEV_INITIAL_REG_NUMBER = "smevInitialRegNumber";
    public static final String SMEV_INITIAL_REG_DATE = "smevInitialRegDate";



    @Override
    public Revision getRevision() {
        return ru.codeinside.gws.api.Revision.rev111111;
    }

    @Override
    public URL getWsdlUrl() {
        return getClass().getClassLoader().getResource("rr_wsdl/RR.wsdl");
    }

    @Override
    public ClientRequest createClientRequest(ExchangeContext ctx) {

        InfoSystem pnzr01581 = new InfoSystem("PNZR01581", "Комплексная система предоставления государственных и муниципальных услуг Пензенской области");
        final String originRequestId = (String) ctx.getVariable("smevOriginRequestId");
        final String requestId = (String) ctx.getVariable("smevRequestId");
        final Boolean smevPool = (Boolean) ctx.getVariable("smevPool");

        final Packet packet = new Packet();
        packet.sender = packet.originator = pnzr01581;
        packet.recipient = new InfoSystem("RRTR01001", "Росреестр");
        packet.typeCode = Packet.Type.SERVICE;

        packet.exchangeType = "2"; // 1 - Запрос на оказание услуги
        packet.originRequestIdRef = originRequestId;
        packet.serviceCode = "10000013628";

        packet.testMsg = (String) ctx.getVariable("smevTest");

        final ClientRequest request = new ClientRequest();

        if (Boolean.TRUE == smevPool) {
            logger.info("PING smevPool    " + smevPool );
            request.action = new QName("http://portal.fccland.ru/rt/", "GetStatus");
            request.soapActionHttpHeader = "http://portal.rosreestr.ru/GetStatus";
            packet.status = Packet.Status.PING;
            packet.requestIdRef = requestId;
            packet.caseNumber = (String) ctx.getVariable(SMEV_INITIAL_REG_NUMBER);
            packet.date = (Date) ctx.getVariable(SMEV_INITIAL_REG_DATE);
            request.appData = createAppData(ctx);
        } else {
            packet.status = Packet.Status.REQUEST;
            request.action = new QName("http://portal.fccland.ru/rt/", "CreateRequest");
            request.soapActionHttpHeader = "http://portal.rosreestr.ru/CreateRequest";
            request.appData = createAppData(ctx);
            BigInteger caseNumber = generateCaseNumber();
            ctx.setVariable(SMEV_INITIAL_REG_NUMBER, caseNumber.toString());
            ctx.setVariable(SMEV_INITIAL_REG_DATE, new Date());

            packet.caseNumber = (String) ctx.getVariable(SMEV_INITIAL_REG_NUMBER);
            packet.date = (Date) ctx.getVariable(SMEV_INITIAL_REG_DATE);


            String id = UUID.randomUUID().toString();
            EnclosureRequestBuilder enclosureBuilder = EnclosureBuilderFactory.createEnclosureBuilder(ctx);
            String xml = enclosureBuilder.createEnclosure( id);
            System.out.println("enclosure:  " + xml);
            String zipName = "req_" + id;
            Enclosure enclosure = new Enclosure(zipName + ".xml", xml.getBytes());
            enclosure.fileName = zipName + ".xml";
            request.enclosures = new Enclosure[]{enclosure};
            request.enclosureDescriptor = zipName;
        }
        request.packet = packet;
        return request;
    }

    private BigInteger generateCaseNumber() {
        BigInteger value = BigInteger.valueOf(new Date().getTime());
        value = value.multiply(BigInteger.valueOf(100000));
        long randomValue = Math.round(Math.random() * 100000.0);
        return value.add(BigInteger.valueOf(randomValue));
    }

	@Override
    public void processClientResponse(ClientResponse response, ExchangeContext ctx) {
        Boolean pooled = (Boolean) ctx.getVariable("smevPool");
        logger.info("smevPool    " + pooled );
        logger.info("response.action    " + response.action );
        logger.info("response.verifyResult.error    " + response.verifyResult.error );
        logger.info("response.packet.status    " + response.packet.status );
        if (response.verifyResult.error != null) {
            logger.info("response.verifyResult.error    " + response.verifyResult.error );
            ctx.setVariable("smevPool", false);
            ctx.setVariable("smevError", response.verifyResult.error);
        } else if (!((new QName("http://portal.fccland.ru/rt/", "getStatusResponse").equals(response.action))||(new QName("http://portal.fccland.ru/rt/", "createRequestResponse").equals(response.action)))) {
            logger.info("!!!!!!!! Неизвестный ответ ");
            ctx.setVariable("smevPool", false);
            ctx.setVariable("smevError", "Неизвестный ответ " + response.action);
        }

            if ((new QName("http://portal.fccland.ru/rt/", "createRequestResponse")).equals(response.action)){
                if (response.packet.status == Packet.Status.ACCEPT) {
                    logger.info("RequestResponse.appdata  " + response.appData);
                    ctx.setVariable("smevPool", true);

                    if (Boolean.TRUE != pooled) {

                        ctx.setVariable("smevRequestId", response.packet.requestIdRef);
                        ctx.setVariable("smevOriginRequestId", response.packet.originRequestIdRef);
                        Element appData = response.appData;

                        if  (appData.getElementsByTagNameNS("http://portal.fccland.ru/types/", "requestNumber")!=null){
                            NodeList requestNumberList = appData.getElementsByTagNameNS("http://portal.fccland.ru/types/", "requestNumber");
                            Element requestNumber  = (Element) requestNumberList.item(0);
                            logger.info(" requestNumberList Length  "  +  requestNumberList.getLength() );
                            String requestNumberString = requestNumber.getTextContent();
                            ctx.setVariable("requestNumber", requestNumberString);
                            logger.info("requestNumber  "  + requestNumberString );
                        }

                    }
                }
                else{
                    ctx.setVariable("smevPool", false);
                    ctx.setVariable("status", response.packet.status);
                }


            } else if ((new QName("http://portal.fccland.ru/rt/", "getStatusResponse")).equals(response.action)){
                ctx.setVariable("smevPool", false);
                System.out.println(response.appData);

                Element appData = response.appData;
                if  ((appData.getElementsByTagNameNS("http://portal.fccland.ru/types/","errorMessage")).getLength()!=0){
                    logger.info("ErrorMessage != NULL!!!!! " + appData.getElementsByTagNameNS("http://portal.fccland.ru/types/","errorMessage"));
                    NodeList errorMessageList = appData.getElementsByTagNameNS("http://portal.fccland.ru/types/","errorMessage");
                    Element errorMessage  = (Element) errorMessageList.item(0);
                    logger.info("response.appdata  " + response.appData);
                    String errorMessageString = errorMessage.getTextContent();
                    ctx.setVariable("statusMessage", errorMessageString);
                    ctx.setVariable("smevPool", false);
                }
                else{

                NodeList statusResponseBeanList = appData.getElementsByTagNameNS("http://portal.fccland.ru/types/", "statusResponseBean");
                Element statusResponse = (Element) statusResponseBeanList.item(0);
                StatusResponseBean statusResponseBean =  XmlTypes.elementToBean(statusResponse, StatusResponseBean.class);
                /*if (!getStatusResponseMessageType.getErrorMessage().equals("ОК")){
                    ctx.setVariable("status", "ошибка");
                    ctx.setVariable("smevError", getStatusResponseMessageType.getErrorMessage());

                }
                else{   */
                 //   String status = appData.getStatusResponseBean().getStatus();
                    final OrderStatus status = OrderStatus.findByCode(statusResponseBean.getStatus());
                    logger.info("Message status " + status);
                    logger.info("Status message " + statusResponseBean.getStatusMessage());
                    switch (status) {
                        case OK:
                        case VERIFICATION:
                        case PROCESSING:
                        case PAYMENT_PENDING:
                        case PAYMENT:
                            ctx.setVariable("status", status.code);
                            ctx.setVariable("result_request", statusResponseBean.getStatusMessage());
                            ctx.setVariable("smevPool", true);
                            break;

                        default:
                        ctx.setVariable("status", status.code);
                        ctx.setVariable("result_request", statusResponseBean.getStatusMessage());
                        ctx.setVariable("smevPool", false);
                        if (response.enclosures != null) {
                            saveEnclosuresToContext("enclosureData", ctx, response.enclosures);
                        }

                }
                }

            }

    }

    private void saveEnclosuresToContext(String varName, ExchangeContext ctx, Enclosure[] enclosures) {
        if (enclosures == null) return;
        List<String> enclosureVars = new LinkedList<String>();
        for (int i = 0; i < enclosures.length; i++){
          String enclosureVar = varName + "_" + i;
          enclosureVars.add(enclosureVar);
          ctx.addEnclosure(enclosureVar, enclosures[i]);
        }
        ctx.setVariable(varName, Joiner.on(';').join(enclosureVars));
    }

    private String createAppData(ExchangeContext ctx) {

        if (ctx.getVariable("requestNumber")!=null)
        {
            String xmlRequestNumber = "<requestNumber xmlns=\"http://portal.fccland.ru/types/\">"+ctx.getVariable("requestNumber")+"</requestNumber>";
            logger.info("xmlRequestNumber " + xmlRequestNumber);
            return xmlRequestNumber;

          /*  final GetStatusRequestMessageType getStatusRequestMessageType = new GetStatusRequestMessageType();
            getStatusRequestMessageType.setRequestNumber((String)ctx.getVariable("requestNumber"));
            logger.info("getStatusRequestMessageType  :   " + new XmlTypes(GetStatusRequestMessageType.class).toXml(getStatusRequestMessageType) );

            return new XmlTypes(GetStatusRequestMessageType.class).toXml(getStatusRequestMessageType);  */
        }
        final CreateRequestBean requestBean = new CreateRequestBean();
        requestBean.setOkato((String) ctx.getVariable("okato"));
        requestBean.setOktmo((String) ctx.getVariable("oktmo"));
        requestBean.setRequestType((String) ctx.getVariable("requestType"));
        return new XmlTypes(CreateRequestBean.class).toXml(requestBean);
    }



}
