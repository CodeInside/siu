/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.utils;

import com.google.common.base.Function;
import com.vaadin.Application;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.terminal.DownloadStream;
import com.vaadin.terminal.StreamResource;
import com.vaadin.terminal.StreamResource.StreamSource;
import com.vaadin.terminal.gwt.server.WebBrowser;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Link;
import com.vaadin.ui.Table;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.BaseTheme;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.task.Attachment;
import org.tepi.filtertable.FilterTable;
import ru.codeinside.gses.service.Functions;

import javax.mail.internet.MimeUtility;
import java.io.InputStream;
import java.net.URI;
import java.util.Set;

public class Components {

  public static Window showComponent(ClickEvent event, CustomComponent putComponent, String caption) {
    Window mainWindow = event.getButton().getApplication().getMainWindow();
    return showComponent(mainWindow, putComponent, caption);
  }

  public static Window showComponent(Window mainwindow, CustomComponent putComponent, String caption) {
    Window subwindow = createWindow(mainwindow, caption);
    subwindow.addComponent(putComponent);
    return subwindow;
  }

  public static Window createWindow(Window mainwindow, String caption) {
    Window subwindow = new Window(caption);
    subwindow.setSizeUndefined();
    subwindow.getContent().setSizeUndefined();
    subwindow.setScrollable(false);
    subwindow.setResizable(false);
    subwindow.setPositionX(50);
    subwindow.setPositionY(50);
    mainwindow.addWindow(subwindow);
    return subwindow;
  }

  public static Table createTable(String width, String height) {
    Table table = new Table();
    table.setImmediate(false);
    table.setWidth(width);
    table.setHeight(height);
    table.setEditable(false);
    table.setImmediate(true);
    table.setSelectable(true);
    table.setNullSelectionAllowed(false);

    return table;
  }

  public static FilterTable createFilterTable(String width, String height) {
    FilterTable table = new FilterTable();
    table.setImmediate(false);
    table.setWidth(width);
    table.setHeight(height);
    table.setEditable(false);
    table.setImmediate(true);
    table.setSelectable(true);
    table.setNullSelectionAllowed(false);

    return table;
  }

  public static Table createProcessRouteTable(final ProcessDefinitionEntity entity, String width, String height) {
    Table result = Components.createTable(width, height);
    result.addContainerProperty("id", String.class, null);
    result.addContainerProperty("name", String.class, null);
    result.addContainerProperty("type", String.class, null);
    result.addContainerProperty("accessPermissions", String.class, null);
    result.setSortDisabled(true);
    result.setColumnHeaders(new String[]{"Номер", "Название", "Тип узла", "Права доступа"});

    int index = 0;
    for (ActivityImpl ac : entity.getActivities()) {
      String candidateUser = "";
      String candidateGroup = "";

      if (ac.getActivityBehavior() instanceof UserTaskActivityBehavior) {
        UserTaskActivityBehavior utab = (UserTaskActivityBehavior) ac.getActivityBehavior();

        Set<Expression> candidateUserIdExpressions = utab.getTaskDefinition().getCandidateUserIdExpressions();
        if (candidateUserIdExpressions.size() > 0) {
          candidateUser = "Пользователи: " + candidateUserIdExpressions;
        }
        Set<Expression> candidateGroupIdExpressions = utab.getTaskDefinition().getCandidateGroupIdExpressions();
        if (candidateGroupIdExpressions.size() > 0) {
          candidateUser = "Группы: " + candidateGroupIdExpressions;
        }
      }
      String actName = ac.getProperty("name") != null ? ac.getProperty("name").toString() : "Без названия";
      result.addItem(new Object[]{ac.getId(), actName, ac.getProperty("type").toString(),
        candidateUser + " " + candidateGroup}, index++);
    }
    return result;
  }

  public static ObjectProperty<String> stringProperty(final Object o, final String defaultName) {
    return stringProperty(o != null ? o.toString() : null, defaultName);
  }

  public static ObjectProperty<String> stringProperty(final String name) {
    return stringProperty(name, " ");
  }

  public static ObjectProperty<String> stringProperty(final String name, final String defaultName) {
    return new ObjectProperty<String>(name != null ? name : defaultName);
  }

  public static ObjectProperty<Component> buttonProperty(final String name, final String defaultName,
                                                         ClickListener listener) {
    return buttonProperty(name != null ? name : defaultName, listener);
  }

  public static ObjectProperty<Component> buttonProperty(final String name, ClickListener listener) {
    Button b = createButton(name, listener);
    return new ObjectProperty<Component>(b);
  }

  public static Button createButton(final String name, ClickListener listener) {
    Button result = new Button(name);
    result.setStyleName(BaseTheme.BUTTON_LINK);
    result.addListener(listener);
    return result;
  }

  public static Link createAttachShowButton(final Attachment attachment, final Application appl) {
    if (attachment == null) {
      return null;
    }
    final Link result = new Link();
    result.setCaption(attachment.getName());
    result.setTargetName("_top");
    result.setImmediate(true);
    String description = attachment.getDescription();
    result.setDescription("Скачать" + (description == null ? "" : (" " + description)));
    StreamSource streamSource = new StreamSource() {

      private static final long serialVersionUID = 456334952891567271L;

      public InputStream getStream() {
        return Functions.withTask(new Function<TaskService, InputStream>() {
          public InputStream apply(TaskService s) {
            return s.getAttachmentContent(attachment.getId());
          }
        });
      }
    };
    StreamResource resource = new StreamResource(streamSource, attachment.getId() + "/" + attachment.getName(), appl) {

      private static final long serialVersionUID = -3869546661105572851L;

      public DownloadStream getStream() {
        final StreamSource ss = getStreamSource();
        if (ss == null) {
          return null;
        }
        final DownloadStream ds = new DownloadStream(ss.getStream(), getMIMEType(), getFilename());
        ds.setBufferSize(getBufferSize());
        ds.setCacheTime(0);
        try {
          WebBrowser browser = (WebBrowser) result.getWindow().getTerminal();
          if (browser.isIE()) {
            URI uri = new URI(null, null, attachment.getName(), null);
            ds.setParameter("Content-Disposition", "attachment; filename=" + uri.toASCIIString());
          } else {
            ds.setParameter("Content-Disposition", "attachment; filename=\"" + MimeUtility.encodeWord(attachment.getName(), "utf-8", "Q") + "\"");
          }
        } catch (Exception e) {
          ds.setParameter("Content-Disposition", "attachment; filename=" + attachment.getName());
        }
        return ds;
      }
    };
    String type = attachment.getType();
    if (type != null) {
      resource.setMIMEType(type);
    }
    result.setResource(resource);
    return result;
  }

  public static void showMessage(SucceededEvent event, String message, int type) {
    Window window = currentWindow(event);
    if (window != null) {
      window.showNotification(message, type);
    }
  }

  public static Window currentWindow(SucceededEvent event) {
    return event.getUpload().getWindow();
  }
}
