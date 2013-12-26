package org.apache.xml.security.algorithms.implementations;

import org.apache.xml.security.algorithms.JCEMapper;
import org.apache.xml.security.algorithms.SignatureAlgorithmSpi;
import org.apache.xml.security.signature.XMLSignatureException;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.AlgorithmParameterSpec;

final public class SignatureGost extends SignatureAlgorithmSpi {

  private static org.apache.commons.logging.Log log =
    org.apache.commons.logging.LogFactory.getLog(SignatureGost.class);

  public static final String URI = "http://www.w3.org/2001/04/xmldsig-more#gostr34102001-gostr3411";

  private final Signature signature;

  public SignatureGost() throws XMLSignatureException {
    String algorithmID = JCEMapper.translateURItoJCEID(this.engineGetURI());
    if (log.isDebugEnabled()) {
      log.debug("Created SignatureGost using " + algorithmID);
    }
    String provider = JCEMapper.getProviderId();
    try {
      if (provider == null) {
        signature = Signature.getInstance(algorithmID);
      } else {
        signature = Signature.getInstance(algorithmID, provider);
      }
    } catch (java.security.NoSuchAlgorithmException ex) {
      Object[] exArgs = {algorithmID, ex.getLocalizedMessage()};
      throw new XMLSignatureException("algorithms.NoSuchAlgorithm", exArgs);
    } catch (NoSuchProviderException ex) {
      Object[] exArgs = {algorithmID, ex.getLocalizedMessage()};
      throw new XMLSignatureException("algorithms.NoSuchAlgorithm", exArgs);
    }
  }

  @Override
  protected String engineGetURI() {
    return URI;
  }

  @Override
  protected String engineGetJCEAlgorithmString() {
    return signature.getAlgorithm();
  }

  @Override
  protected void engineSetParameter(AlgorithmParameterSpec paramAlgorithmParameterSpec) throws XMLSignatureException {
    try {
      signature.setParameter(paramAlgorithmParameterSpec);
    } catch (InvalidAlgorithmParameterException x) {
      throw new XMLSignatureException("empty", x);
    }
  }

  @Override
  protected boolean engineVerify(byte[] paramArrayOfByte) throws XMLSignatureException {
    try {
      return signature.verify(paramArrayOfByte);
    } catch (SignatureException e) {
      throw new XMLSignatureException("empty", e);
    }
  }

  @Override
  protected void engineInitVerify(Key paramKey) throws XMLSignatureException {
    if (!(paramKey instanceof PublicKey)) {
      throw new XMLSignatureException("algorithms.WrongKeyForThisOperation", new Object[]{paramKey.getClass().getName(), PublicKey.class.getName()});
    }
    try {
      signature.initVerify((PublicKey) paramKey);
    } catch (InvalidKeyException e) {
      throw new XMLSignatureException("empty", e);
    }
  }

  @Override
  protected byte[] engineSign() throws XMLSignatureException {
    try {
      return signature.sign();
    } catch (SignatureException e) {
      throw new XMLSignatureException("empty", e);
    }
  }

  @Override
  protected void engineInitSign(Key paramKey, SecureRandom paramSecureRandom) throws XMLSignatureException {
    if (!(paramKey instanceof PrivateKey)) {
      throw new XMLSignatureException("algorithms.WrongKeyForThisOperation", new Object[]{paramKey.getClass().getName(), PrivateKey.class.getName()});
    }
    try {
      signature.initSign((PrivateKey) paramKey, paramSecureRandom);
    } catch (InvalidKeyException e) {
      throw new XMLSignatureException("empty", e);
    }
  }

  @Override
  protected void engineInitSign(Key paramKey) throws XMLSignatureException {
    if (!(paramKey instanceof PrivateKey)) {
      throw new XMLSignatureException("algorithms.WrongKeyForThisOperation", new Object[]{paramKey.getClass().getName(), PrivateKey.class.getName()});
    }
    try {
      signature.initSign((PrivateKey) paramKey);
    } catch (InvalidKeyException e) {
      throw new XMLSignatureException("empty", e);
    }
  }

  @Override
  protected void engineUpdate(byte[] paramArrayOfByte) throws XMLSignatureException {
    try {
      signature.update(paramArrayOfByte);
    } catch (SignatureException e) {
      throw new XMLSignatureException("empty", e);
    }
  }

  @Override
  protected void engineUpdate(byte paramByte) throws XMLSignatureException {
    try {
      signature.update(paramByte);
    } catch (SignatureException e) {
      throw new XMLSignatureException("empty", e);
    }
  }

  @Override
  protected void engineUpdate(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws XMLSignatureException {
    try {
      signature.update(paramArrayOfByte, paramInt1, paramInt2);
    } catch (SignatureException e) {
      throw new XMLSignatureException("empty", e);
    }
  }

  @Override
  protected String engineGetJCEProviderName() {
    return signature.getProvider().getName();
  }

  @Override
  protected void engineSetHMACOutputLength(int paramInt) throws XMLSignatureException {
    throw new XMLSignatureException("algorithms.HMACOutputLengthOnlyForHMAC");
  }

  @Override
  protected void engineInitSign(Key paramKey, AlgorithmParameterSpec paramAlgorithmParameterSpec) throws XMLSignatureException {
    throw new XMLSignatureException("algorithms.CannotUseAlgorithmParameterSpecOnSignatureGostR34102001");
  }

}
