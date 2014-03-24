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
import ru.codeinside.adm.database.Role;

import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Set;

final class CertificateSetter implements Function<Employee, Boolean> {

  final X509Certificate certificate;

  public CertificateSetter(X509Certificate certificate) {
    this.certificate = certificate;
  }

  @Override
  public Boolean apply(Employee employee) {
    Set<Role> roles = employee.getRoles();
    if (roles.contains(Role.Executor) || roles.contains(Role.Declarant)
      || roles.contains(Role.Supervisor) || roles.contains(Role.SuperSupervisor)) {
      if (employee.getCertificate() == null) {
        CertificateOfEmployee certificateOfEmployee = new CertificateOfEmployee();
        //certificateOfEmployee.setTimeOfChange(new Timestamp(System.currentTimeMillis()));
        try {
          certificateOfEmployee.setX509(certificate.getEncoded());
          employee.setCertificate(certificateOfEmployee);
          return true;
        } catch (CertificateEncodingException e) {
          // игнорируем
        }
      }
    }
    return false;
  }
}
