/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.ftarchive;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import ru.codeinside.gses.vaadin.FieldFormType;

public abstract class TypedFieldFormType<T> implements FieldFormType {

  @Override
  final public String convertModelValueToFormValue(Object value) {
    final Class<T> type = getType();
    System.out.println("converted: " + (value == null ? "null" : value.getClass()) + " as " + type);
    return toForm(type.cast(value));
  }

  @Override
  final public Object convertFormValueToModelValue(String value) {
    return toModel(value);
  }

  protected abstract String toForm(T value);

  protected abstract T toModel(String value);

  /**
   * Получить актуальный параметризованный тип.
   */
  @SuppressWarnings("unchecked")
  private Class<T> getType() {
    final Type generic = getClass().getGenericSuperclass();
    if (generic instanceof Class) {
      return (Class<T>) generic;
    }
    if (generic instanceof ParameterizedType) {
      final ParameterizedType type = (ParameterizedType) generic;
      return (Class<T>) type.getActualTypeArguments()[0];
    }
    throw new IllegalStateException("Неизвестный тип " + generic);
  }
  
}
