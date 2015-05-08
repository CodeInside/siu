/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.database;

import ru.codeinside.log.Logger;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import java.io.Serializable;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;


@Entity
@EntityListeners(Logger.class)
@NamedQueries({
  @NamedQuery(name = "findAllEmployees", query = "SELECT u FROM Employee u"),
  @NamedQuery(name = "findAllEmployeeLogins", query = "SELECT u.login FROM Employee u")
})
public class Employee implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(nullable = false, length = 64)
  private String login;

  @Column(nullable = false)
  private String passwordHash;

  @Column(unique = true, length = 11) //TODO проверить уникальность при создании, редактировании и импорте
  private String snils;

  private String status;

  private String fio;

  @Temporal(TemporalType.TIMESTAMP)
  private Date date = new Date();

  private String creator;

  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(name = "roles", joinColumns = @JoinColumn(name = "uid"), uniqueConstraints = @UniqueConstraint(columnNames = {
    "uid", "gid"}))
  @Enumerated(EnumType.STRING)
  @Column(name = "gid", length = 32)
  private Set<Role> roles;

  @ManyToMany(mappedBy = "employees", fetch = FetchType.LAZY)
  private Set<Group> groups;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(//
    name = "employee_groups",//
    joinColumns = @JoinColumn(name = "eid", referencedColumnName = "login"),//
    inverseJoinColumns = @JoinColumn(name = "gid", referencedColumnName = "id"))
  private Set<Group> employeeGroups;

  @ManyToMany
  @JoinTable(//
    name = "organization_groups",//
    joinColumns = @JoinColumn(name = "eid", referencedColumnName = "login"),//
    inverseJoinColumns = @JoinColumn(name = "gid", referencedColumnName = "id"))
  private Set<Group> organizationGroups;


  @JoinColumn(nullable = false)
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private Organization organization;

  @Column(nullable = false)
  private boolean locked;

  @OneToOne(optional = true, fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  CertificateOfEmployee certificate;

  public boolean isLocked() {
    return locked;
  }

  public void setLocked(boolean locked) {
    this.locked = locked;
  }

  public Set<Role> getRoles() {
    if (roles == null) {
      roles = EnumSet.noneOf(Role.class);
    }
    return roles;
  }

  public Employee() {
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getPasswordHash() {
    return passwordHash;
  }

  public void setPasswordHash(String passwordHash) {
    this.passwordHash = passwordHash;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getFio() {
    return fio;
  }

  public void setFio(String fio) {
    this.fio = fio;
  }

  public Date getDate() {
    return date;
  }

  public String getCreator() {
    return creator;
  }

  public void setCreator(String creator) {
    this.creator = creator;
  }

  public Set<Group> getGroups() {
    if (groups == null) {
      groups = new HashSet<Group>();
    }
    return groups;
  }

  public Set<Group> getEmployeeGroups() {
    if (employeeGroups == null) {
      employeeGroups = new HashSet<Group>();
    }
    return employeeGroups;
  }

  public void setEmployeeGroups(Set<Group> employeeGroups) {
    this.employeeGroups = employeeGroups;
  }

  public Set<Group> getOrganizationGroups() {
    if (organizationGroups == null) {
      organizationGroups = new HashSet<Group>();
    }
    return organizationGroups;
  }

  public void setOrganizationGroups(Set<Group> organizationGroups) {
    this.organizationGroups = organizationGroups;
  }

  public Organization getOrganization() {
    return organization;
  }

  public void setOrganization(Organization organization) {
    this.organization = organization;
  }

  public Set<String> getRoleNames() {
    final TreeSet<String> result = new TreeSet<String>();
    for (Role role : getRoles()) {
      result.add(role.description);
    }
    return result;
  }

  public CertificateOfEmployee getCertificate() {
    return certificate;
  }

  public void setCertificate(CertificateOfEmployee certificate) {
    this.certificate = certificate;
  }

  public String getSnils() {
    return snils;
  }

  public void setSnils(String snils) {
    this.snils = snils;
  }
}
