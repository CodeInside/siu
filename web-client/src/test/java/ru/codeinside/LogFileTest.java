/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside;

import org.apache.commons.io.FileUtils;
import org.jboss.weld.resources.SingleThreadScheduledExecutorServiceFactory;
import org.junit.Assert;
import org.junit.Test;
import ru.codeinside.adm.LogConverter;
import ru.codeinside.adm.database.SmevLog;
import ru.codeinside.gses.webui.osgi.LogCustomizer;

import javax.persistence.EntityManager;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class LogFileTest {

  @Test
  public void test() throws URISyntaxException, IOException {
    final URL resource = getClass().getClassLoader().getResource("log");
    assertNotNull(resource);

    final File logs = new File(new File("target"), "logs");
    FileUtils.deleteDirectory(logs);
    FileUtils.copyDirectory(new File("src/test/resources/log"), logs, true);

    File logPackage1 = new File(logs, "c/7/01807d4f1fbc40fca8e12c3becd82dc7");
    assertTrue(logPackage1.setLastModified(100000000L));

    File logPackage2 = new File(logs, "1/3/fb255dab0152428b92443dcf02051813");
    assertTrue(logPackage2.setLastModified(100000011L));

    final List<SmevLog> items = new ArrayList<SmevLog>();

    LogConverter.Storage storage = new LogConverter.Storage() {
      @Override
      public String getLazyDirPath() {
        return logs.getPath();
      }

      @Override
      public SmevLog findLogEntry(String marker) {
        return null;
      }

      @Override
      public void store(SmevLog smevLog) {
        items.add(smevLog);
      }
    };

    LogConverter converter = new LogConverter();

    converter.setStorage(storage);

    assertTrue(converter.logToBd());
    assertEquals(1, items.size());

    SmevLog _1 = items.get(0);
    assertEquals("fb255dab0152428b92443dcf02051813", _1.getMarker());
    assertNotNull(_1.getSendHttp());
    assertNotNull(_1.getReceiveHttp());
    assertEquals("FSSR01001", _1.getSendPacket().getRecipient());
    assertEquals("9330c70a-8b74-4496-9229-7169a9700313", _1.getReceivePacket().getRequestIdRef());

    assertTrue(converter.logToBd());
    assertEquals(2, items.size());


    SmevLog _2 = items.get(1);
    assertEquals("01807d4f1fbc40fca8e12c3becd82dc7", _2.getMarker());
    assertNotNull(_2.getLogDate());
    assertNotNull(_2.getSendHttp());
    assertNotNull(_2.getReceiveHttp());
    assertEquals("9330c70a-8b74-4496-9229-7169a9700313", _2.getSendPacket().getOriginRequestIdRef());
    assertEquals("d1a97f4d-9f29-404a-bd86-3f443ec05bb0", _2.getReceivePacket().getRequestIdRef());

    assertFalse(converter.logToBd());
    assertEquals(2, items.size());
  }
}
