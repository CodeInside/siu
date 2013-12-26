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
@XmlType(name = "tExtractBuilding", propOrder = {
    "isNondomestic",
    "isLiving",
    "isApartments"
})
public class TExtractBuilding {

    @XmlElement(name = "IsNondomestic")
    protected Boolean isNondomestic;
    @XmlElement(name = "IsLiving")
    protected Boolean isLiving;
    @XmlElement(name = "IsApartments")
    protected Boolean isApartments;

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
     * Gets the value of the isLiving property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsLiving() {
        return isLiving;
    }

    /**
     * Sets the value of the isLiving property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsLiving(Boolean value) {
        this.isLiving = value;
    }

    /**
     * Gets the value of the isApartments property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsApartments() {
        return isApartments;
    }

    /**
     * Sets the value of the isApartments property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsApartments(Boolean value) {
        this.isApartments = value;
    }

}
