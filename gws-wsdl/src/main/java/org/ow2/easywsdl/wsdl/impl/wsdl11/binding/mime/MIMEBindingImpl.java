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
package org.ow2.easywsdl.wsdl.impl.wsdl11.binding.mime;

import java.util.ArrayList;
import java.util.List;

import org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfBindingParam;
import org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.mime.ContentType;
import org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.mime.MultipartRelatedType;
import org.ow2.easywsdl.wsdl.org.xmlsoap.schemas.wsdl.mime.TMimeXml;

/**
 * @author Nicolas Salatge - eBM WebSourcing
 */
public class MIMEBindingImpl implements
        org.ow2.easywsdl.wsdl.api.binding.wsdl11.mime.MIMEBinding4Wsdl11 {

    private final List<org.ow2.easywsdl.wsdl.api.binding.wsdl11.mime.MIMEContent> contents = new ArrayList<org.ow2.easywsdl.wsdl.api.binding.wsdl11.mime.MIMEContent>();

    private final List<org.ow2.easywsdl.wsdl.api.binding.wsdl11.mime.MIMEMultipartRelated> multiparts = new ArrayList<org.ow2.easywsdl.wsdl.api.binding.wsdl11.mime.MIMEMultipartRelated>();

    private final List<org.ow2.easywsdl.wsdl.api.binding.wsdl11.mime.MIMEXml> mimeXmls = new ArrayList<org.ow2.easywsdl.wsdl.api.binding.wsdl11.mime.MIMEXml>();

    private AbsItfBindingParam param = null;

    public MIMEBindingImpl(final List<ContentType> contents, final List<TMimeXml> mimeXmls,
            final List<MultipartRelatedType> multiparts, final AbsItfBindingParam param) {
        this.param = param;
        if (contents != null) {
            for (final ContentType content : contents) {
                this.contents.add(new MIMEContentImpl(content));
            }
        }
        if (mimeXmls != null) {
            for (final TMimeXml content : mimeXmls) {
                this.mimeXmls.add(new MIMEXmlImpl(content));
            }
        }
        if (multiparts != null) {
            for (final MultipartRelatedType mpart : multiparts) {
                this.multiparts.add(new MIMEMultipartRelatedImpl(mpart, this.param));
            }
        }
    }

    public List<org.ow2.easywsdl.wsdl.api.binding.wsdl11.mime.MIMEContent> getContents() {
        return this.contents;
    }

    public List<org.ow2.easywsdl.wsdl.api.binding.wsdl11.mime.MIMEMultipartRelated> getMultipartRelateds() {
        return this.multiparts;
    }

    public List<org.ow2.easywsdl.wsdl.api.binding.wsdl11.mime.MIMEXml> getMimeXml() {
        return this.mimeXmls;
    }

}
