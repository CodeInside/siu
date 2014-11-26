package ru.codeinside.gses.activiti.ftarchive;

import java.util.logging.Logger;

import net.mobidom.bp.beans.ГлавныиБухгалтер;
import net.mobidom.bp.beans.Обращение;
import net.mobidom.bp.beans.Руководитель;
import net.mobidom.bp.beans.ФизическоеЛицо;
import net.mobidom.bp.beans.ЮридическоеЛицо;
import ru.codeinside.gses.activiti.forms.api.definitions.PropertyNode;
import ru.codeinside.gses.activiti.forms.types.DateType;
import ru.codeinside.gses.activiti.forms.types.FieldType;

import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.Layout;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;

public class RequestFFT implements FieldType<Обращение> {

  private static final long serialVersionUID = 8309095853078973327L;

  private static Logger log = Logger.getLogger(RequestFFT.class.getName());

  @SuppressWarnings("serial")
  @Override
  public Field createField(String taskId, String fieldId, String name, Обращение value, PropertyNode node, boolean archive) {

    if (value == null) {
      throw new RuntimeException("request value is null");
    }

    Form form = new Form() {

      Обращение value;

      @Override
      public void commit() throws SourceException, InvalidValueException {
        // nothing to do
      }

      @Override
      public void setValue(Object newValue) throws com.vaadin.data.Property.ReadOnlyException, com.vaadin.data.Property.ConversionException {
        if (newValue == null) {
          throw new RuntimeException("request value is null");
        }

        if (!(newValue instanceof Обращение)) {
          throw new RuntimeException("request value is not Request instance. it's = " + newValue.getClass());
        }

        this.value = (Обращение) newValue;
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
    createDateField.setValue(value.getДатаПриема());
    layout.addComponent(createDateField);

    layout.addComponent(createTextField("Услуга", value.getУслуга()));
    layout.addComponent(createTextField("ОГВ", value.getОГВ()));

    if (value.getЮридическоеЛицо() != null) {
      ЮридическоеЛицо юридическоеЛицо = value.getЮридическоеЛицо();

      layout.addComponent(createTextField("Полное название", юридическоеЛицо.getПолноеНазвание()));

      if (юридическоеЛицо.getРуководитель() != null) {
        Руководитель руководитель = юридическоеЛицо.getРуководитель();
        layout.addComponent(createTextField("Руководитель", руководитель.toGeneralString()));
      }

      if (юридическоеЛицо.getГлавныйБухгалтер() != null) {
    	ГлавныиБухгалтер главныиБухгалтер = юридическоеЛицо.getГлавныйБухгалтер();
        layout.addComponent(createTextField("Главный Бухгалтер", главныиБухгалтер.toGeneralString()));
      }

      layout.addComponent(createTextField("Почтовый Адрес", юридическоеЛицо.getПочтовыйАдрес().toGeneralString()));
      layout.addComponent(createTextField("Юридический Адрес", юридическоеЛицо.getЮридическийАдрес().toGeneralString()));
      layout.addComponent(createTextField("Номер телефона", юридическоеЛицо.getТелефон().toGeneralString()));

    } else if (value.getФизическоеЛицо() != null) {
      // ФизическоеЛицо
      ФизическоеЛицо declarerValue = value.getФизическоеЛицо();
      layout.addComponent(createTextField("Фамилия", declarerValue.getФио().getФамилия()));
      layout.addComponent(createTextField("Имя", declarerValue.getФио().getИмя()));
      layout.addComponent(createTextField("Отчество", declarerValue.getФио().getОтчество()));

      PopupDateField birthDateField = new PopupDateField();
      birthDateField.setDateFormat(DateType.PATTERN1);
      birthDateField.setCaption("Дата рождения");
      birthDateField.setValue(declarerValue.getДатаРождения());

      layout.addComponent(birthDateField);
      layout.addComponent(createTextField("Адрес регистрации", declarerValue.getАдрес().toGeneralString()));
      layout.addComponent(createTextField("Номер телефона", declarerValue.getТелефон().toGeneralString()));
    }

    form.setValue(value);

    return form;
  }

  TextField createTextField(String caption, String value) {
    TextField textField = new TextField(caption, value);
    return textField;
  }
}
