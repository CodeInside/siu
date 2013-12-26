/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.actions.deployment;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

public class DeploymentUploadReceiver implements com.vaadin.ui.Upload.Receiver {
	
	private static final long serialVersionUID = -8418204431226821601L;
	public String propertyId = "";
	public String fileName;
	public String mimeType;
	byte [] data;

	public boolean isUsed() {
		return data != null && data.length > 0;
	}
	
	public byte[] getBytes() {		
		return data;
	}
	
	public OutputStream receiveUpload(String filename, String mType) {
		this.fileName = filename;
		String extention = extractExtention(filename);
		if (extention != null)
			this.mimeType = (mType + ";" + extention);
		else {
			this.mimeType = mType;
		}
    return new ByteArrayOutputStream() {
      @Override
      public void close() throws IOException {
        data = Arrays.copyOf(buf, count);
      }
    };
	}

	protected String extractExtention(String fileName) {
		int lastIndex = fileName.lastIndexOf('.');
		if ((lastIndex > 0) && (lastIndex < fileName.length() - 1)) {
			return fileName.substring(lastIndex + 1);
		}
		return null;
	}

}
