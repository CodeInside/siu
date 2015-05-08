package ru.codeinside.auth;

import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.database.Employee;
import ru.codeinside.esia.AppData;
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

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.Holder;
import java.io.IOException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

@WebServlet(urlPatterns = {"/authServlet"})
public class AuthServlet extends HttpServlet {
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String isEsiaAuth = req.getParameter("isEsiaAuth");
    if ("on".equals(isEsiaAuth)) {
      try {
        esiaAuthorization(req,resp);
      } catch (DatatypeConfigurationException e) {
        e.printStackTrace();
      } catch (Exception_Exception e) {
        e.printStackTrace();
      }
    } else {
      //TODO стандартная авторизация
    }
  }

  private void esiaAuthorization(HttpServletRequest req, HttpServletResponse resp) throws DatatypeConfigurationException, Exception_Exception {
    String snils = req.getParameter("snils").replaceAll("\\D+", "");
    String pass = req.getParameter("password");
    Employee user = AdminServiceProvider.get().findEmployeeBySnils(snils);

    if (user != null && createEsiaRequest(snils, pass)) {

      String login = user.getLogin();
      Set<String> roles = user.getRoleNames();

      HasRolePrincipal principal = new UserPrincipal(login, roles);
      req.getSession().setAttribute(AuthorizationFilter.SESSION_ATTR_USER_PRINCIPAL, principal);
      resp.setStatus(HttpServletResponse.SC_OK);
    } else {
      //TODO показать сообщение, что нет юзера с таким СНИЛС
    }
  }

  private Boolean createEsiaRequest(String snils, String pass) throws DatatypeConfigurationException, Exception_Exception {
    Holder<MessageType> message = getMessage();
    MessageConnectESIAData messageData = getMessageData(snils, pass);
    Holder<ResultMessageDataType> result = getResult();

    ConnectESIAService service = new ConnectESIAService();
    service.getConnectESIAPort().getConnectESIA(message, messageData, result);

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
}
