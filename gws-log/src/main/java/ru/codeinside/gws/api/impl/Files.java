/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.api.impl;

import java.io.File;

/**
 * Утилитные функции по работе с файлами.
 */
final public class Files {

  /**
   * Создать абсолютный путь к кеш-файлу внутри временного каталога.
   * Тривиальная схема именования prefix-id-file
   */
  static File createCacheFileName(String path, String id, String prefix, String suffix) {
    String fileName = prefix + "-" + suffix.replace(File.separatorChar, '_');
    return new File(getAppTmpDir(path, id), fileName);
  }

  /**
   * Получить и кешировать в сессии временный каталог приложения.
   */
  static File getAppTmpDir(String path, String suf) {
    File tmpDir = new File(path + File.separator + suf);
    tmpDir.mkdirs();
    return tmpDir;
  }
}
