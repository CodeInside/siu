
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
@XmlType(name = "tOwner", propOrder = {
    "personO",
    "organizationO",
    "governanceO",
    "foreignOrganizationO"
})
public class TOwner {

    @XmlElement(name = "PersonO")
    protected TPersonOwner personO;
    @XmlElement(name = "OrganizationO")
    protected TOrganizationOwner organizationO;
    @XmlElement(name = "GovernanceO")
    protected TGovernanceOwner governanceO;
    @XmlElement(name = "ForeignOrganizationO")
    protected TForeignOrganizationOwner foreignOrganizationO;
    @XmlAttribute(name = "id_db_egrp")
    protected String idDbEgrp;

    /**
     * Gets the value of the personO property.
     * 
     * @return
     *     possible object is
     *     {@link TPersonOwner }
     *     
     */
    public TPersonOwner getPersonO() {
        return personO;
    }

    /**
     * Sets the value of the personO property.
     * 
     * @param value
     *     allowed object is
     *     {@link TPersonOwner }
     *     
     */
    public void setPersonO(TPersonOwner value) {
        this.personO = value;
    }

    /**
     * Gets the value of the organizationO property.
     * 
     * @return
     *     possible object is
     *     {@link TOrganizationOwner }
     *     
     */
    public TOrganizationOwner getOrganizationO() {
        return organizationO;
    }

    /**
     * Sets the value of the organizationO property.
     * 
     * @param value
     *     allowed object is
     *     {@link TOrganizationOwner }
     *     
     */
    public void setOrganizationO(TOrganizationOwner value) {
        this.organizationO = value;
    }

    /**
     * Gets the value of the governanceO property.
     * 
     * @return
     *     possible object is
     *     {@link TGovernanceOwner }
     *     
     */
    public TGovernanceOwner getGovernanceO() {
        return governanceO;
    }

    /**
     * Sets the value of the governanceO property.
     * 
     * @param value
     *     allowed object is
     *     {@link TGovernanceOwner }
     *     
     */
    public void setGovernanceO(TGovernanceOwner value) {
        this.governanceO = value;
    }

    /**
     * Gets the value of the foreignOrganizationO property.
     * 
     * @return
     *     possible object is
     *     {@link TForeignOrganizationOwner }
     *     
     */
    public TForeignOrganizationOwner getForeignOrganizationO() {
        return foreignOrganizationO;
    }

    /**
     * Sets the value of the foreignOrganizationO property.
     * 
     * @param value
     *     allowed object is
     *     {@link TForeignOrganizationOwner }
     *     
     */
    public void setForeignOrganizationO(TForeignOrganizationOwner value) {
        this.foreignOrganizationO = value;
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

}
