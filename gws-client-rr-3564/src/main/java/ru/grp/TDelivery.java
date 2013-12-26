
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
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tDelivery", propOrder = {
    "interdepartmentalRequest",
    "nameOKU",
    "postAddress",
    "eMailAddress",
    "linkEMail",
    "webService"
})
public class TDelivery {

    @XmlElement(name = "InterdepartmentalRequest")
    protected Boolean interdepartmentalRequest;
    @XmlElement(name = "NameOKU")
    protected String nameOKU;
    @XmlElement(name = "PostAddress")
    protected String postAddress;
    @XmlElement(name = "E_mailAddress")
    protected String eMailAddress;
    @XmlElement(name = "LinkE_mail")
    protected String linkEMail;
    @XmlElement(name = "WebService")
    protected Boolean webService;

    /**
     * Gets the value of the interdepartmentalRequest property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isInterdepartmentalRequest() {
        return interdepartmentalRequest;
    }

    /**
     * Sets the value of the interdepartmentalRequest property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setInterdepartmentalRequest(Boolean value) {
        this.interdepartmentalRequest = value;
    }

    /**
     * Gets the value of the nameOKU property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNameOKU() {
        return nameOKU;
    }

    /**
     * Sets the value of the nameOKU property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNameOKU(String value) {
        this.nameOKU = value;
    }

    /**
     * Gets the value of the postAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPostAddress() {
        return postAddress;
    }

    /**
     * Sets the value of the postAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPostAddress(String value) {
        this.postAddress = value;
    }

    /**
     * Gets the value of the eMailAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEMailAddress() {
        return eMailAddress;
    }

    /**
     * Sets the value of the eMailAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEMailAddress(String value) {
        this.eMailAddress = value;
    }

    /**
     * Gets the value of the linkEMail property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLinkEMail() {
        return linkEMail;
    }

    /**
     * Sets the value of the linkEMail property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLinkEMail(String value) {
        this.linkEMail = value;
    }

    /**
     * Gets the value of the webService property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isWebService() {
        return webService;
    }

    /**
     * Sets the value of the webService property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setWebService(Boolean value) {
        this.webService = value;
    }

}
