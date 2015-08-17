package ru.codeinside.adm.database;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@Table(name = "locked_certs")
@IdClass(value = LockedCert.PK.class)
public class LockedCert {

  @Id
  @Column(name = "cert_id")
  private Long certSerialNumber;

  @Id
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "employee_login", nullable = false)
  private Employee employee;

  @Column(name = "attempts")
  private Integer attempts;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "unlock_time")
  private Date unlockTime;

  protected LockedCert() {
  }

  public LockedCert(Employee employee, Long certSerialNumber, Integer attempts, Date unlockTime) {
    this.employee = employee;
    this.certSerialNumber = certSerialNumber;
    this.attempts = attempts;
    this.unlockTime = unlockTime;
  }

  public Employee getEmployee() {
    return employee;
  }

  public Long getCertSerialNumber() {
    return certSerialNumber;
  }

  public Integer getAttempts() {
    return attempts;
  }

  public void setAttempts(Integer attempts) {
    this.attempts = attempts;
  }

  public void setUnlockTime(Date unlockTime) {
    this.unlockTime = unlockTime;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    LockedCert that = (LockedCert) o;
    if (certSerialNumber != null ? !certSerialNumber.equals(that.certSerialNumber) : that.certSerialNumber != null) return false;
    if (employee != null ? !employee.getLogin().equals(that.employee.getLogin()) : that.employee.getLogin() != null) return false;
    return true;
  }

  @Override
  public int hashCode() {
    int result = employee != null ? employee.getLogin().hashCode() : 0;
    result = 31 * result + (certSerialNumber != null ? certSerialNumber.hashCode() : 0);
    return result;
  }

  public Date getUnlockTime() {
    return unlockTime;
  }

  final public static class PK {

    final public String employee;
    final public Long certSerialNumber;

    public PK(String employee, Long certSerialNumber) {
      this.employee = employee;
      this.certSerialNumber = certSerialNumber;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      PK pk = (PK) o;
      if (certSerialNumber != null ? !certSerialNumber.equals(pk.certSerialNumber) : pk.certSerialNumber != null) return false;
      if (employee != null ? !employee.equals(pk.employee) : pk.employee != null) return false;
      return true;
    }

    @Override
    public int hashCode() {
      int result = employee != null ? employee.hashCode() : 0;
      result = 31 * result + (certSerialNumber != null ? certSerialNumber.hashCode() : 0);
      return result;
    }
  }

}