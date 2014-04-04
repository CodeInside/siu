/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.service;

import com.google.common.base.Function;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import org.activiti.engine.ProcessEngine;

import java.util.ArrayList;
import java.util.List;

import static ru.codeinside.gses.webui.Flash.flash;

final public class Fn {

  private Fn() {

  }

  public static <T> T withEngine(final Function<ProcessEngine, T> f0) {
    return activitiService().withEngine(f0);
  }

  public static <R> R withEngine(final F0<R> f0) {
    return activitiService().withEngine(f0);
  }

  public static <R, T> R withEngine(final F1<R, T> f1, final T arg) {
    return activitiService().withEngine(f1, arg);
  }

  public static <R, A1, A2> R withEngine(final F2<R, A1, A2> f2, final A1 arg1, final A2 arg2) {
    return activitiService().withEngine(f2, arg1, arg2);
  }

  public static <R, A1, A2, A3> R withEngine(final F3<R, A1, A2, A3> f3, final A1 arg1, final A2 arg2, final A3 arg3) {
    return activitiService().withEngine(f3, arg1, arg2, arg3);
  }

  public static boolean isEqual(Object a, Object b) {
    return a == b || (a != null && a.equals(b));
  }

  public static String trimToNull(String text) {
    if (text != null) {
      text = text.trim();
      if (text.isEmpty()) {
        text = null;
      }
    }
    return text;
  }

  public static <T> T getValue(final Item item, final String id, final Class<T> check) {
    if (item == null) {
      return null;
    }
    final Property property = item.getItemProperty(id);
    if (property == null) {
      return null;
    }
    return check.cast(property.getValue());
  }

  public static <T extends Throwable> T trim(final T throwable) {
    final StackTraceElement[] stack = throwable.getStackTrace();
    final List<StackTraceElement> trimmed = new ArrayList<StackTraceElement>(stack.length);
    for (int i = 0; i < stack.length; i++) {
      final StackTraceElement e = stack[i];
      trimmed.add(e);
      if (e.getClassName().startsWith("ru.codeinside.")) {
        break;
      }
    }
    throwable.setStackTrace(trimmed.toArray(new StackTraceElement[trimmed.size()]));
    return throwable;
  }

  private static ActivitiService activitiService() {
    return flash().getActivitiService();
  }

}
