package net.mobidom.bp.beans.form;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import net.mobidom.bp.beans.request.DocumentRequest;

import com.vaadin.ui.Field;
import com.vaadin.ui.Form;

public class DocumentRequestForm {

  static Logger log = Logger.getLogger(DocumentRequestForm.class.getName());

  public DocumentRequest documentRequest;
  public Form form;
  public List<PropertyFieldDescriptor<?>> propertyDescriptors;
  public boolean readonly;

  public DocumentRequestForm(DocumentRequest documentRequest, List<PropertyFieldDescriptor<?>> propertyDescriptors, boolean readonly) {
    this.documentRequest = documentRequest;
    this.propertyDescriptors = propertyDescriptors;
    this.readonly = readonly;
    this.form = new Form();
    for (PropertyFieldDescriptor<?> descriptor : propertyDescriptors) {
      Field field = descriptor.getField();
      field.setReadOnly(readonly);
      field.setRequired(!readonly);
      form.addField(descriptor.getId(), field);
      if (!readonly) {
        field.setRequiredError(String.format("Не заполнено обязательное поле: '%s'", descriptor.caption));
      }
    }

    if (!readonly) {
      form.setValidationVisibleOnCommit(true);
    }
  }

  public void setValues(Map<String, Object> params) {
    for (Entry<String, Object> param : params.entrySet()) {
      PropertyFieldDescriptor<?> pfd = getPropertyFieldDescriptorById(param.getKey());
      if (pfd != null) {
        if (pfd instanceof BeanItemSelectPropertyFieldDescriptor<?>) {
          if (((BeanItemSelectPropertyFieldDescriptor<?>) pfd).type == IdCaptionItem.class) {
            IdCaptionItem item = new IdCaptionItem(param.getValue(), null);
            pfd.setValue(item);
          }
        } else {
          pfd.setValue(param.getValue());
        }
      }
    }
  }

  private PropertyFieldDescriptor<?> getPropertyFieldDescriptorById(String id) {

    for (PropertyFieldDescriptor<?> pfd : propertyDescriptors) {
      if (pfd.getId().equals(id)) {
        return pfd;
      }
    }

    return null;
  }

  public boolean accept() {

    try {
      form.commit();
    } catch (Throwable e) {
      return false;
    }

    log.info("accept form values");

    Map<String, Object> params = new HashMap<String, Object>();
    for (PropertyFieldDescriptor<?> descriptor : propertyDescriptors) {
      params.put(descriptor.id, descriptor.getValue());
      log.info(String.format("%s = '%s'", descriptor.id, descriptor.getValue()));
    }

    documentRequest.setRequestParams(params);

    log.info("=====================================");

    return true;
  }
}
