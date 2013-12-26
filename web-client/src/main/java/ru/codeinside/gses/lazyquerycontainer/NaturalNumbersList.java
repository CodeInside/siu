/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */
package ru.codeinside.gses.lazyquerycontainer;

import java.util.AbstractList;
import java.util.RandomAccess;

public final class NaturalNumbersList extends AbstractList<Integer> implements RandomAccess, java.io.Serializable {
	/** Java serialization version UID. */
	private static final long serialVersionUID = 1L;
	/** The size of the list. */
	private final int size;
	/** Array containing list values. This array is created on demand. */
	private Integer[] array = null;

	/**
	 * Constructor which sets the size of the constructed list.
	 * 
	 * @param size
	 *            Size of the constructed list.
	 */
	public NaturalNumbersList(final int size) {
		this.size = size;
	}

	/**
	 * {@inheritDoc}
	 */
	public int size() {
		return size;
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized Integer[] toArray() {
		if (array == null) {
			array = new Integer[size];
			for (int i = 0; i < size; i++) {
				array[i] = i;
			}
		}
		return array.clone();
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public <T> T[] toArray(final T[] a) {
		if (a.length < size) {
			return (T[]) toArray();
		}
		for (int i = 0; i < size; i++) {
			a[i] = (T) Integer.valueOf(i);
		}
		if (a.length > size) {
			a[size] = null;
		}
		return a;
	}

	/**
	 * {@inheritDoc}
	 */
	public Integer get(final int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		}
		return index;
	}

	/**
	 * {@inheritDoc}
	 */
	public Integer set(final int index, final Integer element) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	public int indexOf(final Object o) {
		if (o == null) {
			return -1;
		}
		if (o instanceof Integer) {
			int i = (Integer) o;
			if (i < 0 || i >= size) {
				return -1;
			}
			return i;
		}
		return -1;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean contains(final Object o) {
		return indexOf(o) != -1;
	}
}