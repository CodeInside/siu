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
package org.ow2.easywsdl.schema.util;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * @author alouis - eBM WebSourcing
 * @author ofabre - eBM WebSourcing
 *
 */
public class XMLPrettyPrinter {

	private static String transformerFactoryClass = "";

	/**
	 * <p>
	 * A thread safe TransformerFactory, based on 
	 * com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl
	 * </p>
	 */
	public final static ThreadLocal<TransformerFactory> transformerFactoryThreadLocal = new ThreadLocal<TransformerFactory>() {

		@Override
		protected TransformerFactory initialValue() {
			try {
				
				if (System.getProperty("java.vendor").indexOf("Sun") != -1){
					transformerFactoryClass = "com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl";
				}else if (System.getProperty("java.vendor").indexOf("IBM") != -1){
					transformerFactoryClass = "org.apache.xalan.processor.TransformerFactoryImpl";
				}
				
				Thread.currentThread().getContextClassLoader().loadClass(transformerFactoryClass);
				System.setProperty("javax.xml.transform.TransformerFactory", transformerFactoryClass);
			} catch (ClassNotFoundException e) {
				System.err.println("Warning. EasyXML : TransformerFactory '"+transformerFactoryClass+"' not found, the standard TransformerFactory will be used.");
			}
			return TransformerFactory.newInstance();
		}
	};
	
	
	/**
     * parse the xml String and return it pretty-printed (with correct
     * indentations, etc..)
     * 
     * @param xmlDocument
     *            the xml document to pretty print. Must be non null
     * @param encoding
     *            the encoding to use
     *   
     * @returnpretty printed string if no error occurs. If an error occurs,
     *               return an empty String
     */
    public static String prettyPrint(final Document xmlDocument,String encoding) {
        String result = "";
        try {
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            XMLPrettyPrinter.prettify(xmlDocument, outStream,encoding);
            result = outStream.toString(encoding);
        } catch (final Exception e) {
            System.err.println("write_dom failed:" + e);
            // if an error occurs, the result will be the original string
        }
        return result;

    }

    /**
     * parse the xml Document and return it pretty-printed (with correct
     * indentations, etc..).
     * Use the encoding defined at the parsing or in the document 
     * (utf8 is used if no encoding is defined)
     * @param xmlDocument
     *            the xml document to pretty print. Must be non null
     * @returnpretty printed string if no error occurs. If an error occurs,
     *               return an empty String
     */
    public static String prettyPrint(final Document xmlDocument) {
    	
        return prettyPrint(xmlDocument,getEncoding(xmlDocument));
    }

    
    /**
     * Prettify the node into the output stream.
     *
     * @param node
     * @param out
     * @throws Exception
     */
    public static void prettify(Node node, OutputStream out,String encoding) throws Exception {
        Source source = new DOMSource(node);
        Source stylesheetSource = getStyleSheetSource();

        TransformerFactory tf = transformerFactoryThreadLocal.get();
        Templates templates = tf.newTemplates(stylesheetSource);
        Transformer transformer = templates.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING,encoding);
        transformer.transform(source, new StreamResult(out));
    }

    /**
     * Return the encoding of the document.
     * @param xmlDocument
     * @return InputEncoding or the XmlEncoding of the document, UTF-8 if not found
     */
    public static String getEncoding(Document xmlDocument){
    	String encoding = xmlDocument.getInputEncoding();
    	if(encoding == null){
    		encoding = xmlDocument.getXmlEncoding();
    	}
    	if(encoding == null){
    		encoding = "UTF-8";
    	}
    	return encoding;
    }
    
    private static Source getStyleSheetSource() {
        return new StreamSource(XMLPrettyPrinter.class.getResourceAsStream("/util/prettyPrint.xsl"));
    }
}
