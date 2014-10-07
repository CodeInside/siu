/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.manager;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.activiti.engine.impl.persistence.deploy.Deployer;
import ru.codeinside.gses.activiti.DeployerCustomizer;
import ru.codeinside.gses.activiti.forms.types.VariableTypes;

import java.util.Collection;

final public class SandboxEngine {

  public static ProcessEngine create() {
    return new StandaloneProcessEngineConfiguration() {
      {
        processEngineName = "Sanbox" + System.currentTimeMillis();
        databaseSchemaUpdate = DB_SCHEMA_UPDATE_CREATE_DROP;
        jdbcUrl = "jdbc:h2:mem:" + processEngineName;
        formTypes = new VariableTypes();
      }

      @Override
      protected Collection<? extends Deployer> getDefaultDeployers() {
        return DeployerCustomizer.customize(super.getDefaultDeployers(), true);
      }

      @Override
      protected void initJobExecutor() {
      }

    }.buildProcessEngine();
  }
}

