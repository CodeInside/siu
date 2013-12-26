/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.form;

import commons.Streams;
import ru.codeinside.gses.activiti.FileValue;

import java.io.File;
import java.io.IOException;

final class EFileValue implements FileValue {
  final String name;
  final String mime;
  final File file;

  public EFileValue(String name, String mime, File file) {
    this.name = name;
    this.mime = mime;
    this.file = file;
  }

  @Override
  public String getFileName() {
    return name;
  }

  @Override
  public String getMimeType() {
    return mime;
  }

  @Override
  public byte[] getContent() {
    try {
      return Streams.toBytes(file);
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  public String toString() {
    return name;
  }
}
