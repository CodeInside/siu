/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui;


import org.glassfish.osgicdi.OSGiService;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.gws.api.CertificateVerifyClient;
import ru.codeinside.gws.api.VerifyCertificateResult;

import javax.ejb.Singleton;
import javax.inject.Inject;
import java.security.cert.X509Certificate;

@Singleton
public class CertificateVerifierImpl implements CertificateVerifier {

  @Inject
  @OSGiService(dynamic = true)
  private CertificateVerifyClient client;

  @Override
  public void verifyCertificate(X509Certificate certificate) throws CertificateInvalid {

    String wsdlLocation = AdminServiceProvider.get().getSystemProperty(CertificateVerifier.VERIFY_SERVICE_LOCATION);
    boolean isAllowVerify = AdminServiceProvider.getBoolProperty(CertificateVerifier.ALLOW_VERIFY_CERTIFICATE_PROPERTY);
    if (isAllowVerify) {
      try {
        VerifyCertificateResult result = client.verify(certificate, wsdlLocation);
        if (result.getCode() != 0) {
          throw new CertificateInvalid(result.getDescription());
        }
      } catch (Exception err) {
        throw new CertificateInvalid("Системная ошибка при проверке сертификата :" + err.getMessage());
      }
    }
  }
  
}
