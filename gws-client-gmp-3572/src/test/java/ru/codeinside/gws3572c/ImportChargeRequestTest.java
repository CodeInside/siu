/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws3572c;


import junit.framework.Assert;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import ru.codeinside.gws.api.ClientRequest;
import ru.codeinside.gws.crypto.cryptopro.CryptoProvider;
import ru.codeinside.gws.stubs.DummyContext;
import xmltype.R;

import java.text.ParseException;

/**
 * Игнорируем этот тест т.к. возникли проблемы с определением NS
 */
@Ignore
public class ImportChargeRequestTest {

  private GMPClient3572 client;
  @Before
  public void setUp() throws Exception {
    client = new GMPClient3572();
    CryptoProvider cryptoProvider = new CryptoProvider();
    client.bindCryptoProvider(cryptoProvider);
  }

  @Test
  public void testChargeRequest() throws Exception {
    String expectedRequest = R.getTextResource("gmp/charge_reference.xml");
    ClientRequest clientRequest = client.createClientRequest(createContext());
    String requestWithoutSign = clientRequest.appData.replaceAll("[\\n\\r]", "")
        .replaceAll("<ds:Signature>.+?</ds:Signature>", "")
        .replaceAll("<PostBlock><ID>.+?</ID>", "<PostBlock><ID>13454</ID>");
    Assert.assertEquals(expectedRequest, requestWithoutSign);
  }

  private DummyContext createContext() throws ParseException {
    DummyContext ctx = new DummyContext();
    ctx.setVariable("postBlockSenderIdentifier", "20091d"); // идентификатор отправителя
    ctx.setVariable("ordinalNumber", "013400000011"); // идентификатор отправителя
    ctx.setVariable("postBlockTimeStamp", DateUtils.parseDate("25.07.2012 09:40:47", new String[]{"dd.MM.yyyy HH:mm:ss"})); // дата составления запроса

    ctx.setVariable("supplierOrgInfoName", "Управление Федеральной миграционной службы по Республике Татарстан");
    ctx.setVariable("supplierOrgInfoINN", "1655102196");
    ctx.setVariable("supplierOrgInfoKPP", "165501001");
    ctx.setVariable("accountAccount", "40101810800000010001");
    ctx.setVariable("bankBIK", "049205001");
    ctx.setVariable("bankName", "ГРКЦ НБ РТ г. Казани");


    //ctx.setVariable("chargeSupplierBillID", "19255500000000000079"); // Уникальный идентификатор счёта в ИСП
    ctx.setVariable("chargeBillDate", DateUtils.parseDate( "10.03.2011", new String[]{"dd.MM.yyyy"}));  //Дата выставления счёта
    ctx.setVariable("chargeBillFor", "Госпошлина за выдачу загранпаспорта");  //Дата выставления счёта
    ctx.setVariable("chargeTotalAmount", "1000,00");
    ctx.setVariable("chargeChangeStatus", "1"); /* Статус счёта 1 - новый  2 - изменение  3 - аннулирование */
    ctx.setVariable("chargeTreasureBranch", "УФК по Республике Татарстан");
    ctx.setVariable("chargeKBK", "19210806000011000110");
    ctx.setVariable("chargeOKATO", "92401000000");
    ctx.setVariable("chargeApplicationID", "455555");
    ctx.setVariable("chargeUnifiedPayerIdentifier", "0100000000006667775555643");

    ctx.setVariable("budgetIndexStatus", "0");
    ctx.setVariable("budgetPaymentType", "0");
    ctx.setVariable("budgetPurpose", "0");
    ctx.setVariable("budgetTaxPeriod", "0");
    ctx.setVariable("budgetTaxDocNumber", "0");
    ctx.setVariable("budgetTaxDocDate", "0");
    ctx.setVariable("payerType", "01");
    ctx.setVariable("payerPersonDocumentID2", "6667775555");
    ctx.setVariable("payerPersonCitizenshipID", "643");
    ctx.setVariable("operationType", "importCharge");
    return ctx;
  }
}
