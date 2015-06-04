package ru.codeinside.gses.webui.form;

import org.osgi.framework.ServiceReference;
import ru.codeinside.gses.service.Fn;
import ru.codeinside.gses.webui.Flash;
import ru.codeinside.gses.webui.osgi.Activator;
import ru.codeinside.gses.webui.wizard.ResultTransition;
import ru.codeinside.gses.webui.wizard.TransitionAction;
import ru.codeinside.gws.api.Client;
import ru.codeinside.gws.api.ClientProtocol;
import ru.codeinside.gws.api.ClientRequest;
import ru.codeinside.gws.api.ProtocolFactory;

import javax.xml.soap.SOAPMessage;
import java.io.ByteArrayOutputStream;

/**
 * Действие для получения SOAP-сообещения
 */
public class CreateSoapMessageAction implements TransitionAction {

  private final DataAccumulator dataAccumulator;

  public CreateSoapMessageAction(DataAccumulator dataAccumulator) {
    this.dataAccumulator = dataAccumulator;
  }

  /**
   * Выполнить действие перехода.
   */
  @Override
  public ResultTransition doIt() throws IllegalStateException {
    try {
      //если отсутствовал шаг подписания AppData, дёргаем потребителя и получаем request
      if (dataAccumulator.getClientRequest() == null) {
        clientRequest();
      }

      validateState();

      ClientProtocol clientProtocol = getClientProtocol();
      ByteArrayOutputStream normalizedBody = new ByteArrayOutputStream();

      SOAPMessage message = clientProtocol.createMessage(dataAccumulator.getClient().getWsdlUrl(),
          dataAccumulator.getClientRequest(), null, normalizedBody);
      dataAccumulator.setSoapMessage(message);

      dataAccumulator.setRequestId(0L);// это нужно, что б была ссылка, значение установится при подписании

      return new ResultTransition(normalizedBody.toByteArray());
    } catch (RuntimeException e) {
      e.printStackTrace();
      throw new IllegalStateException("Ошибка получения подготовительных данных: " + e.getMessage());
    }
  }

  public ClientProtocol getClientProtocol() {
    ServiceReference serviceReference = Activator.getContext().getServiceReference(ProtocolFactory.class.getName());
    ProtocolFactory protocolFactory = (ProtocolFactory) Activator.getContext().getService(serviceReference);
    ClientProtocol clientProtocol = protocolFactory.createClientProtocol(dataAccumulator.getClient().getRevision());
    Activator.getContext().ungetService(serviceReference);
    return clientProtocol;
  }

  private void clientRequest() {
    final ServiceReference reference = GetAppDataAction.getServiceReference(dataAccumulator.getServiceName());
    final Client client = GetAppDataAction.getClient(reference);
    dataAccumulator.setClient(client);
    try {
      ClientRequest request = Fn.withEngine(new GetAppDataAction.GetClientRequest(), Flash.login(), dataAccumulator);
      dataAccumulator.setClientRequest(request);
    } finally {
      Activator.getContext().ungetService(reference);
    }
  }

  private void validateState() {
    if (dataAccumulator.getClient() == null) {
      throw new IllegalStateException("Отсутствует клиент для построения запроса.");
    }
    if (dataAccumulator.getClientRequest() == null) {
      throw new IllegalStateException("Отсутствуют данные клиента.");
    }
  }
}
