/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.api;


import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.cert.X509Certificate;
import java.util.List;

/**
 * Поставщик средств криптографии.
 */
public interface CryptoProvider {

    /**
     * Установка технологической подписи ЭП-ОВ.
     */
    void sign(SOAPMessage soapMessage);

    /**
     * Локальная проверка сертификатов ЭП-ОВ и ЭП-СМЭВ.
     */
    VerifyResult verify(SOAPMessage soapMessage);

    /**
     * Нормализация/Каноникализация перед формированием подписи по
     * Exclusive XML Canonicalization от 18 июля 2002 "http://www.w3.org/2001/10/xml-exc-c14n#".
     * <p/>
     * Xеширование выполняется по ГОСТ Р 34.11-94 "http://www.w3.org/2001/04/xmldsig-more#gostr3411".
     *
     * @param namespaces пространства имён в котором определена AppData.
     * @param appData    данные для подписи.
     * @return нормализованный блок AppData.
     */
    @Deprecated
    AppData normalize(List<QName> namespaces, String appData);

    /**
     * Вставить подпись.
     *
     * @param namespaces  пространства имён в котором определена AppData.
     * @param appData     нормализованный блок AppData.
     * @param certificate сертификат
     * @param signature   подпись.
     * @return Обновленный блок AppData.
     */
    @Deprecated
    String inject(List<QName> namespaces, AppData appData, X509Certificate certificate, byte[] signature);


    /**
     * Упаковка подписи в контейнер PKCS7(detached).
     *
     * @param signature подпись.
     * @return контейнер PKCS7(detached)
     */
    byte[] toPkcs7(Signature signature);

    /**
     * Распаковка контейнера PKCS7(detached).
     *
     * @param pkcs7 данные крипто-контейнера.
     * @return частичная подпись без данных.
     */
    Signature fromPkcs7(byte[] pkcs7);

    /**
     * Проверка целостности подписи.
     *
     * @param signature подпись
     * @param content   содержимое
     * @param digest    дайджест/хеш по ГОСТ Р 34.11-94
     * @return true если подптсь прошла проверку, иначе false.
     */
    boolean validate(Signature signature, byte[] digest, byte[] content);

    /**
     * Проверка цифровой подписи по сертификату и свёртки по ГОСТ.
     *
     * @param certificate сертификат для проверки.
     * @param data        входной поток с данными.
     * @param signature   свёртка которыую необходимо проверить.
     * @return true если свертка верна и false иначе.
     */
    boolean verifySignature(X509Certificate certificate, InputStream data, byte[] signature);

    /**
     * Подпись элемента в документе и внедрение результатов подписи в документ
     *
     * @param sourceXML             документ XML содержащий подписанные данные
     * @param elementName           название тега элемента, который нужно подписать
     * @param namespace             ns тега, который нужно подписать. Для подписи будет первый попавшийся элемент с namespace:elementName
     * @param removeIdAttribute     нужно ли удалить id attribute
     * @param signatureAfterElement место куда вставлять ds:Signature
     * @param inclusive             влияет на алгоритм подписи и каноникализацию
     * @return документ с подписанным элементом
     * @throws Exception в случае проблем с работы криптоалгоритмов и в случае ошибок разбора sourceXML
     */
    String signElement(String sourceXML, String elementName, String namespace, boolean removeIdAttribute, boolean signatureAfterElement, boolean inclusive)
            throws Exception;

    /**
     * Получить хеш по "http://www.w3.org/2001/04/xmldsig-more#gostr3411".
     *
     * @param source входные данные
     */
    byte[] digest(InputStream source);
}
