package net.mobidom.bp.beans.types;

import net.mobidom.bp.beans.request.DocumentRequest;

import com.vaadin.ui.Form;

public class FromDocumentRequestBuilder {

  public static Form createForm(final DocumentRequest documentRequest) {
    
    if(documentRequest.getType() == ТипДокумента.НДФЛ_2) {
      Form form = new Form();
    }
    
    // TODO webdom
    return null;
  }

}
