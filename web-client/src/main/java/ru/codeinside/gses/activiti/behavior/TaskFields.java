/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.behavior;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.impl.bpmn.parser.FieldDeclaration;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Functions.compose;
import static com.google.common.base.Predicates.in;
import static com.google.common.base.Predicates.not;
import static com.google.common.base.Predicates.notNull;
import static com.google.common.collect.Collections2.filter;
import static com.google.common.collect.Collections2.transform;
import static com.google.common.collect.ImmutableList.copyOf;
import static com.google.common.collect.Maps.uniqueIndex;
import static ru.codeinside.gses.activiti.behavior.SmevTaskConfig.Usage.REQUIRED;

final class TaskFields {

  final ImmutableMap<String, FieldDeclaration> fields;
  final Set<String> validNames = new HashSet<String>();

  TaskFields(List<FieldDeclaration> fields) {
    this.fields = uniqueIndex(fields, compose(new Unify(), new Names()));
  }

  public Expression parse(SmevTaskConfig.Usage usage, String... names) {
    Collection<String> unifiedNames = transform(copyOf(names), new Unify());
    validNames.addAll(unifiedNames);

    Collection<FieldDeclaration> declarations = Maps.filterKeys(fields, in(unifiedNames)).values();
    if (declarations.size() > 1) {
      throw new IllegalArgumentException(
        "Дублирование поля {" + Joiner.on(" | ").join(transform(declarations, new Names())) + "}"
      );
    }
    Collection<Expression> expressions = filter(transform(declarations, new Values()), notNull());
    if (expressions.isEmpty()) {
      if (usage == REQUIRED) {
        throw new IllegalArgumentException("Пропущено обязательное поле {" + Joiner.on(" | ").join(names) + "}");
      }
      return null;
    }
    return expressions.iterator().next();
  }

  public void verify() {
    Collection<FieldDeclaration> declarations = Maps.filterKeys(fields, not(in(validNames))).values();
    if (!declarations.isEmpty()) {
      throw new IllegalArgumentException(
        "Неизветсные поля {" + Joiner.on(" | ").join(transform(declarations, new Names())) + "}"
      );
    }
  }

  final static class Names implements Function<FieldDeclaration, String> {
    @Override
    public String apply(FieldDeclaration field) {
      return field.getName();
    }
  }

  final static class Values implements Function<FieldDeclaration, Expression> {
    @Override
    public Expression apply(FieldDeclaration field) {
      return (Expression) field.getValue();
    }
  }

  final static class Unify implements Function<String, String> {
    @Override
    public String apply(String string) {
      return Joiner.on("").join(Splitter.on(' ').omitEmptyStrings().trimResults().split(string.toLowerCase()));
    }
  }
}
