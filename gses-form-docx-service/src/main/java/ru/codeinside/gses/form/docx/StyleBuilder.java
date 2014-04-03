/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.form.docx;

import org.docx4j.wml.BooleanDefaultTrue;
import org.docx4j.wml.CTBorder;
import org.docx4j.wml.CTLanguage;
import org.docx4j.wml.HpsMeasure;
import org.docx4j.wml.Jc;
import org.docx4j.wml.JcEnumeration;
import org.docx4j.wml.PPr;
import org.docx4j.wml.PPrBase;
import org.docx4j.wml.RFonts;
import org.docx4j.wml.RPr;
import org.docx4j.wml.STBorder;
import org.docx4j.wml.Style;
import org.docx4j.wml.U;
import org.docx4j.wml.UnderlineEnumeration;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

final class StyleBuilder {

  final List<Style> styles;

  StyleBuilder(List<Style> styles) {
    this.styles = styles;
  }

  void build() {

    removeUnused();

    Style defaultFont = createFontStyle(RStyle.fs, "Шрифт", true, false);
    Style uFont = createFontStyle(RStyle.ufs, "Шрифт1", false, true);
    Style defaultStyle = createDefaultStyle();

    Style orgStyle = createStyle(PStyle.orgStyle, "Организация", 14, true, JcEnumeration.CENTER);
    Style receiptStyle = createStyle(PStyle.receiptStyle, "Заявление", 14, true, JcEnumeration.CENTER);
    Style serviceStyle = createStyle(PStyle.serviceStyle, "Услуга", 12, true, JcEnumeration.LEFT);
    Style field1Style = createStyle(PStyle.field1Style, "Поле1", 10, false, JcEnumeration.LEFT);
    Style field2Style = createStyle(PStyle.field2Style, "Поле2", 10, false, JcEnumeration.LEFT);
    Style field3Style = createStyle(PStyle.field3Style, "Поле3", 10, false, JcEnumeration.LEFT);
    Style field4Style = createStyle(PStyle.field4Style, "Поле4", 10, false, JcEnumeration.LEFT);
    Style field5Style = createStyle(PStyle.field5Style, "Поле5", 10, false, JcEnumeration.LEFT);
    Style field6Style = createStyle(PStyle.field6Style, "Поле6", 10, false, JcEnumeration.LEFT);


    addBorder(orgStyle);

    addSpacing(orgStyle, 0, 400);
    addSpacing(receiptStyle, 200, 200);
    addSpacing(serviceStyle, 100, 200);
    addSpacing(field1Style, 100, 0);
    addSpacing(field2Style, 100, 0);
    addSpacing(field3Style, 100, 0);
    addSpacing(field4Style, 100, 0);
    addSpacing(field5Style, 100, 0);
    addSpacing(field6Style, 100, 0);

    addIndentLeft(field2Style, 110);
    addIndentLeft(field3Style, 220);
    addIndentLeft(field4Style, 330);
    addIndentLeft(field5Style, 440);
    addIndentLeft(field6Style, 550);

    styles.add(defaultFont);
    styles.add(uFont);
    styles.add(defaultStyle);
    styles.add(orgStyle);
    styles.add(receiptStyle);
    styles.add(serviceStyle);
    styles.add(field1Style);
    styles.add(field2Style);
    styles.add(field3Style);
    styles.add(field4Style);
    styles.add(field5Style);
    styles.add(field6Style);
  }

  private void removeUnused() {
    List<Style> toDelete = new ArrayList<Style>();
    for (Style s : styles) {
      if (!"style0".equals(s.getStyleId())) {
        toDelete.add(s);
      }
    }
    for (Style s : toDelete) {
      styles.remove(s);
    }
  }

  private Style createFontStyle(RStyle rStyle, String name, boolean def, boolean underline) {
    Style style = new Style();
    if (def) {
      style.setDefault(true);
    }
    style.setType("character");
    style.setStyleId(rStyle.name());
    style.setName(newStyleName(name));
    RPr run = new RPr();
    if (underline) {
      U u = new U();
      u.setVal(UnderlineEnumeration.SINGLE);
      run.setU(u);
    }
    style.setRPr(run);
    return style;
  }

  private Style createDefaultStyle() {
    Style style = new Style();
    style.setDefault(true);
    style.setType("paragraph");
    style.setStyleId("DocDefaults");
    style.setName(newStyleName("Базовый"));
    style.setPPr(new PPr());
    RPr run = new RPr();
    run.setRFonts(newFont());
    run.setLang(newLanguage());
    style.setRPr(run);
    return style;
  }

  private void addSpacing(Style style, int before, int after) {
    PPrBase.Spacing spacing = new PPrBase.Spacing();
    spacing.setBefore(BigInteger.valueOf(before));
    spacing.setAfter(BigInteger.valueOf(after));
    style.getPPr().setSpacing(spacing);
  }

  private void addIndentLeft(Style style, int n) {
    PPrBase.Ind indent = new PPrBase.Ind();
    indent.setLeft(BigInteger.valueOf(n));
    style.getPPr().setInd(indent);
  }

  private void addBorder(Style style) {
    CTBorder singeBlackBorder = new CTBorder();
    singeBlackBorder.setSz(BigInteger.valueOf(4));
    singeBlackBorder.setVal(STBorder.SINGLE);
    singeBlackBorder.setColor("000000");
    PPrBase.PBdr pBdr = new PPrBase.PBdr();
    pBdr.setBottom(singeBlackBorder);
    style.getPPr().setPBdr(pBdr);
  }

  private Style createStyle(PStyle pStyle, String name, int fontSize, boolean bold, JcEnumeration justify) {
    RPr r = new RPr();
    r.setSz(newMeasure(fontSize));

    if (bold) {
      r.setB(new BooleanDefaultTrue());
    }

    PPr p = new PPr();
    Jc jc = newJustify(justify);
    p.setJc(jc);

    Style style = new Style();
    style.setBasedOn(newBaseOn("DocDefaults"));
    style.setType("paragraph");
    style.setStyleId(pStyle.name());
    style.setName(newStyleName(name));
    style.setPPr(p);
    style.setRPr(r);

    return style;
  }

  private Jc newJustify(JcEnumeration value) {
    Jc jc = new Jc();
    jc.setVal(value);
    return jc;
  }

  private Style.Name newStyleName(String value) {
    Style.Name name = new Style.Name();
    name.setVal(value);
    return name;
  }

  private RFonts newFont() {
    RFonts font = new RFonts();
    font.setAscii("Arial");
    font.setHAnsi("Arial");
    return font;
  }

  private Style.BasedOn newBaseOn(String value) {
    Style.BasedOn basedOn = new Style.BasedOn();
    basedOn.setVal(value);
    return basedOn;
  }

  private HpsMeasure newMeasure(int value) {
    HpsMeasure measure = new HpsMeasure();
    measure.setVal(BigInteger.valueOf(value * 2));
    return measure;
  }

  private CTLanguage newLanguage() {
    CTLanguage lang = new CTLanguage();
    lang.setVal("ru-RU");
    return lang;
  }

}
