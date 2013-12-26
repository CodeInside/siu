/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.components.sign;

import java.io.Serializable;
import java.security.cert.X509Certificate;

public interface SignAppletListener extends Serializable {

  void onLoading(SignApplet signApplet);

  void onNoJcp(SignApplet signApplet);

  void onCert(SignApplet signApplet, X509Certificate certificate);

  void onBlockAck(SignApplet signApplet, int i);

  void onChunkAck(SignApplet signApplet, int i);

  void onSign(SignApplet signApplet, byte[] sign);

}
