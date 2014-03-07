package ru.codeinside.gws.api;

import java.io.OutputStream;

/**
 * Журнал действий для экземпляра услуги (поставщика СМЭВ) на уровне транспорта HTTP.
 * За создание отвечает {@link LogService#createClientLog(String, String)}
 *
 * @author xeodon
 * @see ru.codeinside.gws.api.LogService
 * @since 1.0.7
 */
public interface ServerLog extends AutoCloseable {

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
   * Регистрация запроса от клиента к поставщику после разбора входящего транспортного потока.
   *
   * @param request запрос к поставщику.
   */
  void logRequest(ServerRequest request);

  /**
   * Регистрация ответа от поставщика к клиенту перед записью в исходящий транспортный поток.
   *
   * @param response ответ поставщика.
   */
  void logResponse(ServerResponse response);

  /**
   * Закрыть журнал.
   */
  void close();

}
