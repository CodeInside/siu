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
import ru.codeinside.adm.AdminService;
import ru.codeinside.adm.database.Bid;
import ru.codeinside.adm.database.Procedure;
import ru.codeinside.gses.lazyquerycontainer.LazyQueryContainer;
import ru.codeinside.gses.service.F1;
import ru.codeinside.gses.service.F3;
import ru.codeinside.gses.service.Fn;
import ru.codeinside.gses.webui.Flash;
import ru.codeinside.gses.webui.executor.ExecutorFactory;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

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
      errorBlock.setJobInfo(persistence.getSingle(jobId));
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

    private String processInstanceFilter;

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
    public void setProcessInstanceFilter(String processInstanceFilter) {
      this.processInstanceFilter = processInstanceFilter;
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

    static Job getFailedJob(final ProcessEngine engine, String jobId) {
      return createQuery(engine, null)
        .jobId(jobId)
        .singleResult();
    }


    @Override
    public int size() {
      return Fn.withEngine(new Count(), processInstanceFilter).intValue();
    }

    @Override
    public List<Item> loadItems(final int startIndex, final int count) {
      return Fn.withEngine(new Items(), processInstanceFilter, startIndex, count);
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

    final private static class Count implements F1<Long, String> {
      @Override
      public Long apply(final ProcessEngine engine, String processInstanceFilter) {
        return createQuery(engine, processInstanceFilter).count();
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


    final private static class Items implements F3<List<Item>, String, Integer, Integer> {
      @Override
      public List<Item> apply(final ProcessEngine engine, final String processInstanceFilter, final Integer startIndex, final Integer count) {
        final AdminService adminService = Flash.flash().getAdminService();
        final List<Job> jobs = createQuery(engine, processInstanceFilter).listPage(startIndex, count);
        final List<Item> items = new ArrayList<Item>(jobs.size());
        for (final Job job : jobs) {
          final PropertysetItem item = new PropertysetItem();
          item.addItemProperty("jid", stringProperty(job.getId()));
          if (!job.getExecutionId().equals(job.getProcessInstanceId())) {
            item.addItemProperty("eid", stringProperty(job.getExecutionId()));
          }
          item.addItemProperty("pid", stringProperty(job.getProcessInstanceId()));
          item.addItemProperty("e", stringProperty(job.getExceptionMessage()));

          final Bid bid = adminService.getBidByProcessInstanceId(job.getProcessInstanceId());
          if (bid != null) {
            item.addItemProperty("bid", new ObjectProperty<Long>(bid.getId()));
            item.addItemProperty("startDate", stringProperty(ExecutorFactory.formatter.format(bid.getDateCreated())));
            final Procedure procedure = bid.getProcedure();
            if (procedure != null) {
              if (bid.getTag().isEmpty()) {
                item.addItemProperty("name", stringProperty(procedure.getName()));
              } else {
                item.addItemProperty("name", stringProperty(bid.getTag() + " - " + procedure.getName()));
              }
              item.addItemProperty("ver", stringProperty(procedure.getVersion()));
            }
          }

          items.add(item);
        }
        return items;
      }
    }

  }

  final class ErrorBlock extends HorizontalLayout {
    final TextArea textArea = new TextArea("Стек ошибки:");
    final Button restart = new Button("Возобновить");
    final Button execute = new Button("Выполнить");
    final VerticalLayout diagramLayout = new VerticalLayout();

    JobInfo info;

    ErrorBlock() {
      setSizeFull();
      setSpacing(true);

      restart.addListener(new RestartListener());
      restart.setDescription("Возобновить исполнение процесса в фоновом режиме");
      execute.addListener(new ExecuteListener());
      execute.setDescription("Выполнить процесс немедленно");
      textArea.setSizeFull();
      textArea.setStyleName("small");
      diagramLayout.setCaption("Схема приостановленного процеса:");
      diagramLayout.setSizeFull();

      final VerticalLayout wrapper = new VerticalLayout();
      wrapper.setSizeUndefined();
      wrapper.setSpacing(true);
      wrapper.setWidth(110, UNITS_PIXELS);
      wrapper.addComponent(restart);
      wrapper.addComponent(execute);

      final VerticalLayout buttonsLayout = new VerticalLayout();
      buttonsLayout.addComponent(wrapper);
      buttonsLayout.setSizeFull();
      buttonsLayout.setWidth(110, UNITS_PIXELS);

      addComponent(buttonsLayout);
      addComponent(diagramLayout);
      addComponent(textArea);

      setExpandRatio(diagramLayout, 0.42f);
      setExpandRatio(textArea, 0.42f);

      setJobInfo(null);
    }

    public void setJobInfo(final JobInfo jobInfo) {
      final boolean enabled = jobInfo != null;
      info = enabled ? jobInfo.copy() : null;
      textArea.setReadOnly(false);
      textArea.setValue(enabled ? jobInfo.exception : "");
      textArea.setReadOnly(true);
      textArea.setEnabled(enabled);
      restart.setEnabled(enabled);
      execute.setEnabled(enabled);
      diagramLayout.setEnabled(enabled);
      diagramLayout.removeAllComponents();
      if (enabled) {
        diagramLayout.addComponent(new DiagramPanel(jobInfo.definitionId, jobInfo.executionId));
      }
    }

    void refresh() {
      container.refresh();
      table.setValue(null);
    }

    final private class RestartListener implements Button.ClickListener {
      @Override
      public void buttonClick(Button.ClickEvent event) {
        final String id = info.jobId;
        setJobInfo(null);
        persistence.restartJob(id);
        refresh();
      }
    }

    final private class ExecuteListener implements Button.ClickListener {
      @Override
      public void buttonClick(Button.ClickEvent event) {
        final String id = info.jobId;
        final String branch = "Маршрут " + info.definitionId + ", ветвь " + info.executionId;
        setJobInfo(null);
        try {
          persistence.executeJob(id);
        } catch (RuntimeException e) {
          getWindow().addWindow(new StackTraceDialog(branch + " - ошибка исполнения", e));
        }
        refresh();
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
