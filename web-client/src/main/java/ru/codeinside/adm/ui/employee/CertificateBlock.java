/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.ui.employee;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;
import ru.codeinside.adm.UserItem;
import ru.codeinside.gses.cert.NameParts;
import ru.codeinside.gses.cert.X509;
import ru.codeinside.gses.webui.supervisor.ConfirmWindow;

import java.security.cert.X509Certificate;

public class CertificateBlock extends CustomComponent {

  final byte[] x509;
  boolean certificateWasRemoved = false;

  public CertificateBlock(UserItem userItem) {
    this.x509 = userItem.getX509();
    Component root = new HorizontalLayout();
    if (x509 != null) {
      X509Certificate x509Certificate = X509.decode(x509);
      if (x509Certificate != null) {
        HorizontalLayout h = new HorizontalLayout();
        h.setSpacing(true);
        h.setMargin(true);
        h.setSizeUndefined();

        NameParts subjectParts = X509.getSubjectParts(x509Certificate);

        Label certLabel = new Label(subjectParts.getShortName());
        h.addComponent(certLabel);
        h.setComponentAlignment(certLabel, Alignment.MIDDLE_CENTER);

        Button remove = new Button("Сбросить привязку к сертификату");
        remove.setStyleName(Reindeer.BUTTON_SMALL);
        h.addComponent(remove);
        h.setComponentAlignment(remove, Alignment.MIDDLE_CENTER);

        remove.addListener((Button.ClickListener) new CertificateCleaner(remove, certLabel));

        Panel panel = new Panel();
        panel.setCaption("Используемый сертификат:");
        panel.setContent(h);
        panel.setSizeUndefined();
        root = panel;
      } else {
        certificateWasRemoved = true;
      }
    }
    setCompositionRoot(root);
    setSizeUndefined();
  }

  public boolean isCertificateWasRemoved() {
    return certificateWasRemoved;
  }

  final class CertificateCleaner implements Button.ClickListener, Listener {
    private final Button remove;
    private final Label certLabel;

    public CertificateCleaner(Button remove, Label certLabel) {
      this.remove = remove;
      this.certLabel = certLabel;
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
      Window confirmWindow = new ConfirmWindow("Вы уверены, что хотите сбросить привязку к сертификату?");
      confirmWindow.setResizable(false);
      confirmWindow.setClosable(false);
      confirmWindow.addListener(this);
      getWindow().addWindow(confirmWindow);
    }

    @Override
    public void componentEvent(Event event) {
      if (event instanceof ConfirmWindow.ConfirmOkEvent) {
        remove.setVisible(false);
        certLabel.setValue("отсутствует");
        certificateWasRemoved = true;
      }
    }
  }

}
