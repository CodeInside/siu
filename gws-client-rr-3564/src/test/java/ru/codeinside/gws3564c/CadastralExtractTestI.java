/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws3564c;

import com.sun.xml.ws.transport.http.client.HttpTransportPipe;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import ru.codeinside.gws.api.ClientRequest;
import ru.codeinside.gws.api.ClientResponse;
import ru.codeinside.gws.api.Enclosure;
import ru.codeinside.gws.api.InfoSystem;
import ru.codeinside.gws.api.ServiceDefinition;
import ru.codeinside.gws.api.XmlNormalizer;
import ru.codeinside.gws.core.cproto.ClientRev111111;
import ru.codeinside.gws.crypto.cryptopro.CryptoProvider;
import ru.codeinside.gws.wsdl.ServiceDefinitionParser;
import ru.codeinside.gws.xml.normalizer.XmlNormalizerImpl;

import javax.xml.namespace.QName;
import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.logging.Logger;

@Ignore
public class CadastralExtractTestI extends Assert {

    private final Logger logger = Logger.getLogger(getClass().getName());



    @Test
    public void test() throws Exception {
        InfoSystem pnzr01581 = new InfoSystem("PNZR01581", "Комплексная система предоставления государственных и муниципальных услуг Пензенской области");
        // 1) http://smevtest.fss.ru/fss/SvedRegisterNoPosob
        // 2) http://188.254.16.92:7777/gateway/services/SID0003123
        String RR_ADDRESS = "http://test-ext.fccland.ru/smev2/smevService2";


        CryptoProvider cryptoProvider = new CryptoProvider();
        XmlNormalizer xmlNormalizer = new XmlNormalizerImpl();
        ServiceDefinitionParser definitionParser = new ServiceDefinitionParser();
        RRclient rr = new RRclient();

        ServiceDefinition serviceDefinition = definitionParser.parseServiceDefinition(rr.getWsdlUrl());
        logger.info("def: " + serviceDefinition.services);
        ServiceDefinition.Service service = serviceDefinition.services.get(new QName("http://portal.fccland.ru/rt/", "RosreestrService"));
        logger.info("ports: " + service.ports);


        ClientRev111111 rev111111 = new ClientRev111111(definitionParser, cryptoProvider, xmlNormalizer, null);


        DummyContext ctx = new DummyContext();

        ctx.setVariable("smevTest", "Первичный запрос");

        ctx.setVariable("okato", "29000000000");
        ctx.setVariable("oktmo", "01234567890");
        ctx.setVariable("requestType", "558101010000");
        ctx.setVariable("enclosure_request_type", "CADASTRAL_EXTRACT");



        //request
        //declarant
        ctx.setVariable("declarantType", "GOVERNANCE");
        ctx.setVariable("declKind", "357007000000");      //Required
        ctx.setVariable("declGovernanceName", "Пенсионный фонд");
        ctx.setVariable("declGovernanceCode", "007001001001");
        ctx.setVariable("declGovernanceEmail", "test@test.ru");
        //location
        ctx.setVariable("declGovernanceRegion", "40");             //required
        ctx.setVariable("declGovernanceCityName", "Калуга");        //required attr
        ctx.setVariable("declGovernanceDCity", "г");               //required attr
        ctx.setVariable("declGovernanceStreetName", "Ленина");    //req Attr
        ctx.setVariable("declGovernanceDStreets", "ул");            //req Attr
        ctx.setVariable("declGovernanceLocationLevel1Type", "д");      //req Attr
        ctx.setVariable("declGovernanceLocationLevel1Value", "5");      //req Attr
        ctx.setVariable("declGovernanceLocationApartmentType", "кв");     //req Attr
        ctx.setVariable("declGovernanceLocationApartmentValue", "32");     //req Attr




        //agent
        ctx.setVariable("agentFIOFirst", "Антон");
        ctx.setVariable("agentFIOSurname", "Антонов");
        ctx.setVariable("agentFIOPatronymic", "Антонович");
        ctx.setVariable("agentPDocumentCode", "008001001000");
        ctx.setVariable("agentPDocumentSeries", "2222");
        ctx.setVariable("agentPDocumentNumber", "222222");
        ctx.setVariable("agentPDocumentDate", "2010-10-10");
        ctx.setVariable("agentPDocumentIssueOrgan", "DocumentIssueOrgan");

        ctx.setVariable("agentPostalCode", "113563");
        ctx.setVariable("agentRegion", "40");             //required
        ctx.setVariable("agentCityName", "Калуга");        //required attr
        ctx.setVariable("agentDCity", "г");               //required attr
        ctx.setVariable("agentStreetName", "Советская");    //req Attr
        ctx.setVariable("agentDStreets", "ул");            //req Attr
        ctx.setVariable("agentLocationLevel1Type", "д");      //req Attr
        ctx.setVariable("agentLocationLevel1Value", "10");      //req Attr
        ctx.setVariable("agentLocationLevel2Type", "корп");      //req Attr
        ctx.setVariable("agentLocationLevel2Value", "1");      //req Attr
        ctx.setVariable("agentLocationLevel3Type", "литера");      //req Attr
        ctx.setVariable("agentLocationLevel3Value", "а");      //req Attr
        ctx.setVariable("agentLocationApartmentType", "к");     //req Attr
        ctx.setVariable("agentLocationApartmentValue", "1");     //req Attr
        ctx.setVariable("agentLocationOther", "Иное");     //req Attr
        ctx.setVariable("agentLocationNote", "Неформализованное описание");     //req Attr
        ctx.setVariable("agentEmail", "test@test.ru");
        ctx.setVariable("agentPhone", "213-23-12");
        ctx.setVariable("agentSNILS", "233-234-434 34");
        ctx.setVariable("agentKind", "356003000000");





        //required Data
        ctx.setVariable("KVObjKind", "002001001000");
        ctx.setVariable("KVRegion", "40");             //required
        ctx.setVariable("KVCityName", "Калуга");        //required attr
        ctx.setVariable("KVDCity", "г");               //required attr
        ctx.setVariable("KVStreetName", "Брежнева");    //req Attr
        ctx.setVariable("KVDStreets", "ул");
        ctx.setVariable("KVLocationLevel1Type", "д");      //req Attr
        ctx.setVariable("KVLocationLevel1Value", "23");      //req Attr
        ctx.setVariable("KVLocationApartmentType", "кв");     //req Attr
        ctx.setVariable("KVLocationApartmentValue", "3");     //req Attr

        ctx.setVariable("KV1", true);
        ctx.setVariable("KV2", true);
        ctx.setVariable("KV3", true);
        ctx.setVariable("KV4", true);
        ctx.setVariable("KV5", true);
        ctx.setVariable("KV6", true);

        //AppliedDocument
        ctx.setVariable("appliedACodeDocument", "558101010000");
        ctx.setVariable("appliedADocumentName", "Запрос о предоставлении сведений, внесенных в государственный кадастр недвижимости");
        ctx.setVariable("appliedADocumentNumber", "26-0-1-21/4001/2011-162");
        ctx.setVariable("appliedADocumentDate", "2012-07-23");
        // ctx.setVariable("appliedADocumentImageName", "demand_dcea0456-fb46-4714-9baf-d53ff76679d2_c57945ef-7476-49c2-aba5-e97686e8b60d.pdf");
        // ctx.setVariable("appliedADocumentImageURL", "C:\\md_arch\\OUT\\2012_07_23\\ee0b4ef0-f1b3-4353-993a-368a33bc6435Files\\");


        ctx.setVariable("appliedADocumentOriginalQuantity", "1");
        ctx.setVariable("appliedADocumentOriginalQuantitySheet", "1");





        ClientRequest request = rr.createClientRequest(ctx);

        request.portAddress = RR_ADDRESS;
        userSign(request.enclosures[0]);
        //request.packet.sender = request.packet.originator = pnzr01581;

        HttpTransportPipe.dump = true;


        ClientResponse response = rev111111.send(rr.getWsdlUrl(), request, null);

       /* logger.info("response error   " + response.verifyResult.error);
        logger.info("response action   " + response.action);
        logger.info("packet status   " + response.packet.status); */

        rr.processClientResponse(response, ctx);



        logger.info("smevPool   " + ctx.getVariable("smevPool"));
        while (Boolean.TRUE == ctx.getVariable("smevPool")) {
            System.out.println("Ждём 30 секунд...");
            Thread.sleep(30000);
            ctx.setVariable("smevTest", "Повторный запрос");
            request = rr.createClientRequest(ctx);
            request.portAddress = RR_ADDRESS;
            request.packet.sender = request.packet.originator = pnzr01581;
            response = rev111111.send(rr.getWsdlUrl(), request, null);

            rr.processClientResponse(response, ctx);
        }
        Enclosure enc = ctx.getEnclosure("ReceivedEnclosure");
        if (enc!=null)  {
            //x.fileName
            FileOutputStream fos = new FileOutputStream("target/" + enc.fileName);
            fos.write(enc.content);
        }
        // System.out.println("ФСС: " + response.verifyResult.actor.getSubjectDN());
     /*   assertNull("Ответ проходит через Ростелеком", response.verifyResult .recipient);
        assertEquals(Boolean.FALSE, ctx.getVariable("registering_response"));
        assertEquals(Boolean.TRUE, ctx.getVariable("obtainingGrants1_response"));
        assertEquals(Boolean.TRUE, ctx.getVariable("obtainingGrants2_response"));      */
    }

    void userSign(Enclosure enclosure) throws Exception {
        final long startMs = System.currentTimeMillis();
        final KeyStore keystore = KeyStore.getInstance("HDImageStore");
        keystore.load(null, null);
        final String certName  = "RaUser-fb87d12e-713e-46ea-ae0b-d141c2e21549";
        final String certPass = "12345678";
        X509Certificate cert = ((X509Certificate) keystore.getCertificate(certName));
        assertNotNull(cert);
        PrivateKey privateKey = ((PrivateKey) keystore.getKey(certName, certPass.toCharArray()));
        assertNotNull(privateKey);
        java.security.Signature signature = java.security.Signature.getInstance("GOST3411withGOST3410EL");
        signature.initSign(privateKey);
        signature.update(enclosure.content);
        byte[] signed = signature.sign();
        enclosure.signature = new ru.codeinside.gws.api.Signature(cert, enclosure.content, signed, true);
    }

}
