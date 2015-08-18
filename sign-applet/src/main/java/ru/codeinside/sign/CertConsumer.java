/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.sign;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;

interface CertConsumer {

  void ready(final String name, PrivateKey privateKey, X509Certificate certificate);

  void wrongPassword(long certSerialNumber);

  void refresh();

  void loading();

  void noJcp();

  Filter getFilter();

  String getActionText();

  String getSelectionLabel();
}
