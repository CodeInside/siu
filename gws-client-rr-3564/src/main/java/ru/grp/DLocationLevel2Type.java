
/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.grp;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


@XmlType(name = "dLocationLevel2Type")
@XmlEnum
public enum DLocationLevel2Type {


    /**
     * Корпус
     * 
     */
    @XmlEnumValue("\u043a\u043e\u0440\u043f")
    КОРП("\u043a\u043e\u0440\u043f"),

    /**
     * Дом
     * 
     */
    @XmlEnumValue("\u0434")
    Д("\u0434"),

    /**
     * Участок
     * 
     */
    @XmlEnumValue("\u0443\u0447")
    УЧ("\u0443\u0447"),

    /**
     * Владение
     * 
     */
    @XmlEnumValue("\u0432\u043b")
    ВЛ("\u0432\u043b"),

    /**
     * Гараж
     * 
     */
    @XmlEnumValue("\u0433\u0430\u0440\u0430\u0436")
    ГАРАЖ("\u0433\u0430\u0440\u0430\u0436"),

    /**
     * бокс
     * 
     */
    @XmlEnumValue("\u0431\u043e\u043a\u0441")
    БОКС("\u0431\u043e\u043a\u0441"),

    /**
     * строение
     * 
     */
    @XmlEnumValue("\u0441\u0442\u0440\u043e\u0435\u043d\u0438\u0435")
    СТРОЕНИЕ("\u0441\u0442\u0440\u043e\u0435\u043d\u0438\u0435"),

    /**
     * сооружение
     * 
     */
    @XmlEnumValue("\u0441\u043e\u043e\u0440\u0443\u0436\u0435\u043d\u0438\u0435")
    СООРУЖЕНИЕ("\u0441\u043e\u043e\u0440\u0443\u0436\u0435\u043d\u0438\u0435");
    private final String value;

    DLocationLevel2Type(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static DLocationLevel2Type fromValue(String v) {
        for (DLocationLevel2Type c: DLocationLevel2Type.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
