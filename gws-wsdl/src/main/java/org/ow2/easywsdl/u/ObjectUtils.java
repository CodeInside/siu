package org.ow2.easywsdl.u;

import java.io.Serializable;

public class ObjectUtils {

  /**
   * <p>Singleton used as a <code>null</code> placeholder where
   * <code>null</code> has another meaning.</p>
   * <p/>
   * <p>For example, in a <code>HashMap</code> the
   * {@link java.util.HashMap#get(java.lang.Object)} method returns
   * <code>null</code> if the <code>Map</code> contains
   * <code>null</code> or if there is no matching key. The
   * <code>Null</code> placeholder can be used to distinguish between
   * these two cases.</p>
   * <p/>
   * <p>Another example is <code>Hashtable</code>, where <code>null</code>
   * cannot be stored.</p>
   * <p/>
   * <p>This instance is Serializable.</p>
   */
  public static final Null NULL = new Null();


  // Defaulting
  //-----------------------------------------------------------------------


  /**
   * <p>Compares two objects for equality, where either one or both
   * objects may be <code>null</code>.</p>
   * <p/>
   * <pre>
   * ObjectUtils.equals(null, null)                  = true
   * ObjectUtils.equals(null, "")                    = false
   * ObjectUtils.equals("", null)                    = false
   * ObjectUtils.equals("", "")                      = true
   * ObjectUtils.equals(Boolean.TRUE, null)          = false
   * ObjectUtils.equals(Boolean.TRUE, "true")        = false
   * ObjectUtils.equals(Boolean.TRUE, Boolean.TRUE)  = true
   * ObjectUtils.equals(Boolean.TRUE, Boolean.FALSE) = false
   * </pre>
   *
   * @param object1 the first object, may be <code>null</code>
   * @param object2 the second object, may be <code>null</code>
   * @return <code>true</code> if the values of both objects are the same
   */
  public static boolean equals(Object object1, Object object2) {
    if (object1 == object2) {
      return true;
    }
    if ((object1 == null) || (object2 == null)) {
      return false;
    }
    return object1.equals(object2);
  }

  /**
   * <p>Gets the hash code of an object returning zero when the
   * object is <code>null</code>.</p>
   * <p/>
   * <pre>
   * ObjectUtils.hashCode(null)   = 0
   * ObjectUtils.hashCode(obj)    = obj.hashCode()
   * </pre>
   *
   * @param obj the object to obtain the hash code of, may be <code>null</code>
   * @return the hash code of the object, or zero if null
   * @since 2.1
   */
  public static int hashCode(Object obj) {
    return (obj == null) ? 0 : obj.hashCode();
  }

  /**
   * <p>Appends the toString that would be produced by <code>Object</code>
   * if a class did not override toString itself. <code>null</code>
   * will return <code>null</code>.</p>
   * <p/>
   * <pre>
   * ObjectUtils.appendIdentityToString(*, null)            = null
   * ObjectUtils.appendIdentityToString(null, "")           = "java.lang.String@1e23"
   * ObjectUtils.appendIdentityToString(null, Boolean.TRUE) = "java.lang.Boolean@7fa"
   * ObjectUtils.appendIdentityToString(buf, Boolean.TRUE)  = buf.append("java.lang.Boolean@7fa")
   * </pre>
   *
   * @param buffer the buffer to append to, may be <code>null</code>
   * @param object the object to create a toString for, may be <code>null</code>
   * @return the default toString text, or <code>null</code> if
   *         <code>null</code> passed in
   * @since 2.0
   * @deprecated The design of this method is bad - see LANG-360. Instead, use identityToString(StringBuffer, Object).
   */
  public static StringBuffer appendIdentityToString(StringBuffer buffer, Object object) {
    if (object == null) {
      return null;
    }
    if (buffer == null) {
      buffer = new StringBuffer();
    }
    return buffer
      .append(object.getClass().getName())
      .append('@')
      .append(Integer.toHexString(System.identityHashCode(object)));
  }

  // ToString
  //-----------------------------------------------------------------------

  /**
   * <p>Gets the <code>toString</code> of an <code>Object</code> returning
   * an empty string ("") if <code>null</code> input.</p>
   * <p/>
   * <pre>
   * ObjectUtils.toString(null)         = ""
   * ObjectUtils.toString("")           = ""
   * ObjectUtils.toString("bat")        = "bat"
   * ObjectUtils.toString(Boolean.TRUE) = "true"
   * </pre>
   *
   * @param obj the Object to <code>toString</code>, may be null
   * @return the passed in Object's toString, or nullStr if <code>null</code> input
   * @see StringUtils#defaultString(String)
   * @see String#valueOf(Object)
   * @since 2.0
   */
  public static String toString(Object obj) {
    return obj == null ? "" : obj.toString();
  }

  /**
   * <p>Gets the <code>toString</code> of an <code>Object</code> returning
   * a specified text if <code>null</code> input.</p>
   * <p/>
   * <pre>
   * ObjectUtils.toString(null, null)           = null
   * ObjectUtils.toString(null, "null")         = "null"
   * ObjectUtils.toString("", "null")           = ""
   * ObjectUtils.toString("bat", "null")        = "bat"
   * ObjectUtils.toString(Boolean.TRUE, "null") = "true"
   * </pre>
   *
   * @param obj     the Object to <code>toString</code>, may be null
   * @param nullStr the String to return if <code>null</code> input, may be null
   * @return the passed in Object's toString, or nullStr if <code>null</code> input
   * @see StringUtils#defaultString(String, String)
   * @see String#valueOf(Object)
   * @since 2.0
   */
  public static String toString(Object obj, String nullStr) {
    return obj == null ? nullStr : obj.toString();
  }

  // Min/Max
  //-----------------------------------------------------------------------

  /**
   * Null safe comparison of Comparables.
   *
   * @param c1 the first comparable, may be null
   * @param c2 the second comparable, may be null
   * @return <ul>
   *         <li>If both objects are non-null and unequal, the lesser object.
   *         <li>If both objects are non-null and equal, c1.
   *         <li>If one of the comparables is null, the non-null object.
   *         <li>If both the comparables are null, null is returned.
   *         </ul>
   */
  public static Object min(Comparable c1, Comparable c2) {
    if (c1 != null && c2 != null) {
      return c1.compareTo(c2) < 1 ? c1 : c2;
    } else {
      return c1 != null ? c1 : c2;
    }
  }

  /**
   * Null safe comparison of Comparables.
   *
   * @param c1 the first comparable, may be null
   * @param c2 the second comparable, may be null
   * @return <ul>
   *         <li>If both objects are non-null and unequal, the greater object.
   *         <li>If both objects are non-null and equal, c1.
   *         <li>If one of the comparables is null, the non-null object.
   *         <li>If both the comparables are null, null is returned.
   *         </ul>
   */
  public static Object max(Comparable c1, Comparable c2) {
    if (c1 != null && c2 != null) {
      return c1.compareTo(c2) >= 0 ? c1 : c2;
    } else {
      return c1 != null ? c1 : c2;
    }
  }

  // Null
  //-----------------------------------------------------------------------

  /**
   * <p>Class used as a null placeholder where <code>null</code>
   * has another meaning.</p>
   * <p/>
   * <p>For example, in a <code>HashMap</code> the
   * {@link java.util.HashMap#get(java.lang.Object)} method returns
   * <code>null</code> if the <code>Map</code> contains
   * <code>null</code> or if there is no matching key. The
   * <code>Null</code> placeholder can be used to distinguish between
   * these two cases.</p>
   * <p/>
   * <p>Another example is <code>Hashtable</code>, where <code>null</code>
   * cannot be stored.</p>
   */
  public static class Null implements Serializable {
    /**
     * Required for serialization support. Declare serialization compatibility with Commons Lang 1.0
     *
     * @see java.io.Serializable
     */
    private static final long serialVersionUID = 7092611880189329093L;

    /**
     * Restricted constructor - singleton.
     */
    Null() {
      super();
    }

    /**
     * <p>Ensure singleton.</p>
     *
     * @return the singleton value
     */
    private Object readResolve() {
      return ObjectUtils.NULL;
    }
  }

}
