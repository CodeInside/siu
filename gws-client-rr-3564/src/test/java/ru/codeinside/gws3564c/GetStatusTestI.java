/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws3564c;

import com.sun.xml.ws.transport.http.client.HttpTransportPipe;
import junit.framework.Assert;
import org.junit.Ignore;
import org.junit.Test;
import ru.codeinside.gws.api.ClientRequest;
import ru.codeinside.gws.api.ClientResponse;
import ru.codeinside.gws.api.Enclosure;
import ru.codeinside.gws.api.InfoSystem;
import ru.codeinside.gws.api.XmlNormalizer;
import ru.codeinside.gws.core.cproto.ClientRev111111;
import ru.codeinside.gws.crypto.cryptopro.CryptoProvider;
import ru.codeinside.gws.wsdl.ServiceDefinitionParser;
import ru.codeinside.gws.xml.normalizer.XmlNormalizerImpl;

import java.util.Date;
import java.util.logging.Logger;

@Ignore
public class GetStatusTestI {

  private final Logger logger = Logger.getLogger(getClass().getName());

  @Test
  public void testCheckStatus() throws Exception {
    String requestNumber = "59-90";
    String RR_ADDRESS = "http://test-ext.fccland.ru/smev2/smevService2";
    InfoSystem pnzr01581 = new InfoSystem("PNZR01581", "Комплексная система предоставления государственных и муниципальных услуг Пензенской области");
    CryptoProvider cryptoProvider = new CryptoProvider();
    ServiceDefinitionParser definitionParser = new ServiceDefinitionParser();
    XmlNormalizer xmlNormalizer = new XmlNormalizerImpl();
    ClientRev111111 rev111111 = new ClientRev111111(definitionParser, cryptoProvider, xmlNormalizer, null);
    RRclient rr = new RRclient();
    DummyContext ctx = new DummyContext();
    ctx.setVariable("smevPool", true);
    ctx.setVariable("smevTest", "Первичный запрос");
    ctx.setVariable("requestNumber", requestNumber);
    ctx.setVariable("smevInitialRegDate", new Date());
    ClientRequest request = rr.createClientRequest(ctx);
    request.portAddress = RR_ADDRESS;
    request.packet.sender = request.packet.originator = pnzr01581;
    HttpTransportPipe.dump = true;
    ClientResponse response = rev111111.send(rr.getWsdlUrl(), request, null);
    rr.processClientResponse(response, ctx);
    logger.info("ctx status:  " + ctx.getVariable("status"));
    logger.info("ctx status:  " + ctx.getVariable("statusMessage"));

    String enclosureVars = (String) ctx.getVariable("enclosureData");
    Assert.assertNotNull(enclosureVars);

    for (String enclosureVar : enclosureVars.split(";")) {
      Enclosure enc = ctx.getEnclosure(enclosureVar);
      if (enc != null) {
        logger.info("enclosure not null  : " + enc.fileName);
       // FileOutputStream fos = new FileOutputStream("d:\\" + enc.fileName);
       // fos.write(enc.content);
      }
    }
  }
}


