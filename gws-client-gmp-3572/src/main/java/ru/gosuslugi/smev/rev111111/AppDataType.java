
/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.gosuslugi.smev.rev111111;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import ru.roskazna.xsd.doacknowledgmentrequest.DoAcknowledgmentRequestType;
import ru.roskazna.xsd.doacknowledgmentresponse.DoAcknowledgmentResponseType;
import ru.rosrazna.xsd.smevunifoservice.ExportData;
import ru.rosrazna.xsd.smevunifoservice.ExportDataResponse;
import ru.rosrazna.xsd.smevunifoservice.ImportData;
import ru.rosrazna.xsd.smevunifoservice.ImportDataResponse;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AppDataType", propOrder = {
    "importData",
    "importDataResponse",
    "exportData",
    "exportDataResponse",
    "doAcknowledgmentRequest",
    "doAcknowledgmentResponse"
})
public class AppDataType {

    @XmlElement(name = "ImportData", namespace = "http://rosrazna.ru/xsd/SmevUnifoService")
    protected ImportData importData;
    @XmlElement(name = "ImportDataResponse", namespace = "http://rosrazna.ru/xsd/SmevUnifoService")
    protected ImportDataResponse importDataResponse;
    @XmlElement(namespace = "http://rosrazna.ru/xsd/SmevUnifoService")
    protected ExportData exportData;
    @XmlElement(namespace = "http://rosrazna.ru/xsd/SmevUnifoService")
    protected ExportDataResponse exportDataResponse;
    @XmlElement(name = "DoAcknowledgmentRequest", namespace = "http://roskazna.ru/xsd/DoAcknowledgmentRequest")
    protected DoAcknowledgmentRequestType doAcknowledgmentRequest;
    @XmlElement(name = "DoAcknowledgmentResponse", namespace = "http://roskazna.ru/xsd/DoAcknowledgmentResponse")
    protected DoAcknowledgmentResponseType doAcknowledgmentResponse;

    /**
     * Gets the value of the importData property.
     * 
     * @return
     *     possible object is
     *     {@link ImportData }
     *     
     */
    public ImportData getImportData() {
        return importData;
    }

    /**
     * Sets the value of the importData property.
     * 
     * @param value
     *     allowed object is
     *     {@link ImportData }
     *     
     */
    public void setImportData(ImportData value) {
        this.importData = value;
    }

    /**
     * Gets the value of the importDataResponse property.
     * 
     * @return
     *     possible object is
     *     {@link ImportDataResponse }
     *     
     */
    public ImportDataResponse getImportDataResponse() {
        return importDataResponse;
    }

    /**
     * Sets the value of the importDataResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link ImportDataResponse }
     *     
     */
    public void setImportDataResponse(ImportDataResponse value) {
        this.importDataResponse = value;
    }

    /**
     * Gets the value of the exportData property.
     * 
     * @return
     *     possible object is
     *     {@link ExportData }
     *     
     */
    public ExportData getExportData() {
        return exportData;
    }

    /**
     * Sets the value of the exportData property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExportData }
     *     
     */
    public void setExportData(ExportData value) {
        this.exportData = value;
    }

    /**
     * Gets the value of the exportDataResponse property.
     * 
     * @return
     *     possible object is
     *     {@link ExportDataResponse }
     *     
     */
    public ExportDataResponse getExportDataResponse() {
        return exportDataResponse;
    }

    /**
     * Sets the value of the exportDataResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExportDataResponse }
     *     
     */
    public void setExportDataResponse(ExportDataResponse value) {
        this.exportDataResponse = value;
    }

    /**
     * Gets the value of the doAcknowledgmentRequest property.
     * 
     * @return
     *     possible object is
     *     {@link DoAcknowledgmentRequestType }
     *     
     */
    public DoAcknowledgmentRequestType getDoAcknowledgmentRequest() {
        return doAcknowledgmentRequest;
    }

    /**
     * Sets the value of the doAcknowledgmentRequest property.
     * 
     * @param value
     *     allowed object is
     *     {@link DoAcknowledgmentRequestType }
     *     
     */
    public void setDoAcknowledgmentRequest(DoAcknowledgmentRequestType value) {
        this.doAcknowledgmentRequest = value;
    }

    /**
     * Gets the value of the doAcknowledgmentResponse property.
     * 
     * @return
     *     possible object is
     *     {@link DoAcknowledgmentResponseType }
     *     
     */
    public DoAcknowledgmentResponseType getDoAcknowledgmentResponse() {
        return doAcknowledgmentResponse;
    }

    /**
     * Sets the value of the doAcknowledgmentResponse property.
     * 
     * @param value
     *     allowed object is
     *     {@link DoAcknowledgmentResponseType }
     *     
     */
    public void setDoAcknowledgmentResponse(DoAcknowledgmentResponseType value) {
        this.doAcknowledgmentResponse = value;
    }

}
