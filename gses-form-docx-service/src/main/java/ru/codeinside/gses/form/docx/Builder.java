/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.form.docx;

import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.P;
import org.docx4j.wml.PPr;
import org.docx4j.wml.PPrBase;
import org.docx4j.wml.R;
import org.docx4j.wml.RPr;
import org.docx4j.wml.Text;

final class Builder {

  final MainDocumentPart document;

  P p;
  R r;

  Builder(MainDocumentPart document) {
    this.document = document;
  }

  void addP(PStyle style) {
    r = null;
    p = new P();
    PPrBase.PStyle pStyle = new PPrBase.PStyle();
    pStyle.setVal(style.name());
    PPr ppr = new PPr();
    ppr.setPStyle(pStyle);
    p.setPPr(ppr);
    document.addObject(p);
  }

  void addR() {
    addR(null);
  }

  void addR(RStyle style) {
    r = new R();
    if (style != null) {
      org.docx4j.wml.RStyle rStyle = new org.docx4j.wml.RStyle();
      rStyle.setVal(style.name());
      RPr rPr = new RPr();
      rPr.setRStyle(rStyle);
      r.setRPr(rPr);
    }
    p.getContent().add(r);
  }

  void text(String v) {
    Text text = new Text();
    text.setValue(v);
    r.getContent().add(text);
  }
}
