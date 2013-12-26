/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside;

import org.junit.Assert;
import org.junit.Test;
import ru.codeinside.adm.LogConverter;
import ru.codeinside.adm.database.OepLog;

import javax.persistence.EntityManager;
import java.net.URL;
import java.util.Date;

public class LogFileTest {

    @Test
    public void test() {
        URL resource = this.getClass().getClassLoader().getResource("log");

        final OepLog result = new OepLog();
        result.setDate(new Date());
        result.setMarker("marker");

        LogConverter converter = new LogConverter() {
            public OepLog getOepLog(EntityManager em, String marker) {
                return result;
            }
        };
        converter.setDirPath(resource.getPath());

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
