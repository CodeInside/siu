/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.api;

final public class Enclosure {

  /**
   * Имя файла документа
   */
  public String fileName;

  /**
   * содержимое
   */
  public byte[] content;

  /**
   * Идентификатор документа в описателе.
   */
  public String id;

  /**
   * Код документа
   */
  public String code;

  /**
   * Номер документа
   */
  public String number;

  /**
   * Относительный путь к файлу внутри архива
   */
  public String zipPath;

  /**
   * Тип содержимого(например application/pdf)
   */
  public String mimeType;

  /**
   * Хеш-код вложения ГОСТ 34.11-94.
   */
  public byte[] digest;

    /**
     * Подпись
     */
    public Signature signature;

  public Enclosure(final String zipPath, byte[] content) {
    if (zipPath == null || content == null) {
      throw new NullPointerException();
    }
    this.zipPath = zipPath;
    this.content = content;
  }

  public Enclosure(final String zipPath, final String name, byte[] content) {
    this(zipPath, content);
    this.fileName = name;
  }

  @Override
  public String toString() {
    return "{" +
      (fileName == null ? "" : "fileName='" + fileName + '\'') +
      ", content[]=" + content.length +
      (id == null ? "" : (", id='" + id + '\'')) +
      (code == null ? "" : (", code='" + code + '\'')) +
      (number == null ? "" : (", number='" + number + '\'')) +
      ", zipPath='" + zipPath + '\'' +
      (mimeType == null ? "" : (", mimeType='" + mimeType + '\'')) +
      (digest == null ? "" : (", digest[]=" + digest.length)) +
      (signature == null ? "" : (", signature[]=" + signature)) +
      '}';
  }
}