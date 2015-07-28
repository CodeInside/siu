package ru.codeinside.gses.webui.form;

import com.vaadin.ui.Form;
import ru.codeinside.gses.activiti.FileValue;
import ru.codeinside.gses.activiti.ReadOnly;
import ru.codeinside.gses.activiti.SignatureProtocol;
import ru.codeinside.gses.activiti.VariableToBytes;
import ru.codeinside.gses.activiti.forms.FormID;
import ru.codeinside.gws.api.Enclosure;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class FormUtils {
  private FormUtils() {
    throw new UnsupportedOperationException("Static methods only");
  }

  static void addSignedDataToForm(Form form, SignData signData, String propertyId) {
    byte[] signedData = signData.getData() != null ? signData.getData() : new byte[0];
    final ReadOnly txt = new ReadOnly(new String(signedData, Charset.forName("UTF-8")));
    txt.setCaption("Подписываемые данные");
    txt.addStyleName("light");
    txt.setVisible(false);
    form.addField(propertyId, txt);

    for (Enclosure e : getEnclosuresWithoutSign(signData.getEnclosures())) {
      String fieldId = e.code != null && !e.code.isEmpty() ? e.code : e.fileName;
      ReadOnly field = new ReadOnly(fieldId);
        field.setVisible(false);
        form.addField(fieldId, field);
    }
  }

  static void addSignatureFieldToForm(
          Form form, FormID formId, SignData signData, List<FormField> previousFields, String fieldId, DataAccumulator dataAccumulator) {
    List<Enclosure> enclosures = getEnclosuresWithoutSign(signData.getEnclosures());
    List<FormField> attachmentsFromForm = getAttachmentsFromForm(previousFields);
    int enclosureSize = enclosures.size();
    int formAttachmentsSize = attachmentsFromForm.size();
    boolean[] files = new boolean[enclosureSize + formAttachmentsSize + 1];
    files[0] = false;

    String[] ids = new String[enclosureSize + formAttachmentsSize + 1];
    ids[0] = fieldId;

    byte[][] blocks = new byte[enclosureSize +formAttachmentsSize + 1][];
    blocks[0] = signData.getData() != null ? signData.getData() : new byte[0];

    for (int i = 0; i < enclosureSize; ++i) {
      Enclosure e = enclosures.get(i);

      files[i + 1] = true;
      ids[i + 1] = e.code != null && !e.code.isEmpty() ? e.code : e.fileName;
      blocks[i + 1] = e.content;
    }

    for (int i = 0; i < formAttachmentsSize; ++i) {
      FormField field = attachmentsFromForm.get(i);

      files[i + enclosureSize + 1] = true;
      ids[i + enclosureSize + 1] = field.getPropId();
      blocks[i + enclosureSize + 1] = VariableToBytes.toBytes(field.getValue());
    }

    FormSignatureField signatureField = new FormSignatureField(new SignatureProtocol(formId, FormSignatureSeq.SIGNATURE,
            FormSignatureSeq.SIGNATURE, blocks, files, ids, form, dataAccumulator));
    signatureField.setCaption(FormSignatureSeq.SIGNATURE);
    signatureField.setRequired(true);

    form.addField(FormSignatureSeq.SIGNATURE, signatureField);
  }

  private static List<FormField> getAttachmentsFromForm(List<FormField> previousFields) {
    if (previousFields != null) {
      List<FormField> result = new LinkedList<FormField>();
      for (FormField field : previousFields) {
        if (field.getValue() instanceof FileValue) {
          result.add(field);
        }
      }
      return result;
    }
    return Collections.emptyList();
  }

  private static List<Enclosure> getEnclosuresWithoutSign(List<Enclosure> enclosures) {
    List<Enclosure> withoutSign = new LinkedList<Enclosure>();
    for (Enclosure e : enclosures) {
      if (e.signature == null) {
        withoutSign.add(e);
      }
    }
    return withoutSign;
  }
}
