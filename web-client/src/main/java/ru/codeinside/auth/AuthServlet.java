package ru.codeinside.auth;

import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.database.Employee;
import ru.codeinside.filter.AuthorizationFilter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@WebServlet(urlPatterns = {"/authServlet"})
public class AuthServlet extends HttpServlet {
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String isEsiaAuth = req.getParameter("isEsiaAuth");
    if ("true".equals(isEsiaAuth)) {
      String snils = req.getParameter("snils").replaceAll("\\D+", "");
      Employee user = AdminServiceProvider.get().findEmployeeBySnils(snils);
      if (user != null) {
        //TODO дёрнуть сервис и, если всё пучком, то:

        String login = user.getLogin();
        Set<String> roles = user.getRoleNames();

        HasRolePrincipal principal = new UserPrincipal(login, roles);
        req.getSession().setAttribute(AuthorizationFilter.SESSION_ATTR_USER_PRINCIPAL, principal);
        resp.setStatus(HttpServletResponse.SC_OK);
      } else {
        //TODO показать сообщение, что нет юзера с таким СНИЛС
      }
    } else {
      //TODO стандартная авторизация
    }
  }
}
