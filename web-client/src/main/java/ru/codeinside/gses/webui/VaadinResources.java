/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

import ru.codeinside.gses.vaadin.AbstractApplicationServlet;

import com.vaadin.Application;

@WebServlet(urlPatterns = { "/VAADIN/*" })
final public class VaadinResources extends AbstractApplicationServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected Class<? extends Application> getApplicationClass() {
		throw new UnsupportedOperationException();
	}

	@Override
	protected Application getNewApplication(HttpServletRequest request) throws ServletException {
		throw new UnsupportedOperationException();
	}

}