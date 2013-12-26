/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */
package ru.codeinside.gses.lazyquerycontainer;

/**
 * Enumeration defining the query item states.
 * @author Tommi S.E. Laukkanen
 */
public enum QueryItemStatus {
    /** Item state is up to date which storage. */
	None,
	/** Item is newly added and needs to be inserted to storage. */
	Added,
	/** Item has been modified and needs to updated to storage. */
	Modified,
	/** Item has been removed and needs to be deleted from storage. */
	Removed
}
