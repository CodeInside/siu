package org.ow2.easywsdl.u;

public class NullArgumentException extends IllegalArgumentException {

  /**
   * Required for serialization support.
   *
   * @see java.io.Serializable
   */
  private static final long serialVersionUID = 1174360235354917591L;

  /**
   * <p>Instantiates with the given argument name.</p>
   *
   * @param argName the name of the argument that was <code>null</code>.
   */
  public NullArgumentException(String argName) {
    super((argName == null ? "Argument" : argName) + " must not be null.");
  }

}
