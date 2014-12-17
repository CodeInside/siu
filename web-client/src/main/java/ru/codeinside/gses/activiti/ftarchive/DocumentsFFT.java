package ru.codeinside.gses.activiti.ftarchive;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.mobidom.bp.beans.Документ;
import net.mobidom.bp.beans.Обращение;
import net.mobidom.bp.beans.СсылкаНаДокумент;
import net.mobidom.bp.beans.request.DocumentRequest;

import org.activiti.engine.ProcessEngine;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Element;

import ru.codeinside.gses.activiti.forms.api.definitions.PropertyNode;
import ru.codeinside.gses.activiti.forms.types.FieldType;
import ru.codeinside.gses.activiti.ftarchive.document.DocumentFormGenerator;
import ru.codeinside.gses.activiti.ftarchive.style.TableStyle;
import ru.codeinside.gses.beans.DirectoryBeanProvider;
import ru.codeinside.gses.webui.Flash;
import ru.codeinside.gses.webui.form.FileDownloadResource;

import com.vaadin.Application;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Table;
import com.vaadin.ui.Window;

@SuppressWarnings("rawtypes")
public class DocumentsFFT implements FieldType<String> {
  private static final long serialVersionUID = 307514824270473103L;

  static Logger log = Logger.getLogger(DocumentsFFT.class.getName());

  static SimpleDateFormat SDF = new SimpleDateFormat("dd.MM.yyyy");

  public String pid;
  public Обращение mainRequest;

  public Window window;

  @Override
  public Field createField(String taskId, String fieldId, String name, String value, PropertyNode node, boolean archive) {

    this.window = Flash.app().getMainWindow();
    this.pid = value;

    ProcessEngine processEngine = Flash.flash().getProcessEngine();

    log.info("processEngine = " + processEngine);

    this.mainRequest = (Обращение) processEngine.getRuntimeService().getVariable(pid, "request");

    List<Документ> documents = mainRequest.getДокументы();
    List<СсылкаНаДокумент> documentRefs = mainRequest.getСсылкиНаДокументы();

    Form form = new Form();

    Table documentsTable = new Table();
    documentsTable.addContainerProperty("Документ", Label.class, null);
    documentsTable.addContainerProperty("Просмотр", Button.class, null);
    documentsTable.addContainerProperty("Запрос", Button.class, null);

    for (int i = 0; i < documentRefs.size(); i++) {

      СсылкаНаДокумент documentRef = documentRefs.get(i);
      Label label = new Label(createDocumentRefLabel(documentRef));
      label.setContentMode(Label.CONTENT_PREFORMATTED);

      Button showButton = new Button("Просмотр");
      showButton.setData(documentRef);
      showButton.addListener(new Button.ClickListener() {
        private static final long serialVersionUID = 6966319886178532454L;

        @Override
        public void buttonClick(ClickEvent event) {
          СсылкаНаДокумент docRef = (СсылкаНаДокумент) event.getButton().getData();
          showDocumentReference(event.getButton().getApplication(), event.getButton().getWindow(), docRef);
        }
      });

      documentsTable.addItem(new Object[] { label, showButton, null }, new Integer(i));
    }

    int shift = documentsTable.size();

    for (int i = 0; i < documents.size(); i++) {

      Документ doc = documents.get(i);

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

      documentsTable.addItem(new Object[] { label, showButton, showRequestButton }, new Integer(shift + i));
    }

    documentsTable.setPageLength(documentsTable.size());
    documentsTable.setColumnWidth(documentsTable.getVisibleColumns()[0], -1);
    documentsTable.setColumnWidth(documentsTable.getVisibleColumns()[1], TableStyle.BUTTON_COL_WIDTH);
    documentsTable.setColumnWidth(documentsTable.getVisibleColumns()[2], TableStyle.BUTTON_COL_WIDTH);
    TableStyle.setGeneralStyle(documentsTable);

    form.getLayout().addComponent(documentsTable);

    return form;
  }

  private String createDocumentRefLabel(СсылкаНаДокумент documentRef) {
    return documentRef.getLabelString();
  }

  protected void showDocumentReference(Application application, Window window2, СсылкаНаДокумент docRef) {
    // TODO Auto-generated method stub
    log.info("show document reference here");

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

    try {

      javax.xml.transform.Transformer transformer = javax.xml.transform.TransformerFactory.newInstance().newTransformer();
      javax.xml.transform.stream.StreamResult result = new javax.xml.transform.stream.StreamResult(new java.io.StringWriter());
      javax.xml.transform.dom.DOMSource source = new javax.xml.transform.dom.DOMSource(data);
      transformer.setOutputProperty("omit-xml-declaration", "yes");
      transformer.transform(source, result);
      String xmlString = result.getWriter().toString();

      log.info(document.getDocumentType().getServiceId() + "\n" + xmlString);

    } catch (Exception e) {
      log.log(Level.WARNING, "", e);
    }

    DocumentFormGenerator formGenerator = new DocumentFormGenerator(DirectoryBeanProvider.get(), document.getDocumentType().getServiceId(),
        data);

    Layout layout = formGenerator.generateUI();

    Window newWindow = new Window();
    newWindow.setWidth(800 + 50, Sizeable.UNITS_PIXELS);
    newWindow.setHeight(600 + 100, Sizeable.UNITS_PIXELS);
    newWindow.center();
    newWindow.setContent(layout);
    newWindow.setCaption(document.getТип());
    newWindow.setResizable(false); // нет подстройки под размер

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
