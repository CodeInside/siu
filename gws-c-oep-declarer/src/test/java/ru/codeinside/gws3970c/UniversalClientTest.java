/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws3970c;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.namespace.QName;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import ru.codeinside.gws.api.ClientRequest;
import ru.codeinside.gws.api.ClientResponse;
import ru.codeinside.gws.api.ExchangeContext;
import ru.codeinside.gws.api.InfoSystem;
import ru.codeinside.gws.api.Packet.Status;
import ru.codeinside.gws.api.XmlTypes;
import ru.codeinside.gws.core.cproto.ClientRev120315;
import ru.codeinside.gws.crypto.cryptopro.CryptoProvider;
import ru.codeinside.gws.wsdl.ServiceDefinitionParser;
import ru.codeinside.gws3970c.types.data.DataRow;
import ru.codeinside.gws3970c.types.data.Result;

import com.sun.xml.ws.transport.http.client.HttpTransportPipe;

public class UniversalClientTest extends Assert {

  @Ignore
  @Test
  public void testCreateClientRequest() {
    DummyContext ctx = new DummyContext();
    Long appId = 52L;
    Map<String, String> dataRowParams = new HashMap<String, String>();
    dataRowParams.put("appData_FIO", "Иванов Иван Иванович");
    dataRowParams.put("appData_birthDay", "12.01.1956");
    dataRowParams.put("appData_addressRegister", "г Пенза улица Попова 36");
    dataRowParams.put("appData_toOrganizationName", "Codeinside");
    dataRowParams.put("appData_phone", "8908432422");
    dataRowParams.put("flowName", "RegisterForImproveLivingArea");
    dataRowParams.put("procedureCode", "123");
    for (String key : dataRowParams.keySet()) {
      ctx.setVariable(key, dataRowParams.get(key));
    }
    ctx.setVariable("app_id", appId);
    UniversalClient client = new UniversalClient();
    ClientRequest request = client.createClientRequest(ctx);
    Result result = new XmlTypes(Result.class).fromXml(Result.class, request.appData);
    assertEquals(appId, (Long) result.getParams().getAppId());
    List<DataRow> rows = result.getDataRow();
    assertEquals(dataRowParams.size(), rows.size());
    for (DataRow dataRow : rows) {
      assertEquals(dataRowParams.get(dataRow.getName()), dataRow.getValue());
    }
  }

  static Logger log = Logger.getLogger(UniversalClientTest.class.getName());

  @Ignore
  @Test
  public void test_checkBidStatus() throws Exception {
    InfoSystem pnzr01581 = new InfoSystem("PNZR01581",
        "Комплексная система предоставления государственных и муниципальных услуг Пензенской области");

    String SERVICE_ADDRESS = "http://localhost:8080/smev/mvvact";

    CryptoProvider cryptoProvider = new CryptoProvider();
    ServiceDefinitionParser definitionParser = new ServiceDefinitionParser();
    UniversalClient client = new UniversalClient();

    ClientRev120315 clientRev120315 = new ClientRev120315(definitionParser, cryptoProvider);

    ExchangeContext ctx = new DummyContext();

    ClientRequest request = client.createClientRequest(ctx);
    request.packet.sender = pnzr01581;
    request.packet.originator = pnzr01581;
    request.packet.status = Status.PING;
//    request.packet.originRequestIdRef = "f71e2c77-c459-4882-96ac-92fdf94996d4";
//    ctx.setVariable("internalRequestId", "502");
//    request.packet.originRequestIdRef = "056de988-52bc-477d-906a-828defb57de8";
//    ctx.setVariable("internalRequestId", "809");
    request.packet.originRequestIdRef = "8a328037-8be5-4bbe-841e-2b8487ae294c";
    ctx.setVariable("internalRequestId", "852");
    request.portAddress = SERVICE_ADDRESS;
    request.action = new QName("http://mvv.oep.com/", "updateStatus");
    HttpTransportPipe.dump = true;

    ClientResponse response = clientRev120315.send(client.getWsdlUrl(), request, null);

    client.processClientResponse(response, ctx);

    log.info("==== END ====");
  }
  
  @Test
  public void test_checkCancel() throws Exception {
    InfoSystem pnzr01581 = new InfoSystem("PNZR01581",
        "Комплексная система предоставления государственных и муниципальных услуг Пензенской области");

    String SERVICE_ADDRESS = "http://localhost:8080/smev/mvvact";

    CryptoProvider cryptoProvider = new CryptoProvider();
    ServiceDefinitionParser definitionParser = new ServiceDefinitionParser();
    UniversalClient client = new UniversalClient();

    ClientRev120315 clientRev120315 = new ClientRev120315(definitionParser, cryptoProvider);

    ExchangeContext ctx = new DummyContext();

    ClientRequest request = client.createClientRequest(ctx);
    request.packet.sender = pnzr01581;
    request.packet.originator = pnzr01581;
    request.packet.status = Status.CANCEL;
//    request.packet.originRequestIdRef = "f71e2c77-c459-4882-96ac-92fdf94996d4";
//    ctx.setVariable("internalRequestId", "502");
    request.packet.originRequestIdRef = "056de988-52bc-477d-906a-828defb57de8";
    ctx.setVariable("internalRequestId", "809");
//    request.packet.originRequestIdRef = "8a328037-8be5-4bbe-841e-2b8487ae294c";
//    ctx.setVariable("internalRequestId", "852");
    request.portAddress = SERVICE_ADDRESS;
    request.action = new QName("http://mvv.oep.com/", "updateStatus");
    HttpTransportPipe.dump = true;

    ClientResponse response = clientRev120315.send(client.getWsdlUrl(), request, null);

    client.processClientResponse(response, ctx);

    log.info("==== END ====");
  }

}
