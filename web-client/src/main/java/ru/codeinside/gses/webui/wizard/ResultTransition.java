package ru.codeinside.gses.webui.wizard;

import java.io.Serializable;

/**
 * Подготовленные данные для перехода на слудющий этап
 */
public class ResultTransition implements Serializable {
    private final Object data;

    public ResultTransition(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }
}
