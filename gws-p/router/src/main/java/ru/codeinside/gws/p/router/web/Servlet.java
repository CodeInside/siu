/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.p.router.web;


import ru.codeinside.gws.p.adapter.Registry;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Основано на коде com.sun.xml.ws.transport.http.servlet.WSServletContextListener
 * и com.sun.xml.ws.transport.http.servlet.WSServlet.
 */
public class Servlet extends HttpServlet {

    private ServletAdapterRegistrar registrar;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        if (registrar == null) {
            registrar = new ServletAdapterRegistrar();
            registrar.initialize(config.getServletContext());
        }
    }

    @Override
    public void destroy() {
        registrar = null;
        final Registry registry = Registry.REGISTRY.get();
        if (registry != null) {
            registry.destroyPorts();
        }
        super.destroy();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req, resp);
    }

    private void process(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        new Chain(req, resp, registrar).process();
    }

}
