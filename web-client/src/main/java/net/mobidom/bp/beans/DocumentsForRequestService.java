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
    ref.setLabel("2-НДФЛ");
    ref.setType(DocumentType.NDFL_2);
    list.add(ref);

    ref = new DocumentRequest();
    ref.setLabel("3-НДФЛ");
    ref.setType(DocumentType.NDFL_3);
    list.add(ref);

    ref = new DocumentRequest();
    ref.setLabel("Выписка из лесной декларации");
    ref.setType(DocumentType.UNKNOWN);
    list.add(ref);

    ref = new DocumentRequest();
    ref.setLabel("Данные лицевого счета застрахованного лица");
    ref.setType(DocumentType.СНИЛС);
    list.add(ref);

    ref = new DocumentRequest();
    ref.setLabel("Задолженности по уплате налогов");
    ref.setType(DocumentType.UNKNOWN);
    list.add(ref);

    return list;
  }

}
