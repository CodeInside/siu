/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.service;

/**
 * Идентификация заявки.
 *
 * @author xeodon.
 * @since 1.0.6
 */
final public class BidID {

  /**
   * Идентификатор заявления.
   */
  final public long bidId;

  /**
   * Идентификатор процесса обработки заявления.
   */
  final public long processId;


  public BidID(long bidId, long processId) {
    this.bidId = bidId;
    this.processId = processId;
  }
}
