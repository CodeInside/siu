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
@XmlType(name = "tApplied_Document", propOrder = {
    "quantity",
    "appliedFiles"
})
public class TAppliedDocument
    extends TDocument
{

    @XmlElement(name = "Quantity", required = true)
    protected TQuantity quantity;
    @XmlElement(name = "AppliedFiles")
    protected TAppliedFile appliedFiles;

    /**
     * Gets the value of the quantity property.
     * 
     * @return
     *     possible object is
     *     {@link TQuantity }
     *     
     */
    public TQuantity getQuantity() {
        return quantity;
    }

    /**
     * Sets the value of the quantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link TQuantity }
     *     
     */
    public void setQuantity(TQuantity value) {
        this.quantity = value;
    }

    /**
     * Gets the value of the appliedFiles property.
     * 
     * @return
     *     possible object is
     *     {@link TAppliedFile }
     *     
     */
    public TAppliedFile getAppliedFiles() {
        return appliedFiles;
    }

    /**
     * Sets the value of the appliedFiles property.
     * 
     * @param value
     *     allowed object is
     *     {@link TAppliedFile }
     *     
     */
    public void setAppliedFiles(TAppliedFile value) {
        this.appliedFiles = value;
    }

}
