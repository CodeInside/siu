/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package commons;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

final public class Streams {

  private static File TMP_FILES_DIR = null;

  static public void init(File tmpDir) {
    if (TMP_FILES_DIR == null) {
      if (tmpDir == null) {
        tmpDir = new File(System.getProperty("java.io.tmpdir"));
      }
      TMP_FILES_DIR = new File(tmpDir, "tmp-files");
      if (!TMP_FILES_DIR.exists()) {
        if (!TMP_FILES_DIR.mkdir()) {
          throw new IllegalStateException("can't create " + TMP_FILES_DIR);
        }
      }
      Logger.getLogger(Streams.class.getName()).info("Use '" + TMP_FILES_DIR + "' as tmpDir");
      String[] files = TMP_FILES_DIR.list();
      if (files != null) {
        for (String file : files) {
          new File(file).delete();
        }
      }
    }
  }

  public static File createTempFile(String prefix, String suffix) throws IOException {
    return File.createTempFile(prefix, suffix, TMP_FILES_DIR);
  }

  private Streams() {

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

  public static File copyToTempFile(InputStream source, String prefix, String suffix) throws IOException {
    try {
      File dst = createTempFile(prefix, suffix);
      FileOutputStream fos = null;
      try {
        fos = new FileOutputStream(dst);
        copy(source, fos);
      } finally {
        close(fos);
      }
      return dst;
    } finally {
      close(source);
    }
  }


  public static File copyToTempFile(File src, String prefix, String suffix) throws IOException {
    File dst = createTempFile(prefix, suffix);
    FileOutputStream fos = null;
    FileInputStream fis = null;
    try {
      fos = new FileOutputStream(dst);
      fis = new FileInputStream(src);
      copy(fis, fos);
    } finally {
      close(fos, fis);
    }
    return dst;
  }

  public static byte[] toBytes(final File file) throws IOException {
    final byte[] bytes = new byte[(int) file.length()];
    int offset = 0;
    int numRead;
    FileInputStream fis = null;
    try {
      fis = new FileInputStream(file);
      while (offset < bytes.length && (numRead = fis.read(bytes, offset, bytes.length - offset)) >= 0) {
        offset += numRead;
      }
      if (offset < bytes.length) {
        throw new IOException("Файл получен не полностью!");
      }
    } finally {
      close(fis);
    }
    return bytes;
  }

}
