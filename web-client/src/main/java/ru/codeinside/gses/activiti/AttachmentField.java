/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti;

import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ProgressIndicator;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Window;
import org.activiti.engine.task.Attachment;
import ru.codeinside.gses.vaadin.customfield.CustomField;
import ru.codeinside.gses.webui.Flash;
import ru.codeinside.gses.webui.utils.Components;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Arrays;

final public class AttachmentField extends CustomField implements Serializable, Upload.Receiver {

  final class InMemoryFileValue implements FileValue, Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public String getMimeType() {
      return fileType;
    }

    @Override
    public String getFileName() {
      return fileName;
    }

    @Override
    public byte[] getContent() {
      return fileData;
    }

    public String toString() {
      return fileName;
    }
  }

  private static final long serialVersionUID = 1L;
  final Upload upload;
  final HorizontalLayout layout = new HorizontalLayout();
  final ProgressIndicator indicator = new ProgressIndicator();
  final Label fileInfo = new Label();
  final Label sizeInfo = new Label();
  private Button removeAttachmentButton;
  String fileName;
  String fileType;
  long fileSize = -1;
  byte[] fileData;

  Component oldValue;

  public AttachmentField(final String name, Attachment attachment) {

    upload = new Upload(null, this);
    upload.setButtonCaption("Выбрать файл");
    upload.setImmediate(true);
    upload.addListener(new Upload.StartedListener() {
      private static final long serialVersionUID = 1L;

      @Override
      public void uploadStarted(Upload.StartedEvent event) {
        indicator.setValue(0f);
        indicator.setVisible(true);
        fileInfo.setValue(event.getFilename());
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
        fileName = event.getFilename();
        fileType = event.getMIMEType();
        fileSize = event.getLength();
        setValue(new InMemoryFileValue(), true);
        removeAttachmentButton.setVisible(true);
        upload.setVisible(false);
      }
    });

    upload.addListener(new Upload.FailedListener() {
      private static final long serialVersionUID = 1L;

      @Override
      public void uploadFailed(Upload.FailedEvent event) {
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
    layout.addComponent(fileInfo);
    layout.addComponent(sizeInfo);


    setCaption(name);
    setValidationVisible(true);
    setRequiredError("Выберите файл!");// "" + name + "\""

    initRemoveAttachmentButton();
    if (attachment != null) {
      oldValue = Components.createAttachShowButton(attachment, Flash.app());
      layout.addComponent(oldValue);
      setValue(new AttachmentFileValue(attachment), true);
      removeAttachmentButton.setVisible(true);
    }

    layout.addComponent(upload);
    layout.addComponent(removeAttachmentButton);
    setCompositionRoot(layout);
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
    return new ByteArrayOutputStream() {
      @Override
      public void close() throws IOException {
        fileData = Arrays.copyOf(buf, count);
      }
    };
  }

  @Override
  public void discard() throws SourceException {
    super.discard();
    resetUpload();
  }

  private void resetUpload() {
    fileInfo.setValue(null);
    sizeInfo.setValue(null);
    fileName = null;
    fileType = null;
    fileSize = -1;
    fileData = null;
  }

}