/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.api.impl;

import java.io.*;

/**
 * Утилитные функции для потоков.
 */
final public class Streams {

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

  public static void copy(InputStream in, OutputStream out) throws IOException {
    byte[] buffer = new byte[8192];
    int count;
    while ((count = in.read(buffer)) > 0)
      out.write(buffer, 0, count);
  }

  public static String toString(InputStream in, String encoding) throws IOException {
    try {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      Streams.copy(in, bos);
      return bos.toString(encoding);
    } finally {
      close(in);
    }
  }

  public static String toString(InputStream in) throws IOException {
    return toString(in, "UTF-8");
  }


}
