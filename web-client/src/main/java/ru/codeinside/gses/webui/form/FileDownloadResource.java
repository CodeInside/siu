/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.form;

import com.vaadin.Application;
import com.vaadin.terminal.DownloadStream;
import com.vaadin.terminal.FileResource;
import com.vaadin.terminal.Terminal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public final class FileDownloadResource extends FileResource {
    private final boolean deleteOnClose;

    public FileDownloadResource(boolean deleteOnClose, File file, Application application) {
        super(file, application);
        this.deleteOnClose = deleteOnClose;
    }

    public DownloadStream getStream() {
        try {
            File sourceFile = getSourceFile();
            FileInputStream inputStream = deleteOnClose ? new TempFileInputStream(sourceFile) : new FileInputStream(sourceFile);
            DownloadStream ds = new DownloadStream(inputStream, getMIMEType(), getFilename());
            ds.setParameter("Content-Length", String.valueOf(sourceFile.length()));
            ds.setParameter("Content-Disposition", "attachment; filename=" + sourceFile.getName());
            ds.setCacheTime(getCacheTime());
            return ds;
        } catch (final FileNotFoundException e) {
            getApplication().getErrorHandler().terminalError(new Terminal.ErrorEvent() {
                public Throwable getThrowable() {
                    return e;
                }
            });
            return null;
        }
    }

    final class TempFileInputStream extends FileInputStream {
        public TempFileInputStream(File sourceFile) throws FileNotFoundException {
            super(sourceFile);
        }

        @Override
        public void close() throws IOException {
            super.close();
            getSourceFile().delete();
        }
    }
}
