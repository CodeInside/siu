/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */


package ru.tower.mvd.response.addpayment;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {

})
@XmlRootElement(name = "\u041f\u0440\u0435\u043a\u0440\u0430\u0449\u0435\u043d\u0438\u0435\u0412\u044b\u043f\u043b\u0430\u0442")
public class ПрекращениеВыплат {

    @XmlElement(name = "\u041f\u0440\u0435\u043a\u0440\u0430\u0449\u0435\u043d\u0438\u0435\u0412\u044b\u043f\u043b\u0430\u0442", required = true)
    protected String прекращениеВыплат;
    @XmlElement(name = "\u0414\u0430\u0442\u0430")
    protected String дата;
    @XmlElement(name = "\u041e\u0440\u0433\u0430\u043d\u0438\u0437\u0430\u0446\u0438\u044f", required = true)
    protected String организация;
    @XmlElement(name = "\u041e\u0441\u043d\u043e\u0432\u0430\u043d\u0438\u0435", required = true)
    protected String основание;

    /**
     * Gets the value of the прекращениеВыплат property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getПрекращениеВыплат() {
        return прекращениеВыплат;
    }

    /**
     * Sets the value of the прекращениеВыплат property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setПрекращениеВыплат(String value) {
        this.прекращениеВыплат = value;
    }

    /**
     * Gets the value of the дата property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getДата() {
        return дата;
    }

    /**
     * Sets the value of the дата property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setДата(String value) {
        this.дата = value;
    }

    /**
     * Gets the value of the организация property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getОрганизация() {
        return организация;
    }

    /**
     * Sets the value of the организация property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setОрганизация(String value) {
        this.организация = value;
    }

    /**
     * Gets the value of the основание property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getОснование() {
        return основание;
    }

    /**
     * Sets the value of the основание property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setОснование(String value) {
        this.основание = value;
    }

}
