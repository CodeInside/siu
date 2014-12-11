package net.mobidom.bp.beans.types;

public enum ТипУдостоверенияЛичности {
  
  ПАСПОРТ_ГРАЖДАНИНА_РФ("Паспорт гражданина РФ"), 
  СВИДЕТЕЛЬСТВО_О_РОЖДЕНИИ, 
  ПАСПОРТ_МОРЯКА, 
  УДОСТОВЕРЕНИЕ_ЛИЧНОСТИ_ВОЕННОСЛУЖАЩЕГО, 
  ПАСПОРТ_ГРАЖДАНИНА_СССР("Паспорт гражданина СССР"), 
  ВОЕННЫЙ_БИЛЕТ, 
  ВРЕМЕННОЕ_УДОСТОВЕРЕНИЕ_ЛИЧНОСТИ, 
  ПАСПОРТ_ИНОСТРАННОГО_ГРАЖДАНИНА, 
  ВИД_НА_ЖИТЕЛЬСТВО_В_РФ("Вид на жительство в РФ"), 
  ДИПЛОМАТИЧЕСКИЙ_ПАСПОРТ, 
  УДОСТОВЕРЕНИЕ_БЕЖЕНЦА;
  
  private String label;

  private ТипУдостоверенияЛичности() {
    String label = name().replace('_', ' ').toLowerCase();
    label = label.substring(0, 1).toUpperCase() + label.substring(1);
    this.label = label;
  }

  private ТипУдостоверенияЛичности(String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }
}
