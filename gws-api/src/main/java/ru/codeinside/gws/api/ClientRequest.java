/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.api;

import javax.xml.namespace.QName;

final public class ClientRequest {
  public Packet packet;
  public QName action;
  public String soapActionHttpHeader;
  public QName service;
  public QName port;
  public String portAddress;
  public String appData;
  public boolean signRequired;

  public String enclosureDescriptor;
  public Enclosure[] enclosures;
  public boolean applicantSign;
  public boolean needEnvelopedSignatureForAppData;

}
