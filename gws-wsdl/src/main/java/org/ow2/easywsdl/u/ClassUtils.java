package org.ow2.easywsdl.u;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class ClassUtils {

  /**
   * <p>The package separator character: <code>'&#x2e;' == {@value}</code>.</p>
   */
  public static final char PACKAGE_SEPARATOR_CHAR = '.';

  /**
   * <p>The inner class separator character: <code>'$' == {@value}</code>.</p>
   */
  public static final char INNER_CLASS_SEPARATOR_CHAR = '$';


  /**
   * Maps primitive <code>Class</code>es to their corresponding wrapper <code>Class</code>.
   */
  private static Map primitiveWrapperMap = new HashMap();

  static {
    primitiveWrapperMap.put(Boolean.TYPE, Boolean.class);
    primitiveWrapperMap.put(Byte.TYPE, Byte.class);
    primitiveWrapperMap.put(Character.TYPE, Character.class);
    primitiveWrapperMap.put(Short.TYPE, Short.class);
    primitiveWrapperMap.put(Integer.TYPE, Integer.class);
    primitiveWrapperMap.put(Long.TYPE, Long.class);
    primitiveWrapperMap.put(Double.TYPE, Double.class);
    primitiveWrapperMap.put(Float.TYPE, Float.class);
    primitiveWrapperMap.put(Void.TYPE, Void.TYPE);
  }

  /**
   * Maps wrapper <code>Class</code>es to their corresponding primitive types.
   */
  private static Map wrapperPrimitiveMap = new HashMap();

  static {
    for (Iterator it = primitiveWrapperMap.keySet().iterator(); it.hasNext(); ) {
      Class primitiveClass = (Class) it.next();
      Class wrapperClass = (Class) primitiveWrapperMap.get(primitiveClass);
      if (!primitiveClass.equals(wrapperClass)) {
        wrapperPrimitiveMap.put(wrapperClass, primitiveClass);
      }
    }
  }

  /**
   * Maps a primitive class name to its corresponding abbreviation used in array class names.
   */
  private static Map abbreviationMap = new HashMap();

  /**
   * Maps an abbreviation used in array class names to corresponding primitive class name.
   */
  private static Map reverseAbbreviationMap = new HashMap();

  /**
   * Add primitive type abbreviation to maps of abbreviations.
   *
   * @param primitive    Canonical name of primitive type
   * @param abbreviation Corresponding abbreviation of primitive type
   */
  private static void addAbbreviation(String primitive, String abbreviation) {
    abbreviationMap.put(primitive, abbreviation);
    reverseAbbreviationMap.put(abbreviation, primitive);
  }

  /**
   * Feed abbreviation maps
   */
  static {
    addAbbreviation("int", "I");
    addAbbreviation("boolean", "Z");
    addAbbreviation("float", "F");
    addAbbreviation("long", "J");
    addAbbreviation("short", "S");
    addAbbreviation("byte", "B");
    addAbbreviation("double", "D");
    addAbbreviation("char", "C");
  }


  // Short class name
  // ----------------------------------------------------------------------

  /**
   * <p>Gets the class name minus the package name for an <code>Object</code>.</p>
   *
   * @param object      the class to get the short name for, may be null
   * @param valueIfNull the value to return if null
   * @return the class name of the object without the package name, or the null value
   */
  public static String getShortClassName(Object object, String valueIfNull) {
    if (object == null) {
      return valueIfNull;
    }
    return getShortClassName(object.getClass().getName());
  }

  /**
   * <p>Gets the class name minus the package name from a <code>Class</code>.</p>
   *
   * @param cls the class to get the short name for.
   * @return the class name without the package name or an empty string
   */
  public static String getShortClassName(Class cls) {
    if (cls == null) {
      return StringUtils.EMPTY;
    }
    return getShortClassName(cls.getName());
  }

  /**
   * <p>Gets the class name minus the package name from a String.</p>
   * <p/>
   * <p>The string passed in is assumed to be a class name - it is not checked.</p>
   *
   * @param className the className to get the short name for
   * @return the class name of the class without the package name or an empty string
   */
  public static String getShortClassName(String className) {
    if (className == null) {
      return StringUtils.EMPTY;
    }
    if (className.length() == 0) {
      return StringUtils.EMPTY;
    }

    int lastDotIdx = className.lastIndexOf(PACKAGE_SEPARATOR_CHAR);
    int innerIdx = className.indexOf(
      INNER_CLASS_SEPARATOR_CHAR, lastDotIdx == -1 ? 0 : lastDotIdx + 1);
    String out = className.substring(lastDotIdx + 1);
    if (innerIdx != -1) {
      out = out.replace(INNER_CLASS_SEPARATOR_CHAR, PACKAGE_SEPARATOR_CHAR);
    }
    return out;
  }


  // Class loading
  // ----------------------------------------------------------------------

  /**
   * Returns the class represented by <code>className</code> using the
   * <code>classLoader</code>.  This implementation supports names like
   * "<code>java.lang.String[]</code>" as well as "<code>[Ljava.lang.String;</code>".
   *
   * @param classLoader the class loader to use to load the class
   * @param className   the class name
   * @param initialize  whether the class must be initialized
   * @return the class represented by <code>className</code> using the <code>classLoader</code>
   * @throws ClassNotFoundException if the class is not found
   */
  public static Class getClass(
    ClassLoader classLoader, String className, boolean initialize) throws ClassNotFoundException {
    Class clazz;
    if (abbreviationMap.containsKey(className)) {
      String clsName = "[" + abbreviationMap.get(className);
      clazz = Class.forName(clsName, initialize, classLoader).getComponentType();
    } else {
      clazz = Class.forName(toCanonicalName(className), initialize, classLoader);
    }
    return clazz;
  }

  /**
   * Returns the (initialized) class represented by <code>className</code>
   * using the <code>classLoader</code>.  This implementation supports names
   * like "<code>java.lang.String[]</code>" as well as
   * "<code>[Ljava.lang.String;</code>".
   *
   * @param classLoader the class loader to use to load the class
   * @param className   the class name
   * @return the class represented by <code>className</code> using the <code>classLoader</code>
   * @throws ClassNotFoundException if the class is not found
   */
  public static Class getClass(ClassLoader classLoader, String className) throws ClassNotFoundException {
    return getClass(classLoader, className, true);
  }

  /**
   * Returns the (initialized )class represented by <code>className</code>
   * using the current thread's context class loader. This implementation
   * supports names like "<code>java.lang.String[]</code>" as well as
   * "<code>[Ljava.lang.String;</code>".
   *
   * @param className the class name
   * @return the class represented by <code>className</code> using the current thread's context class loader
   * @throws ClassNotFoundException if the class is not found
   */
  public static Class getClass(String className) throws ClassNotFoundException {
    return getClass(className, true);
  }

  /**
   * Returns the class represented by <code>className</code> using the
   * current thread's context class loader. This implementation supports
   * names like "<code>java.lang.String[]</code>" as well as
   * "<code>[Ljava.lang.String;</code>".
   *
   * @param className  the class name
   * @param initialize whether the class must be initialized
   * @return the class represented by <code>className</code> using the current thread's context class loader
   * @throws ClassNotFoundException if the class is not found
   */
  public static Class getClass(String className, boolean initialize) throws ClassNotFoundException {
    ClassLoader contextCL = Thread.currentThread().getContextClassLoader();
    ClassLoader loader = contextCL == null ? ClassUtils.class.getClassLoader() : contextCL;
    return getClass(loader, className, initialize);
  }


  // ----------------------------------------------------------------------

  /**
   * Converts a class name to a JLS style class name.
   *
   * @param className the class name
   * @return the converted name
   */
  private static String toCanonicalName(String className) {
    className = StringUtils.deleteWhitespace(className);
    if (className == null) {
      throw new NullArgumentException("className");
    } else if (className.endsWith("[]")) {
      StringBuffer classNameBuffer = new StringBuffer();
      while (className.endsWith("[]")) {
        className = className.substring(0, className.length() - 2);
        classNameBuffer.append("[");
      }
      String abbreviation = (String) abbreviationMap.get(className);
      if (abbreviation != null) {
        classNameBuffer.append(abbreviation);
      } else {
        classNameBuffer.append("L").append(className).append(";");
      }
      className = classNameBuffer.toString();
    }
    return className;
  }

}
