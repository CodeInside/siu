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

public class ServiceDefinition {

  public Map<QName, Service> services;
  public Set<String> namespaces;
  public Set<URI> resources;

  static public class Service {
    public Map<QName, Port> ports;
  }

  static public class Port {
    public String soapAddress;
    public QName binding;
    public QName port;
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

  static public class Operation {
    public Arg in;
    public Arg out;

    @Override
    public String toString() {
      return "{" +
        "in=" + in +
        ", out=" + out +
        '}';
    }
  }

  static public class Arg {
    public String name;
    public String soapAction;
    public QName message;
    public Map<String, QName> parts;

    @Override
    public String toString() {
      return "{" +
        "name='" + name + '\'' +
        ", soapAction='" + soapAction + '\'' +
        ", message=" + message +
        ", parts=" + parts +
        '}';
    }
  }

}

