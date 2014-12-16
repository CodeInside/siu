package m;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class G {

  public static void main(String[] args) throws Exception {
    Map<String, String> dict = initDict();
    String path = "G.xml";

    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder db = dbf.newDocumentBuilder();
    Document doc = db.parse(new File(path));

    //
    Element el = doc.getDocumentElement();

    NNN n = NNN.sfill(el);

    //
    List<NNN> list = new ArrayList<NNN>();
    processDict(dict, list, n);

    for (NNN nnn : list) {
      if (nnn.value != null) {
        System.out.println(nnn.name + " " + nnn.sname + " " + nnn.value);
      } else {
        System.out.println(" === " + nnn.name + " " + nnn.sname + " === ");
      }
    }

  }

  private static void processDict(Map<String, String> dict, List<NNN> list, NNN n) {
    if (dict.containsKey(n.name)) {
      n.sname = dict.get(n.name);
      list.add(n);
    }

    for (NNN nnn : n.list) {
      processDict(dict, list, nnn);
    }
  }

  public static class NNN {

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

      // int c = Thread.currentThread().getStackTrace().length;
      // for (int i = 0; i < c; i++)
      // System.out.print(" ");
      //
      // System.out.println(String.format("name = '%s' value='%s'", name,
      // value));

      // System.out.println(name);
    }

    @Override
    public String toString() {
      return "NNN [element=" + element + ", name=" + name + ", value=" + value + ", list=" + list + "]";
    }

  }

  private static Map<String, String> initDict() throws Exception {

    Map<String, String> dict = new HashMap<String, String>();

    String path = "G.dict";
    String content = FileUtils.readFileToString(new File(path));
    String[] values = StringUtils.split(content, '\n');

    for (int i = 0; i < values.length;) {
      String key = values[i];
      key = key.trim();
      if (key.isEmpty()) {
        i++;
        continue;
      }

      String value = values[i + 1];
      value = value.trim();
      if (value.isEmpty()) {
        i++;
        continue;
      }

      dict.put(key, value);
      i += 2;
    }

    return dict;
  }

}
