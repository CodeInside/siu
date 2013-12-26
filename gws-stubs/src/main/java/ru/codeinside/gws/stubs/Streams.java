/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.stubs;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

final public class Streams {

  public static void copyAndClose(InputStream in, OutputStream out) throws IOException {
    try {
      copy(in, out);
    } finally {
      close(in);
      close(out);
    }
  }

  public static void copy(InputStream in, OutputStream out) throws IOException {
    byte[] buffer = new byte[8192];
    int count;
    while ((count = in.read(buffer)) > 0)
      out.write(buffer, 0, count);
  }

  public static void close(Closeable... closeables) {
    for (Closeable closeable : closeables) {
      if (closeable != null) {
        try {
          closeable.close();
        } catch (IOException e) {
          // skip
        }
      }
    }
  }

  public static String toString(InputStream in, String encoding) throws IOException {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    copyAndClose(in, bos);
    return bos.toString(encoding);
  }

  public static byte[] toBytes(InputStream in) throws IOException {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    copyAndClose(in, bos);
    return bos.toByteArray();
  }

  private Streams() {

  }
}
