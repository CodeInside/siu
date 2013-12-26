package org.ow2.easywsdl.u;

public class BooleanUtils {


  //-----------------------------------------------------------------------

  /**
   * <p>Boolean factory that avoids creating new Boolean objecs all the time.</p>
   * <p/>
   * <p>This method was added to JDK1.4 but is available here for earlier JDKs.</p>
   * <p/>
   * <pre>
   *   BooleanUtils.toBooleanObject(false) = Boolean.FALSE
   *   BooleanUtils.toBooleanObject(true)  = Boolean.TRUE
   * </pre>
   *
   * @param bool the boolean to convert
   * @return Boolean.TRUE or Boolean.FALSE as appropriate
   */
  public static Boolean toBooleanObject(boolean bool) {
    return bool ? Boolean.TRUE : Boolean.FALSE;
  }


  /**
   * <p>Converts a Boolean to a String returning one of the input Strings.</p>
   * <p/>
   * <pre>
   *   BooleanUtils.toString(Boolean.TRUE, "true", "false", null)   = "true"
   *   BooleanUtils.toString(Boolean.FALSE, "true", "false", null)  = "false"
   *   BooleanUtils.toString(null, "true", "false", null)           = null;
   * </pre>
   *
   * @param bool        the Boolean to check
   * @param trueString  the String to return if <code>true</code>,
   *                    may be <code>null</code>
   * @param falseString the String to return if <code>false</code>,
   *                    may be <code>null</code>
   * @param nullString  the String to return if <code>null</code>,
   *                    may be <code>null</code>
   * @return one of the three input Strings
   */
  public static String toString(Boolean bool, String trueString, String falseString, String nullString) {
    if (bool == null) {
      return nullString;
    }
    return bool.booleanValue() ? trueString : falseString;
  }


  /**
   * <p>Converts a boolean to a String returning one of the input Strings.</p>
   * <p/>
   * <pre>
   *   BooleanUtils.toString(true, "true", "false")   = "true"
   *   BooleanUtils.toString(false, "true", "false")  = "false"
   * </pre>
   *
   * @param bool        the Boolean to check
   * @param trueString  the String to return if <code>true</code>,
   *                    may be <code>null</code>
   * @param falseString the String to return if <code>false</code>,
   *                    may be <code>null</code>
   * @return one of the two input Strings
   */
  public static String toString(boolean bool, String trueString, String falseString) {
    return bool ? trueString : falseString;
  }

}
