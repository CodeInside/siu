/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti;

import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.activiti.engine.impl.db.DbSqlSession;
import org.activiti.engine.impl.db.DbSqlSessionFactory;
import org.activiti.engine.impl.persistence.deploy.Deployer;
import ru.codeinside.gses.activiti.jta.CustomDbSqlSessionFactory;
import ru.codeinside.gses.activiti.jta.IdGenerator;

import java.util.Collection;

public class CustomStandaloneProcessEngineConfiguration extends StandaloneProcessEngineConfiguration {
  protected Collection<? extends Deployer> getDefaultDeployers() {
    return DeployerCustomizer.customize(super.getDefaultDeployers());
  }

  @Override
  protected void initIdGenerator() {
    if (idGenerator == null) {
      idGenerator = new IdGenerator();
    }
  }

  @Override
  protected void initSessionFactories() {
    super.initSessionFactories();
    final DbSqlSessionFactory factory = (DbSqlSessionFactory) sessionFactories.get(DbSqlSession.class);
    sessionFactories.put(factory.getSessionType(), new CustomDbSqlSessionFactory(factory));
  }
}
