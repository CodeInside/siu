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
import com.vaadin.terminal.DownloadStream;
import com.vaadin.terminal.StreamResource;
import com.vaadin.ui.*;
import com.vaadin.ui.Component;
import com.vaadin.ui.themes.BaseTheme;
import org.tepi.filtertable.FilterTable;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.database.SoapPacket;

import javax.persistence.EntityManager;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;

public class LogTab extends VerticalLayout implements TabSheet.SelectedTabChangeListener {

  FilterTable sl;
  FilterTable ul;
  SmevLog smevLog;
  TabSheet tabSheet;

  EntityManager em;
  EntityManager logEm;

  public LogTab() {
    setSizeFull();
    tabSheet = new TabSheet();
    tabSheet.addListener(this);
    tabSheet.setSizeFull();
    addComponent(tabSheet);
  }

  @Override
  public void attach() {
    createEms();
    super.attach();
  }


  @Override
  public void detach() {
    closeEms();
    super.detach();
  }

  final class SmevLog extends VerticalLayout {

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
      sl.setFilterDecorator(new FilterDecorator_());
      sl.setFilterGenerator(new FilterGenerator_(null, Arrays.asList("client")));
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


      sl.setContainerDataSource(jpaContainer(ru.codeinside.adm.database.SmevLog.class, em));
      sl.setVisibleColumns(new String[]{"bidId", "infoSystem", "component", "client", "logDate"});
      sl.setColumnHeaders(new String[]{"№ заявки", "Информационная система", "Модуль", "Клиент", "Дата"});
      sl.setSortContainerPropertyId("logDate");
      sl.setSortAscending(false);
      sl.addGeneratedColumn("client", new YesNoColumnGenerator());
      sl.addGeneratedColumn("logDate", new DateColumnGenerator("dd.MM.yyyy HH:mm:ss.SSS"));
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
          final ru.codeinside.adm.database.SmevLog oepLog = em.find(ru.codeinside.adm.database.SmevLog.class, event.getProperty().getValue());
          if (oepLog != null) {

            final SoapPacket sendPacket = oepLog.getSendPacket();
            if (sendPacket != null) {
              sendForm.addComponent(new RoTextField("Sender", sendPacket.getSender()));
              sendForm.addComponent(new RoTextField("Recipient", sendPacket.getRecipient()));
              sendForm.addComponent(new RoTextField("Originator", sendPacket.getOriginator()));
              sendForm.addComponent(new RoTextField("Service", sendPacket.getService()));
              sendForm.addComponent(new RoTextField("Type code", sendPacket.getTypeCode()));
              sendForm.addComponent(new RoTextField("Status", sendPacket.getStatus()));
              sendForm.addComponent(new RoTextField("Date", sendPacket.getDate().toString()));
              sendForm.addComponent(new RoTextField("Request ID ref", sendPacket.getRequestIdRef()));
              sendForm.addComponent(new RoTextField("Origin request ID ref", sendPacket.getOriginRequestIdRef()));
              sendForm.addComponent(new RoTextField("Service code", sendPacket.getServiceCode()));
              sendForm.addComponent(new RoTextField("Case number", sendPacket.getCaseNumber()));
              sendForm.addComponent(new RoTextField("Exchange type", sendPacket.getExchangeType()));
            }

            if (oepLog.getSendHttp() != null) {
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

                  String ddMMyy_hhmmss = new SimpleDateFormat("ddMMyy_hhmmss").format(oepLog.getLogDate());
                  StreamResource resource = new StreamResource(
                    streamSource,
                    oepLog.getComponent() + "_send_" + ddMMyy_hhmmss + ".log",
                    event.getButton().getApplication()
                  ) {
                    private static final long serialVersionUID = -3869546661105532851L;

                    public DownloadStream getStream() {
                      final StreamSource ss = getStreamSource();
                      if (ss == null) {
                        return null;
                      }
                      DownloadStream ds = new DownloadStream(ss.getStream(), "text/plain", getFilename());
                      ds.setParameter("Content-Disposition", "attachment; filename=\"" + getFilename() + "\"");
                      return ds;
                    }
                  };
                  Window window = event.getButton().getWindow();
                  window.open(resource, "_top", false);
                }
              });
              sendForm.addComponent(sendHttp);
            }

            final SoapPacket receivePacket = oepLog.getReceivePacket();
            if (receivePacket != null) {
              receiveForm.addComponent(new RoTextField("Sender", receivePacket.getSender()));
              receiveForm.addComponent(new RoTextField("Recipient", receivePacket.getRecipient()));
              receiveForm.addComponent(new RoTextField("Originator", receivePacket.getOriginator()));
              receiveForm.addComponent(new RoTextField("Service", receivePacket.getService()));
              receiveForm.addComponent(new RoTextField("Type code", receivePacket.getTypeCode()));
              receiveForm.addComponent(new RoTextField("Status", receivePacket.getStatus()));
              receiveForm.addComponent(new RoTextField("Date", receivePacket.getDate().toString()));
              receiveForm.addComponent(new RoTextField("Request ID ref", receivePacket.getRequestIdRef()));
              receiveForm.addComponent(new RoTextField("Origin request ID ref", receivePacket.getOriginRequestIdRef()));
              receiveForm.addComponent(new RoTextField("Service code", receivePacket.getServiceCode()));
              receiveForm.addComponent(new RoTextField("Case number", receivePacket.getCaseNumber()));
              receiveForm.addComponent(new RoTextField("Exchange type", receivePacket.getExchangeType()));
            }
            if (oepLog.getReceiveHttp() != null) {
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
                  String ddMMyy_hhmmss = new SimpleDateFormat("ddMMyy_hhmmss").format(oepLog.getLogDate());
                  StreamResource resource = new StreamResource(
                    streamSource,
                    oepLog.getComponent() + "_receive_" + ddMMyy_hhmmss + ".log",
                    event.getButton().getApplication()
                  ) {
                    private static final long serialVersionUID = -3869546661105537851L;

                    public DownloadStream getStream() {
                      final StreamSource ss = getStreamSource();
                      if (ss == null) {
                        return null;
                      }
                      DownloadStream ds = new DownloadStream(ss.getStream(), "text/plain", getFilename());
                      ds.setParameter("Content-Disposition", "attachment; filename=\"" + getFilename() + "\"");
                      return ds;
                    }
                  };
                  Window window = event.getButton().getWindow();
                  window.open(resource, "_top", false);
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

  final class UserLog extends FilterTable {

    public UserLog() {
      ul = this;
      setSizeFull();
      setFilterBarVisible(true);
      setSelectable(true);
      setImmediate(true);
      setRowHeaderMode(Table.ROW_HEADER_MODE_ID);
      setColumnCollapsingAllowed(true);
      setColumnReorderingAllowed(true);
      setFilterDecorator(new FilterDecorator_());

      //TODO: не ясно ещё как поведёт себя в кластере, нужно проверить
      JPAContainer<ru.codeinside.log.Log> container = jpaContainer(ru.codeinside.log.Log.class, logEm);
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
    if (this == tab) {
      if (smevLog == null) {
        tabSheet.addTab(smevLog = new SmevLog(), "СМЭВ");
        tabSheet.addTab(new UserLog(), "Действия пользователя");
      }
      return;
    }
    if (this == tab || smevLog == tab) {
      ((JPAContainer) sl.getContainerDataSource()).getEntityProvider().refresh();
      sl.refreshRowCache();
    }
    if (this == tab || ul == tab) {
      ((JPAContainer) ul.getContainerDataSource()).getEntityProvider().refresh();
      ul.refreshRowCache();
    }
  }

  private void createEms() {
    if (em == null) {
      em = AdminServiceProvider.get().getMyPU().createEntityManager();
    }
    if (logEm == null) {
      logEm = AdminServiceProvider.get().getLogPU().createEntityManager();
    }
  }

  private void closeEms() {
    if (em != null) {
      em.close();
      em = null;
    }
    if (logEm != null) {
      logEm.close();
      logEm = null;
    }
  }

  //TODO: не ясно ещё как поведёт себя в кластере, нужно проверить
  <T> JPAContainer<T> jpaContainer(Class<T> clazz, EntityManager entityManager) {
    JPAContainer<T> container = new JPAContainer<T>(clazz);
    container.setReadOnly(true);
    container.setEntityProvider(new CachingLocalEntityProvider<T>(clazz, entityManager));
    return container;
  }

}
