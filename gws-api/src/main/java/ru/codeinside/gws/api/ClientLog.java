package ru.codeinside.gws.api;

import java.io.Closeable;
import java.io.OutputStream;

/**
 * Журнал действий для экземпляра клиента (потребителя СМЭВ) на уровне СИУ и на уровне транспорта HTTP.
 * За создание отвечает {@link ru.codeinside.gws.api.LogService#createClientLog(String, String)}
 *
 * @author xeodon
 * @see LogService
 * @since 1.0.7
 */
public interface ClientLog extends Closeable {

  /**
   * Регистрация исключительной ситуации.
   *
   * @param e сиутация.
   */
  void log(Throwable e);

  /**
   * Получить поток регистрация данных HTTP из исходящего транспортного потока.
   *
   * @return поток для регистрации.
   */
  OutputStream getHttpOutStream();

  /**
   * Получить поток регистрация данных HTTP из входящего транспортного потока.
   *
   * @return поток для регистрации.
   */
  OutputStream getHttpInStream();

  /**
   * Регистрация запроса клиента к поставщику до отправки в исходящий транпортный поток.
   *
   * @param request запрос клиента к постащику.
   */
  void logRequest(ClientRequest request);

  /**
   * Регистрация ответка от постащика к клиенту после разбора входяшего транпортного потока.
   *
   * @param response ответ постащика к клиенту.
   */
  void logResponse(ClientResponse response);

  /**
   * Закрыть журнал.
   */
  void close();

}
