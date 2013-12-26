/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */


package ru.gkn;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tOwner", propOrder = {
    "person",
    "organization",
    "governance",
    "foreignOrganization"
})
public class TOwner {

    @XmlElement(name = "Person")
    protected TPerson person;
    @XmlElement(name = "Organization")
    protected TOrganization organization;
    @XmlElement(name = "Governance")
    protected TGovernance governance;
    @XmlElement(name = "ForeignOrganization")
    protected TForeignOrganization foreignOrganization;
    @XmlAttribute(name = "declarant_kind", required = true)
    protected String declarantKind;

    /**
     * Gets the value of the person property.
     * 
     * @return
     *     possible object is
     *     {@link TPerson }
     *     
     */
    public TPerson getPerson() {
        return person;
    }

    /**
     * Sets the value of the person property.
     * 
     * @param value
     *     allowed object is
     *     {@link TPerson }
     *     
     */
    public void setPerson(TPerson value) {
        this.person = value;
    }

    /**
     * Gets the value of the organization property.
     * 
     * @return
     *     possible object is
     *     {@link TOrganization }
     *     
     */
    public TOrganization getOrganization() {
        return organization;
    }

    /**
     * Sets the value of the organization property.
     * 
     * @param value
     *     allowed object is
     *     {@link TOrganization }
     *     
     */
    public void setOrganization(TOrganization value) {
        this.organization = value;
    }

    /**
     * Gets the value of the governance property.
     * 
     * @return
     *     possible object is
     *     {@link TGovernance }
     *     
     */
    public TGovernance getGovernance() {
        return governance;
    }

    /**
     * Sets the value of the governance property.
     * 
     * @param value
     *     allowed object is
     *     {@link TGovernance }
     *     
     */
    public void setGovernance(TGovernance value) {
        this.governance = value;
    }

    /**
     * Gets the value of the foreignOrganization property.
     * 
     * @return
     *     possible object is
     *     {@link TForeignOrganization }
     *     
     */
    public TForeignOrganization getForeignOrganization() {
        return foreignOrganization;
    }

    /**
     * Sets the value of the foreignOrganization property.
     * 
     * @param value
     *     allowed object is
     *     {@link TForeignOrganization }
     *     
     */
    public void setForeignOrganization(TForeignOrganization value) {
        this.foreignOrganization = value;
    }

    /**
     * Gets the value of the declarantKind property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeclarantKind() {
        return declarantKind;
    }

    /**
     * Sets the value of the declarantKind property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeclarantKind(String value) {
        this.declarantKind = value;
    }

}
