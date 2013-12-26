
/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.grp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tExtractSubjectObjectType", propOrder = {
    "parcel",
    "room",
    "building",
    "objKind"
})
public class TExtractSubjectObjectType {

    @XmlElement(name = "Parcel")
    protected TExtractParcel parcel;
    @XmlElement(name = "Room")
    protected TExtractRoom room;
    @XmlElement(name = "Building")
    protected TExtractBuilding building;
    @XmlElement(name = "Obj_Kind")
    protected String objKind;

    /**
     * Gets the value of the parcel property.
     * 
     * @return
     *     possible object is
     *     {@link TExtractParcel }
     *     
     */
    public TExtractParcel getParcel() {
        return parcel;
    }

    /**
     * Sets the value of the parcel property.
     * 
     * @param value
     *     allowed object is
     *     {@link TExtractParcel }
     *     
     */
    public void setParcel(TExtractParcel value) {
        this.parcel = value;
    }

    /**
     * Gets the value of the room property.
     * 
     * @return
     *     possible object is
     *     {@link TExtractRoom }
     *     
     */
    public TExtractRoom getRoom() {
        return room;
    }

    /**
     * Sets the value of the room property.
     * 
     * @param value
     *     allowed object is
     *     {@link TExtractRoom }
     *     
     */
    public void setRoom(TExtractRoom value) {
        this.room = value;
    }

    /**
     * Gets the value of the building property.
     * 
     * @return
     *     possible object is
     *     {@link TExtractBuilding }
     *     
     */
    public TExtractBuilding getBuilding() {
        return building;
    }

    /**
     * Sets the value of the building property.
     * 
     * @param value
     *     allowed object is
     *     {@link TExtractBuilding }
     *     
     */
    public void setBuilding(TExtractBuilding value) {
        this.building = value;
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
        return objKind;
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
