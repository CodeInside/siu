/**
 * easySchema - easyWSDL toolbox Platform.
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
package org.ow2.easywsdl.schema.api.extensions;

import javax.xml.XMLConstants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


/**
 * @author Nicolas Salatge - eBM WebSourcing
 */
//com.sun.xml.internal.bind.marshaller.NamespacePrefixMapper
public class NamespaceMapperImpl extends com.sun.xml.bind.marshaller.NamespacePrefixMapper {

  /**
   * Map: key = prefix - value = namespaceUri
   */
  public Map<String, String> ns = new HashMap<String, String>();

  public Map<String, String> getNamespaces() {
    return ns;
  }

  public NamespaceMapperImpl() {
    super();
    addNamespace(XMLConstants.XML_NS_PREFIX, XMLConstants.XML_NS_URI);
  }

  // TODO: This constructor should be replaced by a constructor using a Map<prefix, uri>
  public NamespaceMapperImpl(String[] initialNamespaces) {
    this();
    try {

      for (int i = 0; i < initialNamespaces.length; i++) {

        String prefix = initialNamespaces[i++];
        // TODO: check array size to prevent ArrayIndexOutOfBoundsException
        String namespace = initialNamespaces[i];

        this.ns.put(namespace, prefix);
      }
    } catch (Exception e) {
      System.out
        .println("Error while initialising custom namespaces. Using default namespaces.");
      this.ns.clear();
    }

  }

  public void addNamespace(final String prefix, final String namespaceUri) {
    if (!"".equals(prefix)) {
      this.ns.put(prefix, namespaceUri);
    }
  }

  public String getNamespaceURI(final String prefix) {
    return this.ns.get(prefix);
  }

  public String[] getPreDeclaredNamespaceUris() {

    String[] custNS = new String[this.ns.size() * 2];

    Iterator<String> it = this.ns.keySet().iterator();

    int i = 0;
    while (it.hasNext()) {
      String ns = it.next();
      String prefix = this.ns.get(ns);

      custNS[i++] = prefix;
      custNS[i++] = ns;
    }
    return custNS;
  }

  public String getPreferredPrefix(String namespaceUri, String suggestion,
                                   boolean requirePrefix) {

    String res = getPrefix(namespaceUri);
    if (res != null) {
      return res;
    }

    return suggestion;
  }

  // from javax.xml.namespace.NamespaceContext
  public String getPrefix(final String namespaceURI) {
    String res = null;
    for (final Entry<String, String> entry : this.ns.entrySet()) {
      if (entry.getValue().equals(namespaceURI)) {
        res = entry.getKey();
        break;
      }
    }

    return res;
  }

  public Iterator<String> getPrefixes(final String namespaceURI) {
    final List<String> res = new ArrayList<String>();
    for (final Entry<String, String> entry : this.ns.entrySet()) {
      if (entry.getValue().equals(namespaceURI)) {
        res.add(entry.getKey());
      }
    }
    return res.iterator();
  }

  public String toString() {
    final StringBuffer res = new StringBuffer();
    for (final Entry<String, String> entry : this.ns.entrySet()) {
      res.append("xmlns:");
      res.append(entry.getKey());
      res.append("=");
      res.append(entry.getValue());
      res.append(" \n");
    }
    return res.toString();
  }
}
