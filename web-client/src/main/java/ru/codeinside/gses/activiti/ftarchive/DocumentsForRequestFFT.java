package ru.codeinside.gses.activiti.ftarchive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import net.mobidom.bp.beans.Обращение;
import net.mobidom.bp.beans.СсылкаНаДокумент;
import net.mobidom.bp.beans.builder.DocumentRequestBuilder;
import net.mobidom.bp.beans.form.FromDocumentRequestBuilder;
import net.mobidom.bp.beans.form.RequestForm;
import net.mobidom.bp.beans.request.DocumentRequest;
import net.mobidom.bp.service.DocumentsForRequestService;

import org.activiti.engine.ProcessEngine;

import ru.codeinside.gses.activiti.forms.api.definitions.PropertyNode;
import ru.codeinside.gses.activiti.forms.types.FieldType;
import ru.codeinside.gses.webui.Flash;

import com.vaadin.data.Property;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class DocumentsForRequestFFT implements FieldType<String> {

  private static final long serialVersionUID = -5981529720741999886L;

  static Logger log = Logger.getLogger(DocumentsForRequestFFT.class.getName());

  Table requestsTable;
  Table requestTemplatesTable;

  Map<Integer, Object> requestsMap = new HashMap<Integer, Object>();
  Map<Integer, Object> requestTemplatesMap = new HashMap<Integer, Object>();

  String pid;
  Обращение mainRequest;

  private void updateDocumentRequestsInProcessContext() {
    List<DocumentRequest> documentRequests = new ArrayList<DocumentRequest>();
    for (Object tdata : requestsMap.values()) {
      documentRequests.add((DocumentRequest) tdata);
    }

    Flash.flash().getProcessEngine().getRuntimeService().setVariable(pid, "documentRequests", documentRequests);
  }

  static interface RequestFormCompleted {
    void onSubmit(boolean submit);
  }

  private class RemoveRequestAction implements Button.ClickListener {

    private static final long serialVersionUID = -6567624423008258723L;

    @Override
    public void buttonClick(ClickEvent event) {

      Integer idx = (Integer) event.getButton().getData();
      requestsTable.removeItem(idx);
      DocumentRequest request = (DocumentRequest) requestsMap.remove(idx);

      if (request.getDocRef() != null) {
        СсылкаНаДокумент documentRef = request.getDocRef();
        for (Entry<Integer, Object> en : requestTemplatesMap.entrySet()) {
          if (en.getValue() == documentRef) {
            Property prop = requestTemplatesTable.getItem(en.getKey()).getItemProperty("Запросить");
            if (prop.getValue() instanceof Button) {
              ((Button) prop.getValue()).setEnabled(true);
            }
            break;
          }
        }
      }

      updateDocumentRequestsInProcessContext();
    }
  }

  private class AddRequestAction implements Button.ClickListener {

    private static final long serialVersionUID = -9006594502921784978L;

    @Override
    public void buttonClick(ClickEvent event) {

      Integer idx = (Integer) event.getButton().getData();
      Object data = requestTemplatesMap.get(idx);

      final DocumentRequest request;
      if (data instanceof СсылкаНаДокумент) {

        event.getButton().setEnabled(false);

        СсылкаНаДокумент documentRef = (СсылкаНаДокумент) data;
        request = DocumentRequestBuilder.createRequestForDocumentReference(documentRef, mainRequest);

        addRequestToTable(request);
        updateDocumentRequestsInProcessContext();

      } else if (data instanceof DocumentRequest) {

        DocumentRequest baseRequest = (DocumentRequest) data;
        request = DocumentRequestBuilder.fillDocumentRequest(baseRequest, mainRequest);

        showRequestFormWindow(event.getButton().getWindow(), request, new RequestFormCompleted() {

          @Override
          public void onSubmit(boolean submit) {
            if (submit) {
              addRequestToTable(request);
              updateDocumentRequestsInProcessContext();
            } else {
              log.info("no need to add request");
            }
          }
        });
      }
    }

    private void addRequestToTable(DocumentRequest request) {
      Integer nextIdx = requestsTable.size() + 1;
      Label label = new Label(request.getLabel());
      Button button = new Button("Удалить");
      button.setData(nextIdx);
      button.addListener(new RemoveRequestAction());
      requestsTable.addItem(new Object[] { label, button }, nextIdx);
      requestsMap.put(nextIdx, request);
    }
  }

  private void showRequestFormWindow(Window parentWindow, DocumentRequest documentRequest, final RequestFormCompleted listener) {

    final RequestForm requestForm = FromDocumentRequestBuilder.createForm(documentRequest);

    VerticalLayout vLayout = new VerticalLayout();
    vLayout.addComponent(requestForm.form);

    HorizontalLayout hLayout = new HorizontalLayout();

    Button acceptButton = new Button("Принять");
    acceptButton.addListener(new Button.ClickListener() {

      @Override
      public void buttonClick(ClickEvent event) {
        requestForm.accept();
        listener.onSubmit(true);
      }
    });

    hLayout.addComponent(acceptButton);

    Button cancelButton = new Button("Отменить");
    cancelButton.addListener(new Button.ClickListener() {

      @Override
      public void buttonClick(ClickEvent event) {
        listener.onSubmit(false);
      }
    });
    hLayout.addComponent(cancelButton);

    vLayout.addComponent(hLayout);

    Window newWindow = new Window();
    newWindow.setWidth(800 + 50, Sizeable.UNITS_PIXELS);
    newWindow.setHeight(600 + 100, Sizeable.UNITS_PIXELS);
    newWindow.center();
    newWindow.setContent(vLayout);
    newWindow.setCaption(documentRequest.getLabel());
    newWindow.setResizable(false);

    parentWindow.addWindow(newWindow);
  }

  @SuppressWarnings("unchecked")
  @Override
  public Field createField(String taskId, String fieldId, String name, final String pid, PropertyNode node, boolean archive) {

    this.pid = pid;

    ProcessEngine processEngine = Flash.flash().getProcessEngine();

    log.info("processEngine = " + processEngine);

    mainRequest = (Обращение) processEngine.getRuntimeService().getVariable(pid, "request");

    List<DocumentRequest> documentRequests = null;
    if (processEngine.getRuntimeService().getVariable(pid, "documentRequests") != null) {
      documentRequests = (List<DocumentRequest>) processEngine.getRuntimeService().getVariable(pid, "documentRequests");
    } else {
      documentRequests = new ArrayList<DocumentRequest>();
      processEngine.getRuntimeService().setVariable(pid, "documentRequests", documentRequests);
    }

    List<СсылкаНаДокумент> documentRefs = mainRequest.getСсылкиНаДокументы();
    List<DocumentRequest> defDocumentRequests = DocumentsForRequestService.getDefaultDocumentRequests();

    Form form = new Form();

    // типы документы которые будут запрашиваться
    requestsTable = new Table();
    requestsTable.addContainerProperty("Документ", Label.class, null);
    requestsTable.addContainerProperty("Отмена", Button.class, null);

    // типы документов которые можно запросить = (список из типов документов,
    // которые можно запросить + ссылки на документы)
    requestTemplatesTable = new Table();
    requestTemplatesTable.addContainerProperty("Документ", Label.class, null);
    requestTemplatesTable.addContainerProperty("Запросить", Component.class, null);

    for (int i = 0; i < documentRefs.size(); i++) {

      Integer idx = new Integer(i);

      СсылкаНаДокумент docRef = documentRefs.get(i);

      Label label = new Label(docRef.getLabelString());
      label.setContentMode(Label.CONTENT_PREFORMATTED);

      Button button = new Button("Запросить");
      button.addListener(new AddRequestAction());
      button.setData(idx);
      requestTemplatesTable.addItem(new Object[] { label, button }, idx);
      requestTemplatesMap.put(idx, docRef);
    }

    for (int i = 0; i < defDocumentRequests.size(); i++) {

      Integer idx = new Integer(i + documentRefs.size());

      DocumentRequest docReq = defDocumentRequests.get(i);

      Label label = new Label(docReq.getLabel());
      label.setContentMode(Label.CONTENT_PREFORMATTED);

      Button button = new Button("Запросить");
      button.addListener(new AddRequestAction());
      button.setData(idx);
      requestTemplatesTable.addItem(new Object[] { label, button }, idx);
      requestTemplatesMap.put(idx, docReq);
    }

    requestTemplatesTable.setColumnWidth(requestTemplatesTable.getVisibleColumns()[0], 300);
    requestTemplatesTable.setColumnWidth(requestTemplatesTable.getVisibleColumns()[1], 100);

    form.getLayout().addComponent(requestTemplatesTable);

    requestsTable.setColumnWidth(requestsTable.getVisibleColumns()[0], 300);
    requestsTable.setColumnWidth(requestsTable.getVisibleColumns()[1], 100);

    form.getLayout().addComponent(requestsTable);

    return form;
  }
}
