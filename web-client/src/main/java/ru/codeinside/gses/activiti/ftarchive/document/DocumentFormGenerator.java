package ru.codeinside.gses.activiti.ftarchive.document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ru.codeinside.gses.beans.DirectoryBean;

import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;

public class DocumentFormGenerator {

  static class DocumentElement {

    public Node element;

    public String name;
    public String sname;
    public String value;

    public List<DocumentElement> list = new ArrayList<DocumentElement>();

    public static DocumentElement buildElement(Node el) {
      DocumentElement n = new DocumentElement();
      n.fill(el);
      return n;
    }

    public void fill(Node el) {

      // System.out.println("process " + el.toString());
      element = el;
      name = el.getNodeName();

      short type = el.getNodeType();

      if (type == Node.ATTRIBUTE_NODE) {
        name = el.getNodeName();
        value = el.getNodeValue();
      }

      if (type == Node.ELEMENT_NODE) {
        name = el.getLocalName();

        // attrs
        NamedNodeMap attrs = el.getAttributes();
        if (attrs != null) {
          for (int i = 0; i < attrs.getLength(); i++) {
            Node attr = attrs.item(i);
            list.add(DocumentElement.buildElement(attr));
          }
        }

        // childs
        NodeList childs = el.getChildNodes();
        if (childs != null) {
          for (int i = 0; i < childs.getLength(); i++) {
            Node child = childs.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
              list.add(DocumentElement.buildElement(child));
            } else if (child.getNodeType() == Node.TEXT_NODE && childs.getLength() == 1) {
              value = child.getTextContent();
            }
          }
        }

      }
    }

    public String toString() {
      return "NNN [element=" + element + ", name=" + name + ", value=" + value + ", list=" + list + "]";
    }

  }

  private DirectoryBean dirBean;
  private String serviceId;
  private Element document;

  public DocumentFormGenerator(DirectoryBean dirBean, String serviceId, Element document) {
    this.dirBean = dirBean;
    this.serviceId = serviceId;
    this.document = document;
  }

  public Layout generateUI() {
    Map<String, String> dict = null;
    if (serviceId != null) {
      dict = dirBean.getValues(serviceId);
    } else {
      dict = new HashMap<String, String>();
    }

    DocumentElement n = DocumentElement.buildElement(document);

    List<DocumentElement> list = new ArrayList<DocumentElement>();
    processDict(dict, list, n);

    FormLayout layout = new FormLayout();
    layout.setMargin(true);
    layout.setSpacing(true);

    for (DocumentElement nnn : list) {
      if (nnn.value != null) {
        layout.addComponent(createTextField(nnn.sname, nnn.value));
      } else {
        layout.addComponent(createTextField(nnn.sname, null));
      }
    }

    return layout;
  }

  private Label createTextField(String caption, Object value) {
    Label textField = new Label();
    textField.setCaption(caption);
    textField.setValue(String.valueOf(value));
    textField.setWidth("100%");
    textField.setReadOnly(true);
    return textField;
  }

  private void processDict(Map<String, String> dict, List<DocumentElement> list, DocumentElement n) {
    if (dict.containsKey(n.name)) {
      n.sname = dict.get(n.name);
      list.add(n);
    }

    for (DocumentElement nnn : n.list) {
      processDict(dict, list, nnn);
    }
  }

}
