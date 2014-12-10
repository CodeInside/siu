package net.mobidom.bp.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import net.mobidom.bp.beans.Документ;
import net.mobidom.bp.beans.request.DocumentRequest;
import net.mobidom.bp.beans.request.DocumentRequestType;
import net.mobidom.bp.beans.request.ResponseType;
import net.mobidom.bp.beans.types.ТипДокумента;

import org.activiti.engine.delegate.DelegateExecution;

import ru.codeinside.gses.beans.Smev;

@Named("documentsRequestingService")
@Singleton
public class DocumentsRequestingService {

  static final String REQUEST_OBJECT_KEY = "REQUEST_OBJECT";

  static final Map<ТипДокумента, String> CLIENT_IDS = new HashMap<ТипДокумента, String>() {
    private static final long serialVersionUID = 940475202788077703L;

    {
      put(ТипДокумента.НДФЛ_2, "fns3777");
      put(ТипДокумента.СНИЛС, "pfrf3814");
      put(ТипДокумента.ДАННЫЕ_ЛИЦЕВОГО_СЧЕТА_ЗАСТРАХОВАННОГО_ЛИЦА, "pfrf3815");
      put(ТипДокумента.ТРАНСКРИБИРОВАНИЕ_ФИГ, "midrf3894");
      put(ТипДокумента.ИНН, "fns3626");
      put(ТипДокумента.СВЕДЕНИЯ_О_ЗАРАБОТНОЙ_ПЛАТЕ_ИНЫХ_ВЫПЛАТАХ_И_ВОЗНАГРАЖДЕНИЯХ_ЗАСТРАХОВАННОГО_ЛИЦА, "pfrf3622");
    }
  };

  @Inject
  Smev smev;

  static Logger log = Logger.getLogger(DocumentsRequestingService.class.getName());

  @SuppressWarnings("unchecked")
  public void requestDocuments(DelegateExecution execution) {
    List<DocumentRequest> documentRequests = (List<DocumentRequest>) execution.getVariable("documentRequests");

    List<DocumentRequest> curDocReqs = new ArrayList<DocumentRequest>(documentRequests);
    List<Документ> documents = (List<Документ>) execution.getVariable("documents");
    for (DocumentRequest request : curDocReqs) {

      if (request.getRequestType() == null) {
        request.setRequestType(DocumentRequestType.ЗАПРОС_ДОКУМЕНТА);
      }

      ТипДокумента типДокумента = request.getType();
      String serviceId = CLIENT_IDS.get(типДокумента);

      if (serviceId != null && !serviceId.isEmpty()) {
        try {
          execution.setVariable(REQUEST_OBJECT_KEY, request);
          smev.call(execution, serviceId);

          ResponseType responseType = request.getResponseType();

          if (responseType == ResponseType.DATA_ERROR || responseType == ResponseType.SYSTEM_ERROR) {
            // TODO webdom show error
            log.log(Level.SEVERE, "fault: " + request.getFault());
            break;
          } else if (responseType == ResponseType.RESULT) {
            // DocumentRequest completed
            request.getДокумент().setDocumentRequest(request);
            documents.add(request.getДокумент());
            documentRequests.remove(request);
          } else if (responseType == ResponseType.DATA_NOT_FOUND) {
            // mark as data_not_found => edit request data
          }

        } catch (Exception e) {
          request.setResponseType(ResponseType.SYSTEM_ERROR);
          request.setFault(String.format("Произошла ошибка при получении документа '%s':\n%s", типДокумента, e.getMessage()));
        }

      } else {
        request.setResponseType(ResponseType.SYSTEM_ERROR);
        request.setFault(String.format("Клиент сервиса для получения документа '%s' не зарегистрирован", типДокумента));
      }
    }

    execution.removeVariable(REQUEST_OBJECT_KEY);

  }
}
