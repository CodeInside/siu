package net.mobidom.bp.beans;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import net.mobidom.bp.beans.request.DocumentRequest;

import org.activiti.engine.delegate.DelegateExecution;
import org.w3c.dom.Element;

import ru.codeinside.gses.beans.Smev;

@Named("documentsRequestingService")
@Singleton
public class DocumentsRequestingService {

  @Inject
  Smev smev;

  static Logger log = Logger.getLogger(DocumentsRequestingService.class.getName());

  public void requestDocuments(DelegateExecution execution) {
    List<DocumentRequest> requestingDocuments = (List<DocumentRequest>) execution.getVariable("documentRequests");

    List<Document> documents = (List<Document>) execution.getVariable("documents");

    Iterator<DocumentRequest> documentRequestIt = requestingDocuments.iterator();

    while (documentRequestIt.hasNext()) {

      DocumentRequest documentRequest = documentRequestIt.next();

      if (documentRequest.getType() == DocumentType.СНИЛС) {

        String number = ((SnilsRef) documentRequest.getDocRef()).getNumber();
        execution.setVariableLocal("snils_number", number);

        smev.call(execution, "pfrf3815");

        if (execution.hasVariableLocal("snils_request_fault")) {
          Element fault = (Element) execution.getVariableLocal("snils_request_fault");
          documentRequest.setFault(fault);

        } else if (execution.hasVariableLocal("snils_request_result")) {
          Element result = (Element) execution.getVariableLocal("snils_request_result");

          Document document = new Document();
          document.setType(documentRequest.getType().name());
          XmlContentWrapper xmlContentWrapper = new XmlContentWrapper();
          xmlContentWrapper.setXmlContent(result);
          document.setXmlContent(xmlContentWrapper);

          documents.add(document);
        } else {
          // TODO webdom log info about resultless request
        }

      }

    }

  }

}
