
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


@XmlType(name = "dSovietVillage")
@XmlEnum
public enum DSovietVillage {


    /**
     * Сельсовет
     * 
     */
    @XmlEnumValue("\u0441/\u0441")
    С_С("\u0441/\u0441"),

    /**
     * Волость
     * 
     */
    @XmlEnumValue("\u0432\u043e\u043b\u043e\u0441\u0442\u044c")
    ВОЛОСТЬ("\u0432\u043e\u043b\u043e\u0441\u0442\u044c");
    private final String value;

    DSovietVillage(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static DSovietVillage fromValue(String v) {
        for (DSovietVillage c: DSovietVillage.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
