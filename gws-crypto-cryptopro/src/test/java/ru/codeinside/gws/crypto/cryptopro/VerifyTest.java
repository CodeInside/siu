/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.crypto.cryptopro;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import ru.codeinside.gws.api.VerifyResult;

import javax.xml.soap.SOAPMessage;

public class VerifyTest extends Assert {

  @Test
  public void verify_bad() throws Exception {
    SOAPMessage soap = R.getSoapResource("message.xml");
    final VerifyResult result = R.provider.verify(soap);
    assertNull(result.actor);
    assertEquals("ЭЦП ИС: отсутствует", result.error);
  }

  @Test
  public void verify_changed() throws Exception {
    SOAPMessage soap = R.getSoapResource("smev-1.xml");
    final VerifyResult result = R.provider.verify(soap);
    assertNotNull(result.actor);
    assertEquals("Подпись не прошла проверку!", result.error);
  }

  @Test
  public void verify_ok() throws Exception {
    SOAPMessage soap = R.getSoapResource("smev-2.xml");
    final VerifyResult result = R.provider.verify(soap);
    assertNotNull(result.actor);
    assertNull("Нет искажений", result.error);
    assertEquals(
      "CN=КСПГМУ PNZ1, O=Управление информатизации Пензенской области, L=Пенза, ST=58 Пензенская область, C=RU, OID.1.2.643.3.131.1.1=5836013428, OID.1.2.643.100.1=1065836010778",
      result.actor.getSubjectDN().toString());
    assertNull(result.recipient);
  }

  @Test @Ignore
  public void verify_smev() throws Exception {
    SOAPMessage soap = R.getSoapResource("smev-3.xml");
    final VerifyResult result = R.provider.verify(soap);
    assertNull("Обнаружились искажения " + result.error, result.error);
    assertNotNull(result.actor);
    assertEquals(
      "SERIALNUMBER=00000000001, CN=СМЭВ_тест, OU=ФПД, O=ЗАО ЭйТи Консалтинг, L=Москва, ST=Москва, C=RU",
      result.recipient.getSubjectDN().toString());
    assertEquals(
      "T=0, STREET=ул. Воронцово поле д. 4а, CN=Росреестр, OU=Центральный аппарат, O=Росреестр, L=Москва, ST=77 г. Москва, C=RU, EMAILADDRESS=foiv@rosreestr.ru, OID.1.2.643.3.131.1.1=#120C303037373036353630353336, OID.1.2.643.100.1=#120D31303237373030313233323038",
      result.actor.getSubjectDN().toString());
  }


  @Test
  public void verifyCertDateExpired() throws Exception {
    SOAPMessage soap = R.getSoapResource("rr/response1.xml");
    final VerifyResult result = R.provider.verify(soap);
    assertEquals("Нет искажений", "Сертификат подписи не действителен", result.error);
    assertEquals("Нет роутера", null, result.recipient);
    assertEquals(
      "T=0, STREET=ул. Воронцово поле д. 4а, CN=Росреестр, OU=Центральный аппарат, O=Росреестр, L=Москва, ST=77 г. Москва, C=RU, EMAILADDRESS=foiv@rosreestr.ru, OID.1.2.643.3.131.1.1=#120C303037373036353630353336, OID.1.2.643.100.1=#120D31303237373030313233323038",
      result.actor.getSubjectDN().toString());
  }

  @Test @Ignore
  public void verifySmevResponseWithValidSignValue() throws Exception {
    SOAPMessage soap = R.getSoapResource("rr/response.xml");
    final VerifyResult result = R.provider.verify(soap);
    assertEquals("Нет искажений", "Сертификат подписи не действителен", result.error);
    assertEquals("Нет роутера", null, result.recipient);
    assertEquals(
        "T=0, STREET=ул. Воронцово поле д. 4а, CN=Росреестр, OU=Центральный аппарат, O=Росреестр, L=Москва, ST=77 г. Москва, C=RU, EMAILADDRESS=foiv@rosreestr.ru, OID.1.2.643.3.131.1.1=#120C303037373036353630353336, OID.1.2.643.100.1=#120D31303237373030313233323038",
        result.actor.getSubjectDN().toString());
  }
}
