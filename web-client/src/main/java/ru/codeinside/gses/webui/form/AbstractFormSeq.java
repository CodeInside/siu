package ru.codeinside.gses.webui.form;

import ru.codeinside.gses.webui.wizard.ResultTransition;

public abstract class AbstractFormSeq implements FormSeq {
    protected ResultTransition resultTransition;

    /**
     * Задать результат перехода на новый этап
     *
     * @param resultTransition полученный результат
     */
    @Override
    public void setResultTransition(ResultTransition resultTransition) {
        this.resultTransition = resultTransition;
    }
}
