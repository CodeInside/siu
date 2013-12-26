package org.jvnet.jaxb2_commons.lang.builder;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class CopyBuilder {

	public Object copy(Object object) {
		if (object == null)
			return null;
		else if (object instanceof String)
			return object;
		else if (object instanceof Number)
			return object;
		else if (object instanceof Cloneable) {
			return cloneCloneable((Cloneable) object);
		} else {
			return object;
		}
	}

	public long copy(long value) {
		return value;
	}

	public int copy(int value) {
		return value;
	}

	public short copy(short value) {
		return value;
	}

	public char copy(char value) {
		return value;
	}

	public byte copy(byte value) {
		return value;
	}

	public double copy(double value) {
		return value;
	}

	public float copy(float value) {
		return value;
	}

	public boolean copy(boolean value) {
		return value;
	}

	public Object[] copy(Object[] array) {
		final Object[] copy = new Object[array.length];
		for (int index = 0; index < array.length; index++) {
			final Object element = array[index];
			final Object elementCopy = copy(element);
			copy[index] = elementCopy;
		}
		return copy;
	}

	public long[] copy(long[] array) {
		final long[] copy = new long[array.length];
		System.arraycopy(array, 0, copy, 0, array.length);
		return copy;
	}

	public int[] copy(int[] array) {
		final int[] copy = new int[array.length];
		System.arraycopy(array, 0, copy, 0, array.length);
		return copy;
	}

	public short[] copy(short[] array) {
		final short[] copy = new short[array.length];
		System.arraycopy(array, 0, copy, 0, array.length);
		return copy;
	}

	public char[] copy(char[] array) {
		final char[] copy = new char[array.length];
		System.arraycopy(array, 0, copy, 0, array.length);
		return copy;
	}

	public byte[] copy(byte[] array) {
		final byte[] copy = new byte[array.length];
		System.arraycopy(array, 0, copy, 0, array.length);
		return copy;
	}

	public double[] copy(double[] array) {
		final double[] copy = new double[array.length];
		System.arraycopy(array, 0, copy, 0, array.length);
		return copy;
	}

	public float[] copy(float[] array) {
		final float[] copy = new float[array.length];
		System.arraycopy(array, 0, copy, 0, array.length);
		return copy;
	}

	public boolean[] copy(boolean[] array) {
		final boolean[] copy = new boolean[array.length];
		System.arraycopy(array, 0, copy, 0, array.length);
		return copy;
	}

	private Object cloneCloneable(Cloneable object) {
		Method method = null;

		try {
			method = object.getClass().getMethod("clone", (Class[]) null);
		} catch (NoSuchMethodException nsmex) {
			method = null;
		}

		if (method == null || !Modifier.isPublic(method.getModifiers())) {

			throw new UnsupportedOperationException(
					"Could not clone object [" + object + "].",
					new CloneNotSupportedException(
							"Object class ["
									+ object.getClass()
									+ "] implements java.lang.Cloneable interface, "
									+ "but does not provide a public no-arg clone() method. "
									+ "By convention, classes that implement java.lang.Cloneable "
									+ "should override java.lang.Object.clone() method (which is protected) "
									+ "with a public method."));
		}

		final boolean wasAccessible = method.isAccessible();
		try {
			if (!wasAccessible) {
				try {
					method.setAccessible(true);
				} catch (SecurityException ignore) {
				}
			}

			return method.invoke(object, (Object[]) null);
		} catch (Exception ex) {
			throw new UnsupportedOperationException(
					"Could not clone the object ["
							+ object
							+ "] as invocation of the clone() method has thrown an exception.",
					ex);
		} finally {
			if (!wasAccessible) {
				try {
					method.setAccessible(false);
				} catch (SecurityException ignore) {
				}
			}
		}
	}
}
