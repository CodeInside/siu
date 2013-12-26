package org.ow2.easywsdl.schema.util;


//import com.sun.xml.internal.bind.marshaller.NamespacePrefixMapper;

public class CustomPrefixMapper {

  private static final String[] EMPTY_STRING = new String[0];

  public String[] getPreDeclaredNamespaceUris() {
    return EMPTY_STRING;
  }

  public String[] getPreDeclaredNamespaceUris2() {
    return EMPTY_STRING;
  }

  public String[] getContextualNamespaceDecls() {
    return EMPTY_STRING;
  }

  public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
    if (namespaceUri.equals("http://www.w3.org/2001/XMLSchema")) {
      return "xs";
    }
    return suggestion;
  }

}
