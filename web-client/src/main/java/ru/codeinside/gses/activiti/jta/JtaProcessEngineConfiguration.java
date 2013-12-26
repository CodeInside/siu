/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
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
import ru.codeinside.gses.activiti.DeployerCustomizer;
import ru.codeinside.gses.activiti.history.HistoricDbSqlSessionFactory;
import ru.codeinside.gws.api.CryptoProvider;

import javax.enterprise.inject.spi.BeanManager;
import javax.transaction.TransactionManager;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;

final public class JtaProcessEngineConfiguration extends ProcessEngineConfigurationImpl {

  private final CryptoProvider cryptoProvider;
  private final TransactionManager transactionManager;
  private final BeanManager beanManager;


  public JtaProcessEngineConfiguration(
    final TransactionManager transactionManager, final CryptoProvider cryptoProvider, final BeanManager beanManager) {
    this.transactionManager = transactionManager;
    this.cryptoProvider = cryptoProvider;
    this.beanManager = beanManager;

    transactionsExternallyManaged = true;
  }

  @Override
  protected void initExpressionManager() {
    expressionManager = new CdiExpressionManager(beanManager);
  }

  @Override
  protected void initSessionFactories() {
    super.initSessionFactories();
    final DbSqlSessionFactory factory = (DbSqlSessionFactory) sessionFactories.get(DbSqlSession.class);
    sessionFactories.put(factory.getSessionType(), new HistoricDbSqlSessionFactory(cryptoProvider, factory));
  }

  @Override
  protected void initIdGenerator() {
    if (idGenerator == null) {
      idGenerator = new IdGenerator();
    }
  }

  @Override
  protected InputStream getMyBatisXmlConfigurationSteam() {
    return ReflectUtil.getResourceAsStream("activiti-db-mapping/custom.xml");
  }

  @Override
  protected Collection<? extends Deployer> getDefaultDeployers() {
    return DeployerCustomizer.customize(super.getDefaultDeployers());
  }

  @Override
  protected void initTransactionContextFactory() {
    if (transactionContextFactory == null) {
      transactionContextFactory = new JtaTransactionContextFactory(transactionManager);
    }
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
}
