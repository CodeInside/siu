/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.api.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * Утилитные функции по работе с файлами.
 */
final public class Files {
  /**
   * Создать или использовать содержимое из временного файла.
   * Если файл не существует - создать и заполнить.
   */
  public static File cacheContentToFile(final String path,  final String id, final byte[] bytes, final String fileName) {
    final File file = createTmpCacheFileName(spool(), id, "log", fileName);
    if (!file.exists()) {
      FileOutputStream fos = null;
      try {
        fos = new FileOutputStream(file);
        if (bytes != null && bytes.length > 0) {
          fos.write(bytes);
        }
      } catch (IOException e) {
        throw new RuntimeException(e);
      } finally {
        Streams.close(fos);
        file.renameTo(createCacheFileName(path, id, "log", fileName));
      }
    }
    return file;
  }

  /**
   * Создать абсолютный путь к временному кеш-файлу внутри временного каталога.
   * Тривиальная схема именования prefix-id-file
   */
  static File createTmpCacheFileName(final String path, final String id, final String prefix, final String fileName) {
    return new File(getAppTmpDir(path, "spool"), id + "-" + prefix + "-" + fileName.replace('/',
      '_').replace('\\', '_'));
  }

  /**
   * Создать абсолютный путь к кеш-файлу внутри временного каталога.
   * Тривиальная схема именования prefix-id-file
   */
  static File createCacheFileName(final String path, final String id, final String prefix, final String fileName) {
    // убрать символы каталога
    return new File(getAppTmpDir(path, id), prefix + "-" + fileName.replace('/', '_').replace('\\', '_'));
  }

  /**
   * Получить и кешировать в сессии временный каталог приложения.
   */
  static File getAppTmpDir(final String path, String suf) {
    File tmpDir = new File(path + File.separator + suf);
    tmpDir.mkdirs();
    return tmpDir;
  }

  static String spool(){
    return LogSettings.getPath(true);
  }
}
