
/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.gosuslugi.smev.rev111111;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import ru.tower.mvd.clients.common.responseid.AppDataType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResponseIDMessageData", propOrder = {
    "appData"
})
public class ResponseIDMessageData {

    @XmlElement(name = "AppData", required = true)
    protected AppDataType appData;

    /**
     * Gets the value of the appData property.
     * 
     * @return
     *     possible object is
     *     {@link AppDataType }
     *     
     */
    public AppDataType getAppData() {
        return appData;
    }

    /**
     * Sets the value of the appData property.
     * 
     * @param value
     *     allowed object is
     *     {@link AppDataType }
     *     
     */
    public void setAppData(AppDataType value) {
        this.appData = value;
    }

}
