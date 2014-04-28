/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.api;

import java.net.URL;

/**
 * API сервиса поставщика СМЭВ.
 * Один экземпляр поставщика может обслуживать все запросы потребителей.
 */
public interface Server {

  /**
   * Поддерживаемвя ревизия.
   */
  Revision getRevision();

  /**
   * Ссылка на ресур-описание WEB-сервиса. Ресурс должен быть внутри компонента.
   */
  URL getWsdlUrl();

  /**
   * Обработка запроса потребителя.
   * На каждый запрос потребителя может создаваться свой поток исполнения
   * и в один момент времени может обрабатываться множество запросов.
   *
   * @param requestContext контекст текущего запроса.
   * @return ответ потребителю
   */
  ServerResponse processRequest(RequestContext requestContext);


  /**
   * Обработка состояния заявки.
   * Вызывается из BPMN блока serviceTask в процессе исполнения.
   *
   * @param statusMessage  текст сообщения из параметров BPMN.
   * @param receiptContext конеткст исполнения заявки.
   * @return подготовленный ответ с состоянием для сохранения в контексте цепочки запросов.
   */
  ServerResponse processStatus(String statusMessage, ReceiptContext receiptContext);


  /**
   * Обработка результата заявки.
   * Вызывается из BPMN блока serviceTask в процессе исполнения,
   * либо по завершению исполнения заявки из BPMN блока endEvent.
   *
   * @param resultMessage  текст сообщения из параметров BPMN.
   * @param receiptContext конеткст исполнения заявки.
   * @return подготовленный ответ с результатом для сохранения в контексте цепочки запросов.
   */
  ServerResponse processResult(String resultMessage, ReceiptContext receiptContext);

}
