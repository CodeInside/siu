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

@XmlType(name = "типОтвета")
@XmlEnum
public enum ResponseType {

	@XmlEnumValue("Ответ")
	ОТВЕТ("Ответ"),

	@XmlEnumValue("ДанныеНеНайдены")
	ДАННЫЕ_НЕ_НАЙДЕНЫ("ДанныеНеНайдены"),

	@XmlEnumValue("ОшибкаВЗапросе")
	ОШИБКА_В_ЗАПРОСЕ("ОшибкаВЗапросе");

	private final String value;

	ResponseType(String v) {
		value = v;
	}

	public String value() {
		return value;
	}

	public static ResponseType fromValue(String v) {
		for (ResponseType c : ResponseType.values()) {
			if (c.value.equals(v)) {
				return c;
			}
		}
		throw new IllegalArgumentException(v);
	}

}
