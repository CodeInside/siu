package ru.codeinside.gses.form.docx;

import org.docx4j.Docx4J;
import org.docx4j.convert.out.HTMLSettings;
import org.docx4j.model.structure.PageSizePaper;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.BooleanDefaultTrue;
import org.docx4j.wml.CTBorder;
import org.docx4j.wml.CTLanguage;
import org.docx4j.wml.HpsMeasure;
import org.docx4j.wml.Jc;
import org.docx4j.wml.JcEnumeration;
import org.docx4j.wml.P;
import org.docx4j.wml.PPr;
import org.docx4j.wml.PPrBase;
import org.docx4j.wml.R;
import org.docx4j.wml.RFonts;
import org.docx4j.wml.RPr;
import org.docx4j.wml.STBorder;
import org.docx4j.wml.Style;
import org.docx4j.wml.Text;
import org.docx4j.wml.U;
import org.docx4j.wml.UnderlineEnumeration;
import ru.codeinside.gses.form.FormConverter;
import ru.codeinside.gses.form.FormData;
import ru.codeinside.gses.form.FormEntry;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

final public class DocxFormConverter implements FormConverter {

  //final Map<String, String> styleId = new HashMap<String, String>();

  @Override
  public void createForm(FormData formData, String... files) {

    String docTarget = null;
    String htmlTarget = null;

    for (String file : files) {
      if (file.endsWith(".docx")) {
        docTarget = file;
      } else if (file.endsWith(".html")) {
        htmlTarget = file;
      }
    }

    WordprocessingMLPackage word;

    try {
      word = getTemplate();
    } catch (Docx4JException e) {
      e.printStackTrace();
      throw new IllegalStateException("can't load!");
    }

    createStyles(word);

    MainDocumentPart document = word.getMainDocumentPart();

//    StyleTree styleTree = document.getStyleTree();
//
//    Tree<StyleTree.AugmentedStyle> paragraphStylesTree = styleTree.getParagraphStylesTree();
//    for (Node<StyleTree.AugmentedStyle> styleNode : paragraphStylesTree.toList()) {
//      Style style = styleNode.getData().getStyle();
//      styleId.put(style.getName().getVal(), style.getStyleId());
//    }

    {
      P org = document.addStyledParagraphOfText("orgStyle", formData.orgName);
      List<Object> toRemove = new ArrayList<Object>(document.getContent());
      for (Object o : toRemove) {
        if (o != org) {
          document.getContent().remove(o);
        }
      }
    }

    {
      P receipt = document.addStyledParagraphOfText("receiptStyle", "ЗАЯВЛЕНИЕ №");
      R r = new R();
      receipt.getContent().add(r);
      Text t = new Text();
      if (formData.receiptId != null) {
        t.setValue(formData.receiptId.toString());
      } else {
        t.setValue("    ");
      }
      r.getContent().add(t);
      r.setRPr(createUnderline());

      r = new R();
      t = new Text();
      t.setValue(" от ");
      r.getContent().add(t);
      receipt.getContent().add(r);

      r = new R();
      t = new Text();
      SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
      dateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
      t.setValue(dateFormat.format(formData.receiptDate));
      r.getContent().add(t);
      r.setRPr(createUnderline());
      receipt.getContent().add(r);
    }

    {
      document.addStyledParagraphOfText("serviceStyle", formData.serviceName);
    }

    addEntries(formData.entries, document, 1);

    document.addStyledParagraphOfText("DocDefaults", "");

    if (docTarget != null) {
      try {
        word.save(new File(docTarget));
      } catch (Docx4JException e) {
        e.printStackTrace();
      }
    }

    if (htmlTarget != null) {
      OutputStream os = null;
      try {
        os = new FileOutputStream(htmlTarget);
        HTMLSettings settings = new HTMLSettings();
        settings.setWmlPackage(word);
        Docx4J.toHTML(settings, os, Docx4J.FLAG_NONE);
      } catch (Docx4JException e) {
        e.printStackTrace();
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      } finally {
        close(os);
      }
    }
  }

  private void addEntries(FormEntry[] entries, MainDocumentPart doc, int level) {
    for (FormEntry entry : entries) {
      P p = doc.addStyledParagraphOfText("field" + Math.min(level, 6) + "Style", entry.name);

      if (entry.value != null) {
        R r = new R();
        Text t = new Text();
        t.setValue(" ");
        r.getContent().add(t);
        p.getContent().add(r);

        r = new R();
        t = new Text();
        t.setValue(entry.value);
        r.getContent().add(t);
        r.setRPr(createUnderline());
        p.getContent().add(r);
      }

      if (entry.children != null) {
        addEntries(entry.children, doc, level + 1);
      }
    }
  }

  private WordprocessingMLPackage getTemplate() throws Docx4JException {
    InputStream is = getClass().getClassLoader().getResourceAsStream("template1.docx");
    try {
      if (false) {
        return WordprocessingMLPackage.createPackage(PageSizePaper.A4, false);
      } else {
        return WordprocessingMLPackage.load(is);
      }
    } finally {
      close(is);
    }
  }

  void close(Closeable closable) {
    if (closable != null) {
      try {
        closable.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }


  void createStyles(WordprocessingMLPackage mlPackage) {
    List<Style> styles = mlPackage.getMainDocumentPart().getStyleDefinitionsPart().getJaxbElement().getStyle();
    Style orgStyle = createStyle("orgStyle", "Организация", 14, true, JcEnumeration.CENTER);
    Style receipStyle = createStyle("receiptStyle", "Заявление", 14, true, JcEnumeration.CENTER);
    Style serviceStyle = createStyle("serviceStyle", "Услуга", 12, true, JcEnumeration.LEFT);
    Style field1Style = createStyle("field1Style", "Поле1", 10, false, JcEnumeration.LEFT);
    Style field2Style = createStyle("field2Style", "Поле2", 10, false, JcEnumeration.LEFT);
    Style field3Style = createStyle("field3Style", "Поле3", 10, false, JcEnumeration.LEFT);
    Style field4Style = createStyle("field4Style", "Поле4", 10, false, JcEnumeration.LEFT);
    Style field5Style = createStyle("field5Style", "Поле5", 10, false, JcEnumeration.LEFT);
    Style field6Style = createStyle("field6Style", "Поле6", 10, false, JcEnumeration.LEFT);


    addBorder(orgStyle);

    styles.add(orgStyle);
    styles.add(receipStyle);
    styles.add(serviceStyle);
    styles.add(field1Style);
    styles.add(field2Style);
    styles.add(field3Style);
    styles.add(field4Style);
    styles.add(field5Style);
    styles.add(field6Style);

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

  private RPr createUnderline() {
    RPr params = new RPr();
    U u = new U();
    u.setVal(UnderlineEnumeration.SINGLE);
    params.setU(u);
    return params;
  }

  private Style createStyle(String styleId, String title, int fontSize, boolean bold, JcEnumeration justify) {
    Style newStyle = new Style();
    Style.BasedOn basedOn = new Style.BasedOn();
    basedOn.setVal("DocDefaults");//style0
    newStyle.setBasedOn(basedOn);
    newStyle.setType("paragraph");
    newStyle.setStyleId(styleId);
    Style.Name name = new Style.Name();

    name.setVal(title);
    newStyle.setName(name);

    RPr rpr = new RPr();
    RFonts runFont = new RFonts();
    runFont.setAscii("Arial");
    runFont.setHAnsi("Arial");
    rpr.setRFonts(runFont);
    HpsMeasure size = new HpsMeasure();
    size.setVal(BigInteger.valueOf(fontSize * 2));
    rpr.setSz(size);

    newStyle.setRPr(rpr);

    CTLanguage lang = new CTLanguage();
    lang.setVal("ru-RU");
    rpr.setLang(lang);

    if (bold) {
      rpr.setB(new BooleanDefaultTrue());
    }

    PPr pPr = new PPr();
    newStyle.setPPr(pPr);

    PPrBase.Spacing spacing = new PPrBase.Spacing();
    spacing.setBefore(BigInteger.valueOf(0));
    spacing.setAfter(BigInteger.valueOf(150));
    pPr.setSpacing(spacing);

    Jc jc = new Jc();
    jc.setVal(justify);
    pPr.setJc(jc);

    return newStyle;
  }

}
