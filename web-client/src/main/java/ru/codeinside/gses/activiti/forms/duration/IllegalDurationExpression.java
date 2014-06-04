/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. 
 * If a copy of the MPL was not distributed with this file, 
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.forms.duration;

/**
 * Исключение используется парсером выражения при котором задаются длительность периода выполнения задач
 */
public class IllegalDurationExpression extends Exception {
  public IllegalDurationExpression(String message) {
    super(message);
  }
}
