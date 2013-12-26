/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti;

import com.google.common.base.Function;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Attachment;
import ru.codeinside.gses.service.Functions;
import ru.codeinside.gses.vaadin.customfield.CustomField;
import ru.codeinside.gses.webui.Flash;
import ru.codeinside.gses.webui.utils.Components;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;


public class EnclosureField extends CustomField implements Field, Serializable {
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

  String fileName;
  String fileType;
  long fileSize = -1;
  byte[] fileData;

  public EnclosureField(final Attachment attachment) {
    Component attachShowButton = Components.createAttachShowButton(attachment, Flash.app());
    setCompositionRoot(attachShowButton);
    fileName = attachment.getName();
    fileType = attachment.getType();
    fileData = Functions.withTask(new Function<TaskService, byte[]>() {
      @Override
      public byte[] apply(org.activiti.engine.TaskService input) {
        return convertToByteArray(input.getAttachmentContent(attachment.getId()));
      }

      private byte[] convertToByteArray(InputStream inputStream) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] tmp = new byte[4096];
        int ret = 0;

        try {
          while ((ret = inputStream.read(tmp)) > 0) {
            bos.write(tmp, 0, ret);
          }
        } catch (IOException e) {
          return null;
        }
        return bos.toByteArray();
      }
    });
    fileSize = fileData.length;
    setValue(new InMemoryFileValue(), true);
  }

  @Override
  public Class<?> getType() {
    return FileValue.class;
  }


}
