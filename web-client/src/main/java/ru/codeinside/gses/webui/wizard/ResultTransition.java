package ru.codeinside.gses.webui.wizard;

import java.util.Objects;

/**
 * Подготовленные данные для перехода на слудющий этап
 */
public class ResultTransition {
    private final Object data;

    public ResultTransition(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }
}
