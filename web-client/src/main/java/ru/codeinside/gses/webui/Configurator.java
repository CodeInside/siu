/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.impl.bpmn.parser.BpmnParseListener;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.form.AbstractFormType;
import org.activiti.engine.impl.form.FormTypes;
import org.activiti.engine.impl.jobexecutor.JobExecutor;
import org.glassfish.embeddable.Deployer;
import org.glassfish.osgicdi.OSGiService;
import ru.codeinside.gses.activiti.DelegateFormType;
import ru.codeinside.gses.activiti.ReceiptEnsurance;
import ru.codeinside.gses.activiti.forms.CustomFormTypes;
import ru.codeinside.gses.activiti.ftarchive.AttachmentFFT;
import ru.codeinside.gses.activiti.ftarchive.BooleanFFT;
import ru.codeinside.gses.activiti.ftarchive.DateFFT;
import ru.codeinside.gses.activiti.ftarchive.DirectoryFFT;
import ru.codeinside.gses.activiti.ftarchive.EnclosureItemFFT;
import ru.codeinside.gses.activiti.ftarchive.EnumFFT;
import ru.codeinside.gses.activiti.ftarchive.FormSignatureFFT;
import ru.codeinside.gses.activiti.ftarchive.JsonFFT;
import ru.codeinside.gses.activiti.ftarchive.LongFFT;
import ru.codeinside.gses.activiti.ftarchive.MaskedFFT;
import ru.codeinside.gses.activiti.ftarchive.MultilineFFT;
import ru.codeinside.gses.activiti.ftarchive.SmevRequestEnclosuresFFT;
import ru.codeinside.gses.activiti.ftarchive.SmevRequestFFT;
import ru.codeinside.gses.activiti.ftarchive.SmevResponseEnclosuresFFT;
import ru.codeinside.gses.activiti.ftarchive.StringFFT;
import ru.codeinside.gses.activiti.jta.JtaProcessEngineConfiguration;
import ru.codeinside.gses.activiti.listeners.GsesBpmnParseListener;
import ru.codeinside.gses.activiti.listeners.MailBpmnParseListener;
import ru.codeinside.gses.activiti.mail.SmtpConfig;
import ru.codeinside.gses.activiti.mail.SmtpConfigReader;
import ru.codeinside.gses.vaadin.FieldFormType;
import ru.codeinside.gses.vaadin.FieldFormTypeListener;
import ru.codeinside.gses.vaadin.FieldFormTypeService;
import ru.codeinside.gses.vaadin.beans.GsesBean;
import ru.codeinside.gses.vaadin.beans.GsesBeanListener;
import ru.codeinside.gses.vaadin.beans.GsesBeanService;
import ru.codeinside.gses.webui.utils.RunProfile;
import ru.codeinside.gws.api.CryptoProvider;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.DependsOn;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.sql.DataSource;
import javax.transaction.TransactionManager;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import static org.activiti.engine.ProcessEngineConfiguration.HISTORY_FULL;

@Singleton
@Lock(LockType.READ)
@DependsOn("BaseBean")
public class Configurator {

  final static Logger logger = Logger.getLogger(Configurator.class.getName());
  private static ProcessEngine processEngine;

  @Resource(mappedName = "jdbc/adminka")
  private DataSource dataSource;

  @Resource
  TransactionManager transactionManager;

  @PersistenceUnit(unitName = "myPU")
  EntityManagerFactory emf;

  @PersistenceContext(unitName = "logPU")
  EntityManager emLog;


  @Inject
  BeanManager beanManager;

  // пока не используем эту зависимость
  private FieldFormTypeService ftService = null;

  // пока не используем эту зависимость
  private GsesBeanService gsesBeanService = null;

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


  private static Deployer embeddableDeployer;

  private static List<GsesBean> gsesBeans = Lists.newArrayList();

  final private ImmutableList<FieldFormType> internalFormTypes = ImmutableList.<FieldFormType>of(
    new StringFFT(),
    new MultilineFFT(),
    new LongFFT(),
    new DateFFT(),
    new MaskedFFT(),
    new BooleanFFT(),
    new EnumFFT(),
    new DirectoryFFT(),
    new AttachmentFFT(),
    new FormSignatureFFT(),
    new SmevRequestFFT(),
    new EnclosureItemFFT(),
    new SmevRequestEnclosuresFFT(),
    new SmevResponseEnclosuresFFT(),
    new JsonFFT()
  );


  public static List<GsesBean> getGsesBeans() {
    return gsesBeans;
  }

  private static HashMap<String, FieldFormType> formTypesRegistry = Maps.newHashMap();

  public static Collection<FieldFormType> getFields() {
    return formTypesRegistry.values();
  }

  /**
   * @deprecated Желательно избегать подобных статических функций.
   */
  @Deprecated
  public static FieldFormType getFieldFormType(String name) {
    return formTypesRegistry.get(name);
  }

  private void initTypes(ProcessEngineConfigurationImpl engineConfiguration) {
    final List<FieldFormType> fieldFormTypes;
    if (ftService != null) {
      fieldFormTypes = ftService.getFormTypes();
    } else {
      fieldFormTypes = internalFormTypes;
    }
    final FormTypes formTypes = new CustomFormTypes();
    for (final FieldFormType type : fieldFormTypes) {
      final AbstractFormType fromType = new DelegateFormType(type);
      formTypes.addFormType(fromType);
      formTypesRegistry.put(fromType.getName(), type);
    }
    engineConfiguration.setFormTypes(formTypes);
  }


  private FieldFormTypeListener L = new FieldFormTypeListener() {

    @Override
    public void fieldTypesRegistered(FieldFormTypeService source, Iterable<FieldFormType> fieldTypes) {
      closeEngine();
    }

    @Override
    public void fieldTypesUnregistered(FieldFormTypeService source, Iterable<FieldFormType> fieldTypes) {
      closeEngine();
    }
  };

  @Produces
  public ProcessEngine getProcessEngine() {
    synchronized (Configurator.class) {
      if (processEngine == null) {
        final JtaProcessEngineConfiguration cfg =
          new JtaProcessEngineConfiguration(transactionManager, cryptoProvider, beanManager);

        // асинхронное исполнение
        final JobExecutor jobExecutor = activitiJobProvider.createJobExecutor();
        if (jobExecutor != null) {
          cfg.setJobExecutor(jobExecutor);
          cfg.setJobExecutorActivate(true);
        }

        // Включить историю изменения переменных
        cfg.setHistory(HISTORY_FULL);
        Boolean update = doDbUpdate.isUnsatisfied() ? (!RunProfile.isProduction()) : doDbUpdate.get();
        cfg.setDatabaseSchemaUpdate(Boolean.TRUE == update ? "true" : "false");
        cfg.setDataSource(dataSource);
        cfg.setJpaEntityManagerFactory(emf);
        cfg.setJpaHandleTransaction(false);
        cfg.setJpaCloseEntityManager(true);
        //Подменить реализацию TaskQuery c TaskQueryImpl на TaskQueryImpl2
        cfg.setTaskService(new TaskServiceImpl2());
        cfg.setCustomPostBPMNParseListeners(
          Lists.<BpmnParseListener>newArrayList(
            new GsesBpmnParseListener(receiptEnsuranceHolder.get()),
            new MailBpmnParseListener()
          )
        );
        cfg.setProcessEngineName("СИУ");
        initTypes(cfg);
        // форсируем тут создание EM
        emf.createEntityManager().close();
        // читаем настройки подключения к smtp серверу
        fillSmtpConfig(cfg);
        processEngine = cfg.buildProcessEngine();
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
    SmtpConfig smtpConfig = SmtpConfigReader.readSmtpConnectionParams();
    if (smtpConfig.getPort() != null) {
      cfg.setMailServerPort(smtpConfig.getPort());
    }
    if (smtpConfig.getHost() != null) {
      cfg.setMailServerHost(smtpConfig.getHost());
    }
    if (smtpConfig.getUserName() != null) {
      cfg.setMailServerUsername(smtpConfig.getUserName());
    }
    if (smtpConfig.getPassword() != null) {
      cfg.setMailServerPassword(smtpConfig.getPassword());
    }
    if (smtpConfig.getUseTLS() != null) {
      cfg.setMailServerUseTLS(smtpConfig.getUseTLS());
    }
    if (smtpConfig.getDefaultFrom() != null) {
      cfg.setMailServerDefaultFrom(smtpConfig.getDefaultFrom());
    }
  }

  private GsesBeanListener GBL = new GsesBeanListener() {

    @Override
    public void gsesBeanUnregistered(GsesBeanService arg0, GsesBean arg1) {
      gsesBeans = gsesBeanService.getGsesBeans();
    }

    @Override
    public void gsesBeanRegistered(GsesBeanService arg0, GsesBean arg1) {
      gsesBeans = gsesBeanService.getGsesBeans();
    }
  };

  @PostConstruct
  public void postConstruct() {
    logger.info("Запуск исполнителя процессов");
    if (ftService != null) {
      ftService.registerFieldTypes(internalFormTypes);
    }
    getProcessEngine();
    if (ftService != null) {
      ftService.addListener(L);
    }
    if (gsesBeanService != null) {
      gsesBeanService.addListener(GBL);
      gsesBeans = gsesBeanService.getGsesBeans();
    }
    if (deployer != null) {
      embeddableDeployer = deployer;
    }
  }


  @PreDestroy
  public void close() {
    logger.info("Выключение исполнителя процессов");
    closeEngine();
    if (ftService != null) {
      ftService.unregisterFieldTypes(internalFormTypes);
      ftService.removeListener(L);
    }
    gsesBeanService.removeListener(GBL);
  }

  private void closeEngine() {
    if (processEngine != null) {
      processEngine.close();
      processEngine = null;
    }
  }

  public static ProcessEngine get() {
    return processEngine;
  }

  public static Deployer getDeployer() {
    return embeddableDeployer;
  }

}
