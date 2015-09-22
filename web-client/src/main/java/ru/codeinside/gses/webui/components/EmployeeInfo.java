/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.components;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.database.CertificateOfEmployee;
import ru.codeinside.adm.database.Employee;
import ru.codeinside.adm.database.Group;
import ru.codeinside.adm.database.Role;
import ru.codeinside.gses.cert.NameParts;
import ru.codeinside.gses.cert.X509;
import ru.codeinside.gses.webui.CertificateInvalid;
import ru.codeinside.gses.webui.CertificateReader;
import ru.codeinside.gses.webui.CertificateVerifyClientProvider;
import ru.codeinside.gses.webui.Flash;
import ru.codeinside.gses.webui.components.sign.SignApplet;
import ru.codeinside.gses.webui.components.sign.SignAppletListener;
import ru.codeinside.gses.webui.components.sign.SignUtils;

import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final public class EmployeeInfo extends Panel {

  public EmployeeInfo(String userLogin, Button button) {

    setCaption("Информация пользователя ".concat(userLogin));

    FormLayout layout = new FormLayout();
    for (Component c : AdminServiceProvider.get().withEmployee(userLogin, new CreateInfo(userLogin))) {
      layout.addComponent(c);
    }
    addComponent(layout);
    addComponent(button);
  }

  final static class CreateInfo implements Function<Employee, Collection<Component>> {
    private String login;

    CreateInfo(String login) {
      this.login = login;
    }

    public Collection<Component> apply(Employee employee) {

      Label fio = new Label(employee.getFio());
      fio.setCaption("ФИО:");

      String snilsValue = employee.getSnils() == null ? "" : employee.getSnils();

      final Pattern snilsPattern = Pattern.compile("\\d{11}");
      final Pattern splitSnilsPattern = Pattern.compile("(\\d{3})(\\d{3})(\\d{3})(\\d{2})");
      final Matcher maskMatcher = snilsPattern.matcher(snilsValue);
      final Matcher splitMatcher = splitSnilsPattern.matcher(snilsValue);

      if (maskMatcher.matches()) {
        snilsValue = splitMatcher.replaceAll("$1-$2-$3 $4");
      }

      Label snils = new Label(snilsValue);
      snils.setCaption("СНИЛС:");

      Set<String> roleNames = employee.getRoleNames();
      Label role = new Label();
      if (roleNames == null || roleNames.isEmpty()) {
        role.setValue("отсутствует");
      } else {
        role.setValue(Joiner.on(", ").join(roleNames));
      }
      role.setCaption("Роль:");

      Label organization = new Label(employee.getOrganization().getName());
      organization.setCaption("Организация:");

      Format formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");
      Label date = new Label(formatter.format(employee.getDate()));
      date.setCaption("Дата создания:");

      String sCreator = employee.getCreator();
      Label creator = new Label();
      if (sCreator == null || sCreator.isEmpty()) {
        creator.setValue("не указан");
      } else {
        creator.setValue(sCreator);
      }
      creator.setCaption("Создатель:");

      Label groupsExecutor = new Label(getGroupsNames(employee.getGroups()));
      groupsExecutor.setCaption("Состоит в группах:");

      Label groupsSupervisorEmp = new Label(getGroupsNames(employee.getEmployeeGroups()));
      groupsSupervisorEmp.setCaption("Имеет доступ к группам исполнителей:");

      Label groupsSupervisorOrg = new Label(getGroupsNames(employee.getOrganizationGroups()));
      groupsSupervisorOrg.setCaption("Имеет доступ к группам организаций:");

      Component certificateUi = null;
      Set<Role> roles = employee.getRoles();
      if (roles.contains(Role.Executor) || roles.contains(Role.Declarant)
        || roles.contains(Role.Supervisor) || roles.contains(Role.SuperSupervisor)) {
        CertificateOfEmployee certificate = employee.getCertificate();
        String certName;
        if (certificate == null) {
          certName = "отсутствует";
        } else {
          X509Certificate x509Certificate = X509.decode(certificate.getX509());
          if (x509Certificate != null) {
            NameParts subjectParts = X509.getSubjectParts(x509Certificate);
            certName = subjectParts.getShortName();
          } else {
            certName = "не соответствует стандарту X509";
          }
        }
        Label label = new Label(certName);
        if (certificate != null && Flash.login().equals(employee.getLogin())) {
          HorizontalLayout h = new HorizontalLayout();
          h.setSpacing(true);
          h.addComponent(label);
          Button remove = new Button("Привязать другой сертификат");
          remove.setStyleName(Reindeer.BUTTON_SMALL);
          h.addComponent(remove);
          h.setComponentAlignment(remove, Alignment.BOTTOM_LEFT);
          remove.addListener(new CertificateRebinder(remove, label, login));
          certificateUi = h;
        } else {
          certificateUi = label;
        }
        certificateUi.setCaption("Используемый сертификат:");
      }

      ArrayList<Component> items = new ArrayList<Component>();
      items.add(fio);
      items.add(snils);
      items.add(role);
      items.add(organization);
      items.add(groupsExecutor);
      items.add(groupsSupervisorEmp);
      items.add(groupsSupervisorOrg);
      items.add(date);
      items.add(creator);
      if (certificateUi != null) {
        items.add(certificateUi);
      }
      return items;
    }

    private String getGroupsNames(Set<Group> groups) {
      Set<String> groupsNames = new HashSet<String>();
      for (Group g : groups) {
        groupsNames.add(g.getName());
      }
      return Joiner.on(", ").join(groupsNames);
    }
  }

  final public static class CertificateRebinder implements Button.ClickListener {

    VerticalLayout layout;
    final Button remove;
    final Label label;
    final String login;
    final Label header;
    final Label hint;

    byte[] x509;
    Label appletHint;

    public CertificateRebinder(Button remove, Label label, String login) {
      this.remove = remove;
      this.label = label;
      this.login = login;
      header = new Label();
      hint = new Label();
      header.setStyleName(Reindeer.LABEL_H2);
      header.setVisible(false);
      hint.setVisible(false);
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
      x509 = AdminServiceProvider.get().withEmployee(Flash.login(), new CertificateReader());
      if (x509 == null) {
        label.getApplication().close();
        return;
      }

      Window window = new Window();
      window.setModal(true);
      window.setClosable(true);
      window.setResizable(false);
      window.setResizeLazy(true);

      window.setCaption("Привязка сертификата");

      SignApplet applet = new SignApplet(new Protocol());
      applet.setName("Привязка сертификата");
      applet.setCaption(null);
      applet.setRebindMode(x509);



      appletHint = new Label(
        "Требуется поддержка <b>Java</b> в " + Flash.getActor().getBrowser() + " и наличие <b>КриптоПРО JCP</b>.<br/> " +
          "Для помощи с установкой программного обеспечения и получения сертификата " +
          "обратитесь в <a target='_blank' href='http://ca.oep-penza.ru/'" +
          ">Удостоверяющий центр Оператора Электронного Правительства</a>.", Label.CONTENT_XHTML);

      layout = new VerticalLayout();
      layout.setSizeUndefined();// вписываем
      layout.addComponent(header);
      layout.addComponent(hint);
      layout.addComponent(applet);
      layout.addComponent(appletHint);
      layout.setSpacing(true);
      layout.setMargin(true);

      window.setContent(layout);

      event.getButton().getWindow().addWindow(window);
    }

    public final class Protocol implements SignAppletListener {
      @Override
      public void onLoading(SignApplet signApplet) {
        appletHint.setStyleName(Reindeer.LABEL_SMALL);
        appletHint.setVisible(true);
        header.setVisible(false);
        hint.setVisible(false);
      }

      @Override
      public void onNoJcp(SignApplet signApplet) {
        appletHint.setStyleName(Reindeer.LABEL_H2);
      }

      @Override
      public void onCert(SignApplet signApplet, X509Certificate certificate) {
        boolean ok = AdminServiceProvider.get().withEmployee(Flash.login(), new CertificateReSetter(certificate));
        if (!ok) {
          label.getApplication().close();
        } else {
          try {
            CertificateVerifyClientProvider.getInstance().verifyCertificate(certificate);

            long certSerialNumber = certificate.getSerialNumber().longValue();
            SignUtils.removeLockedCert(login, certSerialNumber);

            NameParts subjectParts = X509.getSubjectParts(certificate);
            label.setValue(subjectParts.getShortName());
            remove.getWindow().removeWindow(appletHint.getWindow());
          } catch (CertificateInvalid certificateInvalid) {
            appletHint.setVisible(false);

            header.setValue("Ошибка валидации сертификата");
            header.setVisible(true);

            String fieldValue = certificateInvalid.getMessage();
            hint.setValue(fieldValue);
            hint.setVisible(true);
          }
        }
      }

      @Override
      public void onWrongPassword(SignApplet signApplet, long certSerialNumber) {
        appletHint.setVisible(false);

        String unlockTimeMessage = SignUtils.lockCertAndGetUnlockTimeMessage(login, certSerialNumber);
        if (unlockTimeMessage != null) {
          layout.removeComponent(signApplet);

          header.setValue(SignUtils.LOCK_CERT_HINT);
          header.setVisible(true);

          hint.setValue(unlockTimeMessage);
          hint.setVisible(true);
        } else {
          header.setValue(SignUtils.WRONG_CERT_PASSWORD_HINT);
          header.setVisible(true);

          hint.setValue(SignUtils.certAttemptsCountMessage(login, certSerialNumber));
          hint.setVisible(true);
        }
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

    final class CertificateReSetter implements Function<Employee, Boolean> {

      final X509Certificate certificate;

      public CertificateReSetter(X509Certificate certificate) {
        this.certificate = certificate;
      }

      @Override
      public Boolean apply(Employee employee) {
        Set<Role> roles = employee.getRoles();
        if (roles.contains(Role.Executor) || roles.contains(Role.Declarant)
          || roles.contains(Role.Supervisor) || roles.contains(Role.SuperSupervisor)) {
          CertificateOfEmployee certificateOfEmployee = employee.getCertificate();
          if (certificateOfEmployee != null && Arrays.equals(x509, certificateOfEmployee.getX509())) {
            try {
              certificateOfEmployee.setX509(certificate.getEncoded());
              return true;
            } catch (CertificateEncodingException e) {
              // игнорируем
            }
          }
        }
        return false;
      }
    }

  }

}
