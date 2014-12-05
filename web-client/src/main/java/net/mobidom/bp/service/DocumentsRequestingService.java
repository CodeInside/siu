package net.mobidom.bp.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import net.mobidom.bp.beans.Документ;
import net.mobidom.bp.beans.request.DocumentRequest;
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
    }
  };

  @Inject
  Smev smev;

  static Logger log = Logger.getLogger(DocumentsRequestingService.class.getName());

  @SuppressWarnings("unchecked")
  public void requestDocuments(DelegateExecution execution) {
    List<DocumentRequest> requestingDocuments = (List<DocumentRequest>) execution.getVariable("documentRequests");

    List<Документ> documents = (List<Документ>) execution.getVariable("documents");

    Iterator<DocumentRequest> documentRequestIt = requestingDocuments.iterator();

    while (documentRequestIt.hasNext()) {

      DocumentRequest documentRequest = documentRequestIt.next();
      ТипДокумента типДокумента = documentRequest.getType();

      String serviceId = CLIENT_IDS.get(типДокумента);

      if (serviceId != null && !serviceId.isEmpty()) {
        execution.setVariable(REQUEST_OBJECT_KEY, documentRequest);
        smev.call(execution, serviceId);

        if (documentRequest.getFault() != null) {
          // TODO webdom
        } else if (documentRequest.getДокумент() != null) {
          // DocumentRequest completed
          documents.add(documentRequest.getДокумент());
          documentRequestIt.remove();
        }

      } else {
        throw new IllegalStateException("not defined serviceId");
      }
    }

    execution.removeVariable(REQUEST_OBJECT_KEY);

  }
}
