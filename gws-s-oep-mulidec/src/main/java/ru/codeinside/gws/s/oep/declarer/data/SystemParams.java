/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.s.oep.declarer.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SystemParams", propOrder = {
  "appId",
  "orgId",
  "formId",
  "statusTitle",
  "statusDate",
  "statusPgu",
  "statusCode"
})
public class SystemParams {

  /**
   * Номер заявки.
   */
  @XmlElement(name = "app_id")
  protected long appId;

  /**
   * Номер организации.
   */
  @XmlElement(name = "org_id")
  protected String orgId;

  /**
   * Номер формы.
   */
  @XmlElement(name = "form_id")
  protected String formId;

  @XmlElement(name = "status_title")
  protected String statusTitle;

  @XmlElement(name = "status_date", required = true)
  @XmlSchemaType(name = "dateTime")
  protected XMLGregorianCalendar statusDate;

  @XmlElement(name = "status_pgu")
  protected String statusPgu;

  @XmlElement(name = "status_code")
  protected String statusCode;

  public long getAppId() {
    return appId;
  }

  public void setAppId(long value) {
    this.appId = value;
  }

  public String getOrgId() {
    return orgId;
  }

  public void setOrgId(String value) {
    this.orgId = value;
  }

  public String getFormId() {
    return formId;
  }

  public void setFormId(String value) {
    this.formId = value;
  }

  public String getStatusTitle() {
    return statusTitle;
  }

  public void setStatusTitle(String value) {
    this.statusTitle = value;
  }

  public XMLGregorianCalendar getStatusDate() {
    return statusDate;
  }

  public void setStatusDate(XMLGregorianCalendar value) {
    this.statusDate = value;
  }

  public String getStatusPgu() {
    return statusPgu;
  }

  public void setStatusPgu(String value) {
    this.statusPgu = value;
  }

  public String getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(String value) {
    this.statusCode = value;
  }

}
