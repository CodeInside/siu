/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package eform;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

final public class Property implements Serializable {

  public String label;
  public String type;
  public String value;
  public boolean required;
  public boolean writable;
  public boolean sign;
  public String certificate;
  public List<Map<String, Property>> children;

  File content;
  String mime;
  boolean modified;

  public void updateValue(String value) {
    this.value = value;
    modified = true;
  }

  public void updateContent(String fileName, String mime, File content, boolean modified) {
    if (modified) {
      cleanContent();
    }
    value = fileName;
    this.mime = mime;
    this.content = content;
    this.modified = modified;
  }

  public Object[] content() {
    if (content == null) {
      return null;
    }
    return new Object[]{content, mime};
  }

  public boolean attachment() {
    return "attachment".equals(type);
  }

  public String freshValue() {
    if (modified) {
      return value;
    }
    return null;
  }

  @Override
  protected void finalize() throws Throwable {
    cleanContent();
    super.finalize();
  }

  /**
   * подчистим файл за собой.
   */
  private void cleanContent() {
    if (content != null) {
      content.delete();
      content = null;
    }
  }
}
