/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui;

import org.activiti.engine.impl.jobexecutor.JobExecutor;

/**
 * Бизнес интерфейс для исполнителя задач.
 */
public interface ActivitiJobProvider {

  /**
   * Отложенный запуск нитки получения задач. Лучше вызвать после полного завершения инициализации.
   */
  void startNow();

  /**
   * Метод только для контекста Activiti.
   */
  JobExecutor createJobExecutor();
}
