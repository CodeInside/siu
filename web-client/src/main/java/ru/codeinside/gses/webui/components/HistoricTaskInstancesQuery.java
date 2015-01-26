/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.components;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.vaadin.data.Item;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.BaseTheme;
import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.task.Task;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.database.Bid;
import ru.codeinside.gses.lazyquerycontainer.Query;
import ru.codeinside.gses.service.Functions;
import ru.codeinside.gses.webui.Flash;
import ru.codeinside.gses.webui.executor.ArchiveFactory;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class HistoricTaskInstancesQuery implements Query, Serializable {
  private static final long serialVersionUID = 1L;
  private String processDefinitionId;
  private String taskId;

  public HistoricTaskInstancesQuery(String processDefinitionId, String taskId) {
    this.processDefinitionId = processDefinitionId;
    this.taskId = taskId;
  }

  @Override
  public int size() {
    List<HistoricTaskInstance> histories = Functions
        .withHistory(new Function<HistoryService, List<HistoricTaskInstance>>() {
          public List<HistoricTaskInstance> apply(HistoryService srv) {
            return srv.createHistoricTaskInstanceQuery().processInstanceId(processDefinitionId).list();
          }
        });
    return histories.size();
  }

  @Override
  public List<Item> loadItems(final int startIndex, final int count) {
    final Task task = Flash.flash().getProcessEngine().getTaskService().createTaskQuery().taskId(taskId).singleResult();
    final String tag = Flash.flash().getAdminService().getBidByTask(taskId).getTag();
    String procedureName = Flash.flash().getExecutorService().getProcedureNameByDefinitionId(task.getProcessDefinitionId());
    if (!tag.isEmpty()) {
      procedureName = tag + " - " + procedureName;
    }
    final String diagramTitle = procedureName;
    List<HistoricTaskInstance> histories = Functions
        .withHistory(new Function<HistoryService, List<HistoricTaskInstance>>() {
          public List<HistoricTaskInstance> apply(HistoryService srv) {
            return srv.createHistoricTaskInstanceQuery().processInstanceId(processDefinitionId).listPage(startIndex, count);
          }
        });
    List<Item> items = Lists.newArrayListWithExpectedSize(histories.size());
    for (final HistoricTaskInstance i : histories) {
      String startTime = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(i.getStartTime());
      String endTime = (i.getEndTime() != null) ? new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(i.getEndTime()) : "";
      Bid bid = AdminServiceProvider.get().getBidByTask(taskId);
      String bidId = bid != null ? bid.getId().toString() : "";
      PropertysetItem item = new PropertysetItem();
      item.addItemProperty("id", new ObjectProperty<String>(bidId));
      item.addItemProperty("hid", new ObjectProperty<HistoricTaskInstance>(i));
      item.addItemProperty("name", new ObjectProperty<String>(i.getName()));
      item.addItemProperty("procedure", new ObjectProperty<String>(procedureName));
      item.addItemProperty("startDate", new ObjectProperty<String>(startTime));
      item.addItemProperty("endDate", new ObjectProperty<String>(endTime));
      item.addItemProperty("assignee", new ObjectProperty<String>(i.getAssignee() != null ? i.getAssignee() : ""));
      Date time = i.getEndTime() == null ? i.getStartTime() : i.getEndTime();
      Button view = new Button();
      view.setStyleName(BaseTheme.BUTTON_LINK);
      view.setDescription("Просмотр");
      view.setIcon(new ThemeResource("../custom/icon/view20.png"));
      view.addListener(new ArchiveFactory.ShowClickListener(i.getTaskDefinitionKey(), bidId, time));
      view.setEnabled(i.getAssignee() != null);

      Button showDiagram = new Button();
      showDiagram.setStyleName(BaseTheme.BUTTON_LINK);
      showDiagram.setDescription("Схема");
      showDiagram.setIcon(new ThemeResource("../custom/icon/scheme20.png"));
      showDiagram.addListener(new Button.ClickListener() {
        @Override
        public void buttonClick(Button.ClickEvent event) {
          Bid bid = AdminServiceProvider.get().getBidByTask(taskId);
          final Window window = new Window(diagramTitle);
          window.setModal(true);
          VerticalLayout layout = new VerticalLayout();
          layout.setMargin(true);
          window.setContent(layout);

          final ShowDiagramComponentParameterObject param = new ShowDiagramComponentParameterObject();
          param.changer = new LayoutChanger(layout);
          param.processDefinitionId = task.getProcessDefinitionId();
          param.executionId = task.getExecutionId();
          param.windowHeader = bid == null ? "" : bid.getProcedure().getName() + " v. " + bid.getVersion();

          Execution execution = Flash.flash().getProcessEngine().getRuntimeService().createExecutionQuery().executionId(param.executionId).singleResult();

          if (execution == null) {
            layout.addComponent(new Label("Заявка уже исполнена"));
            window.center();
            event.getButton().getWindow().addWindow(window);
            return;
          }

          ShowDiagramComponent showDiagramComponent = new ShowDiagramComponent(param);
          layout.addComponent(showDiagramComponent);
          window.center();
          event.getButton().getWindow().addWindow(window);
        }
      });
      Button removeBidFromList = new Button();
      removeBidFromList.setStyleName(BaseTheme.BUTTON_LINK);
      removeBidFromList.setDescription("Убрать из списка");
      removeBidFromList.setIcon(new ThemeResource("../custom/icon/removeFromList20.png"));

      Button deleteBid = new Button();
      deleteBid.setStyleName(BaseTheme.BUTTON_LINK);
      deleteBid.setDescription("Отклонить заявку");
      deleteBid.setIcon(new ThemeResource("../custom/icon/delete20.png"));

      HorizontalLayout buttons = new HorizontalLayout();
      buttons.addComponent(view);
      buttons.addComponent(showDiagram);
      buttons.addComponent(removeBidFromList);
      buttons.addComponent(deleteBid);

      item.addItemProperty("form", new ObjectProperty<HorizontalLayout>(buttons));


//      deleteBidButton.addListener(new Button.ClickListener() {
//        @Override
//        public void buttonClick(Button.ClickEvent event) {
//          final Window mainWindow = getWindow();
//          final Window rejectWindow = new Window();
//          rejectWindow.setWidth("38%");
//          rejectWindow.center();
//          rejectWindow.setCaption("Внимание!");
//          final VerticalLayout verticalLayout = new VerticalLayout();
//          verticalLayout.setSpacing(true);
//          verticalLayout.setMargin(true);
//          final Label messageLabel = new Label("Введите причину отклонения заявки");
//          messageLabel.setStyleName("h2");
//          final TextArea textArea = new TextArea();
//          textArea.setSizeFull();
//          HorizontalLayout buttons = new HorizontalLayout();
//          buttons.setSpacing(true);
//          buttons.setSizeFull();
//          final Button ok = new Button("Ok");
//          Button cancel = new Button("Cancel");
//
//          buttons.addComponent(ok);
//          buttons.addComponent(cancel);
//          buttons.setExpandRatio(ok, 0.99f);
//          verticalLayout.addComponent(messageLabel);
//          verticalLayout.addComponent(textArea);
//          verticalLayout.addComponent(buttons);
//          verticalLayout.setExpandRatio(textArea, 0.99f);
//          rejectWindow.setContent(verticalLayout);
//          mainWindow.addWindow(rejectWindow);
//
//          Button.ClickListener ok1 = new Button.ClickListener() {
//            @Override
//            public void buttonClick(Button.ClickEvent event) {
//              ok.setEnabled(false);
//              verticalLayout.removeComponent(messageLabel);
//              verticalLayout.removeComponent(textArea);
//              final byte[] block;
//              final String textAreaValue = (String) textArea.getValue();
//              if (textAreaValue != null) {
//                block = textAreaValue.getBytes();
//              } else {
//                block = null;
//              }
//              Label reason = new Label(textAreaValue);
//              reason.setCaption("Причина отказа:");
//              verticalLayout.addComponent(reason, 0);
//              event.getButton().removeListener(this);
//
//              SignApplet signApplet = new SignApplet(new SignAppletListener() {
//
//                @Override
//                public void onLoading(SignApplet signApplet) {
//
//                }
//
//                @Override
//                public void onNoJcp(SignApplet signApplet) {
//                  verticalLayout.removeComponent(signApplet);
//                  ReadOnly field = new ReadOnly("В вашей операционной системе требуется установить КриптоПРО JCP", false);
//                  verticalLayout.addComponent(field);
//
//                }
//
//                @Override
//                public void onCert(SignApplet signApplet, X509Certificate certificate) {
//                  boolean ok = false;
//                  String errorClause = null;
//                  try {
//                    boolean link = AdminServiceProvider.getBoolProperty(CertificateVerifier.LINK_CERTIFICATE);
//                    if (link) {
//                      byte[] x509 = AdminServiceProvider.get().withEmployee(Flash.login(), new CertificateReader());
//                      ok = Arrays.equals(x509, certificate.getEncoded());
//                    } else {
//                      ok = true;
//                    }
//                    CertificateVerifyClientProvider.getInstance().verifyCertificate(certificate);
//                  } catch (CertificateEncodingException e) {
//                  } catch (CertificateInvalid err) {
//                    errorClause = err.getMessage();
//                    ok = false;
//                  }
//                  if (ok) {
//                    signApplet.block(1, 1);
//                  } else {
//                    NameParts subject = X509.getSubjectParts(certificate);
//                    String fieldValue = (errorClause == null) ? "Сертификат " + subject.getShortName() + " отклонён" : errorClause;
//                    ReadOnly field = new ReadOnly(fieldValue, false);
//                    verticalLayout.addComponent(field, 0);
//                  }
//                }
//
//                @Override
//                public void onBlockAck(SignApplet signApplet, int i) {
//                  logger().fine("AckBlock:" + i);
//                  signApplet.chunk(1, 1, block);
//                }
//
//                @Override
//                public void onChunkAck(SignApplet signApplet, int i) {
//                  logger().fine("AckChunk:" + i);
//                }
//
//                @Override
//                public void onSign(SignApplet signApplet, byte[] sign) {
//                  final int i = signApplet.getBlockAck();
//                  logger().fine("done block:" + i);
//                  if (i < 1) {
//                    signApplet.block(i + 1, 1);
//                  } else {
//                    verticalLayout.removeComponent(signApplet);
//                    NameParts subjectParts = X509.getSubjectParts(signApplet.getCertificate());
//                    Label field2 = new Label(subjectParts.getShortName());
//                    field2.setCaption("Подписано сертификатом:");
//                    verticalLayout.addComponent(field2, 0);
//                    ok.setEnabled(true);
//                  }
//                }
//
//                private Logger logger() {
//                  return Logger.getLogger(getClass().getName());
//                }
//              });
//              byte[] x509 = AdminServiceProvider.get().withEmployee(Flash.login(), new CertificateReader());
//              if (x509 != null) {
//                signApplet.setSignMode(x509);
//              } else {
//                signApplet.setUnboundSignMode();
//              }
//              verticalLayout.addComponent(signApplet, 0);
//
//              ok.addListener(new Button.ClickListener() {
//                @Override
//                public void buttonClick(Button.ClickEvent event) {
//                  Task result = Flash.flash().getProcessEngine().getTaskService().createTaskQuery().taskId(taskId).singleResult();
//                  if (result == null) {
//                    alreadyGone();
//                    return;
//                  }
//                  ActivitiBean.get().deleteProcessInstance(taskId, textAreaValue);
//                  AdminServiceProvider.get().createLog(Flash.getActor(), "activiti.task", taskId, "remove",
//                      "Отклонить заявку", true);
//                  Flash.fire(new TaskChanged(SupervisorWorkplace.this, taskId));
//                  infoChanger.change(infoComponent);
//                  controlledTasksTable.setValue(null);
//                  controlledTasksTable.refresh();
//                  mainWindow.removeWindow(rejectWindow);
//                }
//              });
//            }
//          };
//          ok.addListener(ok1);
//
//          cancel.addListener(new Button.ClickListener() {
//            @Override
//            public void buttonClick(Button.ClickEvent event) {
//
//              controlledTasksTable.refresh();
//              mainWindow.removeWindow(rejectWindow);
//            }
//          });
//        }
//      });

      items.add(item);
    }
    return items;
  }

  @Override
  public void saveItems(List<Item> addedItems, List<Item> modifiedItems, List<Item> removedItems) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean deleteAllItems() {
    return true;
    //    throw new UnsupportedOperationException();
  }

  @Override
  public Item constructItem() {
    throw new UnsupportedOperationException();
  }
}
