
/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.fccland.portal.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "createRequestRequestMessageType", propOrder = {
    "createRequestBean"
})
public class CreateRequestRequestMessageType {

    @XmlElement(required = true)
    protected CreateRequestBean createRequestBean;

    /**
     * Gets the value of the createRequestBean property.
     * 
     * @return
     *     possible object is
     *     {@link CreateRequestBean }
     *     
     */
    public CreateRequestBean getCreateRequestBean() {
        return createRequestBean;
    }

    /**
     * Sets the value of the createRequestBean property.
     * 
     * @param value
     *     allowed object is
     *     {@link CreateRequestBean }
     *     
     */
    public void setCreateRequestBean(CreateRequestBean value) {
        this.createRequestBean = value;
    }

}
