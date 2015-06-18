package ru.codeinside.gses.webui.form;

import org.activiti.engine.impl.cmd.CreateAttachmentCmd;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.task.Attachment;
import ru.codeinside.gses.activiti.FileValue;
import ru.codeinside.gses.activiti.forms.types.AttachmentType;
import ru.codeinside.gses.activiti.ftarchive.AttachmentFileValue;

import java.io.ByteArrayInputStream;

public class ProcessInstanceAttachmentConverter implements AttachmentConverter {

  private final String processInstanceId;

  public ProcessInstanceAttachmentConverter(String processInstanceId) {
    this.processInstanceId = processInstanceId;
  }

  @Override
  public Object convertAttachment(CommandContext commandContext, Object modelValue) {
    if (modelValue instanceof AttachmentFileValue) {
      AttachmentFileValue attachmentFileValue = (AttachmentFileValue) modelValue;
      modelValue = attachmentFileValue.getAttachment().getId() + AttachmentType.SUFFIX;
    } else if (modelValue instanceof FileValue) {
      FileValue fileValue = (FileValue) modelValue;
      CreateAttachmentCmd createAttachmentCmd = new CreateAttachmentCmd(//
          fileValue.getMimeType(), // attachmentType
          null, // taskId
          processInstanceId, // processInstanceId
          fileValue.getFileName(), // attachmentName
          null, // attachmentDescription
          new ByteArrayInputStream(fileValue.getContent()), // content
          null // url
      );
      Attachment attachment = createAttachmentCmd.execute(commandContext);
      modelValue = attachment.getId() + AttachmentType.SUFFIX;
    }
    return modelValue;
  }
}
