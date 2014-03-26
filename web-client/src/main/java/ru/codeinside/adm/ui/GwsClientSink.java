/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.ui;

import ru.codeinside.gws.api.Revision;

import java.io.Serializable;

interface GwsClientSink extends Serializable {
  public void selectClient(
    Long id,
    Revision revision,
    String url,
    String componentName,
    String version,
    String infoSys,
    String source,
    String description,
    Boolean available,
    Boolean logEnabled
  );
}
