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
import ru.codeinside.gws.api.Enclosure;

import javax.xml.soap.SOAPMessage;
import java.io.ByteArrayOutputStream;
import java.util.Collections;

/**
 * Действие для получения SOAP-сообещения
 */
public class CreateSoapMessageAction implements TransitionAction {

  private final DataAccumulator dataAccumulator;

  public CreateSoapMessageAction(DataAccumulator dataAccumulator) {
    this.dataAccumulator = dataAccumulator;
  }

  @Override
  public ResultTransition doIt() throws IllegalStateException {
    ServiceReference reference = ProtocolUtils.getServiceReference(dataAccumulator.getServiceName(), Client.class);
    if (reference == null) {
      throw new IllegalStateException("Не удалось получить ссылку на сервис " + dataAccumulator.getServiceName());
    }

    final Client client = ProtocolUtils.getService(reference, Client.class);
    if (client == null) {
      throw new IllegalStateException("Клиент " + dataAccumulator.getServiceName() + " недоступен");
    }

    try {

      //если отсутствовал шаг подписания AppData, дёргаем потребителя и получаем request
      if (dataAccumulator.getClientRequest() == null) {
        clientRequest(client);
      }

      validateState();

      ClientProtocol clientProtocol = ProtocolUtils.getClientProtocol(client);
      ByteArrayOutputStream normalizedSignedInfo = new ByteArrayOutputStream();

      SOAPMessage message = clientProtocol.createMessage(client.getWsdlUrl(),
          dataAccumulator.getClientRequest(), null, normalizedSignedInfo);
      dataAccumulator.setSoapMessage(message);

      return new ResultTransition(new SignData(normalizedSignedInfo.toByteArray(), Collections.<Enclosure>emptyList()));
    } catch (RuntimeException e) {
      e.printStackTrace();
      throw new IllegalStateException("Ошибка получения подготовительных данных: " + e.getMessage(), e);
    } finally {
      Activator.getContext().ungetService(reference);
    }
  }

  private void clientRequest(Client client) {
    ClientRequest request = Fn.withEngine(new GetAppDataAction.GetClientRequest(), Flash.login(), dataAccumulator, client);
    dataAccumulator.setClientRequest(request);
  }

  private void validateState() {
    if (dataAccumulator.getClientRequest() == null) {
      throw new IllegalStateException("Отсутствуют данные клиента.");
    }
  }
}
