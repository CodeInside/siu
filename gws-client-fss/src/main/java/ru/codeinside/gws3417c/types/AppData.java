/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws3417c.types;

import ru.codeinside.gws3417c.types.data.Request;
import ru.codeinside.gws3417c.types.data.Response;
import ru.codeinside.gws3417c.types.data.ResponseType;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "AppData")
@XmlType(name = "appData")
public class AppData {

  @XmlElement(name = "запрос")
  protected Request запрос;
  @XmlElement(name = "типОтвета")
  protected ResponseType типОтвета;
  @XmlElement(name = "примечание")
  protected String примечание;
  @XmlElement(name = "ответ")
  protected Response ответ;

  /**
   * Gets the value of the запрос property.
   * 
   * @return possible object is {@link Request }
   * 
   */
  public Request getЗапрос() {
    return запрос;
  }

  /**
   * Sets the value of the запрос property.
   * 
   * @param value
   *          allowed object is {@link Request }
   * 
   */
  public void setЗапрос(Request value) {
    this.запрос = value;
  }

  /**
   * Gets the value of the типОтвета property.
   * 
   * @return possible object is {@link ResponseType }
   * 
   */
  public ResponseType getТипОтвета() {
    return типОтвета;
  }

  /**
   * Sets the value of the типОтвета property.
   * 
   * @param value
   *          allowed object is {@link ResponseType }
   * 
   */
  public void setТипОтвета(ResponseType value) {
    this.типОтвета = value;
  }

  /**
   * Gets the value of the примечание property.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getПримечание() {
    return примечание;
  }

  /**
   * Sets the value of the примечание property.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  public void setПримечание(String value) {
    this.примечание = value;
  }

  /**
   * Gets the value of the ответ property.
   * 
   * @return possible object is {@link Response }
   * 
   */
  public Response getОтвет() {
    return ответ;
  }

  /**
   * Sets the value of the ответ property.
   * 
   * @param value
   *          allowed object is {@link Response }
   * 
   */
  public void setОтвет(Response value) {
    this.ответ = value;
  }

}
