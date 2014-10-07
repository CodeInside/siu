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
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import commons.Named;
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

final class TaskFields {

  final ImmutableMap<String, FieldDeclaration> fields;
  final Set<String> validNames = new HashSet<String>();

  TaskFields(List<FieldDeclaration> fields) {
    this.fields = uniqueIndex(fields, compose(new Unify(), new Names()));
  }

  public <T extends Enum<T> & Named> Field<T> named(Class<T> type, String name, String... aliases) {
    return next(new EnumFieldType<T>(name, type), aliases);
  }

  public Field<Integer> integer(int defaultValue, String name, String... aliases) {
    return next(new IntegerFieldType(name, defaultValue), aliases);
  }

  public Field<Set<String>> optional(String name, String... aliases) {
    return next(new SetFieldType(name, Usage.OPTIONAL), aliases);
  }

  public Field<String> required(String name, String... aliases) {
    return next(new StringFieldType(name, Usage.REQUIRED), aliases);
  }


  public <T> Field<T> next(FieldType<T> type, String... aliases) {
    Collection<String> unifiedNames = unifyNames(type.getName(), aliases);
    validNames.addAll(unifiedNames);

    Collection<FieldDeclaration> declarations = Maps.filterKeys(fields, in(unifiedNames)).values();
    if (declarations.size() > 1) {
      throw new IllegalArgumentException(
        "Дублирование поля {" + Joiner.on(" | ").join(transform(declarations, new Names())) + "}"
      );
    }
    Collection<Expression> expressions = filter(transform(declarations, new Values()), notNull());
    Expression expression;
    if (expressions.isEmpty()) {
      if (type.getUsage() == Usage.REQUIRED) {
        throw new IllegalArgumentException("Пропущено обязательное поле {" + type.getName() + "}, альтернативные названия {" + Joiner.on(" | ").join(aliases) + "}");
      }
      expression = null;
    } else {
      expression = expressions.iterator().next();
    }
    return type.createField(expression);
  }

  public void verify() {
    Collection<FieldDeclaration> declarations = Maps.filterKeys(fields, not(in(validNames))).values();
    if (!declarations.isEmpty()) {
      throw new IllegalArgumentException(
        "Неизвестные поля {" + Joiner.on(" | ").join(transform(declarations, new Names())) + "}"
      );
    }
  }

  private <T> Collection<String> unifyNames(String name, String[] aliases) {
    ImmutableSet.Builder<String> builder = ImmutableSet.builder();
    builder.add(name);
    builder.addAll(copyOf(aliases));
    return Collections2.transform(builder.build(), new Unify());
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
