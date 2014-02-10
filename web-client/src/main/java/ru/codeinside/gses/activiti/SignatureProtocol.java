/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti;

import com.vaadin.ui.Form;
import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.gses.cert.NameParts;
import ru.codeinside.gses.cert.X509;
import ru.codeinside.gses.webui.CertificateInvalid;
import ru.codeinside.gses.webui.CertificateReader;
import ru.codeinside.gses.webui.CertificateVerifier;
import ru.codeinside.gses.webui.CertificateVerifyClientProvider;
import ru.codeinside.gses.webui.Flash;
import ru.codeinside.gses.webui.components.sign.SignApplet;
import ru.codeinside.gses.webui.components.sign.SignAppletListener;

import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.logging.Logger;

public class SignatureProtocol implements SignAppletListener {
  private static final long serialVersionUID = 1L;

  final private FormID formID;
  final private String fieldId;
  final private String[] ids;
  final private byte[][] blocks;
  final private boolean[] files;
  final private byte[][] signs;
  final private String caption;
  final private Form form;

  public SignatureProtocol(FormID formID, String fieldId, String caption, byte[][] blocks, boolean[] files, String[] ids, Form form) {
    this.formID = formID;
    this.fieldId = fieldId;
    this.caption = caption;
    this.form = form;
    this.blocks = blocks;
    this.files = files;
    this.ids = ids;
    signs = new byte[blocks.length][];
  }

  @Override
  public void onLoading(SignApplet signApplet) {
  }

  @Override
  public void onNoJcp(SignApplet signApplet) {
    form.removeItemProperty(fieldId);
    ReadOnly field = new ReadOnly("В вашей операционной системе требуется установить КриптоПРО JCP", false);
    field.setCaption(caption);
    form.addField(fieldId, field);
  }

  @Override
  public void onCert(SignApplet signApplet, X509Certificate certificate) {
    boolean ok = false;
    String errorClause = null;
    try {
      boolean link = AdminServiceProvider.getBoolProperty(CertificateVerifier.LINK_CERTIFICATE);
      if (link) {
        byte[] x509 = AdminServiceProvider.get().withEmployee(Flash.login(), new CertificateReader());
        ok = Arrays.equals(x509, certificate.getEncoded());
      } else {
        ok = true;
      }
      CertificateVerifyClientProvider.getInstance().verifyCertificate(certificate);
    } catch (CertificateEncodingException e) {
    } catch (CertificateInvalid err) {
       errorClause = err.getMessage();
       ok = false;
    }
    if (ok) {
      signApplet.block(1, blocks.length);
    } else {
      form.removeItemProperty(fieldId);
      NameParts subject = X509.getSubjectParts(certificate);
      String fieldValue = (errorClause == null) ? "Сертификат " + subject.getShortName() + " отклонён" : errorClause;
      ReadOnly field = new ReadOnly(fieldValue, false);
      field.setCaption(caption);
      form.addField(fieldId, field);
    }
  }

  @Override
  public void onBlockAck(SignApplet signApplet, int i) {
    logger().fine("AckBlock:" + i);
    if (1 <= i && i <= blocks.length) {
      signApplet.chunk(1, 1, blocks[i - 1]);
    }
  }

  @Override
  public void onChunkAck(SignApplet signApplet, int i) {
    logger().fine("AckChunk:" + i);
    if (1 <= i && i <= blocks.length) {
      blocks[i - 1] = null;
    }
  }

  @Override
  public void onSign(SignApplet signApplet, byte[] sign) {
    final int i = signApplet.getBlockAck();
    logger().fine("done block:" + i);
    if (1 <= i && i <= blocks.length) {
      signs[i - 1] = sign;
      if (i < blocks.length) {
        signApplet.block(i + 1, blocks.length);
      } else {
        form.removeItemProperty(fieldId);
        NameParts subjectParts = X509.getSubjectParts(signApplet.getCertificate());
        FormSignaturesField field2 = new FormSignaturesField(subjectParts.getShortName(), new Signatures(formID, signApplet.getCertificate(), ids, files, signs));
        field2.setCaption(caption);
        field2.setRequired(true);
        form.addField(fieldId, field2);
      }
    }
  }

  private Logger logger() {
    return Logger.getLogger(getClass().getName());
  }
}
