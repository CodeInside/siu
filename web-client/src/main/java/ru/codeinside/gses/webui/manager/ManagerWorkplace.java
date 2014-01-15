/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.manager;

import au.com.bytecode.opencsv.CSVWriter;
import com.google.common.base.Function;
import com.vaadin.Application;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.provider.CachingLocalEntityProvider;
import com.vaadin.terminal.DownloadStream;
import com.vaadin.terminal.StreamResource;
import com.vaadin.ui.*;
import org.activiti.engine.TaskService;
import org.tepi.filtertable.FilterTable;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.database.Directory;
import ru.codeinside.adm.database.ProcedureType;
import ru.codeinside.adm.ui.TableEmployeeFilterDecorator;
import ru.codeinside.gses.apservice.ApServiceForm;
import ru.codeinside.gses.apservice.ApServiceTable;
import ru.codeinside.gses.beans.DirectoryBeanProvider;
import ru.codeinside.gses.manager.ProcedureFilter;
import ru.codeinside.gses.manager.ProcedureForm;
import ru.codeinside.gses.manager.ProcedureTable;
import ru.codeinside.gses.service.Functions;
import ru.codeinside.gses.service.impl.DeclarantServiceImpl;
import ru.codeinside.gses.webui.DeclarantTypeChanged;
import ru.codeinside.gses.webui.Flash;
import ru.codeinside.gses.webui.gws.TRef;
import ru.codeinside.gses.webui.osgi.TRefRegistryImpl;
import ru.codeinside.gses.webui.utils.Components;
import ru.codeinside.gws.api.Server;

import javax.persistence.EntityManagerFactory;
import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class ManagerWorkplace extends VerticalLayout {
  private ApServiceForm ap;
  private static final long serialVersionUID = 111L;
  private final Component servicePanel;
  private final Component procedurePanel;
  private final Component directoryPanel;
  private ProcedureForm procedureForm;
  private ProcedureTable procedureTable;
  private ProcedureFilter filter;

  public ManagerWorkplace() {
    setSizeFull();
    servicePanel = createServicePanel();
    procedurePanel = createProcedurePanel1();
    directoryPanel = DirectoryPanel.createDirectoryPanel();
    final MenuBar menu = new MenuBar();
    menu.setWidth("100%");
    menu.addStyleName("submenu");
    MenuBar.MenuItem servicesItem = menu.addItem("Услуги", new MenuBar.Command() {
      @Override
      public void menuSelected(MenuBar.MenuItem selectedItem) {
        chooseTab(selectedItem, menu, servicePanel, procedurePanel, directoryPanel);
      }
    });
    MenuBar.MenuItem administrativeProceduresItem = menu.addItem("Административные процедуры", new MenuBar.Command() {
      @Override
      public void menuSelected(MenuBar.MenuItem selectedItem) {
        chooseTab(selectedItem, menu, procedurePanel, servicePanel, directoryPanel);
        activateApInterface();
      }
    });
    menu.addItem("Межведомственные процедуры", new MenuBar.Command() {
      @Override
      public void menuSelected(MenuBar.MenuItem selectedItem) {
        chooseTab(selectedItem, menu, procedurePanel, servicePanel, directoryPanel);
        activateMpInterface();
      }
    });
    menu.addItem("Ведение справочников", new MenuBar.Command() {
      @Override
      public void menuSelected(MenuBar.MenuItem selectedItem) {
        chooseTab(selectedItem, menu, directoryPanel, servicePanel, procedurePanel);
        activateMpInterface();
      }
    });
    addComponent(menu);
    setExpandRatio(menu, 0.01f);
    administrativeProceduresItem.getCommand().menuSelected(administrativeProceduresItem);
  }

  private void activateApInterface() {
    filter.setServiceFilterEnabled(true);
    procedureTable.bindToDataSource(ProcedureType.Administrative);
    procedureForm.clean();
    procedureForm.setProcedureType(ProcedureType.Administrative);
  }

  private void activateMpInterface() {
    filter.setServiceFilterEnabled(false);
    procedureTable.bindToDataSource(ProcedureType.Interdepartmental);
    procedureForm.clean();
    procedureForm.setProcedureType(ProcedureType.Interdepartmental);
  }


  private void chooseTab(MenuBar.MenuItem selectedItem, MenuBar menu, Component componentToShow, Component... componentToHides) {
    List<MenuBar.MenuItem> items = menu.getItems();
    for (MenuBar.MenuItem i : items) {
      i.setEnabled(true);
    }
    selectedItem.setEnabled(false);
    for (Component componentToHide : componentToHides) {
      if (components.contains(componentToHide)) {
        removeComponent(componentToHide);
      }
    }
    addComponent(componentToShow);
    setExpandRatio(componentToShow, 0.99f);
  }


  private Component createProcedurePanel1() {
    HorizontalLayout hl = new HorizontalLayout();
    hl.setSpacing(true);
    hl.setWidth("100%");
    hl.setHeight("100%");
    Panel panel00 = new Panel();
    panel00.setHeight("100%");
    panel00.setWidth("100%");
    Panel panel01 = new Panel();
    panel01.setWidth("100%");
    panel01.setHeight("100%");
    Panel panel10 = new Panel();
    panel10.setSizeFull();
    panel10.setScrollable(false);
    hl.addComponent(panel00);
    hl.addComponent(panel01);

    VerticalLayout vl = new VerticalLayout();
    vl.setSizeFull();
    vl.setMargin(true);
    vl.setSpacing(true);
    vl.addComponent(hl);
    vl.addComponent(panel10);

    hl.setExpandRatio(panel00, 0.33f);
    hl.setExpandRatio(panel01, 0.77f);
    vl.setExpandRatio(hl, 0.6f);
    vl.setExpandRatio(panel10, 0.4f);

    procedureForm = new ProcedureForm();
    procedureTable = new ProcedureTable(procedureForm);
    filter = createFilteringForm(procedureTable);
    panel00.addComponent(filter);
    panel01.addComponent(procedureForm);
    panel10.addComponent(procedureTable);
    ap.addItemSetChangeListener(filter);
    ap.addItemSetChangeListener(procedureForm);
    return vl;
  }

  static void refreshDirectoryTable(FilterTable directoryTable) {
    directoryTable.resetFilters();
    ((JPAContainer<Directory>) directoryTable.getContainerDataSource()).refresh();
    directoryTable.requestRepaint();
  }

  static Button createDeleteEntryButton(final Table dirMapTable, final String dirName, final String key) {
    Button button = new Button("Удалить");
    button.addListener(new Button.ClickListener() {
      @Override
      public void buttonClick(Button.ClickEvent event) {
        DirectoryBeanProvider.get().remove(dirName, key);
        AdminServiceProvider.get().createLog(Flash.getActor(), "Directory value", dirName, "remove",
            "key => ".concat(key), true);
        reloadMap(dirName, dirMapTable);
        if (DeclarantServiceImpl.DECLARANT_TYPES.equals(dirName)) {
          Flash.fire(new DeclarantTypeChanged(this));
        }
      }
    });
    return button;
  }

  static void reloadMap(final String dirName, final Table dirMapTable) {
    dirMapTable.removeAllItems();
    int index = 0;
    Map<String, String> values = DirectoryBeanProvider.get().getValues(dirName);
    for (final Map.Entry<String, String> entry : values.entrySet()) {
      Button button = createDeleteEntryButton(dirMapTable, dirName, entry.getKey());
      dirMapTable.addItem(new Object[]{entry.getKey(), entry.getValue(), button}, index++);
    }
  }

  static FilterTable createDirectoryTable() {
    final FilterTable directoryTable = new FilterTable();
    directoryTable.setPageLength(0);
    directoryTable.setFilterDecorator(new TableEmployeeFilterDecorator());
    directoryTable.setSizeFull();
    directoryTable.setFilterBarVisible(true);
    directoryTable.setSelectable(true);
    directoryTable.setImmediate(true);
    directoryTable.setRowHeaderMode(Table.ROW_HEADER_MODE_HIDDEN);
    directoryTable.setColumnCollapsingAllowed(true);
    directoryTable.setColumnReorderingAllowed(true);
    directoryTable.setMultiSelect(false);

    return directoryTable;
  }

  static Table createDirectoryMapTable() {
    final Table mapTable = Components.createTable("100%", "100%");
    mapTable.setPageLength(0);
    mapTable.setCaption("Значения справочника");
    mapTable.addContainerProperty("key", String.class, null);
    mapTable.addContainerProperty("value", String.class, null);
    mapTable.addContainerProperty("form", Component.class, null);

    mapTable.setVisibleColumns(new Object[]{"key", "value", "form"});
    mapTable.setColumnHeaders(new String[]{"Ключ", "Значение", ""});
    mapTable.setSelectable(false);
    return mapTable;
  }

  static void buildContainer(final FilterTable table, final Form createFieldForm, final Table mapTable) {
    EntityManagerFactory myPU = AdminServiceProvider.get().getMyPU();
    final JPAContainer<Directory> container = new JPAContainer<Directory>(Directory.class);
    container.setReadOnly(true);
    container.setEntityProvider(new CachingLocalEntityProvider<Directory>(Directory.class, myPU.createEntityManager()));
    table.setContainerDataSource(container);
    table.addGeneratedColumn("buttonDelete", new CustomTable.ColumnGenerator() {

      private static final long serialVersionUID = 1L;

      public Object generateCell(final CustomTable source, final Object itemId, final Object columnId) {
        Button button = new Button("Удалить", new Button.ClickListener() {

          private static final long serialVersionUID = 1L;

          public void buttonClick(Button.ClickEvent event) {
            String dirName = table.getItem(itemId).getItemProperty("name").toString();
            DirectoryBeanProvider.get().delete(dirName);
            AdminServiceProvider.get().createLog(Flash.getActor(), "Directory", dirName, "remove",
                "key => ".concat(dirName), true);
            refreshDirectoryTable(table);
            mapTable.setVisible(false);
            createFieldForm.setVisible(false);
            if (DeclarantServiceImpl.DECLARANT_TYPES.equals(dirName)) {
              Flash.fire(new DeclarantTypeChanged(this));
            }
          }
        });
        return button;
      }
    });
    table.addGeneratedColumn("buttonDownload", new CustomTable.ColumnGenerator() {

      private static final long serialVersionUID = 1L;

      public Object generateCell(final CustomTable source, final Object itemId, final Object columnId) {
        Button downloads = new Button("Выгрузить", new Button.ClickListener() {

          @Override
          public void buttonClick(Button.ClickEvent event) {
            final Application appl = event.getButton().getApplication();
            try {
              String dirName = table.getItem(itemId).getItemProperty("name").toString();
              AdminServiceProvider.get().createLog(Flash.getActor(), "Directory", dirName, "download",
                  "key => ".concat(dirName), true);
              final OutputStream outp = new ByteArrayOutputStream();
              CSVWriter csvWriter = new CSVWriter(new OutputStreamWriter(outp));

              Map<String, String> values = DirectoryBeanProvider.get().getValues(dirName);
              for (Map.Entry<String, String> entry : values.entrySet()) {
                csvWriter.writeNext(new String[]{dirName, entry.getKey(), entry.getValue()});
              }
              csvWriter.flush();

              StreamResource.StreamSource streamSource = new StreamResource.StreamSource() {

                private static final long serialVersionUID = 456334952891567271L;

                public InputStream getStream() {
                  return Functions.withTask(new Function<TaskService, InputStream>() {
                    public InputStream apply(TaskService s) {
                      return new ByteArrayInputStream(((ByteArrayOutputStream) outp).toByteArray());
                    }
                  });
                }
              };

              StreamResource resource = new StreamResource(streamSource, dirName + ".csv", appl) {

                private static final long serialVersionUID = -3869546661105572851L;

                public DownloadStream getStream() {
                  final StreamSource ss = getStreamSource();
                  if (ss == null) {
                    return null;
                  }
                  final DownloadStream ds = new DownloadStream(ss.getStream(), getMIMEType(), getFilename());
                  ds.setBufferSize(getBufferSize());
                  ds.setCacheTime(getCacheTime());
                  ds.setParameter("Content-Disposition", "attachment; filename=" + getFilename());
                  return ds;
                }
              };
              csvWriter.close();
              Window window = event.getButton().getWindow();
              window.open(resource);
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
        });
        return downloads;
      }
    });
    table.setVisibleColumns(new Object[]{"name", "buttonDelete", "buttonDownload"});
  }

  byte[] csvData;
  byte[] fileData;
  String filename;
  //TODO отрефакторить


  public static void fillServerTable(final Table table) {
    table.removeAllItems();
    List<TRef<Server>> serverRefs = TRefRegistryImpl.getServerRefs();
    int i = 0;
    for (final TRef<Server> ref : serverRefs) {
      if (ref.getRef() != null) {
        Button b = new Button("Выгрузить", new UndeployButtonListener(ref, table));
        URL wsdlUrl = ref.getRef().getWsdlUrl();
        table.addItem(new Object[]{ref.getName(), ref.getSymbolicName(), ref.getVersion(), ref.getLocation(), ref.getRef().getRevision(), wsdlUrl != null ? wsdlUrl.toString() : "", b}, i++);
      }
    }
  }

  private ProcedureFilter createFilteringForm(ProcedureTable procedureTable) {
    ProcedureFilter filter = new ProcedureFilter("Фильтр");
    filter.setProcedureTable(procedureTable);
    return filter;
  }

  private VerticalLayout createServicePanel() {
    final VerticalLayout serviceVerticalLayout = new VerticalLayout();
    serviceVerticalLayout.setSizeFull();
    ap = new ApServiceForm();
    Panel apForm = new Panel();
    apForm.addComponent(ap);
    apForm.setHeight("350px");
    Panel importForm = new Panel();
    ApServiceTable apServiceTable = new ApServiceTable(ap);
    apServiceTable.setSizeFull();
    Form form = new Form(new ServiceWidget(apServiceTable));
    form.setCaption("Импортировать услуги");
    importForm.addComponent(form);
    importForm.setHeight("350px");
    HorizontalLayout hr = new HorizontalLayout();
    hr.setSizeFull();
    hr.setSpacing(true);
    hr.addComponent(apForm);
    hr.addComponent(importForm);
    serviceVerticalLayout.addComponent(hr);
    Panel apTable = new Panel("Список доступных услуг");
    apTable.addComponent(apServiceTable);
    apTable.setSizeFull();
    serviceVerticalLayout.addComponent(apTable);
    serviceVerticalLayout.setSpacing(true);
    serviceVerticalLayout.setMargin(true);
    return serviceVerticalLayout;
  }
}