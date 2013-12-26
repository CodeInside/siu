/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm;

import ru.codeinside.adm.database.Role;

import java.io.Serializable;
import java.util.Set;

final public class UserItem implements Serializable {

  private static final long serialVersionUID = 3L;

  public String getPassword1() {
    return password1;
  }

  public void setPassword1(String password1) {
    this.password1 = password1;
  }

  public String getPassword2() {
    return password2;
  }

  public void setPassword2(String password2) {
    this.password2 = password2;
  }

  public String getFio() {
    return fio;
  }

  public void setFio(String fio) {
    this.fio = fio;
  }

  public Set<Role> getRoles() {
    return roles;
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }

  public Set<String> getGroups() {
    return groups;
  }

  public void setGroups(Set<String> groups) {
    this.groups = groups;
  }

  public Set<String> getEmployeeGroups() {
    return employeeGroups;
  }

  public void setEmployeeGroups(Set<String> employeeGroups) {
    this.employeeGroups = employeeGroups;
  }

  public Set<String> getAllSocialGroups() {
    return allSocialGroups;
  }

  public void setAllSocialGroups(Set<String> allGroups) {
    this.allSocialGroups = allGroups;
  }

  public Set<String> getOrganizationGroups() {
    return organizationGroups;
  }

  public void setOrganizationGroups(Set<String> organizationGroups) {
    this.organizationGroups = organizationGroups;
  }

  public boolean isLocked() {
    return locked;
  }

  public void setLocked(boolean locked) {
    this.locked = locked;
  }

  public byte[] getX509() {
    return x509;
  }

  public void setX509(byte[] x509) {
    this.x509 = x509;
  }

  private String password1;
  private String password2;
  private String fio;
  private Set<Role> roles;
  private Set<String> groups;
  private Set<String> employeeGroups;
  private Set<String> organizationGroups;
  private Set<String> allSocialGroups;
  private boolean locked;
  private byte[] x509;

}
