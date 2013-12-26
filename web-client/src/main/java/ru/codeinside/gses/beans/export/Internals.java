/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.beans.export;

import ru.codeinside.gses.beans.DirectoryBean;
import ru.codeinside.gses.beans.DirectoryBeanProvider;

import java.util.Map;

final public class Internals implements ru.codeinside.gws.api.Internals {

  @Override
  public Map<String, String> getDictionary(String dictionaryId) {
    final DirectoryBean directoryBean = poolDirectoryBean(5000, 250);
    return directoryBean != null ? directoryBean.getValues(dictionaryId) : null;
  }

  private DirectoryBean poolDirectoryBean(final int totalMillis, final int stepMillis) {
    final long timeOut = System.currentTimeMillis() + totalMillis;
    DirectoryBean directoryBean = DirectoryBeanProvider.getOptional();
    if (directoryBean == null) {
      do {
        try {
          Thread.sleep(stepMillis);
        } catch (InterruptedException e) {
          break;
        }
        directoryBean = DirectoryBeanProvider.getOptional();
      } while (directoryBean == null && timeOut > System.currentTimeMillis());
    }
    return directoryBean;
  }
}
