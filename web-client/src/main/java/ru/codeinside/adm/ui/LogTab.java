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
import com.vaadin.ui.*;
import com.vaadin.ui.Component;
import org.tepi.filtertable.FilterTable;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.database.OepLog;
import ru.codeinside.adm.database.SoapPacket;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

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

    public SmevLog() {
      sl = new FilterTable();
      addComponent(sl);
      setSizeFull();
      sl.setSizeFull();
      sl.setFilterBarVisible(true);
      sl.setSelectable(true);
      sl.setImmediate(true);
      sl.setRowHeaderMode(Table.ROW_HEADER_MODE_ID);
      sl.setColumnCollapsingAllowed(true);
      sl.setColumnReorderingAllowed(true);
      sl.setFilterDecorator(new TableEmployeeFilterDecorator());

      //TODO: не ясно ещё как поведёт себя в кластере, нужно проверить
      EntityManagerFactory myPU = AdminServiceProvider.get().getMyPU();
      final JPAContainer<OepLog> container = new JPAContainer<OepLog>(OepLog.class);
      container.setReadOnly(true);
      final EntityManager em = myPU.createEntityManager();
      container.setEntityProvider(new CachingLocalEntityProvider<OepLog>(OepLog.class, em));

      sl.setContainerDataSource(container);
      sl.setVisibleColumns(
        new Object[]{"date"
          , "bidId"
          , "marker"
          , "infoSystem"
          , "client"
          , "logDate"});
      sl.setColumnHeaders(new String[]{"Дата", "№ заявки", "Маркер", "Информационная система", "Клиент", "Дата лога"});
      sl.setSortContainerPropertyId("date");
      sl.setSortAscending(false);
      sl.addGeneratedColumn("date", new DateColumnGenerator("dd.MM.yyyy HH:mm:ss.SSS"));
      sl.addListener(new Property.ValueChangeListener() {
        @Override
        public void valueChange(Property.ValueChangeEvent event) {
          if(event.getProperty().getValue() == null) {
            return;
          }
          SmevLog.this.removeAllComponents();
          HorizontalLayout hl = new HorizontalLayout();
          hl.setMargin(true);
          hl.setSpacing(true);
          hl.setSizeFull();
          VerticalLayout verticalLayout = new VerticalLayout();
          verticalLayout.setMargin(true);
          verticalLayout.setSpacing(true);
          verticalLayout.setSizeFull();
          Button back = new Button("Назад");
          back.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
              SmevLog.this.removeAllComponents();
              SmevLog.this.addComponent(sl);
              sl.setValue(null);
            }
          });
          verticalLayout.addComponent(back);
          verticalLayout.addComponent(hl);
          verticalLayout.setExpandRatio(hl, 1);
          SmevLog.this.addComponent(verticalLayout);
          Panel sendPanel = new Panel("Отправленое сообщение");
          sendPanel.setSizeFull();
          FormLayout sendForm = new FormLayout();
          sendForm.setReadOnly(true);
          sendPanel.setContent(sendForm);
          hl.addComponent(sendPanel);
          Panel receivePanel = new Panel("Принятое сообщение");
          receivePanel.setSizeFull();
          FormLayout receiveForm = new FormLayout();
          receiveForm.setReadOnly(true);
          receivePanel.setContent(receiveForm);
          hl.addComponent(receivePanel);
          OepLog oepLog = em.find(OepLog.class, event.getProperty().getValue());
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
