/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.crypto.cryptopro;

import org.junit.Assert;
import org.junit.Test;
import ru.codeinside.gws.api.VerifyResult;

import javax.xml.soap.SOAPMessage;
import java.io.FileOutputStream;

public class SignTest extends Assert {

  final boolean dump = true;

  /**
   * http://iecp.ru/news/item/382508/
   * <p/>
   * http://cryptopro.ru/blog/2012/07/02/podpis-soobshchenii-soap-dlya-smev-s-ispolzovaniem-kriptopro-jcp
   */
  @Test
  public void sign_then_verify() throws Exception {
    SOAPMessage soap = R.getSoapResource("message.xml");
    R.provider.sign(soap);
    if (dump) {
      FileOutputStream fos = new FileOutputStream("target/message-signed-" + System.currentTimeMillis() + ".xml");
      soap.writeTo(fos);
      fos.close();
    }

    final VerifyResult result = R.provider.verify(soap);
    assertNotNull(result.actor);
    assertNull(result.error);
  }

}
