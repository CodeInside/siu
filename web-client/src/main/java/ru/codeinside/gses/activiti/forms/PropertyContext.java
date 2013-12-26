/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.forms;

/**
 * Контекст обрабатываемого в текущем потокое свойства формы.
 * Необходимо для упрощения связывания с переменной, которую изменяет свойство,
 * так как методы связывания довольно сложны.
 */
final public class PropertyContext {

  public interface Action {
    void inContext();
  }

  public static String getWritePath() {
    return CURRENT_PATH.get();
  }

  public static void withWritePath(final String path, final Action action) {
    assert path != null;
    assert action != null;

    CURRENT_PATH.set(path);
    try {
      action.inContext();
    } finally {
      CURRENT_PATH.remove();
    }
  }

  final private static ThreadLocal<String> CURRENT_PATH = new ThreadLocal<String>();

  private PropertyContext() {

  }
}
