package ru.codeinside.gses.webui.form;

import org.osgi.framework.ServiceReference;
import ru.codeinside.gses.service.Fn;
import ru.codeinside.gses.webui.Flash;
import ru.codeinside.gses.webui.osgi.Activator;
import ru.codeinside.gses.webui.wizard.ResultTransition;
import ru.codeinside.gses.webui.wizard.TransitionAction;
import ru.codeinside.gws.api.Server;
import ru.codeinside.gws.api.ServerProtocol;
import ru.codeinside.gws.api.ServerResponse;
import ru.codeinside.gws.api.ServiceDefinition;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.logging.Logger;

public class CreateResultSoapMessageAction implements TransitionAction {
  private final Logger logger = Logger.getLogger(CreateResultSoapMessageAction.class.getName());

  private final DataAccumulator dataAccumulator;

  public CreateResultSoapMessageAction(DataAccumulator dataAccumulator) {
    this.dataAccumulator = dataAccumulator;
  }

  @Override
  public ResultTransition doIt() throws IllegalStateException {
    ServiceReference reference = null;
    try {
      String serviceName = ProtocolUtils.getServerName(dataAccumulator.getTaskId());
      reference = ProtocolUtils.getServiceReference(serviceName, Server.class);
      final Server server = ProtocolUtils.getService(reference, Server.class);

      if (dataAccumulator.getServerResponse() == null) {
        serverResponse(server);
      }

      validateState();

      List<Object> serviceInfo = ProtocolUtils.getQNameAndServicePort(server);
      QName qName = (QName) serviceInfo.get(0);
      ServiceDefinition.Port port = (ServiceDefinition.Port) serviceInfo.get(1);

      ServerProtocol serverProtocol = ProtocolUtils.getServerProtocol(server);
      ByteArrayOutputStream normalizedSignedInfo = new ByteArrayOutputStream();

      SOAPMessage message = serverProtocol.createMessage(
          dataAccumulator.getServerResponse(),
          qName, //Service
          port,  //Port
          null,  //Log
          normalizedSignedInfo
      );

      dataAccumulator.setSoapMessage(message);

      return new ResultTransition(normalizedSignedInfo.toByteArray());
    } catch (RuntimeException e) {
      e.printStackTrace();
      throw new IllegalStateException("Ошибка получения подготовительных данных: " + e.getMessage(), e);
    } finally {
      if (reference != null) {
        Activator.getContext().ungetService(reference);
      }
    }
  }

  private void validateState() {
    if (dataAccumulator.getServerResponse() == null) {
      throw new IllegalStateException("Отсутствуют данные сервиса.");
    }
  }

  private void serverResponse(Server server) {
    ServerResponse response = Fn.withEngine(new GetRequestAppDataAction.GetServerResponse(), Flash.login(), dataAccumulator, server);
    dataAccumulator.setServerResponse(response);
  }
}
