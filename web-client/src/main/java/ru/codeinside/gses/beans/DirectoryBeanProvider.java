/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.beans;

import javax.annotation.PostConstruct;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@javax.ejb.Singleton
@Startup
@ApplicationScoped
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
