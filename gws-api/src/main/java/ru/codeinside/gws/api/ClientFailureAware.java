/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.api;

/**
 * Обработчик сбоя в протоколе потребителя.
 */
public interface ClientFailureAware {
  /**
   * @param context контекст исполнения
   * @param failure причина сбоя
   */
  void processFailure(ExchangeContext context, RuntimeException failure);
}
