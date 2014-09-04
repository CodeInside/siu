/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

@XmlSchema(
  xmlns = {
    @XmlNs(namespaceURI = "http://oep-penza.ru/com/oep", prefix = "oep")
  },
  namespace = "http://oep-penza.ru/com/oep",
  elementFormDefault = XmlNsForm.QUALIFIED
) package ru.codeinside.gws.c.oep.dict.data;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;