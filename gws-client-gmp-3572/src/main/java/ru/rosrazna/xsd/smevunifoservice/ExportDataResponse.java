
/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.rosrazna.xsd.smevunifoservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import ru.roskazna.xsd.responsetemplate.ResponseTemplate;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "responseTemplate"
})
@XmlRootElement(name = "exportDataResponse")
public class ExportDataResponse {

    @XmlElement(name = "ResponseTemplate", namespace = "", required = true)
    protected ResponseTemplate responseTemplate;

    /**
     * Gets the value of the responseTemplate property.
     * 
     * @return
     *     possible object is
     *     {@link ResponseTemplate }
     *     
     */
    public ResponseTemplate getResponseTemplate() {
        return responseTemplate;
    }

    /**
     * Sets the value of the responseTemplate property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResponseTemplate }
     *     
     */
    public void setResponseTemplate(ResponseTemplate value) {
        this.responseTemplate = value;
    }

}
