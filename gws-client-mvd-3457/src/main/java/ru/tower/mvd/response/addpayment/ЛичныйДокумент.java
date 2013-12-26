/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */


package ru.tower.mvd.response.addpayment;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {

})
@XmlRootElement(name = "\u041b\u0438\u0447\u043d\u044b\u0439\u0414\u043e\u043a\u0443\u043c\u0435\u043d\u0442")
public class ЛичныйДокумент {

    @XmlElement(name = "\u0412\u0438\u0434", required = true)
    protected String вид;
    @XmlElement(name = "\u041d\u043e\u043c\u0435\u0440", required = true)
    @XmlSchemaType(name = "unsignedLong")
    protected BigInteger номер;

    /**
     * Gets the value of the вид property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getВид() {
        return вид;
    }

    /**
     * Sets the value of the вид property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setВид(String value) {
        this.вид = value;
    }

    /**
     * Gets the value of the номер property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getНомер() {
        return номер;
    }

    /**
     * Sets the value of the номер property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setНомер(BigInteger value) {
        this.номер = value;
    }

}
