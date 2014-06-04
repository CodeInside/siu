/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.forms.duration;

/**
 * Граничные периоды выполнения процессов и задач
 */
public class DurationPreference {
  /**
   * длительность периода в днях, по истечении которого необходимо выслать оповещение о приближающейся просрочке
   * выполнения задачи
   */
  public int notificationPeriod;
  /**
   * длительность периода в днях, которое отведено на выполнение задачи
   */
  public int executionPeriod;
  /**
   * длительность периода в днях, в течении которого задача может не выполняться.
   */
  public int inactivePeriod;
}
