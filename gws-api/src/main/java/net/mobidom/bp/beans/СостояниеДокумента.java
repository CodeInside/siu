package net.mobidom.bp.beans;

public enum СостояниеДокумента {
  В_НАЛИЧИИ("В наличии"), ДОЗАПРОС("Дозапрос");

  private String label;

  private СостояниеДокумента(String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }

}
