/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.sign;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.PrivateKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Set;

import static javax.xml.bind.DatatypeConverter.printBase64Binary;

final class Rebinder implements CertConsumer {

  final Vaadin vaadin;
  final Panel ui;
  final byte[] x509;
  final String fio;
  final String organization;
  final int maxAttempts;
  final Set<Long> lockedCerts;

  boolean second;

  Rebinder(Vaadin vaadin, Panel ui, byte[] x509, String fio, String organization, int maxAttempts, Set<Long> lockedCerts) {
    this.vaadin = vaadin;
    this.ui = ui;
    this.x509 = x509;
    this.fio = fio;
    this.organization = organization;
    this.maxAttempts = maxAttempts;
    this.lockedCerts = lockedCerts;
  }

  public void ready(String name, PrivateKey unusedPrivateKey, X509Certificate certificate) {
    if (second) {
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
    } else {
      second = true;
      loading();
    }
  }

  @Override
  public void wrongPassword(long certSerialNumber) {
    vaadin.updateVariable("wrongPassword", String.valueOf(certSerialNumber));
    ui.removeAll();
    refresh();
  }

  @Override
  public void loading() {
    if (!second) { // сообщение лишь на первый сертификат
      vaadin.updateVariable("state", "loading");
    }

    ui.removeAll();
    ui.add(new Label("Загрузка сертификатов..."), BorderLayout.LINE_START);
    Label status = new Label("");
    ui.add(status, BorderLayout.CENTER);
    refresh();
    new Thread(new CertDetector(this, ui, status,fio, organization, maxAttempts, lockedCerts)).start();
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
    if (second) {
      return new Filter() {
        @Override
        public boolean accept(X509Certificate certificate) {
          byte[] encoded;
          try {
            encoded = certificate.getEncoded();
          } catch (CertificateEncodingException e) {
            return false;
          }
          return !Arrays.equals(x509, encoded);
        }
      };
    }
    return new EqualsFilter(x509);
  }

  @Override
  public String getActionText() {
    return second ? "Завершить выбор" : "Подтвердить текущий";
  }

  @Override
  public String getSelectionLabel() {
    return second ? "Выберите новый сертификат:" : "Текущий сертификат:";
  }

}
