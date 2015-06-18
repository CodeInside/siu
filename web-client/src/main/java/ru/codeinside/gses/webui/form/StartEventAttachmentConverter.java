package ru.codeinside.gses.webui.form;

import org.activiti.engine.impl.db.DbSqlSession;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.AttachmentEntity;
import org.activiti.engine.impl.persistence.entity.ByteArrayEntity;
import org.activiti.engine.impl.persistence.entity.CommentEntity;
import org.activiti.engine.impl.persistence.entity.CommentManager;
import org.activiti.engine.impl.util.ClockUtil;
import org.activiti.engine.impl.util.IoUtil;
import org.activiti.engine.task.Attachment;
import org.activiti.engine.task.Event;
import ru.codeinside.gses.activiti.FileValue;
import ru.codeinside.gses.activiti.forms.types.AttachmentType;
import ru.codeinside.gses.activiti.ftarchive.AttachmentFileValue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class StartEventAttachmentConverter implements AttachmentConverter {

  @Override
  public Object convertAttachment(CommandContext commandContext, Object modelValue) {
    if (modelValue instanceof AttachmentFileValue) {
      AttachmentFileValue attachmentFileValue = (AttachmentFileValue) modelValue;
      modelValue = attachmentFileValue.getAttachment().getId() + AttachmentType.SUFFIX;
    } else if (modelValue instanceof FileValue) {
      FileValue fileValue = (FileValue) modelValue;
      CreateTmpAttachmentCmd createAttachmentCmd = new CreateTmpAttachmentCmd(
          fileValue.getMimeType(), // attachmentType
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

  class CreateTmpAttachmentCmd implements Command<Attachment> {

    protected String attachmentType;
    protected String attachmentName;
    protected String attachmentDescription;
    protected InputStream content;
    protected String url;

    public CreateTmpAttachmentCmd(String attachmentType, String attachmentName, String attachmentDescription, InputStream content, String url) {
      this.attachmentType = attachmentType;
      this.attachmentName = attachmentName;
      this.attachmentDescription = attachmentDescription;
      this.content = content;
      this.url = url;
    }

    public Attachment execute(CommandContext commandContext) {
      AttachmentEntity attachment = new AttachmentEntity();
      attachment.setName(attachmentName);
      attachment.setDescription(attachmentDescription);
      attachment.setType(attachmentType);
      attachment.setUrl(url);

      DbSqlSession dbSqlSession = commandContext.getDbSqlSession();
      dbSqlSession.insert(attachment);

      if (content!=null) {
        byte[] bytes = IoUtil.readInputStream(content, attachmentName);
        ByteArrayEntity byteArray = new ByteArrayEntity(bytes);
        dbSqlSession.insert(byteArray);
        attachment.setContentId(byteArray.getId());
      }

      CommentManager commentManager = commandContext.getCommentManager();
      if (commentManager.isHistoryEnabled()) {
        String userId = Authentication.getAuthenticatedUserId();
        CommentEntity comment = new CommentEntity();
        comment.setUserId(userId);
        comment.setType(CommentEntity.TYPE_EVENT);
        comment.setTime(ClockUtil.getCurrentTime());
        comment.setAction(Event.ACTION_ADD_ATTACHMENT);
        comment.setMessage(attachmentName);
        commentManager.insert(comment);
      }

      return attachment;
    }
  }
}
