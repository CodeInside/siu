/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.form.docx;

import org.docx4j.Docx4J;
import org.docx4j.convert.out.HTMLSettings;
import org.docx4j.model.structure.PageSizePaper;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import ru.codeinside.gses.form.FormConverter;
import ru.codeinside.gses.form.FormData;
import ru.codeinside.gses.form.FormEntry;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

final public class DocxFormConverter implements FormConverter {


  public DocxFormConverter() {
    createDocument();
  }

  @Override
  public void createForm(FormData formData) {
    WordprocessingMLPackage mlPackage = createDocument();
    MainDocumentPart document = mlPackage.getMainDocumentPart();

    new StyleBuilder(document.getStyleDefinitionsPart().getJaxbElement().getStyle()).build();

    Builder b = new Builder(document);

    b.addP(PStyle.orgStyle);
    b.addR();
    b.text(formData.orgName);

    b.addP(PStyle.receiptStyle);
    b.addR();
    b.text("ЗАЯВЛЕНИЕ ");
    b.text("№");
    b.addR(RStyle.ufs);
    if (formData.receiptId != null) {
      b.text(formData.receiptId.toString());
    } else {
      b.text("    ");
    }
    b.addR();
    b.text(" от ");

    b.addR(RStyle.ufs);
    if (formData.receiptDate != null) {
      SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
      dateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
      b.text(dateFormat.format(formData.receiptDate));
    } else {
      b.text("    ");
    }

    b.addP(PStyle.serviceStyle);
    b.addR();
    b.text(formData.serviceName);

    if (formData.entries != null) {
      addEntries(formData.entries, b, 1);
    }

    if (formData.docxFile != null) {
      try {
        mlPackage.save(new File(formData.docxFile));
      } catch (Docx4JException e) {
        e.printStackTrace();
      }
    }

    if (formData.htmlFile != null) {
      OutputStream os = null;
      try {
        os = new FileOutputStream(formData.htmlFile);
        HTMLSettings settings = new HTMLSettings();
        settings.setWmlPackage(mlPackage);
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

  void addEntries(FormEntry[] entries, Builder b, int level) {
    for (FormEntry entry : entries) {
      PStyle pStyle = PStyle.valueOf("field" + Math.min(level, 6) + "Style");
      b.addP(pStyle);
      b.addR();
      b.text(entry.name);
      if (entry.value != null) {
        b.text(" ");
        b.addR(RStyle.ufs);
        b.text(entry.value);
      }
      if (entry.children != null) {
        addEntries(entry.children, b, level + 1);
      }
    }
  }

  WordprocessingMLPackage createDocument() {
    try {
      return WordprocessingMLPackage.createPackage(PageSizePaper.A4, false);
    } catch (InvalidFormatException e) {
      throw new IllegalStateException(e);
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

}
