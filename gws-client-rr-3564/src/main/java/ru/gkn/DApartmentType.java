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


@XmlType(name = "dApartmentType")
@XmlEnum
public enum DApartmentType {


    /**
     * Квартира
     * 
     */
    @XmlEnumValue("\u043a\u0432")
    КВ("\u043a\u0432"),

    /**
     * Бокс
     * 
     */
    @XmlEnumValue("\u0431\u043e\u043a\u0441")
    БОКС("\u0431\u043e\u043a\u0441"),

    /**
     * Комната
     * 
     */
    @XmlEnumValue("\u043a")
    К("\u043a"),

    /**
     * Помещение
     * 
     */
    @XmlEnumValue("\u043f\u043e\u043c")
    ПОМ("\u043f\u043e\u043c");
    private final String value;

    DApartmentType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static DApartmentType fromValue(String v) {
        for (DApartmentType c: DApartmentType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
