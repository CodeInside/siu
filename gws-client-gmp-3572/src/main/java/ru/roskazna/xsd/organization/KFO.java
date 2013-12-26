
/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.roskazna.xsd.organization;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "KFOType", propOrder = {
    "kfoStatus",
    "kfoRequisites",
    "dboSiteURL",
    "certificate"
})
public class KFO
    extends Organization
{

    @XmlElement(name = "KFOStatus", required = true)
    protected String kfoStatus;
    @XmlElement(name = "KFORequisites", required = true)
    protected Bank kfoRequisites;
    @XmlSchemaType(name = "anyURI")
    protected String dboSiteURL;
    protected String certificate;

    /**
     * Gets the value of the kfoStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKFOStatus() {
        return kfoStatus;
    }

    /**
     * Sets the value of the kfoStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKFOStatus(String value) {
        this.kfoStatus = value;
    }

    /**
     * Gets the value of the kfoRequisites property.
     * 
     * @return
     *     possible object is
     *     {@link Bank }
     *     
     */
    public Bank getKFORequisites() {
        return kfoRequisites;
    }

    /**
     * Sets the value of the kfoRequisites property.
     * 
     * @param value
     *     allowed object is
     *     {@link Bank }
     *     
     */
    public void setKFORequisites(Bank value) {
        this.kfoRequisites = value;
    }

    /**
     * Gets the value of the dboSiteURL property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDboSiteURL() {
        return dboSiteURL;
    }

    /**
     * Sets the value of the dboSiteURL property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDboSiteURL(String value) {
        this.dboSiteURL = value;
    }

    /**
     * Gets the value of the certificate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCertificate() {
        return certificate;
    }

    /**
     * Sets the value of the certificate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCertificate(String value) {
        this.certificate = value;
    }

}
