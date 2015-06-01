package ru.codeinside.gses.webui.form;

import org.osgi.framework.ServiceReference;
import ru.codeinside.gses.webui.osgi.Activator;
import ru.codeinside.gses.webui.wizard.ResultTransition;
import ru.codeinside.gses.webui.wizard.TransitionAction;
import ru.codeinside.gws.api.ClientProtocol;
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
    validateState();
    try {
      ClientProtocol clientProtocol = getClientProtocol();
      ByteArrayOutputStream normalizedBody = new ByteArrayOutputStream();
      SOAPMessage message = clientProtocol.createMessage(dataAccumulator.getClient().getWsdlUrl(),
          dataAccumulator.getClientRequest(), null, normalizedBody);
      dataAccumulator.setSoapMessage(message);
      return new ResultTransition(normalizedBody.toByteArray());
    } catch (RuntimeException e) {
      throw new IllegalStateException("Ошибка получения подготовительных данных: " + e.getMessage());
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

  public ClientProtocol getClientProtocol() {
    ServiceReference serviceReference = Activator.getContext().getServiceReference(ProtocolFactory.class.getName());
    ProtocolFactory protocolFactory = (ProtocolFactory) Activator.getContext().getService(serviceReference);
    ClientProtocol clientProtocol = protocolFactory.createClientProtocol(dataAccumulator.getClient().getRevision());
    Activator.getContext().ungetService(serviceReference);
    return clientProtocol;
  }
}
