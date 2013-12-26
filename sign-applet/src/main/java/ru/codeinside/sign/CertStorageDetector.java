/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.sign;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PrivilegedAction;
import java.security.cert.Certificate;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

final class CertStorageDetector implements PrivilegedAction<List<Cert>> {

  final String type;
  final Filter filter;

  CertStorageDetector(String type, Filter filter) {
    this.type = type;
    this.filter = filter;
  }

  @Override
  public List<Cert> run() {
    try {
      List<Cert> certs = new ArrayList<Cert>();
      KeyStore keyStore = KeyStore.getInstance(type + "Store", "JCP");
      keyStore.load(null, null);
      final Enumeration<String> aliases = keyStore.aliases();
      while (aliases.hasMoreElements()) {
        String alias = aliases.nextElement();
        X509Certificate certificate = getValidX509Certificate(keyStore, alias);
        if (certificate != null && filter.accept(certificate)) {
          certs.add(new Cert(type, alias, certificate));
        }
      }
      return certs;
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  X509Certificate getValidX509Certificate(KeyStore keyStore, String alias) throws KeyStoreException {
    Certificate certificate = keyStore.getCertificate(alias);
    if (certificate instanceof X509Certificate) {
      try {
        X509Certificate x509Certificate = (X509Certificate) certificate;
        x509Certificate.checkValidity();
        return x509Certificate;
      } catch (CertificateExpiredException e) {
        //
      } catch (CertificateNotYetValidException e) {
        //
      }
    }
    return null;
  }

}
