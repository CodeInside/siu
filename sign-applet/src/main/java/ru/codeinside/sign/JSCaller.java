/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.sign;

import netscape.javascript.JSException;
import netscape.javascript.JSObject;
import netscape.javascript.JSUtil;

import java.applet.Applet;

final class JSCaller extends Thread {

  final boolean debug;
  final String method;
  final Applet applet;
  final String paintableId;
  final String name;
  final Object value;

  private void debug(Object... args) {
    if (debug) {
      System.err.print("debug:");
      for (Object o : args) {
        if (o instanceof JSException) {
          JSException jsException = (JSException) o;
          System.err.print(" js: " + jsException.getMessage() + " ");
          System.err.print(JSUtil.getStackTrace(jsException));
        } else if (o instanceof Throwable) {
          Throwable throwable = (Throwable) o;
          System.err.print(" error: " + throwable.getMessage());
          throwable.printStackTrace();
        } else {
          System.err.print(" " + o);
        }
      }
      System.err.println();
    }
  }

  JSCaller(boolean debug, Applet applet, String method, String paintableId, String name, Object value) {
    this.debug = debug;
    this.applet = applet;
    this.method = method;
    this.paintableId = paintableId;
    this.name = name;
    this.value = value;
  }

  @Override
  public void run() {
    JSObject vaadin;
    try {
      vaadin = (JSObject) JSObject.getWindow(applet).getMember("vaadin");
    } catch (JSException e) {
      debug(name, null == e.getMessage() ? "vaadin not found" : "", e);
      return;
    }

    try {
      vaadin.call(method, new Object[]{paintableId, name, value, true});
    } catch (Exception e) {
      debug("direct_fail", e);
      String command;
      if (value != null && value instanceof String) {
        command = "vaadin." + method + "('" + paintableId + "', '" + name + "', '" + value + "', true)";
      } else {
        command = "vaadin." + method + "('" + paintableId + "', '" + name + "', " + value + ", true)";
      }
      vaadin.eval(command);
    }
  }
}

