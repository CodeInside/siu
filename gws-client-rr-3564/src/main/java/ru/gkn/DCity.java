/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */


package ru.gkn;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


@XmlType(name = "dCity")
@XmlEnum
public enum DCity {


    /**
     * Город
     * 
     */
    @XmlEnumValue("\u0433")
    Г("\u0433"),

    /**
     * Поселок городского типа
     * 
     */
    @XmlEnumValue("\u043f\u0433\u0442")
    ПГТ("\u043f\u0433\u0442"),

    /**
     * Рабочий поселок
     * 
     */
    @XmlEnumValue("\u0440\u043f")
    РП("\u0440\u043f"),

    /**
     * Курортный поселок
     * 
     */
    @XmlEnumValue("\u043a\u043f")
    КП("\u043a\u043f"),

    /**
     * Дачный поселок
     * 
     */
    @XmlEnumValue("\u0434\u043f")
    ДП("\u0434\u043f"),

    /**
     * Сельсовет
     * 
     */
    @XmlEnumValue("\u0441/\u0441")
    С_С("\u0441/\u0441"),

    /**
     * Сельская администрация
     * 
     */
    @XmlEnumValue("\u0441/\u0430")
    С_А("\u0441/\u0430"),

    /**
     * Сельский округ
     * 
     */
    @XmlEnumValue("\u0441/\u043e")
    С_О("\u0441/\u043e"),

    /**
     * Волость
     * 
     */
    @XmlEnumValue("\u0432\u043e\u043b\u043e\u0441\u0442\u044c")
    ВОЛОСТЬ("\u0432\u043e\u043b\u043e\u0441\u0442\u044c"),

    /**
     * Почтовое отделение
     * 
     */
    @XmlEnumValue("\u043f/\u043e")
    П_О("\u043f/\u043e"),

    /**
     * Территория
     * 
     */
    @XmlEnumValue("\u0442\u0435\u0440")
    ТЕР("\u0442\u0435\u0440"),

    /**
     * Сумон
     * 
     */
    @XmlEnumValue("\u0441\u0443\u043c\u043e\u043d")
    СУМОН("\u0441\u0443\u043c\u043e\u043d"),

    /**
     * Сельское поселение
     * 
     */
    @XmlEnumValue("\u0441/\u043f\u043e\u0441")
    С_ПОС("\u0441/\u043f\u043e\u0441"),

    /**
     * Сельское муниципальное образование
     * 
     */
    @XmlEnumValue("\u0441/\u043c\u043e")
    С_МО("\u0441/\u043c\u043e"),

    /**
     * Массив
     * 
     */
    @XmlEnumValue("\u043c\u0430\u0441\u0441\u0438\u0432")
    МАССИВ("\u043c\u0430\u0441\u0441\u0438\u0432"),

    /**
     * Поселок
     * 
     */
    @XmlEnumValue("\u043f")
    П("\u043f");
    private final String value;

    DCity(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static DCity fromValue(String v) {
        for (DCity c: DCity.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
