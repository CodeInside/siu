/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui;

import java.security.cert.X509Certificate;


public interface CertificateVerifier {
  public static String ALLOW_VERIFY_CERTIFICATE_PROPERTY = "CertificateVerifier.allowVerify";
  public static String VERIFY_SERVICE_LOCATION = "CertificateVerifier.serviceLocation";
  public static String LINK_CERTIFICATE = "CertificateVerifier.linkCertificate";
  void verifyCertificate(X509Certificate certificate) throws CertificateInvalid;
}
