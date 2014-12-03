//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.11.17 at 02:11:53 PM MSK 
//


package com.rstyle.skmv.snils_by_data;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.rstyle.skmv.snils_by_data package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Result_QNAME = new QName("http://snils-by-data.skmv.rstyle.com", "result");
    private final static QName _SnilsByDataRequest_QNAME = new QName("http://snils-by-data.skmv.rstyle.com", "SnilsByDataRequest");
    private final static QName _SnilsByDataResponse_QNAME = new QName("http://snils-by-data.skmv.rstyle.com", "SnilsByDataResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.rstyle.skmv.snils_by_data
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link TWINDATA }
     * 
     */
    public TWINDATA createTWINDATA() {
        return new TWINDATA();
    }

    /**
     * Create an instance of {@link SnilsByDataOut }
     * 
     */
    public SnilsByDataOut createSnilsByDataOut() {
        return new SnilsByDataOut();
    }

    /**
     * Create an instance of {@link SnilsByDataIn }
     * 
     */
    public SnilsByDataIn createSnilsByDataIn() {
        return new SnilsByDataIn();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SnilsByDataOut }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://snils-by-data.skmv.rstyle.com", name = "result")
    public JAXBElement<SnilsByDataOut> createResult(SnilsByDataOut value) {
        return new JAXBElement<SnilsByDataOut>(_Result_QNAME, SnilsByDataOut.class, null, value);
    }

}
