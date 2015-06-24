/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.database;

import ru.codeinside.gws.api.InfoSystem;

final public class SmevChain {
  final public boolean hasMessageId;
  final public String originRequestIdRef;
  final public String requestIdRef;
  final public InfoSystem originator;
  final public InfoSystem sender;
  final public InfoSystem recipient;

  public SmevChain(boolean hasMessageId, InfoSystem originator, String originRequestIdRef, InfoSystem sender, String requestIdRef, InfoSystem recipient) {
    this.hasMessageId = hasMessageId;
    this.originRequestIdRef = originRequestIdRef;
    this.requestIdRef = requestIdRef;
    this.originator = originator;
    this.sender = sender;
    this.recipient = recipient;
  }
}
