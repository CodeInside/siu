/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.cert;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.util.List;

final public class NameParts {


  public String getCommonName() {
    return getPart("CN");
  }

  public String getOrganization() {
    return getPart("O");
  }

  public String getGivenName() {
    return getPart("GIVENNAME");
  }

  public String getSurName() {
    return getPart("SURNAME");
  }

  public String getPart(String key) {
    return parts.get(key);
  }

  public String getShortName() {
    final String commonName = getCommonName();
    final String organization = getOrganization();
    final String surName = getSurName();
    final String givenName = getGivenName();
    if (commonName == null && organization == null && surName == null && givenName == null) {
      return original;
    }
    final StringBuilder sb = new StringBuilder();
    if (surName != null && givenName != null) {
      sb.append(surName).append(' ').append(givenName);
    } else if (commonName != null) {
      sb.append(commonName);
    }
    if (organization != null) {
      if (commonName != null || (surName != null && givenName != null)) {
        sb.append(' ');
      }
      sb.append('(').append(organization).append(')');
    }
    return sb.toString();
  }

  public NameParts(final String dn) {
    final ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
    for (final String chunk : Splitter.on(',').trimResults().split(dn)) {
      List<String> pairs = ImmutableList.copyOf(Splitter.on('=').trimResults().split(chunk));
      if (pairs.size() == 2) {
        builder.put(pairs.get(0), pairs.get(1));
      }
    }
    original = dn;
    parts = builder.build();
  }

  final private ImmutableMap<String, String> parts;
  final private String original;

}
