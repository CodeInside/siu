
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


@XmlType(name = "dUrbanDistrict")
@XmlEnum
public enum DUrbanDistrict {


    /**
     * район
     * 
     */
    @XmlEnumValue("\u0440-\u043d")
    Р_Н("\u0440-\u043d");
    private final String value;

    DUrbanDistrict(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static DUrbanDistrict fromValue(String v) {
        for (DUrbanDistrict c: DUrbanDistrict.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
