/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.core.cproto;

import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

import java.io.InputStream;

final public class W3cResourceResolver implements LSResourceResolver {

	private final String base;

	public W3cResourceResolver(String base) {
		this.base = base;
	}

	public LSInput resolveResource(String type, String ns, String publicId, String systemId, String baseURI) {
		final String resource = base + systemId;
		final InputStream is = getClass().getClassLoader().getResourceAsStream(resource);
		if (is == null) {
			throw new IllegalStateException("invalid " + resource);
		}
		LSInput lsInput = new W3cInput();
		lsInput.setBaseURI(baseURI);
		lsInput.setByteStream(is);
		lsInput.setPublicId(publicId);
		lsInput.setSystemId(systemId);
		return lsInput;
	}
}
