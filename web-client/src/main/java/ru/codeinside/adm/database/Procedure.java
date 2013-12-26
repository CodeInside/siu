/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.database;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import ru.codeinside.log.Logger;

@Entity
@EntityListeners(Logger.class)
@NamedQuery(name = "findAllProcedures", query = "SELECT p FROM Procedure p")
@SequenceGenerator(name = "procedure_seq", sequenceName = "procedure_seq")
public class Procedure implements Serializable{

	private static final long serialVersionUID = 223L;
	
	@Id
	@GeneratedValue(generator = "procedure_seq")
	private Long id;
	
	private ProcedureType type;
	
	@Column(length=1500)
	private String name;
	
	@Column(length=1500)
	private String description;
	
	private String version;
	
	private String status;

	@ManyToOne
	private Service service;
	
	@Column(unique = true)
	private Long registerCode;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Employee creator;
	
	@OneToMany(mappedBy = "procedure", fetch = FetchType.LAZY)
	private Set<ProcedureProcessDefinition> processDefinitions; 

	@Temporal(TemporalType.TIMESTAMP)
	private Date dateCreated = new Date();
	
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setName(String name) {
		this.name = name;		
	}

	public void setDescription(String description) {
		this.description = description;		
	}

	public void setService(Service service) {
		this.service = service;		
	}

	public void setCreator(Employee creator) {
		this.creator = creator;		
	}

	public String getName() {
		return name;
	}

	public String getId() {
		return id.toString();
	}

	public String getDescription() {
		return description;
	}

	public ProcedureType getType() {
		return type;
	}

	public void setType(ProcedureType type) {
		this.type = type;
	}
	
	public Service getService() {
		return service;
	}

	public Long getRegisterCode() {
		return registerCode;
	}

	public void setRegisterCode(Long registerCode) {
		this.registerCode = registerCode;
	}

}
