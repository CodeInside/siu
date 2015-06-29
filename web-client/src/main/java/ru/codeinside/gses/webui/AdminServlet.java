/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui;

import com.google.common.collect.ImmutableSet;
import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.AbstractApplicationServlet;
import org.activiti.engine.ProcessEngine;
import ru.codeinside.adm.AdminService;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.database.Role;
import ru.codeinside.adm.ui.AdminApp;
import ru.codeinside.gses.service.ActivitiService;
import ru.codeinside.gses.service.DeclarantService;
import ru.codeinside.gses.service.ExecutorService;
import ru.codeinside.jpa.LazyJtaTransactionContext;

import javax.annotation.Resource;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;
import java.io.IOException;

@WebServlet(urlPatterns = {"/admin/*"}, initParams = {@WebInitParam(name = "widgetset", value = "ru.codeinside.gses.vaadin.WidgetSet")})
@TransactionManagement(TransactionManagementType.BEAN)
public class AdminServlet extends AbstractApplicationServlet {

  private static final long serialVersionUID = 3L;

  @Inject
  AdminServiceProvider registry;

  @Resource
  UserTransaction userTransaction;

  @PersistenceContext(unitName = "myPU")
  EntityManager em;

  @PersistenceContext(unitName = "logPU")
  EntityManager logEm;

  @Override
  protected Application getNewApplication(HttpServletRequest request) throws ServletException {
    AdminApp app = new AdminApp();
    app.setUser(request.getUserPrincipal().getName());
    app.setLogoutURL(request.getContextPath() + "/logout.jsp");
    return app;
  }

  @Override
  protected Class<? extends Application> getApplicationClass() throws ClassNotFoundException {
    return AdminApp.class;
  }

  @Override
  public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    boolean success = false;
    try {
      Flash.set(new RequestContext(req));
      super.service(req, res);
      success = true;
    } finally {
      Flash.clear(success);
    }
  }

  final class RequestContext implements Flasher, Flasher.Closable {

    private final HttpServletRequest req;

    LazyJtaTransactionContext emProvider = new LazyJtaTransactionContext(userTransaction, em);
    LazyJtaTransactionContext logEmProvider = new LazyJtaTransactionContext(userTransaction, logEm);

    public RequestContext(HttpServletRequest req) {
      this.req = req;
    }

    @Override
    public String getLogin() {
      return req.getUserPrincipal().getName();
    }

    @Override
    public String getRemoteAddr() {
      String ipAddress = req.getHeader("X-FORWARDED-FOR");
      if (ipAddress == null) {
        ipAddress = req.getRemoteAddr();
      }
      return ipAddress;
    }

    @Override
    public String getUserAgent() {
      return req.getHeader("user-agent");
    }

    public ImmutableSet<Role> getRoles() {
      throw new UnsupportedOperationException();
    }

    public ActivitiService getActivitiService() {
      throw new UnsupportedOperationException();
    }

    public ProcessEngine getProcessEngine() {
      throw new UnsupportedOperationException();
    }

    public DeclarantService getDeclarantService() {
      throw new UnsupportedOperationException();
    }

    public AdminService getAdminService() {
      throw new UnsupportedOperationException();
    }

    @Override
    public EntityManager getEm() {
      return emProvider.getEntityManager();
    }

    @Override
    public EntityManager getLogEm() {
      return logEmProvider.getEntityManager();
    }

    @Override
    public void close(boolean success) {
      emProvider.close(success);
      logEmProvider.close(success);
    }

    public ExecutorService getExecutorService() {
      throw new UnsupportedOperationException();
    }

  }
}