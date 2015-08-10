package ru.codeinside.gses.webui.components.sign;

import ru.codeinside.adm.AdminService;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.adm.database.Employee;

import java.text.SimpleDateFormat;

public class SignUtils {
  private SignUtils() {}

  public static final String LOCK_CERT_HINT = "Этот сертификат заблокирован на 10 минут.";

  public static String lockCertAndGetUnlockTimeMessage(String employeeLogin, long certSerialNumber) {
    AdminService adminService = AdminServiceProvider.get();
    Employee employee = adminService.findEmployeeByLogin(employeeLogin);
    if (!employee.isCertLocked(certSerialNumber)) {
      employee.addLockedCert(certSerialNumber);
      adminService.saveEmployee(employee);
    }

    return "Время окончания блокировки: " +
        new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(employee.certUnlockTime(certSerialNumber));
  }
}
