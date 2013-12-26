/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.beans;

import java.util.List;

import javax.inject.Named;

import com.google.common.collect.Lists;

@Named("candidates")
public class Candidates {

	public List<String> users() {
		return Lists.newArrayList("kermit", "gonza");
	}

	public List<String> groups(String ownerName, String subgroup) {
		return Lists.newArrayList(ownerName + "_" + subgroup);
	}
}