package ru.codeinside.gses.beans;

import org.activiti.engine.delegate.VariableScope;
import ru.codeinside.gses.activiti.ftarchive.AttachmentFFT;
import ru.codeinside.gws.api.Enclosure;
import ru.codeinside.gws.api.ExchangeContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class StartFormExchangeContext implements ExchangeContext {
  private Map<String, Object> variables = new HashMap<String, Object>();
  private Object local;
  private VariableScope variableScope; //TODO заполнить данные контекста из VariableScope

  public StartFormExchangeContext(VariableScope variableScope) {
    this.variableScope = variableScope;
  }

  /**
   * Получить локальный объект.
   */
  @Override
  public Object getLocal() {
    return local;
  }

  /**
   * Установить локальный объект. Существует лишь между запросом и отвветом.
   *
   * @param value
   */
  @Override
  public void setLocal(Object value) {
    this.local = value;
  }

  /**
   * Получить имена переменных из процесса исполнения.
   */
  @Override
  public Set<String> getVariableNames() {
    return variables.keySet();
  }

  /**
   * Получить значение переменной.
   *
   * @param name
   */
  @Override
  public Object getVariable(String name) {
    return variables.get(name);
  }

  /**
   * Ассоциирована ли переменная с вложением.
   *
   * @param name
   */
  @Override
  public boolean isEnclosure(String name) {
    final Object value = variables.get(name);
    return AttachmentFFT.isAttachmentValue(value);
  }

  /**
   * Сохранить значение переменной в процесс исполнения.
   *
   * @param name
   * @param value
   */
  @Override
  public void setVariable(String name, Object value) {
    variables.put(name, value);
  }

  /**
   * Получить вложение по имени ассоциированой переменной.
   *
   * @param name
   */
  @Override
  public Enclosure getEnclosure(String name) {
    return null; //TODO получить вложения
  }

  /**
   * Добавить вложение и содать ассоциацию с именем переменной.
   *
   * @param name
   * @param enclosure
   */
  @Override
  public void addEnclosure(String name, Enclosure enclosure) {
    throw new UnsupportedOperationException();
  }
}
