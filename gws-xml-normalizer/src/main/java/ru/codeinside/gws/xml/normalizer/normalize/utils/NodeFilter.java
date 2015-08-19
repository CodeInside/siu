package ru.codeinside.gws.xml.normalizer.normalize.utils;

import org.w3c.dom.Node;

/**
 * An interface to tell to the c14n if a node is included or not in the output
 */
public interface NodeFilter {

    /**
     * Tells if a node must be output in c14n.
     *
     * @param n
     * @return 1 if the node should be output.
     *         0 if node must not be output,
     *         -1 if the node and all it's child must not be output.
     */
    int isNodeInclude(Node n);

    /**
     * Tells if a node must be output in a c14n.
     * The caller must assured that this method is always call
     * in document order. The implementations can use this
     * restriction to optimize the transformation.
     *
     * @param n
     * @param level the relative level in the tree
     * @return 1 if the node should be output.
     *         0 if node must not be output,
     *         -1 if the node and all it's child must not be output.
     */
    int isNodeIncludeDO(Node n, int level);

}
