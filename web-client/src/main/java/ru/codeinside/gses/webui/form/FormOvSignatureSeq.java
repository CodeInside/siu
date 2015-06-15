/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2015, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.form;

import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.VerticalLayout;
import ru.codeinside.gses.activiti.forms.FormID;
import ru.codeinside.gses.activiti.forms.Signatures;
import ru.codeinside.gses.webui.form.api.FieldSignatureSource;
import ru.codeinside.gses.webui.wizard.TransitionAction;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class FormOvSignatureSeq extends AbstractFormSeq {

  public static final String OV_SIGN = "SoapBodySignatureField";
  public static final String SIGNED_DATA_ID = "SignedSoapBody";
  public static final String SOAP_MESSAGE = "SignedSoapMessage";
  public static final String REQUEST_ID = "ClientRequestEntityId";

  private Form form;

  public FormOvSignatureSeq(DataAccumulator dataAccumulator) {
    super(dataAccumulator);
  }

  @Override
  public String getCaption() {
    return "Подписание тела запроса подписью ОВ";
  }

  /**
   * Заполненные поля в порядке заполнения.
   */
  @Override
  public List<FormField> getFormFields() {
    List<FormField> fields = new ArrayList<FormField>();
    if (form != null) {
      Collection<?> propertyIds = form.getItemPropertyIds();
      for (Object propertyId : propertyIds) {
        Field field = form.getField(propertyId);
        fields.add(new BasicFormField(propertyId, field.getCaption(), field.getValue()));
      }
    }
    return Collections.unmodifiableList(fields);
  }

  /**
   * Создание формы на основе предыдущей.
   *
   * @param formId
   * @param previous
   */
  @Override
  public Form getForm(FormID formId, FormSeq previous) {
    form = new OvSignatureForm(dataAccumulator.getSoapMessage().get(0), dataAccumulator.getRequestId());

    byte[] signDataBytes = (byte[]) resultTransition.getData();
    String signData = new String(signDataBytes, Charset.forName("UTF-8"));

    FormSeqUtils.addSignedDataToForm(form, signData, SIGNED_DATA_ID);
    FormSeqUtils.addSignatureFieldToForm(form, formId, signData, OV_SIGN, dataAccumulator);

    return form;
  }

  @Override
  public TransitionAction getTransitionAction(List<FormField> formFields) {
    if (dataAccumulator.getClientRequest() == null) {
      dataAccumulator.setFormFields(formFields);
    }
    if (dataAccumulator.getServiceName() != null) {
      return new CreateSoapMessageAction(dataAccumulator);
    } else if (dataAccumulator.getRequestType() != null) {
      return new CreateResultSoapMessageAction(dataAccumulator);
    } else {
      throw new IllegalStateException("Ошибка в маршруте");
    }
  }

  final public static class OvSignatureForm extends Form implements FieldSignatureSource {

    private SOAPMessage soapMessage;
    private List<Long> requestId;// List нужен для того, что бы requestId был mutable. Там всегда один элемент

    public OvSignatureForm(SOAPMessage soapMessage, List<Long> requestId) {
      this.setDescription("Электронная подпись предназначена для идентификации лица, " +
          "подписавшего электронный документ и является полноценной заменой (аналогом) " +
          "собственноручной подписи в случаях, предусмотренных Гражданским кодексом Российской Федерации " +
          "(часть 1, глава 9, статья 160)");
      this.soapMessage = soapMessage;
      this.requestId = requestId;
    }

    public String getSoapMessage() {
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      try {
        soapMessage.writeTo(out);
      } catch (SOAPException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }
      return new String(out.toByteArray());
    }

    public Long getRequestId() {
      return requestId.get(0);
    }

    @Override
    public String getSignedData() {
      Field f = getField(SIGNED_DATA_ID);
      return (String) f.getValue();
    }

    @Override
    public Signatures getSignatures() {
      Field field = getField(FormSignatureSeq.SIGNATURE);
      Object value = field.getValue();
      return value instanceof Signatures ? (Signatures) value : null;
    }

    @Override
    public void attach() {
      super.attach();
      VerticalLayout vl = (VerticalLayout) getParent();
      vl.setWidth(100, UNITS_PERCENTAGE);
      vl.setHeight(-1, UNITS_PIXELS);
    }
  }
}
