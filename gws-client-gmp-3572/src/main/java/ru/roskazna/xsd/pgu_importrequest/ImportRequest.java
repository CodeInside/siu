
/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.roskazna.xsd.pgu_importrequest;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import ru.roskazna.xsd.charge.ChargeType;
import ru.roskazna.xsd.paymentinfo.IncomeInfoType;
import ru.roskazna.xsd.paymentinfo.PaymentInfoType;
import ru.roskazna.xsd.postblock.PostBlock;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ImportRequest", propOrder = {
    "postBlock",
    "charge",
    "finalPayment",
    "income"
})
public class ImportRequest {

    @XmlElement(name = "PostBlock", required = true)
    protected PostBlock postBlock;
    @XmlElement(name = "Charge")
    protected ChargeType charge;
    @XmlElement(name = "FinalPayment")
    protected PaymentInfoType finalPayment;
    @XmlElement(name = "Income")
    protected IncomeInfoType income;

    /**
     * Gets the value of the postBlock property.
     * 
     * @return
     *     possible object is
     *     {@link PostBlock }
     *     
     */
    public PostBlock getPostBlock() {
        return postBlock;
    }

    /**
     * Sets the value of the postBlock property.
     * 
     * @param value
     *     allowed object is
     *     {@link PostBlock }
     *     
     */
    public void setPostBlock(PostBlock value) {
        this.postBlock = value;
    }

    /**
     * Gets the value of the charge property.
     * 
     * @return
     *     possible object is
     *     {@link ChargeType }
     *     
     */
    public ChargeType getCharge() {
        return charge;
    }

    /**
     * Sets the value of the charge property.
     * 
     * @param value
     *     allowed object is
     *     {@link ChargeType }
     *     
     */
    public void setCharge(ChargeType value) {
        this.charge = value;
    }

    /**
     * Gets the value of the finalPayment property.
     * 
     * @return
     *     possible object is
     *     {@link PaymentInfoType }
     *     
     */
    public PaymentInfoType getFinalPayment() {
        return finalPayment;
    }

    /**
     * Sets the value of the finalPayment property.
     * 
     * @param value
     *     allowed object is
     *     {@link PaymentInfoType }
     *     
     */
    public void setFinalPayment(PaymentInfoType value) {
        this.finalPayment = value;
    }

    /**
     * Gets the value of the income property.
     * 
     * @return
     *     possible object is
     *     {@link IncomeInfoType }
     *     
     */
    public IncomeInfoType getIncome() {
        return income;
    }

    /**
     * Sets the value of the income property.
     * 
     * @param value
     *     allowed object is
     *     {@link IncomeInfoType }
     *     
     */
    public void setIncome(IncomeInfoType value) {
        this.income = value;
    }

}
