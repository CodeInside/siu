package ru.codeinside.gses.activiti.ftarchive;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

import net.mobidom.bp.beans.Declarer;
import net.mobidom.bp.beans.Document;
import net.mobidom.bp.beans.DocumentRef;
import net.mobidom.bp.beans.Request;

import org.apache.commons.lang.StringUtils;

import ru.codeinside.gses.activiti.forms.api.definitions.PropertyNode;
import ru.codeinside.gses.activiti.forms.types.DateType;
import ru.codeinside.gses.activiti.forms.types.FieldType;
import ru.codeinside.gses.webui.form.FileDownloadResource;

import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;

public class RequestFFT implements FieldType<Request> {

  private static final long serialVersionUID = 8309095853078973327L;

  private static Logger log = Logger.getLogger(RequestFFT.class.getName());

  @SuppressWarnings("serial")
  @Override
  public Field createField(String taskId, String fieldId, String name, Request value, PropertyNode node, boolean archive) {

    if (value == null) {
      throw new RuntimeException("request value is null");
    }

    Form form = new Form() {

      Request value;

      @Override
      public void commit() throws SourceException, InvalidValueException {
        // nothing to do
      }

      @Override
      public void setValue(Object newValue) throws com.vaadin.data.Property.ReadOnlyException, com.vaadin.data.Property.ConversionException {
        if (newValue == null) {
          throw new RuntimeException("request value is null");
        }

        if (!(newValue instanceof Request)) {
          throw new RuntimeException("request value is not Request instance. it's = " + newValue.getClass());
        }

        this.value = (Request) newValue;
      };

      @Override
      public Object getValue() {
        return this.value;
      }

    };

    form.setCaption("Заявление");

    Layout layout = form.getLayout();

    PopupDateField createDateField = new PopupDateField();
    createDateField.setDateFormat(DateType.PATTERN1);
    createDateField.setCaption("Дата подачи");
    createDateField.setValue(value.getCreateTime());
    layout.addComponent(createDateField);

    layout.addComponent(createTextField("Услуга", value.getServiceName()));
    layout.addComponent(createTextField("Орган", value.getGovernmentAgencyName()));

    // declarerinfo
    Declarer declarerValue = value.getDeclarer();
    layout.addComponent(createTextField("Фамилия", declarerValue.getSurname()));
    layout.addComponent(createTextField("Имя", declarerValue.getSurname()));
    layout.addComponent(createTextField("Отчество", declarerValue.getSurname()));

    PopupDateField birthDateField = new PopupDateField();
    birthDateField.setDateFormat(DateType.PATTERN1);
    birthDateField.setCaption("Дата рождения");
    birthDateField.setValue(declarerValue.getBirthDate());
    layout.addComponent(birthDateField);
    layout.addComponent(createTextField("Адрес регистрации", declarerValue.getRegistrationAddress()));
    layout.addComponent(createTextField("Номер телефона", declarerValue.getPhoneNumber()));
    layout.addComponent(createTextField("Эл.почта", declarerValue.getEmail()));

//    // document refs
//    Table documentRefsTable = new Table("Ссылки на документ");
//    documentRefsTable.addContainerProperty("Документ", Label.class, null);
//    documentRefsTable.addContainerProperty("Действие", Button.class, null);
//
//    log.info("build table for documentRefs");
//
//    int i = 0;
//    for (; i < value.getDocumentRefs().size(); i++) {
//
//      DocumentRef documentRef = value.getDocumentRefs().get(i);
//
//      Label label = new Label(documentRef.getLabelString());
//      label.setContentMode(Label.CONTENT_PREFORMATTED);
//
//      Button actionButton = new Button();
//      actionButton.setData(documentRef);
//
//      if (documentRef.getDocument() == null && documentRef.isNeedToLoad()) {
//        actionButton.setEnabled(false);
//        actionButton.setCaption("Запрос идет");
//      } else if (documentRef.getDocument() == null) {
//        actionButton.setCaption("Запросить");
//        actionButton.addListener(new Button.ClickListener() {
//
//          @Override
//          public void buttonClick(ClickEvent event) {
//            Button button = event.getButton();
//            DocumentRef documentRef = (DocumentRef) button.getData();
//            documentRef.setNeedToLoad(true);
//            log.info("user want to request " + documentRef.getLabelString());
//            button.setEnabled(false);
//            button.setCaption("Запрос идет");
//          }
//        });
//      } else {
//        actionButton.setCaption("Просмотр");
//
//        actionButton.addListener(new Button.ClickListener() {
//
//          @Override
//          public void buttonClick(ClickEvent event) {
//            DocumentRef documentRef = (DocumentRef) event.getButton().getData();
//            // TODO mark document for request or open document
//            log.info("user want to download " + documentRef.getLabelString());
//          }
//        });
//      }
//
//      documentRefsTable.addItem(new Object[] { label, actionButton }, new Integer(i));
//    }
//
//    documentRefsTable.setPageLength(i);
//    documentRefsTable.setColumnWidth(documentRefsTable.getVisibleColumns()[0], 200);
//    documentRefsTable.setColumnWidth(documentRefsTable.getVisibleColumns()[1], 100);
//
//    layout.addComponent(documentRefsTable);
//
//    // documents
//    Table documentsTable = new Table("Документы");
//    documentsTable.addContainerProperty("Документ", Label.class, null);
//    documentsTable.addContainerProperty("Действие", Button.class, null);
//
//    log.info("build table for documents");
//
//    i = 0;
//    for (; i < value.getDocuments().size(); i++) {
//
//      Document document = value.getDocuments().get(i);
//      Label label = new Label(document.getType());
//      label.setContentMode(Label.CONTENT_PREFORMATTED);
//      Button actionButton = new Button("Просмотр");
//      actionButton.setData(document);
//      actionButton.addListener(new Button.ClickListener() {
//
//        @Override
//        public void buttonClick(ClickEvent event) {
//          Document document = (Document) event.getButton().getData();
//          if (document.getBinaryContent() != null) {
//            Window window = event.getButton().getWindow();
//            window.open(new FileDownloadResource(true, createTempFile(document), event.getButton().getApplication()), "_top", false);
//          } else {
//            // TODO XSLT trasform for Element and show Window with data
//          }
//        }
//      });
//
//      documentsTable.addItem(new Object[] { label, actionButton }, new Integer(i));
//    }
//
//    documentsTable.setPageLength(i);
//    documentsTable.setColumnWidth(documentRefsTable.getVisibleColumns()[0], 200);
//    documentsTable.setColumnWidth(documentRefsTable.getVisibleColumns()[1], 100);
//
//    layout.addComponent(documentsTable);

    form.setValue(value);

    return form;
  }

//  private File createTempFile(Document doc) {
//    String suffix = null;
//
//    String mime = doc.getBinaryContent().getMimeType();
//    if (StringUtils.equalsIgnoreCase("application/pdf", mime)) {
//      suffix = ".pdf";
//    }
//
//    try {
//      File tmpFile = File.createTempFile(doc.getType(), suffix);
//      FileOutputStream outputStream = new FileOutputStream(tmpFile);
//      outputStream.write(doc.getBinaryContent().getBinaryData());
//      outputStream.close();
//      return tmpFile;
//    } catch (IOException e) {
//      throw new RuntimeException("can't create temp file", e);
//    }
//  }

  TextField createTextField(String caption, String value) {
    TextField textField = new TextField(caption, value);
    return textField;
  }
}
