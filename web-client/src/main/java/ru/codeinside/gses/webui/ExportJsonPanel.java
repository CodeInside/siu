/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2015, MPL CodeInside http://codeinside.ru
 */
package ru.codeinside.gses.webui;

import com.google.common.collect.ImmutableList;
import com.vaadin.Application;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerItem;
import com.vaadin.addon.jpacontainer.provider.LocalEntityProvider;
import com.vaadin.data.Container;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.terminal.StreamResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.CustomTable;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;
import org.tepi.filtertable.FilterTable;
import ru.codeinside.adm.database.ExportFormEntity;
import ru.codeinside.adm.ui.DateColumnGenerator;
import ru.codeinside.adm.ui.FilterDecorator_;
import ru.codeinside.adm.ui.FilterGenerator_;
import ru.codeinside.jpa.ActivitiEntityManager;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.logging.Logger;

final public class ExportJsonPanel extends CustomComponent {
  private static final Logger logger = Logger.getLogger(ExportJsonPanel.class.getName());

  private boolean filterByLogin = false;

  public ExportJsonPanel(boolean filterByLogin) {
    this.filterByLogin = filterByLogin;
    setCompositionRoot(build());
    setSizeFull();
  }

  public ExportJsonPanel() {
    this(false);
  }

  private Component build() {
    final JPAContainer<ExportFormEntity> container = new JPAContainer<ExportFormEntity>(ExportFormEntity.class);
    container.setEntityProvider(new LocalEntityProvider<ExportFormEntity>(ExportFormEntity.class, ActivitiEntityManager.INSTANCE));
    container.addNestedContainerProperty("employee.login");
    container.addNestedContainerProperty("bid.id");

    if (filterByLogin) {
      Container.Filter byLogin = new Compare.Equal("employee.login", Flash.flash().getLogin());
      container.addContainerFilter(byLogin);
    }

    final Button.ClickListener exportPkcs7Listener = new Button.ClickListener() {
      @Override
      public void buttonClick(Button.ClickEvent clickEvent) {
        handleExportPkcs7Click(clickEvent);
      }
    };

    FilterTable table = new FilterTable();
    table.setContainerDataSource(container);
    table.addGeneratedColumn("date", new DateColumnGenerator("dd.MM.yyyy HH:mm:ss"));
    table.addGeneratedColumn("pkcs7", new CustomTable.ColumnGenerator() {
      @Override
      public Object generateCell(CustomTable customTable, Object itemId, Object columnId) {
        Button btn = new Button("Сохранить");
        btn.addStyleName(Reindeer.BUTTON_LINK);
        btn.setData(customTable.getItem(itemId));
        btn.addListener(exportPkcs7Listener);
        return btn;
      }
    });
    table.setColumnWidth("pkcs7", 105);
    table.setFilterBarVisible(true);
    table.setFilterDecorator(new FilterDecorator_());
    table.setFilterGenerator(new FilterGenerator_(ImmutableList.of("bid.id"), null));
    table.setVisibleColumns(
        new String[]{"bid.id", "employee.login", "date", "procedure", "task", "pkcs7"});
    table.setColumnHeaders(
        new String[]{"Заявка", "Сотрудник", "Дата подписания", "Процедура", "Этап", "Данные формы"});
    table.setNullSelectionAllowed(true);
    table.setImmediate(true);
    table.setFilterFieldVisible("pkcs7", false);
    table.setSizeFull();

    Button refreshButton = new Button("Обновить", new Button.ClickListener() {
      @Override
      public void buttonClick(Button.ClickEvent clickEvent) {
        container.refresh();
      }
    });
    refreshButton.addStyleName(Reindeer.BUTTON_LINK);

    VerticalLayout layout = new VerticalLayout();
    layout.setSpacing(true);
    layout.setSizeFull();
    layout.addComponent(refreshButton);
    layout.addComponent(table);
    layout.setExpandRatio(table, 1f);
    return layout;
  }

  public static void handleExportPkcs7Click(Button.ClickEvent clickEvent) {
    JPAContainerItem<?> item = (JPAContainerItem<?>) clickEvent.getButton().getData();
    ExportFormEntity entity = (ExportFormEntity) item.getEntity();
    try {
      StreamResource downloadResource = getDownloadResource(entity, clickEvent.getButton().getApplication());
      clickEvent.getButton().getWindow().open(downloadResource);
    } catch (IOException e) {
      clickEvent.getButton().getWindow().showNotification("Ошибка", "Загрузка не удалась", Window.Notification.TYPE_ERROR_MESSAGE);
      logger.severe("Error while export pkcs7: " + e.getMessage());
    }
  }

  private static StreamResource getDownloadResource(ExportFormEntity entity, Application app) throws IOException {
    StreamResource.StreamSource downloadSource = new DownloadSource(entity.asZip());

    String encodedFilename = entity.getExportFileName("zip");
    try {
      encodedFilename = URLEncoder.encode(encodedFilename, "UTF-8");
    } catch (UnsupportedEncodingException ignored) {
    }

    return new StreamResource(downloadSource, encodedFilename, app);
  }

  private static final class DownloadSource implements StreamResource.StreamSource {

    private final ByteArrayInputStream stream;

    DownloadSource(byte[] data) {
      this.stream = new ByteArrayInputStream(data);
    }

    @Override
    public InputStream getStream() {
      return stream;
    }
  }
}
