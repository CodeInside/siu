package ru.codeinside.gses.webui.form;

import org.activiti.engine.task.Attachment;

import java.io.Serializable;
import java.util.UUID;

public class TmpAttachment implements Attachment, Serializable {

  private static final long serialVersionUID = 1L;

  private final String id;
  private String name;
  private String description;
  private String type;
  private byte[] content;

  public TmpAttachment() {
    id = UUID.randomUUID().toString();
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  @Override
  public String getTaskId() {
    return null;
  }

  @Override
  public String getProcessInstanceId() {
    return null;
  }

  @Override
  public String getUrl() {
    return null;
  }

  public void setContent(byte[] content) {
    this.content = content;
  }

  public byte[] getContent() {
    return content;
  }
}
