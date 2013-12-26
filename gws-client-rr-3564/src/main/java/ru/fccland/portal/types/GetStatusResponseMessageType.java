
/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.fccland.portal.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getStatusResponseMessageType", propOrder = {
    "statusResponseBean",
    "errorMessage"
})
public class GetStatusResponseMessageType {

    protected StatusResponseBean statusResponseBean;
    protected String errorMessage;

    /**
     * Gets the value of the statusResponseBean property.
     * 
     * @return
     *     possible object is
     *     {@link StatusResponseBean }
     *     
     */
    public StatusResponseBean getStatusResponseBean() {
        return statusResponseBean;
    }

    /**
     * Sets the value of the statusResponseBean property.
     * 
     * @param value
     *     allowed object is
     *     {@link StatusResponseBean }
     *     
     */
    public void setStatusResponseBean(StatusResponseBean value) {
        this.statusResponseBean = value;
    }

    /**
     * Gets the value of the errorMessage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Sets the value of the errorMessage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErrorMessage(String value) {
        this.errorMessage = value;
    }

}
