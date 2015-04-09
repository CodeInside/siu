/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui;

import com.google.common.collect.Lists;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.impl.bpmn.parser.BpmnParseListener;
import org.activiti.engine.impl.jobexecutor.JobExecutor;
import org.glassfish.embeddable.Deployer;
import org.glassfish.osgicdi.OSGiService;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.gses.API;
import ru.codeinside.gses.activiti.ReceiptEnsurance;
import ru.codeinside.gses.activiti.forms.types.VariableTypes;
import ru.codeinside.gses.activiti.jta.JtaProcessEngineConfiguration;
import ru.codeinside.gses.activiti.listeners.GsesBpmnParseListener;
import ru.codeinside.gses.activiti.listeners.MailBpmnParseListener;
import ru.codeinside.gses.webui.utils.RunProfile;
import ru.codeinside.gws.api.CryptoProvider;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.DependsOn;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.TransactionManager;
import java.util.logging.Logger;

@Singleton
@DependsOn("BaseBean")
@Lock(LockType.READ)
public class Configurator {

  final static Logger logger = Logger.getLogger(Configurator.class.getName());

  private static ProcessEngine processEngine;

  private static Deployer embeddableDeployer;

  @Resource
  TransactionManager transactionManager;

  @PersistenceContext(unitName = "myPU")
  EntityManager em;

  @Inject
  BeanManager beanManager;

  @Inject
  @OSGiService(dynamic = true)
  CryptoProvider cryptoProvider;

  @Inject
  ActivitiJobProvider activitiJobProvider;

  @Inject
  Instance<ReceiptEnsurance> receiptEnsuranceHolder;

  @Resource
  Deployer deployer;

  @Named("doDbUpdate")
  @Inject
  Instance<Boolean> doDbUpdate;

  public static ProcessEngine get() {
    return processEngine;
  }

  public static Deployer getDeployer() {
    return embeddableDeployer;
  }

  @Produces
  @ApplicationScoped
  public ProcessEngine getProcessEngine() {
    synchronized (Configurator.class) {
      if (processEngine == null) {
        processEngine = createProcessEngine();
      }
      return processEngine;
    }
  }


  /**
   * Заполняет свойства конфига необходимые для подключения к SMTP и отправке сообщения из Activiti.
   * Свойства выставляются только в том случае если значение, прочтеное из настроек не null, в противном случае
   * будет сохранено значение по умолчанию.
   * Значения по умолчанию можно посмотреть здесь http://activiti.org/userguide/index.html#bpmnEmailTaskServerConfiguration
   *
   * @param cfg activiti engine config
   */
  private void fillSmtpConfig(ProcessEngineConfiguration cfg) {
    if (AdminServiceProvider.get().getSystemProperty(API.MT_PORT) != null) {
      cfg.setMailServerPort(Integer.valueOf(AdminServiceProvider.get().getSystemProperty(API.MT_PORT)));
    }
    if (AdminServiceProvider.get().getSystemProperty(API.MT_HOST) != null) {
      cfg.setMailServerHost(AdminServiceProvider.get().getSystemProperty(API.MT_HOST));
    }
    if (AdminServiceProvider.get().getSystemProperty(API.MT_SENDER_LOGIN) != null) {
      cfg.setMailServerUsername(AdminServiceProvider.get().getSystemProperty(API.MT_SENDER_LOGIN));
    }
    if (AdminServiceProvider.get().getSystemProperty(API.MT_PASSWORD) != null) {
      cfg.setMailServerPassword(AdminServiceProvider.get().getSystemProperty(API.MT_PASSWORD));
    }
    if (AdminServiceProvider.get().getSystemProperty(API.MT_TLS) != null) {
      cfg.setMailServerUseTLS(Boolean.valueOf(AdminServiceProvider.get().getSystemProperty(API.MT_TLS)));
    }
    if (AdminServiceProvider.get().getSystemProperty(API.MT_DEFAULT_FROM) != null) {
      cfg.setMailServerDefaultFrom(AdminServiceProvider.get().getSystemProperty(API.MT_DEFAULT_FROM));
    }
  }

  @PostConstruct
  public void postConstruct() {
    if (deployer != null) {
      embeddableDeployer = deployer;
    }
  }

  @PreDestroy
  public void close() {
    closeEngine();
  }


  private ProcessEngine createProcessEngine() {
    logger.info("Запуск исполнителя процессов");

    JtaProcessEngineConfiguration cfg = new JtaProcessEngineConfiguration(
      transactionManager, cryptoProvider, beanManager, em
    );

    // асинхронное исполнение
    JobExecutor jobExecutor = activitiJobProvider.createJobExecutor();
    if (jobExecutor != null) {
      cfg.setJobExecutor(jobExecutor);
      cfg.setJobExecutorActivate(true);
    }

    Boolean update = doDbUpdate.isUnsatisfied() ? !RunProfile.isProduction() : doDbUpdate.get();
    cfg.setDatabaseSchemaUpdate(Boolean.TRUE.equals(update) ? "true" : "false");

    cfg.setCustomPostBPMNParseListeners(
      Lists.<BpmnParseListener>newArrayList(
        new GsesBpmnParseListener(receiptEnsuranceHolder.get()),
        new MailBpmnParseListener()
      )
    );

    cfg.setProcessEngineName("СИУ");

    cfg.setFormTypes(new VariableTypes());

    // читаем настройки подключения к smtp серверу
    fillSmtpConfig(cfg);

    return cfg.buildProcessEngine();
  }

  private void closeEngine() {
    if (processEngine != null) {
      logger.info("Выключение исполнителя процессов");
      processEngine.close();
      processEngine = null;
    }
  }

}
