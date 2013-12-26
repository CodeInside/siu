
/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.tower.mvd.clients.common.responseid;

import javax.xml.bind.annotation.*;

import ru.tower.mvd.clients.common.responseid.message.Message;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "AppData")
@XmlType(name = "appData")
public class AppDataType {


    @XmlElement(name = "Message", required = true, namespace = "http://tower.ru/mvd/clients/common/responseID")
    protected Message message;

    /**
     * Gets the value of the signature property.
     * 
     * @return
     *     possible object is
     *     {@link SignatureType }
     *     
     */

    /**
     * Sets the value of the signature property.
     * 
     * @param value
     *     allowed object is
     *     {@link SignatureType }
     *     
     */


    /**
     * Gets the value of the message property.
     * 
     * @return
     *     possible object is
     *     {@link Message }
     *     
     */
    public Message getMessage() {
        return message;
    }

    /**
     * Sets the value of the message property.
     * 
     * @param value
     *     allowed object is
     *     {@link Message }
     *     
     */
    public void setMessage(Message value) {
        this.message = value;
    }

}
