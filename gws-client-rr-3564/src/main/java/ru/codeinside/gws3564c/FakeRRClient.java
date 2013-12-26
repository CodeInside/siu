/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws3564c;

import ru.codeinside.gws.api.ClientResponse;
import ru.codeinside.gws.api.Enclosure;
import ru.codeinside.gws.api.ExchangeContext;

import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Реализация подделки-клиента сервиса. В ответ выдает готовый ответ, а не дожидается его от сервиса
 *
 * @author hlinov
 */
public class FakeRRClient extends RRclient {
  private static final String COUNT_REQUESTS = "countRequests";
  private final Logger logger = Logger.getLogger(getClass().getName());

  @Override
  public void processClientResponse(ClientResponse response, ExchangeContext ctx) {
    // инициализируем счетчики
    if (!ctx.getVariableNames().contains(COUNT_REQUESTS)) {
      ctx.setVariable(COUNT_REQUESTS, 0);
    }
    Integer countRequest = (Integer) ctx.getVariable(COUNT_REQUESTS);
    ctx.setVariable(COUNT_REQUESTS, ++countRequest);
    if (countRequest < 2) {
      super.processClientResponse(response, ctx);
    } else {
      //  останавливаем пинг
      ctx.setVariable("smevPool", false);
      ctx.setVariable("status", response.packet.status);
      // заливка переменных
      ctx.setVariable("result_request", "Клиент-подделка прислал ответ");
      // создаем enclosure
      createFakeEnclosure(ctx, "enclosureData_Var1", "Данные из первого вложения");
      createFakeEnclosure(ctx, "enclosureData_Var2", "Данные из второго вложения");
      ctx.setVariable("enclosureData", "enclosureData_Var1;enclosureData_Var2");
    }

  }

  private void createFakeEnclosure(ExchangeContext ctx, String varName, String enclosureData) {
    try {
      Enclosure enclosure = new Enclosure("enclosure.txt", enclosureData.getBytes("UTF8"));
      enclosure.mimeType = "text/plain";
      enclosure.fileName = "enclosure.txt";
      ctx.addEnclosure(varName, enclosure);
    } catch (UnsupportedEncodingException e) {
      logger.log(Level.SEVERE, e.getMessage(), e);
    }
  }
}
