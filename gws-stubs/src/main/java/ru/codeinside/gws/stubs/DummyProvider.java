/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.stubs;

import ru.codeinside.gws.api.AppData;
import ru.codeinside.gws.api.CryptoProvider;
import ru.codeinside.gws.api.Signature;
import ru.codeinside.gws.api.VerifyResult;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import java.io.InputStream;
import java.security.cert.X509Certificate;
import java.util.List;

final public class DummyProvider implements CryptoProvider {

  CryptoProvider delegate = new ru.codeinside.gws.crypto.cryptopro.CryptoProvider();

  @Override
  public VerifyResult verify(SOAPMessage message) {
    return new VerifyResult(null, null, null);
  }

  @Override
  public AppData normalize(List<QName> namespaces, String appData) {
    throw new UnsupportedOperationException();
  }

  @Override
  public String inject(List<QName> namespaces, AppData appData, X509Certificate certificate, byte[] signature) {
    throw new UnsupportedOperationException();
  }

  @Override
  public byte[] toPkcs7(Signature signature) {
    return delegate.toPkcs7(signature);
  }

  @Override
  public Signature fromPkcs7(byte[] pkcs7) {
    return delegate.fromPkcs7(pkcs7);
  }

  @Override
  public boolean validate(Signature signature, byte[] digest, byte[] content) {
    return delegate.validate(signature, digest, content);
  }

  @Override
  public boolean verifySignature(X509Certificate certificate, InputStream data, byte[] signature) {
    return false;
  }

  @Override
  public String signElement(String sourceXML, String elementName, String namespace, boolean removeIdAttribute, boolean signatureAfterElement, boolean inclusive) throws Exception {
    throw new UnsupportedOperationException();
  }

  @Override
  public byte[] digest(InputStream source) {
    return new byte[0];
  }

  @Override
  public void sign(SOAPMessage arg0) {

  }

}