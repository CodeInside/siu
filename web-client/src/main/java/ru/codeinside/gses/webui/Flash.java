/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui;

import com.vaadin.Application;
import com.vaadin.event.EventRouter;
import eu.bitwalker.useragentutils.UserAgent;
import ru.codeinside.gses.webui.components.api.Baseband;
import ru.codeinside.log.Actor;

import java.util.EventObject;

/**
 * Весь набор сервисов для текущего запроса.
 */
final public class Flash {

  final private static ThreadLocal<Flasher> flasher = new ThreadLocal<Flasher>();
  final private static ThreadLocal<Application> app = new ThreadLocal<Application>();

  public static Flasher flash() {
    return flasher.get();
  }

  public static String login() {
    return flash().getLogin();
  }

  public static Application app() {
    return app.get();
  }

  public static Actor getActor() {
    final Flasher flash = Flash.flash();
    if (flash == null) {
      return null;
    }
    final String login = flash.getLogin();
    final String ip = flash.getRemoteAddr();
    final String os;
    final String browser;
    final String agentString = flash.getUserAgent();
    if (agentString == null) {
      browser = null;
      os = null;
    } else {
      UserAgent userAgent = UserAgent.parseUserAgentString(agentString);
      browser = userAgent.getBrowser().getName();
      os = userAgent.getOperatingSystem().getName();
    }
    return new Actor(login, ip, os, browser);
  }

  // Динамическая маршрутизация событий >>>

  public static EventRouter router() {
    final Application now = app();
    if (now instanceof Baseband) {
      return ((Baseband) now).getRouter();
    }
    return null;
  }

  public static void fire(EventObject e) {
    final EventRouter router = router();
    if (router != null) {
      router.fireEvent(e);
    }
  }

  public static void bind(Class<?> eventType, Object target, String methodName) {
    final EventRouter router = router();
    if (router != null) {
      router.addListener(eventType, target, methodName);
    }
  }

  public static void unbind(Class<?> eventType, Object target, String methodName) {
    final EventRouter router = router();
    if (router != null) {
      router.removeListener(eventType, target, methodName);
    }
  }

  // кишки регистрации >>>

  static void set(Flasher newFlasher) {
    flasher.set(newFlasher);
  }

  static void set(Application application) {
    app.set(application);
  }

  static void clear() {
    Flasher instance = flasher.get();
    if (instance instanceof Flasher.Closable) {
      ((Flasher.Closable) instance).close();
    }
    flasher.remove();
    app.remove();
  }
}
