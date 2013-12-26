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
package org.ow2.easywsdl.wsdl.impl.wsdl20;



/**
 * @author Nicolas Salatge - eBM WebSourcing
 */
public class Constants {

    /**
     * WSDL 2.0 Namespace
     */
    public static final String WSDL_20_NAMESPACE = "http://www.w3.org/ns/wsdl";

    /**
     * WSDL 2.0 package
     */
    public static final String WSDL20_PACKAGE = Constants.class.getPackage().getName();

    /**
     * WSDL 2.0 schema name
     */
    public static final String XSD_WSDL_20 = "schema/wsdl/wsdl20/wsdl20.xsd";

    /**
     * WSDL 2.0 root tag
     */
    public static final String WSDL20_ROOT_TAG = "description";

    /**
     * http method default attribute in http binding
     */
    public static final String HTTP_METHOD_DEFAULT = "methodDefault";

    /**
     * http method attribute in http binding
     */
    public static final String HTTP_METHOD = "method";

    /**
     * soap protocol attribute in soap binding
     */
    public static final String SOAP_PROTOCOL = "protocol";

    /**
     * soap mep attribute in soap binding
     */
    public static final String MEP_ATTRIBUTE = "mep";

    /**
     * Errors
     */
    public static final String NOT_SUPPORTED = "the WSDL 2.0 model does not support this operation";

    /**
     * transport soap/http
     */
    public static final String HTTP_WWW_W3_ORG_2003_05_SOAP_BINDINGS_HTTP = "http://www.w3.org/2003/05/soap/bindings/HTTP/";

}
