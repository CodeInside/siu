package net.mobidom.bp;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import net.mobidom.bp.beans.Document;
import net.mobidom.bp.beans.SnilsRef;
import net.mobidom.bp.beans.XmlContentWrapper;
import net.mobidom.bp.beans.request.DocumentRequest;
import net.mobidom.bp.beans.types.DocumentType;

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

      if (documentRequest.getType() == DocumentType.ДАННЫЕ_ЛИЦЕВОГО_СЧЕТА_ЗАСТРАХОВАННОГО_ЛИЦА) {

        String number = ((SnilsRef) documentRequest.getDocRef()).getNumber();
        execution.setVariableLocal("snils_number", number);

        smev.call(execution, "pfrf3815");

        if (execution.hasVariableLocal("snils_request_fault")) {
          Element fault = (Element) execution.getVariableLocal("snils_request_fault");
          documentRequest.setFault(fault);

        } else if (execution.hasVariableLocal("snils_request_result")) {
          Element result = (Element) execution.getVariableLocal("snils_request_result");

          Document document = new Document();
          document.setDocumentType(documentRequest.getType());
          XmlContentWrapper xmlContentWrapper = new XmlContentWrapper();
          xmlContentWrapper.setXmlContent(result);
          document.setXmlContent(xmlContentWrapper);

          documents.add(document);
        } else {
          // TODO webdom log info about resultless request
        }
      }

      if (documentRequest.getType() == DocumentType.ТРАНСКРИБИРОВАНИЕ_ФИГ) {
        Map<String, Object> params = documentRequest.getRequestParams();
        Map<String, Object> ctxParams = new HashMap<String, Object>();
        for (Entry<String, Object> param : params.entrySet()) {
          ctxParams.put(param.getKey(), String.valueOf(param.getValue()));
        }

        execution.setVariableLocal("transcrib_params", ctxParams);

        smev.call(execution, "midrf3894");

        Element result = (Element) execution.getVariableLocal("transcrib_request_result");

        Document document = new Document();
        document.setDocumentType(documentRequest.getType());
        XmlContentWrapper xmlContentWrapper = new XmlContentWrapper();
        xmlContentWrapper.setXmlContent(result);
        document.setXmlContent(xmlContentWrapper);

        documents.add(document);

      }

      documentRequestIt.remove();
    }
  }

}
