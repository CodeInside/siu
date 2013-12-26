
/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.grp;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tPersonOwner", propOrder = {
    "personDocuments",
    "fioList"
})
@XmlSeeAlso({
    ru.grp.RequestGRP.Request.RequiredData.RequiredDataIncapacity.IncapacityOwner.class
})
public class TPersonOwner
    extends PPersonOwnerV1
{

    @XmlElement(name = "PersonDocuments")
    protected TPersonOwner.PersonDocuments personDocuments;
    @XmlElement(name = "FIO_List")
    protected TPersonOwner.FIOList fioList;

    /**
     * Gets the value of the personDocuments property.
     * 
     * @return
     *     possible object is
     *     {@link TPersonOwner.PersonDocuments }
     *     
     */
    public TPersonOwner.PersonDocuments getPersonDocuments() {
        return personDocuments;
    }

    /**
     * Sets the value of the personDocuments property.
     * 
     * @param value
     *     allowed object is
     *     {@link TPersonOwner.PersonDocuments }
     *     
     */
    public void setPersonDocuments(TPersonOwner.PersonDocuments value) {
        this.personDocuments = value;
    }

    /**
     * Gets the value of the fioList property.
     * 
     * @return
     *     possible object is
     *     {@link TPersonOwner.FIOList }
     *     
     */
    public TPersonOwner.FIOList getFIOList() {
        return fioList;
    }

    /**
     * Sets the value of the fioList property.
     * 
     * @param value
     *     allowed object is
     *     {@link TPersonOwner.FIOList }
     *     
     */
    public void setFIOList(TPersonOwner.FIOList value) {
        this.fioList = value;
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
     *         &lt;element name="FIO" type="{}tFIO" maxOccurs="unbounded"/>
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
        "fio"
    })
    public static class FIOList {

        @XmlElement(name = "FIO", required = true)
        protected List<TFIO> fio;

        /**
         * Gets the value of the fio property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the fio property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getFIO().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link TFIO }
         * 
         * 
         */
        public List<TFIO> getFIO() {
            if (fio == null) {
                fio = new ArrayList<TFIO>();
            }
            return this.fio;
        }

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
     *         &lt;element name="PersonDocument" type="{}pIdentityPersonDocument_v1" maxOccurs="unbounded"/>
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
        "personDocument"
    })
    public static class PersonDocuments {

        @XmlElement(name = "PersonDocument", required = true)
        protected List<PIdentityPersonDocumentV1> personDocument;

        /**
         * Gets the value of the personDocument property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the personDocument property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getPersonDocument().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link PIdentityPersonDocumentV1 }
         * 
         * 
         */
        public List<PIdentityPersonDocumentV1> getPersonDocument() {
            if (personDocument == null) {
                personDocument = new ArrayList<PIdentityPersonDocumentV1>();
            }
            return this.personDocument;
        }

    }

}
