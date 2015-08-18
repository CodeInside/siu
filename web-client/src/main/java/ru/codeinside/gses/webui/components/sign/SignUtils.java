package ru.codeinside.gses.webui.components.sign;

import ru.codeinside.adm.AdminService;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.database.CertificateOfEmployee;
import ru.codeinside.adm.database.Employee;
import ru.codeinside.gses.cert.X509;
import ru.codeinside.gses.webui.CertificateVerifier;

import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SignUtils {
  private SignUtils() {}

  public static final String LOCK_CERT_HINT = "Этот сертификат заблокирован на 10 минут.";

  public static String lockCertAndGetUnlockTimeMessage(String employeeLogin, long certSerialNumber) {
    AdminService adminService = AdminServiceProvider.get();
    Employee employee = adminService.findEmployeeByLogin(employeeLogin);
    if (!employee.isCertLocked(certSerialNumber)) {
      employee.addCertAttempt(certSerialNumber, getCertMaxAttempts());
      adminService.saveEmployee(employee);
    }

    Date certUnlockTime = employee.certUnlockTime(certSerialNumber);
    return certUnlockTime == null ? null : "Время окончания блокировки: " +
        new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(employee.certUnlockTime(certSerialNumber));
  }

  public static void removeLockedCert(String employeeLogin, long certSerialNumber) {
    AdminService adminService = AdminServiceProvider.get();
    Employee employee = adminService.findEmployeeByLogin(employeeLogin);
    if (employee.removeLockedCert(certSerialNumber)) {
      adminService.saveEmployee(employee);
    }
  }

  public static int certAttemptsCount(String employeeLogin, long certSerialNumber) {
    AdminService adminService = AdminServiceProvider.get();
    Employee employee = adminService.findEmployeeByLogin(employeeLogin);
    return getCertMaxAttempts() - employee.certAttemptsCount(certSerialNumber);
  }

  public static boolean isEmployeeCertificateExpired(String employeeLogin) {
    AdminService adminService = AdminServiceProvider.get();
    Employee employee = adminService.findEmployeeByLogin(employeeLogin);
    CertificateOfEmployee certificateOfEmployee = employee.getCertificate();
    return isCertificateExpired(certificateOfEmployee);
  }

  public static boolean isCertificateExpired(CertificateOfEmployee certificateOfEmployee) {
    if (certificateOfEmployee == null) {
      return false;
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
    return false;
  }

  public static int getCertMaxAttempts() {
    String maxAttempts = AdminServiceProvider.get().getSystemProperty(CertificateVerifier.CERT_PASSWORD_ATTEMPTS);
    try {
      return (maxAttempts != null && !maxAttempts.isEmpty()) ? Integer.valueOf(maxAttempts) : 5;
    } catch (NumberFormatException e) {
      return 5;
    }
  }
}
