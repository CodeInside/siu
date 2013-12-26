/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.crypto.cryptopro;

import org.apache.xml.security.utils.Base64;
import org.junit.Assert;
import org.junit.Test;
import ru.codeinside.gws.api.AppData;

import javax.xml.namespace.QName;
import java.util.LinkedList;
import java.util.List;

public class NormalizeTest extends Assert {

  @Test
  public void smevBlock() throws Exception {
    String body = R.getTextResource("body1.xml");
    List<QName> namespaces = new LinkedList<QName>();
    namespaces.add(new QName("http://schemas.xmlsoap.org/soap/envelope/", "S"));
    namespaces.add(new QName("http://smev.gosuslugi.ru/rev110801", "smev"));
    AppData appData = R.provider.normalize(namespaces, body);
    assertEquals("fK7+KtBTCGFA0Yb+M68wNke/CAv/zOm5qL7ORFjVu48=", Base64.encode(appData.digest));
    assertEquals(R.getTextResource("body2.xml"), new String(appData.content, "UTF8"));

  }

  //TODO: нужен реальный тест с реальными данными!!!
  @Test
  public void normalizeThenInject() throws Exception {
    CryptoProvider.loadCertificate();

    String body = R.getTextResource("body1.xml");
    List<QName> namespaces = new LinkedList<QName>();
    namespaces.add(new QName("http://schemas.xmlsoap.org/soap/envelope/", "S"));
    namespaces.add(new QName("http://smev.gosuslugi.ru/rev110801", "smev"));
    AppData appData = R.provider.normalize(namespaces, body);

    byte[] sig = new byte[]{1, 2, 3, 4, 5, 6};
    String inject = R.provider.inject(namespaces, appData, CryptoProvider.cert, sig);
    assertTrue(inject.startsWith("<S:Body wsu:Id=\"body\"><Signature "));
  }

}
