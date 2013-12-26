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
package org.ow2.easywsdl.wsdl.api.abstractItf;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.xml.namespace.QName;

import org.ow2.easywsdl.wsdl.api.WSDLElement;
import org.ow2.easywsdl.wsdl.api.WSDLException;


/**
 * This interface represents a WSDL operation. It includes information on input,
 * output and fault messages associated with usage of the operation.
 *
 * @author Nicolas Salatge - eBM WebSourcing
 */
public interface AbsItfOperation<In extends AbsItfInput, Out extends AbsItfOutput, F extends AbsItfFault>
extends WSDLElement {



	/**
	 * Constants for the Patterns.
	 *
	 */
	public enum MEPPatternConstants {
		IN_ONLY("http://www.w3.org/ns/wsdl/in-only"), ROBUST_IN_ONLY(
		"http://www.w3.org/ns/wsdl/robust-in-only"), IN_OUT(
		"http://www.w3.org/ns/wsdl/in-out"), IN_OPTIONAL_OUT(
		"http://www.w3.org/ns/wsdl/in-optional-out"), OUT_ONLY(
		"http://www.w3.org/ns/wsdl/out-only"), ROBUST_OUT_ONLY(
		"http://www.w3.org/ns/wsdl/robust-out-only"), OUT_IN(
		"http://www.w3.org/ns/wsdl/out-in"), OUT_OPTIONAL_IN(
		"http://www.w3.org/ns/wsdl/out-optional-in");

		/**
		 *
		 * @param pattern
		 * @return
		 */
		public static MEPPatternConstants valueOf(final URI pattern) {
			MEPPatternConstants result = null;
			if (pattern != null) {
				for (final MEPPatternConstants p : MEPPatternConstants.values()) {
					if (p.nameSpace.equals(pattern.toString())) {
						result = p;
					}
				}
			}
			return result;
		}

		private final String nameSpace;

		private final URI patternURI;

		/**
		 * Creates a new instance of {@link MEPPatternConstants}
		 *
		 * @param nameSpace
		 */
		private MEPPatternConstants(final String nameSpace) {
			this.nameSpace = nameSpace;
			try {
				this.patternURI = new URI(nameSpace);
			} catch (final URISyntaxException e) {
				throw new Error("Unexpected Error in URI namespace syntax", e); 
			}
		}

		/**
		 *
		 * @return
		 */
		public URI value() {
			return this.patternURI;
		}

		/**
		 * Please use this equals method instead of using :<code>
		 * value().equals(pattern)
		 * </code> which is
		 * almost 10 times slower...
		 *
		 * @param mep
		 * @return
		 */
		public boolean equals(final URI pattern) {
			return this.toString().equals(pattern.toString());
		}

		/**
		 * (non-Javadoc)
		 *
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() {
			return this.nameSpace;
		}
	}

	/**
	 * Set the name of this operation.
	 *
	 * @param name
	 *            the desired name
	 */
	void setQName(QName name);

	/**
	 * Get the name of this operation.
	 *
	 * @return the operation name
	 */
	QName getQName();

	/**
	 * Set the input message specification for this operation.
	 *
	 * @param input
	 *            the new input message
	 */
	void setInput(In input);

	/**
	 * Get the input message specification for this operation.
	 *
	 * @return the input message
	 */
	In getInput();

	/**
	 * Set the output message specification for this operation.
	 *
	 * @param output
	 *            the new output message
	 */
	void setOutput(Out output);

	/**
	 * Get the output message specification for this operation.
	 *
	 * @return the output message specification for the operation
	 */
	Out getOutput();

	/**
	 * Add a fault message that must be associated with this operation.
	 *
	 * @param fault
	 *            the new fault message
	 */
	void addFault(F fault);

	/**
	 * Get the specified fault .
	 *
	 * @param name
	 *            the name of the desired fault message.
	 * @return the corresponding fault message, or null if there wasn't any
	 *         matching message
	 */
	F getFault(String name);

	/**
	 * Get the specified fault .
	 *
	 * @param name
	 *            the name of the fault element.
	 * @return the corresponding fault message, or null if there wasn't any
	 *         matching message
	 */
	F getFaultByElementName(QName name);

	/**
	 * Remove the specified fault message.
	 *
	 * @param name
	 *            the name of the fault message to be removed.
	 * @return the fault message which was removed
	 */
	F removeFault(String name);

	/**
	 * Remove the specified fault .
	 *
	 * @param name
	 *            the name of the fault element to be removed.
	 * @return the fault message which was removed
	 */
	F removeFaultByElementName(QName name);

	/**
	 * Get all the fault messages associated with this operation.
	 *
	 * @return names of fault messages
	 */
	List<F> getFaults();

	/**
	 * Set the parameter ordering for a request-response, or solicit-response
	 * operation.
	 *
	 * @param parameterOrder
	 *            a list of named parameters containing the part names to
	 *            reflect the desired order of parameters for RPC-style
	 *            operations
	 * @throws WSDLException
	 */
	void setParameterOrdering(List<String> parameterOrder) throws WSDLException;

	/**
	 * Get the parameter ordering for this operation.
	 *
	 * @return the parameter ordering, a list consisting of message part names
	 */
	List<String> getParameterOrdering();

	/**
	 * get the pattern
	 */
	MEPPatternConstants getPattern();

	/**
	 * set the pattern
	 *
	 * @param pattern
	 *            the pattern
	 * @throws WSDLException
	 */
	void setPattern(MEPPatternConstants pattern) throws WSDLException;

	/**
	 * get signature of the operation
	 *
	 * @return the signature
	 */
	String getSignature();

	
	In createInput();
	
	Out createOutput();
	
	F createFault();
}
