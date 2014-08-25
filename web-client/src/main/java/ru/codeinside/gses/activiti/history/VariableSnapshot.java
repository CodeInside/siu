/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.history;

import ru.codeinside.adm.database.AuditValue;
import ru.codeinside.gses.cert.NameParts;
import ru.codeinside.gses.cert.X509;

import java.io.Serializable;

final public class VariableSnapshot implements Serializable {

  private static final long serialVersionUID = 1L;

  final public String name;
  final public String login;
  final public Long historyId;
  final public boolean sign;
  final public boolean verified;
  final public String certOwnerName;
  final public String certOwnerOrgName;


  private VariableSnapshot(String name,
                           String login,
                           Long historyId,
                           boolean sign,
                           boolean verified,
                           String certOwnerName,
                           String certOwnerOrgName) {
    this.name = name;
    this.login = login;
    this.historyId = historyId;
    this.sign = sign;
    this.certOwnerName = certOwnerName;
    this.certOwnerOrgName = certOwnerOrgName;
    this.verified = sign && verified;
  }

  public static VariableSnapshot withName(String varName) {
    return new VariableSnapshot(varName, null, null, false, false, null, null);
  }

  public static VariableSnapshot withAudit(String varName, AuditValue value, boolean verified) {
    final boolean hasSignature = value.getSign() != null || value.getCert() != null;
    String certOwnerName = null;
    String certOwnerOrgName = null;
    if (hasSignature) {
      final NameParts nameParts = X509.getSubjectParts(value.getCert());
      String surName = nameParts.getSurName();
      String givenName = nameParts.getGivenName();
      certOwnerName = surName == null || givenName == null ? nameParts.getCommonName() : surName+' '+givenName;
      certOwnerOrgName = nameParts.getOrganization();
    }
    return new VariableSnapshot(varName,
      value.getLogin(),
      value.getHid(),
      hasSignature,
      verified,
      certOwnerName,
      certOwnerOrgName);
  }

  @Override
  public String toString() {
    return "{" +
      "name='" + name + '\'' +
      ", login='" + login + '\'' +
      ", historyId=" + historyId +
      ", sign=" + sign +
      '}';
  }
}
