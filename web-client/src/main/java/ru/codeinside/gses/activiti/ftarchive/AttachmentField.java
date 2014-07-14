/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.ftarchive;

import com.vaadin.terminal.DownloadStream;
import com.vaadin.terminal.Sizeable;
import com.vaadin.terminal.StreamResource;
import com.vaadin.terminal.gwt.server.WebBrowser;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.ProgressIndicator;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Window;
import commons.Streams;
import ru.codeinside.gses.activiti.FileValue;
import ru.codeinside.gses.service.Some;
import ru.codeinside.gses.vaadin.customfield.CustomField;
import ru.codeinside.gses.webui.Flash;
import ru.codeinside.gses.webui.utils.Components;

import javax.mail.internet.MimeUtility;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URI;

final public class AttachmentField extends CustomField implements Serializable, Upload.Receiver {

  private static final long serialVersionUID = 1L;
  final Upload upload;
  final HorizontalLayout layout = new HorizontalLayout();
  final ProgressIndicator indicator = new ProgressIndicator();
  final Label sizeInfo = new Label();

  Component oldValue;
  File tmpFile;
  Button removeAttachmentButton;

  public AttachmentField(final String taskId, final String fieldId, final String name, FileValue attachment) {

    upload = new Upload(null, this);
    upload.setButtonCaption("Выбрать файл");
    upload.setImmediate(true);
    upload.addListener(new Upload.StartedListener() {
      private static final long serialVersionUID = 1L;

      @Override
      public void uploadStarted(Upload.StartedEvent event) {
        indicator.setValue(0f);
        indicator.setVisible(true);
        sizeInfo.setValue("0/" + event.getContentLength());
      }
    });
    upload.addListener(new Upload.ProgressListener() {
      private static final long serialVersionUID = 1L;

      public void updateProgress(long readBytes, long contentLength) {
        indicator.setValue((float) readBytes / (float) contentLength);
        sizeInfo.setValue("" + readBytes + "/" + contentLength);
      }

    });
    upload.addListener(new Upload.SucceededListener() {
      private static final long serialVersionUID = 1L;

      @Override
      public void uploadSucceeded(Upload.SucceededEvent event) {
        removeOldValue();
        String fileName = event.getFilename();
        String fileType = event.getMIMEType();
        FileValue value;
        if (taskId != null) {
          try {
            value = Flash.flash()
              .getExecutorService()
              .saveBytesBuffer(taskId, fieldId, fileName, fileType, tmpFile);
          } finally {
            tmpFile.delete();
          }
        } else {
          value = new TmpFileValue(fileName, fileType, tmpFile);
        }
        setValue(value);
        setDownloadLink(createDownloadLink(value));
        resetUpload();
      }
    });

    upload.addListener(new Upload.FailedListener() {
      private static final long serialVersionUID = 1L;

      @Override
      public void uploadFailed(Upload.FailedEvent event) {
        if (tmpFile != null) {
          tmpFile.delete();
          tmpFile = null;
        }
        getWindow().showNotification("Ошибка при загрузке " + name, Window.Notification.TYPE_HUMANIZED_MESSAGE);
      }
    });

    upload.addListener(new Upload.FinishedListener() {
      private static final long serialVersionUID = 1L;

      @Override
      public void uploadFinished(Upload.FinishedEvent event) {
        indicator.setVisible(false);
        sizeInfo.setValue(null);
      }
    });
    indicator.setVisible(false);
    indicator.setWidth(100, Sizeable.UNITS_PIXELS);

    layout.setSpacing(true);
    layout.addComponent(indicator);
    layout.addComponent(sizeInfo);


    setCaption(name);
    setValidationVisible(true);
    setRequiredError("Выберите файл!");// "" + name + "\""

    initRemoveAttachmentButton();

    if (taskId != null) {
      Some<FileValue> optionalFile = Flash.flash().getExecutorService().getFileBuffer(taskId, fieldId);
      if (optionalFile.isPresent()) {
        FileValue newValue = optionalFile.get();
        setValue(newValue);
        setDownloadLink(createDownloadLink(newValue));
      } else {
        initAttachmentValue(attachment);
      }
    } else {
      initAttachmentValue(attachment);
    }

    layout.addComponent(upload);
    layout.addComponent(removeAttachmentButton);
    setCompositionRoot(layout);
  }

  private void initAttachmentValue(FileValue attachment) {
    if (attachment != null) {
      setValue(attachment, true);
      setDownloadLink(Components.createAttachShowButton(attachment, Flash.app()));
    }
  }

  void setDownloadLink(Component link) {
    oldValue = link;
    layout.addComponent(oldValue);
    removeAttachmentButton.setVisible(true);
    upload.setVisible(false);
  }

  private void initRemoveAttachmentButton() {
    removeAttachmentButton = new Button("Удалить вложение");
    removeAttachmentButton.setVisible(false);
    removeAttachmentButton.addListener(new Button.ClickListener() {
      @Override
      public void buttonClick(Button.ClickEvent event) {
        removeOldValue();
        removeAttachmentButton.setVisible(false);
        upload.setVisible(true);
        setValue(null, true);
        resetUpload();
      }
    });
  }

  void removeOldValue() {
    if (oldValue != null) {
      layout.removeComponent(oldValue);
      oldValue = null;
    }
  }

  @Override
  public Class<FileValue> getType() {
    return FileValue.class;
  }

  public OutputStream receiveUpload(String name, String type) {
    try {
      tmpFile = Streams.createTempFile("upload", ".tmp");
      return new FileOutputStream(tmpFile);
    } catch (IOException e) {
      throw new RuntimeException("On upload " + name, e);
    }
  }

  @Override
  public void discard() throws SourceException {
    super.discard();
    resetUpload();
  }

  private void resetUpload() {
    sizeInfo.setValue(null);
    tmpFile = null;
  }


  Component createDownloadLink(final FileValue fileValue) {
    final Link result = new Link();
    result.setCaption(fileValue.getFileName());
    result.setTargetName("_top");
    result.setImmediate(true);
    result.setDescription("Скачать");
    StreamResource.StreamSource streamSource = new StreamResource.StreamSource() {
      public InputStream getStream() {
        return new ByteArrayInputStream(fileValue.getContent());
      }
    };
    StreamResource resource = new StreamResource(streamSource, fileValue.getFileName(), Flash.app()) {
      public DownloadStream getStream() {
        final StreamSource ss = getStreamSource();
        if (ss == null) {
          return null;
        }
        final DownloadStream ds = new DownloadStream(ss.getStream(), getMIMEType(), getFilename());
        ds.setCacheTime(0);
        try {
          WebBrowser browser = (WebBrowser) result.getWindow().getTerminal();
          if (browser.isIE()) {
            URI uri = new URI(null, null, fileValue.getFileName(), null);
            ds.setParameter("Content-Disposition", "attachment; filename=" + uri.toASCIIString());
          } else {
            ds.setParameter("Content-Disposition", "attachment; filename=\"" + MimeUtility.encodeWord(fileValue.getFileName(), "utf-8", "Q") + "\"");
          }
        } catch (Exception e) {
          ds.setParameter("Content-Disposition", "attachment; filename=" + fileValue.getFileName());
        }
        return ds;
      }
    };
    resource.setMIMEType(fileValue.getMimeType());
    result.setResource(resource);
    return result;
  }
}