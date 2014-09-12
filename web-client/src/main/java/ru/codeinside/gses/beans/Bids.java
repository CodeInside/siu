/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.beans;

import org.activiti.engine.delegate.DelegateExecution;
import ru.codeinside.gses.activiti.Activiti;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.List;

/**
 * Вызывается из BPMN скрипта и возвращает номер заявки.
 */

@Named("bids")
@Singleton
public class Bids {

  public String getBid(DelegateExecution execution){
    if (execution == null){
      return null;
    }
    String pid = execution.getProcessInstanceId();
    if (pid == null) {
      return null;
    }
    List<Long> bidList = Activiti.getEm().createQuery(
      "SELECT b.id FROM Bid b WHERE b.processInstanceId = :pid", Long.class)
      .setParameter("pid", pid).getResultList();
    if (!bidList.isEmpty()) {
      return bidList.get(0).toString();
    }

    return null;
  }
}
