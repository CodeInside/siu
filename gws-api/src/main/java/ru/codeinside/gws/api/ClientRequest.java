/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.api;

import javax.xml.namespace.QName;
import java.util.Arrays;

/**
 * Запрос от потребителя к поставщику.
 */
final public class ClientRequest {
  /**
   * Управляющий пакет СМЭВ.
   */
  public Packet packet;

  /**
   * Имя операции (по WSDL).
   */
  public QName action;

  /**
   * Имя службы (по WSDL).
   */
  public QName service;

  /**
   * Имя порта в который придёт запрос.
   */
  public QName port;

  /**
   * HTTP адрес порта.
   */
  public String portAddress;

  /**
   * данные в формате поставщика (содержимое элемента AppData).
   */
  public String appData;

  /**
   * Требуется ли ЭП-СП.
   */
  public boolean signRequired;

  /**
   * Какой блок внутри AppData необходимо подписывать. Если не указан то подписывается блок AppData.
   * Задается относительно блока AppData, например {@code /AppData/Target}
   */
  public String signingXPath;

  /**
   * Идентификатор описателя вложений.
   */
  public String enclosureDescriptor;

  /**
   * SoapMessage в виде массива байт
   */
  public byte[] requestMessage;

  /**
   * Вложения.
   */
  public Enclosure[] enclosures;

  /**
   * Есть ли подпись заявителя.
   */
  public boolean applicantSign;


  @Override
  public String toString() {
    return "{" +
        "packet=" + packet +
        ", action=" + action +
        ", service=" + service +
        ", port=" + port +
        ", portAddress='" + portAddress + '\'' +
        ", appData='" + appData + '\'' +
        ", signRequired=" + signRequired +
        ", enclosureDescriptor='" + enclosureDescriptor + '\'' +
        ", enclosures=" + Arrays.toString(enclosures) +
        ", applicantSign=" + applicantSign +
        '}';
  }
}
