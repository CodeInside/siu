/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.ftarchive;

import commons.Streams;
import ru.codeinside.gses.activiti.FileValue;

import java.io.File;
import java.io.IOException;

final public class TmpFileValue implements FileValue {

  final String name;
  final String type;
  final File tmpFile;


  TmpFileValue(String name, String type, File tmpFile) {
    this.name = name;
    this.type = type;
    this.tmpFile = tmpFile;
  }

  @Override
  public String getFileName() {
    return name;
  }

  @Override
  public String getMimeType() {
    return type;
  }


  @Override
  public byte[] getContent() {
    try {
      return Streams.toBytes(tmpFile);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String toString() {
    return name;
  }

  @Override
  protected void finalize() throws Throwable {
    tmpFile.delete();
    super.finalize();
  }
}
