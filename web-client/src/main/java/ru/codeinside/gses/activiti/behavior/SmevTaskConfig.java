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

public class SmevTaskConfig {

  final Expression consumer;
  final Expression pingCount;
  final Expression pingIntervalSec;
  final Expression retryCount;
  final Expression retryIntervalSec;
  final Expression candidateGroup;

  public SmevTaskConfig(List<FieldDeclaration> fieldDeclarations) {

    Expression pingCount = null;
    Expression retryCount = null;
    Expression pingIntervalSec = null;
    Expression retryIntervalSec = null;
    Expression candidateGroup = null;
    Expression consumer = null;

    for (FieldDeclaration fieldDeclaration : fieldDeclarations) {
      String name = fieldDeclaration.getName();
      Expression expression = (Expression) fieldDeclaration.getValue();
      if ("потребитель".equalsIgnoreCase(name)) {
        consumer = expression;
      } else if ("интервалОпроса".equalsIgnoreCase(name)) {
        pingIntervalSec = expression;
      } else if ("интервалПовторов".equalsIgnoreCase(name)) {
        retryIntervalSec = expression;
      } else if ("повторов".equalsIgnoreCase(name)) {
        retryCount = expression;
      } else if ("опросов".equalsIgnoreCase(name)) {
        pingCount = expression;
      } else if ("исполнители".equalsIgnoreCase(name)) {
        candidateGroup = expression;
      }
    }

    if (consumer == null) {
      throw new IllegalArgumentException("поле 'исполнитель' обязательно");
    }

    this.pingCount = pingCount;
    this.retryCount = retryCount;
    this.pingIntervalSec = pingIntervalSec;
    this.retryIntervalSec = retryIntervalSec;
    this.candidateGroup = candidateGroup;
    this.consumer = consumer;
  }

}
