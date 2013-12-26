
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
import ru.roskazna.xsd.pgu_importrequest.ImportRequest;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "importRequest"
})
@XmlRootElement(name = "ImportData")
public class ImportData {

    @XmlElement(name = "ImportRequest", namespace = "http://roskazna.ru/xsd/PGU_ImportRequest", required = true)
    protected ImportRequest importRequest;

    /**
     * Gets the value of the importRequest property.
     * 
     * @return
     *     possible object is
     *     {@link ImportRequest }
     *     
     */
    public ImportRequest getImportRequest() {
        return importRequest;
    }

    /**
     * Sets the value of the importRequest property.
     * 
     * @param value
     *     allowed object is
     *     {@link ImportRequest }
     *     
     */
    public void setImportRequest(ImportRequest value) {
        this.importRequest = value;
    }

}
