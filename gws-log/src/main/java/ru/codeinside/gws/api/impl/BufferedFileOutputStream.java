/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */
package ru.codeinside.gws.api.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * @author xeodon
 * @since 1.0.2
 */
final class BufferedFileOutputStream extends BufferedOutputStream {
  public BufferedFileOutputStream(File file) throws FileNotFoundException {
    super(new FileOutputStream(file), 16 * 1024);
  }
}
