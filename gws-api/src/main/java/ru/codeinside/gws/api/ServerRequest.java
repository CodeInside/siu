/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.api;

import org.w3c.dom.Element;

import javax.xml.namespace.QName;
import java.util.List;

/**
 * Запрос от потребитля для обработки поставщиком.
 */
public class ServerRequest {
    /**
     * Результат проверки ЭЦП.
     */
    public VerifyResult verifyResult;

    /**
     * Пакет маршрутизатора СМЭВ.
     */
    public RouterPacket routerPacket;

    /**
     * Имя операции (по WSDL).
     */
    public QName action;

    /**
     * Управляющий пакет СМЭВ.
     */
    public Packet packet;

    /**
     * Элемент с данными потребителя (AppData).
     */
    public Element appData;

    /**
     * Код документа.
     * Также используется как идентификатор описателя вложений.
     */
    public String docRequestCode;

    /**
     * Вложения.
     */
    public List<Enclosure> attachmens;

    @Override
    public String toString() {
        return "{" +
                "verifyResult=" + verifyResult +
                ", routerPacket=" + routerPacket +
                ", action=" + action +
                ", packet=" + packet +
                ", appData=" + appData +
                ", docRequestCode='" + docRequestCode + '\'' +
                ", attachmens=" + attachmens +
                '}';
    }
}
