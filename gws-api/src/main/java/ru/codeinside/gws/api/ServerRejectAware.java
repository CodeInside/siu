/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */
package ru.codeinside.gws.api;

/**
 * Обработчик отклонения заявки на сервере.
 */
public interface ServerRejectAware {

  /**
   * Вызывается при отклонении заявки.
   *
   * @param reason  причина отклонения.
   * @param context контекст исполнения.
   * @return подготовленный ответ для сохранения и передачи потребителю в дальнейшем.
   */
  ServerResponse processReject(String reason, ReceiptContext context);
}
