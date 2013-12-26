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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.ow2.easywsdl.schema.api.SchemaReader.FeatureConstants;
import org.ow2.easywsdl.wsdl.api.Description;
import org.ow2.easywsdl.wsdl.api.abstractElmt.AbstractDescriptionImpl;
import org.ow2.easywsdl.wsdl.api.abstractItf.AbsItfDescription;


/**
 * @author Nicolas Salatge - eBM WebSourcing
 */
public class Util {

	@SuppressWarnings("unchecked")
	public static Map<FeatureConstants, Object> convertWSDLFeatures2SchemaFeature(
			final AbsItfDescription desc) {
		final Map<FeatureConstants, Object> features = new HashMap<FeatureConstants, Object>();
		features
		.put(
				FeatureConstants.VERBOSE,
				((AbstractDescriptionImpl) desc)
				.getFeatureValue(org.ow2.easywsdl.wsdl.api.WSDLReader.FeatureConstants.VERBOSE));
		features
		.put(
				FeatureConstants.IMPORT_DOCUMENTS,
				((AbstractDescriptionImpl) desc)
				.getFeatureValue(org.ow2.easywsdl.wsdl.api.WSDLReader.FeatureConstants.IMPORT_DOCUMENTS));
		return features;
	}

	public static String getPrefix(final String name) {
		String res = null;
		if ((name != null)&&(name.indexOf(":") != -1)) {
			res = name.substring(0, name.indexOf(":"));
		}
		return res;
	}

	public static String getLocalPartWithoutPrefix(final String name) {
		String res = name;
		if (name.indexOf(':') != -1) {
			res = name.substring(name.indexOf(':') + 1, name.length());
		}
		return res;
	}

	public static String convertSchemaLocationIntoString(Description wsdlDef) {
		StringBuffer buf = new StringBuffer();
		if(wsdlDef.getSchemaLocation().size() > 0) {
			for(Entry<String, String> entry: wsdlDef.getSchemaLocation().entrySet()) {
				buf.append(entry.getKey() + " " + entry.getValue() + " ");
			}
		} else {
			return null;
		}
		return buf.toString();
	}
}
