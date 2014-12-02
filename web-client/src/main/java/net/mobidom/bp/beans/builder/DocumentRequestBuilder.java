package net.mobidom.bp.beans.builder;

import net.mobidom.bp.beans.Обращение;
import net.mobidom.bp.beans.СсылкаНаДокумент;
import net.mobidom.bp.beans.request.DocumentRequest;
import net.mobidom.bp.beans.types.DocumentTypesHelper;
import net.mobidom.bp.beans.types.ТипДокумента;

public class DocumentRequestBuilder {

  public static DocumentRequest createRequestForDocumentReference(СсылкаНаДокумент documentRef, Обращение обращение) {
    DocumentRequest request = new DocumentRequest();

    request.setDocRef(documentRef);
    request.setLabel(documentRef.getLabelString());

    ТипДокумента documentType = DocumentTypesHelper.defineDocumentTypeByReferenceType(documentRef);
    request.setType(documentType);

    return request;
  }
  
  public static DocumentRequest fillDocumentRequest(DocumentRequest documentRequest, Обращение обращение) {
    DocumentRequest request = new DocumentRequest();
    
    request.setLabel(documentRequest.getLabel());
    request.setType(documentRequest.getType()); 
    
    return request;
  }

}
