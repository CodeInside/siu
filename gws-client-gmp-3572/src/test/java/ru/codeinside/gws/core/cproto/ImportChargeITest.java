/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.core.cproto;

import com.sun.xml.ws.transport.http.client.HttpTransportPipe;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import ru.codeinside.gws.api.ClientRequest;
import ru.codeinside.gws.api.ClientResponse;
import ru.codeinside.gws.api.ExchangeContext;
import ru.codeinside.gws.api.InfoSystem;
import ru.codeinside.gws.api.XmlNormalizer;
import ru.codeinside.gws.crypto.cryptopro.CryptoProvider;
import ru.codeinside.gws.stubs.DummyContext;
import ru.codeinside.gws.wsdl.ServiceDefinitionParser;
import ru.codeinside.gws.xml.normalizer.XmlNormalizerImpl;
import ru.codeinside.gws3572c.GMPClient3572;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Date;
import java.util.logging.LogManager;

@Ignore
public class ImportChargeITest extends Assert {

  static {
    final InputStream is = ImportChargeITest.class.getClassLoader().getResourceAsStream("logging.properties");
    try {
      if (is != null) {
        LogManager.getLogManager().readConfiguration(is);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private GMPClient3572 client;
  public static final String SERVICE_ADDRESS = "http://188.254.16.92:7777/gateway/services/SID0003218?wsdl";
  private InfoSystem pnzr01581;
  private ClientRev111111 rev111111;

  private DummyContext createContextForErrorRequest() throws ParseException {
    DummyContext ctx = new DummyContext();
    ctx.setVariable("postBlockIdRequest", "13454"); // идентификатор запроса
    ctx.setVariable("postBlockSenderIdentifier", "000221"); // идентификатор отправителя
    //ctx.setVariable("postBlockSenderIdentifier", "20091d"); // идентификатор отправителя
    ctx.setVariable("ordinalNumber", "013400000011"); // идентификатор отправителя
    ctx.setVariable("postBlockTimeStamp", DateUtils.parseDate("25.07.2012 09:40:47", new String[]{"dd.MM.yyyy HH:mm:ss"})); // дата составления запроса

    ctx.setVariable("supplierOrgInfoName", "Управление Федеральной миграционной службы по Республике Татарстан");
    ctx.setVariable("supplierOrgInfoINN", "1655102196");
    ctx.setVariable("supplierOrgInfoKPP", "165501001");
    ctx.setVariable("accountAccount", "40101810800000010001");
    ctx.setVariable("bankBIK", "049205001");
    ctx.setVariable("bankName", "ГРКЦ НБ РТ г. Казани");


    ctx.setVariable("chargeSupplierBillID", "19255500000000000079"); // Уникальный идентификатор счёта в ИСП
    ctx.setVariable("chargeBillDate", DateUtils.parseDate("10.03.2011", new String[]{"dd.MM.yyyy"}));  //Дата выставления счёта
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
    ctx.setVariable("payerType", "1");
    ctx.setVariable("payerPersonDocumentID1", "01234567890");
    ctx.setVariable("operationType", "importCharge");
    return ctx;
  }


  @Before
  public void setUp() throws Exception {
    pnzr01581 = new InfoSystem("8201", "Комплексная система предоставления государственных и муниципальных услуг Пензенской области");
    CryptoProvider cryptoProvider = new CryptoProvider();
    XmlNormalizer xmlNormalizer = new XmlNormalizerImpl();
    rev111111 = new ClientRev111111(new ServiceDefinitionParser(), cryptoProvider, xmlNormalizer, null);
    client = new GMPClient3572();
    client.bindCryptoProvider(cryptoProvider);
    HttpTransportPipe.dump = true;
    ClientRev111111.validate = true;
  }

  @Test
  public void testOnUnregisteredSupplierId() throws Exception {

    ExchangeContext ctx = createContextForErrorRequest();
    ctx.setVariable("smevTest", "Первичный запрос");

    ClientRequest request = client.createClientRequest(ctx);

    request.portAddress = SERVICE_ADDRESS;
    request.packet.sender = request.packet.originator = pnzr01581;


    ClientResponse response = rev111111.send(client.getWsdlUrl(), request, null);
    client.processClientResponse(response, ctx);

    Assert.assertEquals("9500", ctx.getVariable("ticketPostBlockSenderIdentifier"));
    Assert.assertNotNull(ctx.getVariable("requestProcessResultErrorCode"));
    Assert.assertNotNull("Участник с таким идентификатором не зарегистрирован", ctx.getVariable("requestProcessResultErrorDescription"));
  }

  @Test
  public void testOnRegisteredSupplierIdAndGeneratedOrdinalId() throws Exception {

    ExchangeContext ctx = createContextForRequest();
    ctx.setVariable("ordinalNumber", null);
    ctx.setVariable("smevTest", "Первичный запрос");

    ClientRequest request = client.createClientRequest(ctx);

    request.portAddress = SERVICE_ADDRESS;
    request.packet.sender = request.packet.originator = pnzr01581;


    ClientResponse response = rev111111.send(client.getWsdlUrl(), request, null);
    client.processClientResponse(response, ctx);
    Assert.assertEquals(false, ctx.getVariable("smevPool"));  // повторять запрос не надо
    Assert.assertEquals(true, ctx.getVariable("responseSuccess")); // начисление должно пройти успешно
  }

  @Test
  public void testOnRegisteredSupplierIdAndDuplPostId() throws Exception {

    ExchangeContext ctx = createContextForRequest();
    ctx.setVariable("smevTest", "Первичный запрос");

    ClientRequest request = client.createClientRequest(ctx);

    request.portAddress = SERVICE_ADDRESS;
    request.packet.sender = request.packet.originator = pnzr01581;


    ClientResponse response = rev111111.send(client.getWsdlUrl(), request, null);
    client.processClientResponse(response, ctx);


    Assert.assertEquals(false, ctx.getVariable("smevPool"));  // повторять запрос не надо
    Assert.assertEquals(false, ctx.getVariable("responseSuccess")); // т.к. начисление уже существует

  }

  @Test
  public void testOnPNZData() throws Exception {

    ExchangeContext ctx = createContextForPnzRequest();
    ctx.setVariable("smevTest", "Первичный запрос");

    ClientRequest request = client.createClientRequest(ctx);

    request.portAddress = SERVICE_ADDRESS;
    request.packet.sender = request.packet.originator = pnzr01581;


    ClientResponse response = rev111111.send(client.getWsdlUrl(), request, null);
    client.processClientResponse(response, ctx);


    Assert.assertEquals(false, ctx.getVariable("smevPool"));  // повторять запрос не надо
    Assert.assertEquals(false, ctx.getVariable("responseSuccess")); // т.к. начисление уже существует

  }

  private ExchangeContext createContextForPnzRequest() throws ParseException {
    ExchangeContext ctx = new DummyContext();
    //ctx.setVariable("postBlockIdRequest", "134541"); // идентификатор запроса
    ctx.setVariable("postBlockSenderIdentifier", "002811"); // идентификатор отправителя
    //ctx.setVariable("ordinalNumber", "013400000011"); // порядковый номер запроса
    ctx.setVariable("postBlockTimeStamp", new Date()); // дата составления запроса

    ctx.setVariable("supplierOrgInfoName", "Управление информатизации Пензенской области");
    ctx.setVariable("supplierOrgInfoINN", "5836013428");
    ctx.setVariable("supplierOrgInfoKPP", "583601001");
    ctx.setVariable("accountAccount", "40201810000000000000");
    ctx.setVariable("accountKind", "1");

    ctx.setVariable("bankBIK", "045655001");
    ctx.setVariable("bankName", "ГУ ГРКЦ Банка России по Пензенской области");


    //  ctx.setVariable("chargeSupplierBillID", "19255500000000000079"); // Уникальный идентификатор счёта в ИСП
    ctx.setVariable("chargeBillDate", DateUtils.parseDate("10.03.2011", new String[]{"dd.MM.yyyy"}));  //Дата выставления счёта
    ctx.setVariable("chargeBillFor", "Госпошлина за выдачу загранпаспорта");  //Дата выставления счёта
    ctx.setVariable("chargeTotalAmount", "1000,00");
    ctx.setVariable("chargeChangeStatus", "1"); /* Статус счёта 1 - новый  2 - изменение  3 - аннулирование */
    ctx.setVariable("chargeTreasureBranch", "УФК по Пензенской области");
    ctx.setVariable("chargeKBK", "19210807100010034110");
    ctx.setVariable("chargeOKATO", "56401000000");
    // ctx.setVariable("chargeApplicationID", "455555");
    //ctx.setVariable("chargeUnifiedPayerIdentifier", "0100000000006667775555643");

    ctx.setVariable("budgetIndexStatus", "0");
    ctx.setVariable("budgetPaymentType", "тестовый платеж");
    ctx.setVariable("budgetPurpose", "0");    // основание платежа
    ctx.setVariable("budgetTaxPeriod", "0");
    ctx.setVariable("budgetTaxDocNumber", "0");
    ctx.setVariable("budgetTaxDocDate", "0");
    ctx.setVariable("payerType", "1");
    ctx.setVariable("payerPersonDocumentID1", "76384768389");
    ctx.setVariable("operationType", "importCharge");
    return ctx;
  }

  private ExchangeContext createContextForRequest() throws ParseException {
    ExchangeContext ctx = new DummyContext();
    ctx.setVariable("postBlockIdRequest", "134541"); // идентификатор запроса
    ctx.setVariable("postBlockSenderIdentifier", "20091d"); // идентификатор отправителя
    ctx.setVariable("ordinalNumber", "013400000011"); // порядковый номер запроса
    ctx.setVariable("postBlockTimeStamp", DateUtils.parseDate("25.07.2012 09:40:47", new String[]{"dd.MM.yyyy HH:mm:ss"})); // дата составления запроса

    ctx.setVariable("supplierOrgInfoName", "Минсоцзащиты Республики Мордовия");
    ctx.setVariable("supplierOrgInfoINN", "1326196216");
    ctx.setVariable("supplierOrgInfoKPP", "132601001");
    ctx.setVariable("accountAccount", "40201810900000000006");
    ctx.setVariable("accountKind", "1");

    ctx.setVariable("bankBIK", "049205001");
    ctx.setVariable("bankName", "ГРЦ НБ Респ.Мордовия Банка России г. Саранск");


    ctx.setVariable("chargeSupplierBillID", "19255500000000000079"); // Уникальный идентификатор счёта в ИСП
    ctx.setVariable("chargeBillDate", DateUtils.parseDate("10.03.2011", new String[]{"dd.MM.yyyy"}));  //Дата выставления счёта
    ctx.setVariable("chargeBillFor", "Госпошлина за выдачу загранпаспорта");  //Дата выставления счёта
    ctx.setVariable("chargeTotalAmount", "1000,00");
    ctx.setVariable("chargeChangeStatus", "1"); /* Статус счёта 1 - новый  2 - изменение  3 - аннулирование */
    ctx.setVariable("chargeTreasureBranch", "УФК по Республике Татарстан");
    ctx.setVariable("chargeKBK", "81301130925000013290");
    ctx.setVariable("chargeOKATO", "89401364000");
    // ctx.setVariable("chargeApplicationID", "455555");
    ctx.setVariable("chargeUnifiedPayerIdentifier", "0100000000006667775555643");

    ctx.setVariable("budgetIndexStatus", "0");
    ctx.setVariable("budgetPaymentType", "0");
    ctx.setVariable("budgetPurpose", "0");
    ctx.setVariable("budgetTaxPeriod", "0");
    ctx.setVariable("budgetTaxDocNumber", "0");
    ctx.setVariable("budgetTaxDocDate", "0");
    ctx.setVariable("payerType", "1");
    ctx.setVariable("payerPersonDocumentID1", "01234567890");
    ctx.setVariable("operationType", "importCharge");
    return ctx;
  }

}