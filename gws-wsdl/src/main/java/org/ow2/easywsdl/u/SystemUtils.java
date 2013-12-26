package org.ow2.easywsdl.u;

public class SystemUtils {


  /**
   * <p>
   * The <code>java.awt.headless</code> System Property.
   * The value of this property is the String <code>"true"</code> or <code>"false"</code>.
   * </p>
   * <p/>
   * <p>Defaults to <code>null</code> if the runtime does not have
   * security access to read this property or the property does not exist.</p>
   * <p/>
   * <p>
   * This value is initialized when the class is loaded. If {@link System#setProperty(String, String)}
   * or {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the value
   * will be out of sync with that System property.
   * </p>
   *
   * @see #isJavaAwtHeadless()
   * @since Java 1.4
   */
  public static final String JAVA_AWT_HEADLESS = getSystemProperty("java.awt.headless");


  /**
   * <p>The <code>line.separator</code> System Property. Line separator
   * (<code>&quot;\n&quot;</code> on UNIX).</p>
   * <p/>
   * <p>Defaults to <code>null</code> if the runtime does not have
   * security access to read this property or the property does not exist.</p>
   * <p/>
   * <p>
   * This value is initialized when the class is loaded. If {@link System#setProperty(String, String)}
   * or {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the value
   * will be out of sync with that System property.
   * </p>
   *
   * @since Java 1.1
   */
  public static final String LINE_SEPARATOR = getSystemProperty("line.separator");


  //-----------------------------------------------------------------------

  /**
   * <p>Gets a System property, defaulting to <code>null</code> if the property
   * cannot be read.</p>
   * <p/>
   * <p>If a <code>SecurityException</code> is caught, the return
   * value is <code>null</code> and a message is written to <code>System.err</code>.</p>
   *
   * @param property the system property name
   * @return the system property value or <code>null</code> if a security problem occurs
   */
  private static String getSystemProperty(String property) {
    try {
      return System.getProperty(property);
    } catch (SecurityException ex) {
      // we are not allowed to look at this property
      System.err.println(
        "Caught a SecurityException reading the system property '" + property
          + "'; the SystemUtils property value will default to null."
      );
      return null;
    }
  }

  /**
   * Returns whether the {@link #JAVA_AWT_HEADLESS} value is <code>true</code>.
   *
   * @return <code>true</code> if <code>JAVA_AWT_HEADLESS</code> is <code>"true"</code>,
   *         <code>false</code> otherwise.
   * @see #JAVA_AWT_HEADLESS
   * @since Java 1.4
   */
  public static boolean isJavaAwtHeadless() {
    return JAVA_AWT_HEADLESS != null ? JAVA_AWT_HEADLESS.equals(Boolean.TRUE.toString()) : false;
  }

}
