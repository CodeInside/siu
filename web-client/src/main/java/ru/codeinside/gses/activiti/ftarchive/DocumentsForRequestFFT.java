package ru.codeinside.gses.activiti.ftarchive;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import net.mobidom.bp.beans.Обращение;
import net.mobidom.bp.beans.СсылкаНаДокумент;
import net.mobidom.bp.beans.form.DocumentRequestForm;
import net.mobidom.bp.beans.form.DocumentRequestFormBuilder;
import net.mobidom.bp.beans.request.DocumentRequest;
import net.mobidom.bp.beans.types.DocumentTypesHelper;
import net.mobidom.bp.beans.types.ТипДокумента;

import org.activiti.engine.ProcessEngine;
import org.vaadin.dialogs.ConfirmDialog;

import ru.codeinside.gses.activiti.forms.api.definitions.PropertyNode;
import ru.codeinside.gses.activiti.forms.types.FieldType;
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
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
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

  public Window window;

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

  public static void showRequestFormWindow(final Window parentWindow, DocumentRequest documentRequest, final RequestFormCompleted listener,
      boolean readonly) {

    final DocumentRequestForm documentRequestForm = DocumentRequestFormBuilder.createForm(documentRequest, readonly);

    if (documentRequestForm == null) {
      ConfirmDialog.show(parentWindow,
          String.format("Информация по сервису 'Запрос документа: %s' отсутствует!", documentRequest.getType().getLabel()),
          new ConfirmDialog.Listener() {
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

    vLayout.addComponent(documentRequestForm.form);

    HorizontalLayout hLayout = new HorizontalLayout();

    if (!readonly) {
      Button acceptButton = new Button("Принять");
      acceptButton.addListener(new Button.ClickListener() {
        private static final long serialVersionUID = -6269781519620356734L;

        @Override
        public void buttonClick(ClickEvent event) {
          if (documentRequestForm.accept()) {

            if (listener != null)
              listener.onSubmit(true);

            parentWindow.removeWindow(newWindow);
          }
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

    this.window = Flash.app().getMainWindow();
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

    List<СсылкаНаДокумент> templateDocumentRequest = mainRequest.getСсылкиНаДокументы();

    List<DocumentRequest> templateDocumentRequests = new ArrayList<DocumentRequest>();

    List<ТипДокумента> availableTypes = ТипДокумента.requestableTypes();
    for (ТипДокумента type : availableTypes) {

      DocumentRequest documentRequest = new DocumentRequest();
      documentRequest.setType(type);
      documentRequest.setLabel(type.getLabel());
      templateDocumentRequests.add(documentRequest);
    }

    Form form = new Form();

    // типы документы которые будут запрашиваться
    requestsTable = new Table();
    requestsTable.addContainerProperty("Запрос документа", TextArea.class, null);
    requestsTable.addContainerProperty("Инфо", Button.class, null);
    requestsTable.addContainerProperty("Данные", Button.class, null);
    requestsTable.addContainerProperty("Отмена", Button.class, null);

    // типы документов которые можно запросить = (список из типов документов,
    // которые можно запросить + ссылки на документы)
    requestTemplatesTable = new Table();
    requestTemplatesTable.addContainerProperty("Документ", TextArea.class, null);
    requestTemplatesTable.addContainerProperty("Запросить", Button.class, null);

    for (int i = 0; i < templateDocumentRequest.size(); i++) {

      Integer idx = new Integer(i);

      СсылкаНаДокумент docRef = templateDocumentRequest.get(i);

      TextArea label = new TextArea();
      label.setValue(docRef.getLabelString());
      label.setWordwrap(true);
      label.setWidth("100%");
      label.setReadOnly(true);
      label.setRows(2);

      final Button button = new Button("Запросить");
      button.addListener(new Button.ClickListener() {
        private static final long serialVersionUID = -9108557983902457561L;

        @Override
        public void buttonClick(ClickEvent event) {
          // button.setEnabled(false);
          СсылкаНаДокумент docReference = (СсылкаНаДокумент) event.getButton().getData();
          addRequestForСсылкаНаДокумент(docReference);
        }
      });
      button.setData(docRef);
      requestTemplatesTable.addItem(new Object[] { label, button }, idx);
      requestTemplatesMap.put(idx, docRef);
    }

    for (int i = 0; i < templateDocumentRequests.size(); i++) {

      Integer idx = new Integer(i + templateDocumentRequest.size());

      DocumentRequest docReq = templateDocumentRequests.get(i);

      TextArea label = new TextArea();
      label.setValue(docReq.getLabel());
      label.setWordwrap(true);
      label.setWidth("100%");
      label.setReadOnly(true);
      label.setRows(2);

      Button button = new Button("Запросить");
      button.addListener(new Button.ClickListener() {
        private static final long serialVersionUID = 1042368331219897593L;

        @Override
        public void buttonClick(ClickEvent event) {
          DocumentRequest templateRequest = (DocumentRequest) event.getButton().getData();
          addRequestForTemplateRequest(templateRequest);
        }

      });
      button.setData(docReq);
      requestTemplatesTable.addItem(new Object[] { label, button }, idx);
      requestTemplatesMap.put(idx, docReq);
    }

    requestTemplatesTable.setPageLength(requestTemplatesMap.size() + 1);
    requestTemplatesTable.setColumnWidth(requestTemplatesTable.getVisibleColumns()[0], -1);
    requestTemplatesTable.setColumnWidth(requestTemplatesTable.getVisibleColumns()[1], TableStyle.BUTTON_COL_WIDTH);
    TableStyle.setGeneralStyle(requestTemplatesTable);

    form.getLayout().addComponent(requestTemplatesTable);

    if (!documentRequests.isEmpty()) {
      for (DocumentRequest request : documentRequests) {
        addDocumentRequest(request);
      }
    }

    requestsTable.setPageLength(5);
    requestsTable.setColumnWidth(requestsTable.getVisibleColumns()[0], -1);
    requestsTable.setColumnWidth(requestsTable.getVisibleColumns()[1], TableStyle.BUTTON_COL_WIDTH);
    requestsTable.setColumnWidth(requestsTable.getVisibleColumns()[2], TableStyle.BUTTON_COL_WIDTH);
    requestsTable.setColumnWidth(requestsTable.getVisibleColumns()[3], TableStyle.BUTTON_COL_WIDTH);
    TableStyle.setGeneralStyle(requestsTable);

    form.getLayout().addComponent(requestsTable);

    return form;
  }

  protected void addRequestForTemplateRequest(DocumentRequest templateRequest) {
    DocumentRequest request = new DocumentRequest();
    request.setLabel(templateRequest.getLabel());
    request.setType(templateRequest.getType());
    addDocumentRequestWithForm(request);
  }

  public void addRequestForСсылкаНаДокумент(СсылкаНаДокумент documentRef) {
    DocumentRequest request = new DocumentRequest();

    for (Entry<String, Serializable> param : documentRef.getDocumentRequestParams().entrySet())
      request.addRequestParam(param.getKey(), param.getValue());

    ТипДокумента documentType = DocumentTypesHelper.defineDocumentTypeByReferenceType(documentRef);

    request.setLabel(documentType.getLabel());

    request.setType(documentType);
    request.setCreateDate(new Date());

    addDocumentRequestWithForm(request);

    // addDocumentRequest(request);
  }

  public void addDocumentRequestWithForm(final DocumentRequest request) {
    showRequestFormWindow(window, request, new RequestFormCompleted() {

      @Override
      public void onSubmit(boolean submit) {
        if (submit)
          addDocumentRequest(request);
      }
    }, false);
  }

  public void addDocumentRequest(final DocumentRequest request) {
    final Integer nextIdx = requestsTable.size() + 1;
    request.setCreateDate(new Date());

    requestsTable.addItem(buildDocumentRequestItemContent(nextIdx, request), nextIdx);

    if (requestsTable.size() >= 5) {
      requestsTable.setPageLength(requestsTable.size() + 1);
    }

    requestsMap.put(nextIdx, request);

    updateDocumentRequestsInProcessContext();
  }

  public Object[] buildDocumentRequestItemContent(final Integer idx, final DocumentRequest request) {
    
    TextArea label = new TextArea();
    label.setValue(request.requestParamsToLabel());
    label.setWordwrap(true);
    label.setWidth("100%");
    label.setReadOnly(true);
    label.setRows(2);

    Button infoButton = null;
    if (request.getFault() != null || request.getResponseType() != null) {
      String infoButtonCaption = "Инфо";
      String infoMessage = "";
      if (request.getResponseType() != null) {
        switch (request.getResponseType()) {
        case DATA_ERROR:
          infoMessage = "Ошибка в данных запроса.";
          infoButtonCaption = "Ошибка";
          break;
        case RESULT_NOT_READY:
          infoMessage = "Результат не готов.";
          infoButtonCaption = "Инфо";
          break;
        case SYSTEM_ERROR:
          infoMessage = "Системная ошибка.";
          infoButtonCaption = "Ошибка";
          break;
        case DATA_NOT_FOUND:
          infoMessage = "По запросу данные не найдены";
          infoButtonCaption = "Инфо";
          break;
        default:
          infoMessage = "";
          infoButtonCaption = "Инфо";
          break;
        }
      }

      if (request.getFault() != null) {
        infoMessage += "\n" + String.valueOf(request.getFault());
      }

      infoButton = new Button(infoButtonCaption);

      infoButton.setData(infoMessage);

      infoButton.addListener(new Button.ClickListener() {
        private static final long serialVersionUID = -6915592981761898193L;

        @Override
        public void buttonClick(ClickEvent event) {
          String infoMessage = (String) event.getButton().getData();
          ConfirmDialog.show(window, "Информация", infoMessage, "ОК", null, new ConfirmDialog.Listener() {
            private static final long serialVersionUID = -7009556628554747672L;

            @Override
            public void onClose(ConfirmDialog dialog) {
            }
          });

        }
      });

    }

    Button editRequestButton = new Button("Редактировать");
    editRequestButton.addListener(new Button.ClickListener() {
      private static final long serialVersionUID = 6348767896636362763L;

      @Override
      public void buttonClick(ClickEvent event) {
        showRequestFormWindow(window, request, new DocumentsForRequestFFT.RequestFormCompleted() {

          @Override
          public void onSubmit(boolean submit) {
            if (submit) {
              replaceDocumentRequestItem(idx, request);
            }
          }
        }, false);
      }
    });

    Button removeRequestButton = new Button("Удалить");
    removeRequestButton.addListener(new Button.ClickListener() {
      private static final long serialVersionUID = 4081006034387873421L;

      @Override
      public void buttonClick(ClickEvent event) {
        removeDocumentRequestItem(idx);
      }
    });

    return new Object[] { label, infoButton, editRequestButton, removeRequestButton };
  }

  public void replaceDocumentRequestItem(final Integer idx, final DocumentRequest request) {
    requestsTable.removeItem(idx);
    Object[] comps = buildDocumentRequestItemContent(idx, request);
    requestsTable.addItem(comps, idx);
  }

  public void removeDocumentRequestItem(final Integer idx) {
    requestsTable.removeItem(idx);
    requestsMap.remove(idx);
    updateDocumentRequestsInProcessContext();
  }

}