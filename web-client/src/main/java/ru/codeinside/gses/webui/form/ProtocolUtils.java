package ru.codeinside.gses.webui.form;

import org.activiti.engine.delegate.BpmnError;
import org.apache.commons.lang.StringUtils;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.database.Bid;
import ru.codeinside.adm.database.ExternalGlue;
import ru.codeinside.adm.database.InfoSystemService;
import ru.codeinside.gses.activiti.Activiti;
import ru.codeinside.gses.webui.osgi.Activator;
import ru.codeinside.gws.api.Client;
import ru.codeinside.gws.api.ClientProtocol;
import ru.codeinside.gws.api.ClientRequest;
import ru.codeinside.gws.api.Enclosure;
import ru.codeinside.gws.api.InfoSystem;
import ru.codeinside.gws.api.ProtocolFactory;
import ru.codeinside.gws.api.Server;
import ru.codeinside.gws.api.ServerProtocol;
import ru.codeinside.gws.api.ServiceDefinition;
import ru.codeinside.gws.api.ServiceDefinitionParser;

import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.List;

public class ProtocolUtils {
  private ProtocolUtils() {
    throw new UnsupportedOperationException("Static methods only");
  }

  public static ClientProtocol getClientProtocol(Client client) {
    ClientProtocol clientProtocol;
    ServiceReference serviceReference = Activator.getContext().getServiceReference(ProtocolFactory.class.getName());

    try {
      ProtocolFactory protocolFactory = (ProtocolFactory) Activator.getContext().getService(serviceReference);
      clientProtocol = protocolFactory.createClientProtocol(client.getRevision());
    } finally {
      Activator.getContext().ungetService(serviceReference);
    }
    return clientProtocol;
  }

  public static ServerProtocol getServerProtocol(Server service) {
    ServerProtocol serverProtocol;
    ServiceReference serviceReference = Activator.getContext().getServiceReference(ProtocolFactory.class.getName());

    try {
      ProtocolFactory protocolFactory = (ProtocolFactory) Activator.getContext().getService(serviceReference);
      final ServiceDefinitionParser serviceDefinitionParser = AdminServiceProvider.get().getServiceDefinitionParser();
      ServiceDefinition serviceDefinition = serviceDefinitionParser.parseServiceDefinition(service.getWsdlUrl());
      serverProtocol = protocolFactory.createServerProtocol(serviceDefinition);
    } finally {
      Activator.getContext().ungetService(serviceReference);
    }
    return  serverProtocol;
  }

  public static <T> ServiceReference getServiceReference(String serviceName, Class<T> clazz) {
    ServiceReference[] references;
    String filter = "(&(component.name=" + serviceName + "))";
    try {
      references = Activator.getContext().getAllServiceReferences(clazz.getName(), filter);
    } catch (InvalidSyntaxException e) {
      throw new IllegalStateException("Не удаётся получить ссылку на сервис " + serviceName);
    }

    ServiceReference reference = null;
    if (references == null || references.length < 1) {
      throw new IllegalStateException("Сервис " + serviceName + " не найден");
    } else if (references.length == 1) {
      reference = references[0];
    } else if (references.length > 1) { // Если есть несколько сервисов с таким именем, берём тот, у которого выше версия
      for (ServiceReference comparedReference : references) {
        if (reference == null) {
          reference = comparedReference;
        }

        int comparedReferenceId = Integer.valueOf(comparedReference.getProperty("service.id").toString());
        int referenceId = Integer.valueOf(reference.getProperty("service.id").toString());

        if (comparedReferenceId > referenceId) {
          reference = comparedReference;
        }
      }
    }
    return reference;
  }

  public static String getServerName(String taskId) {
    Bid bid;
    if (taskId != null) {
      bid = AdminServiceProvider.get().getBidByTask(taskId);
    } else {
      throw new IllegalStateException("Task id is null");
    }

    ExternalGlue glue = bid.getGlue();

    if (glue == null) {
      throw new BpmnError("Нет связи с внешней услугой");
    }

    return glue.getName();
  }

  public static <T> T getService(ServiceReference reference, Class<T> clazz) {
    return  (T) Activator.getContext().getService(reference);
  }

  //В возвращаемом списке первый элемент - QName, второй - ServiceDefinition.Port
  public static List<Object> getQNameAndServicePort(Server server) {
    final ServiceDefinitionParser serviceDefinitionParser = AdminServiceProvider.get().getServiceDefinitionParser();
    ServiceDefinition serviceDefinition = serviceDefinitionParser.parseServiceDefinition(server.getWsdlUrl());
    List<Object> result = new ArrayList<Object>();

    ServiceDefinition.Service service = null;
    if (serviceDefinition != null &&
        serviceDefinition.services!= null &&
        serviceDefinition.services.keySet().size() == 1) {

      for (QName name : serviceDefinition.services.keySet()) {
        result.add(name);
        service = serviceDefinition.services.get(name);
      }

      if (service != null && service.ports.size() == 1) {
        for (ServiceDefinition.Port port : service.ports.values()) {
          result.add(port);
        }
      } else {
        throw new IllegalStateException("Порт сервиса поставщика не найден");
      }

    } else {
      throw new IllegalStateException("Сервис поставщика не найден");
    }

    return result;
  }

  public static Enclosure createEnclosureInStartEventContext(TmpAttachment attachment, String variableName) {
    if (attachment != null) {
      byte[] content = attachment.getContent();
      if (content != null) {
        String name = StringUtils.trimToNull(attachment.getName());
        if (name == null) {
          name = StringUtils.trimToNull(variableName);
        }
        Enclosure enclosure = new Enclosure(name, name, content);
        enclosure.id = attachment.getId();
        enclosure.number = attachment.getId();
        enclosure.mimeType = attachment.getType();
        enclosure.digest = Activiti.createDigest(content);
//        enclosure.signature = createSignature(executionId, variableName, content);//TODO
        enclosure.code = variableName;
        return enclosure;
      }
    }
    return null;
  }

  public static void fillRequestPacket(ClientRequest request, String serviceName) {
    InfoSystemService currentService = validateAndGetService(serviceName);

    String address = StringUtils.trimToNull(currentService.getAddress());
    if (address != null) {
      request.portAddress = address;
    }

    final ru.codeinside.adm.database.InfoSystem infoSystem = currentService.getInfoSystem();
    request.packet.recipient = new InfoSystem(infoSystem.getCode(), infoSystem.getName());

    if (request.packet.sender == null) {
      request.packet.sender = getDefaultSender();
      if (request.packet.originator == null) {
        request.packet.originator = request.packet.sender;
      }
    }
  }

  public static InfoSystem getDefaultSender() {
    ru.codeinside.adm.database.InfoSystem sender = AdminServiceProvider.get().getMainInfoSystem();
    return new InfoSystem(sender.getCode(), sender.getName());
  }

  private static InfoSystemService validateAndGetService(String serviceName) {
    List<InfoSystemService> services = AdminServiceProvider.get().getInfoSystemServiceBySName(serviceName);
    if (services == null || services.isEmpty()) {
      throw new IllegalStateException("Нет модуля потребителя СМЭВ с именем '" + serviceName + "'");
    }
    return getServiceWithMaxVersion(services);
  }

  private static InfoSystemService getServiceWithMaxVersion(List<InfoSystemService> services) {
    InfoSystemService curService = null;
    for (InfoSystemService s : services) {
      if (curService == null) {
        curService = s;
      }
      if (s.getSversion().compareTo(curService.getSversion()) >= 0) {
        curService = s;
      }
    }
    return curService;
  }
}
