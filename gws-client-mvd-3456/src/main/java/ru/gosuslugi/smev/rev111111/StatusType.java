
/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.gosuslugi.smev.rev111111;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


@XmlType(name = "StatusType")
@XmlEnum
public enum StatusType {


    /**
     * Запрос
     * 
     */
    REQUEST,

    /**
     * Результат
     * 
     */
    RESULT,

    /**
     * Мотивированный отказ
     * 
     */
    REJECT,

    /**
     * Ошибка при ФЛК
     * 
     */
    INVALID,

    /**
     * Сообщение-квиток о приеме
     * 
     */
    ACCEPT,

    /**
     * Запрос данных/результатов
     * 
     */
    PING,

    /**
     * В обработке
     * 
     */
    PROCESS,

    /**
     * Уведомление об ошибке
     * 
     */
    NOTIFY,

    /**
     * Технический сбой
     * 
     */
    FAILURE,

    /**
     * Отзыв заявления
     * 
     */
    CANCEL,

    /**
     * Возврат состояния
     * 
     */
    STATE;

    public String value() {
        return name();
    }

    public static StatusType fromValue(String v) {
        return valueOf(v);
    }

}
