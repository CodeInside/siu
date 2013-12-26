/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.sign;

import javax.xml.bind.DatatypeConverter;
import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.logging.LogManager;

final public class SignApplet extends Applet {

  private Signer dispatch;

  @Override
  public void init() {

    try {
      LogManager.getLogManager().readConfiguration(getClass().getResourceAsStream("/logging.properties"));
    } catch (IOException e) {
      // skip;
    }

    AccessController.doPrivileged(new CheckServiceAction(
      "javax.xml.datatype.DatatypeFactory",
      "com.sun.org.apache.xerces.internal.jaxp.datatype.DatatypeFactoryImpl"
    ));

    AccessController.doPrivileged(new CheckServiceAction(
      "javax.xml.parsers.DocumentBuilderFactory",
      "com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl"
    ));

    AccessController.doPrivileged(new CheckServiceAction(
      "javax.xml.transform.TransformerFactory",
      "com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl"
    ));


    boolean debug = Boolean.valueOf(getParameter("appDebug"));
    String pid = getParameter("paintableId");
    String mode = getParameter("mode");

    setLayout(new BorderLayout(2, 2));

    Vaadin vaadin = new JsVaadin(debug, this, pid);
    CertConsumer consumer;
    if ("binding".equalsIgnoreCase(mode)) {
      consumer = new Binder(vaadin, this, getParameter("fio"));
    } else if ("rebind".equalsIgnoreCase(mode)) {
      byte[] x509 = DatatypeConverter.parseBase64Binary(getParameter("x509"));
      consumer = new Rebinder(vaadin, this, x509, getParameter("fio"));
    } else {
      byte[] x509 = DatatypeConverter.parseBase64Binary(getParameter("x509"));
      dispatch = new Signer(vaadin, this, x509);
      consumer = dispatch;
    }
    EventQueue.invokeLater(new CertLoading(consumer));
  }

  @SuppressWarnings("unused") // JS API
  public void execute(final String command) {
    EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        doExecute(command, new String[0]);
      }
    });
  }

  @SuppressWarnings("unused") // JS API
  public void execute(final String command, final String[] params) {
    EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        doExecute(command, params);
      }
    });
  }

  void doExecute(String command, String[] params) {
    if ("close".equals(command)) {
      removeAll();
      invalidate();
      repaint();
    } else if ("block".equals(command)) {
      int current = Integer.parseInt(params[0]);
      int total = Integer.parseInt(params[1]);
      dispatch.block(current, total);
    } else if ("chunk".equals(command)) {
      int current = Integer.parseInt(params[0]);
      int total = Integer.parseInt(params[1]);
      byte[] bytes = DatatypeConverter.parseBase64Binary(params[2]);
      dispatch.chunk(current, total, bytes);
    }
  }

  static class CheckServiceAction implements PrivilegedAction<Object> {
    private final String cfg;
    private final String test;

    public CheckServiceAction(String cfg, String test) {
      this.cfg = cfg;
      this.test = test;
    }

    @Override
    public Object run() {
      try {
        Class.forName(test, false, SignApplet.class.getClassLoader());
        System.setProperty(cfg, test);
      } catch (Exception e) {

      }
      return null;
    }
  }
}