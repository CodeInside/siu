package ru.codeinside.gses.activiti.ftarchive.document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TextField;

import ru.codeinside.gses.beans.DirectoryBean;

public class DocumentFormGenerator {

  static class NNN {

    public Node element;

    public String name;
    public String sname;
    public String value;

    public List<NNN> list = new ArrayList<NNN>();

    public static NNN sfill(Node el) {
      NNN n = new NNN();
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

        // attrs
        NamedNodeMap attrs = el.getAttributes();
        if (attrs != null) {
          for (int i = 0; i < attrs.getLength(); i++) {
            Node attr = attrs.item(i);
            list.add(NNN.sfill(attr));
          }
        }

        // childs
        NodeList childs = el.getChildNodes();
        if (childs != null) {
          for (int i = 0; i < childs.getLength(); i++) {
            Node child = childs.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
              list.add(NNN.sfill(child));
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

    Map<String, String> dict = dirBean.getValues(serviceId);

    NNN n = NNN.sfill(document);

    List<NNN> list = new ArrayList<NNN>();
    processDict(dict, list, n);

    FormLayout layout = new FormLayout();

    for (NNN nnn : list) {
      if (nnn.value != null) {
        layout.addComponent(createTextField(nnn.sname, nnn.value));
      } else {
        layout.addComponent(createTextField(nnn.sname, null));
      }
    }

    return layout;
  }

  private TextField createTextField(String caption, Object value) {
    TextField textField = new TextField();
    textField.setCaption(caption);
    if (value == null) {
      textField.setValue("");
    } else {
      textField.setValue(String.valueOf(value));
    }
    textField.setWidth("100%");
    textField.setReadOnly(true);
    return textField;
  }

  private void processDict(Map<String, String> dict, List<NNN> list, NNN n) {
    if (dict.containsKey(n.name)) {
      n.sname = dict.get(n.name);
      list.add(n);
    }

    for (NNN nnn : n.list) {
      processDict(dict, list, nnn);
    }
  }

}
