/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.sign;

import sun.security.util.DerInputStream;
import sun.security.util.DerValue;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.List;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.io.IOException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.BoxLayout;

final class CertSelector implements Runnable {

  final Panel ui;
  final java.util.List<Cert> certs;
  final CertConsumer consumer;
  final String fio;
  final String organization;
  int maxAttempts;
  Set<Long> lockedCerts;
  Cert selectedCert;


  CertSelector(Panel ui, ArrayList<Cert> certs, CertConsumer consumer, String fio, String organization, int maxAttempts, Set<Long> lockedCerts) {
    this.ui = ui;
    this.certs = certs;
    this.consumer = consumer;
    this.maxAttempts = maxAttempts;
    this.lockedCerts = lockedCerts;
    this.fio = fio;
    this.organization = organization;
  }

  CertSelector(Panel ui, ArrayList<Cert> certs, CertConsumer consumer, int maxAttempts, Set<Long> lockedCerts) {
    this.ui = ui;
    this.certs = certs;
    this.consumer = consumer;
    this.maxAttempts = maxAttempts;
    this.lockedCerts = lockedCerts;
    this.fio = null;
    this.organization = null;
  }

  @Override
  public void run() {
    final AtomicInteger counter = new AtomicInteger(0);
    ui.removeAll();
    final Label comp1 = new Label(consumer.getSelectionLabel());
    final Label comp2 = new Label("подпись, соответствующую данной учетной записи.");
    final Label comp3 = new Label("Использовать данную ЭП в СИУ невозможно.");
    Panel p = new Panel();
    p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
    p.add(comp1);
    p.add(comp2);
    p.add(comp3);

    Font f = new Font("Arial", Font.PLAIN, 12);
    comp1.setFont(f);
    comp2.setFont(f);
    comp3.setFont(f);
    comp2.setVisible(false);
    comp3.setVisible(false);
    comp2.setBackground(Color.RED);
    comp3.setBackground(Color.RED);
    ui.add(p, BorderLayout.PAGE_START);
    List list = new List();
    for (Cert cert : certs) {
      list.add(cert.name);
    }
    ui.add(list, BorderLayout.CENTER);
    final Button next = new Button(" Ввести пароль ");
    next.setEnabled(false);

    Panel panel = new Panel(new BorderLayout());
    panel.add(next, BorderLayout.LINE_END);
    ui.add(panel, BorderLayout.PAGE_END);
    list.addItemListener(new ItemListener() {
      @Override
      public void itemStateChanged(ItemEvent e) {
        boolean selected = ItemEvent.SELECTED == e.getStateChange();
        if (selected) {
          selectedCert = certs.get((Integer) e.getItem());
          if (consumer instanceof Binder || consumer instanceof Rebinder) {
            String certificateFIO = selectedCert.extract(selectedCert.certificate.getSubjectDN().getName(), "CN=");
            String surName = selectedCert.extract(selectedCert.certificate.getSubjectDN().getName(), "SURNAME=");
            String givenName = selectedCert.extract(selectedCert.certificate.getSubjectDN().getName(), "GIVENNAME=");
            String organizationName = selectedCert.extract(selectedCert.certificate.getSubjectDN().getName(), "O=");

            if (((surName != null && givenName != null && fio.equals(surName + " " + givenName)) || fio.equals(certificateFIO)) &&
                organizationName != null && organizationName.equals(organization)) {
              Date privateKeyDate = ignoreTime(new Date(getPrivateKeyTime(selectedCert.certificate)));
              Date today = ignoreTime(new Date());
              long diff = privateKeyDate.getTime() - today.getTime();
              int diffDays =  (int) (diff / 1000 /*seconds*/ / 60 /*minutes*/ / 60 /*hours*/ / 24 /*days*/);

              if (diffDays >= 0 && diffDays < 15) {
                comp1.setText("Этот сертификат истекает " + getDiffMessage(diffDays));
                comp1.setBackground(Color.RED);
              } else {
                comp1.setText("Выбранная электронная подпись соответствует данной учетной записи.");
                comp1.setBackground(null);
              }
              comp2.setVisible(false);
              next.setEnabled(true);

            } else {
              comp1.setText("Неверно выбрана электронная подпись. Выберите электронную");
              comp1.setBackground(Color.RED);
              comp2.setVisible(true);
              next.setEnabled(false);
            }
          } else if (consumer instanceof Signer) {
            comp2.setText("Обратитесь в Удостоверяющий центр для получения новой ЭП.");
            long certificateTime = selectedCert.certificate.getNotAfter().getTime();
            long currentTime = System.currentTimeMillis();
            long privateKeyTime = getPrivateKeyTime(selectedCert.certificate);
            if (currentTime <= certificateTime && (privateKeyTime == 0L || currentTime <= privateKeyTime)) {
              next.setEnabled(true);
              comp1.setText("Вы можете использовать выбранную подпись.");
              comp1.setBackground(null);
              comp2.setVisible(false);
              comp3.setVisible(false);
            } else {
              next.setEnabled(false);
              comp1.setText("Срок действия закрытого ключа ЭП истек в " +
                      new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(privateKeyTime))
              );
              comp1.setBackground(Color.RED);
              comp2.setVisible(true);
              comp3.setVisible(true);
            }
          }
        }
        refresh();
      }
    });
    refresh();

    ActionListener certSelector = new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        ui.removeAll();

        if (lockedCerts.contains(selectedCert.certificate.getSerialNumber().longValue())) {
          consumer.lockCert(selectedCert.certificate.getSerialNumber().longValue());
        } else {

          ui.add(new Label(selectedCert.name), BorderLayout.PAGE_START);
          final TextField pass = new TextField();
          pass.setEchoChar('*');

          Panel panel3 = new Panel(new GridBagLayout());
          final Label hint = new Label("Введите пароль:");
          final Label passHint = new Label();
          passHint.setVisible(false);

          panel3.add(hint, new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.LAST_LINE_START,
              GridBagConstraints.HORIZONTAL, new Insets(2, 2, 0, 2), 0, 0));

          panel3.add(passHint, new GridBagConstraints(0, 1, 1, 1, 1, 0, GridBagConstraints.FIRST_LINE_START,
              GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 2), 0, 0));

          panel3.add(pass, new GridBagConstraints(0, 2, 1, 1, 1, 0, GridBagConstraints.FIRST_LINE_START,
              GridBagConstraints.HORIZONTAL, new Insets(0, 2, 2, 2), 0, 0));

          ui.add(panel3, BorderLayout.CENTER);

          final Button prev = new Button("Выбрать другой сертификат");
          prev.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              EventQueue.invokeLater(new CertLoading(consumer));
            }
          });
          final Button next = new Button(consumer.getActionText());
          Panel panel = new Panel(new BorderLayout(2, 2));
          panel.add(prev, BorderLayout.LINE_START);
          panel.add(next, BorderLayout.LINE_END);
          ui.add(panel, BorderLayout.PAGE_END);
          consumer.refresh();
          pass.addTextListener(new TextListener() {
            @Override
            public void textValueChanged(TextEvent e) {
              next.setEnabled(true);
              hint.setText("Пароль:");
            }
          });
          ActionListener withPass = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              prev.setEnabled(false);
              next.setEnabled(false);
              pass.setEnabled(false);
              hint.setText("Проверка пароля...");
              refresh();
              try {
                KeyStore keyStore = KeyStore.getInstance(selectedCert.type + "Store", "JCP");
                keyStore.load(null, null);
                char[] password = pass.getText().toCharArray().length > 0 ? pass.getText().toCharArray() : null;
                PrivateKey privateKey = (PrivateKey) keyStore.getKey(selectedCert.alias, password);
                if (privateKey != null) {
                  consumer.ready(selectedCert.name, privateKey, selectedCert.certificate);
                } else {
                  hint.setText("Сертификат не поддерживается");
                  prev.setEnabled(true);
                  refresh();
                }
              } catch (Exception ex) {
                if ("Password is not valid.".equals(ex.getMessage())) {
                  counter.getAndIncrement();
                  if (counter.get() >= maxAttempts) {
                    consumer.lockCert(selectedCert.certificate.getSerialNumber().longValue());
                  } else {
                    hint.setText("Ошибка, введите правильный пароль!");
                    passHint.setText("Количество попыток ввода пароля до блокировки: " + (maxAttempts - counter.get()));
                    passHint.setVisible(true);
                  }
                } else {
                  hint.setText(ex.getMessage());
                }
                prev.setEnabled(true);
                pass.setEnabled(true);
                pass.requestFocus();
                refresh();
              }
            }
          };
          pass.addActionListener(withPass);
          next.addActionListener(withPass);
          pass.requestFocus();
          refresh();
        }
      }
    };
    next.addActionListener(certSelector);
    list.requestFocus();
    refresh();

  }

  private void refresh() {
    ui.validate();
    ui.repaint();
  }

  long getPrivateKeyTime(X509Certificate certificate) {
    long time = 0L;
    try {
      byte[] value = certificate.getExtensionValue("2.5.29.16");
      if (value != null) {
        DerInputStream der = new DerInputStream(value);
        der = new DerInputStream(der.getOctetString());
        DerValue[] sequence = der.getSequence(2);
        time = new SimpleDateFormat("yyyyMMddHHmmss'Z'").parse(new String(sequence[1].getDataBytes())).getTime();
      }
    } catch (ParseException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return time;
  }

  private String getDiffMessage(int diffDays) {
    switch (diffDays) {
      case 0: return "СЕГОДНЯ";
      case 1: return "ЗАВТРА";
      case 2: return "через 1 день";
      case 3:
      case 4:
      case 5: return "через " + (diffDays - 1) + " дня";
      default: return "через " + (diffDays - 1) + " дней";
    }
  }

  public static Date ignoreTime(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);

    return calendar.getTime();
  }
}
