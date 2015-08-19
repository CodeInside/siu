package ru.codeinside.gses.form;

import java.util.Arrays;

public class FormEntry {

  public String id;

  public String name;

  public String value;

  public FormEntry[] children;

  @Override
  public String toString() {
    return "{'" + name + "'='" + value + "\' " + Arrays.toString(children) + '}';
  }
}
