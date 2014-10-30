/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package commons;

import org.activiti.engine.ActivitiException;
import ru.codeinside.gws.api.ServerException;

import javax.ejb.EJBException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;

/**
 * Функции над исключительными ситуациями
 */
final public class Exceptions {

  /**
   * Преобразование исключения вместе со стеком в строку.
   */
  public static String toString(Throwable e) {
    StringWriter sw = new StringWriter();
    e.printStackTrace(new PrintWriter(sw));
    return sw.toString();
  }

  /**
   * Уменьшение стека до пакета "ru.codeinside." исключительно
   */
  public static <T extends Throwable> T trim(final T throwable) {
    return trim(throwable, false);
  }

  /**
   * Уменьшение стека до пакета "ru.codeinside."
   */
  public static <T extends Throwable> T trim(final T throwable, boolean including) {
    return trimStackTo(throwable, "ru.codeinside.", including);
  }

  /**
   * Уменьшить стек и преобразовать в строку.
   */
  public static String trimToString(Throwable e) {
    return toString(trim(e));
  }

  /**
   * Уменьшить до первопричины и преобразовать в строку.
   */
  public static String trimToCauseString(Throwable throwable) {
    while (throwable.getCause() != null) {
      throwable = throwable.getCause();
    }
    return trimToString(throwable);
  }

  /**
   * Получить причину из обёрток и заменить на ServerException.
   */
  public static ServerException convertToApi(EJBException e) {
    Throwable reason = e.getCause();
    if (reason == null) {
      reason = e;
    } else if (reason instanceof ActivitiException) {
      if (reason.getCause() != null) {
        reason = reason.getCause();
      }
    }
    ServerException wrapper = new ServerException(reason.toString());
    wrapper.setStackTrace(reason.getStackTrace());
    return trim(wrapper, true);
  }

  /**
   * Уменьшение стека до пакета {@code stopPackage}.
   *
   * @param throwable   исключение
   * @param stopPackage начло имени пакета
   * @param including   включительно до найденного пакета
   */
  private static <T extends Throwable> T trimStackTo(T throwable, String stopPackage, boolean including) {
    StackTraceElement[] stackElements = throwable.getStackTrace();
    int length = 0;
    for (StackTraceElement element : stackElements) {
      if (element.getClassName().startsWith(stopPackage)) {
        if (including) {
          length++;
        }
        break;
      }
      length++;
    }
    if (length == 0) {
      throwable.setStackTrace(new StackTraceElement[0]);
    } else if (length != stackElements.length) {
      throwable.setStackTrace(Arrays.copyOf(stackElements, length));
    }
    return throwable;
  }

  private Exceptions() {

  }
}
