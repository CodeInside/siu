/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2015, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.api;

import javax.xml.soap.SOAPMessage;

/**
 * API сервиса для встраивания подписи в xml-документ
 */
public interface XmlSignatureInjector {
    /**
     * Встроить подпись
     *
     * @param wrappedAppData данные обернутые в тэг AppData
     * @return тег AppData с встроенной подписью
     */
    String injectSpToAppData(WrappedAppData wrappedAppData);

    /**
     * Встроить ЭП-ОВ в заголовок SOAP сообщения
     *
     * @param message
     * @param signature
     * @return
     */
    void injectOvToSoapHeader(SOAPMessage message, Signature signature);

    /**
     * Подготовить сообщение для подписи
     *
     * @param message SOAP-сообщение
     * @param bodyHash хэш тэга Body
     */
    void prepareSoapMessage(SOAPMessage message, byte[] bodyHash);

    /**
     * Подготовить сруктуру блока AppData для встраивания подписи СП
     *
     * @param clientRequest данные потребителя
     *
     * @param isSignatureLast позиция дорбавления подписи
     *
     * @return нормализованный блок SignedInfo для получения подписи
     */
    byte[] prepareAppData(ClientRequest clientRequest, boolean isSignatureLast, XmlNormalizer normalizer, CryptoProvider cryptoProvider);

    /**
     * Подготовить сруктуру блока AppData для встраивания подписи СП
     *
     * @param serverResponse данные поставщика
     *
     * @param isSignatureLast позиция дорбавления подписи
     *
     * @return нормализованный блок SignedInfo для получения подписи
     */
    byte[] prepareAppData(ServerResponse serverResponse, boolean isSignatureLast, XmlNormalizer normalizer, CryptoProvider cryptoProvider);
}
