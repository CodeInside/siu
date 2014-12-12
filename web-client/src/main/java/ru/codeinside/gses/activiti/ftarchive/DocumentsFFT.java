package ru.codeinside.gses.activiti.ftarchive;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import net.mobidom.bp.beans.Документ;
import net.mobidom.bp.beans.request.DocumentRequest;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Element;

import ru.codeinside.gses.activiti.forms.api.definitions.PropertyNode;
import ru.codeinside.gses.activiti.forms.types.FieldType;
import ru.codeinside.gses.activiti.ftarchive.style.TableStyle;
import ru.codeinside.gses.webui.form.FileDownloadResource;

import com.vaadin.Application;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@SuppressWarnings("rawtypes")
public class DocumentsFFT implements FieldType<List> {
  private static final long serialVersionUID = 307514824270473103L;

  static Logger log = Logger.getLogger(DocumentsFFT.class.getName());

  static SimpleDateFormat SDF = new SimpleDateFormat("dd.MM.yyyy");

  @Override
  public Field createField(String taskId, String fieldId, String name, List value, PropertyNode node, boolean archive) {

    @SuppressWarnings("unchecked")
    List<Документ> list = value;

    Form form = new Form();

    Table documents = new Table();
    documents.addContainerProperty("Документ", Label.class, null);
    documents.addContainerProperty("Просмотр", Button.class, null);
    documents.addContainerProperty("Запрос", Button.class, null);

    int i = 0;
    for (; i < list.size(); i++) {

      Документ doc = list.get(i);

      Label label = new Label(createDocumentLabel(doc));
      label.setContentMode(Label.CONTENT_PREFORMATTED);

      Button showButton = new Button("Просмотр");
      showButton.setData(doc);
      showButton.addListener(new Button.ClickListener() {
        private static final long serialVersionUID = 6966319886178532454L;

        @Override
        public void buttonClick(ClickEvent event) {
          Документ document = (Документ) event.getButton().getData();
          showDocument(event.getButton().getApplication(), event.getButton().getWindow(), document);
        }
      });

      Button showRequestButton = null;
      if (doc.getDocumentRequest() != null) {
        DocumentRequest request = doc.getDocumentRequest();
        showRequestButton = new Button("Запрос");
        showRequestButton.setData(request);
        showRequestButton.addListener(new Button.ClickListener() {
          private static final long serialVersionUID = -7488811385112225562L;

          @Override
          public void buttonClick(ClickEvent event) {
            DocumentRequest request = (DocumentRequest) event.getButton().getData();
            showDocumentRequestWindow(event.getButton().getApplication(), event.getButton().getWindow(), request);
          }
        });
      }

      documents.addItem(new Object[] { label, showButton, showRequestButton }, new Integer(i));
    }

    documents.setPageLength(i);
    documents.setColumnWidth(documents.getVisibleColumns()[0], -1);
    documents.setColumnWidth(documents.getVisibleColumns()[1], TableStyle.BUTTON_COL_WIDTH);
    documents.setColumnWidth(documents.getVisibleColumns()[2], TableStyle.BUTTON_COL_WIDTH);
    TableStyle.setGeneralStyle(documents);

    form.getLayout().addComponent(documents);

    return form;
  }

  private String createDocumentLabel(Документ doc) {

    StringBuilder sb = new StringBuilder();

    if (doc.getDocumentRequest() != null) {

      DocumentRequest req = doc.getDocumentRequest();

      sb.append(req.requestParamsToLabel()).append(" ");

      if (req.getCompleteDate() != null)
        sb.append("от ").append(SDF.format(req.getCompleteDate())).append(" ");

    } else {

      if (doc.getDocumentType() != null)
        sb.append(doc.getDocumentType().getLabel()).append(": ");

      if (doc.getСерия() != null)
        sb.append(doc.getСерия()).append(" ");

      if (doc.getНомер() != null)
        sb.append(doc.getНомер()).append(" ");

      if (doc.getВид() != null)
        sb.append(doc.getВид().toString().replace('_', ' ')).append(" ");
    }

    return sb.toString();
  }

  protected void showDocumentRequestWindow(Application application, Window window, DocumentRequest request) {
    // TODO webdom
    DocumentsForRequestFFT.showRequestFormWindow(window, request, null, true);
  }

  private void showDocument(Application application, Window window, Документ document) {
    if (document.getBinaryContent() != null) {
      showBinaryDocument(application, window, document);
    } else {
      // TODO webdom XSLT trasform for Element and show Window with data
      showXmlElementDocument(application, window, document);
    }

  }

  private void showBinaryDocument(Application application, Window window, Документ document) {
    window.open(new FileDownloadResource(true, createTempFile(document), application), "_top", false);
  }

  private void showXmlElementDocument(Application application, Window window, Документ document) {

    Element data = document.getXmlContent().getXmlContent();
    String stringData = null;
    try {

      Transformer transformer = TransformerFactory.newInstance().newTransformer();
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      StreamResult result = new StreamResult(new StringWriter());
      DOMSource source = new DOMSource(data);
      transformer.transform(source, result);
      stringData = result.getWriter().toString();

    } catch (Exception e) {
      log.log(Level.SEVERE, "cant show xml element", e);
      stringData = String.format("Не удается отобразить документ: \n %s", e.getMessage());
    }

    Label textArea = new Label();
    textArea.setContentMode(Label.CONTENT_PREFORMATTED);
    textArea.setValue(stringData);
    textArea.setReadOnly(true);
    textArea.setWidth(700, Sizeable.UNITS_PIXELS);
    textArea.setHeight(500, Sizeable.UNITS_PIXELS);

    VerticalLayout layout = new VerticalLayout();
    layout.setMargin(true);
    Window newWindow = new Window();
    newWindow.setWidth(800 + 50, Sizeable.UNITS_PIXELS);
    newWindow.setHeight(600 + 100, Sizeable.UNITS_PIXELS);
    newWindow.center();
    newWindow.setContent(layout);
    newWindow.setCaption(document.getТип());
    newWindow.setResizable(false); // нет подстройки под размер

    layout.addComponent(textArea);

    window.addWindow(newWindow);

  }

  private File createTempFile(Документ doc) {
    String suffix = null;

    String mime = doc.getBinaryContent().getMimeType();
    if (StringUtils.equalsIgnoreCase("application/pdf", mime)) {
      suffix = ".pdf";
    }

    try {
      File tmpFile = File.createTempFile(String.valueOf(doc.getТип()), suffix);
      FileOutputStream outputStream = new FileOutputStream(tmpFile);
      outputStream.write(doc.getBinaryContent().getBinaryData());
      outputStream.close();
      return tmpFile;
    } catch (IOException e) {
      throw new RuntimeException("can't create temp file", e);
    }
  }

}
