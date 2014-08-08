/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.behavior;

import org.activiti.engine.impl.bpmn.parser.FieldDeclaration;
import ru.codeinside.adm.database.SmevTaskStrategy;

import java.util.List;

public class SmevTaskConfig {

  final Field<String> consumer;
  final Field<SmevTaskStrategy> strategy;
  final Field<Integer> pingCount;
  final Field<Integer> pingInterval;
  final Field<Integer> retryCount;
  final Field<Integer> retryInterval;
  final Field<String> candidateGroup;

  public SmevTaskConfig(List<FieldDeclaration> fields) {
    TaskFields taskFields = new TaskFields(fields);
    consumer = taskFields.required("потребитель", "модуль", "компонент");
    strategy = taskFields.named(SmevTaskStrategy.class, "стратегия", "поведение");
    pingCount = taskFields.integer(10, "количество опросов", "опросов");
    pingInterval = taskFields.integer(60, "интервал опроса", "задержка опроса");
    retryCount = taskFields.integer(5, "количество повторов", "повторов");
    retryInterval = taskFields.integer(600, "интервал опроса", "задержка опроса");
    candidateGroup = taskFields.optional("исполнители", "группы исполнителей");
    taskFields.verify();
  }

}
