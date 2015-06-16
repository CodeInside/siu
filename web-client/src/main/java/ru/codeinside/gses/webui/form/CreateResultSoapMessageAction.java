package ru.codeinside.gses.webui.form;

import org.osgi.framework.ServiceReference;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.database.Bid;
import ru.codeinside.adm.database.ExternalGlue;
import ru.codeinside.gses.webui.osgi.Activator;
import ru.codeinside.gses.webui.wizard.ResultTransition;
import ru.codeinside.gses.webui.wizard.TransitionAction;
import ru.codeinside.gws.api.InfoSystem;
import ru.codeinside.gws.api.Packet;
import ru.codeinside.gws.api.Server;
import ru.codeinside.gws.api.ServerProtocol;
import ru.codeinside.gws.api.ServiceDefinition;
import ru.codeinside.gws.api.ServiceDefinitionParser;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CreateResultSoapMessageAction implements TransitionAction {

  private final DataAccumulator dataAccumulator;

  public CreateResultSoapMessageAction(DataAccumulator dataAccumulator) {
    this.dataAccumulator = dataAccumulator;
  }

  @Override
  public ResultTransition doIt() throws IllegalStateException {
    ServiceReference reference = null;
    try {
      validateState();

      fillResponsePacket();

      String serviceName = ProtocolUtils.getServerName(dataAccumulator.getTaskId());
      reference = ProtocolUtils.getServiceReference(serviceName, Server.class);
      final Server server = ProtocolUtils.getService(reference, Server.class);

      List<Object> serviceInfo = getQNameAndServicePort(server);
      QName qName = (QName) serviceInfo.get(0);
      ServiceDefinition.Port port = (ServiceDefinition.Port) serviceInfo.get(1);

      ServerProtocol serverProtocol = ProtocolUtils.getServerProtocol(server);
      ByteArrayOutputStream normalizedBody = new ByteArrayOutputStream();

      //TODO create SOAPMessage
      SOAPMessage message = serverProtocol.createMessage(
          null,  //TODO где взять ServerRequest?
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
      throw new IllegalStateException("Ошибка получения подготовительных данных: " + e.getMessage());
    } finally {
      Activator.getContext().ungetService(reference);
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
    ru.codeinside.adm.database.InfoSystem recipient; //TODO где брать? Задавать в маршруте?
    ru.codeinside.adm.database.InfoSystem originator;
    if (glue != null) {
      sender = glue.getSender();
      packet.sender = new InfoSystem(sender.getCode(), sender.getName());

      originator = glue.getOrigin();
      packet.originator = new InfoSystem(originator.getCode(), originator.getName());
    } else {
      throw new IllegalStateException("Нет связи с внешней услугой");
    }

    packet.originRequestIdRef = glue.getRequestIdRef();
    packet.requestIdRef = packet.originRequestIdRef;

    //TODO где брать recipient'а ?
    packet.recipient = new InfoSystem("PNZR01581", "Комплексная система предоставления государственных и муниципальных услуг Пензенской области");

    packet.date = new Date();
  }

  private List<Object> getQNameAndServicePort(Server server) {
    final ServiceDefinitionParser serviceDefinitionParser = AdminServiceProvider.get().getServiceDefinitionParser();
    ServiceDefinition serviceDefinition = serviceDefinitionParser.parseServiceDefinition(server.getWsdlUrl());
    List<Object> result = new ArrayList<Object>();

    ServiceDefinition.Service service = null;
    if (serviceDefinition.services.keySet().size() == 1) {
      for (QName name : serviceDefinition.services.keySet()) {
        result.add(name);
        service = serviceDefinition.services.get(name);
      }

      if (service != null && service.ports.size() == 1) {
        for (ServiceDefinition.Port port : service.ports.values()) {
          result.add(port);
        }
      } else {
        //TODO что делать, если портов несколько?
        throw new IllegalStateException("?");
      }
    } else {
      //TODO что делать, если сервис не один?
      throw new IllegalStateException("?");
    }

    return result;
  }
}
