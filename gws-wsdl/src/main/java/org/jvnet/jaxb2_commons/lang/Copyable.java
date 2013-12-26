package org.jvnet.jaxb2_commons.lang;

public interface Copyable {

	public Object createCopy();
	
	public Object copyTo(Object target);

}
