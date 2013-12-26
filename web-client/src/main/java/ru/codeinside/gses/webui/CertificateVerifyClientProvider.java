/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui;

import javax.annotation.PostConstruct;
import javax.ejb.DependsOn;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

@Singleton
@Startup
@DependsOn("BaseBean")
public class CertificateVerifyClientProvider {
  @Inject
  CertificateVerifier verifier;

  transient static CertificateVerifier instance;
  @PostConstruct
  void initialize() {
    synchronized (CertificateVerifier.class) {
      if (instance == null) {
         instance = verifier;
      }
    }
  }

  public static CertificateVerifier getInstance() {
    return instance;
  }
}
