/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package liquibase.util;

import org.junit.Assert;
import org.junit.Test;

public class MD5UtilTest extends Assert {
    @Test
    public void testComputeMD5() throws Exception {
        assertEquals("25f9e794323b453885f5181f1b624d0b", MD5Util.computeMD5("123456789"));
    }
}
