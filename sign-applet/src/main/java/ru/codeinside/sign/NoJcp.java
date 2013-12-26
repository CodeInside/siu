/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.sign;

final class NoJcp implements Runnable {

  final private CertConsumer consumer;

  NoJcp(CertConsumer consumer) {
    this.consumer = consumer;
  }

  @Override
  public void run() {
    consumer.noJcp();
  }
}
