package ru.codeinside.gses.activiti.ftarchive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import net.mobidom.bp.beans.Обращение;
import net.mobidom.bp.beans.СсылкаНаДокумент;
import net.mobidom.bp.beans.form.DocumentRequestForm;
import net.mobidom.bp.beans.form.DocumentRequestFormBuilder;
import net.mobidom.bp.beans.request.DocumentRequest;
import net.mobidom.bp.service.DocumentsForRequestService;

import org.activiti.engine.ProcessEngine;
import org.vaadin.dialogs.ConfirmDialog;

import ru.codeinside.gses.activiti.forms.api.definitions.PropertyNode;
import ru.codeinside.gses.activiti.forms.types.FieldType;
import ru.codeinside.gses.activiti.ftarchive.requests.AddRequestAction;
import ru.codeinside.gses.activiti.ftarchive.style.TableStyle;
import ru.codeinside.gses.webui.Flash;

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

  public Table requestsTable;
  public Table requestTemplatesTable;

  public Map<Integer, Object> requestsMap = new HashMap<Integer, Object>();
  public Map<Integer, Object> requestTemplatesMap = new HashMap<Integer, Object>();

  public String pid;
  public Обращение mainRequest;

  public void updateDocumentRequestsInProcessContext() {
    List<DocumentRequest> documentRequests = new ArrayList<DocumentRequest>();
    for (Object tdata : requestsMap.values()) {
      documentRequests.add((DocumentRequest) tdata);
    }

    Flash.flash().getProcessEngine().getRuntimeService().setVariable(pid, "documentRequests", documentRequests);
  }

  public static interface RequestFormCompleted {
    void onSubmit(boolean submit);
  }

  public static void showRequestFormWindow(final Window parentWindow, DocumentRequest documentRequest, final RequestFormCompleted listener, boolean readonly) {

    final DocumentRequestForm documentRequestForm = DocumentRequestFormBuilder.createForm(documentRequest, readonly);

    if (documentRequestForm == null) {
      // TODO webdom show alert window
      ConfirmDialog.show(parentWindow, String.format("Информация для сервиса 'Запрос документа: %s' отсутствует!", documentRequest.getType()), new ConfirmDialog.Listener() {
        private static final long serialVersionUID = 6279949007080346738L;

        @Override
        public void onClose(ConfirmDialog dialog) {
        }
      });
      return;
    }

    if (documentRequest.getRequestParams() != null && !documentRequest.getRequestParams().isEmpty()) {
      documentRequestForm.setValues(documentRequest.getRequestParams());
    }

    final Window newWindow = new Window();

    VerticalLayout vLayout = new VerticalLayout();
    vLayout.setStyleName("v-window-padding");

    vLayout.addComponent(documentRequestForm.formLayout);

    HorizontalLayout hLayout = new HorizontalLayout();

    if (!readonly) {
      Button acceptButton = new Button("Принять");
      acceptButton.addListener(new Button.ClickListener() {
        private static final long serialVersionUID = -6269781519620356734L;

        @Override
        public void buttonClick(ClickEvent event) {
          documentRequestForm.accept();

          if (listener != null)
            listener.onSubmit(true);

          parentWindow.removeWindow(newWindow);
        }
      });

      hLayout.addComponent(acceptButton);
    }

    Button cancelButton = new Button(readonly ? "Скрыть" : "Отменить");
    cancelButton.addListener(new Button.ClickListener() {
      private static final long serialVersionUID = -5645542995800887879L;

      @Override
      public void buttonClick(ClickEvent event) {

        if (listener != null)
          listener.onSubmit(false);

        parentWindow.removeWindow(newWindow);
      }
    });
    hLayout.addComponent(cancelButton);

    vLayout.addComponent(hLayout);

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
    requestsTable.addContainerProperty("Данные", Button.class, null);
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
      button.addListener(new AddRequestAction(this));
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
      button.addListener(new AddRequestAction(this));
      button.setData(idx);
      requestTemplatesTable.addItem(new Object[] { label, button }, idx);
      requestTemplatesMap.put(idx, docReq);
    }

    requestTemplatesTable.setPageLength(requestTemplatesMap.size() + 1);
    requestTemplatesTable.setColumnWidth(requestTemplatesTable.getVisibleColumns()[0], TableStyle.DATA_COL_WIDTH);
    requestTemplatesTable.setColumnWidth(requestTemplatesTable.getVisibleColumns()[1], TableStyle.BUTTON_COL_WIDTH);
    requestsTable.setWidth(TableStyle.DATA_COL_WIDTH + TableStyle.BUTTON_COL_WIDTH + 100, Sizeable.UNITS_PIXELS);

    form.getLayout().addComponent(requestTemplatesTable);

    requestsTable.setPageLength(5);
    requestsTable.setColumnWidth(requestsTable.getVisibleColumns()[0], TableStyle.DATA_COL_WIDTH);
    requestsTable.setColumnWidth(requestsTable.getVisibleColumns()[1], 2 * TableStyle.BUTTON_COL_WIDTH);
    requestsTable.setColumnWidth(requestsTable.getVisibleColumns()[2], TableStyle.BUTTON_COL_WIDTH);
    requestsTable.setWidth(TableStyle.DATA_COL_WIDTH + (3 * TableStyle.BUTTON_COL_WIDTH) + 100, Sizeable.UNITS_PIXELS);

    form.getLayout().addComponent(requestsTable);

    return form;
  }

}