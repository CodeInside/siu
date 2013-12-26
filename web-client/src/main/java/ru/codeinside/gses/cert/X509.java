/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.cert;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import java.util.logging.Logger;

final public class X509 {


  public static X509Certificate decode(final byte[] bytes) {
    try {
      CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
      InputStream in = new ByteArrayInputStream(bytes);
      return (X509Certificate) certFactory.generateCertificate(in);
    } catch (CertificateException e) {
      logger().log(Level.WARNING, "decode x509 certificate fail", e);
      return null;
    }
  }

  public static String getSubjectName(final X509Certificate certificate) {
    if (certificate != null) {
      return certificate.getSubjectDN().getName();
    }
    return "";
  }

  public static NameParts getSubjectParts(final byte[] bytes) {
    return getSubjectParts(decode(bytes));
  }

  public static NameParts getSubjectParts(final X509Certificate certificate) {
    return getNameParts(getSubjectName(certificate));
  }

  private static NameParts getNameParts(String dn) {
    return new NameParts(dn);
  }

  private X509() {

  }

  private static Logger logger() {
    return Logger.getLogger(X509.class.getName());
  }

}
