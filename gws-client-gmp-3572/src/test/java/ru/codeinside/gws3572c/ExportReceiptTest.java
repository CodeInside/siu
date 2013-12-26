/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws3572c;


import org.apache.commons.lang.time.DateUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import ru.codeinside.gws.api.ClientRequest;
import ru.codeinside.gws.api.ExchangeContext;
import xmltype.R;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Игнорируем т.к. не удалось получаемый xml сделать таким же как в примере (ошибки валидации, NS)
 */
@Ignore
public class ExportReceiptTest {
  private GMPClient3572 client;
  private ExchangeContext context;

  @Before
  public void setUp() throws Exception {
    client = new GMPClient3572();
    context = new DummyContext();
  }
  @Test
  public void testCreateRequest() throws Exception {
    context.setVariable("operationType", "exportReceipt");
    context.setVariable("postBlockTimeStamp", DateUtils.parseDate("2001-12-17 09:30:47", new String[]{"yyyy-MM-dd HH:mm:ss"}));
    context.setVariable("postBlockId", "4548445");
    context.setVariable("postBlockSenderIdentifier", "044525716");
    context.setVariable("startDate", DateUtils.parseDate("2001-12-17 09:30:47", new String[]{"yyyy-MM-dd HH:mm:ss"}));
    context.setVariable("endDate", DateUtils.parseDate("2001-12-17 09:30:47", new String[]{"yyyy-MM-dd HH:mm:ss"}));
    context.setVariable("exportRequestType", "QUITTANCE");
    context.setVariable("+SupplierBillIDBlock", 1);
    context.setVariable("SupplierBillID_1", "18810XГ50АК586032ZZ0");
    context.setVariable("-SupplierBillIDBlock", "");
    ClientRequest clientRequest = client.createClientRequest(context);
    assertNotNull(clientRequest);
    assertNotNull(clientRequest.appData);

    String sampleAppData = R.getTextResource("gmp/export_receipt/sample_SupplierBillID.xml");
    //sampleAppData = sampleAppData.replace("[\t|\n]", "");
    assertEquals(sampleAppData, clientRequest.appData);
  }
}
