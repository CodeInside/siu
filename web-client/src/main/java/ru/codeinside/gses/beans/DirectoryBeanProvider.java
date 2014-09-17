/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.beans;

import javax.annotation.PostConstruct;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

@Singleton
@Startup
@Lock(LockType.READ)
public class DirectoryBeanProvider {

  @Inject
  DirectoryBean directoryBean;

  transient static DirectoryBean instance;

  @PostConstruct
  void initialize() {
    synchronized (DirectoryBeanProvider.class) {
      if (instance == null) {
        instance = directoryBean;
      }
    }
  }

  public static DirectoryBean get() {
    if (instance == null) {
      throw new IllegalStateException("Сервис не зарегистрирован!");
    }
    return instance;
  }

  public static DirectoryBean getOptional() {
    return instance;
  }

}
