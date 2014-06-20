/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.ftarchive;

import com.google.common.base.Function;
import commons.Streams;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Attachment;
import ru.codeinside.gses.activiti.FileValue;
import ru.codeinside.gses.service.Functions;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Ссылка на существующие вложение.
 */
final public class AttachmentFileValue implements FileValue {


  final Attachment attachment;


  AttachmentFileValue(Attachment attachment) {
    this.attachment = attachment;
  }

  @Override
  public String getFileName() {
    return attachment.getName();
  }

  @Override
  public String getMimeType() {
    return attachment.getType();
  }


  @Override
  public byte[] getContent() {
    return Functions.withTask(new Function<TaskService, byte[]>() {
      public byte[] apply(TaskService s) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
          Streams.copy(s.getAttachmentContent(attachment.getId()), baos);
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
        return baos.toByteArray();
      }
    });
  }

  public Attachment getAttachment() {
    return attachment;
  }

  @Override
  public String toString() {
    return attachment.getName();
  }
}
