package org.jvnet.jaxb2_commons.lang.builder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBElement;

import org.jvnet.jaxb2_commons.lang.CopyTo;
import org.jvnet.jaxb2_commons.lang.Copyable;
import org.w3c.dom.Node;

public class JAXBCopyBuilder extends CopyBuilder {

	@SuppressWarnings("unchecked")
	public Object copy(Object object) {
		if (object == null)
			return null;
		else if (object instanceof List) {
			return copy((List) object);
		} else if (object instanceof Set) {
			return copy((Set) object);
		} else if (object instanceof CopyTo)
			return ((CopyTo) object).copyTo(((CopyTo) object).createCopy(), this);
		else if (object instanceof Copyable)
			return ((Copyable) object).copyTo(((Copyable) object).createCopy());
		else if (object instanceof Node)
			return ((Node) object).cloneNode(true);
		else if (object instanceof JAXBElement) {
			final JAXBElement sourceElement = (JAXBElement) object;
			final Object sourceObject = sourceElement.getValue();
			final Object copyObject = copy(sourceObject);
			final JAXBElement copyElement = new JAXBElement(sourceElement
					.getName(), sourceElement.getDeclaredType(), sourceElement
					.getScope(), copyObject);
			return copyElement;
		} else {
			return super.copy(object);
		}
	}

	@SuppressWarnings("unchecked")
	public Object copy(List list) {
		final List copy = new ArrayList(list.size());
		for (final Object element : list) {
			final Object copyElement = copy(element);
			copy.add(copyElement);
		}
		return copy;
	}

	@SuppressWarnings("unchecked")
	public Object copy(Set set) {
		final Set copy = new HashSet(set.size());
		for (final Object element : set) {
			final Object copyElement = copy(element);
			copy.add(copyElement);
		}
		return copy;
	}
}
