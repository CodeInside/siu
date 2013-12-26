/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.crypto.cryptopro;

import com.sun.org.apache.xpath.internal.XPathAPI;
import junit.framework.Assert;
import org.apache.xml.security.algorithms.MessageDigestAlgorithm;
import org.apache.xml.security.c14n.Canonicalizer;
import org.apache.xml.security.keys.KeyInfo;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.transforms.Transforms;
import org.apache.xml.security.utils.Constants;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.logging.Logger;

/**
 * Формирование и проверка подписи всего XML-документа для алгоритма ГОСТ Р
 * 34.10-2001.
 *
 * @author Copyright 2004-2007 Crypto-Pro. All rights reserved.
 * @.Version
 */
public class SignDoc {

    @Test
    public void mainTest() {
        try {

            final String signMethod =
                    XMLSignature.ALGO_ID_SIGNATURE_GOST_GOST3410_3411;

            // алгоритм хеширования, используемый при подписи (ГОСТ Р 34.11-94)
            final String digestMethod =
                    MessageDigestAlgorithm.ALGO_ID_DIGEST_GOST3411;

        /* В первую очередь осуществляет регистрация алгоритма подписи ГОСТ Р 34.10-2001*/
            //JCPXMLDSigInit.init();
            CryptoProvider.loadCertificate();

            Logger.getLogger("LOG").info("sign doc begin");
            String result = signDoc(signMethod, digestMethod, R.getTextResource("gmp/tests.xml")
            );
            Logger.getLogger("LOG").info("Result \n\n\n\n" + result);
            Logger.getLogger("LOG").info("sign doc end\nsign doc verify");
            Assert.assertTrue(signDocVer(result));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Формирование подписи всего XML-документа для алгоритма ГОСТ Р 34.10-2001.
     *
     *
     *
     *
     * @param signMethod алгоритм подписи (ГОСТ Р 34.10-2001)
     * @param digestMethod алгоритм хеширования, используемый при подписи (ГОСТ Р
     * 34.11-94)
     * @param testDoc подписываемый документ
     * @throws Exception e
     */
    String signDoc(String signMethod, String digestMethod,
                   String testDoc)
            throws Exception {


        // получение закрытого ключа
        final PrivateKey privateKey = CryptoProvider.privateKey;


        // генерирование X509-сертификата из закодированного представления сертификата
        final X509Certificate cert = CryptoProvider.cert;

    /* Загружаем подписываемый XML-документ из файла */

        // инициализация объекта чтения XML-документа
        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        // установка флага, определяющего игнорирование пробелов в содержимом элементов при обработке XML-документа
        dbf.setIgnoringElementContentWhitespace(true);

        // установка флага, определяющего преобразование узлов CDATA в текстовые узлы при обработке XML-документа
        dbf.setCoalescing(true);

        // установка флага, определяющего поддержку пространств имен при обработке XML-документа
        dbf.setNamespaceAware(true);

        // загрузка содержимого подписываемого документа на основе установленных флагами правил
        final DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(testDoc));
        final Document doc = documentBuilder.parse(is);

    /* Добавление узла подписи <ds:Signature> в загруженный XML-документ */

        // инициализация объекта формирования ЭЦП в соответствии с алгоритмом ГОСТ Р 34.10-2001
        final XMLSignature sig = new XMLSignature(doc, "", signMethod);

        // получение корневого узла XML-документа
        final Element anElement = doc.getDocumentElement();

        // добавление в корневой узел XML-документа узла подписи
        anElement.appendChild(sig.getElement());

    /* Определение правил работы с XML-документом и добавление в узел подписи этих правил */

        // создание узла преобразований <ds:Transforms> обрабатываемого XML-документа
        final Transforms transforms = new Transforms(doc);

        // добавление в узел преобразований правил работы с документом
        transforms.addTransform(Transforms.TRANSFORM_ENVELOPED_SIGNATURE);
       // transforms.addTransform(Transforms.TRANSFORM_C14N_WITH_COMMENTS);
        transforms.addTransform(Canonicalizer.ALGO_ID_C14N_EXCL_OMIT_COMMENTS);

        // добавление в узел подписи ссылок (узла <ds:Reference>), определяющих правила работы с
        // XML-документом (обрабатывается текущий документ с заданными в узле <ds:Transforms> правилами
        // и заданным алгоритмом хеширования)
        sig.addDocument("", transforms, digestMethod);

    /* Создание подписи всего содержимого XML-документа на основе закрытого ключа, заданных правил и алгоритмов */

        // создание внутри узла подписи узла <ds:KeyInfo> информации об открытом ключе на основе
        // сертификата
        sig.addKeyInfo(cert);

        // создание подписи XML-документа
        sig.sign(privateKey);
        Element keyInfo = first(sig.getElement(), "http://www.w3.org/2000/09/xmldsig#", "KeyInfo" );
        Element x509Data = first(keyInfo, "http://www.w3.org/2000/09/xmldsig#", "X509Data" );
        Element x509SubjectName = doc.createElementNS("http://www.w3.org/2000/09/xmldsig#", "X509SubjectName");
        x509SubjectName.appendChild(doc.createTextNode( cert.getSubjectDN().toString()));
        x509Data.appendChild(x509SubjectName);
    /* Сохранение подписанного XML-документа в файл */

        // определение потока, в который осуществляется запись подписанного XML-документа

        StreamResult result = new StreamResult(new StringWriter());
        // инициализация объекта копирования содержимого XML-документа в поток
        final TransformerFactory tf = TransformerFactory.newInstance();

        // создание объекта копирования содержимого XML-документа в поток
        final Transformer trans = tf.newTransformer();

        // копирование содержимого XML-документа в поток
        trans.transform(new DOMSource(doc), result);

        return convertElementToString(doc.getDocumentElement());
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

    /**
     * Проверка подписи всего XML-документа для алгоритма ГОСТ Р 34.10-2001.
     *
     * @param signDoc подписанный документ
     * @throws Exception /
     */
   boolean signDocVer(String signDoc) throws Exception {

        // инициализация объекта чтения XML-документа
        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        // установка флага, определяющего игнорирование пробелов в содержимом элементов при обработке XML-документа
        dbf.setIgnoringElementContentWhitespace(true);

        // установка флага, определяющего преобразование узлов CDATA в текстовые узлы при обработке XML-документа
        dbf.setCoalescing(true);

        // установка флага, определяющего поддержку пространств имен при обработке XML-документа
        dbf.setNamespaceAware(true);

        // загрузка содержимого подписываемого документа на основе установленных флагами правил
        final DocumentBuilder documentBuilder = dbf.newDocumentBuilder();

    /* Загружаем подписанный XML-документ из строки */
        InputSource is = new InputSource(new StringReader(signDoc));
        final Document doc = documentBuilder.parse(is);

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
            boolean result = signature.checkSignatureValue(certKey);
            Logger.getLogger("LOG").info("The XML signature  is " +
                    (result
                            ? "valid (good)" : "invalid (bad)"));
            return result;

        }
        // в противном случае осуществляется проверка на открытом ключе
        else {
            // чтение открытого ключа из узла информации об открытом ключе
            final PublicKey pk = ki.getPublicKey();

            // если открытый ключ найден, то на нем осуществляется проверка подписи
            if (pk != null) {
                boolean result = signature.checkSignatureValue(pk);
                Logger.getLogger("LOG").info(
                        "The XML signature is " + (result
                                ? "valid (good)" : "invalid (bad)"));
                return result;
            }
            // в противном случае проверка не может быть выполнена
            else
                throw new Exception(
                        "There are no information about public key. Verification couldn't be implemented");

        }
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
}
