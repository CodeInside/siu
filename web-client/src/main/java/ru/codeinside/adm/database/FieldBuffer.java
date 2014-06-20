/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.database;

import org.eclipse.persistence.annotations.CascadeOnDelete;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * @since 1.0.8
 */
@Entity
@Table(name = "field_buffer")
@IdClass(value = FieldBuffer.PK.class)
public class FieldBuffer {

  @Id
  @JoinColumn(name = "task")
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private FormBuffer formBuffer;

  @Id
  @Column(name = "field")
  private String fieldId;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "created", nullable = false, updatable = false)
  private Date createDate;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "updated", nullable = false)
  private Date updateDate;

  /**
   * text, fileName
   */
  @Column(name = "text_", columnDefinition = "text")
  private String textValue;

  /**
   * MIME type
   */
  @Column(name = "mime")
  private String mime;

  /**
   * long, boolean, fileSize
   */
  @Column(name = "long_")
  private Long longValue;

  /**
   * bytes, fileContent
   */
  @CascadeOnDelete
  @ManyToOne(fetch = FetchType.LAZY, optional = true)
  @JoinColumn(name = "bytes")
  private BytesBuffer bytesValue;

  protected FieldBuffer() {

  }

  public FieldBuffer(FormBuffer formBuffer, String fieldId, Date now) {
    this.formBuffer = formBuffer;
    this.fieldId = fieldId;
    this.createDate = this.updateDate = now;
  }

  public FormBuffer getFormBuffer() {
    return formBuffer;
  }

  public String getFieldId() {
    return fieldId;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date _) {
    this.updateDate = _;
  }

  public String getTextValue() {
    return textValue;
  }

  public void setTextValue(String _) {
    textValue = _;
  }

  public Long getLongValue() {
    return longValue;
  }

  public void setLongValue(Long _) {
    this.longValue = _;
  }

  public BytesBuffer getBytesValue() {
    return bytesValue;
  }

  public void setBytesValue(BytesBuffer _) {
    this.bytesValue = _;
  }

  public String getMime() {
    return mime;
  }

  public void setMime(String _) {
    this.mime = _;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    FieldBuffer that = (FieldBuffer) o;
    if (fieldId != null ? !fieldId.equals(that.fieldId) : that.fieldId != null) return false;
    if (formBuffer != null ? !formBuffer.equals(that.formBuffer) : that.formBuffer != null) return false;
    return true;
  }

  @Override
  public int hashCode() {
    int result = formBuffer != null ? formBuffer.hashCode() : 0;
    result = 31 * result + (fieldId != null ? fieldId.hashCode() : 0);
    return result;
  }

  final public static class PK {

    final public String formBuffer;
    final public String fieldId;

    public PK(String formBuffer, String fieldId) {
      this.formBuffer = formBuffer;
      this.fieldId = fieldId;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      PK pk = (PK) o;
      if (fieldId != null ? !fieldId.equals(pk.fieldId) : pk.fieldId != null) return false;
      if (formBuffer != null ? !formBuffer.equals(pk.formBuffer) : pk.formBuffer != null) return false;
      return true;
    }

    @Override
    public int hashCode() {
      int result = formBuffer != null ? formBuffer.hashCode() : 0;
      result = 31 * result + (fieldId != null ? fieldId.hashCode() : 0);
      return result;
    }
  }
}
