package net.mobidom.bp;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import net.mobidom.bp.beans.DocumentType;
import net.mobidom.bp.beans.request.DocumentRequest;

@Named("documentsForRequestService")
@Singleton
public class DocumentsForRequestService {

  public static List<DocumentRequest> getDefaultDocumentRequests() {
    List<DocumentRequest> list = new ArrayList<DocumentRequest>();

    DocumentRequest docReq = new DocumentRequest();
    docReq.setLabel("2-НДФЛ");
    docReq.setType(DocumentType.NDFL_2);
    list.add(docReq);

    docReq = new DocumentRequest();
    docReq.setLabel("3-НДФЛ");
    docReq.setType(DocumentType.NDFL_3);
    list.add(docReq);

    docReq = new DocumentRequest();
    docReq.setLabel("Выписка из лесной декларации");
    docReq.setType(DocumentType.UNKNOWN);
    list.add(docReq);

    docReq = new DocumentRequest();
    docReq.setLabel("Данные лицевого счета застрахованного лица");
    docReq.setType(DocumentType.СНИЛС);
    list.add(docReq);

    docReq = new DocumentRequest();
    docReq.setLabel("Задолженности по уплате налогов");
    docReq.setType(DocumentType.UNKNOWN);
    list.add(docReq);

    docReq = new DocumentRequest();
    docReq.setLabel("Транскрибирование ФИГ");
    docReq.setType(DocumentType.ТРАНСКРИБИРОВАНИЕ_ФИГ);
    // only for test
    docReq.addRequestParam("LAT_SURNAME", "LABBEE");
    docReq.addRequestParam("LAT_FIRSTNAME", "GABRIELLE CECILIA");
    docReq.addRequestParam("COUNTRY_CODE", "USA");
    list.add(docReq);

    return list;
  }

}
