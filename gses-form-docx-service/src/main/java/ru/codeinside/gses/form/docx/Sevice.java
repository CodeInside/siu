/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.form.docx;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.codeinside.gses.form.FormData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

final public class Sevice {

  public static void main(String... args) {
    final Logger logger = LoggerFactory.getLogger(Sevice.class);
    final DocxFormConverter converter = new DocxFormConverter();
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, Charset.forName("UTF-8")));
    for (; ; ) {
      System.gc();
      try {
        String line = reader.readLine();
        if (line == null) {
          break;
        }
        line = line.trim();
        if ("exit".equalsIgnoreCase(line)) {
          break;
        }
        if (line.isEmpty()) {
          continue;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        FormData data = objectMapper.readValue(line, FormData.class);
        converter.createForm(data);
        System.out.println("ok");
        System.out.flush();
      } catch (IOException e) {
        logger.warn("io error", e);
        break;
      }
    }
  }
}
