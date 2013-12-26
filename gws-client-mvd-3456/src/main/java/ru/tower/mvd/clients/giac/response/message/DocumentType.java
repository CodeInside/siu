
/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.tower.mvd.clients.giac.response.message;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DocumentType", propOrder = {

})
public class DocumentType {

    @XmlElement(name = "PrivatePerson", required = true)
    protected GIACPrivatePersonType privatePerson;

    /**
     * Gets the value of the privatePerson property.
     * 
     * @return
     *     possible object is
     *     {@link GIACPrivatePersonType }
     *     
     */
    public GIACPrivatePersonType getPrivatePerson() {
        return privatePerson;
    }

    /**
     * Sets the value of the privatePerson property.
     * 
     * @param value
     *     allowed object is
     *     {@link GIACPrivatePersonType }
     *     
     */
    public void setPrivatePerson(GIACPrivatePersonType value) {
        this.privatePerson = value;
    }

}
