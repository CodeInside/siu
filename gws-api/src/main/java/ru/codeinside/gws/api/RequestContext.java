/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.api;

import java.util.List;

/**
 * Контекст поставщика при обработке запроса от потребителя.
 * Обработка запросов потребителя и исполнение заявок происходит асинхронно
 * и в разных потоках. Подготовка ответов осуществляется в контексте исполнения заявки.
 * <p/>
 * Потребитель формирует цепочу запросов для конкретной заявки по правилам идентификации цепочки СМЭВ:
 * <ul>
 * <li><b>serverRequest.routerPacket.messageId</b> - при использовании сервисов-посредников Ростелекома</li>
 * <li><b>serverRequest.packet.originRequestIdRef</b> - при прямом вызове сервиса</li>
 * </ul>
 */
public interface RequestContext {

  /**
   * Флаг начала цепочки.
   *
   * @return true если запрос является первым в цепочке запросов.
   */
  boolean isFirst();

  /**
   * Идентификатор заявления, связанного с цепочкой.
   *
   * @return null если цепочка не связана с заявлением, иначе идентификатор заявления.
   */
  String getBid();

  /**
   * Создание контекста подачи заявления.
   *
   * @param procedureCode код процедуры, которую необходимо исполнить.
   * @return контекст подачи заявления.
   * @throws ServerException при ошибках с хранилищем заявок, или если процедура не найдена.
   */
  DeclarerContext getDeclarerContext(long procedureCode);

  /**
   * Запрос потребителя, обрабатываемый в текущем контексте.
   *
   * @return текущий запрос потребителя.
   */
  ServerRequest getRequest();

  /**
   * Получить подготовленное состояние исполнения текущей заявки.
   *
   * @return ответ с состоянием, либо null если ответ не подготовлен.
   * @throws ServerException при ошибках с хранилищем заявок.
   */
  ServerResponse getState();

  /**
   * Получить подготовленный результат исполнения текущей заявки.
   *
   * @return ответ с результатом, либо null если ответ не подготовлен.
   * @throws ServerException при ошибках с хранилищем заявок.
   */
  ServerResponse getResult();

  /**
   * Идентификаторы заявлений, связанные с цепочкой.
   *
   * @return список идентификаторов заявлений, для которых запущен процесс исполнения.
   * @throws ServerException при ошибках с хранилищем заявок.
   * @since 1.0.8
   */
  List<String> getBids();

  /**
   * Получить подготовленное состояние исполнения заявки.
   *
   * @param bid идентификатор заявки.
   * @return ответ с состоянием, либо null если ответ не подготовлен.
   * @throws ServerException при ошибках с хранилищем заявок.
   * @since 1.0.8
   */
  ServerResponse getState(String bid);

  /**
   * Получить подготовленный результат исполнения заявки.
   *
   * @param bid идентификатор заявки.
   * @return ответ с результатом, либо null если ответ не подготовлен.
   * @throws ServerException при ошибках с хранилищем заявок.
   * @since 1.0.8
   */
  ServerResponse getResult(String bid);

  /**
   * Получение контекста существующей заявки для вненсения дополнительных данных
   *
   * @return контекст текущей заявки
   * @since 1.0.12
   */
  ReceiptContext getReceiptContext();

}
