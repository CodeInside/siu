/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws3417c.types.data;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "СтатусОпекуна")
@XmlEnum
public enum Curator {

	@XmlEnumValue("Нет")
	НЕТ("Нет"),

	@XmlEnumValue("Мать")
	МАТЬ("Мать"),

	@XmlEnumValue("Отец")
	ОТЕЦ("Отец"),

	@XmlEnumValue("Опекун")
	ОПЕКУН("Опекун"),

	@XmlEnumValue("Другое")
	ДРУГОЕ("Другое");

	private final String value;

	Curator(String v) {
		value = v;
	}

	public String value() {
		return value;
	}

	public static Curator fromValue(String v) {
		for (Curator c : Curator.values()) {
			if (c.value.equals(v)) {
				return c;
			}
		}
		throw new IllegalArgumentException(v);
	}

	public static Curator find(String v) {
		for (Curator c : Curator.values()) {
			if (c.value.equalsIgnoreCase(v)) {
				return c;
			}
		}
		return null;
	}

}
