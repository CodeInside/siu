/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.crypto.cryptopro;

import com.sun.org.apache.xpath.internal.XPathAPI;
import junit.framework.Assert;
import org.apache.xml.security.keys.KeyInfo;
import org.apache.xml.security.utils.Constants;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;    import org.apache.xml.security.signature.XMLSignature;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

public class DigestXmlTest {


  @Test
    public void testVerifySign() throws Exception {
        //org.apache.xml.security.Init.init();
        CryptoProvider.loadCertificate();
        //Assert.assertEquals(XML_FOR_CHECK_SIGN,XML_FOR_CHECK_SIGNED_OUR_CERT );
        Assert.assertTrue(signDocVer(R.getTextResource("gmp/reference.xml")));
    }


  public static boolean signDocVer(String signDoc) throws Exception {

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
            System.out.println("The XML signature  is " +
                    (result
                            ? "valid (good)" : "invalid (bad)"));
            return  result;

        }
        // в противном случае осуществляется проверка на открытом ключе
        else {
            // чтение открытого ключа из узла информации об открытом ключе
            final PublicKey pk = ki.getPublicKey();

            // если открытый ключ найден, то на нем осуществляется проверка подписи
            if (pk != null) {
                boolean result = signature.checkSignatureValue(pk);
                System.out.println(
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
}
