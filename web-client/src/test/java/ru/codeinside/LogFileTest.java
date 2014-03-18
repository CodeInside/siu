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

import javax.persistence.EntityManager;
import java.net.URL;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class LogFileTest {

    @Test
    public void test() {
        URL resource = this.getClass().getClassLoader().getResource("log");

        final SmevLog result = new SmevLog();
        result.setDate(new Date());
        result.setMarker("marker");


        LogConverter converter = new LogConverter() {
          @Override
          public SmevLog getOepLog(EntityManager em, String marker) {
            return result;
          }
        };
        converter.setDirPath(resource.getPath());
      System.out.println(resource.getPath());

        Assert.assertNull(result.getSendHttp());
        Assert.assertNull(result.getReceiveHttp());
        Assert.assertNull(result.getSendPacket());
        Assert.assertNull(result.getReceivePacket());

        converter.logToBd();

        Assert.assertNotNull(result.getSendHttp());
        Assert.assertNotNull(result.getReceiveHttp());
        Assert.assertNotNull(result.getSendPacket());
        Assert.assertNotNull(result.getReceivePacket());
    }
}
