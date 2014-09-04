/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.c.oep.dict;

import ru.codeinside.gws.api.Client;
import ru.codeinside.gws.api.ClientRequest;
import ru.codeinside.gws.api.ClientResponse;
import ru.codeinside.gws.api.ExchangeContext;
import ru.codeinside.gws.api.Packet;
import ru.codeinside.gws.api.Revision;
import ru.codeinside.gws.api.XmlTypes;
import ru.codeinside.gws.c.oep.dict.app.AppData;
import ru.codeinside.gws.c.oep.dict.data.DataRow;
import ru.codeinside.gws.c.oep.dict.data.Result;

import javax.xml.namespace.QName;
import java.net.URL;
import java.util.List;

final public class Dictionary implements Client {

  @Override
  public Revision getRevision() {
    return Revision.rev120315;
  }

  @Override
  public URL getWsdlUrl() {
    return getClass().getClassLoader().getResource("wsdl/universaldictionary.wsdl");
  }

  @Override
  public ClientRequest createClientRequest(ExchangeContext ctx) {
    ClientRequest request = new ClientRequest();
    request.packet = createPacket();
    request.action = new QName("http://dictionary.oep.com/", "getEntryList");
    request.appData = createAppData(ctx);
    return request;
  }

  private Packet createPacket() {
    Packet packet = new Packet();
    //packet.recipient = new InfoSystem("PNZR01581", "Комплексная система предоставления государственных и муниципальных услуг Пензенской области");
    packet.typeCode = Packet.Type.SERVICE;
    //packet.date = new Date();
    //packet.serviceName = "UniversalDictionary";
    packet.exchangeType = "1";
    packet.status = Packet.Status.REQUEST;
    return packet;
  }

  private String createAppData(ExchangeContext ctx) {
    DataRow dataRow = new DataRow();
    dataRow.setName("dictionaryName");
    dataRow.setValue((String) ctx.getVariable("dictionaryName"));
    Result result = new Result();
    result.getDataRow().add(dataRow);
    return new XmlTypes(Result.class).toXml(result);
  }

  @Override
  public void processClientResponse(ClientResponse response, ExchangeContext ctx) {
    AppData appData = XmlTypes.elementToBean(response.appData, AppData.class);
    Result result = appData.getResult().get(0);
    List<DataRow> rows = result.getDataRow();
    ctx.setVariable("values", rows.size());
    int i = 1;
    for (DataRow row : rows) {
      ctx.setVariable("name_" + i, row.getName());
      ctx.setVariable("value_" + i, row.getValue());
      i++;
    }
  }
}
