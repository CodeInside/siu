package ru.codeinside.gses.activiti.ftarchive;

import java.util.logging.Logger;

import net.mobidom.bp.beans.Declarer;
import net.mobidom.bp.beans.Request;
import ru.codeinside.gses.activiti.forms.api.definitions.PropertyNode;
import ru.codeinside.gses.activiti.forms.types.DateType;
import ru.codeinside.gses.activiti.forms.types.FieldType;

import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.Layout;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;

public class RequestFFT implements FieldType<Request> {

  private static final long serialVersionUID = 8309095853078973327L;

  private static Logger log = Logger.getLogger(RequestFFT.class.getName());

  @SuppressWarnings("serial")
  @Override
  public Field createField(String taskId, String fieldId, String name, Request value, PropertyNode node, boolean archive) {

    if (value == null) {
      throw new RuntimeException("request value is null");
    }

    Form form = new Form() {

      Request value;

      @Override
      public void commit() throws SourceException, InvalidValueException {
        // nothing to do
      }

      @Override
      public void setValue(Object newValue) throws com.vaadin.data.Property.ReadOnlyException, com.vaadin.data.Property.ConversionException {
        if (newValue == null) {
          throw new RuntimeException("request value is null");
        }

        if (!(newValue instanceof Request)) {
          throw new RuntimeException("request value is not Request instance. it's = " + newValue.getClass());
        }

        this.value = (Request) newValue;
      };

      @Override
      public Object getValue() {
        return this.value;
      }

    };

    form.setCaption("Заявление");

    Layout layout = form.getLayout();

    PopupDateField createDateField = new PopupDateField();
    createDateField.setDateFormat(DateType.PATTERN1);
    createDateField.setCaption("Дата подачи");
    createDateField.setValue(value.getCreateTime());
    layout.addComponent(createDateField);

    layout.addComponent(createTextField("Услуга", value.getServiceName()));
    layout.addComponent(createTextField("Орган", value.getGovernmentAgencyName()));

    // declarerinfo
    Declarer declarerValue = value.getDeclarer();
    layout.addComponent(createTextField("Фамилия", declarerValue.getSurname()));
    layout.addComponent(createTextField("Имя", declarerValue.getSurname()));
    layout.addComponent(createTextField("Отчество", declarerValue.getSurname()));

    PopupDateField birthDateField = new PopupDateField();
    birthDateField.setDateFormat(DateType.PATTERN1);
    birthDateField.setCaption("Дата рождения");
    birthDateField.setValue(declarerValue.getBirthDate());
    layout.addComponent(birthDateField);
    layout.addComponent(createTextField("Адрес регистрации", declarerValue.getRegistrationAddress()));
    layout.addComponent(createTextField("Номер телефона", declarerValue.getPhoneNumber()));
    layout.addComponent(createTextField("Эл.почта", declarerValue.getEmail()));

    form.setValue(value);

    return form;
  }

//  private File createTempFile(Document doc) {
//    String suffix = null;
//
//    String mime = doc.getBinaryContent().getMimeType();
//    if (StringUtils.equalsIgnoreCase("application/pdf", mime)) {
//      suffix = ".pdf";
//    }
//
//    try {
//      File tmpFile = File.createTempFile(doc.getType(), suffix);
//      FileOutputStream outputStream = new FileOutputStream(tmpFile);
//      outputStream.write(doc.getBinaryContent().getBinaryData());
//      outputStream.close();
//      return tmpFile;
//    } catch (IOException e) {
//      throw new RuntimeException("can't create temp file", e);
//    }
//  }

  TextField createTextField(String caption, String value) {
    TextField textField = new TextField(caption, value);
    return textField;
  }
}
