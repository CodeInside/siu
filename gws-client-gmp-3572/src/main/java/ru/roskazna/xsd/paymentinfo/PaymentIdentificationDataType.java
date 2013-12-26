
/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.roskazna.xsd.paymentinfo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import ru.roskazna.xsd.organization.Bank;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PaymentIdentificationDataType", propOrder = {
    "bank",
    "ufk",
    "systemIdentifier"
})
public class PaymentIdentificationDataType {

    @XmlElement(name = "Bank")
    protected Bank bank;
    @XmlElement(name = "UFK")
    protected String ufk;
    @XmlElement(name = "SystemIdentifier", required = true)
    protected String systemIdentifier;

    /**
     * Gets the value of the bank property.
     * 
     * @return
     *     possible object is
     *     {@link ru.roskazna.xsd.organization.Bank }
     *     
     */
    public Bank getBank() {
        return bank;
    }

    /**
     * Sets the value of the bank property.
     * 
     * @param value
     *     allowed object is
     *     {@link ru.roskazna.xsd.organization.Bank }
     *     
     */
    public void setBank(Bank value) {
        this.bank = value;
    }

    /**
     * Gets the value of the ufk property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUFK() {
        return ufk;
    }

    /**
     * Sets the value of the ufk property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUFK(String value) {
        this.ufk = value;
    }

    /**
     * Gets the value of the systemIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSystemIdentifier() {
        return systemIdentifier;
    }

    /**
     * Sets the value of the systemIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSystemIdentifier(String value) {
        this.systemIdentifier = value;
    }

}
