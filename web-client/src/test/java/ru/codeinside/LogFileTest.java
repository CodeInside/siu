/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside;

import org.jboss.weld.resources.SingleThreadScheduledExecutorServiceFactory;
import org.junit.Assert;
import org.junit.Test;
import ru.codeinside.adm.LogConverter;
import ru.codeinside.adm.database.SmevLog;
import ru.codeinside.gses.webui.osgi.LogCustomizer;

import javax.persistence.EntityManager;
import java.net.URL;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class LogFileTest {

  @Test
  public void test() {
    final URL resource = getClass().getClassLoader().getResource("log");
    final SmevLog result = new SmevLog();

    LogConverter converter = new LogConverter() {
      @Override
      protected String getLazyDirPath() {
        return resource.getPath();
      }

      @Override
      protected SmevLog findLogEntry(String marker) {
        result.setDate(new Date());
        result.setMarker(marker);
        return result;
      }
    };

    Assert.assertNull(result.getSendHttp());
    Assert.assertNull(result.getReceiveHttp());
    Assert.assertNull(result.getSendPacket());
    Assert.assertNull(result.getReceivePacket());

    Assert.assertTrue(converter.logToBd());

    Assert.assertNotNull(result.getSendHttp());
    Assert.assertNotNull(result.getReceiveHttp());
    Assert.assertNotNull(result.getSendPacket());
    Assert.assertNotNull(result.getReceivePacket());
  }
}
