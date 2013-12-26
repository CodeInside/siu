
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
import ru.tower.mvd.clients.pf.requestid.AppDataType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RequestPFIDType", propOrder = {
    "message",
    "messageData"
})
public class RequestPFIDType {

    @XmlElement(name = "Message", required = true)
    protected MessageType message;
    @XmlElement(name = "MessageData", required = true)
    protected RequestPFIDType.MessageData messageData;

    /**
     * Gets the value of the message property.
     * 
     * @return
     *     possible object is
     *     {@link MessageType }
     *     
     */
    public MessageType getMessage() {
        return message;
    }

    /**
     * Sets the value of the message property.
     * 
     * @param value
     *     allowed object is
     *     {@link MessageType }
     *     
     */
    public void setMessage(MessageType value) {
        this.message = value;
    }

    /**
     * Gets the value of the messageData property.
     * 
     * @return
     *     possible object is
     *     {@link RequestPFIDType.MessageData }
     *     
     */
    public RequestPFIDType.MessageData getMessageData() {
        return messageData;
    }

    /**
     * Sets the value of the messageData property.
     * 
     * @param value
     *     allowed object is
     *     {@link RequestPFIDType.MessageData }
     *     
     */
    public void setMessageData(RequestPFIDType.MessageData value) {
        this.messageData = value;
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
     *         &lt;element name="AppData" type="{http://tower.ru/mvd/clients/pf/requestID}AppDataType"/>
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
        "appData"
    })
    public static class MessageData {

        @XmlElement(name = "AppData", required = true)
        protected AppDataType appData;

        /**
         * Gets the value of the appData property.
         * 
         * @return
         *     possible object is
         *     {@link AppDataType }
         *     
         */
        public AppDataType getAppData() {
            return appData;
        }

        /**
         * Sets the value of the appData property.
         * 
         * @param value
         *     allowed object is
         *     {@link AppDataType }
         *     
         */
        public void setAppData(AppDataType value) {
            this.appData = value;
        }

    }

}
