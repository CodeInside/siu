/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2015, MPL CodeInside http://codeinside.ru
 */
package ru.codeinside.gses.webui;

import com.google.common.collect.ImmutableList;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerItem;
import com.vaadin.addon.jpacontainer.provider.LocalEntityProvider;
import com.vaadin.terminal.StreamResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.CustomTable;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import org.tepi.filtertable.FilterTable;
import ru.codeinside.adm.database.ExportFormEntity;
import ru.codeinside.adm.ui.DateColumnGenerator;
import ru.codeinside.adm.ui.FilterDecorator_;
import ru.codeinside.adm.ui.FilterGenerator_;
import ru.codeinside.jpa.ActivitiEntityManager;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

final public class ExportJsonPanel extends CustomComponent {
  private FilterTable table;
  private JPAContainer<ExportFormEntity> container;

  public ExportJsonPanel() {
    setCompositionRoot(build());
    setSizeFull();
  }

  private Component build() {
    container = new JPAContainer<ExportFormEntity>(ExportFormEntity.class);
    container.setEntityProvider(new LocalEntityProvider<ExportFormEntity>(ExportFormEntity.class, ActivitiEntityManager.INSTANCE));
    container.addNestedContainerProperty("employee.login");
    container.addNestedContainerProperty("bid.id");

    final Button.ClickListener exportContentListener = new Button.ClickListener() {
      @Override
      public void buttonClick(Button.ClickEvent clickEvent) {
        handleDownloadClick(clickEvent, true);
      }
    };

    final Button.ClickListener exportSignatureListener = new Button.ClickListener() {
      @Override
      public void buttonClick(Button.ClickEvent clickEvent) {
        handleDownloadClick(clickEvent, false);
      }
    };

    table = new FilterTable();
    table.setContainerDataSource(container);
    table.addGeneratedColumn("date", new DateColumnGenerator("dd.MM.yyyy HH:mm:ss"));
    table.addGeneratedColumn("content", new CustomTable.ColumnGenerator() {
      @Override
      public Object generateCell(CustomTable customTable, Object itemId, Object columnId) {
        Button btn = new Button("Сохранить");
        btn.addStyleName(Reindeer.BUTTON_LINK);
        btn.setData(customTable.getItem(itemId));
        btn.addListener(exportContentListener);
        return btn;
      }
    });
    table.addGeneratedColumn("signature", new CustomTable.ColumnGenerator() {
      @Override
      public Object generateCell(CustomTable customTable, Object itemId, Object columnId) {
        Button btn = new Button("Сохранить");
        btn.addStyleName(Reindeer.BUTTON_LINK);
        btn.setData(customTable.getItem(itemId));
        btn.addListener(exportSignatureListener);
        return btn;
      }
    });
    table.setColumnWidth("content", 105);
    table.setColumnWidth("signature", 105);
    table.setFilterBarVisible(true);
    table.setFilterDecorator(new FilterDecorator_());
    table.setFilterGenerator(new FilterGenerator_(ImmutableList.of("bid.id"), null));
    table.setVisibleColumns(
        new String[]{"bid.id", "employee.login", "date", "procedure", "task", "content", "signature"});
    table.setColumnHeaders(
        new String[]{"Заявка", "Сотрудник", "Дата подписания", "Процедура", "Этап", "Данные формы", "Подпись"});
    table.setNullSelectionAllowed(true);
    table.setImmediate(true);
    table.setSizeFull();

    VerticalLayout layout = new VerticalLayout();
    layout.setSizeFull();
    layout.addComponent(table);
    layout.setExpandRatio(table, 1f);
    return layout;
  }

  private void handleDownloadClick(Button.ClickEvent clickEvent, boolean content) {
    JPAContainerItem<?> item = (JPAContainerItem<?>) clickEvent.getButton().getData();
    ExportFormEntity entity = (ExportFormEntity) item.getEntity();
    StreamResource downloadResource = getDownloadResource(entity, content);
    clickEvent.getButton().getWindow().open(downloadResource);
  }

  private StreamResource getDownloadResource(ExportFormEntity entity, boolean content) {
    StreamResource.StreamSource downloadSource = new DownloadSource(content ? entity.getJson() : entity.getPkcs7());
    String fileExt = content ? "json" : "sig";
    String filename = String.format("%s-%d-%s-%4$td-%4$tm-%4$tYT%4$tH-%4$tM-%4$tS.%5$s",
        entity.getEmployee().getLogin(), entity.getBid().getId(), entity.getTask(), entity.getDate(), fileExt);

    String encodedFilename = filename;
    try {
      encodedFilename = URLEncoder.encode(filename, "UTF-8");
    } catch (UnsupportedEncodingException ignored) { }

    return new StreamResource(downloadSource, encodedFilename, getApplication());
  }

  private final class DownloadSource implements StreamResource.StreamSource {

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
