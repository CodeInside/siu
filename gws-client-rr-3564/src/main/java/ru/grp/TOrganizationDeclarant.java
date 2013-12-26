
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
@XmlType(name = "tOrganizationDeclarant", propOrder = {
    "agent"
})
public class TOrganizationDeclarant
    extends POrganizationV1
{

    @XmlElement(name = "Agent", required = true)
    protected TAgent agent;

    /**
     * Gets the value of the agent property.
     * 
     * @return
     *     possible object is
     *     {@link TAgent }
     *     
     */
    public TAgent getAgent() {
        return agent;
    }

    /**
     * Sets the value of the agent property.
     * 
     * @param value
     *     allowed object is
     *     {@link TAgent }
     *     
     */
    public void setAgent(TAgent value) {
        this.agent = value;
    }

}
