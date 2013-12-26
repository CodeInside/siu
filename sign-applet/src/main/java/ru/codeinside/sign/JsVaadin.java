/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.sign;

import java.applet.Applet;

final class JsVaadin implements Vaadin {

  final private static long MAX_JS_WAIT_TIME = 10000;

  final boolean debug;
  final Applet applet;
  final String paintableId;

  JsVaadin(boolean debug, Applet applet, String paintableId) {
    this.debug = debug;
    this.applet = applet;
    this.paintableId = paintableId;
  }

  //public void updateVariable(String name, boolean value) {
  //  jsCall("appletUpdateBooleanVariable", name, value);
  //}

  public void updateVariable(String name, int value) {
    jsCall("appletUpdateIntVariable", name, value);
  }

  //public void updateVariable(String name, double value) {
  //  jsCall("appletUpdateDoubleVariable", name, value);
  //}

  public void updateVariable(String name, String value) {
    jsCall("appletUpdateStringVariable", name, value);
  }


  private void jsCall(String method, String name, Object value) {
    try {
      jsCallSync(method, name, value);
    } catch (InterruptedException e) {
      throw new RuntimeException("Timed out", e);
    }
  }


  private void jsCallSync(String method, String name, Object value) throws InterruptedException {
    JSCaller t = new JSCaller(debug, applet, method, paintableId, name, value);
    t.start();
    t.join(MAX_JS_WAIT_TIME);
  }

}
