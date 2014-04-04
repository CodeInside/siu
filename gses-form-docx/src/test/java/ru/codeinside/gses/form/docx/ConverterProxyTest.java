/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.form.docx;

import org.junit.Test;
import ru.codeinside.gses.form.FormData;
import ru.codeinside.gses.form.FormEntry;

import java.util.Date;

public class ConverterProxyTest {

  @Test
  public void testCreateForm() throws Exception {

    RemoteService.testMode = true;

    ConverterProxy converter = new ConverterProxy();
    try {

      FormData data = new FormData();
      data.htmlFile = "target/x.html";
      data.docxFile = "target/x.docx";
      data.orgName = "Рога и копыта";
      data.receiptId = 12345L;
      data.receiptDate = new Date();
      data.serviceName = "Предоставление рогатого скота в наем, во временное пользование и ещё как либо, "
        + "главное чтобы скотина выжила и остались рожки да ножки";

      FormEntry c1 = e("Окрас", "Рыжий\n в усмерть");
      FormEntry h1 = e("Вид", "Бык");
      h1.children = new FormEntry[200];
      for (int i = 0; i < h1.children.length; i++) {
        h1.children[i] = c1;
      }
      FormEntry h2 = e("Вид", "Бычок");

      FormEntry heads = e("Поголовье");
      heads.children = new FormEntry[]{h1, h2};

      FormEntry kind = e("Вид скота", "Рогоносый вепрь");

      data.entries = new FormEntry[]{kind, heads};

      long time1 = System.currentTimeMillis();
      converter.createForm(data);
      long time2 = System.currentTimeMillis();
      System.out.println("ellapsed 1: " + (time2 - time1));

      data.htmlFile = "target/x1.html";
      data.docxFile = "target/x1.docx";
      converter.createForm(data);
      long time3 = System.currentTimeMillis();
      System.out.println("ellapsed 2: " + (time3 - time2));


      data.htmlFile = "target/x2.html";
      data.docxFile = "target/x3.docx";
      converter.createForm(data);
      long time4 = System.currentTimeMillis();
      System.out.println("ellapsed 3: " + (time4 - time3));

    } finally {
      converter.close();
    }
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

}
