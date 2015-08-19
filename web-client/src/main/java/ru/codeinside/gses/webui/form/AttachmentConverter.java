package ru.codeinside.gses.webui.form;

import org.activiti.engine.impl.interceptor.CommandContext;

public interface AttachmentConverter {
  Object convertAttachment(CommandContext commandContext, Object modelValue);
}
