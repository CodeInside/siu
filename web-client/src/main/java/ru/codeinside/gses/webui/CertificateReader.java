/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui;

import com.google.common.base.Function;
import ru.codeinside.adm.database.CertificateOfEmployee;
import ru.codeinside.adm.database.Employee;

final public class CertificateReader implements Function<Employee, byte[]> {
  @Override
  public byte[] apply(Employee employee) {
    CertificateOfEmployee certificate = employee.getCertificate();
    return certificate == null ? null : certificate.getX509();
  }
}
