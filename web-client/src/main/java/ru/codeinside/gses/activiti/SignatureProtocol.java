/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti;

import com.vaadin.ui.Form;
import org.osgi.framework.ServiceReference;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.gses.activiti.forms.FormID;
import ru.codeinside.gses.activiti.forms.Signatures;
import ru.codeinside.gses.cert.NameParts;
import ru.codeinside.gses.cert.X509;
import ru.codeinside.gses.webui.CertificateInvalid;
import ru.codeinside.gses.webui.CertificateReader;
import ru.codeinside.gses.webui.CertificateVerifier;
import ru.codeinside.gses.webui.CertificateVerifyClientProvider;
import ru.codeinside.gses.webui.Flash;
import ru.codeinside.gses.webui.components.sign.SignApplet;
import ru.codeinside.gses.webui.components.sign.SignAppletListener;
import ru.codeinside.gses.webui.form.DataAccumulator;
import ru.codeinside.gses.webui.form.FormOvSignatureSeq;
import ru.codeinside.gses.webui.form.FormSpSignatureSeq;
import ru.codeinside.gses.webui.form.ProtocolUtils;
import ru.codeinside.gses.webui.osgi.Activator;
import ru.codeinside.gws.api.Client;
import ru.codeinside.gws.api.ClientProtocol;
import ru.codeinside.gws.api.CryptoProvider;
import ru.codeinside.gws.api.Enclosure;
import ru.codeinside.gws.api.Server;
import ru.codeinside.gws.api.ServerProtocol;
import ru.codeinside.gws.api.ServiceDefinition;
import ru.codeinside.gws.api.Signature;
import ru.codeinside.gws.api.WrappedAppData;
import ru.codeinside.gws.api.XmlSignatureInjector;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class SignatureProtocol implements SignAppletListener {
  private static final long serialVersionUID = 1L;

  final private FormID formID;
  final private String fieldId;
  final private String[] ids;
  final private byte[][] blocks;
  final private boolean[] files;
  final private byte[][] signs;
  final private String caption;
  final private Form form;
  final private DataAccumulator dataAccumulator;

  public SignatureProtocol(FormID formID, String fieldId, String caption, byte[][] blocks, boolean[] files, String[] ids, Form form, DataAccumulator dataAccumulator) {
    this.formID = formID;
    this.fieldId = fieldId;
    this.caption = caption;
    this.form = form;
    this.blocks = blocks;
    this.files = files;
    this.ids = ids;
    this.dataAccumulator = dataAccumulator;
    signs = new byte[blocks.length][];
  }

  public SignatureProtocol(FormID formID, String fieldId, String caption, byte[][] blocks, boolean[] files, String[] ids, Form form) {
    this(formID, fieldId, caption, blocks, files, ids, form, null);
  }

  @Override
  public void onLoading(SignApplet signApplet) {
  }

  @Override
  public void onNoJcp(SignApplet signApplet) {
    form.removeItemProperty(fieldId);
    ReadOnly field = new ReadOnly("В вашей операционной системе требуется установить КриптоПРО JCP", false);
    field.setCaption(caption);
    form.addField(fieldId, field);
  }

  @Override
  public void onCert(SignApplet signApplet, X509Certificate certificate) {
    boolean ok = false;
    String errorClause = null;
    try {
      boolean link = AdminServiceProvider.getBoolProperty(CertificateVerifier.LINK_CERTIFICATE);
      if (link) {
        byte[] x509 = AdminServiceProvider.get().withEmployee(Flash.login(), new CertificateReader());
        ok = Arrays.equals(x509, certificate.getEncoded());
      } else {
        ok = true;
      }
      CertificateVerifyClientProvider.getInstance().verifyCertificate(certificate);
    } catch (CertificateEncodingException e) {
    } catch (CertificateInvalid err) {
      errorClause = err.getMessage();
      ok = false;
    }
    if (ok) {
      signApplet.block(1, blocks.length);
    } else {
      form.removeItemProperty(fieldId);
      NameParts subject = X509.getSubjectParts(certificate);
      String fieldValue = (errorClause == null) ? "Сертификат " + subject.getShortName() + " отклонён" : errorClause;
      ReadOnly field = new ReadOnly(fieldValue, false);
      field.setCaption(caption);
      form.addField(fieldId, field);
    }
  }

  @Override
  public void onBlockAck(SignApplet signApplet, int i) {
    logger().fine("AckBlock:" + i);
    if (1 <= i && i <= blocks.length) {
      signApplet.chunk(1, 1, blocks[i - 1]);
    }
  }

  @Override
  public void onChunkAck(SignApplet signApplet, int i) {
    logger().fine("AckChunk:" + i);
    if (1 <= i && i <= blocks.length) {
      blocks[i - 1] = null;
    }
  }

  @Override
  public void onSign(SignApplet signApplet, byte[] sign) {
    final int i = signApplet.getBlockAck();
    logger().fine("done block:" + i);
    if (1 <= i && i <= blocks.length) {
      signs[i - 1] = sign;
      if (i < blocks.length) {
        signApplet.block(i + 1, blocks.length);
      } else {
        form.removeItemProperty(fieldId);
        NameParts subjectParts = X509.getSubjectParts(signApplet.getCertificate());
        FormSignaturesField field2 = new FormSignaturesField(subjectParts.getShortName(), new Signatures(formID, signApplet.getCertificate(), ids, files, signs));

        if (ids.length > 0 && ids[0].equals(FormSpSignatureSeq.SP_SIGN)) {
          Signatures spSignatures = (Signatures) field2.getValue();
          dataAccumulator.setSpSignatures(spSignatures);

          if (dataAccumulator.getServiceName() != null) {
            linkSignaturesWithEnclosures(signApplet, spSignatures, toList(dataAccumulator.getClientRequest().enclosures));
            if (dataAccumulator.getClientRequest().signRequired) {
              dataAccumulator.getClientRequest().appData = injectSignatureToAppData(spSignatures, dataAccumulator.getClientRequest().appData);
            }
          } else if (dataAccumulator.getRequestType() != null) {
            linkSignaturesWithEnclosures(signApplet, spSignatures, dataAccumulator.getServerResponse().attachmens);
            if (dataAccumulator.getServerResponse().signRequired) {
              dataAccumulator.getServerResponse().appData = injectSignatureToAppData(spSignatures, dataAccumulator.getServerResponse().appData);
            }
          }

          // если нет следующего шага, формируем сообщение и пишем clientRequest (или serverResponse для поставщика) в базу
          if (!dataAccumulator.isNeedOv()) {
            if (dataAccumulator.getClientRequest() != null) {
              buildClientRequest();
            } else if (dataAccumulator.getServerResponse() != null) {
              buildServerResponse();
            }
          }

        } else if (ids.length > 0 && ids[0].equals(FormOvSignatureSeq.OV_SIGN)) {
          Signatures ovSignatures = (Signatures) field2.getValue();
          dataAccumulator.setOvSignatures(ovSignatures);

          injectSignatureToSoapHeader(dataAccumulator.getSoapMessage(), ovSignatures);

          if (dataAccumulator.getServiceName() != null) {
            dataAccumulator.getClientRequest().requestMessage = ProtocolUtils.getBytesFromSoapMessage(dataAccumulator.getSoapMessage());
          } else if (dataAccumulator.getRequestType() != null) {
            dataAccumulator.getServerResponse().responseMessage = ProtocolUtils.getBytesFromSoapMessage(dataAccumulator.getSoapMessage());
          } else {
            throw new IllegalStateException("Ошибка в маршруте");
          }
        } else {
          Signatures signatures = field2.getValue() instanceof Signatures ? (Signatures) field2.getValue() : null;
          dataAccumulator.setSignatures(signatures);
        }

        field2.setCaption(caption);
        field2.setRequired(true);
        form.addField(fieldId, field2);
      }
    }
  }

  private List<Enclosure> toList(Enclosure[] enclosures) {
    if (enclosures != null) {
      return Arrays.asList(enclosures);
    }
    return null;
  }

  /**
   * Связать полученные подписи с вложениями
   * @param signApplet аплет подписания
   * @param spSignatures полученные сигнатуры
   */
  private void linkSignaturesWithEnclosures(SignApplet signApplet, Signatures spSignatures, List<Enclosure> enclosures) {
    if (enclosures != null) {
      for (Enclosure e : enclosures) {
        String propId = e.code != null && !e.code.isEmpty() ? e.code : e.fileName;
        int signPos = spSignatures.findSign(propId);
        if (signPos > -1) {
          e.signature = new Signature(signApplet.getCertificate(), e.content, spSignatures.signs[signPos], getDigest(e.content), true);
        }
      }
    }
  }

  private String injectSignatureToAppData(Signatures spSignatures, String appData) {
    if (appData == null) {
      return null;
    }

    ServiceReference serviceReference = null;
    try {
      serviceReference = Activator.getContext().getServiceReference(XmlSignatureInjector.class.getName());
      XmlSignatureInjector injector = (XmlSignatureInjector) Activator.getContext().getService(serviceReference);

      CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
      InputStream in = new ByteArrayInputStream(spSignatures.certificate);
      X509Certificate cert = (X509Certificate) certFactory.generateCertificate(in);
      byte[] signatureValue = spSignatures.signs[0];
      byte[] content = appData.getBytes(Charset.forName("UTF-8"));
      byte[] digest = getDigest(content);

      Signature signature = new Signature(cert, content, signatureValue, digest, true);
      WrappedAppData wrappedAppData = new WrappedAppData(appData, signature);
      return injector.injectSpToAppData(wrappedAppData);
    } catch (CertificateException e) {
      throw new RuntimeException("Injection signature to AppData error");
    } finally {
      if (serviceReference != null) {
        Activator.getContext().ungetService(serviceReference);
      }
    }
  }

  private void injectSignatureToSoapHeader(SOAPMessage soapMessage, Signatures ovSignatures) {
    ServiceReference serviceReference = null;
    try {
      serviceReference = Activator.getContext().getServiceReference(XmlSignatureInjector.class.getName());
      XmlSignatureInjector injector = (XmlSignatureInjector) Activator.getContext().getService(serviceReference);

      CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
      InputStream in = new ByteArrayInputStream(ovSignatures.certificate);
      X509Certificate cert = (X509Certificate) certFactory.generateCertificate(in);
      byte[] content = blocks[0];
      byte[] signatureValue = ovSignatures.signs[0];
      byte[] digest = getDigest(content);

      Signature signature = new Signature(cert, content, signatureValue, digest, true);

      injector.injectOvToSoapHeader(soapMessage, signature);

    } catch (CertificateException e) {
      throw new RuntimeException("Injection signature to SoapHeader error");
    } finally {
      if (serviceReference != null) {
        Activator.getContext().ungetService(serviceReference);
      }
    }
  }

  private byte[] getDigest(byte[] signedContent) {
    ServiceReference cryptoProviderReference = null;
    byte[] digest = null;
    try {
      cryptoProviderReference = Activator.getContext().getServiceReference(CryptoProvider.class.getName());
      if (cryptoProviderReference == null) {
        throw new IllegalStateException("Ошибка получения DigestValue: сервис CryptoProvider недоступен");
      }

      CryptoProvider cryptoProvider = ProtocolUtils.getService(cryptoProviderReference, CryptoProvider.class);
      ByteArrayInputStream is = new ByteArrayInputStream(signedContent);
      digest = cryptoProvider.digest(is);
    } finally {
      if (cryptoProviderReference != null) {
        Activator.getContext().ungetService(cryptoProviderReference);
      }
    }
    return digest;
  }

  private void buildClientRequest() {
    ServiceReference reference = null;
    ServiceReference cryptoReference = null;
    try {
      reference = ProtocolUtils.getServiceReference(dataAccumulator.getServiceName(), Client.class);
      final Client client = ProtocolUtils.getService(reference, Client.class);

      cryptoReference = Activator.getContext().getServiceReference(CryptoProvider.class.getName());
      final CryptoProvider crypto = (CryptoProvider) Activator.getContext().getService(cryptoReference);

      ClientProtocol clientProtocol = ProtocolUtils.getClientProtocol(client);

      SOAPMessage message = clientProtocol.createMessage(client.getWsdlUrl(),
          dataAccumulator.getClientRequest(), null, null);
      dataAccumulator.setSoapMessage(message);
      crypto.sign(message);
      dataAccumulator.getClientRequest().requestMessage = ProtocolUtils.getBytesFromSoapMessage(message);
    } catch (Exception e) {
      e.printStackTrace();
      throw new IllegalStateException("Ошибка получения подготовительных данных: " + e.getMessage(), e);
    } finally {
      if (reference != null) {
        Activator.getContext().ungetService(reference);
      }
      if (cryptoReference != null) {
        Activator.getContext().ungetService(cryptoReference);
      }
    }
  }

  private void buildServerResponse() {
    ServiceReference reference = null;
    ServiceReference cryptoReference = null;
    try {
      String serviceName = ProtocolUtils.getServerName(dataAccumulator.getTaskId());
      reference = ProtocolUtils.getServiceReference(serviceName, Server.class);
      final Server server = ProtocolUtils.getService(reference, Server.class);

      cryptoReference = Activator.getContext().getServiceReference(CryptoProvider.class.getName());
      final CryptoProvider crypto = (CryptoProvider) Activator.getContext().getService(cryptoReference);

      List<Object> serviceInfo = ProtocolUtils.getQNameAndServicePort(server);
      QName qName = (QName) serviceInfo.get(0);
      ServiceDefinition.Port port = (ServiceDefinition.Port) serviceInfo.get(1);

      ServerProtocol serverProtocol = ProtocolUtils.getServerProtocol(server);

      SOAPMessage message = serverProtocol.createMessage(
          dataAccumulator.getServerResponse(),
          qName, //Service
          port,  //Port
          null,  //Log
          null
      );
      dataAccumulator.setSoapMessage(message);
      crypto.sign(message);
      dataAccumulator.getServerResponse().responseMessage = ProtocolUtils.getBytesFromSoapMessage(message);
    } catch (RuntimeException e) {
      e.printStackTrace();
      throw new IllegalStateException("Ошибка получения подготовительных данных: " + e.getMessage(), e);
    } finally {
      if (reference != null) {
        Activator.getContext().ungetService(reference);
      }
      if (cryptoReference != null) {
        Activator.getContext().ungetService(cryptoReference);
      }
    }
  }

  private Logger logger() {
    return Logger.getLogger(getClass().getName());
  }
}
