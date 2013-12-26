/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.database;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import ru.codeinside.log.Logger;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


@Entity
@EntityListeners(Logger.class)
@Table(name = "groups")
@NamedQueries({

  @NamedQuery(name = "groupByName", query = "SELECT g FROM Group g where g.name=:name"),

  @NamedQuery(name = "groupNamesBySocial", query = "SELECT g.name FROM Group g where g.social = :social"),

  @NamedQuery(name = "groupsBySocial", query = "SELECT g FROM Group g where g.social = :social"),

  @NamedQuery(name = "allGroups", query = "SELECT g FROM Group g"),

  @NamedQuery(name = "allGroupNames", query = "SELECT g.name FROM Group g")

})
@SequenceGenerator(name = "group_seq", sequenceName = "group_seq")
public class Group implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(generator = "group_seq")
  private Long id;

  @Column(nullable = false, unique = true)
  private String name;

  @Column(nullable = false)
  private String title;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(//
    name = "groups_org",//
    joinColumns = @JoinColumn(name = "gid", referencedColumnName = "id"),//
    inverseJoinColumns = @JoinColumn(name = "oid", referencedColumnName = "id"))
  private Set<Organization> organizations;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(//
    name = "groups_usr",//
    joinColumns = @JoinColumn(name = "gid", referencedColumnName = "id"),//
    inverseJoinColumns = @JoinColumn(name = "uid", referencedColumnName = "login"))
  private Set<Employee> employees;

  @ManyToMany(mappedBy = "employeeGroups", fetch = FetchType.LAZY)
  private Set<Employee> supervisorsEmployees;

  @ManyToMany(mappedBy = "employeeGroups", fetch = FetchType.LAZY)
  private Set<Employee> supervisorsOrganizations;


  @Column(updatable = false)
  private boolean social = false;

  public Group() {

  }

  public Group(boolean social) {
    this.social = social;
  }

  public Long getId() {
    return id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public Set<Organization> getOrganizations() {
    if (social) {
      final Set<Organization> empty = Collections.emptySet();
      return Collections.unmodifiableSet(empty);
    }
    if (organizations == null) {
      organizations = new HashSet<Organization>();
    }
    return organizations;
  }

  public Set<Employee> getEmployees() {
    if (!social) {
      final Set<Employee> empty = Collections.emptySet();
      return Collections.unmodifiableSet(empty);
    }
    if (employees == null) {
      employees = new HashSet<Employee>();
    }
    return employees;
  }


  public Set<Employee> getSupervisorsEmployees() {
    if (supervisorsEmployees == null) {
      supervisorsEmployees = new HashSet<Employee>();
    }
    return supervisorsEmployees;
  }

  public void setSupervisorsEmployees(Set<Employee> supervisors) {
    this.supervisorsEmployees = supervisors;
  }

  public Set<Employee> getSupervisorsOrganizations() {
    if (supervisorsOrganizations == null) {
      supervisorsOrganizations = new HashSet<Employee>();
    }
    return supervisorsOrganizations;
  }

  public void setSupervisorsOrganizations(Set<Employee> supervisorsOrganizations) {
    this.supervisorsOrganizations = supervisorsOrganizations;
  }


  /**
   * Признак "социальная" значит только для служащих.
   */
  public boolean isSocial() {
    return social;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }
}
