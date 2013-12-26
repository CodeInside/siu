/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.database;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import ru.codeinside.log.Logger;

@Entity
@EntityListeners(Logger.class)
@NamedQuery(name = "findAllServices", query = "SELECT s FROM Service s")
@SequenceGenerator(name = "service_seq", sequenceName = "service_seq")
public class Service implements Serializable {

	private static final long serialVersionUID = 222L;

	@Id
	@GeneratedValue(generator = "service_seq")
	private Long id;

	@Column(length = 1500)
	private String name;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dateCreated = new Date();

	@ManyToOne(fetch = FetchType.LAZY)
	private Employee creator;

	@OneToMany(mappedBy = "service", fetch = FetchType.LAZY)
	private Set<Procedure> procedures;

  @ElementCollection(targetClass = String.class)
  @CollectionTable(name = "service_declarant_type")
  @Column(name="declarant_type")
  private List<String> declarantTypes;

  @Column(unique = true)
  private Long registerCode;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setCreator(Employee creator) {
		this.creator = creator;
	}

	@Override
	public String toString() {
		return getId() != null ? getId().toString() : "";
	}

  public List<String> getDeclarantTypes() {
    return declarantTypes;
  }

  public void setDeclarantTypes(List<String> declarantTypes) {
    this.declarantTypes = declarantTypes;
  }

  public void addDeclarantType(String declarantType){
    if(declarantTypes==null){
      declarantTypes = new ArrayList<String>();
    }
    declarantTypes.add(declarantType);
  }

    public Set<Procedure> getProcedures() {
        return procedures;
    }


  public Long getRegisterCode() {
    return registerCode;
  }

  public void setRegisterCode(Long registerCode) {
    this.registerCode = registerCode;
  }
}