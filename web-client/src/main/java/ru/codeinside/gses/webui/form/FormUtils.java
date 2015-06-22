package ru.codeinside.gses.webui.form;

import com.vaadin.ui.Form;
import org.activiti.engine.impl.persistence.entity.ByteArrayEntity;
import ru.codeinside.gses.activiti.ReadOnly;
import ru.codeinside.gses.activiti.SignatureProtocol;
import ru.codeinside.gses.activiti.forms.FormID;
import ru.codeinside.gses.service.Fn;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class FormUtils {
  private FormUtils() {
    throw new UnsupportedOperationException("Static methods only");
  }

  static void addSignedDataToForm(Form form, String signData, String propertyId) {
    final ReadOnly txt = new ReadOnly(signData);
    txt.setCaption("Подписываемые данные");
    txt.addStyleName("light");
    form.addField(propertyId, txt);
  }

  static void addSignatureFieldToForm(Form form, FormID formId, String signData, String fieldId, DataAccumulator dataAccumulator) {
    byte[] signDataBytes = signData.getBytes();
    boolean[] files = {false};
    String[] ids = {fieldId};

    FormSignatureField signatureField = new FormSignatureField(
        new SignatureProtocol(formId, FormSignatureSeq.SIGNATURE, FormSignatureSeq.SIGNATURE,
            new byte[][]{signDataBytes}, files, ids, form, dataAccumulator));
    signatureField.setCaption(FormSignatureSeq.SIGNATURE);
    signatureField.setRequired(true);

    form.addField(FormSignatureSeq.SIGNATURE, signatureField);
  }

  static String persistSoapMessage(SOAPMessage soapMessage) {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    try {
      soapMessage.writeTo(out);
    } catch (SOAPException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    ByteArrayEntity content = new ByteArrayEntity();
    content.setBytes(out.toByteArray());

    boolean isOk = Fn.withEngine(new ProtocolUtils.SaveByteArrayEntity(), content);

    if (isOk) {
      return content.getId();
    } else {
      throw new IllegalStateException("Не удалось сохранить подготовленное SOAP сообщение");
    }
  }
}
