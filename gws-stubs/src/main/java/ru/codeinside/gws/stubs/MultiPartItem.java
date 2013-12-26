/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.stubs;

import java.util.LinkedHashMap;
import java.util.Map;

final public class MultiPartItem {
  final Map<String, String> headers = new LinkedHashMap<String, String>();
  final byte[] content;

  public MultiPartItem(byte[] content) {
    assert content != null;
    this.content = content;
  }

  public void addHeader(String name, String value) {
    headers.put(name, value);
  }
}
