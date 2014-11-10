package net.mobidom.bp.beans;

import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import net.mobidom.bp.beans.request.DocumentRequest;

import org.activiti.engine.delegate.DelegateExecution;

import ru.codeinside.gses.beans.Smev;

@Named("documentsForRequestService")
@Singleton
public class DocumentsRequestingService {

  @Inject
  Smev smev;

  static Logger log = Logger.getLogger(DocumentsRequestingService.class.getName());

  public void requestDocuments(DelegateExecution execution) {
    List<DocumentRequest> requestingDocuments = (List<DocumentRequest>) execution.getVariable("documentRequests");
    for (DocumentRequest documentRequest : requestingDocuments) {
      if (documentRequest.getType() == DocumentType.SNILS) {
        String number = documentRequest.getParams().getNumber();
        execution.setVariableLocal("snils_number", number);
      }
    }
  }

}
