/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package xmltype;


import junit.framework.Assert;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Before;
import org.junit.Test;
import ru.codeinside.gws.api.XmlTypes;
import ru.roskazna.xsd.budgetindex.BudgetIndex;
import ru.roskazna.xsd.charge.ChargeType;
import ru.roskazna.xsd.organization.Account;
import ru.roskazna.xsd.organization.Bank;
import ru.roskazna.xsd.organization.Organization;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

public class XMLTypeTest {

    private ChargeType charge;

    @Before
    public void setUp() throws Exception {
        charge = createCharge();
    }



    private ChargeType createCharge() throws ParseException {
        final ChargeType charge = new ChargeType();

        charge.setSupplierBillID("19255500000000000079"); // Уникальный идентификатор счёта в ИСП TODO уточнить, что этот параметр конечный пользователь должен знать (Bill.xsd)
        Date billDate = DateUtils.parseDate("2011-03-10", new String[]{"yyyy-MM-dd"});
        charge.setBillDate(XmlTypes.date(DateFormatUtils.format(billDate, "dd.MM.yyyy"))); //Дата выставления счёта

        final Organization supplierOrgInfo = new Organization();
        supplierOrgInfo.setName("Управление Федеральной миграционной службы по Республике Татарстан");
        supplierOrgInfo.setINN("1655102196");
        supplierOrgInfo.setKPP("165501001");
        final Account account = new Account();
        account.setAccount("40101810800000010001");
        final Bank bank = new Bank();
        bank.setBIK("049205001");
        bank.setName("ГРКЦ НБ РТ г. Казани");
        account.setBank(bank);
        supplierOrgInfo.setAccount(account);
        charge.setSupplierOrgInfo(supplierOrgInfo);
        charge.setBillFor("Госпошлина за выдачу загранпаспорта");
        charge.setTotalAmount(100000L);
        charge.setChangeStatus("1"); /* Статус счёта 1 - новый  2 - изменение  3 - аннулирование */
        charge.setTreasureBranch("УФК по Республике Татарстан"); // Орган ФК, на счёт которого должны поступать средства плательщика
        charge.setKBK("19210806000011000110");
        charge.setOKATO("92401000000");


        final BudgetIndex budgetIndex = new BudgetIndex();
        budgetIndex.setStatus("0"); //Статус плательщика //TODO уточнить  значения budgetIndex для этих полей оператор должен знать или они берутся и расшифровываются с использование справочника?
        budgetIndex.setPaymentType("0"); // тип платежа
        budgetIndex.setPurpose("0"); // основание платежа 2 символа максимум
        budgetIndex.setTaxPeriod("0"); // налоговый период до 10 символов
        budgetIndex.setTaxDocNumber("0"); // Показатель номера документа
        budgetIndex.setTaxDocDate("0");   // Показатель даты документа
        charge.setBudgetIndex(budgetIndex);

        charge.setApplicationID("455555"); //TODO уточнить, что этот идентификатор генерируется программой и оператор не должен о нем знать Уникальный идентификатор заявки до 20 символов
        charge.setUnifiedPayerIdentifier("0100000000006667775555643"); //  единый идентификатор плательщика
        return charge;
    }
}
