/**
 * easyWSDL - easyWSDL toolbox Platform.
 * Copyright (c) 2008,  eBM Websourcing
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the University of California, Berkeley nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE REGENTS AND CONTRIBUTORS ``AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE REGENTS AND CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.ow2.easywsdl.wsdl.util;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CustomPrefixMapper extends NamespacePrefixMapper {

  public Map<String, String> predefinedNamespaces = new HashMap<String, String>();

  public CustomPrefixMapper() {
  }

  public CustomPrefixMapper(String[] customNamespaces) {
    try {

      for (int i = 0; i < customNamespaces.length; i++) {

        String prefix = customNamespaces[i++];
        String namespace = customNamespaces[i];

        this.predefinedNamespaces.put(namespace, prefix);
      }
    } catch (Exception e) {
      System.out
        .println("Error while initialising custom namespaces. Using default namespaces.");
      this.predefinedNamespaces.clear();
    }

  }

  @Override
  public String getPreferredPrefix(String namespaceUri, String suggestion,
                                   boolean requirePrefix) {

    if (namespaceUri.equals("http://schemas.xmlsoap.org/wsdl/")) {
      return "w";
    } else if (namespaceUri
      .equals("http://schemas.xmlsoap.org/wsdl/soap12/")) {
      return "soap12";
    } else if (namespaceUri.equals("http://schemas.xmlsoap.org/wsdl/soap/")) {
      return "soap";
    } else if (namespaceUri.equals("http://schemas.xmlsoap.org/wsdl/http/")) {
      return "http";
    } else if (namespaceUri.equals("http://schemas.xmlsoap.org/wsdl/mime/")) {
      return "mime";
    } else if (namespaceUri.equals("http://www.w3.org/2001/XMLSchema")) {
      return "xs";
    } else if (this.predefinedNamespaces.containsKey(namespaceUri)) {
      return this.predefinedNamespaces.get(namespaceUri);
    }

    return suggestion;
  }

  @Override
  public String[] getPreDeclaredNamespaceUris() {

    String[] custNS = new String[this.predefinedNamespaces.size() * 2];

    Iterator<String> it = this.predefinedNamespaces.keySet().iterator();

    int i = 0;
    while (it.hasNext()) {
      String ns = it.next();
      String prefix = this.predefinedNamespaces.get(ns);

      custNS[i++] = prefix;
      custNS[i++] = ns;
    }
    return custNS;
  }

}
