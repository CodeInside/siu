/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.form.docx;

import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.junit.Test;
import ru.codeinside.gses.form.FormData;
import ru.codeinside.gses.form.FormEntry;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

public class DocxFormConverterTest {


  @Test
  public void testCreateForm() throws Exception {

    DocxFormConverter converter = new DocxFormConverter();

    FormData data = new FormData();
    data.docxFile = "target/x.docx";
    data.htmlFile = "target/x.html";

    data.orgName = "Рога и копыта";
    data.receiptId = 12345L;
    data.receiptDate = new Date();
    data.serviceName = "Предоставление рогатого скота в наем";

    FormEntry c1 = e("Окрас", "Рыжий");
    FormEntry h1 = e("Вид", "Бык");
    h1.children = new FormEntry[]{c1};
    FormEntry h2 = e("Вид", "Бычок");

    FormEntry heads = e("Поголовье");
    heads.children = new FormEntry[]{h1, h2};

    FormEntry kind = e("Вид скота", "Рогоносый вепрь");

    data.entries = new FormEntry[]{kind, heads};
    converter.createForm(data);
  }

  FormEntry e(String name) {
    FormEntry e = new FormEntry();
    e.name = name;
    return e;
  }

  FormEntry e(String name, String value) {
    FormEntry e = new FormEntry();
    e.name = name;
    e.value = value;
    return e;
  }

  private WordprocessingMLPackage getTemplate() throws Docx4JException {
    InputStream is = getClass().getClassLoader().getResourceAsStream("empty.docx");
    try {
      return WordprocessingMLPackage.load(is);
    } finally {
      try {
        is.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

}
