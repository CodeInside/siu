/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws3970c;

import org.junit.Assert;
import org.junit.Test;
import ru.codeinside.gws.api.ClientRequest;
import ru.codeinside.gws.api.XmlTypes;
import ru.codeinside.gws3970c.types.data.DataRow;
import ru.codeinside.gws3970c.types.data.Result;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UniversalClientTest extends Assert {
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
    assertEquals(appId, (Long)result.getParams().getAppId());
    List<DataRow> rows = result.getDataRow();
    assertEquals(dataRowParams.size(), rows.size());
    for (DataRow dataRow : rows) {
      assertEquals(dataRowParams.get(dataRow.getName()), dataRow.getValue());
    }
  }
}
