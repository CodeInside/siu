/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.api;

/**
 * Каноническая форма данных, подготовленная для добавления ЭЦП физического лица.
 * <p/>
 * Про канонизацию XML: http://www.iso.ru/print/rus/document5910.phtml
 */
@Deprecated
final public class AppData {

  /**
   * Содержимое по "http://www.w3.org/2001/10/xml-exc-c14n#".
   */
  final public byte[] content;

  /**
   * Дайджет по "http://www.w3.org/2001/04/xmldsig-more#gostr3411".
   */
  final public byte[] digest;

  public AppData(byte[] content, byte[] digest) {
    this.content = content;
    this.digest = digest;
  }
}
