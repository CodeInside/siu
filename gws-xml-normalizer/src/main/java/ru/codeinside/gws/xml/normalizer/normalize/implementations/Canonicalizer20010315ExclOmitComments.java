package ru.codeinside.gws.xml.normalizer.normalize.implementations;

import ru.codeinside.gws.xml.normalizer.normalize.utils.Constants;

public class Canonicalizer20010315ExclOmitComments extends Canonicalizer20010315Excl {

    /**
     *
     */
    public Canonicalizer20010315ExclOmitComments() {
        super(false);
    }

    /**
     * @inheritDoc
     */
    public final String engineGetURI() {
        return Constants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS;
    }

    /**
     * @inheritDoc
     */
    public final boolean engineGetIncludeComments() {
        return false;
    }
}