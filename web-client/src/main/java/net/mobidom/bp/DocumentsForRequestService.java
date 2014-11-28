package net.mobidom.bp;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import net.mobidom.bp.beans.request.DocumentRequest;
import net.mobidom.bp.beans.types.ТипДокумента;

@Named("documentsForRequestService")
@Singleton
public class DocumentsForRequestService {

  public static List<DocumentRequest> getDefaultDocumentRequests() {
    List<DocumentRequest> list = new ArrayList<DocumentRequest>();

    DocumentRequest docReq = new DocumentRequest();
    docReq.setLabel("2-НДФЛ");
    docReq.setType(ТипДокумента.НДФЛ_2);
    list.add(docReq);

    docReq = new DocumentRequest();
    docReq.setLabel("3-НДФЛ");
    docReq.setType(ТипДокумента.НДФЛ_3);
    list.add(docReq);

    docReq = new DocumentRequest();
    docReq.setLabel("Данные лицевого счета застрахованного лица");
    docReq.setType(ТипДокумента.ДАННЫЕ_ЛИЦЕВОГО_СЧЕТА_ЗАСТРАХОВАННОГО_ЛИЦА);
    list.add(docReq);

    docReq = new DocumentRequest();
    docReq.setLabel("Задолженности по уплате налогов");
    docReq.setType(ТипДокумента.UNKNOWN);
    list.add(docReq);

    docReq = new DocumentRequest();
    docReq.setLabel("Транскрибирование ФИГ");
    docReq.setType(ТипДокумента.ТРАНСКРИБИРОВАНИЕ_ФИГ);
    // only for test
    docReq.addRequestParam("LAT_SURNAME", "LABBEE");
    docReq.addRequestParam("LAT_FIRSTNAME", "GABRIELLE CECILIA");
    docReq.addRequestParam("COUNTRY_CODE", "USA");
    list.add(docReq);
    
    docReq = new DocumentRequest();
    docReq.setLabel("СНИЛС Заявителя");
    docReq.setType(ТипДокумента.СНИЛС);
    list.add(docReq);
    
    docReq = new DocumentRequest();
    docReq.setLabel("Кадастровый паспорт");
    docReq.setType(ТипДокумента.КАДАСТРОВЫЙ_ПАСПОРТ_ОБЪЕКТА_НЕДВИЖИМОСТИ);
    list.add(docReq);
        

    return list;
  }

}
