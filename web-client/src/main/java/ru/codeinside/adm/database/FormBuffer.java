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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @since 1.0.8
 */
@Entity
@Table(name = "form_buffer")
public class FormBuffer {

  @Id
  @Column(name = "task")
  private String taskId;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "created", nullable = false, updatable = false)
  private Date createDate;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "updated", nullable = false)
  private Date updateDate;

  @CascadeOnDelete
  @OneToMany(mappedBy = "formBuffer", fetch = FetchType.LAZY)
  private Set<FieldBuffer> fields;

  protected FormBuffer() {

  }

  public FormBuffer(String taskId, Date now) {
    this.taskId = taskId;
    this.createDate = this.updateDate = now;
    this.fields = new HashSet<FieldBuffer>();
  }

  /**
   * @return идентификатор блока UserTask.
   */
  public String getTaskId() {
    return taskId;
  }

  /**
   * @return момент создания.
   */
  public Date getCreateDate() {
    return createDate;
  }

  /**
   * @return момент изменения.
   */
  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  public Set<FieldBuffer> getFields() {
    return fields;
  }
}
