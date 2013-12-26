/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */


package ru.gkn;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tQuantity", propOrder = {
    "original",
    "copy",
    "originalAndCopy"
})
public class TQuantity {

    @XmlElement(name = "Original")
    protected TQuantityAttribute original;
    @XmlElement(name = "Copy")
    protected TQuantityAttribute copy;
    @XmlElement(name = "OriginalAndCopy")
    protected TQuantity.OriginalAndCopy originalAndCopy;

    /**
     * Gets the value of the original property.
     * 
     * @return
     *     possible object is
     *     {@link TQuantityAttribute }
     *     
     */
    public TQuantityAttribute getOriginal() {
        return original;
    }

    /**
     * Sets the value of the original property.
     * 
     * @param value
     *     allowed object is
     *     {@link TQuantityAttribute }
     *     
     */
    public void setOriginal(TQuantityAttribute value) {
        this.original = value;
    }

    /**
     * Gets the value of the copy property.
     * 
     * @return
     *     possible object is
     *     {@link TQuantityAttribute }
     *     
     */
    public TQuantityAttribute getCopy() {
        return copy;
    }

    /**
     * Sets the value of the copy property.
     * 
     * @param value
     *     allowed object is
     *     {@link TQuantityAttribute }
     *     
     */
    public void setCopy(TQuantityAttribute value) {
        this.copy = value;
    }

    /**
     * Gets the value of the originalAndCopy property.
     * 
     * @return
     *     possible object is
     *     {@link TQuantity.OriginalAndCopy }
     *     
     */
    public TQuantity.OriginalAndCopy getOriginalAndCopy() {
        return originalAndCopy;
    }

    /**
     * Sets the value of the originalAndCopy property.
     * 
     * @param value
     *     allowed object is
     *     {@link TQuantity.OriginalAndCopy }
     *     
     */
    public void setOriginalAndCopy(TQuantity.OriginalAndCopy value) {
        this.originalAndCopy = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="Original" type="{}tQuantityAttribute"/>
     *         &lt;element name="Copy" type="{}tQuantityAttribute"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "original",
        "copy"
    })
    public static class OriginalAndCopy {

        @XmlElement(name = "Original", required = true)
        protected TQuantityAttribute original;
        @XmlElement(name = "Copy", required = true)
        protected TQuantityAttribute copy;

        /**
         * Gets the value of the original property.
         * 
         * @return
         *     possible object is
         *     {@link TQuantityAttribute }
         *     
         */
        public TQuantityAttribute getOriginal() {
            return original;
        }

        /**
         * Sets the value of the original property.
         * 
         * @param value
         *     allowed object is
         *     {@link TQuantityAttribute }
         *     
         */
        public void setOriginal(TQuantityAttribute value) {
            this.original = value;
        }

        /**
         * Gets the value of the copy property.
         * 
         * @return
         *     possible object is
         *     {@link TQuantityAttribute }
         *     
         */
        public TQuantityAttribute getCopy() {
            return copy;
        }

        /**
         * Sets the value of the copy property.
         * 
         * @param value
         *     allowed object is
         *     {@link TQuantityAttribute }
         *     
         */
        public void setCopy(TQuantityAttribute value) {
            this.copy = value;
        }

    }

}
