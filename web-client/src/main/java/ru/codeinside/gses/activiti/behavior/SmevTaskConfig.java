/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.behavior;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.impl.bpmn.parser.FieldDeclaration;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    Parser parser = new Parser(fields);
    consumer = parser.parse(REQUIRED, "потребитель", "модуль", "компонент");
    strategy = parser.parse(REQUIRED, "стратегия", "поведение");
    pingCount = parser.parse(OPTIONAL, "количество опросов", "опросов");
    pingInterval = parser.parse(OPTIONAL, "интервал опроса", "задержка опроса");
    retryCount = parser.parse(OPTIONAL, "количество повторов", "повторов");
    retryInterval = parser.parse(OPTIONAL, "интервал опроса", "задержка опроса");
    candidateGroup = parser.parse(OPTIONAL, "исполнители", "группы исполнителей");
    parser.verify();
  }


  final static class Parser {

    final List<FieldDeclaration> fields;
    final Set<String> validNames = new HashSet<String>();

    Parser(List<FieldDeclaration> fields) {
      this.fields = fields;
    }

    private Expression parse(Usage usage, String... names) {
      Set<String> nameSet = unify(Arrays.asList(names));
      validNames.addAll(nameSet);

      Expression value = null;
      for (FieldDeclaration field : fields) {
        if (nameSet.contains(unify(field.getName()))) {
          value = (Expression) field.getValue();
          break;
        }
      }
      if (value == null && usage == REQUIRED) {
        throw new IllegalArgumentException("Пропущено обязательное поле {" + Joiner.on(" | ").join(names) + "}");
      }
      return value;
    }

    private Set<String> unify(Collection<String> names) {
      Set<String> namesSet = new HashSet<String>();
      for (String name : names) {
        namesSet.add(unify(name));
      }
      return namesSet;
    }

    private String unify(String string) {
      return Joiner.on("").join(Splitter.on(' ').omitEmptyStrings().trimResults().split(string.toLowerCase()));
    }

    public void verify() {
      for (FieldDeclaration field : fields) {
        String name = field.getName();
        if (!validNames.contains(unify(name))) {
          throw new IllegalArgumentException("Не известное поле {" + name + "}");
        }
      }
    }
  }
}
