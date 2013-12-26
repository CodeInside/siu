
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
@XmlType(name = "tExtractObjectType", propOrder = {
    "room",
    "building",
    "objKind"
})
public class TExtractObjectType {

    @XmlElement(name = "Room")
    protected TExtractRoomRequired room;
    @XmlElement(name = "Building")
    protected TExtractBuildingRequired building;
    @XmlElement(name = "Obj_Kind")
    protected String objKind;

    /**
     * Gets the value of the room property.
     * 
     * @return
     *     possible object is
     *     {@link TExtractRoomRequired }
     *     
     */
    public TExtractRoomRequired getRoom() {
        return room;
    }

    /**
     * Sets the value of the room property.
     * 
     * @param value
     *     allowed object is
     *     {@link TExtractRoomRequired }
     *     
     */
    public void setRoom(TExtractRoomRequired value) {
        this.room = value;
    }

    /**
     * Gets the value of the building property.
     * 
     * @return
     *     possible object is
     *     {@link TExtractBuildingRequired }
     *     
     */
    public TExtractBuildingRequired getBuilding() {
        return building;
    }

    /**
     * Sets the value of the building property.
     * 
     * @param value
     *     allowed object is
     *     {@link TExtractBuildingRequired }
     *     
     */
    public void setBuilding(TExtractBuildingRequired value) {
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
