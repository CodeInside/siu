package ru.codeinside.gses.webui.form;

import ru.codeinside.gses.form.FormEntry;

public interface FormDataSource {
  /**
   * Построение дерева данных.
   *
   * @return дерево записей.
   */
  FormEntry createFormTree();
}
