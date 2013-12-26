/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws3417c.types.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "запрос")
@XmlType(name = "запрос")
public class Request {

	@XmlAttribute(required = true)
	protected String regionFrom;
	@XmlAttribute(required = true)
	protected String nameOrganizationFrom;
	@XmlAttribute(required = true)
	protected String iNameCiv;
	@XmlAttribute(required = true)
	protected String fNameCiv;
	@XmlAttribute
	protected String mNameCiv;
	@XmlAttribute(required = true)
	@XmlSchemaType(name = "date")
	protected XMLGregorianCalendar docDatCiv;
	@XmlAttribute(required = true)
	protected String codeKind;
	@XmlAttribute(required = true)
	protected String seriesNumber;
	@XmlAttribute
	protected String inn;
	@XmlAttribute
	protected String snils;
	@XmlAttribute
	protected Curator status;
	@XmlAttribute(required = true)
	protected String iNameKind;
	@XmlAttribute(required = true)
	protected String fNameKind;
	@XmlAttribute
	protected String mNameKind;
	@XmlAttribute(required = true)
	@XmlSchemaType(name = "date")
	protected XMLGregorianCalendar docDatKind;
	@XmlAttribute(required = true)
	protected String sbDoc;
	@XmlAttribute(required = true)
	protected String nbDoc;
	@XmlAttribute(required = true)
	@XmlSchemaType(name = "date")
	protected XMLGregorianCalendar startDate;
	@XmlAttribute(required = true)
	@XmlSchemaType(name = "date")
	protected XMLGregorianCalendar endDate;

	/**
	 * Gets the value of the regionFrom property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getRegionFrom() {
		return regionFrom;
	}

	/**
	 * Sets the value of the regionFrom property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setRegionFrom(String value) {
		this.regionFrom = value;
	}

	/**
	 * Gets the value of the nameOrganizationFrom property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getNameOrganizationFrom() {
		return nameOrganizationFrom;
	}

	/**
	 * Sets the value of the nameOrganizationFrom property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setNameOrganizationFrom(String value) {
		this.nameOrganizationFrom = value;
	}

	/**
	 * Gets the value of the iNameCiv property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getINameCiv() {
		return iNameCiv;
	}

	/**
	 * Sets the value of the iNameCiv property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setINameCiv(String value) {
		this.iNameCiv = value;
	}

	/**
	 * Gets the value of the fNameCiv property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getFNameCiv() {
		return fNameCiv;
	}

	/**
	 * Sets the value of the fNameCiv property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setFNameCiv(String value) {
		this.fNameCiv = value;
	}

	/**
	 * Gets the value of the mNameCiv property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getMNameCiv() {
		return mNameCiv;
	}

	/**
	 * Sets the value of the mNameCiv property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setMNameCiv(String value) {
		this.mNameCiv = value;
	}

	/**
	 * Gets the value of the docDatCiv property.
	 * 
	 * @return possible object is {@link XMLGregorianCalendar }
	 * 
	 */
	public XMLGregorianCalendar getDocDatCiv() {
		return docDatCiv;
	}

	/**
	 * Sets the value of the docDatCiv property.
	 * 
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 * 
	 */
	public void setDocDatCiv(XMLGregorianCalendar value) {
		this.docDatCiv = value;
	}

	/**
	 * Gets the value of the codeKind property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCodeKind() {
		return codeKind;
	}

	/**
	 * Sets the value of the codeKind property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setCodeKind(String value) {
		this.codeKind = value;
	}

	/**
	 * Gets the value of the seriesNumber property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getSeriesNumber() {
		return seriesNumber;
	}

	/**
	 * Sets the value of the seriesNumber property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setSeriesNumber(String value) {
		this.seriesNumber = value;
	}

	/**
	 * Gets the value of the inn property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getInn() {
		return inn;
	}

	/**
	 * Sets the value of the inn property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setInn(String value) {
		this.inn = value;
	}

	/**
	 * Gets the value of the snils property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getSnils() {
		return snils;
	}

	/**
	 * Sets the value of the snils property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setSnils(String value) {
		this.snils = value;
	}

	/**
	 * Gets the value of the status property.
	 * 
	 * @return possible object is {@link Curator }
	 * 
	 */
	public Curator getStatus() {
		return status;
	}

	/**
	 * Sets the value of the status property.
	 * 
	 * @param value
	 *            allowed object is {@link Curator }
	 * 
	 */
	public void setStatus(Curator value) {
		this.status = value;
	}

	/**
	 * Gets the value of the iNameKind property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getINameKind() {
		return iNameKind;
	}

	/**
	 * Sets the value of the iNameKind property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setINameKind(String value) {
		this.iNameKind = value;
	}

	/**
	 * Gets the value of the fNameKind property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getFNameKind() {
		return fNameKind;
	}

	/**
	 * Sets the value of the fNameKind property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setFNameKind(String value) {
		this.fNameKind = value;
	}

	/**
	 * Gets the value of the mNameKind property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getMNameKind() {
		return mNameKind;
	}

	/**
	 * Sets the value of the mNameKind property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setMNameKind(String value) {
		this.mNameKind = value;
	}

	/**
	 * Gets the value of the docDatKind property.
	 * 
	 * @return possible object is {@link XMLGregorianCalendar }
	 * 
	 */
	public XMLGregorianCalendar getDocDatKind() {
		return docDatKind;
	}

	/**
	 * Sets the value of the docDatKind property.
	 * 
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 * 
	 */
	public void setDocDatKind(XMLGregorianCalendar value) {
		this.docDatKind = value;
	}

	/**
	 * Gets the value of the sbDoc property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getSbDoc() {
		return sbDoc;
	}

	/**
	 * Sets the value of the sbDoc property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setSbDoc(String value) {
		this.sbDoc = value;
	}

	/**
	 * Gets the value of the nbDoc property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getNbDoc() {
		return nbDoc;
	}

	/**
	 * Sets the value of the nbDoc property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setNbDoc(String value) {
		this.nbDoc = value;
	}

	/**
	 * Gets the value of the startDate property.
	 * 
	 * @return possible object is {@link XMLGregorianCalendar }
	 * 
	 */
	public XMLGregorianCalendar getStartDate() {
		return startDate;
	}

	/**
	 * Sets the value of the startDate property.
	 * 
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 * 
	 */
	public void setStartDate(XMLGregorianCalendar value) {
		this.startDate = value;
	}

	/**
	 * Gets the value of the endDate property.
	 * 
	 * @return possible object is {@link XMLGregorianCalendar }
	 * 
	 */
	public XMLGregorianCalendar getEndDate() {
		return endDate;
	}

	/**
	 * Sets the value of the endDate property.
	 * 
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 * 
	 */
	public void setEndDate(XMLGregorianCalendar value) {
		this.endDate = value;
	}

}
