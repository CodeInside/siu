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


@XmlType(name = "dLocationLevel3Type")
@XmlEnum
public enum DLocationLevel3Type {


    /**
     * Строение
     * 
     */
    @XmlEnumValue("\u0441\u0442\u0440\u043e\u0435\u043d")
    СТРОЕН("\u0441\u0442\u0440\u043e\u0435\u043d"),

    /**
     * Корпус
     * 
     */
    @XmlEnumValue("\u043a\u043e\u0440\u043f")
    КОРП("\u043a\u043e\u0440\u043f"),

    /**
     * Блок
     * 
     */
    @XmlEnumValue("\u0431\u043b\u043e\u043a")
    БЛОК("\u0431\u043b\u043e\u043a"),

    /**
     * литера
     * 
     */
    @XmlEnumValue("\u043b\u0438\u0442\u0435\u0440\u0430")
    ЛИТЕРА("\u043b\u0438\u0442\u0435\u0440\u0430");
    private final String value;

    DLocationLevel3Type(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static DLocationLevel3Type fromValue(String v) {
        for (DLocationLevel3Type c: DLocationLevel3Type.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
