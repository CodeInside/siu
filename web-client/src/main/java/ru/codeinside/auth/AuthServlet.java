package ru.codeinside.auth;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/authServlet"})
public class AuthServlet extends HttpServlet {
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//    Set<String> roles = new HashSet<String>();
//    roles.add("Administrator");
//    roles.add("1");
//    roles.add("2");
//    HasRolePrincipal principal = new UserPrincipal("demoadmin", roles);
//    req.getSession().setAttribute(AuthorizationFilter.SESSION_ATTR_USER_PRINCIPAL, principal);
//    resp.setStatus(HttpServletResponse.SC_OK);

    // TODO: Реализовать авторизацию стангдартную и через ЕСИА в кавычках
  }
}
