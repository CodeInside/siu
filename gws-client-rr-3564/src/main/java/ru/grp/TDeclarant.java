
/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.grp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tDeclarant", propOrder = {
    "person",
    "organization",
    "governance",
    "foreignOrganization"
})
public class TDeclarant {

    @XmlElement(name = "Person")
    protected TDeclarant.Person person;
    @XmlElement(name = "Organization")
    protected TOrganizationDeclarant organization;
    @XmlElement(name = "Governance")
    protected TGovernanceDeclarant governance;
    @XmlElement(name = "ForeignOrganization")
    protected TForeignOrganizationDeclarant foreignOrganization;
    @XmlAttribute(name = "declarant_kind", required = true)
    protected String declarantKind;
    @XmlAttribute(name = "id_db_egrp")
    protected String idDbEgrp;
    @XmlAttribute(required = true)
    protected boolean signatured;

    /**
     * Gets the value of the person property.
     * 
     * @return
     *     possible object is
     *     {@link TDeclarant.Person }
     *     
     */
    public TDeclarant.Person getPerson() {
        return person;
    }

    /**
     * Sets the value of the person property.
     * 
     * @param value
     *     allowed object is
     *     {@link TDeclarant.Person }
     *     
     */
    public void setPerson(TDeclarant.Person value) {
        this.person = value;
    }

    /**
     * Gets the value of the organization property.
     * 
     * @return
     *     possible object is
     *     {@link TOrganizationDeclarant }
     *     
     */
    public TOrganizationDeclarant getOrganization() {
        return organization;
    }

    /**
     * Sets the value of the organization property.
     * 
     * @param value
     *     allowed object is
     *     {@link TOrganizationDeclarant }
     *     
     */
    public void setOrganization(TOrganizationDeclarant value) {
        this.organization = value;
    }

    /**
     * Gets the value of the governance property.
     * 
     * @return
     *     possible object is
     *     {@link TGovernanceDeclarant }
     *     
     */
    public TGovernanceDeclarant getGovernance() {
        return governance;
    }

    /**
     * Sets the value of the governance property.
     * 
     * @param value
     *     allowed object is
     *     {@link TGovernanceDeclarant }
     *     
     */
    public void setGovernance(TGovernanceDeclarant value) {
        this.governance = value;
    }

    /**
     * Gets the value of the foreignOrganization property.
     * 
     * @return
     *     possible object is
     *     {@link TForeignOrganizationDeclarant }
     *     
     */
    public TForeignOrganizationDeclarant getForeignOrganization() {
        return foreignOrganization;
    }

    /**
     * Sets the value of the foreignOrganization property.
     * 
     * @param value
     *     allowed object is
     *     {@link TForeignOrganizationDeclarant }
     *     
     */
    public void setForeignOrganization(TForeignOrganizationDeclarant value) {
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

    /**
     * Gets the value of the idDbEgrp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdDbEgrp() {
        return idDbEgrp;
    }

    /**
     * Sets the value of the idDbEgrp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdDbEgrp(String value) {
        this.idDbEgrp = value;
    }

    /**
     * Gets the value of the signatured property.
     * 
     */
    public boolean isSignatured() {
        return signatured;
    }

    /**
     * Sets the value of the signatured property.
     * 
     */
    public void setSignatured(boolean value) {
        this.signatured = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;extension base="{}tPersonDeclarant">
     *     &lt;/extension>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Person
        extends TPersonDeclarant
    {


    }

}
