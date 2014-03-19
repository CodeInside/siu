/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.p.router.web.wrap;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.OutputStream;

final class ServletOutputStreamLogger extends ServletOutputStream {

  final OutputStream os;
  final OutputStream dump;

  public ServletOutputStreamLogger(OutputStream os, OutputStream dump) {
    this.os = os;
    this.dump = dump;
  }

  @Override
  public void write(int b) throws IOException {
    os.write(b);
    try {
      dump.write(b);
    } catch (IOException ignore) {
    }
  }

  @Override
  public void write(byte[] b) throws IOException {
    write(b, 0, b.length);
  }

  @Override
  public void write(byte[] b, int off, int len) throws IOException {
    os.write(b, off, len);
    try {
      dump.write(b, off, len);
    } catch (IOException ignore) {
    }
  }

  @Override
  public void flush() throws IOException {
    os.flush();
  }

  @Override
  public void close() throws IOException {
    os.close();
  }
}
