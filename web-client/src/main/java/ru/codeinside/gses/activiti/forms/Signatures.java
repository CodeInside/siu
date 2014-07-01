/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.forms;

import java.io.Serializable;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;

final public class Signatures implements Serializable {

  private static final long serialVersionUID = 1L;

  public final FormID formID;
  public final byte[] certificate;
  public final String[] propertyIds;
  public final boolean[] files;
  public final byte[][] signs;

  public Signatures(FormID formID, X509Certificate certificate, String[] propertyIds, boolean[] files, byte[][] signs) {
    try {
      this.certificate = certificate.getEncoded();
    } catch (CertificateEncodingException e) {
      throw new RuntimeException(e);
    }
    this.formID = formID;
    this.propertyIds = propertyIds;
    this.files = files;
    this.signs = signs;
  }

  public int findSign(String id) {
    for (int i = 0; i < propertyIds.length; i++) {
      if (id.equals(propertyIds[i])) {
        return i;
      }
    }
    return -1;
  }
}
