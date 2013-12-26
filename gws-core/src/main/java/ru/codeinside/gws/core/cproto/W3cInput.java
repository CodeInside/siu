/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.core.cproto;

import org.w3c.dom.ls.LSInput;

import java.io.InputStream;
import java.io.Reader;

final public class W3cInput implements LSInput {

	protected String fPublicId = null;
	protected String fSystemId = null;
	protected String fBaseSystemId = null;
	protected InputStream fByteStream = null;
	protected Reader fCharStream = null;
	protected String fData = null;
	protected String fEncoding = null;
	protected boolean fCertifiedText = false;

	public InputStream getByteStream() {
		return fByteStream;
	}

	public void setByteStream(InputStream byteStream) {
		fByteStream = byteStream;
	}

	public Reader getCharacterStream() {
		return fCharStream;
	}

	public void setCharacterStream(Reader characterStream) {
		fCharStream = characterStream;
	}

	public String getStringData() {
		return fData;
	}

	public void setStringData(String stringData) {
		fData = stringData;
	}

	public String getEncoding() {
		return fEncoding;
	}

	public void setEncoding(String encoding) {
		fEncoding = encoding;
	}

	public String getPublicId() {
		return fPublicId;
	}

	public void setPublicId(String publicId) {
		fPublicId = publicId;
	}

	public String getSystemId() {
		return fSystemId;
	}

	public void setSystemId(String systemId) {
		fSystemId = systemId;
	}

	public String getBaseURI() {
		return fBaseSystemId;
	}

	public void setBaseURI(String baseURI) {
		fBaseSystemId = baseURI;
	}

	public boolean getCertifiedText() {
		return fCertifiedText;
	}

	public void setCertifiedText(boolean certifiedText) {
		fCertifiedText = certifiedText;
	}
}
