
/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.roskazna.xsd.common;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


@XmlType(name = "AddressKindType")
@XmlEnum
public enum AddressKindType {

    @XmlEnumValue("Zip")
    ZIP("Zip"),
    @XmlEnumValue("Country")
    COUNTRY("Country"),
    @XmlEnumValue("Region")
    REGION("Region"),
    @XmlEnumValue("State")
    STATE("State"),
    @XmlEnumValue("Town")
    TOWN("Town"),
    @XmlEnumValue("City")
    CITY("City"),
    @XmlEnumValue("Street")
    STREET("Street"),
    @XmlEnumValue("House")
    HOUSE("House"),
    @XmlEnumValue("Building")
    BUILDING("Building"),
    @XmlEnumValue("Flat")
    FLAT("Flat");
    private final String value;

    AddressKindType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AddressKindType fromValue(String v) {
        for (AddressKindType c: AddressKindType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
