/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.forms.api.definitions;

import com.google.common.collect.ImmutableMap;
import ru.codeinside.gses.activiti.forms.api.duration.DurationPreference;

/**
 * Описатель дерева свойств формы.
 * Коллекция корневых элементов.
 */
public interface PropertyTree extends PropertyCollection {

  /**
   * Все элементы дерева.
   */
  ImmutableMap<String, PropertyNode> getIndex();

  DurationPreference getDurationPreference();

  String getFormKey();

  boolean isSignatureRequired();

  boolean isDataFlow();

  String getConsumerName();

  boolean needSp();

  boolean needOv();

  boolean needTep();

  boolean needSend();

  boolean isLazyWriter();

  boolean isAppDataSignatureBlockLast();

  boolean isResultDataFlow();

  String getRequestType();

  String getResponseMessage();

  boolean resultNeedSp();

  boolean resultNeedOv();
}
