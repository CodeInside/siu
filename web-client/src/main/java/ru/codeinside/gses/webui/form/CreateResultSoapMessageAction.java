package ru.codeinside.gses.webui.form;

import ru.codeinside.gses.webui.wizard.ResultTransition;
import ru.codeinside.gses.webui.wizard.TransitionAction;
import ru.codeinside.gws.api.ServerProtocol;

import java.io.ByteArrayOutputStream;

public class CreateResultSoapMessageAction implements TransitionAction {

  private final DataAccumulator dataAccumulator;

  public CreateResultSoapMessageAction(DataAccumulator dataAccumulator) {
    this.dataAccumulator = dataAccumulator;
  }

  @Override
  public ResultTransition doIt() throws IllegalStateException {
    validateState();

    ServerProtocol serverProtocol = FormSeqUtils.getServerProtocol(dataAccumulator.getServer());
    ByteArrayOutputStream normalizedBody = new ByteArrayOutputStream();

    //TODO create SOAPMessage
//    serverProtocol.createMessage(...);

    throw new IllegalStateException("FIN");
  }

  private void validateState() {
    if (dataAccumulator.getServer() == null) {
      throw new IllegalStateException("Отсутствует сервис для построения запроса.");
    }
    if (dataAccumulator.getServerResponse() == null) {
      throw new IllegalStateException("Отсутствуют данные сервиса.");
    }
  }
}
