package net.mobidom.bp.beans.types;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import net.mobidom.bp.beans.request.DocumentRequest;

import com.vaadin.ui.Form;

public class RequestForm {

  static Logger log = Logger.getLogger(RequestForm.class.getName());

  public DocumentRequest documentRequest;
  public Form form;

  public RequestForm() {
  }

  public RequestForm(DocumentRequest documentRequest, Form form) {
    super();
    this.documentRequest = documentRequest;
    this.form = form;
  }

  public void accept() {
    log.info("accept from values");

    Map<String, Object> params = new HashMap<String, Object>();
    form.commit();

    Collection<?> propIds = form.getItemDataSource().getItemPropertyIds();
    Iterator<?> propIt = propIds.iterator();
    while (propIt.hasNext()) {
      String propId = (String) propIt.next();
      Object propValue = form.getItemProperty(propId).getValue();
      if (propValue instanceof Date) {
        params.put(propId, propValue);
      } else {
        params.put(propId, String.valueOf(propValue));
      }
    }

    documentRequest.setRequestParams(params);

  }

}
