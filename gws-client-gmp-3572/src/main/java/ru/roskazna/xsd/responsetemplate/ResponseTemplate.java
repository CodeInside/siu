
/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.roskazna.xsd.responsetemplate;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import ru.roskazna.xsd.doacknowledgmentresponse.DoAcknowledgmentResponseType;
import ru.roskazna.xsd.errinfo.ErrInfo;
import ru.roskazna.xsd.exportincomesresponse.ExportIncomesResponse;
import ru.roskazna.xsd.exportpaymentsresponse.ExportPaymentsResponse;
import ru.roskazna.xsd.exportquittanceresponse.ExportQuittanceResponse;
import ru.roskazna.xsd.pgu_chargesresponse.ExportChargesResponse;
import ru.roskazna.xsd.postblock.PostBlock;
import ru.roskazna.xsd.ticket.Ticket;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResponseTemplate", propOrder = {
    "postBlock",
    "requestProcessResult"
})
@XmlSeeAlso({
    Ticket.class,
    ExportChargesResponse.class,
    ExportPaymentsResponse.class,
    ExportIncomesResponse.class,
    ExportQuittanceResponse.class,
    DoAcknowledgmentResponseType.class
})
public class ResponseTemplate {

    @XmlElement(name = "PostBlock", required = true)
    protected PostBlock postBlock;
    @XmlElement(name = "RequestProcessResult")
    protected ErrInfo requestProcessResult;

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
     * Gets the value of the requestProcessResult property.
     * 
     * @return
     *     possible object is
     *     {@link ErrInfo }
     *     
     */
    public ErrInfo getRequestProcessResult() {
        return requestProcessResult;
    }

    /**
     * Sets the value of the requestProcessResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ErrInfo }
     *     
     */
    public void setRequestProcessResult(ErrInfo value) {
        this.requestProcessResult = value;
    }

}
