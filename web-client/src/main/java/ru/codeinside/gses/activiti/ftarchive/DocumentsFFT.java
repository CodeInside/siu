package ru.codeinside.gses.activiti.ftarchive;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.mobidom.bp.beans.Документ;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Element;

import ru.codeinside.gses.activiti.forms.api.definitions.PropertyNode;
import ru.codeinside.gses.activiti.forms.types.FieldType;
import ru.codeinside.gses.vaadin.JsonFormIntegration;
import ru.codeinside.gses.webui.form.FileDownloadResource;
import ru.codeinside.gses.webui.wizard.ExpandRequired;

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

public class DocumentsFFT implements FieldType<List> {

  static Logger log = Logger.getLogger(DocumentsFFT.class.getName());

  @Override
  public Field createField(String taskId, String fieldId, String name, List value, PropertyNode node, boolean archive) {

    List<Документ> list = value;

    Form form = new Form();

    // form.getLayout().addComponent();

    Table documents = new Table();
    documents.addContainerProperty("Документ", Label.class, null);
    documents.addContainerProperty("Просмотр", Button.class, null);

    int i = 0;
    for (; i < list.size(); i++) {

      Документ ref = list.get(i);

      Label label = new Label(String.valueOf(ref.getТип()));
      label.setContentMode(Label.CONTENT_PREFORMATTED);

      Button actionButton = new Button("Просмотр");
      actionButton.setData(ref);
      actionButton.addListener(new Button.ClickListener() {

        @Override
        public void buttonClick(ClickEvent event) {
          Документ document = (Документ) event.getButton().getData();
          showDocument(event.getButton().getApplication(), event.getButton().getWindow(), document);
        }
      });

      documents.addItem(new Object[] { label, actionButton }, new Integer(i));
    }

    documents.setPageLength(i);
    documents.setColumnWidth(documents.getVisibleColumns()[0], 300);
    documents.setColumnWidth(documents.getVisibleColumns()[1], 100);

    form.getLayout().addComponent(documents);

    return form;
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

      javax.xml.transform.Transformer transformer = javax.xml.transform.TransformerFactory.newInstance().newTransformer();
      transformer.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
      javax.xml.transform.stream.StreamResult result = new javax.xml.transform.stream.StreamResult(new java.io.StringWriter());
      javax.xml.transform.dom.DOMSource source = new javax.xml.transform.dom.DOMSource(data);
      transformer.transform(source, result);

      stringData = result.getWriter().toString();
    } catch (Exception e) {
      log.log(Level.SEVERE, "cant show xml element", e);
      stringData = String.format("Не удалается отобразить документ: \n %s", e.getMessage());
    }

    Label label = new Label(stringData);

    VerticalLayout layout = new VerticalLayout();
    layout.setMargin(true);
    Window newWindow = new Window();
    newWindow.setWidth(800 + 50, Sizeable.UNITS_PIXELS);
    newWindow.setHeight(600 + 100, Sizeable.UNITS_PIXELS);
    newWindow.center();
    newWindow.setContent(layout);
    newWindow.setCaption(document.getТип());
    newWindow.setResizable(false); // нет подстройки под размер

    layout.addComponent(label);

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
