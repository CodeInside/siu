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
import ru.codeinside.gses.cert.X509;

import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.Set;

final class IsCertificateRequired implements Function<Employee, Boolean> {

  @Override
  public Boolean apply(Employee employee) {
    Set<Role> roles = employee.getRoles();
    if (roles.contains(Role.Executor) || roles.contains(Role.Declarant)
      || roles.contains(Role.Supervisor) || roles.contains(Role.SuperSupervisor)) {
      CertificateOfEmployee certificateOfEmployee =  employee.getCertificate();

      if (certificateOfEmployee == null) {
        return true;
      } else {
        X509Certificate certificate = X509.decode(certificateOfEmployee.getX509());
        if (certificate == null) return true;
        
        try {
          certificate.checkValidity();
        } catch (CertificateExpiredException e) {
          return true;
        } catch (CertificateNotYetValidException e) {
          return true;
        }
      }
    }
    return false;
  }
}
