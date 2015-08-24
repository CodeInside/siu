/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.sign;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.PrivateKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Set;

import static javax.xml.bind.DatatypeConverter.printBase64Binary;

final class Binder implements CertConsumer {

  final Vaadin vaadin;
  final Panel ui;
  final Filter filter = new AcceptAll();
  final String fio;
  final String organization;
  final int maxAttempts;
  final Set<Long> lockedCerts;

  Binder(Vaadin vaadin, Panel ui, String fio, String organization, int maxAttempts, Set<Long> lockedCerts) {
    this.vaadin = vaadin;
    this.ui = ui;
    this.fio = fio;
    this.organization = organization;
    this.maxAttempts = maxAttempts;
    this.lockedCerts = lockedCerts;
  }

  public void ready(String name, PrivateKey unusedPrivateKey, X509Certificate certificate) {
    String status;
    try {
      vaadin.updateVariable("cert", printBase64Binary(certificate.getEncoded()));
      status = "Готово.";
    } catch (CertificateEncodingException e) {
      status = "Ошибка.";
    }

    ui.removeAll();
    ui.add(new Label(name), BorderLayout.PAGE_START);
    ui.add(new Label(status), BorderLayout.CENTER);

    Button cancel = new Button("Отменить");
    cancel.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        loading();
      }
    });
    Panel buttons = new Panel(new BorderLayout(2, 2));
    buttons.add(cancel, BorderLayout.LINE_START);
    ui.add(buttons, BorderLayout.PAGE_END);
    refresh();
  }

  @Override
  public void wrongPassword(long certSerialNumber) {
    vaadin.updateVariable("wrongPassword", String.valueOf(certSerialNumber));
    refresh();
  }

  @Override
  public void loading() {
    vaadin.updateVariable("state", "loading");

    ui.removeAll();
    ui.add(new Label("Загрузка сертификатов..."), BorderLayout.LINE_START);
    Label status = new Label("");
    ui.add(status, BorderLayout.CENTER);
    refresh();
    new Thread(new CertDetector(this, ui, status, fio, organization, maxAttempts, lockedCerts)).start();
  }

  public void refresh() {
    ui.validate();
    ui.repaint();
  }

  @Override
  public void noJcp() {
    vaadin.updateVariable("state", "noJcp");

    ui.removeAll();
    ui.add(new Label("КриптоПРО JCP не установлен!"), BorderLayout.LINE_START);
    refresh();
  }

  @Override
  public Filter getFilter() {
    return filter;
  }

  @Override
  public String getActionText() {
    return "Завершить выбор";
  }

  @Override
  public String getSelectionLabel() {
    return "Выберите сертификат:";
  }

}
