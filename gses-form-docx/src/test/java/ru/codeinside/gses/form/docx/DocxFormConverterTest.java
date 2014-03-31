package ru.codeinside.gses.form.docx;

import org.junit.Test;
import ru.codeinside.gses.form.FormData;
import ru.codeinside.gses.form.FormEntry;

import java.util.Date;

public class DocxFormConverterTest {

  @Test
  public void testCreateForm() throws Exception {
    DocxFormConverter converter = new DocxFormConverter();

    FormData data = new FormData();
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
    converter.createForm(data, "target/x.docx", "target/x.html");
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
