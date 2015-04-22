/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.test;

import org.activiti.engine.delegate.DelegateExecution;
import org.glassfish.osgicdi.OSGiService;
import ru.codeinside.gses.activiti.ReceiptEnsurance;
import ru.codeinside.gws.api.AppData;
import ru.codeinside.gws.api.CryptoProvider;
import ru.codeinside.gws.api.ServiceDefinition;
import ru.codeinside.gws.api.ServiceDefinitionParser;
import ru.codeinside.gws.api.Signature;
import ru.codeinside.gws.api.VerifyResult;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import javax.naming.NamingException;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import java.io.InputStream;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.List;

@Singleton
public class ActivitiContext {

  @Produces
  public ReceiptEnsurance getEnsurance() {
    return new ReceiptEnsurance() {
      @Override
      public void completeReceipt(DelegateExecution delegateExecution, String deleteReason) {
      }
    };
  }

  @Produces
  @Named("doDbUpdate")
  public Boolean getDoDbUpdate() {
    return Boolean.TRUE;
  }


  @Produces
  @OSGiService(dynamic = true)
  public ServiceDefinitionParser produceServiceDefinitionParser() {
    return new ServiceDefinitionParser() {
      @Override
      public ServiceDefinition parseServiceDefinition(URL wsdlUrl) {
        throw new UnsupportedOperationException();
      }
    };
  }

  @Produces
  @OSGiService(dynamic = true)
  public CryptoProvider produceCryptoProvider() {
    return new CryptoProvider() {
      @Override
      public void sign(SOAPMessage soapMessage) {
        throw new UnsupportedOperationException();
      }

      @Override
      public VerifyResult verify(SOAPMessage soapMessage) {
        throw new UnsupportedOperationException();
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
        throw new UnsupportedOperationException();
      }

      @Override
      public Signature fromPkcs7(byte[] pkcs7) {
        throw new UnsupportedOperationException();
      }

      @Override
      public boolean validate(Signature signature, byte[] digest, byte[] content) {
        throw new UnsupportedOperationException();
      }

      @Override
      public boolean verifySignature(X509Certificate certificate, InputStream data, byte[] signature) {
        throw new UnsupportedOperationException();
      }

      @Override
      public String signElement(String sourceXML, String elementName, String namespace, boolean removeIdAttribute, boolean signatureAfterElement, boolean inclusive) throws Exception {
        throw new UnsupportedOperationException();
      }

      @Override
      public byte[] digest(InputStream source) {
        return new byte[0];
      }
    };
  }

  @PostConstruct
  void onStart() throws NamingException {
  }

  @PreDestroy
  void onStop() throws NamingException {
  }

}
