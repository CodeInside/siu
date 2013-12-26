/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.api;

import java.util.Date;

/**
 * Пакет данных, добавляемый маршрутизатором СМЭВ в заголовок конверта.
 * <p/>
 * (rev111111)
 */
final public class RouterPacket {

  /**
   * Узел маршрутизатора.
   */
  public String nodeId;

  /**
   * Присвоенный номер.
   */
  public String messageId;

  /**
   * Дата поступления.
   */
  public Date timeStamp;

  /**
   * Направление данных.
   */
  public Direction direction;

  public enum Direction {
    /**
     * Запрос от потребителя к поставщику
     */
    REQUEST,

    /**
     * Ответ поставщика потребителю
     */
    RESPONSE;
  }

  @Override
  public String toString() {
    return "[nodeId=" + nodeId + ", messageId=" + messageId + ", timeStamp=" + timeStamp + ", direction="
      + direction + "]";
  }

  public static Direction parseDirection(String text) {
    if ("REQUEST".equals(text)) {
      return Direction.REQUEST;
    } else if ("RESPONSE".equals(text)) {
      return Direction.RESPONSE;
    }
    return null;
  }
}
