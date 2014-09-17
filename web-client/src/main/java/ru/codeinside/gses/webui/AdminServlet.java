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

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/admin/*"}, initParams = {@WebInitParam(name = "widgetset", value = "ru.codeinside.gses.vaadin.WidgetSet")})
@ServletSecurity(@HttpConstraint(rolesAllowed = "Administrator"))
public class AdminServlet extends AbstractApplicationServlet {

  private static final long serialVersionUID = 3L;

  @Inject
  AdminServiceProvider registry;

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
  public void service(final HttpServletRequest req, final HttpServletResponse res) throws ServletException,
    IOException {
    try {
      Flash.set(new Flasher() {

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
          return null;
        }

        public ActivitiService getActivitiService() {
          return null;
        }

        public ProcessEngine getProcessEngine() {
          return null;
        }

        public DeclarantService getDeclarantService() {
          return null;
        }
				public AdminService getAdminService() {
					return null;
				}

                @Override
                public EntityManager getEm() {
                    return null;
                }

        public ExecutorService getExecutorService() {
          return null;
        }

        public AdminService getAdminService() {
          return null;
        }

      });
      super.service(req, res);
    } finally {
      Flash.clear();
    }
  }
}