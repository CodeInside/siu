package ru.codeinside.gses.activiti.ftarchive;

import java.util.logging.Logger;

import net.mobidom.bp.beans.Declarer;
import net.mobidom.bp.beans.Document;
import net.mobidom.bp.beans.Request;
import ru.codeinside.gses.activiti.forms.api.definitions.PropertyNode;
import ru.codeinside.gses.activiti.forms.types.DateType;
import ru.codeinside.gses.activiti.forms.types.FieldType;

import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.Button;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Button.ClickEvent;

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
    layout.addComponent(createTextField("Орган", value.getServiceName()));

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

    // documents
    Table documentsTable = new Table("Документы");
    documentsTable.setWidth("300px");
    // тип документа
    documentsTable.addContainerProperty("Документ", Label.class, null);
    // скачать
    documentsTable.addContainerProperty("Просмотр", Button.class, null);

    log.info("build table for documents");

    for (int i = 0; i < value.getDocuments().size(); i++) {

      Document document = value.getDocuments().get(i);
      Label label = new Label(document.getType());
      Button showButton = new Button("Просмотр");
      showButton.setData(document);
      showButton.addListener(new Button.ClickListener() {

        @Override
        public void buttonClick(ClickEvent event) {
          Document document = (Document) event.getButton().getData();

          log.info("user want to download " + document.getType());
          log.info("xml = " + (document.getXmlContent() != null) + ", doc = "
              + (document.getBinaryContent() != null ? document.getBinaryContent().getMimeType() : "null"));
        }
      });

      documentsTable.addItem(new Object[] { label, showButton }, new Integer(i));
    }

    layout.addComponent(documentsTable);

    form.setValue(value);

    return form;
  }

  TextField createTextField(String caption, String value) {
    TextField textField = new TextField(caption, value);
    return textField;
  }
}
