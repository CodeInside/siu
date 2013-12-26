/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.api;

import java.util.Date;

/**
 * Описательный пакет в конверте СМЭВ. (rev111111).
 * <p/>
 * TODO: более содержательное имя
 */
final public class Packet {

  // required
  public InfoSystem sender;

  // required
  public InfoSystem recipient;

  public InfoSystem originator;

  // required
  public Type typeCode;

  // required
  public Status status;

  // required
  public Date date;

  // required
  public String exchangeType;

  public String requestIdRef;
  public String originRequestIdRef;

  /**
   * заменяет serviceCode начиная с rev120315
   */
  public String serviceName;
  public String serviceCode;
  public String caseNumber;
  public String testMsg;
  public String oktmo;

  @Override
  public String toString() {
    return "{" +
      "sender=" + sender +
      ", recipient=" + recipient +
      ", originator=" + originator +
      ", typeCode=" + typeCode +
      ", status=" + status +
      ", date=" + date +
      ", exchangeType='" + exchangeType + '\'' +
      ", requestIdRef='" + requestIdRef + '\'' +
      ", originRequestIdRef='" + originRequestIdRef + '\'' +
      (serviceName == null ? "" : ", serviceName='" + serviceName + "\'") +
      (serviceCode == null ? "" : ", serviceCode='" + serviceCode + "\'") +
      (caseNumber == null ? "" : ", caseNumber='" + caseNumber + "\'") +
      (testMsg == null ? "" : ", testMsg='" + testMsg + "\'") +
      (oktmo == null ? "" : ", oktmo='" + oktmo + "\'") +
      '}';
  }

  public enum Type {
    /**
     * Оказание государственных услуг.
     */
    SERVICE("GSRV"),

    /**
     * Исполнение государственныъ функций.
     */
    EXECUTION("GFNC"),

    /**
     * Другие цели.
     */
    OTHER("OTHR");


    private String name;

    Type(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }
  }

  public enum Status {
    /**
     * Запрос
     */
    REQUEST,

    /**
     * Результат
     */
    RESULT,

    /**
     * Мотивированный отказ
     */
    REJECT,

    /**
     * Ошибка при ФЛК
     */
    INVALID,

    /**
     * Сообщение квиток о приеме
     */
    ACCEPT,

    /**
     * Запрос данных/результатов
     */
    PING,

    /**
     * В обработке
     */
    PROCESS,

    /**
     * Уведомление об ошибке
     */
    NOTIFY,

    /**
     * Технический сбой
     */
    FAILURE,

    /**
     * Отзыв заявления
     */
    CANCEL,

    /**
     * Возврат состояния
     */
    STATE,

    /**
     * Пакетный режим
     */
    PACKET

  }
}
