/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.log;

import com.google.common.base.Preconditions;
import org.apache.commons.lang.StringUtils;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Entity
@SequenceGenerator(name = "log_seq", sequenceName = "log_seq")
public class Log implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(generator = "log_seq")
  private Long id;

  @Embedded
  private Actor actor;

  @Column(nullable = false)
  private String entityName;

  @Column(nullable = false)
  private String entityId;

  @Column(nullable = false)
  private String action;
  @Column(nullable = false, name = "action_result")
  private Boolean actionResult;
  @Temporal(TemporalType.TIMESTAMP)
  private Date date = new Date();


  @Column(length = 1535)
  private String info;

  public Log() {
    actionResult = true;
  }

  public Log(Actor actor, String entityName, String entityId, String action, String info, Boolean actionResult) {
    Preconditions.checkArgument(StringUtils.isNotBlank(entityId), "Entity Id is empty");
    Preconditions.checkArgument(StringUtils.isNotBlank(entityName), "Entity Name is empty");
    Preconditions.checkArgument(StringUtils.isNotBlank(action), "Action is empty");
    this.actor = actor;
    this.entityName = entityName;
    this.entityId = entityId;
    this.action = action;
    this.info = info;
    this.actionResult = actionResult;

  }

  public Long getId() {
    return id;
  }

  public Actor getActor() {
    return actor;
  }

  public String getEntityName() {
    return entityName;
  }

  public String getEntityId() {
    return entityId;
  }


  public String getAction() {
    return action;
  }

  public Date getDate() {
    return date;
  }

  public String getInfo() {
    return info;
  }

  public Boolean getActionResult() {
    return actionResult;
  }

  public void setInfo(String info) {
    this.info = info;
  }
}
