/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import org.apache.commons.lang.StringUtils;
import ru.codeinside.gses.activiti.ReadOnly;
import ru.codeinside.gses.webui.components.Logout;
import ru.codeinside.gses.webui.components.sign.SignApplet;
import ru.codeinside.gses.webui.components.sign.SignAppletListener;
import ru.codeinside.gses.webui.components.sign.SignUtils;

import java.security.cert.X509Certificate;

final public class CertificateSelection extends CustomComponent {

  final CertificateListener certificateListener;
  final Label header;
  final Label hint;
  final Label appletHint;
  final String login;
  final String userName;
  final VerticalLayout flow;

  public CertificateSelection(String userLogin, CertificateListener certificateListener) {
    this.flow = new VerticalLayout();
    this.certificateListener = certificateListener;
    this.login = userLogin;

    String userName = StringUtils.trimToNull(Flash.flash().getAdminService().findEmployeeByLogin(userLogin).getFio());
    if (userName == null) {
      userName = userLogin;
    }

    this.userName = userName;

    header = new Label();
    if (SignUtils.isEmployeeCertificateExpired(login)) {
      header.setValue("Срок действия сертификата истёк");
    } else {
      header.setValue("Выбор сертификата");
    }
    header.setStyleName(Reindeer.LABEL_H1);

    hint = new Label(
      "<b>" + userName + "</b>, для продолжения работы в Системе исполнения услуг " +
        "Вам необходимо выбрать сертификат, который в дальнейшем будет использоваться для " +
        "<a target='_blank' href='http://ru.wikipedia.org/wiki/" +
        "%D0%AD%D0%BB%D0%B5%D0%BA%D1%82%D1%80%D0%BE%D0%BD%D0%BD%D0%B0%D1%8F_%D0%BF%D0%BE%D0%B4%D0%BF%D0%B8%D1%81%D1%8C'" +
        ">электронной подписи</a></i> предоставляемых Вами данных.", Label.CONTENT_XHTML);

    SignApplet applet = new SignApplet(new Protocol());
    applet.setName("Выбор сертификата");
    applet.setCaption(null);
    applet.setBindingMode();

    appletHint = new Label(
      "Требуется поддержка <b>Java</b> в " + Flash.getActor().getBrowser() + " и наличие <b>КриптоПРО JCP</b>.<br/> " +
        "Справки по получению сертификата и установке программного обеспечения " +
        "можно получить в <a target='_blank' href='http://ca.oep-penza.ru/'" +
        ">Удостоверяющем центре ОАО Оператор Электронного Правительства</a>.", Label.CONTENT_XHTML);


    Button logout = new Button("Выход (выбрать сертификат в другой раз)", new Logout());
    logout.setStyleName(Reindeer.BUTTON_SMALL);

    HorizontalLayout buttons = new HorizontalLayout();
    buttons.setSpacing(true);
    buttons.setMargin(true);
    buttons.addComponent(logout);

    flow.setSizeUndefined();
    flow.setMargin(true);
    flow.setSpacing(true);
    flow.addComponent(header);
    flow.addComponent(hint);
    flow.addComponent(applet);
    flow.addComponent(appletHint);
    flow.addComponent(buttons);

    Panel panel = new Panel();
    panel.setSizeUndefined();
    panel.setContent(flow);

    VerticalLayout center = new VerticalLayout();
    center.addComponent(panel);
    center.setExpandRatio(panel, 1f);
    center.setComponentAlignment(panel, Alignment.MIDDLE_CENTER);
    center.setSizeFull();

    setCompositionRoot(center);

    setSizeFull();
  }

  final class Protocol implements SignAppletListener {
    @Override
    public void onLoading(SignApplet signApplet) {
      if (SignUtils.isEmployeeCertificateExpired(login)) {
        header.setValue("Срок действия сертификата истёк");
      } else {
        header.setValue("Выбор сертификата");
      }

      hint.setVisible(true);
      hint.setValue(
          "<b>" + userName + "</b>, для продолжения работы в Системе исполнения услуг " +
              "Вам необходимо выбрать сертификат, который в дальнейшем будет использоваться для " +
              "<a target='_blank' href='http://ru.wikipedia.org/wiki/" +
              "%D0%AD%D0%BB%D0%B5%D0%BA%D1%82%D1%80%D0%BE%D0%BD%D0%BD%D0%B0%D1%8F_%D0%BF%D0%BE%D0%B4%D0%BF%D0%B8%D1%81%D1%8C'" +
              ">электронной подписи</a></i> предоставляемых Вами данных.");
      hint.setContentMode(Label.CONTENT_XHTML);

      appletHint.setStyleName(Reindeer.LABEL_SMALL);
      appletHint.setVisible(true);
    }

    @Override
    public void onNoJcp(SignApplet signApplet) {
      appletHint.setStyleName(Reindeer.LABEL_H2);
    }

    @Override
    public void onCert(SignApplet signApplet, X509Certificate certificate) {
      signApplet.close();
      try {
        CertificateVerifyClientProvider.getInstance().verifyCertificate(certificate);
        certificateListener.onCertificate(certificate);
      } catch (CertificateInvalid certificateInvalid) {
        header.setValue("Ошибка валидации сертификата");
        appletHint.setVisible(false);
        hint.setVisible(false);

        String fieldValue = certificateInvalid.getMessage();
        ReadOnly field = new ReadOnly(fieldValue);
        flow.addComponent(field, 1);
      }
    }

    @Override
    public void onLockCert(SignApplet signApplet, long certSerialNumber) {
      header.setValue(SignUtils.LOCK_CERT_HINT);
      appletHint.setVisible(false);

      String unlockTimeMessage = SignUtils.lockCertAndGetUnlockTimeMessage(login, certSerialNumber);

      hint.setValue(unlockTimeMessage);
    }

    @Override
    public void onBlockAck(SignApplet signApplet, int i) {
    }

    @Override
    public void onChunkAck(SignApplet signApplet, int i) {
    }

    @Override
    public void onSign(SignApplet signApplet, byte[] sign) {
    }
  }
}
