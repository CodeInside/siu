/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.form;


import com.google.common.base.Function;
import com.vaadin.Application;
import com.vaadin.terminal.FileResource;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import commons.Streams;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.database.Bid;
import ru.codeinside.adm.database.Employee;
import ru.codeinside.gses.form.FormData;
import ru.codeinside.gses.form.FormEntry;
import ru.codeinside.gses.webui.osgi.FormConverterCustomicer;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class PrintPanel extends CustomComponent {

  final static ThemeResource PRINT_ICON = new ThemeResource("../custom/icon/printer22.png");
  final static ThemeResource DOWNLOAD_ICON = new ThemeResource("../custom/icon/download22.png");

  final static AtomicInteger SERIAL = new AtomicInteger(0);

  File htmlFile;
  File docxFile;

  PrintPanel(FormDataSource dataSource, Application app, String procedureName, String taskId) {
    setSizeFull();
    try {
      String orgName = AdminServiceProvider.get().withEmployee(app.getUser().toString(), new Function<Employee, String>() {
        @Override
        public String apply(Employee employee) {
          return employee.getOrganization().getName();
        }
      });
      htmlFile = Streams.createTempFile("form-", ".html");
      docxFile = Streams.createTempFile("form-", ".docx");
      FormEntry formEntry = dataSource.createFormTree();
      FormData data = new FormData();
      data.orgName = orgName;
      data.serviceName = procedureName;
      if (taskId != null) {
        Bid bid = AdminServiceProvider.get().getBidByTask(taskId);
        data.receiptId = bid.getId();
        data.receiptDate = bid.getDateCreated();
      } else {
        data.receiptDate = new Date();
      }
      data.htmlFile = htmlFile.getAbsolutePath();
      data.docxFile = docxFile.getAbsolutePath();
      data.entries = formEntry.children;
      FormConverterCustomicer.convert(data);

      String classId = "doc" + SERIAL.incrementAndGet();

      Button print = new Button("Печатать", new PrintAction(classId));
      print.setStyleName("img-button");
      print.setIcon(PRINT_ICON);
      print.setImmediate(true);

      Button download = new Button("Скачать", new DownloadAction(docxFile));
      download.setStyleName("img-button");
      download.setIcon(DOWNLOAD_ICON);
      download.setImmediate(true);


      Panel documentPanel = createDocumentPanel(app, classId);

      HorizontalLayout buttons = new HorizontalLayout();
      buttons.setImmediate(true);
      buttons.setSpacing(true);
      buttons.addComponent(print);
      buttons.addComponent(download);

      VerticalLayout printLayout = new VerticalLayout();
      printLayout.setSizeFull();
      printLayout.addComponent(buttons);
      printLayout.addComponent(documentPanel);
      printLayout.setExpandRatio(documentPanel, 1f);

      setCompositionRoot(printLayout);
    } catch (IOException e) {
      setCompositionRoot(new Label(e.getMessage()));
    }
  }

  private Panel createDocumentPanel(Application app, String classId) {
    Embedded document = new Embedded(null, new FileResource(htmlFile, app));
    document.setDebugId(classId);
    document.setType(Embedded.TYPE_BROWSER);
    document.setSizeFull();

    VerticalLayout documentLayout = new VerticalLayout();
    documentLayout.setMargin(true);
    documentLayout.setSizeFull();
    documentLayout.addComponent(document);
    documentLayout.setExpandRatio(document, 1f);

    Panel documentPanel = new Panel(documentLayout);
    documentPanel.setSizeFull();
    return documentPanel;
  }

  @Override
  public void detach() {
    if (htmlFile != null) {
      htmlFile.delete();
    }
    if (docxFile != null) {
      docxFile.delete();
    }
    super.detach();
  }

  final static class PrintAction implements Button.ClickListener {

    final String classId;

    public PrintAction(String classId) {
      this.classId = classId;
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
      String script = "try{var f = document.getElementById('" + classId + "').firstChild;"
        + "f.focus();"
        + "f.contentWindow.print();} catch(e){alert(e);}";
      event.getButton().getWindow().executeJavaScript(script);
    }
  }

  final static class DownloadAction implements Button.ClickListener {

    final File file;

    DownloadAction(File file) {
      this.file = file;
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
      Button button = event.getButton();
      button.getWindow().open(new FileDownloadResource(file, button.getApplication()), "_top", false);
    }
  }
}
