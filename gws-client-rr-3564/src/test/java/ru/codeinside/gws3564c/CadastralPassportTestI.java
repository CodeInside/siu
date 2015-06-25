/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws3564c;


import com.sun.xml.ws.transport.http.client.HttpTransportPipe;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.codeinside.gws.api.ClientRequest;
import ru.codeinside.gws.api.ClientResponse;
import ru.codeinside.gws.api.Enclosure;
import ru.codeinside.gws.api.ExchangeContext;
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
import java.security.Signature;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.util.logging.Logger;

import static ru.codeinside.gws3564c.enclosure.grp.TestUtils.getDateValue;

//@Ignore (value = "Т.к. тест выполняется долго и для его завершения нужно связываьтся со службой поддержки сервиса")
public class CadastralPassportTestI extends Assert {


  private final Logger logger = Logger.getLogger(getClass().getName());
  private ExchangeContext ctx;


  @Before
  public void setUp() throws Exception {
    ctx = new DummyContext();
  }

  @Test
  public void test() throws Exception {
    InfoSystem pnzr01581 = new InfoSystem("PNZR01581",
                                          "Комплексная система предоставления государственных и муниципальных услуг Пензенской области");
    // 1) http://smevtest.fss.ru/fss/SvedRegisterNoPosob
    // 2) http://188.254.16.92:7777/gateway/services/SID0003123
    String RR_ADDRESS = "http://test-ext.fccland.ru/smev2/smevService2";


    CryptoProvider cryptoProvider = new CryptoProvider();
    XmlNormalizer xmlNormalizer = new XmlNormalizerImpl();
    ServiceDefinitionParser definitionParser = new ServiceDefinitionParser();
    RRclient rr = new RRclient();

    ServiceDefinition serviceDefinition = definitionParser.parseServiceDefinition(rr.getWsdlUrl());
    logger.info("def: " + serviceDefinition.services);
    ServiceDefinition.Service service = serviceDefinition.services
                                                         .get(new QName("http://portal.fccland.ru/rt/",
                                                                        "RosreestrService"));
//    logger.info("ports: " + service.ports);

    ClientRev111111 rev111111 = new ClientRev111111(definitionParser, cryptoProvider, xmlNormalizer, null);
    createGetCadastralPassportContext();
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
    if (enc != null) {
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

  private ExchangeContext createGetCadastralPassportContext() throws ParseException {
    ctx.setVariable("smevTest", "Первичный запрос");
    ctx.setVariable("okato", "29000000000"); // string
    ctx.setVariable("enclosure_request_type", "CADASTRAL_PASSPORT"); // string
    ctx.setVariable("oktmo", "01234567"); // string
    ctx.setVariable("requestType", "558103080000"); // string
    ctx.setVariable("declarantType", "GOVERNANCE"); // string
    ctx.setVariable("declKind", "357007000000"); // string
    ctx.setVariable("declGovernanceCode", "007001001001"); // string
    ctx.setVariable("declGovernanceName", "Пенсионный фонд"); // string
    ctx.setVariable("declGovernanceEmail", "test@test"); // string
    ctx.setVariable("declLocationRegion", "40"); // string
    ctx.setVariable("declLocationCityName", "Калуга"); // string
    ctx.setVariable("declLocationDCity", "г"); // string
    ctx.setVariable("declLocationStreetName", "Ленина"); // string
    ctx.setVariable("declLocationDStreets", "ул"); // string
    ctx.setVariable("declLocationLocationLevel1Type", "д"); // string
    ctx.setVariable("declLocationLocationLevel1Value", "5"); // string
    ctx.setVariable("declLocationLocationApartmentType", "кв"); // string
    ctx.setVariable("declLocationLocationApartmentValue", "32"); // string
    ctx.setVariable("declLocationLocationLocationOther", "Иное"); // string
    ctx.setVariable("declLocationLocationLocationNote", "Неформализованное описание"); // string
    ctx.setVariable("agentFIOFirst", "Антон"); // string
    ctx.setVariable("agentFIOSurname", "Антонов"); // string
    ctx.setVariable("agentFIOPatronymic", "Антонович"); // string
    ctx.setVariable("agentPDocumentCode", "008001001000"); // string
    ctx.setVariable("agentPDocumentSeries", "2222"); // string
    ctx.setVariable("agentPDocumentNumber", "222222"); // string
    ctx.setVariable("agentPDocumentDate", getDateValue("2010-10-10")); // string
    ctx.setVariable("agentPDocumentIssueOrgan", "УВД,Код подразделен"); // string
    ctx.setVariable("agentPostalCode", "113563"); // string
    ctx.setVariable("agentRegion", "40"); // string
    ctx.setVariable("agentCityName", "Калуга"); // string
    ctx.setVariable("agentDCity", "г"); // string
    ctx.setVariable("agentStreetName", "Советская"); // string
    ctx.setVariable("agentDStreets", "ул"); // string
    ctx.setVariable("agentLocationLevel1Type", "д"); // string
    ctx.setVariable("agentLocationLevel1Value", "10"); // string
    ctx.setVariable("agentLocationLevel2Type", "корп"); // string
    ctx.setVariable("agentLocationLevel2Value", "1"); // string
    ctx.setVariable("agentLocationLevel3Type", "литера"); // string
    ctx.setVariable("agentLocationLevel3Value", "а"); // string
    ctx.setVariable("agentLocationApartmentType", "к"); // string
    ctx.setVariable("agentLocationApartmentValue", "1"); // string
    ctx.setVariable("agentLocationOther", "Иное"); // string
    ctx.setVariable("agentLocationNote", "Неформализованное описание"); // string
    ctx.setVariable("agentEmail", "test@test.ru"); // string
    ctx.setVariable("agentPhone", "213-23-12"); // string
    ctx.setVariable("agentSNILS", "233-234-434 34"); // string
    ctx.setVariable("agentKind", "356003000000"); // string
    ctx.setVariable("applied", 1l);
    ctx.setVariable("payment", 0l);
    ctx.setVariable("appliedADocumentNumber_1", "26-0-1-21/4001/2011-162"); // string
    ctx.setVariable("appliedADocumentDate_1", getDateValue("2012-07-23")); // string
    ctx.setVariable("appliedADocumentOriginalQuantity_1", 1L); // string
    ctx.setVariable("appliedADocumentOriginalQuantitySheet_1", 1L); // string
    ctx.setVariable("appliedADocumentName_1",
                    "Запрос о предоставлении сведений, внесенных в государственный кадастр недвижимости"); // string
    ctx.setVariable("appliedADocumentCode_1", "558101010000"); // string
    ctx.setVariable("cadastralPassportObjKind", "002001001000"); // string
    ctx.setVariable("cadastralPassport", "558102100000"); // string
    ctx.setVariable("cadastralPassportRegion", "40"); // string
    ctx.setVariable("cadastralPassportCityName", "Калуга"); // string
    ctx.setVariable("cadastralPassportDCity", "г"); // string
    ctx.setVariable("cadastralPassportStreetName", "Брежнева"); // string
    ctx.setVariable("cadastralPassportDStreets", "ул"); // string
    ctx.setVariable("cadastralPassportLocationLevel1Value", "23"); // string
    ctx.setVariable("cadastralPassportLocationLevel1Type", "д"); // string
    ctx.setVariable("cadastralPassportLocationApartmentValue", "3"); // string
    ctx.setVariable("cadastralPassportLocationApartmentType", "кв"); // string
    return ctx;
  }

  void userSign(Enclosure enclosure) throws Exception {
    final long startMs = System.currentTimeMillis();
    final KeyStore keystore = KeyStore.getInstance("HDImageStore");
    keystore.load(null, null);
    final String certName = "RaUser-68ceb4ff-bdac-4fe6-9e61-1d55feedb744";
    final String certPass = "12345678";
    X509Certificate cert = ((X509Certificate) keystore.getCertificate(certName));
    assertNotNull(cert);
    PrivateKey privateKey = ((PrivateKey) keystore.getKey(certName, certPass.toCharArray()));
    assertNotNull(privateKey);
    Signature signature = Signature.getInstance("GOST3411withGOST3410EL");
    signature.initSign(privateKey);
    signature.update(enclosure.content);
    byte[] signed = signature.sign();
    enclosure.signature = new ru.codeinside.gws.api.Signature(cert, enclosure.content, signed, true);
  }
}


//<ns2:requestNumber>12-34719