package org.apache.xml.security.keys.keyresolver.implementations;

import org.apache.xml.security.keys.keyresolver.KeyResolverException;
import org.apache.xml.security.keys.keyresolver.KeyResolverSpi;
import org.apache.xml.security.keys.storage.StorageResolver;
import org.apache.xml.security.utils.Constants;
import org.apache.xml.security.utils.XMLUtils;
import org.w3c.dom.Element;

import javax.crypto.SecretKey;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

/**
 * Resolves a SecretKey within a KeyStore based on the KeyName.
 * The KeyName is the key entry alias within the KeyStore.
 */
public class SecretKeyResolver extends KeyResolverSpi {
  /**
   * {@link org.apache.commons.logging} logging facility
   */
  private static org.apache.commons.logging.Log log =
    org.apache.commons.logging.LogFactory.getLog(SecretKeyResolver.class.getName());

  private KeyStore keyStore;
  private char[] password;

  /**
   * Constructor.
   */
  public SecretKeyResolver(KeyStore keyStore, char[] password) {
    this.keyStore = keyStore;
    this.password = password;
  }

  /**
   * This method returns whether the KeyResolverSpi is able to perform the requested action.
   *
   * @param element
   * @param baseURI
   * @param storage
   * @return whether the KeyResolverSpi is able to perform the requested action.
   */
  public boolean engineCanResolve(Element element, String baseURI, StorageResolver storage) {
    return XMLUtils.elementIsInSignatureSpace(element, Constants._TAG_KEYNAME);
  }

  /**
   * Method engineLookupAndResolvePublicKey
   *
   * @param element
   * @param baseURI
   * @param storage
   * @return null if no {@link PublicKey} could be obtained
   * @throws KeyResolverException
   */
  public PublicKey engineLookupAndResolvePublicKey(
    Element element, String baseURI, StorageResolver storage
  ) throws KeyResolverException {
    return null;
  }

  /**
   * Method engineResolveX509Certificate
   *
   * @param element
   * @param baseURI
   * @param storage
   * @throws KeyResolverException
   * @inheritDoc
   */
  public X509Certificate engineLookupResolveX509Certificate(
    Element element, String baseURI, StorageResolver storage
  ) throws KeyResolverException {
    return null;
  }

  /**
   * Method engineResolveSecretKey
   *
   * @param element
   * @param baseURI
   * @param storage
   * @return resolved SecretKey key or null if no {@link SecretKey} could be obtained
   * @throws KeyResolverException
   */
  public SecretKey engineResolveSecretKey(
    Element element, String baseURI, StorageResolver storage
  ) throws KeyResolverException {
    if (log.isDebugEnabled()) {
      log.debug("Can I resolve " + element.getTagName() + "?");
    }

    if (XMLUtils.elementIsInSignatureSpace(element, Constants._TAG_KEYNAME)) {
      String keyName = element.getFirstChild().getNodeValue();
      try {
        Key key = keyStore.getKey(keyName, password);
        if (key instanceof SecretKey) {
          return (SecretKey) key;
        }
      } catch (Exception e) {
        log.debug("Cannot recover the key", e);
      }
    }

    log.debug("I can't");
    return null;
  }

  /**
   * Method engineResolvePrivateKey
   *
   * @param element
   * @param baseURI
   * @param storage
   * @return resolved PrivateKey key or null if no {@link PrivateKey} could be obtained
   * @throws KeyResolverException
   * @inheritDoc
   */
  public PrivateKey engineLookupAndResolvePrivateKey(
    Element element, String baseURI, StorageResolver storage
  ) throws KeyResolverException {
    return null;
  }
}
