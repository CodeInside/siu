/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.beans.filevalues;

import org.apache.commons.lang.StringUtils;
import org.apache.tika.mime.MimeTypes;
import ru.codeinside.gses.activiti.FileValue;
import ru.codeinside.gws.api.Enclosure;

import java.io.Serializable;

final public class SmevFileValue implements FileValue, Serializable {

  private static final long serialVersionUID = 3228944369313699363L;
  private final Enclosure enclosure;

  public SmevFileValue(Enclosure enclosure) {
    this.enclosure = enclosure;
  }

  @Override
  public String getFileName() {
    return enclosure.fileName;
  }

  @Override
  public String getMimeType() {
    if (StringUtils.isNotEmpty(enclosure.mimeType)) {
      return enclosure.mimeType;
    }
    return new MimeTypes().getMimeType(enclosure.content).getName();
  }

  @Override
  public byte[] getContent() {
    return enclosure.content;
  }

  public Enclosure getEnclosure() {
    return this.enclosure;
  }
}