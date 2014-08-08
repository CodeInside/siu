/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.database;

import ru.codeinside.gws.api.InfoSystem;

final public class SmevChain {
  final public String requestIdRef;
  final public String originRequestIdRef;
  final public InfoSystem origin;
  final public InfoSystem sender;

  public SmevChain(InfoSystem origin, String originRequestIdRef, InfoSystem sender, String requestIdRef) {
    this.requestIdRef = requestIdRef;
    this.originRequestIdRef = originRequestIdRef;
    this.origin = origin;
    this.sender = sender;
  }
}
