/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui;

import com.google.common.collect.ImmutableSet;
import com.vaadin.Application;
import com.vaadin.event.EventRouter;
import com.vaadin.terminal.gwt.server.HttpServletRequestListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Table;
import com.vaadin.ui.Window;
import eform.Form;
import eform.FormsHolder;
import ru.codeinside.adm.AdminService;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.database.Role;
import ru.codeinside.gses.webui.components.api.Baseband;
import ru.codeinside.gses.webui.eventbus.SizePublisher;
import ru.codeinside.gses.webui.utils.RunProfile;
import ru.codeinside.log.Actor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ActivitiApp extends Application implements HttpServletRequestListener, Baseband, FormsHolder {

  private static final long serialVersionUID = 1L;

  final EventRouter eventRouter = new EventRouter();
  final URL serverUrl;
  final AtomicLong serial = new AtomicLong();
  final Map<Long, Form> forms = new HashMap<Long, Form>();

  public ActivitiApp(URL serverUrl, String logoutUrl) {
    this.serverUrl = serverUrl;
    setLogoutURL(logoutUrl);
  }

  public URL getServerUrl() {
    return serverUrl;
  }

  public long nextId() {
    return serial.getAndIncrement();
  }

  public Map<Long, Form> getForms() {
    return forms;
  }

  @Override
  public void init() {
    try {
      String userLogin = Flash.login();
      Actor userActor = Flash.getActor();
      ImmutableSet<Role> userRoles = Flash.flash().getRoles();
      boolean productionMode = RunProfile.isProduction();

      setUser(userLogin);
      setTheme("custom");
      setMainWindow(new Window("СИУ :: " + userLogin, createUi(userLogin, userRoles, productionMode)));

      AdminServiceProvider.get().createLog(userActor, "application", userLogin, "userLogin", null, true);
    } catch (Table.CacheUpdateException e) {
      for (Throwable cause : e.getCauses()) {
        Logger.getAnonymousLogger().log(Level.WARNING, "on table" + e.getTable(), cause);
      }
    }
  }

  private ComponentContainer createUi(final String userLogin, final Set<Role> userRoles, final boolean productionMode) {

    if (!productionMode) {
      getContext().addTransactionListener(new SizePublisher(eventRouter));
    }

    if (AdminServiceProvider.get().withEmployee(userLogin, new IsCertificateRequired()) && AdminServiceProvider.getBoolProperty(CertificateVerifier.LINK_CERTIFICATE)) {
      return new CertificateSelection(userLogin, new CertificateSelector(userLogin, userRoles, productionMode));
    }

    return new Workplace(userLogin, userRoles, productionMode);
  }

  @Override
  public void close() {
    super.close();

    AdminService adminService = AdminServiceProvider.tryGet();
    if (adminService != null) {
      try {
        adminService.createLog(Flash.getActor(), "application", (String) getUser(), "logout", null, true);
      } catch (Exception e) {
        // Замечание: при выключении OSGI модуля JEE сервис МОЖЕТ быть не доступен.s
      }
    }
  }

  /**
   * Вызывается ДО инициализации!
   */
  @Override
  public void onRequestStart(HttpServletRequest request, HttpServletResponse response) {
    Flash.set(this);
  }

  @Override
  public void onRequestEnd(HttpServletRequest request, HttpServletResponse response) {
    Flash.clear();
  }

  @Override
  public EventRouter getRouter() {
    return eventRouter;
  }

  @SuppressWarnings("unused") // Vaadin Reflection API
  public static SystemMessages getSystemMessages() {
    CustomizedSystemMessages messages = new CustomizedSystemMessages();
    messages.setSessionExpiredNotificationEnabled(false);
    messages.setCommunicationErrorNotificationEnabled(false);
    return messages;
  }

  @Deprecated
  public Component activate(String name) {
    return null;
  }

  @Deprecated
  public void refresh(String... names) {
  }

  final class CertificateSelector implements CertificateListener {

    final String userLogin;
    final Set<Role> userRoles;
    final boolean productionMode;

    CertificateSelector(String userLogin, Set<Role> userRoles, boolean productionMode) {
      this.userLogin = userLogin;
      this.userRoles = userRoles;
      this.productionMode = productionMode;
    }

    @Override
    public void onCertificate(X509Certificate certificate) {
      boolean ok = AdminServiceProvider.get().withEmployee(userLogin, new CertificateSetter(certificate));
      if (!ok) {
        close();
      } else {
        getMainWindow().setContent(new Workplace(userLogin, userRoles, productionMode));
      }
    }
  }
}
