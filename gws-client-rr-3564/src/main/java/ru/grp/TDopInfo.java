
/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.grp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tDopInfo", propOrder = {
    "name",
    "letter",
    "inventory",
    "infoAdd"
})
@XmlSeeAlso({
    TDopInfoParcel.class
})
public class TDopInfo {

    @XmlElement(name = "Name")
    protected String name;
    @XmlElement(name = "Letter")
    protected String letter;
    @XmlElement(name = "Inventory")
    protected String inventory;
    @XmlElement(name = "InfoAdd")
    protected String infoAdd;

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the letter property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLetter() {
        return letter;
    }

    /**
     * Sets the value of the letter property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLetter(String value) {
        this.letter = value;
    }

    /**
     * Gets the value of the inventory property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInventory() {
        return inventory;
    }

    /**
     * Sets the value of the inventory property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInventory(String value) {
        this.inventory = value;
    }

    /**
     * Gets the value of the infoAdd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInfoAdd() {
        return infoAdd;
    }

    /**
     * Sets the value of the infoAdd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInfoAdd(String value) {
        this.infoAdd = value;
    }

}
