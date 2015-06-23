package ru.codeinside.gses.webui.form;

import org.osgi.framework.ServiceReference;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.database.Bid;
import ru.codeinside.adm.database.ExternalGlue;
import ru.codeinside.gses.service.Fn;
import ru.codeinside.gses.webui.Flash;
import ru.codeinside.gses.webui.osgi.Activator;
import ru.codeinside.gses.webui.wizard.ResultTransition;
import ru.codeinside.gses.webui.wizard.TransitionAction;
import ru.codeinside.gws.api.InfoSystem;
import ru.codeinside.gws.api.Packet;
import ru.codeinside.gws.api.Server;
import ru.codeinside.gws.api.ServerProtocol;
import ru.codeinside.gws.api.ServerResponse;
import ru.codeinside.gws.api.ServiceDefinition;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import java.io.ByteArrayOutputStream;
import java.util.Date;
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

      fillResponsePacket();

      List<Object> serviceInfo = ProtocolUtils.getQNameAndServicePort(server);
      QName qName = (QName) serviceInfo.get(0);
      ServiceDefinition.Port port = (ServiceDefinition.Port) serviceInfo.get(1);

      ServerProtocol serverProtocol = ProtocolUtils.getServerProtocol(server);
      ByteArrayOutputStream normalizedBody = new ByteArrayOutputStream();

      SOAPMessage message = serverProtocol.createMessage(
          dataAccumulator.getServerResponse(),
          qName, //Service
          port,  //Port
          null,  //Log
          normalizedBody
      );

      dataAccumulator.setSoapMessage(message);

      return new ResultTransition(normalizedBody.toByteArray());
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

  private void fillResponsePacket() {
    Packet packet = dataAccumulator.getServerResponse().packet;
    Bid bid = AdminServiceProvider.get().getBidByTask(dataAccumulator.getTaskId());

    ExternalGlue glue = bid.getGlue();

    ru.codeinside.adm.database.InfoSystem sender;
    ru.codeinside.adm.database.InfoSystem originator;
    if (glue != null &&
        (sender = glue.getSender()) != null &&
        (originator = glue.getOrigin()) != null) {
      packet.recipient = new InfoSystem(sender.getCode(), sender.getName());
      packet.originator = new InfoSystem(originator.getCode(), originator.getName());
    } else {
      throw new IllegalStateException("Нет связи с внешней услугой");
    }

    packet.originRequestIdRef = glue.getRequestIdRef();
    packet.requestIdRef = packet.originRequestIdRef;

    //TODO где брать sender'а? Задавать в маршруте?
    packet.sender = new InfoSystem("PNZR01581", "Комплексная система предоставления государственных и муниципальных услуг Пензенской области");

    packet.date = new Date();
  }

  private void serverResponse(Server server) {
    ServerResponse response = Fn.withEngine(new GetRequestAppDataAction.GetServerResponse(), Flash.login(), dataAccumulator, server);
    dataAccumulator.setServerResponse(response);
  }
}
