/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */


package ru.gkn;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tObjectLot", propOrder = {
    "cadastralNumber",
    "location",
    "objectDesc"
})
public class TObjectLot {

    @XmlElement(name = "CadastralNumber")
    protected String cadastralNumber;
    @XmlElement(name = "Location")
    protected PAddressV1 location;
    @XmlElement(name = "ObjectDesc")
    protected ru.gkn.TObject.ObjectDesc objectDesc;
    @XmlAttribute(name = "obj_kind", required = true)
    protected String objKind;

    /**
     * Gets the value of the cadastralNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCadastralNumber() {
        return cadastralNumber;
    }

    /**
     * Sets the value of the cadastralNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCadastralNumber(String value) {
        this.cadastralNumber = value;
    }

    /**
     * Gets the value of the location property.
     * 
     * @return
     *     possible object is
     *     {@link PAddressV1 }
     *     
     */
    public PAddressV1 getLocation() {
        return location;
    }

    /**
     * Sets the value of the location property.
     * 
     * @param value
     *     allowed object is
     *     {@link PAddressV1 }
     *     
     */
    public void setLocation(PAddressV1 value) {
        this.location = value;
    }

    /**
     * Gets the value of the objectDesc property.
     * 
     * @return
     *     possible object is
     *     {@link ru.gkn.TObject.ObjectDesc }
     *     
     */
    public ru.gkn.TObject.ObjectDesc getObjectDesc() {
        return objectDesc;
    }

    /**
     * Sets the value of the objectDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link ru.gkn.TObject.ObjectDesc }
     *     
     */
    public void setObjectDesc(ru.gkn.TObject.ObjectDesc value) {
        this.objectDesc = value;
    }

    /**
     * Gets the value of the objKind property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getObjKind() {
        if (objKind == null) {
            return "002001001000";
        } else {
            return objKind;
        }
    }

    /**
     * Sets the value of the objKind property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setObjKind(String value) {
        this.objKind = value;
    }

}
