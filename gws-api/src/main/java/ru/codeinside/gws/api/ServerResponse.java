/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.api;

import javax.xml.namespace.QName;
import java.util.List;

/**
 * Ответ от поставщика потребителю.
 */
public class ServerResponse {
  /**
   * Имя операции (по WSDL).
   */
  public QName action;

  /**
   * Управляющий пакет СМЭВ.
   */
  public Packet packet;

  /**
   * Данные поставщика (содержимое AppData).
   */
  public String appData;

  /**
   * SoapMessage в виде массива байт
   */
  public byte[] responseMessage;

  /**
   * Код документа.
   * Также используется как идентификатор описателя вложений.
   */
  public String docRequestCode;

  /**
   * Вложения.
   */
  public List<Enclosure> attachmens;

  /**
   * Требуется ли ЭП-СП.
   */
  public boolean signRequired;

  /**
   * Какой блок внутри AppData необходимо подписывать. Если не указан то подписывается блок AppData.
   * Задается относительно блока AppData, например {@code /AppData/Target}
   */
  public String signingXPath;

  @Override
  public String toString() {
    return "{" +
        "action=" + action +
        ", packet=" + packet +
        ", appData='" + appData + '\'' +
        ", docRequestCode='" + docRequestCode + '\'' +
        ", attachmens=" + attachmens +
        '}';
  }
}
