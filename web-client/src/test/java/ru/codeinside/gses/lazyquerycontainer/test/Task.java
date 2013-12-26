/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */
package ru.codeinside.gses.lazyquerycontainer.test;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Task entity for JPA testing.
 */
@Entity
public class Task implements Serializable {
    /** Java serialization version UID. */
    private static final long serialVersionUID = 1L;
	/** Unique identifier of the task. */
    @Id
    @GeneratedValue
    private long taskId;
	/** Name of the task. */
	private String name;
	/** Reporter of the task. */
	private String reporter;
	/** Assignee of the task. */
	private String assignee;
    /**
     * @return the taskId
     */
    public long getTaskId() {
        return taskId;
    }
    /**
     * @param taskId the taskId to set
     */
    public void setTaskId(final long taskId) {
        this.taskId = taskId;
    }
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * @param name the name to set
     */
    public void setName(final String name) {
        this.name = name;
    }
    /**
     * @return the reporter
     */
    public String getReporter() {
        return reporter;
    }
    /**
     * @param reporter the reporter to set
     */
    public void setReporter(final String reporter) {
        this.reporter = reporter;
    }
    /**
     * @return the assignee
     */
    public String getAssignee() {
        return assignee;
    }
    /**
     * @param assignee the assignee to set
     */
    public void setAssignee(final String assignee) {
        this.assignee = assignee;
    }
    /**
     * @return the serialversionuid
     */
    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    @Override
    public String toString() {
        return "Task name: " + name + " reporter: " + reporter + " assignee: " + assignee;
    }
  
}
