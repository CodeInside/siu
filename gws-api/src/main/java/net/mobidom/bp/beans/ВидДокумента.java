package net.mobidom.bp.beans;

public enum ВидДокумента {
  ОРИГИНАЛ("Оригинал"), КОПИЯ("Копия"), НОТАРИАЛЬНАЯ_КОПИЯ("Нотариальная копия");

  private String label;

  private ВидДокумента(String label) {
    this.label = label;
  }

  public String getLabel() {
    return this.label;
  }
}
