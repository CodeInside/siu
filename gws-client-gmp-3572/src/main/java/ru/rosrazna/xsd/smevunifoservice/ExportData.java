
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
import ru.roskazna.xsd.pgu_datarequest.DataRequest;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "dataRequest"
})
@XmlRootElement(name = "exportData")
public class ExportData {

    @XmlElement(name = "DataRequest", namespace = "http://roskazna.ru/xsd/PGU_DataRequest", required = true)
    protected DataRequest dataRequest;

    /**
     * Gets the value of the dataRequest property.
     * 
     * @return
     *     possible object is
     *     {@link DataRequest }
     *     
     */
    public DataRequest getDataRequest() {
        return dataRequest;
    }

    /**
     * Sets the value of the dataRequest property.
     * 
     * @param value
     *     allowed object is
     *     {@link DataRequest }
     *     
     */
    public void setDataRequest(DataRequest value) {
        this.dataRequest = value;
    }

}
