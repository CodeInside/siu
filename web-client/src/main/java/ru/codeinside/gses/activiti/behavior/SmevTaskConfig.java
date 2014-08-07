/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.behavior;

import org.activiti.engine.delegate.Expression;
import org.activiti.engine.impl.bpmn.parser.FieldDeclaration;

import java.util.List;

import static ru.codeinside.gses.activiti.behavior.SmevTaskConfig.Usage.OPTIONAL;
import static ru.codeinside.gses.activiti.behavior.SmevTaskConfig.Usage.REQUIRED;

public class SmevTaskConfig {

  static enum Usage {
    REQUIRED, OPTIONAL
  }

  final Expression consumer;
  final Expression strategy;
  final Expression pingCount;
  final Expression pingInterval;
  final Expression retryCount;
  final Expression retryInterval;
  final Expression candidateGroup;

  public SmevTaskConfig(List<FieldDeclaration> fields) {
    TaskFields taskFields = new TaskFields(fields);
    consumer = taskFields.parse(REQUIRED, "потребитель", "модуль", "компонент");
    strategy = taskFields.parse(REQUIRED, "стратегия", "поведение");
    pingCount = taskFields.parse(OPTIONAL, "количество опросов", "опросов");
    pingInterval = taskFields.parse(OPTIONAL, "интервал опроса", "задержка опроса");
    retryCount = taskFields.parse(OPTIONAL, "количество повторов", "повторов");
    retryInterval = taskFields.parse(OPTIONAL, "интервал опроса", "задержка опроса");
    candidateGroup = taskFields.parse(OPTIONAL, "исполнители", "группы исполнителей");
    taskFields.verify();
  }


}
