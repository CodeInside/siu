/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.beans;

import org.activiti.engine.ActivitiException;
import ru.codeinside.gses.service.Fn;
import ru.codeinside.gws.api.ServerException;

import javax.ejb.EJBException;

/**
 * Преобразования исключений к API.
 */
final public class Exceptions {

  // к этому моменту все исплючения уже зарегистрированы в журнале ядром JEE.
  static ServerException convertToApi(EJBException e) {
    Throwable reason = ((EJBException) e).getCausedByException();
    if (reason == null) {
      reason = e;
    } else if (reason instanceof ActivitiException) {
      if (reason.getCause() != null) {
        reason = reason.getCause();
      }
    }
    return Fn.trim(new ServerException(reason.getMessage()));
  }
}

