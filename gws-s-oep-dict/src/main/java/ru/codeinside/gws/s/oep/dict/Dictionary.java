/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.s.oep.dict;

import ru.codeinside.gws.api.Internals;
import ru.codeinside.gws.api.Packet;
import ru.codeinside.gws.api.ReceiptContext;
import ru.codeinside.gws.api.RequestContext;
import ru.codeinside.gws.api.Revision;
import ru.codeinside.gws.api.ServerPipeline;
import ru.codeinside.gws.api.ServerRequest;
import ru.codeinside.gws.api.ServerResponse;
import ru.codeinside.gws.api.XmlTypes;
import ru.codeinside.gws.s.oep.dict.app.AppData;
import ru.codeinside.gws.s.oep.dict.data.DataRow;
import ru.codeinside.gws.s.oep.dict.data.Result;
import ru.codeinside.gws.s.oep.dict.data.SystemParams;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.net.URL;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

final public class Dictionary implements ServerPipeline {

  volatile Internals internals;

  @Override
  public Revision getRevision() {
    return Revision.rev120315;
  }

  @Override
  public URL getWsdlUrl() {
    return getClass().getClassLoader().getResource("wsdl/universaldictionary.wsdl");
  }

  @Override
  public ServerResponse processRequest(ServerRequest request) {
    String dictionaryId = null;
    String exceptRows = null;
    Map<String, String> dictionary = null;
    final AppData appData = XmlTypes.elementToBean(request.appData, AppData.class);
    if (appData != null) {
      final List<Result> result = appData.getResult();
      if (!result.isEmpty()) {
        Result requestData = result.get(0);
        dictionaryId = requestData.getRowValue("dictionaryName");
        exceptRows = requestData.getRowValue("exceptRows");
      }
    }
    if (dictionaryId != null) {
      dictionary = internals.getDictionary(dictionaryId);
    }
    if (dictionary != null && exceptRows != null) {
      for (final String part : exceptRows.split(",")) {
        final String key = part.trim();
        dictionary.remove(key);
      }
    }
    final Result result = new Result();
    if (dictionary != null) {
      final List<DataRow> dataRow = result.getDataRow();
      for (final Map.Entry<String, String> entry : dictionary.entrySet()) {
        final DataRow row = new DataRow();
        row.setName(entry.getKey());
        row.setValue(entry.getValue());
        dataRow.add(row);
      }
    } else {
      final SystemParams systemParams = new SystemParams();
      systemParams.setAppId(0);
      systemParams.setStatusCode(dictionaryId == null ? "Пропущен параметр dictionaryName" : "Справочник не найден");
      systemParams.setStatusDate(getCurrentXmlDate());
      result.setParams(systemParams);
    }
    final ServerResponse response = new ServerResponse();
    response.packet = new Packet();
    response.packet.status = dictionary != null ? Packet.Status.RESULT : Packet.Status.REJECT;
    response.packet.exchangeType = "1";
    response.packet.typeCode = Packet.Type.SERVICE;
    response.packet.serviceName = request.packet.serviceName;
    response.action = request.action;
    response.appData = XmlTypes.beanToXml(result);
    return response;
  }

  @Override
  public ServerResponse processRequest(final RequestContext ctx) {
    throw new UnsupportedOperationException();
  }


  @Override
  public ServerResponse processStatus(String statusMessage, ReceiptContext receiptContext) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ServerResponse processResult(String resultMessage, ReceiptContext receiptContext) {
    throw new UnsupportedOperationException();
  }

  // ----------------- internals ----------------

  protected void bindInternals(Internals internals) {
    this.internals = internals;
  }

  protected void unbindInternals(Internals internals) {
    this.internals = null;
  }

  private XMLGregorianCalendar getCurrentXmlDate() {
    try {
      return DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar());
    } catch (DatatypeConfigurationException e) {
      throw new IllegalStateException(e);
    }
  }

}
