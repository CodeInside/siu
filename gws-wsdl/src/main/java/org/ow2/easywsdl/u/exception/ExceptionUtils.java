package org.ow2.easywsdl.u.exception;

import org.ow2.easywsdl.u.ArrayUtils;
import org.ow2.easywsdl.u.ClassUtils;
import org.ow2.easywsdl.u.StringUtils;
import org.ow2.easywsdl.u.SystemUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class ExceptionUtils {

  /**
   * <p>The names of methods commonly used to access a wrapped exception.</p>
   */
  private static String[] CAUSE_METHOD_NAMES = {
    "getCause",
    "getNextException",
    "getTargetException",
    "getException",
    "getSourceException",
    "getRootCause",
    "getCausedByException",
    "getNested",
    "getLinkedException",
    "getNestedException",
    "getLinkedCause",
    "getThrowable",
  };

  /**
   * <p>The Method object for Java 1.4 getCause.</p>
   */
  private static final Method THROWABLE_CAUSE_METHOD;


  static {
    Method causeMethod;
    try {
      causeMethod = Throwable.class.getMethod("getCause", null);
    } catch (Exception e) {
      causeMethod = null;
    }
    THROWABLE_CAUSE_METHOD = causeMethod;
  }


  /**
   * Returns the given list as a <code>String[]</code>.
   *
   * @param list a list to transform.
   * @return the given list as a <code>String[]</code>.
   */
  private static String[] toArray(List list) {
    return (String[]) list.toArray(new String[list.size()]);
  }

  //-----------------------------------------------------------------------

  /**
   * <p>Introspects the <code>Throwable</code> to obtain the cause.</p>
   * <p/>
   * <p>The method searches for methods with specific names that return a
   * <code>Throwable</code> object. This will pick up most wrapping exceptions,
   * including those from JDK 1.4, and
   * {@link org.ow2.easywsdl.u.exception.NestableException NestableException}.
   * <p/>
   * <p>The default list searched for are:</p>
   * <ul>
   * <li><code>getCause()</code></li>
   * <li><code>getNextException()</code></li>
   * <li><code>getTargetException()</code></li>
   * <li><code>getException()</code></li>
   * <li><code>getSourceException()</code></li>
   * <li><code>getRootCause()</code></li>
   * <li><code>getCausedByException()</code></li>
   * <li><code>getNested()</code></li>
   * </ul>
   * <p/>
   * <p>In the absence of any such method, the object is inspected for a
   * <code>detail</code> field assignable to a <code>Throwable</code>.</p>
   * <p/>
   * <p>If none of the above is found, returns <code>null</code>.</p>
   *
   * @param throwable the throwable to introspect for a cause, may be null
   * @return the cause of the <code>Throwable</code>,
   *         <code>null</code> if none found or null throwable input
   * @since 1.0
   */
  public static Throwable getCause(Throwable throwable) {
    synchronized (CAUSE_METHOD_NAMES) {
      return getCause(throwable, CAUSE_METHOD_NAMES);
    }
  }

  /**
   * <p>Introspects the <code>Throwable</code> to obtain the cause.</p>
   * <p/>
   * <ol>
   * <li>Try known exception types.</li>
   * <li>Try the supplied array of method names.</li>
   * <li>Try the field 'detail'.</li>
   * </ol>
   * <p/>
   * <p>A <code>null</code> set of method names means use the default set.
   * A <code>null</code> in the set of method names will be ignored.</p>
   *
   * @param throwable   the throwable to introspect for a cause, may be null
   * @param methodNames the method names, null treated as default set
   * @return the cause of the <code>Throwable</code>,
   *         <code>null</code> if none found or null throwable input
   * @since 1.0
   */
  public static Throwable getCause(Throwable throwable, String[] methodNames) {
    if (throwable == null) {
      return null;
    }
    Throwable cause = getCauseUsingWellKnownTypes(throwable);
    if (cause == null) {
      if (methodNames == null) {
        synchronized (CAUSE_METHOD_NAMES) {
          methodNames = CAUSE_METHOD_NAMES;
        }
      }
      for (int i = 0; i < methodNames.length; i++) {
        String methodName = methodNames[i];
        if (methodName != null) {
          cause = getCauseUsingMethodName(throwable, methodName);
          if (cause != null) {
            break;
          }
        }
      }

      if (cause == null) {
        cause = getCauseUsingFieldName(throwable, "detail");
      }
    }
    return cause;
  }


  /**
   * <p>Finds a <code>Throwable</code> for known types.</p>
   * <p/>
   * <p>Uses <code>instanceof</code> checks to examine the exception,
   * looking for well known types which could contain chained or
   * wrapped exceptions.</p>
   *
   * @param throwable the exception to examine
   * @return the wrapped exception, or <code>null</code> if not found
   */
  private static Throwable getCauseUsingWellKnownTypes(Throwable throwable) {
    if (throwable instanceof Nestable) {
      return ((Nestable) throwable).getCause();
    } else if (throwable instanceof SQLException) {
      return ((SQLException) throwable).getNextException();
    } else if (throwable instanceof InvocationTargetException) {
      return ((InvocationTargetException) throwable).getTargetException();
    } else {
      return null;
    }
  }

  /**
   * <p>Finds a <code>Throwable</code> by method name.</p>
   *
   * @param throwable  the exception to examine
   * @param methodName the name of the method to find and invoke
   * @return the wrapped exception, or <code>null</code> if not found
   */
  private static Throwable getCauseUsingMethodName(Throwable throwable, String methodName) {
    Method method = null;
    try {
      method = throwable.getClass().getMethod(methodName, null);
    } catch (NoSuchMethodException ignored) {
      // exception ignored
    } catch (SecurityException ignored) {
      // exception ignored
    }

    if (method != null && Throwable.class.isAssignableFrom(method.getReturnType())) {
      try {
        return (Throwable) method.invoke(throwable, ArrayUtils.EMPTY_OBJECT_ARRAY);
      } catch (IllegalAccessException ignored) {
        // exception ignored
      } catch (IllegalArgumentException ignored) {
        // exception ignored
      } catch (InvocationTargetException ignored) {
        // exception ignored
      }
    }
    return null;
  }

  /**
   * <p>Finds a <code>Throwable</code> by field name.</p>
   *
   * @param throwable the exception to examine
   * @param fieldName the name of the attribute to examine
   * @return the wrapped exception, or <code>null</code> if not found
   */
  private static Throwable getCauseUsingFieldName(Throwable throwable, String fieldName) {
    Field field = null;
    try {
      field = throwable.getClass().getField(fieldName);
    } catch (NoSuchFieldException ignored) {
      // exception ignored
    } catch (SecurityException ignored) {
      // exception ignored
    }

    if (field != null && Throwable.class.isAssignableFrom(field.getType())) {
      try {
        return (Throwable) field.get(throwable);
      } catch (IllegalAccessException ignored) {
        // exception ignored
      } catch (IllegalArgumentException ignored) {
        // exception ignored
      }
    }
    return null;
  }

  //-----------------------------------------------------------------------

  /**
   * <p>Checks if the Throwable class has a <code>getCause</code> method.</p>
   * <p/>
   * <p>This is true for JDK 1.4 and above.</p>
   *
   * @return true if Throwable is nestable
   * @since 2.0
   */
  public static boolean isThrowableNested() {
    return THROWABLE_CAUSE_METHOD != null;
  }


  //-----------------------------------------------------------------------

  /**
   * <p>Counts the number of <code>Throwable</code> objects in the
   * exception chain.</p>
   * <p/>
   * <p>A throwable without cause will return <code>1</code>.
   * A throwable with one cause will return <code>2</code> and so on.
   * A <code>null</code> throwable will return <code>0</code>.</p>
   * <p/>
   * <p>From version 2.2, this method handles recursive cause structures
   * that might otherwise cause infinite loops. The cause chain is
   * processed until the end is reached, or until the next item in the
   * chain is already in the result set.</p>
   *
   * @param throwable the throwable to inspect, may be null
   * @return the count of throwables, zero if null input
   */
  public static int getThrowableCount(Throwable throwable) {
    return getThrowableList(throwable).size();
  }

  /**
   * <p>Returns the list of <code>Throwable</code> objects in the
   * exception chain.</p>
   * <p/>
   * <p>A throwable without cause will return an array containing
   * one element - the input throwable.
   * A throwable with one cause will return an array containing
   * two elements. - the input throwable and the cause throwable.
   * A <code>null</code> throwable will return an array of size zero.</p>
   * <p/>
   * <p>From version 2.2, this method handles recursive cause structures
   * that might otherwise cause infinite loops. The cause chain is
   * processed until the end is reached, or until the next item in the
   * chain is already in the result set.</p>
   *
   * @param throwable the throwable to inspect, may be null
   * @return the array of throwables, never null
   * @see #getThrowableList(Throwable)
   */
  public static Throwable[] getThrowables(Throwable throwable) {
    List list = getThrowableList(throwable);
    return (Throwable[]) list.toArray(new Throwable[list.size()]);
  }

  /**
   * <p>Returns the list of <code>Throwable</code> objects in the
   * exception chain.</p>
   * <p/>
   * <p>A throwable without cause will return a list containing
   * one element - the input throwable.
   * A throwable with one cause will return a list containing
   * two elements. - the input throwable and the cause throwable.
   * A <code>null</code> throwable will return a list of size zero.</p>
   * <p/>
   * <p>This method handles recursive cause structures that might
   * otherwise cause infinite loops. The cause chain is processed until
   * the end is reached, or until the next item in the chain is already
   * in the result set.</p>
   *
   * @param throwable the throwable to inspect, may be null
   * @return the list of throwables, never null
   * @since Commons Lang 2.2
   */
  public static List getThrowableList(Throwable throwable) {
    List list = new ArrayList();
    while (throwable != null && list.contains(throwable) == false) {
      list.add(throwable);
      throwable = ExceptionUtils.getCause(throwable);
    }
    return list;
  }

  //-----------------------------------------------------------------------

  /**
   * <p>Returns the (zero based) index of the first <code>Throwable</code>
   * that matches the specified class (exactly) in the exception chain.
   * Subclasses of the specified class do not match - see
   * <p/>
   * <p>A <code>null</code> throwable returns <code>-1</code>.
   * A <code>null</code> type returns <code>-1</code>.
   * No match in the chain returns <code>-1</code>.</p>
   *
   * @param throwable the throwable to inspect, may be null
   * @param clazz     the class to search for, subclasses do not match, null returns -1
   * @return the index into the throwable chain, -1 if no match or null input
   */
  public static int indexOfThrowable(Throwable throwable, Class clazz) {
    return indexOf(throwable, clazz, 0, false);
  }

  /**
   * <p>Returns the (zero based) index of the first <code>Throwable</code>
   * that matches the specified type in the exception chain from
   * a specified index.
   * Subclasses of the specified class do not match - see
   * <p/>
   * <p>A <code>null</code> throwable returns <code>-1</code>.
   * A <code>null</code> type returns <code>-1</code>.
   * No match in the chain returns <code>-1</code>.
   * A negative start index is treated as zero.
   * A start index greater than the number of throwables returns <code>-1</code>.</p>
   *
   * @param throwable the throwable to inspect, may be null
   * @param clazz     the class to search for, subclasses do not match, null returns -1
   * @param fromIndex the (zero based) index of the starting position,
   *                  negative treated as zero, larger than chain size returns -1
   * @return the index into the throwable chain, -1 if no match or null input
   */
  public static int indexOfThrowable(Throwable throwable, Class clazz, int fromIndex) {
    return indexOf(throwable, clazz, fromIndex, false);
  }

  /**
   * <p>Worker method for the <code>indexOfType</code> methods.</p>
   *
   * @param throwable the throwable to inspect, may be null
   * @param type      the type to search for, subclasses match, null returns -1
   * @param fromIndex the (zero based) index of the starting position,
   *                  negative treated as zero, larger than chain size returns -1
   * @param subclass  if <code>true</code>, compares with {@link Class#isAssignableFrom(Class)}, otherwise compares
   *                  using references
   * @return index of the <code>type</code> within throwables nested withing the specified <code>throwable</code>
   */
  private static int indexOf(Throwable throwable, Class type, int fromIndex, boolean subclass) {
    if (throwable == null || type == null) {
      return -1;
    }
    if (fromIndex < 0) {
      fromIndex = 0;
    }
    Throwable[] throwables = ExceptionUtils.getThrowables(throwable);
    if (fromIndex >= throwables.length) {
      return -1;
    }
    if (subclass) {
      for (int i = fromIndex; i < throwables.length; i++) {
        if (type.isAssignableFrom(throwables[i].getClass())) {
          return i;
        }
      }
    } else {
      for (int i = fromIndex; i < throwables.length; i++) {
        if (type.equals(throwables[i].getClass())) {
          return i;
        }
      }
    }
    return -1;
  }

  /**
   * <p>Removes common frames from the cause trace given the two stack traces.</p>
   *
   * @param causeFrames   stack trace of a cause throwable
   * @param wrapperFrames stack trace of a wrapper throwable
   * @throws IllegalArgumentException if either argument is null
   * @since 2.0
   */
  public static void removeCommonFrames(List causeFrames, List wrapperFrames) {
    if (causeFrames == null || wrapperFrames == null) {
      throw new IllegalArgumentException("The List must not be null");
    }
    int causeFrameIndex = causeFrames.size() - 1;
    int wrapperFrameIndex = wrapperFrames.size() - 1;
    while (causeFrameIndex >= 0 && wrapperFrameIndex >= 0) {
      // Remove the frame from the cause trace if it is the same
      // as in the wrapper trace
      String causeFrame = (String) causeFrames.get(causeFrameIndex);
      String wrapperFrame = (String) wrapperFrames.get(wrapperFrameIndex);
      if (causeFrame.equals(wrapperFrame)) {
        causeFrames.remove(causeFrameIndex);
      }
      causeFrameIndex--;
      wrapperFrameIndex--;
    }
  }


  //-----------------------------------------------------------------------

  /**
   * <p>Returns an array where each element is a line from the argument.</p>
   * <p/>
   * <p>The end of line is determined by the value of {@link SystemUtils#LINE_SEPARATOR}.</p>
   * <p/>
   * <p>Functionality shared between the
   * <code>getStackFrames(Throwable)</code> methods of this and the
   * {@link org.ow2.easywsdl.u.exception.NestableDelegate} classes.</p>
   *
   * @param stackTrace a stack trace String
   * @return an array where each element is a line from the argument
   */
  static String[] getStackFrames(String stackTrace) {
    String linebreak = SystemUtils.LINE_SEPARATOR;
    StringTokenizer frames = new StringTokenizer(stackTrace, linebreak);
    List list = new ArrayList();
    while (frames.hasMoreTokens()) {
      list.add(frames.nextToken());
    }
    return toArray(list);
  }


  //-----------------------------------------------------------------------

  /**
   * Gets a short message summarising the exception.
   * <p/>
   * The message returned is of the form
   * {ClassNameWithoutPackage}: {ThrowableMessage}
   *
   * @param th the throwable to get a message for, null returns empty string
   * @return the message, non-null
   * @since Commons Lang 2.2
   */
  public static String getMessage(Throwable th) {
    if (th == null) {
      return "";
    }
    String clsName = ClassUtils.getShortClassName(th, null);
    String msg = th.getMessage();
    return clsName + ": " + StringUtils.defaultString(msg);
  }


}
