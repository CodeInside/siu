/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.supervisor;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import commons.Exceptions;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.runtime.Job;
import org.activiti.engine.runtime.JobQuery;
import org.activiti.engine.runtime.ProcessInstance;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.gses.activiti.ReadOnly;
import ru.codeinside.gses.beans.ActivitiBean;
import ru.codeinside.gses.cert.NameParts;
import ru.codeinside.gses.cert.X509;
import ru.codeinside.gses.lazyquerycontainer.LazyQueryContainer;
import ru.codeinside.gses.service.F1;
import ru.codeinside.gses.service.Fn;
import ru.codeinside.gses.webui.CertificateInvalid;
import ru.codeinside.gses.webui.CertificateReader;
import ru.codeinside.gses.webui.CertificateVerifier;
import ru.codeinside.gses.webui.CertificateVerifyClientProvider;
import ru.codeinside.gses.webui.Flash;
import ru.codeinside.gses.webui.components.sign.SignApplet;
import ru.codeinside.gses.webui.components.sign.SignAppletListener;
import ru.codeinside.gses.webui.components.sign.SignUtils;
import ru.codeinside.gses.webui.executor.ExecutorFactory;
import ru.codeinside.jpa.ActivitiEntityManager;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.io.Serializable;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static ru.codeinside.gses.webui.utils.Components.stringProperty;

final public class ExceptionsPanel extends VerticalLayout {

  final Persistence persistence = new Persistence();
  final LazyQueryContainer container = persistence.createContainer();
  final ErrorBlock errorBlock = new ErrorBlock();
  final Table table = new Table(null, container);
  final PersistenceFilter filter = new PersistenceFilter(table, persistence);

  public ExceptionsPanel() {
    setSizeFull();
    setMargin(true);
    setSpacing(true);

    addComponent(filter);

    table.setDescription("Процессы, приостановленные из-за ошибок");
    table.setSizeFull();
    table.setColumnHeaders(new String[]{
        "Заявка", "Дата подачи заявки", "Процедура", "Версия", "Процесс", "Ветвь", "Ошибка"
    });
    table.setColumnIcon("eid", new ThemeResource("icon/branch.png"));
    table.setSelectable(true);
    table.setNullSelectionAllowed(true);
    table.setImmediate(true);
    table.addListener(new JobClickListener());
//    table.setColumnExpandRatio("e", 0.1f);
    addComponent(table);
    setExpandRatio(table, 0.3f);

    addComponent(errorBlock);
    setExpandRatio(errorBlock, 0.7f);
  }

  final private class JobClickListener implements Property.ValueChangeListener {

    @Override
    public void valueChange(Property.ValueChangeEvent event) {
      final Table table = (Table) event.getProperty();
      final Object index = table.getValue();
      String jobId = null;
      if (index != null) {
        final Item item = table.getItem(index);
        jobId = Fn.getValue(item, "jid", String.class);
      }
      errorBlock.setJobInfo(persistence.getSingle(jobId), index);
    }
  }

  final static class JobInfo implements Serializable {
    final String jobId;
    final String executionId;
    final String definitionId;
    final String exception;

    JobInfo(final String jobId, final String executionId, final String definitionId, final String exception) {
      this.jobId = jobId;
      this.executionId = executionId;
      this.definitionId = definitionId;
      this.exception = exception;
    }

    JobInfo copy() {
      return new JobInfo(jobId, executionId, definitionId, null);
    }
  }

  final static class Persistence extends SimpleQuery implements FilterablePersistence {

    private EntityManager em = ActivitiEntityManager.INSTANCE;
    private Map<FilterableValue, Object> filters = new HashMap<FilterableValue, Object>();

    public Persistence() {
      super(false, 10);
      addProperty("bid", Long.class, null, true, false);
      addProperty("startDate", String.class, null, true, false);
      addProperty("name", String.class, null, true, false);
      addProperty("ver", String.class, null, true, false);
      addProperty("pid", String.class, null, true, false);
      addProperty("eid", String.class, null, true, false);
      addProperty("e", String.class, null, true, false);
    }

    @Override
    public void addFilter(FilterableValue key, Object value) {
      if (value != null) {
        filters.put(key, value);
      } else if (filters != null && filters.containsKey(key)) {
        filters.remove(key);
      }
    }

    static JobQuery createQuery(final ProcessEngine engine, final String processInstanceFilter) {
      JobQuery jobQuery = engine
          .getManagementService()
          .createJobQuery()
          .withException()
          .withRetriesLeft();
      if (processInstanceFilter != null) {
        return jobQuery.processInstanceId(processInstanceFilter);
      }
      return jobQuery; // переопределённое поведение - поиск retries==0
    }

    private List<Object[]> getFilteredJobs(int startIndex, int count) {
      Query query = createNativeQuery(false)
          .setFirstResult(startIndex)
          .setMaxResults(count);
      return query.getResultList();
    }

    private int getFilteredJobsCount() {
      Query query = createNativeQuery(true);
      try {
        return ((Long) query.getSingleResult()).intValue();
      } catch (NoResultException ignore) {
        return 0;
      }
    }

    private Query createNativeQuery(final boolean isCount) {
      String sql = "SELECT " + (isCount ? "COUNT(j.*) " :
          "j.id_, j.execution_id_, j.process_Instance_id_, j.exception_msg_, b.id, b.datecreated, b.tag, p.name, p.version ") +
          "FROM act_ru_job j " +
          "LEFT JOIN bid b ON j.process_instance_id_ = b.processinstanceid " +
          "LEFT JOIN procedure p ON p.id = b.procedure_id ";

      if (filters != null && filters.size() > 0) {
        String processInstanceId = (String) filters.get(FilterableValue.PROCESS_INSTANCE_ID);
        Date dateFrom = filters.get(FilterableValue.DATE_FROM) instanceof Date ? (Date) filters.get(FilterableValue.DATE_FROM) : null;
        Date dateTo = filters.get(FilterableValue.DATE_TO) instanceof Date ? (Date) filters.get(FilterableValue.DATE_TO) : null;
        String procedureName = (String) filters.get(FilterableValue.PROCEDURE_NAME);
        boolean isAdded = false;

        if (processInstanceId != null) {
          sql += "WHERE j.process_instance_id_ = '" + processInstanceId + "'";
          isAdded = true;
        }
        if (dateFrom != null) {
          sql += (isAdded ? " AND" : "WHERE ") + " b.datecreated >= '" + dateFrom + "'";
          isAdded = true;
        }
        if (dateTo != null) {
          sql += (isAdded ? " AND" : "WHERE ") + " b.datecreated <= '" + dateTo + "'";
          isAdded = true;
        }
        if (procedureName != null) {
          procedureName = procedureName.toLowerCase();
          sql += (isAdded ? " AND" : "WHERE ") + " lower(p.name) LIKE '" + procedureName + "%'";
        }
      }
      return em.createNativeQuery(sql);
    }

    static Job getFailedJob(final ProcessEngine engine, String jobId) {
      return createQuery(engine, null)
          .jobId(jobId)
          .singleResult();
    }


    @Override
    public int size() {
      return getFilteredJobsCount();
    }

    @Override
    public List<Item> loadItems(final int startIndex, final int count) {
      return constructItems(startIndex, count);
    }

    public JobInfo getSingle(String jobId) {
      if (jobId == null) {
        return null;
      }
      return Fn.withEngine(new Single(), jobId);
    }

    public void restartJob(String jobId) {
      if (jobId != null) {
        Fn.withEngine(new Restart(), jobId);
      }
    }

    public void executeJob(String jobId) {
      if (jobId != null) {
        Fn.withEngine(new Execute(), jobId);
      }
    }

    private List<Item> constructItems(final Integer startIndex, final Integer count) {
      final List<Object[]> resultList = getFilteredJobs(startIndex, count);
      final List<Item> items = new ArrayList<Item>(resultList.size());
      for (final Object[] result : resultList) {
        final PropertysetItem item = new PropertysetItem();
        String jobId = (String) result[0];
        String executionId = (String) result[1];
        String processInstanceId = (String) result[2];
        String errorMessage = (String) result[3];
        Long bidId = (Long) result[4];
        Date dateCreated = (Date) result[5];
        String bidTag = (String) result[6];
        String procedureName = (String) result[7];
        String procedureVersion = (String) result[8];

        item.addItemProperty("jid", stringProperty(jobId));
        if (!executionId.equals(processInstanceId)) {
          item.addItemProperty("eid", stringProperty(executionId));
        }
        item.addItemProperty("pid", stringProperty(processInstanceId));
        item.addItemProperty("e", stringProperty(errorMessage));

        if (dateCreated != null) {
          item.addItemProperty("bid", new ObjectProperty<Long>(bidId));
          item.addItemProperty("startDate", stringProperty(ExecutorFactory.formatter.format(dateCreated)));
          if (procedureName != null) {
            if (bidTag == null || bidTag.isEmpty()) {
              item.addItemProperty("name", stringProperty(procedureName));
            } else {
              item.addItemProperty("name", stringProperty(bidTag + " - " + procedureName));
            }
            item.addItemProperty("ver", stringProperty(procedureVersion));
          }
        }

        items.add(item);
      }
      return items;
    }

    // --- functions ---

    final private static class Restart implements F1<Boolean, String> {
      @Override
      public Boolean apply(final ProcessEngine engine, final String jobId) {
        final Job job = getFailedJob(engine, jobId);
        if (job == null) {
          return false;
        }
        engine.getManagementService().setJobRetries(jobId, 1);
        return true;
      }
    }

    final private static class Single implements F1<JobInfo, String> {
      @Override
      public JobInfo apply(final ProcessEngine processEngine, final String jobId) {
        final Job job = getFailedJob(processEngine, jobId);
        if (job == null) {
          return null;
        }
        final ProcessInstance processInstance = processEngine.getRuntimeService().createProcessInstanceQuery().processInstanceId(job.getProcessInstanceId()).singleResult();
        final String exception = processEngine.getManagementService().getJobExceptionStacktrace(jobId);
        return new JobInfo(job.getId(), job.getExecutionId(), processInstance.getProcessDefinitionId(), exception);
      }
    }

    final private static class Execute implements F1<Boolean, String> {
      @Override
      public Boolean apply(final ProcessEngine engine, final String jobId) {
        final Job job = getFailedJob(engine, jobId);
        if (job == null) {
          return false;
        }
        final ManagementService managementService = engine.getManagementService();
        managementService.setJobRetries(jobId, 1);
        managementService.executeJob(jobId);
        return true;
      }
    }
  }

  final class ErrorBlock extends HorizontalLayout {
    final TextArea textArea = new TextArea("Стек ошибки:");
    final Button restart = new Button("Возобновить");
    final Button execute = new Button("Выполнить");
    final Button delete = new Button("Отклонить заявку");
    final VerticalLayout diagramLayout = new VerticalLayout();
    Object itemId;

    JobInfo info;

    ErrorBlock() {
      setSizeFull();
      setSpacing(true);

      restart.addListener(new RestartListener());
      restart.setDescription("Возобновить исполнение процесса в фоновом режиме");
      execute.addListener(new ExecuteListener());
      execute.setDescription("Выполнить процесс немедленно");
      delete.addListener(new DeleteBidListener());
      textArea.setSizeFull();
      textArea.setStyleName("small");
      diagramLayout.setCaption("Схема приостановленного процеса:");
      diagramLayout.setSizeFull();

      final VerticalLayout wrapper = new VerticalLayout();
      wrapper.setSizeUndefined();
      wrapper.setSpacing(true);
      wrapper.setWidth(130, UNITS_PIXELS);
      wrapper.addComponent(restart);
      wrapper.addComponent(execute);
      wrapper.addComponent(delete);

      final VerticalLayout buttonsLayout = new VerticalLayout();
      buttonsLayout.addComponent(wrapper);
      buttonsLayout.setSizeFull();
      buttonsLayout.setWidth(130, UNITS_PIXELS);

      addComponent(buttonsLayout);
      addComponent(diagramLayout);
      addComponent(textArea);

      setExpandRatio(diagramLayout, 0.42f);
      setExpandRatio(textArea, 0.42f);

      setJobInfo(null, null);
    }

    public void setJobInfo(final JobInfo jobInfo, final Object itemId) {
      final boolean enabled = jobInfo != null;
      info = enabled ? jobInfo.copy() : null;
      textArea.setReadOnly(false);
      textArea.setValue(enabled ? jobInfo.exception : "");
      textArea.setReadOnly(true);
      textArea.setEnabled(enabled);
      restart.setEnabled(enabled);
      execute.setEnabled(enabled);
      delete.setEnabled(enabled);
      diagramLayout.setEnabled(enabled);
      diagramLayout.removeAllComponents();
      if (enabled) {
        diagramLayout.addComponent(new DiagramPanel(jobInfo.definitionId, jobInfo.executionId));
      }
      this.itemId = itemId;
    }

    void refresh() {
      container.refresh();
      table.setValue(null);
    }

    final private class RestartListener implements Button.ClickListener {
      @Override
      public void buttonClick(Button.ClickEvent event) {
        final String id = info.jobId;
        setJobInfo(null, null);
        persistence.restartJob(id);
        refresh();
      }
    }

    final private class ExecuteListener implements Button.ClickListener {
      @Override
      public void buttonClick(Button.ClickEvent event) {
        final String id = info.jobId;
        final String branch = "Маршрут " + info.definitionId + ", ветвь " + info.executionId;
        setJobInfo(null, null);
        try {
          persistence.executeJob(id);
        } catch (RuntimeException e) {
          getWindow().addWindow(new StackTraceDialog(branch + " - ошибка исполнения", e));
        }
        refresh();
      }
    }

    final private class DeleteBidListener implements Button.ClickListener {
      @Override
      public void buttonClick(Button.ClickEvent event) {
        final Window mainWindow = getWindow();
        final Window rejectWindow = new Window();
        rejectWindow.setWidth("38%");
        rejectWindow.center();
        rejectWindow.setCaption("Внимание!");
        final VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSpacing(true);
        verticalLayout.setMargin(true);
        final Label messageLabel = new Label("Введите причину отклонения заявки");
        messageLabel.setStyleName("h2");
        final Label lockHint = new Label();
        lockHint.setStyleName("h2");
        final Label lockTimeHint = new Label();
        final TextArea textArea = new TextArea();
        textArea.setSizeFull();
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSpacing(true);
        buttons.setSizeFull();
        final Button ok = new Button("Ok");
        Button cancel = new Button("Cancel");

        buttons.addComponent(ok);
        buttons.addComponent(cancel);
        buttons.setExpandRatio(ok, 0.99f);
        verticalLayout.addComponent(lockHint);
        verticalLayout.addComponent(lockTimeHint);
        verticalLayout.addComponent(messageLabel);
        verticalLayout.addComponent(textArea);
        verticalLayout.addComponent(buttons);
        verticalLayout.setExpandRatio(textArea, 0.99f);
        rejectWindow.setContent(verticalLayout);
        mainWindow.addWindow(rejectWindow);

        Button.ClickListener ok1 = new Button.ClickListener() {
          @Override
          public void buttonClick(Button.ClickEvent event) {
            ok.setEnabled(false);
            verticalLayout.removeComponent(messageLabel);
            verticalLayout.removeComponent(textArea);
            final byte[] block;
            final String textAreaValue = (String) textArea.getValue();
            if (textAreaValue != null) {
              block = textAreaValue.getBytes();
            } else {
              block = null;
            }
            final Label reason = new Label(textAreaValue);
            reason.setCaption("Причина отказа:");
            verticalLayout.addComponent(reason, 0);
            event.getButton().removeListener(this);

            SignApplet signApplet = new SignApplet(new SignAppletListener() {

              @Override
              public void onLoading(SignApplet signApplet) {
                reason.setVisible(true);
                lockHint.setVisible(false);
                lockTimeHint.setVisible(false);
              }

              @Override
              public void onNoJcp(SignApplet signApplet) {
                verticalLayout.removeComponent(signApplet);
                ReadOnly field = new ReadOnly("В вашей операционной системе требуется установить КриптоПРО JCP", false);
                verticalLayout.addComponent(field);

              }

              @Override
              public void onCert(SignApplet signApplet, X509Certificate certificate) {
                boolean ok = false;
                String errorClause = null;
                try {
                  boolean link = AdminServiceProvider.getBoolProperty(CertificateVerifier.LINK_CERTIFICATE);
                  String login = Flash.login();
                  if (link) {
                    byte[] x509 = AdminServiceProvider.get().withEmployee(login, new CertificateReader());
                    ok = Arrays.equals(x509, certificate.getEncoded());
                  } else {
                    ok = true;
                  }
                  CertificateVerifyClientProvider.getInstance().verifyCertificate(certificate);

                  long certSerialNumber = certificate.getSerialNumber().longValue();
                  SignUtils.removeLockedCert(login, certSerialNumber);
                } catch (CertificateEncodingException ignore) {
                } catch (CertificateInvalid err) {
                  errorClause = err.getMessage();
                  ok = false;
                }
                if (ok) {
                  signApplet.block(1, 1);
                } else {
                  NameParts subject = X509.getSubjectParts(certificate);
                  String fieldValue = (errorClause == null) ? "Сертификат " + subject.getShortName() + " отклонён" : errorClause;
                  ReadOnly field = new ReadOnly(fieldValue, false);
                  verticalLayout.addComponent(field, 0);
                }
              }

              @Override
              public void onWrongPassword(SignApplet signApplet, long certSerialNumber) {
                reason.setVisible(false);

                String login = Flash.login();
                String unlockTime = SignUtils.lockCertAndGetUnlockTimeMessage(login, certSerialNumber);
                if (unlockTime != null) {
                  verticalLayout.removeComponent(signApplet);

                  lockHint.setValue(SignUtils.LOCK_CERT_HINT);
                  lockHint.setVisible(true);

                  lockTimeHint.setValue(unlockTime);
                  lockTimeHint.setVisible(true);
                } else {
                  lockHint.setValue(SignUtils.WRONG_CERT_PASSWORD_HINT);
                  lockHint.setVisible(true);

                  lockTimeHint.setValue(SignUtils.certAttemptsCountMessage(login, certSerialNumber));
                  lockTimeHint.setVisible(true);
                }
              }

              @Override
              public void onBlockAck(SignApplet signApplet, int i) {
                logger().fine("AckBlock:" + i);
                signApplet.chunk(1, 1, block);
              }

              @Override
              public void onChunkAck(SignApplet signApplet, int i) {
                logger().fine("AckChunk:" + i);
              }

              @Override
              public void onSign(SignApplet signApplet, byte[] sign) {
                final int i = signApplet.getBlockAck();
                logger().fine("done block:" + i);
                if (i < 1) {
                  signApplet.block(i + 1, 1);
                } else {
                  verticalLayout.removeComponent(signApplet);
                  NameParts subjectParts = X509.getSubjectParts(signApplet.getCertificate());
                  Label field2 = new Label(subjectParts.getShortName());
                  field2.setCaption("Подписано сертификатом:");
                  verticalLayout.addComponent(field2, 0);
                  ok.setEnabled(true);
                }
              }

              private Logger logger() {
                return Logger.getLogger(getClass().getName());
              }
            });
            byte[] x509 = AdminServiceProvider.get().withEmployee(Flash.login(), new CertificateReader());
            if (x509 != null) {
              signApplet.setSignMode(x509);
            } else {
              signApplet.setUnboundSignMode();
            }
            verticalLayout.addComponent(signApplet, 0);

            ok.addListener(new Button.ClickListener() {
              @Override
              public void buttonClick(Button.ClickEvent event) {

                ActivitiBean.get().deleteProcessInstanceById(info.executionId, textAreaValue);
                AdminServiceProvider.get().createLog(Flash.getActor(), "activiti.processInstanceId", info.executionId, "remove",
                    "Отклонить заявку", true);
                table.setContainerDataSource(persistence.createContainer());
                table.setValue(null);
                setJobInfo(null, null);
                mainWindow.removeWindow(rejectWindow);
              }
            });
          }
        };
        ok.addListener(ok1);

        cancel.addListener(new Button.ClickListener() {
          @Override
          public void buttonClick(Button.ClickEvent event) {

            setJobInfo(null, null);
            table.refreshRowCache();
            mainWindow.removeWindow(rejectWindow);
          }
        });
      }
    }
  }

  final static class StackTraceDialog extends Window {
    public StackTraceDialog(final String caption, final Throwable cause) {
      super(caption);
      setWidth(66, UNITS_PERCENTAGE);
      setHeight(66, UNITS_PERCENTAGE);
      setBorder(BORDER_MINIMAL);
      setModal(true);
      setClosable(true);
      getContent().setSizeFull();

      final TextArea stackTrace = new TextArea("Стек ошибки:");
      stackTrace.setValue(Exceptions.trimToCauseString(cause));
      stackTrace.setReadOnly(true);
      stackTrace.setSizeFull();
      stackTrace.setStyleName("small");
      addComponent(stackTrace);

    }

  }

}
