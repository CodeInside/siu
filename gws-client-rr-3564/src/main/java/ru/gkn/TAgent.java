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
@XmlType(name = "tAgent", propOrder = {
    "agentKind",
    "attorneyDocument"
})
public class TAgent
    extends PPersonV1
{

    @XmlElement(name = "agent_kind", required = true)
    protected String agentKind;
    @XmlElement(name = "AttorneyDocument")
    protected TAppliedDocument attorneyDocument;

    /**
     * Gets the value of the agentKind property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAgentKind() {
        return agentKind;
    }

    /**
     * Sets the value of the agentKind property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAgentKind(String value) {
        this.agentKind = value;
    }

    /**
     * Gets the value of the attorneyDocument property.
     * 
     * @return
     *     possible object is
     *     {@link TAppliedDocument }
     *     
     */
    public TAppliedDocument getAttorneyDocument() {
        return attorneyDocument;
    }

    /**
     * Sets the value of the attorneyDocument property.
     * 
     * @param value
     *     allowed object is
     *     {@link TAppliedDocument }
     *     
     */
    public void setAttorneyDocument(TAppliedDocument value) {
        this.attorneyDocument = value;
    }

}
