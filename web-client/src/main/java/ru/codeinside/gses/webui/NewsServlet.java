/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui;

import ru.codeinside.adm.database.News;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class NewsServlet extends HttpServlet {

  public NewsServlet() {
    super();
  }

  @Override
  public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    GetNewsAction action = new GetNewsAction();
    String url = action.perform(request, response);
    if (url != null)
      getServletContext().getRequestDispatcher(url).forward(request, response);
  }

  public class GetNewsAction {
    public String perform(HttpServletRequest request, HttpServletResponse response) {
      request.getSession().setAttribute("news", News.getNews());
      return "/login.jsp";
    }
  }
}
