/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.forms.values;

import ru.codeinside.gses.activiti.forms.api.values.Audit;

final class ValueAudit implements Audit {

  final String login;
  final boolean verified;
  final String owner;
  final String organization;

  ValueAudit(String login, boolean verified, String owner, String organization) {
    this.login = login;
    this.verified = verified;
    this.owner = owner;
    this.organization = organization;
  }

  @Override
  public String getLogin() {
    return login;
  }

  @Override
  public boolean isVerified() {
    return verified;
  }

  @Override
  public String getOwner() {
    return owner;
  }

  @Override
  public String getOrganization() {
    return organization;
  }
}
