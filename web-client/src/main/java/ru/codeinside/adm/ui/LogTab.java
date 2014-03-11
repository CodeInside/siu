/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.ui;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.provider.CachingLocalEntityProvider;
import com.vaadin.data.Property;
import com.vaadin.terminal.StreamResource;
import com.vaadin.ui.*;
import com.vaadin.ui.Component;
import com.vaadin.ui.themes.BaseTheme;
import org.tepi.filtertable.FilterTable;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.database.OepLog;
import ru.codeinside.adm.database.SoapPacket;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class LogTab extends VerticalLayout implements TabSheet.SelectedTabChangeListener {

  FilterTable sl;
  FilterTable ul;
  SmevLog smevLog;
  TabSheet tabSheet;

  public LogTab() {
    setSizeFull();
    tabSheet = new TabSheet();
    smevLog = new SmevLog();
    tabSheet.addTab(smevLog, "СМЕВ");
    tabSheet.addTab(new UserLog(), "Действия пользователя");
    tabSheet.addListener(this);
    tabSheet.setSizeFull();
    addComponent(tabSheet);
  }

  public class SmevLog extends VerticalLayout {

    VerticalSplitPanel splitPanel;

    public SmevLog() {
      splitPanel = new VerticalSplitPanel();
      sl = new FilterTable();
      splitPanel.setFirstComponent(sl);
      addComponent(splitPanel);
      setSizeFull();
      sl.setSizeFull();
      sl.setFilterBarVisible(true);
      sl.setSelectable(true);
      sl.setImmediate(true);
      sl.setRowHeaderMode(Table.ROW_HEADER_MODE_ID);
      sl.setColumnCollapsingAllowed(true);
      sl.setColumnReorderingAllowed(true);
      sl.setFilterDecorator(new TableEmployeeFilterDecorator());

      final HorizontalLayout hl = new HorizontalLayout();
      hl.setMargin(true);
      hl.setSpacing(true);
      hl.setSizeFull();
      splitPanel.setSecondComponent(hl);
      Panel sendPanel = new Panel("Отправленое сообщение");
      sendPanel.setSizeFull();
      final FormLayout sendForm = new FormLayout();
      sendPanel.setContent(sendForm);
      hl.addComponent(sendPanel);
      Panel receivePanel = new Panel("Принятое сообщение");
      receivePanel.setSizeFull();
      final FormLayout receiveForm = new FormLayout();
      receivePanel.setContent(receiveForm);
      hl.addComponent(receivePanel);
      final Panel error = new Panel("Error");
      error.setSizeFull();


      //TODO: не ясно ещё как поведёт себя в кластере, нужно проверить
      EntityManagerFactory myPU = AdminServiceProvider.get().getMyPU();
      final JPAContainer<OepLog> container = new JPAContainer<OepLog>(OepLog.class);
      container.setReadOnly(true);
      final EntityManager em = myPU.createEntityManager();
      container.setEntityProvider(new CachingLocalEntityProvider<OepLog>(OepLog.class, em));

      sl.setContainerDataSource(container);
      sl.setVisibleColumns(
        new Object[]{"bidId"
          , "infoSystem"
          , "client"
          , "logDate"});
      sl.setColumnHeaders(new String[]{"№ заявки", "Информационная система", "Клиент", "Дата лога"});
      sl.setSortContainerPropertyId("date");
      sl.setSortAscending(false);
      sl.addGeneratedColumn("date", new DateColumnGenerator("dd.MM.yyyy HH:mm:ss.SSS"));
      sl.addListener(new Property.ValueChangeListener() {
        @Override
        public void valueChange(Property.ValueChangeEvent event) {
          sendForm.removeAllComponents();
          receiveForm.removeAllComponents();
          error.removeAllComponents();
          hl.removeComponent(error);
          if (event.getProperty().getValue() == null) {
            return;
          }
          final OepLog oepLog = em.find(OepLog.class, event.getProperty().getValue());
          if (oepLog != null) {
            SoapPacket sendPacket = oepLog.getSendPacket();
            if (sendPacket != null) {
              sendForm.addComponent(new RoTextField("Sender", sendPacket.getSender()));
              sendForm.addComponent(new RoTextField("Recipient", sendPacket.getRecipient()));
              sendForm.addComponent(new RoTextField("Originator", sendPacket.getOriginator()));
              sendForm.addComponent(new RoTextField("Service", sendPacket.getService()));
              sendForm.addComponent(new RoTextField("Type code", sendPacket.getTypeCode()));
              sendForm.addComponent(new RoTextField("Status", sendPacket.getStatus()));
              sendForm.addComponent(new RoTextField("Date", sendPacket.getDate()));
              sendForm.addComponent(new RoTextField("Request ID ref", sendPacket.getRequestIdRef()));
              sendForm.addComponent(new RoTextField("Origin request ID ref", sendPacket.getOriginRequestIdRef()));
              sendForm.addComponent(new RoTextField("Service code", sendPacket.getServiceCode()));
              sendForm.addComponent(new RoTextField("Case number", sendPacket.getCaseNumber()));
              sendForm.addComponent(new RoTextField("Exchange type", sendPacket.getExchangeType()));

              Button sendHttp = new Button("Скачать http-log");
              sendHttp.addStyleName(BaseTheme.BUTTON_LINK);
              sendHttp.addListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                  StreamResource.StreamSource streamSource = new StreamResource.StreamSource() {

                    private static final long serialVersionUID = 456334952891567271L;

                    public InputStream getStream() {
                      return new ByteArrayInputStream(oepLog.getSendHttp().getData());
                    }
                  };

                  StreamResource resource = new StreamResource(streamSource, "send_http", event.getButton().getApplication());
                  Window window = event.getButton().getWindow();
                  window.open(resource);
                }
              });
              sendForm.addComponent(sendHttp);
            }

            SoapPacket receivePacket = oepLog.getReceivePacket();
            if (receivePacket != null) {
              receiveForm.addComponent(new RoTextField("Sender", receivePacket.getSender()));
              receiveForm.addComponent(new RoTextField("Recipient", receivePacket.getRecipient()));
              receiveForm.addComponent(new RoTextField("Originator", receivePacket.getOriginator()));
              receiveForm.addComponent(new RoTextField("Service", receivePacket.getService()));
              receiveForm.addComponent(new RoTextField("Type code", receivePacket.getTypeCode()));
              receiveForm.addComponent(new RoTextField("Status", receivePacket.getStatus()));
              receiveForm.addComponent(new RoTextField("Date", receivePacket.getDate()));
              receiveForm.addComponent(new RoTextField("Request ID ref", receivePacket.getRequestIdRef()));
              receiveForm.addComponent(new RoTextField("Origin request ID ref", receivePacket.getOriginRequestIdRef()));
              receiveForm.addComponent(new RoTextField("Service code", receivePacket.getServiceCode()));
              receiveForm.addComponent(new RoTextField("Case number", receivePacket.getCaseNumber()));
              receiveForm.addComponent(new RoTextField("Exchange type", receivePacket.getExchangeType()));
              Button receiveHttp = new Button("Скачать http-log");
              receiveHttp.addStyleName(BaseTheme.BUTTON_LINK);
              receiveHttp.addListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                  StreamResource.StreamSource streamSource = new StreamResource.StreamSource() {

                    private static final long serialVersionUID = 456334952891567271L;

                    public InputStream getStream() {
                      return new ByteArrayInputStream(oepLog.getReceiveHttp().getData());
                    }
                  };

                  StreamResource resource = new StreamResource(streamSource, "receive_http", event.getButton().getApplication());
                  Window window = event.getButton().getWindow();
                  window.open(resource);
                }
              });
              receiveForm.addComponent(receiveHttp);
            }
            if (oepLog.getError() != null && !oepLog.getError().equals("")) {
              FormLayout newContent = new FormLayout();
              newContent.addComponent(new Label(oepLog.getError()));
              error.setContent(newContent);
              hl.addComponent(error);
            }
          }
        }
      });
    }


    private class RoTextField extends Label {
      RoTextField(String caption, String value) {
        super(value);
        setCaption(caption);
      }
    }
  }

  public class UserLog extends FilterTable {

    public UserLog() {
      ul = this;
      setSizeFull();
      setFilterBarVisible(true);
      setSelectable(true);
      setImmediate(true);
      setRowHeaderMode(Table.ROW_HEADER_MODE_ID);
      setColumnCollapsingAllowed(true);
      setColumnReorderingAllowed(true);
      setFilterDecorator(new TableEmployeeFilterDecorator());

      //TODO: не ясно ещё как поведёт себя в кластере, нужно проверить
      EntityManagerFactory logPU = AdminServiceProvider.get().getLogPU();
      final JPAContainer<ru.codeinside.log.Log> container = new JPAContainer<ru.codeinside.log.Log>(ru.codeinside.log.Log.class);
      container.setReadOnly(true);
      container.setEntityProvider(new CachingLocalEntityProvider<ru.codeinside.log.Log>(ru.codeinside.log.Log.class, logPU.createEntityManager()));
      container.addNestedContainerProperty("actor.name");
      container.addNestedContainerProperty("actor.ip");
      container.addNestedContainerProperty("actor.browser");
      container.addNestedContainerProperty("actor.os");
      setContainerDataSource(container);
      setVisibleColumns(
        new Object[]{"actor.name", "actor.browser", "actor.ip", "actor.os", "entityName", "entityId", "action", "info", "date"});
      setColumnHeaders(new String[]{"Субъект", "Браузер", "IP", "ОС", "Объект", "ID", "Действие", "Дополнительно", "Дата"});
      setSortContainerPropertyId("date");
      setSortAscending(false);
      addGeneratedColumn("date", new DateColumnGenerator("dd.MM.yyyy HH:mm:ss.SSS"));
    }
  }

  /**
   * сбрасываем состояние.
   */
  @Override
  public void selectedTabChange(TabSheet.SelectedTabChangeEvent event) {
    Component tab = event.getTabSheet().getSelectedTab();
    if (this == tab || this.smevLog == tab) {
      ((JPAContainer) sl.getContainerDataSource()).getEntityProvider().refresh();
      sl.refreshRowCache();
    }
    if (this == tab || this.ul == tab) {
      ((JPAContainer) ul.getContainerDataSource()).getEntityProvider().refresh();
      ul.refreshRowCache();
    }
  }

}
