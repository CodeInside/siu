/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.p.router.web.wrap;

import javax.servlet.ServletInputStream;
import java.io.IOException;
import java.io.OutputStream;

final class ServletInputStreamLogger extends ServletInputStream {

  final ServletInputStream in;
  final OutputStream dump;

  public ServletInputStreamLogger(ServletInputStream in, OutputStream dump) {
    this.in = in;
    this.dump = dump;
  }

  @Override
  public int read() throws IOException {
    int b = in.read();
    if (b >= 0) {
      try {
        dump.write(b);
      } catch (IOException ignore) {
      }
    }
    return b;
  }

  @Override
  public int read(byte[] buffer) throws IOException {
    return read(buffer, 0, buffer.length);
  }

  @Override
  public int read(byte[] buffer, int off, int len) throws IOException {
    int n = in.read(buffer, off, len);
    if (n > 0) {
      try {
        dump.write(buffer, off, n);
      } catch (IOException ignore) {
      }
    }
    return n;
  }

  @Override
  public long skip(long n) throws IOException {
    return in.skip(n);
  }

  @Override
  public int available() throws IOException {
    return in.available();
  }

  @Override
  public void close() throws IOException {
    in.close();
  }

  @Override
  public synchronized void mark(int readlimit) {
    in.mark(readlimit);
  }

  @Override
  public synchronized void reset() throws IOException {
    in.reset();
  }

  @Override
  public boolean markSupported() {
    return in.markSupported();
  }
}
