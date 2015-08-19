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
import org.activiti.engine.impl.identity.Authentication;
import ru.codeinside.adm.AdminService;
import ru.codeinside.adm.database.Role;
import ru.codeinside.gses.service.ActivitiService;
import ru.codeinside.gses.service.DeclarantService;
import ru.codeinside.gses.service.ExecutorService;
import ru.codeinside.jpa.LazyJtaTransactionContext;

import javax.annotation.Resource;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


@WebServlet(
  urlPatterns = {"/ui/*"},
  initParams = {
    @WebInitParam(name = "widgetset", value = "ru.codeinside.gses.vaadin.WidgetSet")
    /*,@WebInitParam(name = "productionMode", value = "false")*/
  })
@TransactionManagement(TransactionManagementType.BEAN)
public class ActivitiServlet extends AbstractApplicationServlet {

  private static final long serialVersionUID = 2L;

  @Inject
  ActivitiService activitiService;

  @Inject
  Instance<ProcessEngine> processEngine;

  @Inject
  DeclarantService declarantService;

  @Inject
  ExecutorService executorService;

  @Inject
  AdminService adminService;

  @PersistenceContext(unitName = "myPU")
  EntityManager em;

  @Resource
  UserTransaction userTransaction;


  @Override
  protected Class<? extends Application> getApplicationClass() {
    return ActivitiApp.class;
  }

  @Override
  public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    boolean success = false;
    try {
      Flash.set(new RequestContext(req));
      String login = Flash.login();
      Authentication.setAuthenticatedUserId(login);
      super.service(req, res);
      success = true;
    } finally {
      Authentication.setAuthenticatedUserId(null);
      Flash.clear(success);
    }
  }

  @Override
  protected Application getNewApplication(HttpServletRequest request) throws ServletException {
    URL serverUrl;
    try {
      // Attention - using HTTP !
      serverUrl = new URL("http://" + request.getLocalName() + ":" + request.getLocalPort());
    } catch (MalformedURLException e) {
      throw new ServletException(e);
    }
    return new ActivitiApp(serverUrl, request.getContextPath() + "/logout.jsp");
  }

  final class RequestContext implements Flasher, Flasher.Closable {

    final HttpServletRequest req;
    final LazyJtaTransactionContext emContext = new LazyJtaTransactionContext(userTransaction, em);

    ImmutableSet<Role> lazyRoles;
    String lazyLogin;

    public RequestContext(HttpServletRequest req) {
      this.req = req;
    }

    @Override
    public ImmutableSet<Role> getRoles() {
      if (lazyRoles == null) {
        ImmutableSet.Builder<Role> builder = ImmutableSet.builder();
        for (Role role : Role.values()) {
          if (req.isUserInRole(role.name())) {
            builder.add(role);
          }
        }
        lazyRoles = builder.build();
        storeInSession("ROLES", lazyRoles);
      }
      return lazyRoles;
    }

    @Override
    public ProcessEngine getProcessEngine() {
      return processEngine.isUnsatisfied() ? null : processEngine.get();
    }

    @Override
    public ActivitiService getActivitiService() {
      return activitiService;
    }

    @Override
    public String getLogin() {
      if (lazyLogin == null) {
        lazyLogin = req.getUserPrincipal().getName();
        storeInSession("LOGIN", lazyLogin);
      }
      return lazyLogin;
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

    @Override
    public EntityManager getEm() {
      return emContext.getEntityManager();
    }

    @Override
    public EntityManager getLogEm() {
      throw new UnsupportedOperationException();
    }

    @Override
    public DeclarantService getDeclarantService() {
      return declarantService;
    }

    @Override
    public ExecutorService getExecutorService() {
      return executorService;
    }

    @Override
    public AdminService getAdminService() {
      return adminService;
    }

    private void storeInSession(String name, Object value) {
      HttpSession session = req.getSession(false);
      if (session != null) {
        if (session.getAttribute(name) == null) {
          session.setAttribute(name, value);
        }
      }
    }

    @Override
    public void close(boolean success) {
      emContext.close(success);
    }
  }
}