/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.sign;

import java.security.cert.X509Certificate;

final class Cert {

  final String type;
  final String alias;
  final X509Certificate certificate;
  final String name;

  public Cert(String type, String alias, X509Certificate certificate) {
    this.type = type;
    this.alias = alias;
    this.certificate = certificate;
    name = rebuild(certificate.getSubjectDN().getName());
  }

  @Override
  public String toString() {
    return name;
  }

  private String rebuild(final String fullName) {
    final String cn = extract(fullName, "CN=");
    final String o = extract(fullName, "O=");
    final String sn = extract(fullName, "SURNAME=");
    final String gn = extract(fullName, "GIVENNAME=");
    if (cn == null && o == null && sn == null && gn == null) {
      return fullName;
    }
    StringBuilder s = new StringBuilder();
    if (sn != null && gn != null) {
      s.append(sn).append(' ').append(gn);
    } else if (cn != null) {
      s.append(cn);
    }
    if (o != null) {
      if (cn != null || (sn != null && gn != null)) {
        s.append(' ');
      }
      s.append('(').append(o).append(')');
    }
    return s.toString();
  }

  String extract(final String full, final String part) {
    final int index = full.indexOf(part);
    if (index < 0) {
      return null;
    }
    final int first = index + part.length();
    final int last = full.indexOf(", ", first);
    if (last < 0) {
      return full.substring(first);
    }
    return full.substring(first, last);
  }
}
