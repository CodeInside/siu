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
package org.ow2.easywsdl.schema.api;

/**
 * 
 * @author Nicolas Salatge - eBM WebSourcing
 * 
 */
public class XmlException extends Exception {
    public static final long serialVersionUID = 1;

    public static final String INVALID_Schema = "INVALID_Schema";

    public static final String PARSER_ERROR = "PARSER_ERROR";

    public static final String OTHER_ERROR = "OTHER_ERROR";

    public static final String CONFIGURATION_ERROR = "CONFIGURATION_ERROR";

    public static final String UNBOUND_PREFIX = "UNBOUND_PREFIX";

    public static final String NO_PREFIX_SPECIFIED = "NO_PREFIX_SPECIFIED";

    private String faultCode = null;

    private Throwable targetThrowable = null;

    private String location = null;

    public XmlException(final String faultCode, final String msg, final Throwable t) {
        super(msg, t);
        this.setFaultCode(faultCode);
    }

    public XmlException(final String msg, final Throwable t) {
        super(msg, t);
    }

    public XmlException(final Throwable t) {
        super(t);
    }

    public XmlException(final String msg) {
        super(msg);
    }

    public XmlException(final String faultCode, final String msg) {
        this(faultCode, msg, null);
    }

    public void setFaultCode(final String faultCode) {
        this.faultCode = faultCode;
    }

    public String getFaultCode() {
        return this.faultCode;
    }

    public void setTargetException(final Throwable targetThrowable) {
        this.targetThrowable = targetThrowable;
    }

    public Throwable getTargetException() {
        if (this.targetThrowable == null) {
            return this.getCause();
        } else {
            return this.targetThrowable;
        }
    }

    /**
     * Set the location using an XPath expression. Used for error messages.
     * 
     * @param location
     *            an XPath expression describing the location where the
     *            exception occurred.
     */
    public void setLocation(final String location) {
        this.location = location;
    }

    /**
     * Get the location, if one was set. Should be an XPath expression which is
     * used for error messages.
     */
    public String getLocation() {
        return this.location;
    }

    @Override
    public String getMessage() {
    	if(!super.getMessage().contains("SchemaException")) {
	        final StringBuffer strBuf = new StringBuffer();
	
	        strBuf.append("SchemaException");
	
	        if (this.location != null) {
	            try {
	                strBuf.append(" (at " + this.location + ")");
	            } catch (final IllegalArgumentException e) {
	            }
	        }
	
	        if (this.faultCode != null) {
	            strBuf.append(": faultCode=" + this.faultCode);
	        }
	
	        final String thisMsg = super.getMessage();
	        String targetMsg = null;
	        String targetName = null;
	        if (this.getTargetException() != null) {
	            targetMsg = this.getTargetException().getMessage();
	            targetName = this.getTargetException().getClass().getName();
	        }
	
	        if ((thisMsg != null) && ((targetMsg == null) || !thisMsg.equals(targetMsg))) {
	            strBuf.append(": " + thisMsg);
	        }
	
	        if (targetName != null) {
	            strBuf.append(": " + targetName);
	        }
	
	        if (targetMsg != null) {
	            strBuf.append(": " + targetMsg);
	        }
	
	        return strBuf.toString();
    	} else {
    		return super.getMessage();
    	}
    }
}
