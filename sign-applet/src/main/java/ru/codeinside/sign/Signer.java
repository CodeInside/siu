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
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Set;

import static javax.xml.bind.DatatypeConverter.printBase64Binary;

final class Signer implements CertConsumer {

  final private Vaadin vaadin;
  final private Panel ui;
  final Filter filter;
  final int maxAttempts;
  final Set<Long> lockedCerts;

  private PrivateKey privateKey;
  private Signature signature;

  private final Label label = new Label("Запуск...");

  Signer(Vaadin vaadin, Panel ui, byte[] x509, int maxAttempts, Set<Long> lockedCerts) {
    this.vaadin = vaadin;
    this.ui = ui;
    this.maxAttempts = maxAttempts;
    this.lockedCerts = lockedCerts;
    filter = new EqualsFilter(x509);
  }

  Signer(Vaadin vaadin, Panel ui, int maxAttempts, Set<Long> lockedCerts) {
    this.vaadin = vaadin;
    this.ui = ui;
    this.maxAttempts = maxAttempts;
    this.lockedCerts = lockedCerts;
    filter = new AcceptAll();
  }

  private int currentBlock;
  private int blocksCount;
  private int currentChunk;
  private int chunksCount;

  public void ready(final String name, PrivateKey privateKey, X509Certificate certificate) {
    this.privateKey = privateKey;

    currentBlock = 0;
    blocksCount = 0;
    currentChunk = 0;
    chunksCount = 0;

    ui.removeAll();
    ui.add(new Label(name), BorderLayout.PAGE_START);
    ui.add(label, BorderLayout.CENTER);

    Button prev = new Button("Отменить");
    Panel panel = new Panel(new BorderLayout(2, 2));
    panel.add(prev, BorderLayout.LINE_START);
    ui.add(panel, BorderLayout.PAGE_END);
    prev.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        loading();
      }
    });
    ui.validate();
    ui.repaint();

    notifyReady(certificate);
  }

  @Override
  public void wrongPassword(long certSerialNumber) {
    vaadin.updateVariable("wrongPassword", String.valueOf(certSerialNumber));
    ui.removeAll();
    refresh();
  }

  @Override
  public void loading() {
    vaadin.updateVariable("state", "loading");

    ui.removeAll();
    ui.add(new Label("Загрузка сертификатов..."), BorderLayout.LINE_START);
    Label label = new Label("");
    ui.add(label, BorderLayout.CENTER);
    ui.validate();
    ui.repaint();
    new Thread(new CertDetector(this, ui, label, maxAttempts, lockedCerts)).start();
  }

  public void refresh() {
    ui.validate();
    ui.repaint();
  }

  @Override
  public void noJcp() {
    ui.removeAll();
    ui.add(new Label("Библиотека JCP не установлена."), BorderLayout.LINE_START);
    refresh();
    vaadin.updateVariable("state", "noJcp");
  }

  @Override
  public Filter getFilter() {
    return filter;
  }

  @Override
  public String getActionText() {
    return "Подписать";
  }

  @Override
  public String getSelectionLabel() {
    return "Текущий сертификат:";
  }

  public void block(int num, int total) {
    currentBlock = num;
    blocksCount = total;
    currentChunk = 0;
    chunksCount = 0;
    try {
      signature = Signature.getInstance("GOST3411withGOST3410EL");
      signature.initSign(privateKey);
      notifyBlock(currentBlock);
    } catch (NoSuchAlgorithmException e) {
      fail(e);
    } catch (InvalidKeyException e) {
      fail(e);
    }
  }


  public void chunk(final int num, final int total, final byte[] bytes) {
    currentChunk = num;
    chunksCount = total;
    try {
      signature.update(bytes);
      if (num != total) {
        notifyChunk(currentChunk);
      } else {
        final byte[] signed = signature.sign();
        signature = null;
        notifySign(signed);
      }
    } catch (SignatureException e) {
      fail(e);
    }
  }

  private void fail(final Exception e) {
    label.setText("Ошибка: " + e.getMessage());
    refresh();

    final StringWriter w = new StringWriter();
    e.printStackTrace(new PrintWriter(w));
    vaadin.updateVariable("fail", w.toString());
  }

  private void notifyBlock(int i) {
    vaadin.updateVariable("block", i);

    float complete = 100f * i / blocksCount;
    label.setText(((int) complete) + "%");
    refresh();
  }

  private void notifyChunk(int i) {
    vaadin.updateVariable("chunk", i);

    float big = 100f * currentBlock / blocksCount;
    float small = 100f / blocksCount * i / chunksCount;
    float complete = big + small;
    label.setText(((int) complete) + "%");
    refresh();
  }

  private void notifySign(final byte[] b) {
    vaadin.updateVariable("sign", printBase64Binary(b));

    float complete = 100f * currentBlock / blocksCount;
    label.setText(((int) complete) + "%");
    refresh();
  }

  private void notifyReady(final X509Certificate c) {
    try {
      vaadin.updateVariable("cert", printBase64Binary(c.getEncoded()));
      label.setText("Получение данных с СИУ для подписания...");
      refresh();
    } catch (CertificateEncodingException e) {
      fail(e);
    }
  }

}
