/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.api;

/**
 * Исключительная ситуация на сервере во время обработки заявления.
 *
 * @author xeodon
 * @since 1.0.8
 */
public class ServerException extends RuntimeException {

  public ServerException(String message) {
    super(message);
  }

}
