/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws3572c;


import com.sun.org.apache.xpath.internal.XPathAPI;
import junit.framework.Assert;
import org.apache.commons.lang.time.DateUtils;
import org.apache.xml.security.keys.KeyInfo;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.utils.Base64;
import org.apache.xml.security.utils.Constants;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import ru.codeinside.gws.api.ClientRequest;
import ru.codeinside.gws.crypto.cryptopro.CryptoProvider;
import ru.codeinside.gws.stubs.DummyContext;
import xmltype.R;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.ParseException;

public class GMPClientSignTest {

  private GMPClient3572 client;
  private DocumentBuilder documentBuilder;

  @Before
  public void setUp() throws Exception {
    client = new GMPClient3572();
    CryptoProvider cryptoProvider = new CryptoProvider();
    client.bindCryptoProvider(cryptoProvider);


    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    dbf.setNamespaceAware(true);
    documentBuilder = dbf.newDocumentBuilder();

  }

  private DummyContext createContext() throws ParseException {
    DummyContext ctx = new DummyContext();
    ctx.setVariable("payerType" , "1");
    ctx.setVariable("payerPersonDocumentID1" , "12345678901");
    ctx.setVariable("postBlockIdRequest", "13454"); // идентификатор запроса
    ctx.setVariable("postBlockSenderIdentifier", "20091d"); // идентификатор отправителя
    ctx.setVariable("ordinalNumber", "013400000011"); // порядковый номер заявки
    ctx.setVariable("postBlockTimeStamp", DateUtils.parseDate("25.07.2012 09:40:47", new String[]{"dd.MM.yyyy HH:mm:ss"})); // дата составления запроса

    ctx.setVariable("supplierOrgInfoName", "Управление Федеральной миграционной службы по Республике Татарстан");
    ctx.setVariable("supplierOrgInfoINN", "1655102196");
    ctx.setVariable("supplierOrgInfoKPP", "165501001");
    ctx.setVariable("accountAccount", "40101810800000010001");
    ctx.setVariable("bankBIK", "049205001");
    ctx.setVariable("bankName", "ГРКЦ НБ РТ г. Казани");


    ctx.setVariable("chargeSupplierBillID", "19255500000000000079"); // Уникальный идентификатор счёта в ИСП
    ctx.setVariable("chargeBillDate", DateUtils.parseDate( "10.03.2011", new String[]{"dd.MM.yyyy"}));  //Дата выставления счёта
    ctx.setVariable("chargeBillFor", "Госпошлина за выдачу загранпаспорта");  //Дата выставления счёта
    ctx.setVariable("chargeTotalAmount", "1000,00");
    ctx.setVariable("chargeChangeStatus", "1"); /* Статус счёта 1 - новый  2 - изменение  3 - аннулирование */
    ctx.setVariable("chargeTreasureBranch", "УФК по Республике Татарстан");
    ctx.setVariable("chargeKBK", "19210806000011000110");
    ctx.setVariable("chargeOKATO", "92401000000");
    ctx.setVariable("chargeApplicationID", "455555");
    ctx.setVariable("chargeUnifiedPayerIdentifier", "0100000000006667775555643");

    ctx.setVariable("budgetIndexStatus", "0");
    ctx.setVariable("budgetPaymentType", "0");
    ctx.setVariable("budgetPurpose", "0");
    ctx.setVariable("budgetTaxPeriod", "0");
    ctx.setVariable("budgetTaxDocNumber", "0");
    ctx.setVariable("budgetTaxDocDate", "0");
    ctx.setVariable("operationType", "importCharge");
    return ctx;
  }


  /*
       Проверяется подпись сущности запроса на импорт.
      */
  @Test
  public void testSignForEntity() throws Exception {
    ClientRequest request = client.createClientRequest(createContext());
    InputSource is = new InputSource(new StringReader(request.appData));
    Document doc = documentBuilder.parse(is);

//    Element elementForSign = (Element) doc.getElementsByTagNameNS(null, "Charge").item(0);
//
//    Node parentNode;
    Document detachedDocument;
//    if (!elementForSign.isSameNode(doc.getDocumentElement())) {
//      parentNode = elementForSign.getParentNode();
//      parentNode.removeChild(elementForSign);
//
//      detachedDocument = documentBuilder.newDocument();
//      Node importedElementForSign = detachedDocument.importNode(elementForSign, true);
//      detachedDocument.appendChild(importedElementForSign);
//    } else {
      detachedDocument = doc;
//    }


    Element nscontext = detachedDocument.createElementNS(null, "namespaceContext");
    nscontext.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + "ds".trim(), "http://www.w3.org/2000/09/xmldsig#");

    Element certificateElement = (Element) XPathAPI.selectSingleNode(detachedDocument, "//ds:X509Certificate[1]", nscontext);
    Element sigElement = (Element) certificateElement.getParentNode().getParentNode().getParentNode();

    XMLSignature signature = new XMLSignature(sigElement, "");

    CertificateFactory cf = CertificateFactory.getInstance("X.509");
    X509Certificate certKey = (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(Base64.decode(certificateElement.getTextContent().trim().getBytes())));

    Assert.assertNotNull("There are no information about public key. Verification couldn't be implemented", certKey);
    Assert.assertTrue("Signature is not valid", signature.checkSignatureValue(certKey));
  }

  /* проверяется подпись от примера присланного казначейством */
  @Test
  public void validateGMPSample() throws Exception {
    SOAPMessage sampleDoc = R.getSoapResource("gmp/gmd_sample.xml");
    SOAPBody soapBody = sampleDoc.getSOAPBody();
    Element unifoTransferMsg = first(soapBody, null, "UnifoTransferMsg");
    Assert.assertNotNull(unifoTransferMsg);
    Element messageData = first(unifoTransferMsg, null, "MessageData");
    Assert.assertNotNull(messageData);
    Element appData = first(messageData, null, "AppData");
    Assert.assertNotNull(appData);
    Element importData = first(appData, null, "ImportData");
    Assert.assertNotNull(importData);
    Element importRequest = first(importData, "http://roskazna.ru/xsd/PGU_ImportRequest", "ImportRequest");
    Assert.assertNotNull("importRequest not found", importRequest);
    Element charge = first(importRequest, null, "Charge");
    Assert.assertNotNull("charge not found", charge);
    Document docEntity = createDocumentFromElement(charge);
    Assert.assertNotNull("doc from charge null", docEntity);
    Assert.assertTrue("Документ не прошел валидацию", signDocVer(docEntity));
  }

  private Document createDocumentFromElement(Element element) throws ParserConfigurationException, IOException, SAXException {
    final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    // установка флага, определяющего игнорирование пробелов в содержимом элементов при обработке XML-документа
    dbf.setIgnoringElementContentWhitespace(true);
    // установка флага, определяющего преобразование узлов CDATA в текстовые узлы при обработке XML-документа
    dbf.setCoalescing(true);
    // установка флага, определяющего поддержку пространств имен при обработке XML-документа
    dbf.setNamespaceAware(true);
    InputSource is = new InputSource(new StringReader(convertElementToString(element)));
    return dbf.newDocumentBuilder().parse(is);

  }

  /**
   * Проверка подписи всего XML-документа для алгоритма ГОСТ Р 34.10-2001.
   *
   * @throws Exception /
   */
  boolean signDocVer(Document doc) throws Exception {
    /* Чтение узла подписи <ds:Signature> из XML-документа */

    // чтение из загруженного документа содержимого пространства имени Signature
    final Element nscontext = doc.createElementNS(null, "namespaceContext");
    nscontext.setAttributeNS("http://www.w3.org/2000/xmlns/",
        "xmlns:" + "ds".trim(), Constants.SignatureSpecNS);

    // выбор из прочитанного содержимого пространства имени узла подписи <ds:Signature>
    final Element sigElement = (Element) XPathAPI
        .selectSingleNode(doc, "//ds:Signature[1]", nscontext);

    /* Проверка подписи XML-документа на основе информации об открытом ключе, хранящейся в
    XML-документе */

    // инициализация объекта проверки подписи
    final XMLSignature signature = new XMLSignature(sigElement, "");

    // чтение узла <ds:KeyInfo> информации об открытом ключе
    final KeyInfo ki = signature.getKeyInfo();

    // чтение сертификата их узла информации об открытом ключе
    final X509Certificate certKey = ki.getX509Certificate();

    // если сертификат найден, то осуществляется проверка
    // подписи на основе сертфиката
    if (certKey != null) {

      return signature.checkSignatureValue(certKey);

    }
    // в противном случае осуществляется проверка на открытом ключе
    else {
      // чтение открытого ключа из узла информации об открытом ключе
      final PublicKey pk = ki.getPublicKey();

      // если открытый ключ найден, то на нем осуществляется проверка подписи
      if (pk != null) {

        return signature.checkSignatureValue(pk);
      }
      // в противном случае проверка не может быть выполнена
      else
        throw new Exception(
            "There are no information about public key. Verification couldn't be implemented");

    }
  }

  private static String convertElementToString(Element signElement) {
    Transformer transformer;
    try {
      transformer = TransformerFactory.newInstance().newTransformer();
//            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      StreamResult result = new StreamResult(new StringWriter());
      DOMSource source = new DOMSource(signElement);
      transformer.transform(source, result);
      return result.getWriter().toString();
    } catch (Exception e) {
      return null;
    }
  }

  private static Element first(final Node parent, final String uri, final String localName) {
    final NodeList nodes = parent.getChildNodes();
    final int n = nodes.getLength();
    for (int i = 0; i < n; i++) {
      final Node node = nodes.item(i);
      if (node instanceof Element) {
        final Element element = (Element) node;
        if (localName.equals(element.getLocalName()) && (uri == null || uri.equals(element.getNamespaceURI()))) {
          return element;
        }
      }
    }
    return null;
  }

}
