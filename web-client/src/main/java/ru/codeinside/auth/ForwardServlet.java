package ru.codeinside.auth;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

@WebServlet(urlPatterns = {"/back"})
public class ForwardServlet extends HttpServlet {
  private Logger log = Logger.getLogger(ForwardServlet.class.getName());
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    req.setAttribute("isEsiaAuth", "on");
    String url = "/login.jsp";
    try {
      req.getRequestDispatcher(url).forward(req, resp);
    } catch (ServletException e) {
      log.severe("Не удалось перейти на на " + url + ": " + e.getMessage());
      e.printStackTrace();
    } catch (IOException e) {
      log.severe("Не удалось перейти на на " + url + ": " + e.getMessage());
      e.printStackTrace();
    }
  }
}
