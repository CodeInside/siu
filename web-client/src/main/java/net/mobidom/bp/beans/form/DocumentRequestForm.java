package net.mobidom.bp.beans.form;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import net.mobidom.bp.beans.request.DocumentRequest;

import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Layout;

public class DocumentRequestForm {

  static Logger log = Logger.getLogger(DocumentRequestForm.class.getName());

  public DocumentRequest documentRequest;
  public Layout formLayout;
  public List<PropertyFieldDescriptor<?>> propertyDescriptors;

  public DocumentRequestForm(DocumentRequest documentRequest, List<PropertyFieldDescriptor<?>> propertyDescriptors) {
    this.documentRequest = documentRequest;
    this.propertyDescriptors = propertyDescriptors;
    this.formLayout = new FormLayout();
    for (PropertyFieldDescriptor<?> descriptor : propertyDescriptors) {
      formLayout.addComponent(descriptor.getField());
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

  public void accept() {

    log.info("accept form values");

    Map<String, Object> params = new HashMap<String, Object>();
    for (PropertyFieldDescriptor<?> descriptor : propertyDescriptors) {
      params.put(descriptor.id, descriptor.getValue());
      log.info(String.format("%s = '%s'", descriptor.id, descriptor.getValue()));
    }

    documentRequest.setRequestParams(params);

    log.info("=====================================");

  }
}
