/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.core;

import org.junit.Assert;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.LogManager;

public class R {

  static {
    final InputStream is = R.class.getClassLoader().getResourceAsStream("logging.properties");
    try {
      if (is != null) {
        LogManager.getLogManager().readConfiguration(is);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static void init() {

  }

  public static SOAPMessage getSoapResource(final String resource) throws IOException, SOAPException {
    final InputStream stream = getRequiredResourceStream(resource);
    return MessageFactory.newInstance().createMessage(null, stream);
  }

  public static String getTextResource(final String resource) throws IOException {
    final InputStream stream = getRequiredResourceStream(resource);
    final ByteArrayOutputStream bos = new ByteArrayOutputStream();
    final byte[] buffer = new byte[1024];
    int read;
    while ((read = stream.read(buffer)) != -1) {
      bos.write(buffer, 0, read);
    }
    return new String(bos.toByteArray(), "UTF-8");
  }

  public static InputStream getRequiredResourceStream(final String resource) {
    final InputStream message = R.class.getClassLoader().getResourceAsStream(resource);
    Assert.assertNotNull("Не найден ресурс " + resource, message);
    return message;
  }

  public static URL getRequiredURL(final String resource) {
    final URL url = R.class.getClassLoader().getResource(resource);
    Assert.assertNotNull("Не найден ресурс " + resource, url);
    return url;
  }

}
