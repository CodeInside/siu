/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.forms;

import ru.codeinside.gses.activiti.FileValue;
import ru.codeinside.gses.webui.Flash;

final public class FileBufferValue implements FileValue {

  private final String fileName;
  private final Integer contentId;
  private String mimeType;

  public FileBufferValue(String fileName, String mimeType, Integer contentId) {
    this.fileName = fileName;
    this.mimeType = mimeType;
    this.contentId = contentId;
  }

  @Override
  public String getFileName() {
    return fileName;
  }

  @Override
  public String getMimeType() {
    return mimeType;
  }

  @Override
  public byte[] getContent() {
    if (contentId == null) {
      return new byte[0];
    }
    return Flash.flash().getExecutorService().getBytes(contentId);
  }

  @Override
  public String toString() {
    return fileName;
  }
}
