package net.mobidom.bp.beans;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import net.mobidom.bp.beans.request.DocumentRequest;

@Named("documentsForRequestService")
@Singleton
public class DocumentsForRequestService {

  public static List<DocumentRequest> getDefaultDocumentRequests() {
    List<DocumentRequest> list = new ArrayList<DocumentRequest>();

    DocumentRequest ref = new DocumentRequest();
    ref.setType("2-НДФЛ");
    list.add(ref);
    
    ref = new DocumentRequest();
    ref.setType("3-НДФЛ");
    list.add(ref);
    
    ref = new DocumentRequest();
    ref.setType("Выписка из лесной декларации");
    list.add(ref);
    
    ref = new DocumentRequest();
    ref.setType("Данные лицевого счета застрахованного лица");
    list.add(ref);
    
    ref = new DocumentRequest();
    ref.setType("Задолженности по уплате налогов");
    list.add(ref);

    return list;
  }

}
