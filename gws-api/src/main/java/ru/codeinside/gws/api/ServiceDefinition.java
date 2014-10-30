/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.api;

import javax.xml.namespace.QName;
import java.net.URI;
import java.util.Map;
import java.util.Set;

/**
 * Описатель WEB служб из WSDL, достаточный чтобы работать на уровне сообщений.
 */
final public class ServiceDefinition {

    /**
     * Именованные службы.
     */
    public Map<QName, Service> services;

    /**
     * Используемые в WSDL пространства имён.
     */
    public Set<String> namespaces;

    /**
     * Используемые в WSDL ресурсы.
     */
    public Set<URI> resources;

    @Override
    public String toString() {
        return "{services=" + services + ", namespaces=" + namespaces + ", resources=" + resources + '}';
    }

    /**
     * Служба SOAP.
     */
    final static public class Service {

        /**
         * Именованные порты.
         */
        public Map<QName, Port> ports;

        @Override
        public String toString() {
            return ports == null ? "{}" : ports.toString();
        }
    }

    /**
     * Описатель HTTP порта, через который проходят SOAP-операции.
     */
    final static public class Port {

        /**
         * Точка подключения.
         */
        public String soapAddress;
        /**
         * Имя свзязки.
         */

        public QName binding;

        /**
         * Имя порта.
         */
        public QName port;

        /**
         * Именованные операции.
         */
        public Map<QName, Operation> operations;

        @Override
        public String toString() {
            return "{" +
                    "soapAddress='" + soapAddress + '\'' +
                    ", binding=" + binding +
                    ", port=" + port +
                    ", operations=" + operations +
                    '}';
        }
    }

    /**
     * SOAP-oперация
     */
    final static public class Operation {

        /**
         * HTTP заголовок SOAPAction.
         */
        public String soapAction;

        /**
         * Входящий аргумент.
         */
        public Arg in;

        /**
         * Исходящий аргумент.
         */
        public Arg out;

        @Override
        public String toString() {
            return "{soapAction='" + soapAction + "', in=" + in + ", out=" + out + '}';
        }
    }

    /**
     * Аргумент для SOAP-операции
     */
    final static public class Arg {

        /**
         * Имя аргумента.
         */
        public String name;

        /**
         * Имя типа сообщения.
         */
        public QName message;

        /**
         * Именованные части.
         */
        public Map<String, QName> parts;

        @Override
        public String toString() {
            return "{name='" + name + "', message=" + message + ", parts=" + parts + '}';
        }
    }

}

