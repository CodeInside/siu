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


@XmlType(name = "dDistrict")
@XmlEnum
public enum DDistrict {


    /**
     * Район
     * 
     */
    @XmlEnumValue("\u0440-\u043d")
    Р_Н("\u0440-\u043d"),

    /**
     * Улус
     * 
     */
    @XmlEnumValue("\u0443")
    У("\u0443"),

    /**
     * Территория
     * 
     */
    @XmlEnumValue("\u0442\u0435\u0440")
    ТЕР("\u0442\u0435\u0440"),

    /**
     * Кожуун
     * 
     */
    @XmlEnumValue("\u043a\u043e\u0436\u0443\u0443\u043d")
    КОЖУУН("\u043a\u043e\u0436\u0443\u0443\u043d"),

    /**
     * Автономный округ
     * 
     */
    АО("\u0410\u041e");
    private final String value;

    DDistrict(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static DDistrict fromValue(String v) {
        for (DDistrict c: DDistrict.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
