package net.mobidom.bp.beans;

public class SnilsRef implements DocumentRef {

  private static final long serialVersionUID = 4358706063204231072L;

  String number;

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  @Override
  public String getFullNumber() {
    return number;
  }

}
