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
import sun.security.util.DerOutputStream;
import sun.security.x509.CRLDistributionPointsExtension;
import sun.security.x509.DistributionPoint;
import sun.security.x509.X509CertImpl;

import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.inject.Inject;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.CRLException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.List;

@Singleton
@Lock(LockType.READ)
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
        if ("local".equals(wsdlLocation)) {
          isRevoked(certificate);
        } else {
          VerifyCertificateResult result = client.verify(certificate, wsdlLocation);
          if (result.getCode() != 0) {
            throw new CertificateInvalid(result.getDescription());
          }
        }
      } catch (Exception err) {
        throw new CertificateInvalid("Системная ошибка при проверке сертификата: " + err.getMessage());
      }
    }
  }

  private void isRevoked(X509Certificate certificate) throws CertificateInvalid {
    DerOutputStream os = null;
    InputStream crlStream = null;
    try {
      CRLDistributionPointsExtension extension = ((X509CertImpl) certificate).getCRLDistributionPointsExtension();
      List<DistributionPoint> crlDistributionPoints = (List<DistributionPoint>) extension.get("points");
      for (DistributionPoint point : crlDistributionPoints) {
        os = new DerOutputStream();
        point.encode(os);

        String crlUrl = new String(os.toByteArray());
        crlUrl = crlUrl.substring(crlUrl.indexOf("http"));

        crlStream = new URL(crlUrl).openStream();

        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        X509CRL crl = (X509CRL) cf.generateCRL(crlStream);

        if (crl.isRevoked(certificate)) {
          throw new CertificateInvalid("Сертификат отозван");
        }
      }
    } catch (NullPointerException e) {
      throw new CertificateInvalid("Список отозванных сертификатов не найден");
    } catch (MalformedURLException e) {
      throw new CertificateInvalid("Не удалось получить список отозванных сертификатов");
    } catch (CertificateException e) {
      throw new CertificateInvalid("Не удалось получить список отозванных сертификатов");
    } catch (CRLException e) {
      throw new CertificateInvalid("Не удалось получить список отозванных сертификатов");
    } catch (IOException e) {
      throw new CertificateInvalid("Не удалось получить список отозванных сертификатов");
    } finally {
      close(os);
      close(crlStream);
    }
  }

  private void close(Closeable os) {
    if (os != null) {
      try {
        os.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

}
