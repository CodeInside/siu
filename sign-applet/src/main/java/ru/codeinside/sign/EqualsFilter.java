/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.sign;

import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Arrays;

final class EqualsFilter implements Filter {

  final byte[] x509;

  public EqualsFilter(byte[] x509) {
    this.x509 = x509;
  }

  @Override
  public boolean accept(X509Certificate certificate) {
    byte[] encoded;
    try {
      encoded = certificate.getEncoded();
    } catch (CertificateEncodingException e) {
      return false;
    }
    return Arrays.equals(x509, encoded);
  }
}
