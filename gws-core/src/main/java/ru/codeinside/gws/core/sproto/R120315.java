/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.core.sproto;


import ru.codeinside.gws.api.CryptoProvider;
import ru.codeinside.gws.api.Revision;
import ru.codeinside.gws.api.XmlNormalizer;
import ru.codeinside.gws.api.XmlSignatureInjector;
import ru.codeinside.gws.core.Xml;


/**
 * В тестах можно включить дамп: HttpAdapter.dump = false;
 */
public class R120315 extends ServerProtocolImpl {

  public R120315(final CryptoProvider cryptoProvider, XmlNormalizer xmlNormalizer, XmlSignatureInjector injector) {
    super(cryptoProvider, Xml.REV120315, Revision.rev120315, xmlNormalizer, injector);
  }
}
