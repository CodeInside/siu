/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.database;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import ru.codeinside.log.Logger;

@Entity(name = "procedure_process_definition")
@EntityListeners(Logger.class)
public class ProcedureProcessDefinition implements Serializable {

	private static final long serialVersionUID = 6583425270460724689L;

	private Double version;

	@Id
	private String processDefinitionId;

	private String processDefinitionKey;

	@JoinColumn(nullable = false)
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private Procedure procedure;

	private DefinitionStatus status;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dateCreated = new Date();

	@OneToOne
	private ProcedureProcessDefinition child;

	@ManyToOne(fetch = FetchType.LAZY)
	private Employee creator;

	public void setChild(ProcedureProcessDefinition child) {
		this.child = child;
	}

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	public DefinitionStatus getStatus() {
		return status;
	}

	public void setStatus(DefinitionStatus status) {
		this.status = status;
	}

	public Employee getCreator() {
		return creator;
	}

	public void setCreator(Employee creator) {
		this.creator = creator;
	}

	public Procedure getProcedure() {
		return procedure;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setProcedure(Procedure procedure) {
		this.procedure = procedure;
	}

	public Double getVersion() {
		return version;
	}

	public void setVersion(Double version) {
		this.version = version;
	}

	public String getProcessDefinitionKey() {
		return processDefinitionKey;
	}

	public void setProcessDefinitionKey(String processDefinitionKey) {
		this.processDefinitionKey = processDefinitionKey;
	}

}
