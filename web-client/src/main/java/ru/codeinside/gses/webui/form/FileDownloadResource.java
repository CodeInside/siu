package ru.codeinside.gses.webui.form;

import com.vaadin.Application;
import com.vaadin.terminal.DownloadStream;
import com.vaadin.terminal.FileResource;
import com.vaadin.terminal.Terminal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

final class FileDownloadResource extends FileResource {
  public FileDownloadResource(File file, Application application) {
    super(file, application);
  }

  public DownloadStream getStream() {
    try {
      File sourceFile = getSourceFile();
      DownloadStream ds = new DownloadStream(new FileInputStream(sourceFile), getMIMEType(), getFilename());
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
