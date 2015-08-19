package ru.codeinside.gws.xml.normalizer.normalize.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import ru.codeinside.gws.xml.normalizer.normalize.exceptions.XMLSecurityException;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * This Object serves as Content for the ds:Transforms for exclusive
 * Canonicalization.
 * <BR />
 * It implements the {@link Element} interface
 * and can be used directly in a DOM tree.
 *
 * @author Christian Geuer-Pollmann
 */
public class InclusiveNamespaces extends ElementProxy {

    /**
     * Field _TAG_EC_INCLUSIVENAMESPACES
     */
    public static final String _TAG_EC_INCLUSIVENAMESPACES =
            "InclusiveNamespaces";

    /**
     * Field _ATT_EC_PREFIXLIST
     */
    public static final String _ATT_EC_PREFIXLIST = "PrefixList";

    /**
     * Field ExclusiveCanonicalizationNamespace
     */
    public static final String ExclusiveCanonicalizationNamespace =
            "http://www.w3.org/2001/10/xml-exc-c14n#";

    /**
     * Constructor XPathContainer
     *
     * @param doc
     * @param prefixList
     */
    public InclusiveNamespaces(Document doc, String prefixList) {
        this(doc, InclusiveNamespaces.prefixStr2Set(prefixList));
    }

    /**
     * Constructor InclusiveNamespaces
     *
     * @param doc
     * @param prefixes
     */
    public InclusiveNamespaces(Document doc, Set<String> prefixes) {
        super(doc);

        SortedSet<String> prefixList = null;
        if (prefixes instanceof SortedSet<?>) {
            prefixList = (SortedSet<String>) prefixes;
        } else {
            prefixList = new TreeSet<String>(prefixes);
        }

        StringBuilder sb = new StringBuilder();
        for (String prefix : prefixList) {
            if (prefix.equals("xmlns")) {
                sb.append("#default ");
            } else {
                sb.append(prefix + " ");
            }
        }

        this.constructionElement.setAttributeNS(
                null, InclusiveNamespaces._ATT_EC_PREFIXLIST, sb.toString().trim());
    }

    /**
     * Constructor InclusiveNamespaces
     *
     * @param element
     * @param BaseURI
     * @throws ru.codeinside.gws.xml.normalizer.normalize.exceptions.XMLSecurityException
     */
    public InclusiveNamespaces(Element element, String BaseURI)
            throws XMLSecurityException {
        super(element, BaseURI);
    }

    /**
     * Method getInclusiveNamespaces
     *
     * @return The Inclusive Namespace string
     */
    public String getInclusiveNamespaces() {
        return this.constructionElement.getAttributeNS(null, InclusiveNamespaces._ATT_EC_PREFIXLIST);
    }

    /**
     * Decodes the <code>inclusiveNamespaces</code> String and returns all
     * selected namespace prefixes as a Set. The <code>#default</code>
     * namespace token is represented as an empty namespace prefix
     * (<code>"xmlns"</code>).
     * <BR/>
     * The String <code>inclusiveNamespaces=" xenc    ds #default"</code>
     * is returned as a Set containing the following Strings:
     * <UL>
     * <LI><code>xmlns</code></LI>
     * <LI><code>xenc</code></LI>
     * <LI><code>ds</code></LI>
     * </UL>
     *
     * @param inclusiveNamespaces
     * @return A set to string
     */
    public static SortedSet<String> prefixStr2Set(String inclusiveNamespaces) {
        SortedSet<String> prefixes = new TreeSet<String>();

        if ((inclusiveNamespaces == null) || (inclusiveNamespaces.length() == 0)) {
            return prefixes;
        }

        String[] tokens = inclusiveNamespaces.split("\\s");
        for (String prefix : tokens) {
            if (prefix.equals("#default")) {
                prefixes.add("xmlns");
            } else {
                prefixes.add(prefix);
            }
        }

        return prefixes;
    }

    /**
     * Method getBaseNamespace
     *
     * @inheritDoc
     */
    public String getBaseNamespace() {
        return InclusiveNamespaces.ExclusiveCanonicalizationNamespace;
    }

    /**
     * Method getBaseLocalName
     *
     * @inheritDoc
     */
    public String getBaseLocalName() {
        return InclusiveNamespaces._TAG_EC_INCLUSIVENAMESPACES;
    }
}

