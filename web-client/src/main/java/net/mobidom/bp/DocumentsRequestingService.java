package net.mobidom.bp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import net.mobidom.bp.beans.XmlContentWrapper;
import net.mobidom.bp.beans.Документ;
import net.mobidom.bp.beans.СНИЛС;
import net.mobidom.bp.beans.request.DocumentRequest;
import net.mobidom.bp.beans.types.ТипДокумента;

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

    List<Документ> documents = (List<Документ>) execution.getVariable("documents");

    Iterator<DocumentRequest> documentRequestIt = requestingDocuments.iterator();

    while (documentRequestIt.hasNext()) {

      DocumentRequest documentRequest = documentRequestIt.next();

      if (documentRequest.getType() == ТипДокумента.ДАННЫЕ_ЛИЦЕВОГО_СЧЕТА_ЗАСТРАХОВАННОГО_ЛИЦА) {

        String number = ((СНИЛС) documentRequest.getDocRef()).getНомер();
        execution.setVariableLocal("snils_number", number);

        smev.call(execution, "pfrf3815");

        if (execution.hasVariableLocal("snils_request_fault")) {
          Element fault = (Element) execution.getVariableLocal("snils_request_fault");
          documentRequest.setFault(fault);

        } else if (execution.hasVariableLocal("snils_request_result")) {
          Element result = (Element) execution.getVariableLocal("snils_request_result");

          Документ document = new Документ();
          document.setDocumentType(documentRequest.getType());
          XmlContentWrapper xmlContentWrapper = new XmlContentWrapper();
          xmlContentWrapper.setXmlContent(result);
          document.setXmlContent(xmlContentWrapper);

          documents.add(document);
        } else {
          // TODO webdom log info about resultless request
        }
      }

      if (documentRequest.getType() == ТипДокумента.ТРАНСКРИБИРОВАНИЕ_ФИГ) {
        
        Map<String, Object> params = documentRequest.getRequestParams();
        Map<String, Object> ctxParams = new HashMap<String, Object>();
        for (Entry<String, Object> param : params.entrySet()) {
          ctxParams.put(param.getKey(), String.valueOf(param.getValue()));
        }

        execution.setVariableLocal("transcrib_params", ctxParams);

        smev.call(execution, "midrf3894");

        Element result = (Element) execution.getVariableLocal("transcrib_request_result");

        Документ document = new Документ();
        document.setDocumentType(documentRequest.getType());
        XmlContentWrapper xmlContentWrapper = new XmlContentWrapper();
        xmlContentWrapper.setXmlContent(result);
        document.setXmlContent(xmlContentWrapper);

        documents.add(document);

      }

      if (documentRequest.getType() == ТипДокумента.СНИЛС) {
        Map<String, Object> params = documentRequest.getRequestParams();
        Map<String, String> ctxParams = new HashMap<String, String>();
        for (Entry<String, Object> param : params.entrySet()) {
          if (param.getValue() instanceof Date) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            ctxParams.put(param.getKey(), sdf.format(param.getValue()));
          } else {
            ctxParams.put(param.getKey(), String.valueOf(param.getValue()));
          }
        }
        execution.setVariableLocal("snilsbydata_params", ctxParams);

        smev.call(execution, "pfrf3814");

        if (execution.hasVariableLocal("snilsbydata_request_fault")) {
          
          Element fault = (Element) execution.getVariableLocal("snilsbydata_request_fault");
          documentRequest.setFault(fault);
          
          // TODO webdom fix it          
          Документ document = new Документ();
          document.setDocumentType(documentRequest.getType());
          XmlContentWrapper xmlContentWrapper = new XmlContentWrapper();
          xmlContentWrapper.setXmlContent(fault);
          document.setXmlContent(xmlContentWrapper);

          documents.add(document);

        } else if (execution.hasVariableLocal("snilsbydata_request_result")) {
          
          Element result = (Element) execution.getVariableLocal("snilsbydata_request_result");
          
          Документ document = new Документ();
          document.setDocumentType(documentRequest.getType());
          XmlContentWrapper xmlContentWrapper = new XmlContentWrapper();
          xmlContentWrapper.setXmlContent(result);
          document.setXmlContent(xmlContentWrapper);

          documents.add(document);
        }
      } 
      
      if (documentRequest.getType() == ТипДокумента.НДФЛ_2) {
        smev.call(execution, "fns3777");
      }
      
      if (documentRequest.getType() == ТипДокумента.КАДАСТРОВЫЙ_ПАСПОРТ_ОБЪЕКТА_НЕДВИЖИМОСТИ) {
        smev.call(execution, "gws3564c");
      }

      documentRequestIt.remove();
    }

  }

}
