package ru.codeinside.auth;

import com.google.common.collect.Sets;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.database.Employee;
import ru.codeinside.adm.database.Role;
import ru.codeinside.esia.AppData;
import ru.codeinside.esia.ConnectESIA;
import ru.codeinside.esia.ConnectESIAService;
import ru.codeinside.esia.DataRow;
import ru.codeinside.esia.Exception_Exception;
import ru.codeinside.esia.MessageConnectESIAData;
import ru.codeinside.esia.MessageType;
import ru.codeinside.esia.OrgExternalType;
import ru.codeinside.esia.Result;
import ru.codeinside.esia.ResultAppData;
import ru.codeinside.esia.ResultMessageDataType;
import ru.codeinside.esia.StatusType;
import ru.codeinside.esia.TypeCodeType;
import ru.codeinside.filter.AuthorizationFilter;
import ru.codeinside.gses.API;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Holder;
import javax.xml.ws.WebServiceException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

@WebServlet(urlPatterns = {"/authServlet"})
public class AuthServlet extends HttpServlet {
  private Logger log = Logger.getLogger(AuthServlet.class.getName());

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String isEsiaAuth = req.getParameter("isEsiaAuth");
    if ("on".equals(isEsiaAuth)) {
      esiaAuthorization(req, resp);
    } else {
      standardAuthorization(req, resp);
    }
  }

  private void standardAuthorization(HttpServletRequest req, HttpServletResponse resp) {
    String login = req.getParameter("username");
    String password = req.getParameter("password");
    Employee employee = AdminServiceProvider.get().findEmployeeByLogin(login);
    if (employee != null && employee.checkPassword(password)) {
      setPrincipal(req, employee);
      sendRedirect(resp, "/web-client/");
    } else {
      sendForward(req, resp, "/loginError.jsp", "loginError");
    }
  }

  private void esiaAuthorization(HttpServletRequest req, HttpServletResponse resp) {
    String snils = req.getParameter("snils").replaceAll("\\D+", "");
    String pass = req.getParameter("password");
    Employee user = AdminServiceProvider.get().findEmployeeBySnils(snils);

    try {
      if (user != null && createEsiaRequest(snils, pass)) {
        setPrincipal(req, user);
        resp.setStatus(HttpServletResponse.SC_OK);
        sendRedirect(resp, "/web-client/");
      } else {
        sendForward(req, resp, "/loginError.jsp", "snilsError");
      }
    } catch (Exception_Exception e) {
      e.printStackTrace();
      sendForward(req, resp, "/loginError.jsp", "esiaError");
    } catch (MalformedURLException e) {
      e.printStackTrace();
      sendForward(req, resp, "/loginError.jsp", "esiaError");
    } catch (DatatypeConfigurationException e) {
      e.printStackTrace();
      sendForward(req, resp, "/loginError.jsp", "esiaError");
    } catch (WebServiceException e) {
      e.printStackTrace();
      sendForward(req, resp, "/loginError.jsp", "esiaError");
    }
  }

  private Boolean createEsiaRequest(String snils, String pass) throws Exception_Exception, MalformedURLException, DatatypeConfigurationException {
    Holder<MessageType> message = getMessage();
    MessageConnectESIAData messageData = getMessageData(snils, pass);
    Holder<ResultMessageDataType> result = getResult();

    String serviceAddress = AdminServiceProvider.get().getSystemProperty(API.ESIA_SERVICE_ADDRESS);

    ConnectESIAService service = new ConnectESIAService();
    ConnectESIA endpoint = service.getConnectESIAPort();
    BindingProvider provider = (BindingProvider) endpoint;
    Map<String, Object> requestContext = provider.getRequestContext();
    requestContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, serviceAddress);

    endpoint.getConnectESIA(message, messageData, result);

    return getEsiaRequestResult(result);
  }

  private Holder<MessageType> getMessage() throws DatatypeConfigurationException {
    MessageType message = new MessageType();

    OrgExternalType sender = new OrgExternalType();
    sender.setCode("604101");
    sender.setName("Автоматизированная информационная система ДОКА (Пензенской области)");

    OrgExternalType recipient = new OrgExternalType();
    recipient.setCode("OEP");
    recipient.setName("OEP");

    OrgExternalType originator = new OrgExternalType();
    originator.setCode("604101");
    originator.setName("Автоматизированная информационная система ДОКА (Пензенской области)");

    message.setSender(sender);
    message.setRecipient(recipient);
    message.setOriginator(originator);
    message.setTypeCode(TypeCodeType.GSRV);
    message.setStatus(StatusType.REQUEST);

    GregorianCalendar calendar = new GregorianCalendar();
    calendar.setTime(new Date());
    XMLGregorianCalendar date = DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);

    message.setDate(date);
    message.setExchangeType("2");

    Holder<MessageType> messageHolder = new Holder<MessageType>();
    messageHolder.value = message;

    return messageHolder;
  }

  private MessageConnectESIAData getMessageData(String snils, String pass) {
    MessageConnectESIAData messageData = new MessageConnectESIAData();

    Result result = new Result();
    List<DataRow> dataRows = result.getDataRow();

    DataRow snilsData = new DataRow();
    snilsData.setName("username");
    snilsData.setValue(snils);

    DataRow passData = new DataRow();
    passData.setName("password");
    passData.setValue(pass);

    dataRows.add(snilsData);
    dataRows.add(passData);

    AppData appData = new AppData();
    appData.setResult(result);

    messageData.setAppData(appData);

    return messageData;
  }

  private Holder<ResultMessageDataType> getResult() {
    ResultMessageDataType resultType = new ResultMessageDataType();
    ResultAppData resultAppData = new ResultAppData();
    resultType.setAppData(resultAppData);
    Holder<ResultMessageDataType> resultHolder = new Holder<ResultMessageDataType>();
    resultHolder.value = resultType;
    return resultHolder;
  }

  private boolean getEsiaRequestResult(Holder<ResultMessageDataType> result) {
    List<Result> resultList = result.value.getAppData().getResult();
    if (resultList != null && resultList.size() == 1) {
      List<DataRow> dataRows = resultList.get(0).getDataRow();
      if (dataRows != null && dataRows.size() == 1) {
        return Boolean.valueOf(dataRows.get(0).getValue());
      } else {
        return false;
      }
    } else {
      return false;
    }
  }

  private void sendRedirect(HttpServletResponse resp, String url) {
    try {
      resp.sendRedirect(url);
    } catch (IOException e) {
      log.severe("Не удалось сделать редирект на " + url + ": " + e.getMessage());
    }
  }

  private void sendForward(HttpServletRequest req, HttpServletResponse resp, String url, String errorMessage) {
    req.setAttribute("error", errorMessage);
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

  private void setPrincipal(HttpServletRequest req, Employee employee) {
    String login = employee.getLogin();
    Set<String> roles = Sets.newHashSet();
    for (Role role : employee.getRoles()) {
      roles.add(role.name());
    }
    HasRolePrincipal principal = new UserPrincipal(login, roles);
    req.getSession().setAttribute(AuthorizationFilter.SESSION_ATTR_USER_PRINCIPAL, principal);
  }
}
