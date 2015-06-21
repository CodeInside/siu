package ru.codeinside.gses.webui.form;

import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.util.IoUtil;
import ru.codeinside.gses.activiti.FileValue;
import ru.codeinside.gses.activiti.forms.types.AttachmentType;
import ru.codeinside.gses.activiti.ftarchive.AttachmentFileValue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class StartEventAttachmentConverter implements AttachmentConverter {

  private DataAccumulator dataAccumulator;

  StartEventAttachmentConverter(DataAccumulator dataAccumulator) {
    this.dataAccumulator = dataAccumulator;
  }

  @Override
  public Object convertAttachment(CommandContext commandContext, Object modelValue) {
    if (modelValue instanceof AttachmentFileValue) {
      AttachmentFileValue attachmentFileValue = (AttachmentFileValue) modelValue;
      modelValue = attachmentFileValue.getAttachment().getId() + AttachmentType.SUFFIX;
    } else if (modelValue instanceof FileValue) {
      FileValue fileValue = (FileValue) modelValue;
      TmpAttachment attachment = createTmpAttachment(fileValue.getMimeType(), fileValue.getFileName(), new ByteArrayInputStream(fileValue.getContent()));
      dataAccumulator.setAttachment(attachment);
      modelValue = attachment.getId() + AttachmentType.SUFFIX;
    }
    return modelValue;
  }

  public static TmpAttachment createTmpAttachment(String attachmentType, String attachmentName, InputStream content) {
    TmpAttachment attachment = new TmpAttachment();
    attachment.setType(attachmentType);
    attachment.setName(attachmentName);
    byte[] contentBytes = IoUtil.readInputStream(content, attachmentName);
    attachment.setContent(contentBytes);
    return attachment;
  }
}
