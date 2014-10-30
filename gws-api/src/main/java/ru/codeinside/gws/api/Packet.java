/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.api;

import java.io.Serializable;
import java.util.Date;

/**
 * Описательный пакет в конверте СМЭВ.
 */
final public class Packet implements Serializable {

    /**
     * Информационная система отправителя.
     * Обязательное поле.
     */
    public InfoSystem sender;

    /**
     * Информационная система получателя.
     * Обязательное поле.
     */
    public InfoSystem recipient;

    /**
     * Информационная система, создавшая заявку.
     */
    public InfoSystem originator;

    /**
     * Код типа исполнения.
     * Обязательное поле.
     */
    public Type typeCode;

    /**
     * Статус пакета.
     * Обязательное поле.
     */
    public Status status;

    /**
     * Дата и время формирования пакета.
     * Обязательное поле.
     */
    public Date date;

    /**
     * Тип взаимодействия.
     * Обязательное поле.
     */
    public String exchangeType;

    /**
     * Идентификатор запроса для организации запросов в цепочки.
     */
    public String requestIdRef;

    /**
     * Идентификатор первого запроса для организации запросов в цепочки.
     */
    public String originRequestIdRef;

    /**
     * Имя услуги.
     * Заменяет serviceCode начиная с rev120315.
     */
    public String serviceName;

    /**
     * Код услуги в информационной системе поставщика.
     */
    public String serviceCode;

    /**
     * Номер дела.
     */
    public String caseNumber;

    /**
     * Сообщение для тестирования.
     */
    public String testMsg;

    /**
     * ОКТМО (идентификатор муниципалитета).
     */
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
         * Исполнение государственных функций.
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
