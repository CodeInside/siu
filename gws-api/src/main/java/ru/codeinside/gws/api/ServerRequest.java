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

public class ServerRequest {
  public VerifyResult verifyResult;
  public RouterPacket routerPacket;
  public QName action;
  public Packet packet;
  public Element appData;
  public String docRequestCode;
  public List<Enclosure> attachmens;

}
