/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.components.sign;

import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.gses.cert.X509;
import ru.codeinside.gses.vaadin.AppletIntegration;
import ru.codeinside.gses.webui.Flash;
import ru.codeinside.gses.webui.utils.RunProfile;

import javax.xml.bind.DatatypeConverter;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Logger;

public class SignApplet extends AppletIntegration {

  SignAppletListener listener;

  X509Certificate certificate;

  int blocksCount;
  int blockReq;
  int blockAck;
  int chunksCount;
  int chunkReq;
  int chunkAck;

  public SignApplet(SignAppletListener listener) {
    this.listener = listener;
    if (!RunProfile.isProduction()) {
      setAppletParams("appDebug", "true");
    }
    setAppletArchives(Arrays.asList("sign-applet-1.8.jar"));
    setName("Подписание данных личной ЭЦП");
    setAppletParams("appletId", "formSign");
    setAppletClass("ru.codeinside.sign.SignApplet");
    setWidth("477px");
    setHeight("177px");

    // загружается быстро, не успеют увидеть
    setAppletParams("image", "applet.gif");
  }

  private String getCodeBase() {
    final URL url = getWindow().getApplication().getURL();
    String appBase = url.getPath();
    return appBase.substring(0, 1 + appBase.lastIndexOf('/', appBase.length() - 2));
  }

  @Override
  public void attach() {
    setCodebase(getCodeBase());
  }

  public void setBindingMode() {
    setAppletParams("mode", "binding");
    setAppletParams("fio", AdminServiceProvider.get().findEmployeeByLogin(Flash.login()).getFio());
  }

  public void setRebindMode(byte[] x509) {
    setAppletParams("mode", "rebind");
    setAppletParams("x509", DatatypeConverter.printBase64Binary(x509));
    setAppletParams("fio", AdminServiceProvider.get().findEmployeeByLogin(Flash.login()).getFio());
  }

  public void setSignMode(byte[] x509) {
    setAppletParams("mode", "sign");
    setAppletParams("x509", DatatypeConverter.printBase64Binary(x509));
  }

  public void setUnboundSignMode() {
    setAppletParams("mode", "unbound");
  }

  public void changeVariables(Object source, Map<String, Object> variables) {

    Logger.getLogger(getClass().getName()).fine("applet send: " + variables.keySet());

    if (variables.containsKey("state")) {
      String state = variables.get("state").toString();
      if ("loading".equals(state)) {
        listener.onLoading(this);
      } else if ("noJcp".equals(state)) {
        listener.onNoJcp(this);
      }
    }
    if (variables.containsKey("cert")) {
      byte[] encoded = DatatypeConverter.parseBase64Binary(variables.get("cert").toString());
      X509Certificate cert = X509.decode(encoded);
      if (cert != null) {
        certificate = cert;
        listener.onCert(this, certificate);
      }
    }
    if (variables.containsKey("block")) {
      blockAck = Integer.parseInt(variables.get("block").toString());
      listener.onBlockAck(this, blockAck);
    }
    if (variables.containsKey("chunk")) {
      chunkAck = Integer.parseInt(variables.get("chunk").toString());
      listener.onChunkAck(this, chunkAck);
    }
    if (variables.containsKey("sign")) {
      chunkAck = chunksCount;
      byte[] sign = DatatypeConverter.parseBase64Binary(variables.get("sign").toString());
      listener.onSign(this, sign);
    }

  }

  public void block(int i, int n) {
    blockReq = i;
    blocksCount = n;
    executeCommand("block", new String[]{i + "", n + ""});
  }

  public void chunk(int i, int n, byte[] bytes) {
    chunkReq = i;
    chunksCount = n;
    executeCommand("chunk", new String[]{i + "", n + "", DatatypeConverter.printBase64Binary(bytes)});
  }

  public void close() {
    executeCommand("close");
  }

  public X509Certificate getCertificate() {
    return certificate;
  }

  public int getBlockAck() {
    return blockAck;
  }
}
