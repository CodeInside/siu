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
import commons.Streams;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Attachment;
import ru.codeinside.gses.service.Functions;
import ru.codeinside.gses.vaadin.customfield.CustomField;
import ru.codeinside.gses.webui.Flash;
import ru.codeinside.gses.webui.utils.Components;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;


public class EnclosureField extends CustomField implements Field, Serializable {

  final class AttachmentFileValue implements FileValue, Serializable {
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
      return Functions.withTask(new Function<TaskService, byte[]>() {
        @Override
        public byte[] apply(org.activiti.engine.TaskService input) {
          ByteArrayOutputStream bos = new ByteArrayOutputStream();
          try {
            Streams.copy(input.getAttachmentContent(attachmentId), bos);
            return bos.toByteArray();
          } catch (IOException e) {
            return null;
          }
        }
      });
    }

    public String toString() {
      return fileName;
    }
  }

  String fileName;
  String fileType;
  String attachmentId;

  public EnclosureField(final Attachment attachment) {
    Component attachShowButton = Components.createAttachShowButton(attachment, Flash.app());
    setCompositionRoot(attachShowButton);
    fileName = attachment.getName();
    fileType = attachment.getType();
    setValue(new AttachmentFileValue(), true);
  }

  @Override
  public Class<?> getType() {
    return FileValue.class;
  }


}
