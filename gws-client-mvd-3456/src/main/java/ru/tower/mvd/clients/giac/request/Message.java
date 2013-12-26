
/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.tower.mvd.clients.giac.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {

})
@XmlRootElement(name = "Message")
public class Message {

    @XmlElement(name = "Header", required = true)
    protected BaseHeaderType header;
    @XmlElement(name = "Document", required = true)
    protected DocumentType document;
    @XmlElement(name = "DSignature")
    protected Object dSignature;

    /**
     * Gets the value of the header property.
     * 
     * @return
     *     possible object is
     *     {@link BaseHeaderType }
     *     
     */
    public BaseHeaderType getHeader() {
        return header;
    }

    /**
     * Sets the value of the header property.
     * 
     * @param value
     *     allowed object is
     *     {@link BaseHeaderType }
     *     
     */
    public void setHeader(BaseHeaderType value) {
        this.header = value;
    }

    /**
     * Gets the value of the document property.
     * 
     * @return
     *     possible object is
     *     {@link DocumentType }
     *     
     */
    public DocumentType getDocument() {
        return document;
    }

    /**
     * Sets the value of the document property.
     * 
     * @param value
     *     allowed object is
     *     {@link DocumentType }
     *     
     */
    public void setDocument(DocumentType value) {
        this.document = value;
    }

    /**
     * Gets the value of the dSignature property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getDSignature() {
        return dSignature;
    }

    /**
     * Sets the value of the dSignature property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setDSignature(Object value) {
        this.dSignature = value;
    }

}
