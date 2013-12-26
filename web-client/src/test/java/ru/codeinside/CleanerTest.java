/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

/**
 * Arquillian не удаляет временные каталоги!
 * Удалим за него.
 */
public class CleanerTest extends Assert {

    @Test
    public void cleanTmp() {
        final File baseTmpDir = new File(System.getProperty("java.io.tmpdir"));
        final File[] tmpDirs = baseTmpDir.listFiles(new TmpDirFilter());
        for (File file : tmpDirs) {
            try {
                FileUtils.deleteDirectory(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        assertEquals(0, baseTmpDir.listFiles(new TmpDirFilter()).length);
    }

    static class TmpDirFilter implements FilenameFilter {
        @Override
        public boolean accept(File dir, String name) {
            boolean found = name.startsWith("arquillian") && name.endsWith("test.war");
            if (!found) {
                found = name.startsWith("gfembed") && name.endsWith("tmp");
            }
            return found;
        }
    }
}
