package ru.codeinside.gses.webui.form;

import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.database.InfoSystemService;
import ru.codeinside.gses.webui.wizard.ResultTransition;
import ru.codeinside.gses.webui.wizard.TransitionAction;
import ru.codeinside.gws.api.Client;
import ru.codeinside.gws.api.ClientRequest;

import java.util.List;

public class GetAppDataAction implements TransitionAction {
  private String serviceName;
  GetAppDataAction(String serviceName) {
    this.serviceName = serviceName;
  }
  /**
   * Выполнить действие перехода.
   */
  @Override
  public ResultTransition doIt() throws IllegalStateException {
    InfoSystemService service = validateAndGetService(serviceName);
    Client client = AdminServiceProvider.get().getClientRefByNameAndVersion(service.getName(), service.getSversion()).getRef();

    ClientRequest request = client.createClientRequest(null); //TODO получить ExchangeContext
    return new ResultTransition(request.appData);
  }

  private InfoSystemService getServiceWithMaxVersion(List<InfoSystemService> services) {
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

  private InfoSystemService validateAndGetService(String serviceName) {
    List<InfoSystemService> services = AdminServiceProvider.get().getInfoSystemServiceBySName(serviceName);
    if (services == null || services.isEmpty()) {
      throw new IllegalStateException("Нет модуля потребителя СМЭВ с именем '" + serviceName + "'");
    }
    return getServiceWithMaxVersion(services);
  }

}
