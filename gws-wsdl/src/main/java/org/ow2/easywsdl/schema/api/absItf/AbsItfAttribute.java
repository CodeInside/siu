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

package org.ow2.easywsdl.schema.api.absItf;

import org.ow2.easywsdl.schema.api.SchemaElement;
import org.ow2.easywsdl.schema.api.SchemaReader.FeatureConstants;

/**
 * @author Nicolas Salatge - eBM WebSourcing
 */
public interface AbsItfAttribute<T extends AbsItfType> extends SchemaElement {

	 public enum Use {
	        OPTIONAL("optional"), REQUIRED(
	                "required"), PROHIBITED("prohibited");

	        private final String value;

	        /**
	         * Creates a new instance of {@link FeatureConstants}
	         * 
	         * @param value
	         */
	        private Use(final String value) {
	            this.value = value;
	        }

	        /**
	         * 
	         * @return
	         */
	        public String value() {
	            return this.value;
	        }

	
	        public boolean equals(final String val) {
	            return this.toString().equals(val);
	        }

	        /*
	         * (non-Javadoc)
	         * 
	         * @see java.lang.Enum#toString()
	         */
	        @Override
	        public String toString() {
	            return this.value;
	        }
	        
	        public static Use value(String val) {
	        	Use res = null;
	        	if(OPTIONAL.value().equals(val)) {
	        		res = OPTIONAL;
	        	} else if(REQUIRED.value().equals(val)) {
	        		res = REQUIRED;
	        	} else if(PROHIBITED.value().equals(val)) {
	        		res = PROHIBITED;
	        	} 
	        	return res;
	        }
	    }
	
	
    String getName();

    String getValue();
    
    Use getUse();

    String getNamespaceUri();
    
    T getType();

    void setType(T type);
}
