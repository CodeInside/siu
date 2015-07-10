package ru.codeinside.gses.webui.form;

import com.vaadin.ui.Form;
import ru.codeinside.gses.activiti.ReadOnly;
import ru.codeinside.gses.activiti.SignatureProtocol;
import ru.codeinside.gses.activiti.forms.FormID;
import ru.codeinside.gws.api.Enclosure;

import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;


public class FormUtils {
  private FormUtils() {
    throw new UnsupportedOperationException("Static methods only");
  }

  static void addSignedDataToForm(Form form, SignData signData, String propertyId) {
    final ReadOnly txt = new ReadOnly(new String(signData.getData(), Charset.forName("UTF-8")));
    txt.setCaption("Подписываемые данные");
    txt.addStyleName("light");
    txt.setVisible(false);
    form.addField(propertyId, txt);

    for (Enclosure e : getEnclosuresWithoutSign(signData.getEnclosures())) {
        ReadOnly field = new ReadOnly(e.fileName);
        field.setVisible(false);
        form.addField(e.fileName, field);
    }
  }

  static void addSignatureFieldToForm(
          Form form, FormID formId, SignData signData, String fieldId, DataAccumulator dataAccumulator) {
    List<Enclosure> enclosures = getEnclosuresWithoutSign(signData.getEnclosures());
    int enclosureSize = enclosures.size();
    boolean[] files = new boolean[enclosureSize + 1];
    files[0] = false;

    String[] ids = new String[enclosureSize + 1];
    ids[0] = fieldId;

    byte[][] blocks = new byte[enclosureSize + 1][];
    blocks[0] = signData.getData();

    for (int i = 0; i < enclosureSize; ++i) {
      Enclosure enclosure = enclosures.get(i);

      files[i + 1] = true;
      ids[i + 1] = enclosure.fileName;
      blocks[i + 1] = enclosure.content;
    }

    FormSignatureField signatureField = new FormSignatureField(new SignatureProtocol(formId, FormSignatureSeq.SIGNATURE,
            FormSignatureSeq.SIGNATURE, blocks, files, ids, form, dataAccumulator));
    signatureField.setCaption(FormSignatureSeq.SIGNATURE);
    signatureField.setRequired(true);

    form.addField(FormSignatureSeq.SIGNATURE, signatureField);
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
