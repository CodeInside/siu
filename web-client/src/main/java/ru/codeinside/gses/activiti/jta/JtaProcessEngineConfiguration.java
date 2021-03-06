/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.jta;


import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.cfg.jta.JtaTransactionContextFactory;
import org.activiti.engine.impl.db.DbSqlSession;
import org.activiti.engine.impl.db.DbSqlSessionFactory;
import org.activiti.engine.impl.interceptor.CommandContextInterceptor;
import org.activiti.engine.impl.interceptor.CommandInterceptor;
import org.activiti.engine.impl.persistence.deploy.Deployer;
import org.activiti.engine.impl.util.ReflectUtil;
import org.activiti.engine.impl.variable.EntityManagerSession;
import org.activiti.engine.impl.variable.JPAEntityVariableType;
import org.activiti.engine.impl.variable.SerializableType;
import org.activiti.engine.impl.variable.VariableType;
import ru.codeinside.gses.activiti.DeployerCustomizer;
import ru.codeinside.gses.activiti.history.HistoricDbSqlSessionFactory;
import ru.codeinside.gses.service.CryptoProviderAware;
import ru.codeinside.gses.webui.TaskServiceImpl2;
import ru.codeinside.gws.api.CryptoProvider;

import javax.enterprise.inject.spi.BeanManager;
import javax.persistence.EntityManager;
import javax.transaction.TransactionManager;
import java.io.InputStream;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;

final public class JtaProcessEngineConfiguration extends ProcessEngineConfigurationImpl implements CryptoProviderAware {

  private final CryptoProvider cryptoProvider;
  private final TransactionManager transactionManager;
  private final BeanManager beanManager;
  private final EntityManager em;


  public JtaProcessEngineConfiguration(
    TransactionManager transactionManager, CryptoProvider cryptoProvider, BeanManager beanManager, EntityManager em) {
    this.transactionManager = transactionManager;
    this.cryptoProvider = cryptoProvider;
    this.beanManager = beanManager;
    this.em = em;

    // Включить историю изменения переменных:
    history = HISTORY_FULL;

    // расширение сервиса:
    taskService = new TaskServiceImpl2();
  }

  @Override
  protected void initExpressionManager() {
    expressionManager = new CdiExpressionManager(beanManager);
  }

  @Override
  protected void initSessionFactories() {
    super.initSessionFactories();
    DbSqlSessionFactory factory = (DbSqlSessionFactory) sessionFactories.get(DbSqlSession.class);
    sessionFactories.put(factory.getSessionType(), new HistoricDbSqlSessionFactory(factory));
  }

  @Override
  protected void initIdGenerator() {
    idGenerator = new IdGenerator();
  }

  @Override
  protected InputStream getMyBatisXmlConfigurationSteam() {
    return ReflectUtil.getResourceAsStream("activiti-db-mapping/custom.xml");
  }

  @Override
  protected Collection<? extends Deployer> getDefaultDeployers() {
    return DeployerCustomizer.customize(super.getDefaultDeployers(), false);
  }


  @Override
  protected Collection<? extends CommandInterceptor> getDefaultCommandInterceptorsTxRequired() {
    final JtaTransactionInterceptor i1 = new JtaTransactionInterceptor(transactionManager, false);
    final CommandContextInterceptor i2 = new CommandContextInterceptor(commandContextFactory, this);
    return Arrays.asList(i1, i2);
  }

  @Override
  protected Collection<? extends CommandInterceptor> getDefaultCommandInterceptorsTxRequiresNew() {
    final JtaTransactionInterceptor i1 = new JtaTransactionInterceptor(transactionManager, true);
    final CommandContextInterceptor i2 = new CommandContextInterceptor(commandContextFactory, this);
    return Arrays.asList(i1, i2);
  }

  @Override
  public CryptoProvider getCryptoProviderProxy() {
    return cryptoProvider;
  }

  /**
   * Создаём согласованный c сервисами EM.
   */
  @Override
  protected void initJpa() {
    sessionFactories.put(EntityManagerSession.class, new JtaEntityManagerSessionFactory(em));
    VariableType jpaType = variableTypes.getVariableType(JPAEntityVariableType.TYPE_NAME);
    if (jpaType == null) {
      int serializableIndex = variableTypes.getTypeIndex(SerializableType.TYPE_NAME);
      if (serializableIndex > -1) {
        variableTypes.addType(new JPAEntityVariableType(), serializableIndex);
      } else {
        variableTypes.addType(new JPAEntityVariableType());
      }
    }
  }

  /**
   * Управление транзакциями в JTA.
   */
  @Override
  protected void initTransactionFactory() {
    if (transactionFactory == null) {
      transactionFactory = new JtaTransactionFactory();
    }
  }

  /**
   * Контекст управления транзакциями в JTA.
   */
  @Override
  protected void initTransactionContextFactory() {
    if (transactionContextFactory == null) {
      transactionContextFactory = new JtaTransactionContextFactory(transactionManager);
    }
  }

  /**
   * Связанный с EntityManager источник данных внтури JTA.
   */
  @Override
  protected void initDataSource() {
    if (dataSource == null) {
      dataSource = new JtaEntityManagerDataSource(em);
      try {
        DatabaseMetaData databaseMetaData = dataSource.getConnection().getMetaData();
        String databaseProductName = databaseMetaData.getDatabaseProductName();
        databaseType = databaseTypeMappings.getProperty(databaseProductName);
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
