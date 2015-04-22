package ru.codeinside.gws.xml.normalizer.normalize.implementations;


import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import ru.codeinside.gws.xml.normalizer.normalize.XMLSignatureInput;
import ru.codeinside.gws.xml.normalizer.normalize.exceptions.CanonicalizationException;
import ru.codeinside.gws.xml.normalizer.normalize.utils.C14nHelper;
import ru.codeinside.gws.xml.normalizer.normalize.utils.Constants;
import ru.codeinside.gws.xml.normalizer.normalize.utils.InclusiveNamespaces;
import ru.codeinside.gws.xml.normalizer.normalize.utils.XMLUtils;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Implements &quot; <A
 * HREF="http://www.w3.org/TR/2002/REC-xml-exc-c14n-20020718/">Exclusive XML
 * Canonicalization, Version 1.0 </A>&quot; <BR />
 * Credits: During restructuring of the Canonicalizer framework, Ren??
 * Kollmorgen from Software AG submitted an implementation of ExclC14n which
 * fitted into the old architecture and which based heavily on my old (and slow)
 * implementation of "Canonical XML". A big "thank you" to Ren?? for this.
 * <BR />
 * <i>THIS </i> implementation is a complete rewrite of the algorithm.
 *
 * @author Christian Geuer-Pollmann <geuerp@apache.org>
 * @version $Revision: 1147448 $
 * @see <a href="http://www.w3.org/TR/2002/REC-xml-exc-c14n-20020718/ Exclusive#">
 *      XML Canonicalization, Version 1.0</a>
 */
public abstract class Canonicalizer20010315Excl extends CanonicalizerBase {

    private static final String XML_LANG_URI = Constants.XML_LANG_SPACE_SpecNS;
    private static final String XMLNS_URI = Constants.NamespaceSpecNS;

    /**
     * This Set contains the names (Strings like "xmlns" or "xmlns:foo") of
     * the inclusive namespaces.
     */
    private SortedSet<String> inclusiveNSSet;

    private final SortedSet<Attr> result = new TreeSet<Attr>(COMPARE);

    /**
     * Constructor Canonicalizer20010315Excl
     *
     * @param includeComments
     */
    public Canonicalizer20010315Excl(boolean includeComments) {
        super(includeComments);
    }

    /**
     * Method engineCanonicalizeSubTree
     *
     * @param rootNode
     * @throws CanonicalizationException
     * @inheritDoc
     */
    public byte[] engineCanonicalizeSubTree(Node rootNode)
            throws CanonicalizationException {
        return engineCanonicalizeSubTree(rootNode, "", null);
    }

    /**
     * Method engineCanonicalizeSubTree
     *
     * @param rootNode
     * @param inclusiveNamespaces
     * @throws CanonicalizationException
     * @inheritDoc
     */
    public byte[] engineCanonicalizeSubTree(
            Node rootNode, String inclusiveNamespaces
    ) throws CanonicalizationException {
        return engineCanonicalizeSubTree(rootNode, inclusiveNamespaces, null);
    }

    /**
     * Method engineCanonicalizeSubTree
     *
     * @param rootNode
     * @param inclusiveNamespaces
     * @param excl                A element to exclude from the c14n process.
     * @return the rootNode c14n.
     * @throws CanonicalizationException
     */
    public byte[] engineCanonicalizeSubTree(
            Node rootNode, String inclusiveNamespaces, Node excl
    ) throws CanonicalizationException {
        inclusiveNSSet = InclusiveNamespaces.prefixStr2Set(inclusiveNamespaces);
        return super.engineCanonicalizeSubTree(rootNode, excl);
    }

    /**
     * @param rootNode
     * @param inclusiveNamespaces
     * @return the rootNode c14n.
     * @throws CanonicalizationException
     */
    public byte[] engineCanonicalize(
            XMLSignatureInput rootNode, String inclusiveNamespaces
    ) throws CanonicalizationException {
        inclusiveNSSet = InclusiveNamespaces.prefixStr2Set(inclusiveNamespaces);
        return super.engineCanonicalize(rootNode);
    }

    /**
     * Method engineCanonicalizeXPathNodeSet
     *
     * @param xpathNodeSet
     * @param inclusiveNamespaces
     * @throws CanonicalizationException
     * @inheritDoc
     */
    public byte[] engineCanonicalizeXPathNodeSet(
            Set<Node> xpathNodeSet, String inclusiveNamespaces
    ) throws CanonicalizationException {
        inclusiveNSSet = InclusiveNamespaces.prefixStr2Set(inclusiveNamespaces);
        return super.engineCanonicalizeXPathNodeSet(xpathNodeSet);
    }

    @Override
    protected Iterator<Attr> handleAttributesSubtree(Element element, NameSpaceSymbTable ns)
            throws CanonicalizationException {
        // result will contain the attrs which have to be output
        final SortedSet<Attr> result = this.result;
        result.clear();

        // The prefix visibly utilized (in the attribute or in the name) in
        // the element
        SortedSet<String> visiblyUtilized = new TreeSet<String>();
        if (inclusiveNSSet != null && !inclusiveNSSet.isEmpty()) {
            visiblyUtilized.addAll(inclusiveNSSet);
        }

        if (element.hasAttributes()) {
            final NamedNodeMap attrs = element.getAttributes();
            final int attrsLength = attrs.getLength();
            for (int i = 0; i < attrsLength; i++) {
                Attr attribute = (Attr) attrs.item(i);
                String NName = attribute.getLocalName();
                String NNodeValue = attribute.getNodeValue();
                String namespaceURI = attribute.getNamespaceURI();

                // SOAP bug?
                if (namespaceURI == null && NName == null) {
                    final String name = attribute.getName();
                    if (name != null && name.startsWith("xmlns:")) {
                        //fix SOAP bug:
                        namespaceURI = XMLNS_URI;
                        NName = name.substring(6);
                    }
                }

                if (!XMLNS_URI.equals(namespaceURI)) {
                    // Not a namespace definition.
                    // The Element is output element, add the prefix (if used) to
                    // visiblyUtilized
                    String prefix = attribute.getPrefix();
                    if (prefix != null && !(prefix.equals(XML) || prefix.equals(XMLNS))) {
                        visiblyUtilized.add(prefix);
                    }
                    // Add to the result.
                    result.add(attribute);
                } else {
                    boolean isXML = XML.equals(NName) && XML_LANG_URI.equals(NNodeValue);
                    if (isXML) {
                        //
                    } else {
                        boolean newNs = ns.addMapping(NName, NNodeValue, attribute);
                        if (!newNs) {
                            //
                        } else if (C14nHelper.namespaceIsRelative(NNodeValue)) {
                            // The default mapping for xml must not be output.
                            // New definition check if it is relative.
                            Object exArgs[] = {element.getTagName(), NName, attribute.getNodeValue()};
                            throw new CanonicalizationException(
                                    "c14n.Canonicalizer.RelativeNamespace", exArgs
                            );
                        }
                    }
                }
            }
        }
        String prefix;
        if (element.getNamespaceURI() != null
                && !(element.getPrefix() == null || element.getPrefix().length() == 0)) {
            prefix = element.getPrefix();
        } else {
            prefix = XMLNS;
        }
        visiblyUtilized.add(prefix);

        for (String s : visiblyUtilized) {
            Attr key = ns.getMapping(s);
            if (key != null) {
                result.add(key);
            }
        }

        return result.iterator();
    }

    /**
     * @param element
     * @throws CanonicalizationException
     * @inheritDoc
     */
    @Override
    protected final Iterator<Attr> handleAttributes(Element element, NameSpaceSymbTable ns)
            throws CanonicalizationException {
        // result will contain the attrs which have to be output
        final SortedSet<Attr> result = this.result;
        result.clear();

        // The prefix visibly utilized (in the attribute or in the name) in
        // the element
        Set<String> visiblyUtilized = null;
        // It's the output selected.
        boolean isOutputElement = isVisibleDO(element, ns.getLevel()) == 1;
        if (isOutputElement) {
            visiblyUtilized = new TreeSet<String>();
            if (inclusiveNSSet != null && !inclusiveNSSet.isEmpty()) {
                visiblyUtilized.addAll(inclusiveNSSet);
            }
        }

        if (element.hasAttributes()) {
            NamedNodeMap attrs = element.getAttributes();
            int attrsLength = attrs.getLength();
            for (int i = 0; i < attrsLength; i++) {
                Attr attribute = (Attr) attrs.item(i);

                String NName = attribute.getLocalName();
                String NNodeValue = attribute.getNodeValue();

                if (!XMLNS_URI.equals(attribute.getNamespaceURI())) {
                    if (isVisible(attribute) && isOutputElement) {
                        // The Element is output element, add the prefix (if used)
                        // to visibyUtilized
                        String prefix = attribute.getPrefix();
                        if (prefix != null && !(prefix.equals(XML) || prefix.equals(XMLNS))) {
                            visiblyUtilized.add(prefix);
                        }
                        // Add to the result.
                        result.add(attribute);
                    }
                } else if (isOutputElement && !isVisible(attribute) && !XMLNS.equals(NName)) {
                    ns.removeMappingIfNotRender(NName);
                } else {
                    if (!isOutputElement && isVisible(attribute)
                            && inclusiveNSSet.contains(NName)
                            && !ns.removeMappingIfRender(NName)) {
                        Node n = ns.addMappingAndRender(NName, NNodeValue, attribute);
                        if (n != null) {
                            result.add((Attr) n);
                            if (C14nHelper.namespaceIsRelative(attribute)) {
                                Object exArgs[] = {element.getTagName(), NName, attribute.getNodeValue()};
                                throw new CanonicalizationException(
                                        "c14n.Canonicalizer.RelativeNamespace", exArgs
                                );
                            }
                        }
                    }

                    if (ns.addMapping(NName, NNodeValue, attribute)
                            && C14nHelper.namespaceIsRelative(NNodeValue)) {
                        // New definition check if it is relative
                        Object exArgs[] = {element.getTagName(), NName, attribute.getNodeValue()};
                        throw new CanonicalizationException(
                                "c14n.Canonicalizer.RelativeNamespace", exArgs
                        );
                    }
                }
            }
        }

        if (isOutputElement) {
            // The element is visible, handle the xmlns definition
            Attr xmlns = element.getAttributeNodeNS(XMLNS_URI, XMLNS);
            if (xmlns != null && !isVisible(xmlns)) {
                // There is a definition but the xmlns is not selected by the
                // xpath. then xmlns=""
                ns.addMapping(XMLNS, "", nullNode);
            }

            String prefix = null;
            if (element.getNamespaceURI() != null
                    && !(element.getPrefix() == null || element.getPrefix().length() == 0)) {
                prefix = element.getPrefix();
            } else {
                prefix = XMLNS;
            }
            visiblyUtilized.add(prefix);

            for (String s : visiblyUtilized) {
                Attr key = ns.getMapping(s);
                if (key != null) {
                    result.add(key);
                }
            }
        }

        return result.iterator();
    }

    protected void circumventBugIfNeeded(XMLSignatureInput input)
            throws CanonicalizationException, ParserConfigurationException,
            IOException, SAXException {
        if (!input.isNeedsToBeExpanded() || inclusiveNSSet.isEmpty() || inclusiveNSSet.isEmpty()) {
            return;
        }
        Document doc = null;
        if (input.getSubNode() != null) {
            doc = XMLUtils.getOwnerDocument(input.getSubNode());
        } else {
            doc = XMLUtils.getOwnerDocument(input.getNodeSet());
        }
        XMLUtils.circumventBug2650(doc);
    }
}
