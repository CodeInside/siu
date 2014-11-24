package ru.codeinside.gses.activiti.ftarchive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import net.mobidom.bp.DocumentsForRequestService;
import net.mobidom.bp.beans.ФизическоеЛицо;
import net.mobidom.bp.beans.СсылкаНаДокумент;
import net.mobidom.bp.beans.Обращение;
import net.mobidom.bp.beans.request.DocumentRequest;
import net.mobidom.bp.beans.types.ТипСсылкиНаДокумент;
import net.mobidom.bp.beans.types.ТипДокумента;

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

  Table requestingDocumentsTable;
  Table documentForRequestTable;

  Map<Integer, Object> requestingDocumentsMap = new HashMap<Integer, Object>();
  Map<Integer, Object> documentForRequestMap = new HashMap<Integer, Object>();

  String pid;

  Обращение mainRequest;

  class RemoveRequestAction implements Button.ClickListener {

    @Override
    public void buttonClick(ClickEvent event) {

      Integer idx = (Integer) event.getButton().getData();
      requestingDocumentsTable.removeItem(idx);
      requestingDocumentsMap.remove(idx);

      List<DocumentRequest> documentRequests = new ArrayList<DocumentRequest>();
      for (Object data : requestingDocumentsMap.values()) {
        documentRequests.add((DocumentRequest) data);
      }

      Flash.flash().getProcessEngine().getRuntimeService().setVariable(pid, "documentRequests", documentRequests);

    }
  }

  class AddRequestAction implements Button.ClickListener {

    @Override
    public void buttonClick(ClickEvent event) {

      Integer idx = (Integer) event.getButton().getData();
      Object data = documentForRequestMap.get(idx);

      if (data instanceof СсылкаНаДокумент) {

        event.getButton().setEnabled(false);

        СсылкаНаДокумент ref = (СсылкаНаДокумент) data;
        Integer nextIdx = requestingDocumentsTable.size() + 1;

        DocumentRequest request = new DocumentRequest();

        // TODO webdom define actual type
        ТипДокумента documentType = null;
        if (ref.getТипСсылкиНаДокумент() == ТипСсылкиНаДокумент.СНИЛС) {
          request.setDocRef(ref);
          documentType = ТипДокумента.ДАННЫЕ_ЛИЦЕВОГО_СЧЕТА_ЗАСТРАХОВАННОГО_ЛИЦА;
        }

        request.setType(documentType);

        Label label = new Label(ref.getLabelString());
        Button button = new Button("Удалить");
        button.setData(nextIdx);
        button.addListener(new RemoveRequestAction());
        requestingDocumentsTable.addItem(new Object[] { label, button }, nextIdx);
        requestingDocumentsMap.put(nextIdx, request);

      } else if (data instanceof DocumentRequest) {

        // TODO webdom

        DocumentRequest request = (DocumentRequest) data;
        if (request.getType() == ТипДокумента.СНИЛС) {
//          ФизическоеЛицо declarer = mainRequest.getDeclarer();
//          request.addRequestParam("surname", declarer.getSurname());
//          request.addRequestParam("name", declarer.getName());
//          request.addRequestParam("patronymic", declarer.getPatronymic());
//          request.addRequestParam("birthdate", declarer.getBirthDate());
//          request.addRequestParam("gender", "F");
        }

        Integer nextIdx = requestingDocumentsTable.size() + 1;

        Label label = new Label(request.getLabel());
        Button button = new Button("Удалить");
        button.setData(nextIdx);
        button.addListener(new RemoveRequestAction());
        requestingDocumentsTable.addItem(new Object[] { label, button }, nextIdx);
        requestingDocumentsMap.put(nextIdx, request);
      }

      List<DocumentRequest> documentRequests = new ArrayList<DocumentRequest>();
      for (Object tdata : requestingDocumentsMap.values()) {
        documentRequests.add((DocumentRequest) tdata);
      }

      Flash.flash().getProcessEngine().getRuntimeService().setVariable(pid, "documentRequests", documentRequests);
    }
  }

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
    requestingDocumentsTable = new Table();
    requestingDocumentsTable.addContainerProperty("Документ", Label.class, null);
    requestingDocumentsTable.addContainerProperty("Отмена", Button.class, null);

    // типы документов которые можно запросить = (список из типов документов,
    // которые можно запросить + ссылки на документы)
    documentForRequestTable = new Table();
    documentForRequestTable.addContainerProperty("Документ", Label.class, null);
    documentForRequestTable.addContainerProperty("Запросить", Component.class, null);

    for (int i = 0; i < documentRefs.size(); i++) {

      Integer idx = new Integer(i);

      СсылкаНаДокумент docRef = documentRefs.get(i);

      Label label = new Label(docRef.getLabelString());
      label.setContentMode(Label.CONTENT_PREFORMATTED);

      Button button = new Button("Запросить");
      button.addListener(new AddRequestAction());
      button.setData(idx);
      documentForRequestTable.addItem(new Object[] { label, button }, idx);
      documentForRequestMap.put(idx, docRef);
    }

    for (int i = 0; i < defDocumentRequests.size(); i++) {

      Integer idx = new Integer(i + documentRefs.size());

      DocumentRequest docReq = defDocumentRequests.get(i);

      Label label = new Label(docReq.getLabel());
      label.setContentMode(Label.CONTENT_PREFORMATTED);

      Button button = new Button("Запросить");
      button.addListener(new AddRequestAction());
      button.setData(idx);
      documentForRequestTable.addItem(new Object[] { label, button }, idx);
      documentForRequestMap.put(idx, docReq);
    }

    documentForRequestTable.setColumnWidth(documentForRequestTable.getVisibleColumns()[0], 300);
    documentForRequestTable.setColumnWidth(documentForRequestTable.getVisibleColumns()[1], 100);

    form.getLayout().addComponent(documentForRequestTable);

    requestingDocumentsTable.setColumnWidth(requestingDocumentsTable.getVisibleColumns()[0], 300);
    requestingDocumentsTable.setColumnWidth(requestingDocumentsTable.getVisibleColumns()[1], 100);

    form.getLayout().addComponent(requestingDocumentsTable);

    // Button requestButton = new Button("Запросить");
    // requestButton.addListener(new Button.ClickListener() {
    //
    // @Override
    // public void buttonClick(ClickEvent event) {
    // List<DocumentRequest> documentRequests = new
    // ArrayList<DocumentRequest>();
    // for (Object data : requestingDocumentsMap.values()) {
    // documentRequests.add((DocumentRequest) data);
    // }
    //
    // Flash.flash().getProcessEngine().getRuntimeService().setVariable(pid,
    // "documentRequests", documentRequests);
    //
    // }
    // });
    //
    // form.getLayout().addComponent(requestButton);

    return form;
  }
}
