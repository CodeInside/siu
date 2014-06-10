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
import java.io.InputStream;

public final class FileDownloadResource extends FileResource {
  private final File file;

  public FileDownloadResource(File file, Application application) {
    super(file, application);
    this.file = file;
  }

  public DownloadStream getStream() {
    try {
      File sourceFile = getSourceFile();
      DownloadStream ds = new DownloadStream(new FileInputStream(sourceFile) {
        @Override
        public void close() throws IOException {
          super.close();
          file.delete();
        }
      }, getMIMEType(), getFilename()) {

      };
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
}
