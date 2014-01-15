/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.database;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import ru.codeinside.log.Logger;

@Entity
@EntityListeners(Logger.class)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "parent_id", "name" }))
@NamedQueries({
  @NamedQuery(name = "findAllOrganizations", query = "SELECT o FROM Organization o"),
  @NamedQuery(name = "findAllOrganizationIds", query = "SELECT o.id FROM Organization o")
})
@SequenceGenerator(name = "organization_seq", sequenceName = "organization_seq")
public class Organization implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "organization_seq")
	private Long id;

	@Column(nullable = false)
	private String name;

	@ManyToOne(fetch = FetchType.LAZY)
	private Organization parent;

	@Temporal(TemporalType.TIMESTAMP)
	private Date date = new Date();

	@ManyToOne(fetch = FetchType.LAZY)
	private Employee creator;

	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
	private Set<Organization> organizations;

	@ManyToMany(mappedBy = "organizations", fetch = FetchType.LAZY)
	private Set<Group> groups;

	@OneToMany(mappedBy = "organization", fetch = FetchType.LAZY)
	private Set<Employee> employees;

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDate() {
		return date;
	}

	public Employee getCreator() {
		return creator;
	}

	public void setCreator(Employee creator) {
		this.creator = creator;
	}

	public Set<Employee> getEmployees() {
		if (employees == null) {
			employees = new HashSet<Employee>();
		}
		return employees;
	}

	public Set<Group> getGroups() {
		if (groups == null) {
			groups = new HashSet<Group>();
		}
		return groups;
	}

	public Organization getParent() {
		return parent;
	}

	public void setParent(Organization parent) {
		this.parent = parent;
	}

	public Set<Organization> getOrganizations() {
		if (organizations == null) {
			organizations = new HashSet<Organization>();
		}
		return organizations;
	}

}
