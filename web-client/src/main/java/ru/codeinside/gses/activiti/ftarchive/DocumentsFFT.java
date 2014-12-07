package ru.codeinside.gses.activiti.ftarchive;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import net.mobidom.bp.beans.Документ;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Element;

import ru.codeinside.gses.activiti.forms.api.definitions.PropertyNode;
import ru.codeinside.gses.activiti.forms.types.FieldType;
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

  @Override
  public Field createField(String taskId, String fieldId, String name, List value, PropertyNode node, boolean archive) {

    @SuppressWarnings("unchecked")
    List<Документ> list = value;

    Form form = new Form();

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
        private static final long serialVersionUID = 6966319886178532454L;

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
