package ru.codeinside.gses.activiti.ftarchive;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import net.mobidom.bp.beans.Document;

import org.apache.commons.lang.StringUtils;

import ru.codeinside.gses.activiti.forms.api.definitions.PropertyNode;
import ru.codeinside.gses.activiti.forms.types.FieldType;
import ru.codeinside.gses.webui.form.FileDownloadResource;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.Window;

public class DocumentsFFT implements FieldType<List> {

  static Logger log = Logger.getLogger(DocumentsFFT.class.getName());

  @Override
  public Field createField(String taskId, String fieldId, String name, List value, PropertyNode node, boolean archive) {

    List<Document> list = value;

    Form form = new Form();

    // form.getLayout().addComponent();

    Table documents = new Table();
    documents.addContainerProperty("Документ", Label.class, null);
    documents.addContainerProperty("Просмотр", Button.class, null);

    int i = 0;
    for (; i < list.size(); i++) {

      Document ref = list.get(i);

      Label label = new Label(String.valueOf(ref.getType()));
      label.setContentMode(Label.CONTENT_PREFORMATTED);

      Button actionButton = new Button("Просмотр");
      actionButton.setData(ref);
      actionButton.addListener(new Button.ClickListener() {

        @Override
        public void buttonClick(ClickEvent event) {
          Document document = (Document) event.getButton().getData();
          if (document.getBinaryContent() != null) {
            Window window = event.getButton().getWindow();
            window.open(new FileDownloadResource(true, createTempFile(document), event.getButton().getApplication()), "_top", false);
          } else {
            // TODO XSLT trasform for Element and show Window with data
          }
        }
      });

      documents.addItem(new Object[] { label, actionButton }, new Integer(i));
    }

    documents.setPageLength(i);
    documents.setColumnWidth(documents.getVisibleColumns()[0], 300);
    documents.setColumnWidth(documents.getVisibleColumns()[1], 100);

    form.getLayout().addComponent(documents);

    // Button startDocumentRequest = new Button("Запросить");
    // startDocumentRequest.addListener(new Button.ClickListener() {
    //
    // private static final long serialVersionUID = -5890409854604642345L;
    //
    // @Override
    // public void buttonClick(ClickEvent event) {
    // log.info("start document request");
    // }
    // });

    // form.getLayout().addComponent(startDocumentRequest);

    return form;
  }

  private File createTempFile(Document doc) {
    String suffix = null;

    String mime = doc.getBinaryContent().getMimeType();
    if (StringUtils.equalsIgnoreCase("application/pdf", mime)) {
      suffix = ".pdf";
    }

    try {
      File tmpFile = File.createTempFile(String.valueOf(doc.getType()), suffix);
      FileOutputStream outputStream = new FileOutputStream(tmpFile);
      outputStream.write(doc.getBinaryContent().getBinaryData());
      outputStream.close();
      return tmpFile;
    } catch (IOException e) {
      throw new RuntimeException("can't create temp file", e);
    }
  }

}
