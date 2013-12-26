
/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.grp;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


@XmlType(name = "sSex")
@XmlEnum
public enum SSex {


    /**
     * Мужской
     * 
     */
    M,

    /**
     * Женский
     * 
     */
    F;

    public String value() {
        return name();
    }

    public static SSex fromValue(String v) {
        return valueOf(v);
    }

}
