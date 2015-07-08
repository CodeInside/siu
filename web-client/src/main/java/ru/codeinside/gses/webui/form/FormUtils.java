package ru.codeinside.gses.webui.form;

import com.vaadin.ui.Form;
import ru.codeinside.gses.activiti.ReadOnly;
import ru.codeinside.gses.activiti.SignatureProtocol;
import ru.codeinside.gses.activiti.forms.FormID;


public class FormUtils {
  private FormUtils() {
    throw new UnsupportedOperationException("Static methods only");
  }

  static void addSignedDataToForm(Form form, String signData, String propertyId) {
    final ReadOnly txt = new ReadOnly(signData);
    txt.setCaption("Подписываемые данные");
    txt.addStyleName("light");
    txt.setVisible(false);
    form.addField(propertyId, txt);
  }

  static void addSignatureFieldToForm(Form form, FormID formId, String signData, String fieldId, DataAccumulator dataAccumulator) {
    byte[] signDataBytes = signData != null ? signData.getBytes() : new byte[0];
    boolean[] files = {false};
    String[] ids = {fieldId};

    FormSignatureField signatureField = new FormSignatureField(
        new SignatureProtocol(formId, FormSignatureSeq.SIGNATURE, FormSignatureSeq.SIGNATURE,
            new byte[][]{signDataBytes}, files, ids, form, dataAccumulator));
    signatureField.setCaption(FormSignatureSeq.SIGNATURE);
    signatureField.setRequired(true);

    form.addField(FormSignatureSeq.SIGNATURE, signatureField);
  }
}
