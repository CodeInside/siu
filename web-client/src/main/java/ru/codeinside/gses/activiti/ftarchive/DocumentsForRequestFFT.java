package ru.codeinside.gses.activiti.ftarchive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import net.mobidom.bp.beans.DocumentRef;
import net.mobidom.bp.beans.DocumentsForRequestService;
import net.mobidom.bp.beans.Request;
import net.mobidom.bp.beans.request.DocumentRequest;

import org.activiti.engine.ProcessEngine;

import ru.codeinside.gses.activiti.forms.api.definitions.PropertyNode;
import ru.codeinside.gses.activiti.forms.types.FieldType;
import ru.codeinside.gses.webui.Flash;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;

public class DocumentsForRequestFFT implements FieldType<String> {

  private static final long serialVersionUID = -5981529720741999886L;

  static Logger log = Logger.getLogger(DocumentsForRequestFFT.class.getName());

  Table requestingDocuments;
  Table documentForRequest;

  Map<Integer, Object> requestingDocumentsMap = new HashMap<Integer, Object>();
  Map<Integer, Object> documentForRequestMap = new HashMap<Integer, Object>();

  class RemoveRequestAction implements Button.ClickListener {

    @Override
    public void buttonClick(ClickEvent event) {

      Integer idx = (Integer) event.getButton().getData();
      requestingDocuments.removeItem(idx);
      requestingDocumentsMap.remove(idx);
    }
  }

  class AddRequestAction implements Button.ClickListener {

    @Override
    public void buttonClick(ClickEvent event) {

      Integer idx = (Integer) event.getButton().getData();
      Object data = documentForRequestMap.get(idx);

      if (data instanceof DocumentRef) {

        event.getButton().setEnabled(false);

        DocumentRef ref = (DocumentRef) data;
        Integer nextIdx = requestingDocuments.size() + 1;

        DocumentRequest request = new DocumentRequest();
        // TODO define actual type
        // request.setType(type);

        request.setCustomData(ref.getLabelString());

        Label label = new Label(ref.getLabelString());
        Button button = new Button("Удалить");
        button.setData(nextIdx);
        button.addListener(new RemoveRequestAction());
        requestingDocuments.addItem(new Object[] { label, button }, nextIdx);
        requestingDocumentsMap.put(nextIdx, request);

      } else if (data instanceof DocumentRequest) {
        DocumentRequest request = (DocumentRequest) data;
        Integer nextIdx = requestingDocuments.size() + 1;

        Label label = new Label(request.getType());
        Button button = new Button("Удалить");
        button.setData(nextIdx);
        button.addListener(new RemoveRequestAction());
        requestingDocuments.addItem(new Object[] { label, button }, nextIdx);
        requestingDocumentsMap.put(nextIdx, request);
      }
    }
  }

  @Override
  public Field createField(String taskId, String fieldId, String name, String value, PropertyNode node, boolean archive) {

    ProcessEngine processEngine = Flash.flash().getProcessEngine();

    log.info("processEngine = " + processEngine);

    Request request = (Request) processEngine.getRuntimeService().getVariable(value, "request");

    List<DocumentRequest> documentRequests = null;
    if (processEngine.getRuntimeService().getVariable(value, "documentRequests") != null) {
      documentRequests = (List<DocumentRequest>) processEngine.getRuntimeService().getVariable(value, "request");
    } else {
      documentRequests = new ArrayList<DocumentRequest>();
    }

    List<DocumentRef> documentRefs = request.getDocumentRefs();
    List<DocumentRequest> defDocumentRequests = DocumentsForRequestService.getDefaultDocumentRequests();

    Form form = new Form();

    // типы документы которые будут запрашиваться
    requestingDocuments = new Table();
    requestingDocuments.addContainerProperty("Документ", Label.class, null);
    requestingDocuments.addContainerProperty("Отмена", Button.class, null);

    // типы документов которые можно запросить = (список из типов документов,
    // которые можно запросить + ссылки на документы)
    documentForRequest = new Table();
    documentForRequest.addContainerProperty("Документ", Label.class, null);
    documentForRequest.addContainerProperty("Запросить", Component.class, null);

    for (int i = 0; i < documentRefs.size(); i++) {

      Integer idx = new Integer(i);

      DocumentRef docRef = documentRefs.get(i);

      Label label = new Label(docRef.getLabelString());
      label.setContentMode(Label.CONTENT_PREFORMATTED);

      Button button = new Button("Запросить");
      button.addListener(new AddRequestAction());
      button.setData(idx);
      documentForRequest.addItem(new Object[] { label, button }, idx);
      documentForRequestMap.put(idx, docRef);
    }

    for (int i = 0; i < defDocumentRequests.size(); i++) {

      Integer idx = new Integer(i + documentRefs.size());

      DocumentRequest docReq = defDocumentRequests.get(i);

      Label label = new Label(docReq.getType());
      label.setContentMode(Label.CONTENT_PREFORMATTED);

      Button button = new Button("Запросить");
      button.addListener(new AddRequestAction());
      button.setData(idx);
      documentForRequest.addItem(new Object[] { label, button }, idx);
      documentForRequestMap.put(idx, docReq);
    }

    documentForRequest.setColumnWidth(documentForRequest.getVisibleColumns()[0], 300);
    documentForRequest.setColumnWidth(documentForRequest.getVisibleColumns()[1], 100);

    form.getLayout().addComponent(documentForRequest);

    requestingDocuments.setColumnWidth(requestingDocuments.getVisibleColumns()[0], 300);
    requestingDocuments.setColumnWidth(requestingDocuments.getVisibleColumns()[1], 100);

    form.getLayout().addComponent(requestingDocuments);

    Button requestButton = new Button("Запросить");
    requestButton.addListener(new Button.ClickListener() {

      @Override
      public void buttonClick(ClickEvent event) {
        log.info("user want to request documents");
      }
    });

    form.getLayout().addComponent(requestButton);

    return form;
  }
}
