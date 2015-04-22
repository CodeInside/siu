package ru.codeinside.gws.xml.normalizer.normalize.utils;

public class Constants {
    /**
     * The namespace of the
     * <A HREF="http://www.w3.org/TR/2001/CR-xmldsig-core-20010419/">XML Signature specification</A>
     */
    public static final String SignatureSpecNS = "http://www.w3.org/2000/09/xmldsig#";

    /**
     * The URI for XMLNS spec
     */
    public static final String NamespaceSpecNS = "http://www.w3.org/2000/xmlns/";

    public static final String EncryptionSpecNS =
            "http://www.w3.org/2001/04/xmlenc#";

    /**
     * The URI for XML spec
     */
    public static final String XML_LANG_SPACE_SpecNS = "http://www.w3.org/XML/1998/namespace";

    /**
     * The URL defined in XML-SEC Rec for exclusive c14n <b>without</b> comments.
     */
    public static final String ALGO_ID_C14N_EXCL_OMIT_COMMENTS =
            "http://www.w3.org/2001/10/xml-exc-c14n#";

    /**
     * Field exceptionMessagesResourceBundleDir
     */
    public static final String exceptionMessagesResourceBundleDir =
            "org/apache/xml/security/resource";

    /**
     * Field exceptionMessagesResourceBundleBase is the location of the <CODE>ResourceBundle</CODE>
     */
    public static final String exceptionMessagesResourceBundleBase =
            exceptionMessagesResourceBundleDir + "/" + "xmlsecurity";
}
