/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.api;

import org.w3c.dom.Element;

import javax.xml.namespace.QName;
import java.util.Arrays;

/**
 * Ответ от поставщика для потребителя.
 */
final public class ClientResponse {

    /**
     * Результат проверки ЭЦП.
     */
    public VerifyResult verifyResult;

    /**
     * Пакет маршрутизатора СМЭВ.
     */
    public RouterPacket routerPacket;

    /**
     * Операция (по WSDL).
     */
    public QName action;

    /**
     * Управляющий пакет СМЭВ.
     */
    public Packet packet;

    /**
     * Элемент с данными поставщика (AppData).
     */
    public Element appData;

    /**
     * Идентификатор описателя вложений.
     */
    public String enclosureDescriptor;

    /**
     * Вложения.
     */
    public Enclosure[] enclosures;

    @Override
    public String toString() {
        return "{" +
                "verifyResult=" + verifyResult +
                ", routerPacket=" + routerPacket +
                ", action=" + action +
                ", packet=" + packet +
                ", appData=" + appData +
                ", enclosureDescriptor='" + enclosureDescriptor + '\'' +
                ", enclosures=" + Arrays.toString(enclosures) +
                '}';
    }
}
