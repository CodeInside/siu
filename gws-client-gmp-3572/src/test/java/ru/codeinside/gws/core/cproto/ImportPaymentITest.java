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
import java.util.logging.LogManager;

@Ignore
public class ImportPaymentITest {
    static {
        final InputStream is = ImportPaymentITest.class.getClassLoader().getResourceAsStream("logging.properties");
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

    private DummyContext createContext() throws ParseException {
        DummyContext ctx = new DummyContext();
        ctx.setVariable("postBlockIdRequest", "54545"); // идентификатор запроса
        ctx.setVariable("postBlockSenderIdentifier", "044525225"); // идентификатор отправителя
        //ctx.setVariable("postBlockSenderIdentifier", "20091d"); // идентификатор отправителя
        ctx.setVariable("ordinalNumber", "013400000011"); // идентификатор отправителя
        ctx.setVariable("postBlockTimeStamp", DateUtils.parseDate("25.07.2012 09:40:47", new String[]{"dd.MM.yyyy HH:mm:ss"})); // дата составления запроса

        ctx.setVariable("supplierOrgInfoName", "Управление Федеральной миграционной службы по Республике Татарстан");
        ctx.setVariable("supplierOrgInfoINN", "1655102196");
        ctx.setVariable("supplierOrgInfoKPP", "165501001");

        ctx.setVariable("accountAccount", "40101810800000010001");


        ctx.setVariable("paymentSupplierBillID", "18810ЗA63КК560868ZZ6"); // Уникальный идентификатор счёта в ИСП
        ctx.setVariable("paymentNarrative", "Оплата Штрафа за административное правонарушение");  //Дата выставления счёта
        ctx.setVariable("paymentAmount", "800,00");
        ctx.setVariable("paymentDate", DateUtils.parseDate("06.12.2012", new String[]{"dd.MM.yyyy"}));  //Дата выставления счёта

        ctx.setVariable("paymentChangeStatus", "1"); /* Статус счёта 1 - новый  2 - изменение  3 - аннулирование */
        ctx.setVariable("paymentPayeeINN", "6317021970"); // ИНН получателя
        ctx.setVariable("paymentPayeeKPP", "631601001");  // КПП получателя
        ctx.setVariable("paymentKBK", "18811630000010000140");
        ctx.setVariable("paymentOKATO", "36401000000");

        ctx.setVariable("paymentBankName", "ГРКЦ ГУ банка России по Самарской области г.Самара");
        ctx.setVariable("paymentBIK", "043601001");
        ctx.setVariable("paymentSystemId", "BBDF8204867833");

        ctx.setVariable("budgetIndexStatus", "0");
        ctx.setVariable("budgetPaymentType", "0");
        ctx.setVariable("budgetPurpose", "0");
        ctx.setVariable("budgetTaxPeriod", "0");
        ctx.setVariable("budgetTaxDocNumber", "0");
        ctx.setVariable("budgetTaxDocDate", "0");

        ctx.setVariable("payerType", "1");
        ctx.setVariable("payerPersonDocumentID1", "01234567890");
        ctx.setVariable("operationType", "importPayment");
        ctx.setVariable("paymentPA", "12345678890");  // лицевой счет плательщика

        ctx.setVariable("paymentPayeeBankAcc", "12345678890");  // счет получателя
        ctx.setVariable("paymentPayeeBankAccKind", "1");  // тип счет получателя
        ctx.setVariable("paymentPayeeAccount", "40101810800000010001");
        ctx.setVariable("paymentPayeeBankBIK", "049205001");
        ctx.setVariable("paymentPayeeBankName", "ГРКЦ НБ РТ г. Казани");
        ctx.setVariable("paymentCorrespondentBankAccount", "40101810800000010001");

        ctx.setVariable("payeeAddDataBlock", 1L);
        ctx.setVariable("payeeAddDataKey_1", "key");
        ctx.setVariable("payeeAddDataValue_1", "value");
        return ctx;
    }


    @Before
    public void setUp() throws Exception {
        pnzr01581 = new InfoSystem("PNZR01581", "Комплексная система предоставления государственных и муниципальных услуг Пензенской области");
        CryptoProvider cryptoProvider = new CryptoProvider();
        XmlNormalizer xmlNormalizer = new XmlNormalizerImpl();
        rev111111 = new ClientRev111111(new ServiceDefinitionParser(), cryptoProvider, xmlNormalizer, null);
        client = new GMPClient3572();
        client.bindCryptoProvider (cryptoProvider);
        HttpTransportPipe.dump = true;
        ClientRev111111.validate = true;
    }


    @Test
    public void testImportPayment() throws Exception {
        ExchangeContext ctx = createContext();
        ctx.setVariable("smevTest", "Первичный запрос");

        ClientRequest request = client.createClientRequest(ctx);
        request.portAddress = SERVICE_ADDRESS;
        request.packet.sender = request.packet.originator = pnzr01581;

        ClientResponse response = rev111111.send(client.getWsdlUrl(), request, null);
        client.processClientResponse(response, ctx);

        Assert.assertEquals(false, ctx.getVariable("smevPool"));  // повторять запрос не надо
        Assert.assertEquals(false, ctx.getVariable("responseSuccess")); // т.к. платеж уже существует
        Assert.assertEquals("5", ctx.getVariable("requestProcessResultErrorCode"));

    }
}
