package ru.codeinside.gws.xml.normalizer.normalize.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Christian Geuer-Pollmann
 */
public class HelperNodeList implements NodeList {

    /**
     * Field nodes
     */
    List<Node> nodes = new ArrayList<Node>();
    boolean allNodesMustHaveSameParent = false;

    /**
     *
     */
    public HelperNodeList() {
        this(false);
    }


    /**
     * @param allNodesMustHaveSameParent
     */
    public HelperNodeList(boolean allNodesMustHaveSameParent) {
        this.allNodesMustHaveSameParent = allNodesMustHaveSameParent;
    }

    /**
     * Method item
     *
     * @param index
     * @return node with index i
     */
    public Node item(int index) {
        return nodes.get(index);
    }

    /**
     * Method getLength
     *
     * @return length of the list
     */
    public int getLength() {
        return nodes.size();
    }

    /**
     * Method appendChild
     *
     * @param node
     * @throws IllegalArgumentException
     */
    public void appendChild(Node node) throws IllegalArgumentException {
        if (this.allNodesMustHaveSameParent && this.getLength() > 0
                && this.item(0).getParentNode() != node.getParentNode()) {
            throw new IllegalArgumentException("Nodes have not the same Parent");
        }
        nodes.add(node);
    }

    /**
     * @return the document that contains this nodelist
     */
    public Document getOwnerDocument() {
        if (this.getLength() == 0) {
            return null;
        }
        return XMLUtils.getOwnerDocument(this.item(0));
    }
}