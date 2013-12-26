/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */


package ru.gkn;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tExtractRoom", propOrder = {
    "isNondomestic",
    "isFlat",
    "isRoom"
})
public class TExtractRoom {

    @XmlElement(name = "IsNondomestic")
    protected Boolean isNondomestic;
    @XmlElement(name = "IsFlat")
    protected Boolean isFlat;
    @XmlElement(name = "IsRoom")
    protected Boolean isRoom;

    /**
     * Gets the value of the isNondomestic property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsNondomestic() {
        return isNondomestic;
    }

    /**
     * Sets the value of the isNondomestic property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsNondomestic(Boolean value) {
        this.isNondomestic = value;
    }

    /**
     * Gets the value of the isFlat property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsFlat() {
        return isFlat;
    }

    /**
     * Sets the value of the isFlat property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsFlat(Boolean value) {
        this.isFlat = value;
    }

    /**
     * Gets the value of the isRoom property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsRoom() {
        return isRoom;
    }

    /**
     * Sets the value of the isRoom property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsRoom(Boolean value) {
        this.isRoom = value;
    }

}
