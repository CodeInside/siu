/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.crypto.cryptopro;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jcp.xml.dsig.internal.dom.XMLDSigRI;
import org.apache.ws.security.message.token.X509Security;
import org.apache.xml.security.Init;
import org.apache.xml.security.algorithms.MessageDigestAlgorithm;
import org.apache.xml.security.c14n.CanonicalizationException;
import org.apache.xml.security.c14n.Canonicalizer;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.signature.XMLSignatureInput;
import org.apache.xml.security.transforms.Transforms;
import org.apache.xml.security.utils.Base64;
import org.apache.xml.security.utils.Constants;
import org.apache.xml.security.utils.DigesterOutputStream;
import org.apache.xml.security.utils.XMLUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import ru.codeinside.gws.api.AppData;
import ru.codeinside.gws.api.Signature;
import ru.codeinside.gws.api.VerifyResult;

import javax.xml.bind.DatatypeConverter;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.stream.EventFilter;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

final public class CryptoProvider implements ru.codeinside.gws.api.CryptoProvider {


  static {
    if (!Init.isInitialized()) {
      Init.init();
    }
    SIGNATURE_FACTORY = XMLSignatureFactory.getInstance("DOM", new XMLDSigRI());
  }

  final private static String ENVELOP = "http://schemas.xmlsoap.org/soap/envelope/";
  final private static String WSU = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd";
  final private static String WSSE = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";
  final private static String WSS_X509V3 = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3";
  final private static String WSS_BASE64_BINARY = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary";
  final private static String ACTOR_RECIPIENT = "http://smev.gosuslugi.ru/actors/recipient";
  final private static String ACTOR_SMEV = "http://smev.gosuslugi.ru/actors/smev";
  final private static XMLSignatureFactory SIGNATURE_FACTORY;
  final private static Log log = LogFactory.getLog(CryptoProvider.class);
  final private static String DEFAULT_CERT_NAME = "KSPGMU";
  final private static String DEFAULT_CERT_PASS = "12345678";
  static transient boolean started;
  static PrivateKey privateKey;
  static X509Certificate cert;

  private static VerifyResult verifyMessage(final SOAPMessage message) throws Exception {
    final long startMs = System.currentTimeMillis();
    try {
      final SOAPPart doc = message.getSOAPPart();
      final SOAPEnvelope envelope = doc.getEnvelope();
      final SOAPHeader soapHeader = message.getSOAPHeader();
      final Element recipient = findSecurityToken(soapHeader, ACTOR_RECIPIENT);
      final X509Certificate recipientCert;
      if (recipient != null) {
        final ValidateResult recipientResult = validate(recipient);
        if (recipientResult.error != null) {
          return new VerifyResult("ЭЦП шлюза: " + recipientResult.error, null, recipientResult.cert);
        }
        recipientCert = recipientResult.cert;
      } else {
        recipientCert = null;
      }
      final Element smev = findSecurityToken(soapHeader, ACTOR_SMEV);
      if (smev == null) {
        return new VerifyResult("ЭЦП ИС: отсутствует", null, recipientCert);
      }
      final ValidateResult smevResult = validate(smev);
      return new VerifyResult(smevResult.error, smevResult.cert, recipientCert);
    } finally {
      if (log.isDebugEnabled()) {
        log.debug("VERIFY: " + (System.currentTimeMillis() - startMs) + "ms");
      }
    }
  }

  /**
   * Пока загружаем один раз и не обновляем.
   * <p/>
   * <p/>
   * Термин «шифрование на сертификате» происходит из алгоритма RSA. Там можно зашифровать сообщение на сертификате
   * получателя, так чтоб он своим закрытым ключом его расшифровал. С российскими алгоритмами так нельзя. Для эмуляции
   * шифрования на сертификате можно использовать такую схему:
   * <ol>
   * <li>Сначала надо создать эфемерную ключевую пару.</li>
   * <li>Потом выработать ключ согласования из эфемерного закрытого и сертификата получателя.</li>
   * <li>Создать случайный секретный ключ шифрования.</li>
   * <li>Этот секретный ключ зашифровать на ключе согласования.</li>
   * <li>Данные шифровать случайным секретным ключом.</li>
   * <li>Отправить получателю зашифрованные данные, зашифрованный секретный ключ и открытый ключ или его сертификат.</li>
   * </ol>
   * <p/>
   * Т.е. стандартная схема, как в примерах, только ключевая пара не постоянная, а эфемерная, одноразовая. Эта схема
   * позволяет шифровать сообщение для пользователя имея только его сертификат. Он сможет его расшифровать, но не
   * будет гарантированно знать, от кого оно пришло, может и от противника.
   * <p/>
   * Если создавать вместо эфемерной пары пользовательский постоянный ключ с сертификатом, то вся схема сохранится, но
   * получатель сможет гарантированно назвать отправителя.
   *
   * @throws KeyStoreException
   * @throws IOException
   * @throws CertificateException
   * @throws NoSuchAlgorithmException
   * @throws UnrecoverableKeyException
   */
  static void loadCertificate() throws KeyStoreException, NoSuchAlgorithmException, CertificateException,
          IOException, UnrecoverableKeyException {
    if (!started) {
      synchronized (CryptoProvider.class) {
        if (!started) {
          final long startMs = System.currentTimeMillis();
          final KeyStore keystore = KeyStore.getInstance("HDImageStore");
          keystore.load(null, null);

          final Properties properties = new Properties();
          properties.setProperty("name", DEFAULT_CERT_NAME);
          properties.setProperty("pass", DEFAULT_CERT_PASS);

          final File userHome = new File(System.getProperty("user.home"));
          final File keyFile = new File(userHome, "gses-key.properties");
          if (!keyFile.exists()) {
            log.warn(keyFile + " не обнаружен, используются ключи тестовой системы");
          } else {
            final FileInputStream is = new FileInputStream(keyFile);
            properties.load(is);
            is.close();
          }
          final String certName_ = properties.getProperty("name");
          final String certPass_ = properties.getProperty("pass");

          privateKey = ((PrivateKey) keystore.getKey(certName_, certPass_.toCharArray()));
          cert = ((X509Certificate) keystore.getCertificate(certName_));

          try {
            cert.checkValidity();
            log.info("Загружен действующий до " + cert.getNotAfter() + " сертификат " + cert.getSubjectDN().getName());
          } catch (CertificateExpiredException e) {
            log.error("Закончилось время действия для сертификата " + cert.getSubjectDN().getName());
            cert = null;
            privateKey = null;
          } catch (CertificateNotYetValidException e) {
            log.error("Не началось время действия для сертификата " + cert.getSubjectDN().getName());
            cert = null;
            privateKey = null;
          }
          if ((privateKey != null) && (cert != null)) {
            started = true;
          }
          if (log.isDebugEnabled()) {
            log.debug("LOAD CERTIFICATE: " + (System.currentTimeMillis() - startMs) + "ms");
          }
        }
      }
    }
  }

  /**
   * Накопление идентификаторов.
   */
  private static void fixWsuId(final Node node, final DOMValidateContext ctx, final Set<String> ids) {
    if (node instanceof Element) {
      final NamedNodeMap attributes = node.getAttributes();
      if (attributes != null) {
        final Node wsuId = attributes.getNamedItemNS(WSU, "Id");
        if (wsuId != null) {
          final String id = wsuId.getNodeValue();
          if (ids.contains(id)) {
            throw new RuntimeException("Нарушение уникальности " + node + " @" + wsuId);
          }
          ids.add(id);
          ctx.setIdAttributeNS((Element) node, WSU, "Id");
        }
      }
    }
    final NodeList children = node.getChildNodes();
    if (children != null) {
      for (int i = 0; i < children.getLength(); i++) {
        fixWsuId(children.item(i), ctx, ids);
      }
    }
  }

  private static ValidateResult validate(final Element securityToken) throws Exception {
    final X509Security x509 = new X509Security(securityToken);
    final X509Certificate cert = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(
      new ByteArrayInputStream(x509.getToken()));
    if (cert == null) {
      return new ValidateResult("Не найден cертификат подписи", null);
    }
    try {
      cert.checkValidity();
    } catch (CertificateException e) {
      return new ValidateResult("Сертификат подписи не действителен", cert);
    }
    final Element signature = first(securityToken.getParentNode(), Constants.SignatureSpecNS, "Signature");
    if (signature == null) {
      return new ValidateResult("Не найден элемент подписи", cert);
    }
    final DOMValidateContext ctx = new DOMValidateContext(cert.getPublicKey(), signature);
    fixWsuId(securityToken.getOwnerDocument(), ctx, new HashSet<String>());
    final boolean valid = SIGNATURE_FACTORY.unmarshalXMLSignature(ctx).validate(ctx);
    return new ValidateResult(valid ? null : "Подпись не прошла проверку!", cert);
  }

  private static Element findSecurityToken(final Element holder, final String actor) {
    final Element security = first(holder, WSSE, "Security", ENVELOP, "actor", actor);
    if (security != null) {
      return first(security, WSSE, "BinarySecurityToken");
    }
    return null;
  }

  private static Element first(final Node parent, final String uri, final String localName) {
    final NodeList nodes = parent.getChildNodes();
    final int n = nodes.getLength();
    for (int i = 0; i < n; i++) {
      final Node node = nodes.item(i);
      if (node instanceof Element) {
        final Element element = (Element) node;
        if (localName.equals(element.getLocalName()) && uri.equals(element.getNamespaceURI())) {
          return element;
        }
      }
    }
    return null;
  }

  private static Element first(final Node parent, final String uri, final String localName, final String attrUri,
                               final String attr, final String attrValue) {
    final NodeList nodes = parent.getChildNodes();
    final int n = nodes.getLength();
    for (int i = 0; i < n; i++) {
      final Node node = nodes.item(i);
      if (node instanceof Element) {
        final Element element = (Element) node;
        if (localName.equals(element.getLocalName()) && uri.equals(element.getNamespaceURI())
          && attrValue.equals(element.getAttributeNS(attrUri, attr))) {
          return element;
        }
      }
    }
    return null;
  }

  public void sign(final SOAPMessage message) {
    try {
      loadCertificate();

      final long startMs = System.currentTimeMillis();

      final SOAPPart doc = message.getSOAPPart();
      final QName wsuId = doc.getEnvelope().createQName("Id", "wsu");
      final SOAPHeader header = message.getSOAPHeader();
      final QName actor = header.createQName("actor", header.getPrefix());

      final SOAPElement security = header.addChildElement("Security", "wsse", WSSE);
      security.addAttribute(actor, ACTOR_SMEV);
      SOAPElement binarySecurityToken = security.addChildElement("BinarySecurityToken", "wsse");
      binarySecurityToken.setAttribute("EncodingType", WSS_BASE64_BINARY);
      binarySecurityToken.setAttribute("ValueType", WSS_X509V3);
      binarySecurityToken.setValue(DatatypeConverter.printBase64Binary(cert.getEncoded()));
      binarySecurityToken.addAttribute(wsuId, "CertId");

      XMLSignature signature = new XMLSignature(doc, "", XMLSignature.ALGO_ID_SIGNATURE_GOST_GOST3410_3411, Canonicalizer.ALGO_ID_C14N_EXCL_OMIT_COMMENTS);
      {
        Element element = signature.getElement();
        Element keyInfo = doc.createElementNS(Constants.SignatureSpecNS, "KeyInfo");
        Element securityTokenReference = doc.createElementNS(WSSE, "SecurityTokenReference");
        Element reference = doc.createElementNS(WSSE, "Reference");
        reference.setAttribute("URI", "#CertId");
        reference.setAttribute("ValueType", WSS_X509V3);
        securityTokenReference.appendChild(reference);
        keyInfo.appendChild(securityTokenReference);
        element.appendChild(keyInfo);
        security.appendChild(element);
      }
      Transforms transforms = new Transforms(doc);
      transforms.addTransform(Canonicalizer.ALGO_ID_C14N_EXCL_OMIT_COMMENTS);
      signature.addDocument("#body", transforms, MessageDigestAlgorithm.ALGO_ID_DIGEST_GOST3411);
      signature.sign(privateKey);
      if (log.isDebugEnabled()) {
        log.debug("SIGN: " + (System.currentTimeMillis() - startMs) + "ms");
      }
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String signElement(String sourceXML, String elementName, String namespace, boolean removeIdAttribute, boolean signatureAfterElement, boolean inclusive)
      throws Exception {
    loadCertificate();
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    dbf.setIgnoringElementContentWhitespace(true);
    dbf.setCoalescing(true);
    dbf.setNamespaceAware(true);
    DocumentBuilder documentBuilder = dbf.newDocumentBuilder();

    InputSource is = new InputSource(new StringReader(sourceXML));
    Document doc = documentBuilder.parse(is);
    Element elementForSign = (Element) doc.getElementsByTagNameNS(namespace, elementName).item(0);

    Node parentNode = null;
    Element detachedElementForSign;
    Document detachedDocument;
//    if (!elementForSign.isSameNode(doc.getDocumentElement())) {
//      parentNode = elementForSign.getParentNode();
//      parentNode.removeChild(elementForSign);
//
//      detachedDocument = documentBuilder.newDocument();
//      Node importedElementForSign = detachedDocument.importNode(elementForSign, true);
//      detachedDocument.appendChild(importedElementForSign);
//      detachedElementForSign = detachedDocument.getDocumentElement();
//    } else {
      detachedElementForSign = elementForSign;
      detachedDocument = doc;
//    }

    String signatureMethodUri = inclusive ? "urn:ietf:params:xml:ns:cpxmlsec:algorithms:gostr34102001-gostr3411" : "http://www.w3.org/2001/04/xmldsig-more#gostr34102001-gostr3411";
    String canonicalizationMethodUri = inclusive ? "http://www.w3.org/TR/2001/REC-xml-c14n-20010315" : "http://www.w3.org/2001/10/xml-exc-c14n#";
    XMLSignature sig = new XMLSignature(detachedDocument, "", signatureMethodUri, canonicalizationMethodUri);

    String id = (detachedElementForSign.getAttribute("Id") != null) ? detachedElementForSign.getAttribute("Id") : detachedElementForSign.getTagName();
    if (!removeIdAttribute) {
      detachedElementForSign.setAttributeNS(null, "Id", id);
      Attr idAttr = detachedElementForSign.getAttributeNode("Id");
      detachedElementForSign.setIdAttributeNode(idAttr, true);
    }

    if (signatureAfterElement)
      detachedElementForSign.insertBefore(sig.getElement(), detachedElementForSign.getLastChild().getNextSibling());
    else {
      detachedElementForSign.insertBefore(sig.getElement(), detachedElementForSign.getFirstChild());
    }
    Transforms transforms = new Transforms(detachedDocument);
    transforms.addTransform("http://www.w3.org/2000/09/xmldsig#enveloped-signature");
    transforms.addTransform(inclusive ? "http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments" : "http://www.w3.org/2001/10/xml-exc-c14n#");

    String digestURI = inclusive ? "urn:ietf:params:xml:ns:cpxmlsec:algorithms:gostr3411" : "http://www.w3.org/2001/04/xmldsig-more#gostr3411";
    sig.addDocument(removeIdAttribute ? "" : "#" + id, transforms, digestURI);
    sig.addKeyInfo(cert);
    sig.sign(privateKey);

    if ((!elementForSign.isSameNode(doc.getDocumentElement())) && (parentNode != null)) {
      Node signedNode = doc.importNode(detachedElementForSign, true);
      parentNode.appendChild(signedNode);
    }

    TransformerFactory tf = TransformerFactory.newInstance();
    Transformer trans = tf.newTransformer();
    trans.setOutputProperty("omit-xml-declaration", "yes");
    StringWriter stringWriter = new StringWriter();
    StreamResult streamResult = new StreamResult(stringWriter);
    trans.transform(new DOMSource(doc), streamResult);
    return stringWriter.toString();
  }

  @Override
  public byte[] digest(InputStream source) {
    try {
      MessageDigest md = MessageDigest.getInstance("GOST3411");
      int count;
      byte[] buff = new byte[1024];
      while ((count = source.read(buff, 0, buff.length)) > 0) {
        md.update(buff, 0, count);
      }
      return md.digest();
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public VerifyResult verify(final SOAPMessage message) {
    try {
      return verifyMessage(message);
    } catch (final RuntimeException e) {
      throw e;
    } catch (final Exception e) {
      final Throwable cause = e.getCause();
      if (cause instanceof RuntimeException) {
        throw (RuntimeException) cause;
      }
      throw new RuntimeException(e);
    }
  }

  @Override
  @Deprecated
  public AppData normalize(List<QName> namespaces, String appData) {
    try {
      final Document doc = createDocumentFromFragment(namespaces, appData);
      NodeList childNodes = doc.getDocumentElement().getChildNodes();
      Element body = (Element) childNodes.item(0);
      String _id;
      Attr id = body.getAttributeNodeNS(WSU, "Id");
      if (id == null) {
        _id = "AppData";
        body.setAttributeNS(WSU, "Id", _id);
      } else {
        _id = id.getValue();
      }
      final Transforms transforms = new Transforms(doc);
      // пока не добавляем ds:Signature, поэтому и не фильтруем
      // Element signature = doc.createElementNS(Constants.SignatureSpecNS, Constants._TAG_SIGNATURE);
      // signature = (Element) body.insertBefore(signature, body.getFirstChild());
      // transforms.setElement(signature, _id);
      // transforms.addTransform(Transforms.TRANSFORM_ENVELOPED_SIGNATURE);
      transforms.addTransform(Transforms.TRANSFORM_C14N_EXCL_OMIT_COMMENTS);
      ByteArrayOutputStream c14nStream = new ByteArrayOutputStream();
      MessageDigestAlgorithm mda = MessageDigestAlgorithm.getInstance(doc, MessageDigestAlgorithm.ALGO_ID_DIGEST_GOST3411);
      mda.reset();
      XMLSignatureInput output = transforms.performTransforms(new XMLSignatureInput(body), c14nStream);
      DigesterOutputStream digesterStream = new DigesterOutputStream(mda);
      output.updateOutputStream(digesterStream);
      return new AppData(c14nStream.toByteArray(), digesterStream.getDigestValue());
    } catch (ParserConfigurationException e) {
      throw new RuntimeException(e);
    } catch (SAXException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (CanonicalizationException e) {
      throw new RuntimeException(e);
    } catch (XMLSecurityException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  @Deprecated
  public String inject(final List<QName> namespaces, final AppData normalized, final X509Certificate certificate, final byte[] sig) {
    try {
      final String normalizedAppData = new String(normalized.content, "UTF8");
      final Document doc = createDocumentFromFragment(namespaces, normalizedAppData);
      NodeList childNodes = doc.getDocumentElement().getChildNodes();
      Element body = (Element) childNodes.item(0);
      Attr idAttr = body.getAttributeNodeNS(WSU, "Id");
      if (idAttr == null) {
        throw new IllegalStateException("Не нормализованный блок");
      }
      final String id = idAttr.getValue();
      final Transforms transforms = new Transforms(doc);
      Element signature = doc.createElementNS(Constants.SignatureSpecNS, Constants._TAG_SIGNATURE);
      signature = (Element) body.insertBefore(signature, body.getFirstChild());
      transforms.setElement(signature, id);
      transforms.addTransform(Transforms.TRANSFORM_ENVELOPED_SIGNATURE);
      transforms.addTransform(Transforms.TRANSFORM_C14N_EXCL_OMIT_COMMENTS);
      ByteArrayOutputStream c14nStream = new ByteArrayOutputStream();
      MessageDigestAlgorithm mda = MessageDigestAlgorithm.getInstance(doc, MessageDigestAlgorithm.ALGO_ID_DIGEST_GOST3411);
      mda.reset();
      XMLSignatureInput output = transforms.performTransforms(new XMLSignatureInput(body), c14nStream);
      DigesterOutputStream digesterStream = new DigesterOutputStream(mda);
      output.updateOutputStream(digesterStream);
      AppData check = new AppData(c14nStream.toByteArray(), digesterStream.getDigestValue());
      if (!Arrays.equals(check.digest, normalized.digest)) {
        final StringBuilder sb = new StringBuilder("Хеш сумма не совпадает:\n");
        sb.append("параметер: ").append(new String(normalized.digest, "UTF8")).append('\n');
        sb.append(" контроль: ").append(new String(check.digest, "UTF8"));
        throw new IllegalStateException(sb.toString());
      }

      Element keyInfo = doc.createElementNS(Constants.SignatureSpecNS, "KeyInfo");
      Element securityTokenReference = doc.createElementNS(WSSE, "SecurityTokenReference");
      Element reference = doc.createElementNS(WSSE, "Reference");
      reference.setAttribute("URI", "#CertId");
      reference.setAttribute("ValueType", WSS_X509V3);
      securityTokenReference.appendChild(reference);
      keyInfo.appendChild(securityTokenReference);
      signature.appendChild(keyInfo);
      Element signatureValueElement =
        XMLUtils.createElementInSignatureSpace(doc, Constants._TAG_SIGNATUREVALUE);
      signature.appendChild(signatureValueElement);
      String base64codedValue = Base64.encode(sig);
      if (base64codedValue.length() > 76 && !XMLUtils.ignoreLineBreaks()) {
        base64codedValue = "\n" + base64codedValue + "\n";
      }
      signatureValueElement.appendChild(doc.createTextNode(base64codedValue));
      return saxFilter(doc);
    } catch (ParserConfigurationException e) {
      throw new RuntimeException(e);
    } catch (SAXException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (CanonicalizationException e) {
      throw new RuntimeException(e);
    } catch (XMLSecurityException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public byte[] toPkcs7(final Signature signature) {
    return SunPkcs7.toPkcs7(signature);
  }

  @Override
  public Signature fromPkcs7(final byte[] pkcs7) {
    return SunPkcs7.fromPkcs7(pkcs7);
  }

  @Override
  public boolean validate(final Signature signature, final byte[] digest, final byte[] content) {
    // основной контракт - наличие данных, сертификата и подписи
    if (content == null || signature == null || signature.certificate == null || signature.sign == null) {
      return false;
    }

    // проверям сначала хеш
    if (digest != null) {
      try {
        final MessageDigest gost3411 = MessageDigest.getInstance("GOST3411");
        gost3411.update(content);
        byte[] digest2 = gost3411.digest();
        if (!Arrays.equals(digest, digest2)) {
          return false;
        }
      } catch (NoSuchAlgorithmException e) {
        throw new IllegalStateException("Отсутсвует GOST3411");
      }
    }

    // проверка подписи (не сертификата!)
    try {
      final java.security.Signature sig = java.security.Signature.getInstance("GOST3411withGOST3410EL");
      sig.initVerify(signature.certificate);
      sig.update(content);
      return sig.verify(signature.sign);
    } catch (final NoSuchAlgorithmException e) {
      throw new IllegalStateException("Отсутсвует GOST3411withGOST3410EL");
    } catch (final InvalidKeyException e) {
      return false;
    } catch (final SignatureException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public boolean verifySignature(X509Certificate certificate, InputStream data, byte[] sign) {
    try {
      if (certificate == null || data == null || sign == null) {
        throw new NullPointerException();
      }

      final boolean valid;
      final java.security.Signature signature = createGost3411WithGost3410();
      if (!isKeyValid(certificate, signature)) {
        valid = false;
      } else {
        updateSignature(signature, data);
        valid = verifySignature(signature, sign);
      }
      return valid;

    } finally {
      close(data);
    }
  }

  private String domToString(Element node) {
    try {
      final TransformerFactory factory = TransformerFactory.newInstance();
      final Transformer transformer = factory.newTransformer();
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
      final StringWriter sw = new StringWriter();
      transformer.transform(new DOMSource(node), new StreamResult(sw));
      return sw.toString();
    } catch (TransformerException e) {
      throw new RuntimeException(e);
    }
  }

  private Document createDocumentFromFragment(List<QName> namespaces, String appData) throws SAXException, IOException, ParserConfigurationException {
    // эти элементы должны определятся на верхнем уровне:
    final QName wsu = new QName(WSU, "wsu");
    final QName ds = new QName("http://www.w3.org/2000/09/xmldsig#", "ds");
    if (namespaces.indexOf(wsu) == -1) {
      namespaces.add(wsu);
    }
    if (namespaces.indexOf(ds) == -1) {
      namespaces.add(ds);
    }
    final StringBuilder sb = new StringBuilder();
    sb.append("<root");
    for (final QName name : namespaces) {
      sb.append(" xmlns:");
      sb.append(name.getLocalPart());
      sb.append("=\"");
      sb.append(name.getNamespaceURI());
      sb.append("\"");
    }
    sb.append(">");
    sb.append(appData);
    sb.append("</root>");
    final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setIgnoringElementContentWhitespace(true);
    factory.setCoalescing(true);
    factory.setNamespaceAware(true);
    return factory.newDocumentBuilder().parse(new ByteArrayInputStream(sb.toString().getBytes("UTF-8")));
  }

  private String saxFilter(Node node) {
    try {
      final Transformer transformer = TransformerFactory.newInstance().newTransformer();
      final StringWriter w1 = new StringWriter();
      transformer.transform(new DOMSource(node), new StreamResult(w1));
      XMLInputFactory xif = XMLInputFactory.newInstance();
      XMLEventReader eventReader = xif.createXMLEventReader(new StreamSource(new StringReader(w1.toString())));
      XMLEventReader filteredReader = xif.createFilteredReader(eventReader, new EventFilter() {
        @Override
        public boolean accept(XMLEvent event) {
          int type = event.getEventType();
          if (type == XMLStreamConstants.START_DOCUMENT || type == XMLStreamConstants.END_DOCUMENT) {
            return false;
          }
          if (event.isStartElement()) {
            StartElement startElement = (StartElement) event;
            QName name = startElement.getName();
            if ("".equals(name.getNamespaceURI()) && "root".equals(name.getLocalPart())) {
              return false;
            }
          }
          if (event.isEndElement()) {
            EndElement endElement = (EndElement) event;
            QName name = endElement.getName();
            if ("".equals(name.getNamespaceURI()) && "root".equals(name.getLocalPart())) {
              return false;
            }
          }
          return true;
        }
      });
      StringWriter sw = new StringWriter();
      XMLOutputFactory xof = XMLOutputFactory.newInstance();
      XMLEventWriter writer = xof.createXMLEventWriter(sw);
      while (filteredReader.hasNext()) {
        writer.add(filteredReader.nextEvent());
      }
      return sw.toString();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  static class ValidateResult {
    final public String error;
    final public X509Certificate cert;

    ValidateResult(final String error, final X509Certificate cert) {
      this.error = error;
      this.cert = cert;
    }
  }


  private boolean verifySignature(final java.security.Signature signature, final byte[] sign) {
    try {
      return signature.verify(sign);
    } catch (SignatureException e) {
      throw new RuntimeException("CryptoPRO error", e);
    }
  }

  private boolean isKeyValid(final X509Certificate certificate, final java.security.Signature signature) {
    boolean valid;
    try {
      signature.initVerify(certificate);
      valid = true;
    } catch (InvalidKeyException e) {
      log.info("Invalid certificate", e);
      valid = false;
    }
    return valid;
  }

  private void updateSignature(final java.security.Signature signature, final InputStream data) {
    final byte[] buffer = new byte[1024];
    int length;
    try {
      while ((length = data.read(buffer)) != -1) {
        signature.update(buffer, 0, length);
      }
    } catch (IOException e) {
      throw new RuntimeException("data reading error", e);
    } catch (SignatureException e) {
      throw new RuntimeException("CryptoPRO error", e);
    }
  }

  private void close(final InputStream inputStream) {
    if (inputStream != null) {
      try {
        inputStream.close();
      } catch (IOException e) {
        log.info("failure on close", e);
      }
    }
  }

  private java.security.Signature createGost3411WithGost3410() {
    final java.security.Signature signature;
    try {
      signature = java.security.Signature.getInstance("GOST3411withGOST3410EL");
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("КриптоПРО не установлен", e);
    }
    return signature;
  }

}
