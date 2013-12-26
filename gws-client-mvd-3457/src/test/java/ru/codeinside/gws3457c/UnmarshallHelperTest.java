/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws3457c;

import junit.framework.Assert;
import org.junit.Test;
import ru.tower.mvd.response.addpayment.ResponseAdditionalPaymentRequest;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public class UnmarshallHelperTest {
    @Test
    public void testUnmarshalResponseAdditionalPaymentData() throws Exception {
        String data = "<?xml version=\"1.0\" encoding=\"WINDOWS-1251\"?>\n" +
                "<ОТВЕТ_НА_ЗАПРОС_СВЕДЕНИЙ_О_ПОЛУЧЕНИИ_ПЕНСИИ_НА_МЕСЯЦ_УСТАНОВЛЕНИЯ_ДОПЛАТЫ><ФИО><Фамилия>МИХАЙЛОВ</Фамилия><Имя>ЮРИЙ</Имя><Отчество>СЕРГЕЕВИЧ</Отчество></ФИО><СтраховойНомер>077-817-954 25</СтраховойНомер><ДатаПоСостояниюНа>08.08.2011</ДатаПоСостояниюНа><ДатаФормирования>28.09.2012</ДатаФормирования><НаличиеДанных>ДА</НаличиеДанных><Подразделение><Наименование>ГУ МВД России по г. Москве</Наименование><Регион>77</Регион></Подразделение><ЛичныйДокумент><Вид>ПЕНСИОННОЕ УДОСТОВЕРЕНИЕ</Вид><Номер>1234567890</Номер></ЛичныйДокумент><ВсеВыплаты><Выплата><ВидВыплаты>ГОСУДАРСТВЕННАЯ_ПЕНСИЯ</ВидВыплаты><СуммаВыплаты>3000</СуммаВыплаты></Выплата><Выплата><ВидВыплаты>ТРУДОВАЯ_ПЕНСИЯ</ВидВыплаты><СуммаВыплаты>2000</СуммаВыплаты></Выплата><Выплата><ВидВыплаты>ЕДВ_ГЕРОЯМ</ВидВыплаты><СуммаВыплаты>1000</СуммаВыплаты></Выплата><КоличествоВыплат>3</КоличествоВыплат></ВсеВыплаты></ОТВЕТ_НА_ЗАПРОС_СВЕДЕНИЙ_О_ПОЛУЧЕНИИ_ПЕНСИИ_НА_МЕСЯЦ_УСТАНОВЛЕНИЯ_ДОПЛАТЫ>";
        final ResponseAdditionalPaymentRequest additionalPaymentResponse = UnmarshallHelper.parseAdditionaPaymentResult(data);
        Assert.assertNotNull(additionalPaymentResponse);
        Assert.assertNotNull(additionalPaymentResponse.getФИО());
        Assert.assertEquals("МИХАЙЛОВ", additionalPaymentResponse.getФИО().getФамилия());
        Assert.assertEquals("ЮРИЙ", additionalPaymentResponse.getФИО().getИмя());
        Assert.assertEquals("СЕРГЕЕВИЧ", additionalPaymentResponse.getФИО().getОтчество());
        Assert.assertEquals("077-817-954 25", additionalPaymentResponse.getСтраховойНомер());
        Assert.assertEquals("08.08.2011", additionalPaymentResponse.getДатаПоСостояниюНа());
        Assert.assertEquals("28.09.2012", additionalPaymentResponse.getДатаФормирования());
        Assert.assertEquals("ДА", additionalPaymentResponse.getНаличиеДанных());
        Assert.assertEquals("ГУ МВД России по г. Москве", additionalPaymentResponse.getПодразделение().getНаименование());
        Assert.assertEquals("77", additionalPaymentResponse.getПодразделение().getРегион());
        Assert.assertEquals("ПЕНСИОННОЕ УДОСТОВЕРЕНИЕ", additionalPaymentResponse.getЛичныйДокумент().getВид());
        Assert.assertEquals(new BigInteger("1234567890"), additionalPaymentResponse.getЛичныйДокумент().getНомер());

        ResponseAdditionalPaymentRequest.ВсеВыплаты.Выплата payment = findPaymentByType( additionalPaymentResponse.getВсеВыплаты().getВыплата(), "ГОСУДАРСТВЕННАЯ_ПЕНСИЯ");
        Assert.assertEquals(new BigDecimal(3000), payment.getСуммаВыплаты());

        payment = findPaymentByType( additionalPaymentResponse.getВсеВыплаты().getВыплата(), "ТРУДОВАЯ_ПЕНСИЯ");
        Assert.assertEquals(new BigDecimal(2000), payment.getСуммаВыплаты());

        payment = findPaymentByType( additionalPaymentResponse.getВсеВыплаты().getВыплата(), "ЕДВ_ГЕРОЯМ");
        Assert.assertEquals(new BigDecimal(1000), payment.getСуммаВыплаты());
    }


    @Test
    public void testUnmarshalResponsePensionValue() throws Exception {
        String data = "<?xml version=\"1.0\" encoding=\"WINDOWS-1251\"?>\n" +
                "<ОТВЕТ_НА_ЗАПРОС_СВЕДЕНИЙ_О_ПОЛУЧЕНИИ_ПЕНСИИ_НА_МЕСЯЦ_УСТАНОВЛЕНИЯ_ДОПЛАТЫ><ФИО><Фамилия>МИХАЙЛОВ</Фамилия><Имя>ЮРИЙ</Имя><Отчество>СЕРГЕЕВИЧ</Отчество></ФИО><СтраховойНомер>077-817-954 25</СтраховойНомер><ДатаПоСостояниюНа>08.08.2011</ДатаПоСостояниюНа><ДатаФормирования>28.09.2012</ДатаФормирования><НаличиеДанных>ДА</НаличиеДанных><Подразделение><Наименование>ГУ МВД России по г. Москве</Наименование><Регион>77</Регион></Подразделение><ЛичныйДокумент><Вид>ПЕНСИОННОЕ УДОСТОВЕРЕНИЕ</Вид><Номер>1234567890</Номер></ЛичныйДокумент><ВсеВыплаты><Выплата><ВидВыплаты>ГОСУДАРСТВЕННАЯ_ПЕНСИЯ</ВидВыплаты><СуммаВыплаты>3000</СуммаВыплаты></Выплата><Выплата><ВидВыплаты>ТРУДОВАЯ_ПЕНСИЯ</ВидВыплаты><СуммаВыплаты>2000</СуммаВыплаты></Выплата><Выплата><ВидВыплаты>ЕДВ_ГЕРОЯМ</ВидВыплаты><СуммаВыплаты>1000</СуммаВыплаты></Выплата><КоличествоВыплат>3</КоличествоВыплат></ВсеВыплаты></ОТВЕТ_НА_ЗАПРОС_СВЕДЕНИЙ_О_ПОЛУЧЕНИИ_ПЕНСИИ_НА_МЕСЯЦ_УСТАНОВЛЕНИЯ_ДОПЛАТЫ>";
        final ResponseAdditionalPaymentRequest additionalPaymentResponse = UnmarshallHelper.parseAdditionaPaymentResult(data);
        Assert.assertNotNull(additionalPaymentResponse);
        Assert.assertNotNull(additionalPaymentResponse.getФИО());
        Assert.assertEquals("МИХАЙЛОВ", additionalPaymentResponse.getФИО().getФамилия());
        Assert.assertEquals("ЮРИЙ", additionalPaymentResponse.getФИО().getИмя());
        Assert.assertEquals("СЕРГЕЕВИЧ", additionalPaymentResponse.getФИО().getОтчество());
        Assert.assertEquals("077-817-954 25", additionalPaymentResponse.getСтраховойНомер());
        Assert.assertEquals("08.08.2011", additionalPaymentResponse.getДатаПоСостояниюНа());
        Assert.assertEquals("28.09.2012", additionalPaymentResponse.getДатаФормирования());
        Assert.assertEquals("ДА", additionalPaymentResponse.getНаличиеДанных());
        Assert.assertEquals("ГУ МВД России по г. Москве", additionalPaymentResponse.getПодразделение().getНаименование());
        Assert.assertEquals("77", additionalPaymentResponse.getПодразделение().getРегион());
        Assert.assertEquals("ПЕНСИОННОЕ УДОСТОВЕРЕНИЕ", additionalPaymentResponse.getЛичныйДокумент().getВид());
        Assert.assertEquals(new BigInteger("1234567890"), additionalPaymentResponse.getЛичныйДокумент().getНомер());

        ResponseAdditionalPaymentRequest.ВсеВыплаты.Выплата payment = findPaymentByType( additionalPaymentResponse.getВсеВыплаты().getВыплата(), "ГОСУДАРСТВЕННАЯ_ПЕНСИЯ");
        Assert.assertEquals(new BigDecimal(3000), payment.getСуммаВыплаты());

        payment = findPaymentByType( additionalPaymentResponse.getВсеВыплаты().getВыплата(), "ТРУДОВАЯ_ПЕНСИЯ");
        Assert.assertEquals(new BigDecimal(2000), payment.getСуммаВыплаты());

        payment = findPaymentByType( additionalPaymentResponse.getВсеВыплаты().getВыплата(), "ЕДВ_ГЕРОЯМ");
        Assert.assertEquals(new BigDecimal(1000), payment.getСуммаВыплаты());
    }


    private ResponseAdditionalPaymentRequest.ВсеВыплаты.Выплата findPaymentByType(List<ResponseAdditionalPaymentRequest.ВсеВыплаты.Выплата> payments, String paymentType) {
        for (ResponseAdditionalPaymentRequest.ВсеВыплаты.Выплата payment : payments) {
            if (payment.getВидВыплаты().equals(paymentType)) {
                return payment;
            }
        }
        return null;
    }
}
