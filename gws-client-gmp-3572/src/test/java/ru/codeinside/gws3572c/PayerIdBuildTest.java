/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws3572c;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.codeinside.gws.api.ExchangeContext;

/**
 * Тестируется построение ид. плательщика
 */
public class PayerIdBuildTest {
  private ExchangeContext context;
  GMPClient3572 client;

  @Before
  public void setUp() throws Exception {
    context = new DummyContext();
    client = new GMPClient3572();
  }

  @Test
  public void testSNILS_PersonPayer() throws Exception {
    String suffix = "_1";
    context.setVariable("payerType" + suffix, "1");
    context.setVariable("payerPersonDocumentID1" + suffix, "12345678901");
    Assert.assertEquals("112345678901", client.buildUnifiedPayerID(context, suffix));
  }

  @Test
  public void testLegalResidentOfRussia() throws Exception {
    String suffix = "_1";
    context.setVariable("payerType" + suffix, "2");
    String inn = "1234567890";
    context.setVariable("payerLegalINN" + suffix, inn);
    String kpp = "123456789";
    context.setVariable("payerLegalKPP" + suffix, kpp);
    Assert.assertEquals("2" + inn + kpp, client.buildUnifiedPayerID(context, suffix));
  }

  @Test
  public void testLegalNoResidentOfRussia() throws Exception {
    String suffix = "_1";
    context.setVariable("payerType" + suffix, "3");
    String kio = "12345";
    context.setVariable("payerLegalNonResidentINN" + suffix, kio);
    String kpp = "123456789";
    context.setVariable("payerLegalNonResidentKPP" + suffix, kpp);
    Assert.assertEquals("3" + kio + kpp, client.buildUnifiedPayerID(context, suffix));
  }

  @Test
  public void testPersonDocument() throws Exception {
    String suffix = "_1";
    testBuildPersonPayerId(suffix, "24", "", "абв123567890", "payerPersonDocumentID18", "00000000АБВ123567890643");
    testBuildPersonPayerId(suffix, "23", "", "абв123567890", "payerPersonDocumentID17", "00000000АБВ123567890643");
    testBuildPersonPayerId(suffix, "22", "", "абв123567890", "payerPersonDocumentID16", "00000000АБВ123567890643");
    testBuildPersonPayerId(suffix, "21", "", "абв123567890", "payerPersonDocumentID15", "00000000АБВ123567890643");
    testBuildPersonPayerId(suffix, "13", "643", "абв123567890", "payerPersonDocumentID14", "00000000АБВ123567890");
    testBuildPersonPayerId(suffix, "12", "643", "абв123567890", "payerPersonDocumentID13", "00000000АБВ123567890");
    testBuildPersonPayerId(suffix, "11", "643", "абв123567890", "payerPersonDocumentID12", "00000000АБВ123567890");
    testBuildPersonPayerId(suffix, "10", "643", "абв123567890", "payerPersonDocumentID11", "00000000АБВ123567890");
    testBuildPersonPayerId(suffix, "09", "643", "абв123567890", "payerPersonDocumentID10", "00000000АБВ123567890");
    testBuildPersonPayerId(suffix, "08", "643", "абв123567890", "payerPersonDocumentID9", "00000000АБВ123567890");
    testBuildPersonPayerId(suffix, "07", "643", "абв123567890", "payerPersonDocumentID8", "00000000АБВ123567890");
    testBuildPersonPayerId(suffix, "06", "643", "абв123567890", "payerPersonDocumentID7", "00000000АБВ123567890");
    testBuildPersonPayerId(suffix, "05", "643", "абв123567890", "payerPersonDocumentID6", "00000000АБВ123567890");
    testBuildPersonPayerId(suffix, "04", "643", "абв123567890", "payerPersonDocumentID5", "00000000АБВ123567890");
    testBuildPersonPayerId(suffix, "03", "643", "абв123567890", "payerPersonDocumentID4", "00000000АБВ123567890");
    testBuildPersonPayerId(suffix, "02", "643", "абв123567890", "payerPersonDocumentID3", "00000000АБВ123567890");
    testBuildPersonPayerId(suffix, "01", "643", "1234-567890", "payerPersonDocumentID2", "00000000001234567890");
  }

  private void testBuildPersonPayerId(String suffix, String paymentType, String countryCode, String documentId, String documentField, String documentPartId) {
    String payerType = paymentType;
    context.setVariable("payerType" + suffix, payerType);
    context.setVariable(documentField + suffix, documentId);
    context.setVariable("payerPersonCitizenshipID" + suffix, countryCode);
    Assert.assertEquals(payerType + documentPartId + countryCode, client.buildUnifiedPayerID(context, suffix));
  }
}
