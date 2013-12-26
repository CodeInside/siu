/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.jta;

import javax.enterprise.inject.spi.BeanManager;

final public class CdiResolver extends org.activiti.cdi.impl.el.CdiResolver {

  private final BeanManager beanManager;

  public CdiResolver(final BeanManager beanManager) {
    this.beanManager = beanManager;
  }

  @Override
  protected BeanManager getBeanManager() {
    return beanManager;
  }
}
