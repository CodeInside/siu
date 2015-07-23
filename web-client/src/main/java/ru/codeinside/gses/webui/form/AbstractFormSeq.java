package ru.codeinside.gses.webui.form;

import ru.codeinside.gses.webui.wizard.ResultTransition;

public abstract class AbstractFormSeq implements FormSeq {
    protected final DataAccumulator dataAccumulator;

    protected ResultTransition resultTransition;

    public AbstractFormSeq() {
        this.dataAccumulator = null;
    }

    public AbstractFormSeq(DataAccumulator dataAccumulator) {
        this.dataAccumulator = dataAccumulator;
    }

    /**
     * Задать результат перехода на новый этап
     *
     * @param resultTransition полученный результат
     */
    @Override
    public void setResultTransition(ResultTransition resultTransition) {
        this.resultTransition = resultTransition;
    }

    @Override
    public void backwardAction() {
        if (dataAccumulator != null) {
            dataAccumulator.setClientRequest(null);
            dataAccumulator.setServerResponse(null);
            dataAccumulator.setSoapMessage(null);
        }
    }
}
