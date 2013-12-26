package org.ow2.easywsdl.u;

import org.ow2.easywsdl.u.builder.EqualsBuilder;
import org.ow2.easywsdl.u.builder.HashCodeBuilder;
import org.ow2.easywsdl.u.builder.ToStringBuilder;
import org.ow2.easywsdl.u.builder.ToStringStyle;

import java.lang.reflect.Array;

public class ArrayUtils {

  /**
   * An empty immutable <code>Object</code> array.
   */
  public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

  /**
   * An empty immutable <code>String</code> array.
   */
  public static final String[] EMPTY_STRING_ARRAY = new String[0];

  /**
   * The index value when an element is not found in a list or array: <code>-1</code>.
   * This value is returned by methods in this class and can also be used in comparisons with values returned by
   * various method from {@link java.util.List}.
   */
  public static final int INDEX_NOT_FOUND = -1;


  // Basic methods handling multi-dimensional arrays
  //-----------------------------------------------------------------------

  /**
   * <p>Outputs an array as a String, treating <code>null</code> as an empty array.</p>
   * <p/>
   * <p>Multi-dimensional arrays are handled correctly, including
   * multi-dimensional primitive arrays.</p>
   * <p/>
   * <p>The format is that of Java source code, for example <code>{a,b}</code>.</p>
   *
   * @param array the array to get a toString for, may be <code>null</code>
   * @return a String representation of the array, '{}' if null array input
   */
  public static String toString(Object array) {
    return toString(array, "{}");
  }

  /**
   * <p>Outputs an array as a String handling <code>null</code>s.</p>
   * <p/>
   * <p>Multi-dimensional arrays are handled correctly, including
   * multi-dimensional primitive arrays.</p>
   * <p/>
   * <p>The format is that of Java source code, for example <code>{a,b}</code>.</p>
   *
   * @param array        the array to get a toString for, may be <code>null</code>
   * @param stringIfNull the String to return if the array is <code>null</code>
   * @return a String representation of the array
   */
  public static String toString(Object array, String stringIfNull) {
    if (array == null) {
      return stringIfNull;
    }
    return new ToStringBuilder(array, ToStringStyle.SIMPLE_STYLE).append(array).toString();
  }

  /**
   * <p>Get a hashCode for an array handling multi-dimensional arrays correctly.</p>
   * <p/>
   * <p>Multi-dimensional primitive arrays are also handled correctly by this method.</p>
   *
   * @param array the array to get a hashCode for, may be <code>null</code>
   * @return a hashCode for the array, zero if null array input
   */
  public static int hashCode(Object array) {
    return new HashCodeBuilder().append(array).toHashCode();
  }

  /**
   * <p>Compares two arrays, using equals(), handling multi-dimensional arrays
   * correctly.</p>
   * <p/>
   * <p>Multi-dimensional primitive arrays are also handled correctly by this method.</p>
   *
   * @param array1 the left hand array to compare, may be <code>null</code>
   * @param array2 the right hand array to compare, may be <code>null</code>
   * @return <code>true</code> if the arrays are equal
   */
  public static boolean isEquals(Object array1, Object array2) {
    return new EqualsBuilder().append(array1, array2).isEquals();
  }


  // Clone
  //-----------------------------------------------------------------------

  /**
   * <p>Shallow clones an array returning a typecast result and handling
   * <code>null</code>.</p>
   * <p/>
   * <p>The objects in the array are not cloned, thus there is no special
   * handling for multi-dimensional arrays.</p>
   * <p/>
   * <p>This method returns <code>null</code> for a <code>null</code> input array.</p>
   *
   * @param array the array to shallow clone, may be <code>null</code>
   * @return the cloned array, <code>null</code> if <code>null</code> input
   */
  public static Object[] clone(Object[] array) {
    if (array == null) {
      return null;
    }
    return (Object[]) array.clone();
  }

  /**
   * <p>Clones an array returning a typecast result and handling
   * <code>null</code>.</p>
   * <p/>
   * <p>This method returns <code>null</code> for a <code>null</code> input array.</p>
   *
   * @param array the array to clone, may be <code>null</code>
   * @return the cloned array, <code>null</code> if <code>null</code> input
   */
  public static long[] clone(long[] array) {
    if (array == null) {
      return null;
    }
    return (long[]) array.clone();
  }

  /**
   * <p>Clones an array returning a typecast result and handling
   * <code>null</code>.</p>
   * <p/>
   * <p>This method returns <code>null</code> for a <code>null</code> input array.</p>
   *
   * @param array the array to clone, may be <code>null</code>
   * @return the cloned array, <code>null</code> if <code>null</code> input
   */
  public static int[] clone(int[] array) {
    if (array == null) {
      return null;
    }
    return (int[]) array.clone();
  }

  /**
   * <p>Clones an array returning a typecast result and handling
   * <code>null</code>.</p>
   * <p/>
   * <p>This method returns <code>null</code> for a <code>null</code> input array.</p>
   *
   * @param array the array to clone, may be <code>null</code>
   * @return the cloned array, <code>null</code> if <code>null</code> input
   */
  public static short[] clone(short[] array) {
    if (array == null) {
      return null;
    }
    return (short[]) array.clone();
  }

  /**
   * <p>Clones an array returning a typecast result and handling
   * <code>null</code>.</p>
   * <p/>
   * <p>This method returns <code>null</code> for a <code>null</code> input array.</p>
   *
   * @param array the array to clone, may be <code>null</code>
   * @return the cloned array, <code>null</code> if <code>null</code> input
   */
  public static char[] clone(char[] array) {
    if (array == null) {
      return null;
    }
    return (char[]) array.clone();
  }

  /**
   * <p>Clones an array returning a typecast result and handling
   * <code>null</code>.</p>
   * <p/>
   * <p>This method returns <code>null</code> for a <code>null</code> input array.</p>
   *
   * @param array the array to clone, may be <code>null</code>
   * @return the cloned array, <code>null</code> if <code>null</code> input
   */
  public static byte[] clone(byte[] array) {
    if (array == null) {
      return null;
    }
    return (byte[]) array.clone();
  }

  /**
   * <p>Clones an array returning a typecast result and handling
   * <code>null</code>.</p>
   * <p/>
   * <p>This method returns <code>null</code> for a <code>null</code> input array.</p>
   *
   * @param array the array to clone, may be <code>null</code>
   * @return the cloned array, <code>null</code> if <code>null</code> input
   */
  public static double[] clone(double[] array) {
    if (array == null) {
      return null;
    }
    return (double[]) array.clone();
  }

  /**
   * <p>Clones an array returning a typecast result and handling
   * <code>null</code>.</p>
   * <p/>
   * <p>This method returns <code>null</code> for a <code>null</code> input array.</p>
   *
   * @param array the array to clone, may be <code>null</code>
   * @return the cloned array, <code>null</code> if <code>null</code> input
   */
  public static float[] clone(float[] array) {
    if (array == null) {
      return null;
    }
    return (float[]) array.clone();
  }

  /**
   * <p>Clones an array returning a typecast result and handling
   * <code>null</code>.</p>
   * <p/>
   * <p>This method returns <code>null</code> for a <code>null</code> input array.</p>
   *
   * @param array the array to clone, may be <code>null</code>
   * @return the cloned array, <code>null</code> if <code>null</code> input
   */
  public static boolean[] clone(boolean[] array) {
    if (array == null) {
      return null;
    }
    return (boolean[]) array.clone();
  }


  //-----------------------------------------------------------------------

  /**
   * <p>Returns the length of the specified array.
   * This method can deal with <code>Object</code> arrays and with primitive arrays.</p>
   * <p/>
   * <p>If the input array is <code>null</code>, <code>0</code> is returned.</p>
   * <p/>
   * <pre>
   * ArrayUtils.getLength(null)            = 0
   * ArrayUtils.getLength([])              = 0
   * ArrayUtils.getLength([null])          = 1
   * ArrayUtils.getLength([true, false])   = 2
   * ArrayUtils.getLength([1, 2, 3])       = 3
   * ArrayUtils.getLength(["a", "b", "c"]) = 3
   * </pre>
   *
   * @param array the array to retrieve the length from, may be null
   * @return The length of the array, or <code>0</code> if the array is <code>null</code>
   * @throws IllegalArgumentException if the object arguement is not an array.
   * @since 2.1
   */
  public static int getLength(Object array) {
    if (array == null) {
      return 0;
    }
    return Array.getLength(array);
  }


  // Object IndexOf
  //-----------------------------------------------------------------------

  /**
   * <p>Finds the index of the given object in the array.</p>
   * <p/>
   * <p>This method returns {@link #INDEX_NOT_FOUND} (<code>-1</code>) for a <code>null</code> input array.</p>
   *
   * @param array        the array to search through for the object, may be <code>null</code>
   * @param objectToFind the object to find, may be <code>null</code>
   * @return the index of the object within the array,
   *         {@link #INDEX_NOT_FOUND} (<code>-1</code>) if not found or <code>null</code> array input
   */
  public static int indexOf(Object[] array, Object objectToFind) {
    return indexOf(array, objectToFind, 0);
  }

  /**
   * <p>Finds the index of the given object in the array starting at the given index.</p>
   * <p/>
   * <p>This method returns {@link #INDEX_NOT_FOUND} (<code>-1</code>) for a <code>null</code> input array.</p>
   * <p/>
   * <p>A negative startIndex is treated as zero. A startIndex larger than the array
   * length will return {@link #INDEX_NOT_FOUND} (<code>-1</code>).</p>
   *
   * @param array        the array to search through for the object, may be <code>null</code>
   * @param objectToFind the object to find, may be <code>null</code>
   * @param startIndex   the index to start searching at
   * @return the index of the object within the array starting at the index,
   *         {@link #INDEX_NOT_FOUND} (<code>-1</code>) if not found or <code>null</code> array input
   */
  public static int indexOf(Object[] array, Object objectToFind, int startIndex) {
    if (array == null) {
      return INDEX_NOT_FOUND;
    }
    if (startIndex < 0) {
      startIndex = 0;
    }
    if (objectToFind == null) {
      for (int i = startIndex; i < array.length; i++) {
        if (array[i] == null) {
          return i;
        }
      }
    } else {
      for (int i = startIndex; i < array.length; i++) {
        if (objectToFind.equals(array[i])) {
          return i;
        }
      }
    }
    return INDEX_NOT_FOUND;
  }


  /**
   * <p>Checks if the object is in the given array.</p>
   * <p/>
   * <p>The method returns <code>false</code> if a <code>null</code> array is passed in.</p>
   *
   * @param array        the array to search through
   * @param objectToFind the object to find
   * @return <code>true</code> if the array contains the object
   */
  public static boolean contains(Object[] array, Object objectToFind) {
    return indexOf(array, objectToFind) != INDEX_NOT_FOUND;
  }

  // long IndexOf
  //-----------------------------------------------------------------------

  /**
   * <p>Finds the index of the given value in the array.</p>
   * <p/>
   * <p>This method returns {@link #INDEX_NOT_FOUND} (<code>-1</code>) for a <code>null</code> input array.</p>
   *
   * @param array       the array to search through for the object, may be <code>null</code>
   * @param valueToFind the value to find
   * @return the index of the value within the array,
   *         {@link #INDEX_NOT_FOUND} (<code>-1</code>) if not found or <code>null</code> array input
   */
  public static int indexOf(long[] array, long valueToFind) {
    return indexOf(array, valueToFind, 0);
  }

  /**
   * <p>Finds the index of the given value in the array starting at the given index.</p>
   * <p/>
   * <p>This method returns {@link #INDEX_NOT_FOUND} (<code>-1</code>) for a <code>null</code> input array.</p>
   * <p/>
   * <p>A negative startIndex is treated as zero. A startIndex larger than the array
   * length will return {@link #INDEX_NOT_FOUND} (<code>-1</code>).</p>
   *
   * @param array       the array to search through for the object, may be <code>null</code>
   * @param valueToFind the value to find
   * @param startIndex  the index to start searching at
   * @return the index of the value within the array,
   *         {@link #INDEX_NOT_FOUND} (<code>-1</code>) if not found or <code>null</code> array input
   */
  public static int indexOf(long[] array, long valueToFind, int startIndex) {
    if (array == null) {
      return INDEX_NOT_FOUND;
    }
    if (startIndex < 0) {
      startIndex = 0;
    }
    for (int i = startIndex; i < array.length; i++) {
      if (valueToFind == array[i]) {
        return i;
      }
    }
    return INDEX_NOT_FOUND;
  }


  /**
   * <p>Checks if the value is in the given array.</p>
   * <p/>
   * <p>The method returns <code>false</code> if a <code>null</code> array is passed in.</p>
   *
   * @param array       the array to search through
   * @param valueToFind the value to find
   * @return <code>true</code> if the array contains the object
   */
  public static boolean contains(long[] array, long valueToFind) {
    return indexOf(array, valueToFind) != INDEX_NOT_FOUND;
  }

  // int IndexOf
  //-----------------------------------------------------------------------

  /**
   * <p>Finds the index of the given value in the array.</p>
   * <p/>
   * <p>This method returns {@link #INDEX_NOT_FOUND} (<code>-1</code>) for a <code>null</code> input array.</p>
   *
   * @param array       the array to search through for the object, may be <code>null</code>
   * @param valueToFind the value to find
   * @return the index of the value within the array,
   *         {@link #INDEX_NOT_FOUND} (<code>-1</code>) if not found or <code>null</code> array input
   */
  public static int indexOf(int[] array, int valueToFind) {
    return indexOf(array, valueToFind, 0);
  }

  /**
   * <p>Finds the index of the given value in the array starting at the given index.</p>
   * <p/>
   * <p>This method returns {@link #INDEX_NOT_FOUND} (<code>-1</code>) for a <code>null</code> input array.</p>
   * <p/>
   * <p>A negative startIndex is treated as zero. A startIndex larger than the array
   * length will return {@link #INDEX_NOT_FOUND} (<code>-1</code>).</p>
   *
   * @param array       the array to search through for the object, may be <code>null</code>
   * @param valueToFind the value to find
   * @param startIndex  the index to start searching at
   * @return the index of the value within the array,
   *         {@link #INDEX_NOT_FOUND} (<code>-1</code>) if not found or <code>null</code> array input
   */
  public static int indexOf(int[] array, int valueToFind, int startIndex) {
    if (array == null) {
      return INDEX_NOT_FOUND;
    }
    if (startIndex < 0) {
      startIndex = 0;
    }
    for (int i = startIndex; i < array.length; i++) {
      if (valueToFind == array[i]) {
        return i;
      }
    }
    return INDEX_NOT_FOUND;
  }


  /**
   * <p>Checks if the value is in the given array.</p>
   * <p/>
   * <p>The method returns <code>false</code> if a <code>null</code> array is passed in.</p>
   *
   * @param array       the array to search through
   * @param valueToFind the value to find
   * @return <code>true</code> if the array contains the object
   */
  public static boolean contains(int[] array, int valueToFind) {
    return indexOf(array, valueToFind) != INDEX_NOT_FOUND;
  }

  // short IndexOf
  //-----------------------------------------------------------------------

  /**
   * <p>Finds the index of the given value in the array.</p>
   * <p/>
   * <p>This method returns {@link #INDEX_NOT_FOUND} (<code>-1</code>) for a <code>null</code> input array.</p>
   *
   * @param array       the array to search through for the object, may be <code>null</code>
   * @param valueToFind the value to find
   * @return the index of the value within the array,
   *         {@link #INDEX_NOT_FOUND} (<code>-1</code>) if not found or <code>null</code> array input
   */
  public static int indexOf(short[] array, short valueToFind) {
    return indexOf(array, valueToFind, 0);
  }

  /**
   * <p>Finds the index of the given value in the array starting at the given index.</p>
   * <p/>
   * <p>This method returns {@link #INDEX_NOT_FOUND} (<code>-1</code>) for a <code>null</code> input array.</p>
   * <p/>
   * <p>A negative startIndex is treated as zero. A startIndex larger than the array
   * length will return {@link #INDEX_NOT_FOUND} (<code>-1</code>).</p>
   *
   * @param array       the array to search through for the object, may be <code>null</code>
   * @param valueToFind the value to find
   * @param startIndex  the index to start searching at
   * @return the index of the value within the array,
   *         {@link #INDEX_NOT_FOUND} (<code>-1</code>) if not found or <code>null</code> array input
   */
  public static int indexOf(short[] array, short valueToFind, int startIndex) {
    if (array == null) {
      return INDEX_NOT_FOUND;
    }
    if (startIndex < 0) {
      startIndex = 0;
    }
    for (int i = startIndex; i < array.length; i++) {
      if (valueToFind == array[i]) {
        return i;
      }
    }
    return INDEX_NOT_FOUND;
  }


  /**
   * <p>Checks if the value is in the given array.</p>
   * <p/>
   * <p>The method returns <code>false</code> if a <code>null</code> array is passed in.</p>
   *
   * @param array       the array to search through
   * @param valueToFind the value to find
   * @return <code>true</code> if the array contains the object
   */
  public static boolean contains(short[] array, short valueToFind) {
    return indexOf(array, valueToFind) != INDEX_NOT_FOUND;
  }

  // char IndexOf
  //-----------------------------------------------------------------------

  /**
   * <p>Finds the index of the given value in the array.</p>
   * <p/>
   * <p>This method returns {@link #INDEX_NOT_FOUND} (<code>-1</code>) for a <code>null</code> input array.</p>
   *
   * @param array       the array to search through for the object, may be <code>null</code>
   * @param valueToFind the value to find
   * @return the index of the value within the array,
   *         {@link #INDEX_NOT_FOUND} (<code>-1</code>) if not found or <code>null</code> array input
   * @since 2.1
   */
  public static int indexOf(char[] array, char valueToFind) {
    return indexOf(array, valueToFind, 0);
  }

  /**
   * <p>Finds the index of the given value in the array starting at the given index.</p>
   * <p/>
   * <p>This method returns {@link #INDEX_NOT_FOUND} (<code>-1</code>) for a <code>null</code> input array.</p>
   * <p/>
   * <p>A negative startIndex is treated as zero. A startIndex larger than the array
   * length will return {@link #INDEX_NOT_FOUND} (<code>-1</code>).</p>
   *
   * @param array       the array to search through for the object, may be <code>null</code>
   * @param valueToFind the value to find
   * @param startIndex  the index to start searching at
   * @return the index of the value within the array,
   *         {@link #INDEX_NOT_FOUND} (<code>-1</code>) if not found or <code>null</code> array input
   * @since 2.1
   */
  public static int indexOf(char[] array, char valueToFind, int startIndex) {
    if (array == null) {
      return INDEX_NOT_FOUND;
    }
    if (startIndex < 0) {
      startIndex = 0;
    }
    for (int i = startIndex; i < array.length; i++) {
      if (valueToFind == array[i]) {
        return i;
      }
    }
    return INDEX_NOT_FOUND;
  }


  /**
   * <p>Checks if the value is in the given array.</p>
   * <p/>
   * <p>The method returns <code>false</code> if a <code>null</code> array is passed in.</p>
   *
   * @param array       the array to search through
   * @param valueToFind the value to find
   * @return <code>true</code> if the array contains the object
   * @since 2.1
   */
  public static boolean contains(char[] array, char valueToFind) {
    return indexOf(array, valueToFind) != INDEX_NOT_FOUND;
  }

  // byte IndexOf
  //-----------------------------------------------------------------------

  /**
   * <p>Finds the index of the given value in the array.</p>
   * <p/>
   * <p>This method returns {@link #INDEX_NOT_FOUND} (<code>-1</code>) for a <code>null</code> input array.</p>
   *
   * @param array       the array to search through for the object, may be <code>null</code>
   * @param valueToFind the value to find
   * @return the index of the value within the array,
   *         {@link #INDEX_NOT_FOUND} (<code>-1</code>) if not found or <code>null</code> array input
   */
  public static int indexOf(byte[] array, byte valueToFind) {
    return indexOf(array, valueToFind, 0);
  }

  /**
   * <p>Finds the index of the given value in the array starting at the given index.</p>
   * <p/>
   * <p>This method returns {@link #INDEX_NOT_FOUND} (<code>-1</code>) for a <code>null</code> input array.</p>
   * <p/>
   * <p>A negative startIndex is treated as zero. A startIndex larger than the array
   * length will return {@link #INDEX_NOT_FOUND} (<code>-1</code>).</p>
   *
   * @param array       the array to search through for the object, may be <code>null</code>
   * @param valueToFind the value to find
   * @param startIndex  the index to start searching at
   * @return the index of the value within the array,
   *         {@link #INDEX_NOT_FOUND} (<code>-1</code>) if not found or <code>null</code> array input
   */
  public static int indexOf(byte[] array, byte valueToFind, int startIndex) {
    if (array == null) {
      return INDEX_NOT_FOUND;
    }
    if (startIndex < 0) {
      startIndex = 0;
    }
    for (int i = startIndex; i < array.length; i++) {
      if (valueToFind == array[i]) {
        return i;
      }
    }
    return INDEX_NOT_FOUND;
  }

  /**
   * <p>Checks if the value is in the given array.</p>
   * <p/>
   * <p>The method returns <code>false</code> if a <code>null</code> array is passed in.</p>
   *
   * @param array       the array to search through
   * @param valueToFind the value to find
   * @return <code>true</code> if the array contains the object
   */
  public static boolean contains(byte[] array, byte valueToFind) {
    return indexOf(array, valueToFind) != INDEX_NOT_FOUND;
  }

  // double IndexOf
  //-----------------------------------------------------------------------

  /**
   * <p>Finds the index of the given value in the array.</p>
   * <p/>
   * <p>This method returns {@link #INDEX_NOT_FOUND} (<code>-1</code>) for a <code>null</code> input array.</p>
   *
   * @param array       the array to search through for the object, may be <code>null</code>
   * @param valueToFind the value to find
   * @return the index of the value within the array,
   *         {@link #INDEX_NOT_FOUND} (<code>-1</code>) if not found or <code>null</code> array input
   */
  public static int indexOf(double[] array, double valueToFind) {
    return indexOf(array, valueToFind, 0);
  }

  /**
   * <p>Finds the index of the given value within a given tolerance in the array.
   * This method will return the index of the first value which falls between the region
   * defined by valueToFind - tolerance and valueToFind + tolerance.</p>
   * <p/>
   * <p>This method returns {@link #INDEX_NOT_FOUND} (<code>-1</code>) for a <code>null</code> input array.</p>
   *
   * @param array       the array to search through for the object, may be <code>null</code>
   * @param valueToFind the value to find
   * @param tolerance   tolerance of the search
   * @return the index of the value within the array,
   *         {@link #INDEX_NOT_FOUND} (<code>-1</code>) if not found or <code>null</code> array input
   */
  public static int indexOf(double[] array, double valueToFind, double tolerance) {
    return indexOf(array, valueToFind, 0, tolerance);
  }

  /**
   * <p>Finds the index of the given value in the array starting at the given index.</p>
   * <p/>
   * <p>This method returns {@link #INDEX_NOT_FOUND} (<code>-1</code>) for a <code>null</code> input array.</p>
   * <p/>
   * <p>A negative startIndex is treated as zero. A startIndex larger than the array
   * length will return {@link #INDEX_NOT_FOUND} (<code>-1</code>).</p>
   *
   * @param array       the array to search through for the object, may be <code>null</code>
   * @param valueToFind the value to find
   * @param startIndex  the index to start searching at
   * @return the index of the value within the array,
   *         {@link #INDEX_NOT_FOUND} (<code>-1</code>) if not found or <code>null</code> array input
   */
  public static int indexOf(double[] array, double valueToFind, int startIndex) {
    if (ArrayUtils.isEmpty(array)) {
      return INDEX_NOT_FOUND;
    }
    if (startIndex < 0) {
      startIndex = 0;
    }
    for (int i = startIndex; i < array.length; i++) {
      if (valueToFind == array[i]) {
        return i;
      }
    }
    return INDEX_NOT_FOUND;
  }

  /**
   * <p>Finds the index of the given value in the array starting at the given index.
   * This method will return the index of the first value which falls between the region
   * defined by valueToFind - tolerance and valueToFind + tolerance.</p>
   * <p/>
   * <p>This method returns {@link #INDEX_NOT_FOUND} (<code>-1</code>) for a <code>null</code> input array.</p>
   * <p/>
   * <p>A negative startIndex is treated as zero. A startIndex larger than the array
   * length will return {@link #INDEX_NOT_FOUND} (<code>-1</code>).</p>
   *
   * @param array       the array to search through for the object, may be <code>null</code>
   * @param valueToFind the value to find
   * @param startIndex  the index to start searching at
   * @param tolerance   tolerance of the search
   * @return the index of the value within the array,
   *         {@link #INDEX_NOT_FOUND} (<code>-1</code>) if not found or <code>null</code> array input
   */
  public static int indexOf(double[] array, double valueToFind, int startIndex, double tolerance) {
    if (ArrayUtils.isEmpty(array)) {
      return INDEX_NOT_FOUND;
    }
    if (startIndex < 0) {
      startIndex = 0;
    }
    double min = valueToFind - tolerance;
    double max = valueToFind + tolerance;
    for (int i = startIndex; i < array.length; i++) {
      if (array[i] >= min && array[i] <= max) {
        return i;
      }
    }
    return INDEX_NOT_FOUND;
  }


  /**
   * <p>Checks if the value is in the given array.</p>
   * <p/>
   * <p>The method returns <code>false</code> if a <code>null</code> array is passed in.</p>
   *
   * @param array       the array to search through
   * @param valueToFind the value to find
   * @return <code>true</code> if the array contains the object
   */
  public static boolean contains(double[] array, double valueToFind) {
    return indexOf(array, valueToFind) != INDEX_NOT_FOUND;
  }

  /**
   * <p>Checks if a value falling within the given tolerance is in the
   * given array.  If the array contains a value within the inclusive range
   * defined by (value - tolerance) to (value + tolerance).</p>
   * <p/>
   * <p>The method returns <code>false</code> if a <code>null</code> array
   * is passed in.</p>
   *
   * @param array       the array to search
   * @param valueToFind the value to find
   * @param tolerance   the array contains the tolerance of the search
   * @return true if value falling within tolerance is in array
   */
  public static boolean contains(double[] array, double valueToFind, double tolerance) {
    return indexOf(array, valueToFind, 0, tolerance) != INDEX_NOT_FOUND;
  }

  // float IndexOf
  //-----------------------------------------------------------------------

  /**
   * <p>Finds the index of the given value in the array.</p>
   * <p/>
   * <p>This method returns {@link #INDEX_NOT_FOUND} (<code>-1</code>) for a <code>null</code> input array.</p>
   *
   * @param array       the array to search through for the object, may be <code>null</code>
   * @param valueToFind the value to find
   * @return the index of the value within the array,
   *         {@link #INDEX_NOT_FOUND} (<code>-1</code>) if not found or <code>null</code> array input
   */
  public static int indexOf(float[] array, float valueToFind) {
    return indexOf(array, valueToFind, 0);
  }

  /**
   * <p>Finds the index of the given value in the array starting at the given index.</p>
   * <p/>
   * <p>This method returns {@link #INDEX_NOT_FOUND} (<code>-1</code>) for a <code>null</code> input array.</p>
   * <p/>
   * <p>A negative startIndex is treated as zero. A startIndex larger than the array
   * length will return {@link #INDEX_NOT_FOUND} (<code>-1</code>).</p>
   *
   * @param array       the array to search through for the object, may be <code>null</code>
   * @param valueToFind the value to find
   * @param startIndex  the index to start searching at
   * @return the index of the value within the array,
   *         {@link #INDEX_NOT_FOUND} (<code>-1</code>) if not found or <code>null</code> array input
   */
  public static int indexOf(float[] array, float valueToFind, int startIndex) {
    if (ArrayUtils.isEmpty(array)) {
      return INDEX_NOT_FOUND;
    }
    if (startIndex < 0) {
      startIndex = 0;
    }
    for (int i = startIndex; i < array.length; i++) {
      if (valueToFind == array[i]) {
        return i;
      }
    }
    return INDEX_NOT_FOUND;
  }


  /**
   * <p>Checks if the value is in the given array.</p>
   * <p/>
   * <p>The method returns <code>false</code> if a <code>null</code> array is passed in.</p>
   *
   * @param array       the array to search through
   * @param valueToFind the value to find
   * @return <code>true</code> if the array contains the object
   */
  public static boolean contains(float[] array, float valueToFind) {
    return indexOf(array, valueToFind) != INDEX_NOT_FOUND;
  }

  // boolean IndexOf
  //-----------------------------------------------------------------------

  /**
   * <p>Finds the index of the given value in the array.</p>
   * <p/>
   * <p>This method returns {@link #INDEX_NOT_FOUND} (<code>-1</code>) for a <code>null</code> input array.</p>
   *
   * @param array       the array to search through for the object, may be <code>null</code>
   * @param valueToFind the value to find
   * @return the index of the value within the array,
   *         {@link #INDEX_NOT_FOUND} (<code>-1</code>) if not found or <code>null</code> array input
   */
  public static int indexOf(boolean[] array, boolean valueToFind) {
    return indexOf(array, valueToFind, 0);
  }

  /**
   * <p>Finds the index of the given value in the array starting at the given index.</p>
   * <p/>
   * <p>This method returns {@link #INDEX_NOT_FOUND} (<code>-1</code>) for a <code>null</code> input array.</p>
   * <p/>
   * <p>A negative startIndex is treated as zero. A startIndex larger than the array
   * length will return {@link #INDEX_NOT_FOUND} (<code>-1</code>).</p>
   *
   * @param array       the array to search through for the object, may be <code>null</code>
   * @param valueToFind the value to find
   * @param startIndex  the index to start searching at
   * @return the index of the value within the array,
   *         {@link #INDEX_NOT_FOUND} (<code>-1</code>) if not found or <code>null</code>
   *         array input
   */
  public static int indexOf(boolean[] array, boolean valueToFind, int startIndex) {
    if (ArrayUtils.isEmpty(array)) {
      return INDEX_NOT_FOUND;
    }
    if (startIndex < 0) {
      startIndex = 0;
    }
    for (int i = startIndex; i < array.length; i++) {
      if (valueToFind == array[i]) {
        return i;
      }
    }
    return INDEX_NOT_FOUND;
  }


  /**
   * <p>Checks if the value is in the given array.</p>
   * <p/>
   * <p>The method returns <code>false</code> if a <code>null</code> array is passed in.</p>
   *
   * @param array       the array to search through
   * @param valueToFind the value to find
   * @return <code>true</code> if the array contains the object
   */
  public static boolean contains(boolean[] array, boolean valueToFind) {
    return indexOf(array, valueToFind) != INDEX_NOT_FOUND;
  }

  // Primitive/Object array converters
  // ----------------------------------------------------------------------

  // Character array converters
  // ----------------------------------------------------------------------


  /**
   * <p>Checks if an array of primitive doubles is empty or <code>null</code>.</p>
   *
   * @param array the array to test
   * @return <code>true</code> if the array is empty or <code>null</code>
   * @since 2.1
   */
  public static boolean isEmpty(double[] array) {
    if (array == null || array.length == 0) {
      return true;
    }
    return false;
  }

  /**
   * <p>Checks if an array of primitive floats is empty or <code>null</code>.</p>
   *
   * @param array the array to test
   * @return <code>true</code> if the array is empty or <code>null</code>
   * @since 2.1
   */
  public static boolean isEmpty(float[] array) {
    if (array == null || array.length == 0) {
      return true;
    }
    return false;
  }

  /**
   * <p>Checks if an array of primitive booleans is empty or <code>null</code>.</p>
   *
   * @param array the array to test
   * @return <code>true</code> if the array is empty or <code>null</code>
   * @since 2.1
   */
  public static boolean isEmpty(boolean[] array) {
    if (array == null || array.length == 0) {
      return true;
    }
    return false;
  }

  /**
   * <p>Adds all the elements of the given arrays into a new array.</p>
   * <p>The new array contains all of the element of <code>array1</code> followed
   * by all of the elements <code>array2</code>. When an array is returned, it is always
   * a new array.</p>
   * <p/>
   * <pre>
   * ArrayUtils.addAll(null, null)     = null
   * ArrayUtils.addAll(array1, null)   = cloned copy of array1
   * ArrayUtils.addAll(null, array2)   = cloned copy of array2
   * ArrayUtils.addAll([], [])         = []
   * ArrayUtils.addAll([null], [null]) = [null, null]
   * ArrayUtils.addAll(["a", "b", "c"], ["1", "2", "3"]) = ["a", "b", "c", "1", "2", "3"]
   * </pre>
   *
   * @param array1 the first array whose elements are added to the new array, may be <code>null</code>
   * @param array2 the second array whose elements are added to the new array, may be <code>null</code>
   * @return The new array, <code>null</code> if <code>null</code> array inputs.
   *         The type of the new array is the type of the first array.
   * @since 2.1
   */
  public static Object[] addAll(Object[] array1, Object[] array2) {
    if (array1 == null) {
      return clone(array2);
    } else if (array2 == null) {
      return clone(array1);
    }
    Object[] joinedArray = (Object[]) Array.newInstance(array1.getClass().getComponentType(),
      array1.length + array2.length);
    System.arraycopy(array1, 0, joinedArray, 0, array1.length);
    System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
    return joinedArray;
  }

  /**
   * <p>Adds all the elements of the given arrays into a new array.</p>
   * <p>The new array contains all of the element of <code>array1</code> followed
   * by all of the elements <code>array2</code>. When an array is returned, it is always
   * a new array.</p>
   * <p/>
   * <pre>
   * ArrayUtils.addAll(array1, null)   = cloned copy of array1
   * ArrayUtils.addAll(null, array2)   = cloned copy of array2
   * ArrayUtils.addAll([], [])         = []
   * </pre>
   *
   * @param array1 the first array whose elements are added to the new array.
   * @param array2 the second array whose elements are added to the new array.
   * @return The new boolean[] array.
   * @since 2.1
   */
  public static boolean[] addAll(boolean[] array1, boolean[] array2) {
    if (array1 == null) {
      return clone(array2);
    } else if (array2 == null) {
      return clone(array1);
    }
    boolean[] joinedArray = new boolean[array1.length + array2.length];
    System.arraycopy(array1, 0, joinedArray, 0, array1.length);
    System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
    return joinedArray;
  }

  /**
   * <p>Adds all the elements of the given arrays into a new array.</p>
   * <p>The new array contains all of the element of <code>array1</code> followed
   * by all of the elements <code>array2</code>. When an array is returned, it is always
   * a new array.</p>
   * <p/>
   * <pre>
   * ArrayUtils.addAll(array1, null)   = cloned copy of array1
   * ArrayUtils.addAll(null, array2)   = cloned copy of array2
   * ArrayUtils.addAll([], [])         = []
   * </pre>
   *
   * @param array1 the first array whose elements are added to the new array.
   * @param array2 the second array whose elements are added to the new array.
   * @return The new char[] array.
   * @since 2.1
   */
  public static char[] addAll(char[] array1, char[] array2) {
    if (array1 == null) {
      return clone(array2);
    } else if (array2 == null) {
      return clone(array1);
    }
    char[] joinedArray = new char[array1.length + array2.length];
    System.arraycopy(array1, 0, joinedArray, 0, array1.length);
    System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
    return joinedArray;
  }

  /**
   * <p>Adds all the elements of the given arrays into a new array.</p>
   * <p>The new array contains all of the element of <code>array1</code> followed
   * by all of the elements <code>array2</code>. When an array is returned, it is always
   * a new array.</p>
   * <p/>
   * <pre>
   * ArrayUtils.addAll(array1, null)   = cloned copy of array1
   * ArrayUtils.addAll(null, array2)   = cloned copy of array2
   * ArrayUtils.addAll([], [])         = []
   * </pre>
   *
   * @param array1 the first array whose elements are added to the new array.
   * @param array2 the second array whose elements are added to the new array.
   * @return The new byte[] array.
   * @since 2.1
   */
  public static byte[] addAll(byte[] array1, byte[] array2) {
    if (array1 == null) {
      return clone(array2);
    } else if (array2 == null) {
      return clone(array1);
    }
    byte[] joinedArray = new byte[array1.length + array2.length];
    System.arraycopy(array1, 0, joinedArray, 0, array1.length);
    System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
    return joinedArray;
  }

  /**
   * <p>Adds all the elements of the given arrays into a new array.</p>
   * <p>The new array contains all of the element of <code>array1</code> followed
   * by all of the elements <code>array2</code>. When an array is returned, it is always
   * a new array.</p>
   * <p/>
   * <pre>
   * ArrayUtils.addAll(array1, null)   = cloned copy of array1
   * ArrayUtils.addAll(null, array2)   = cloned copy of array2
   * ArrayUtils.addAll([], [])         = []
   * </pre>
   *
   * @param array1 the first array whose elements are added to the new array.
   * @param array2 the second array whose elements are added to the new array.
   * @return The new short[] array.
   * @since 2.1
   */
  public static short[] addAll(short[] array1, short[] array2) {
    if (array1 == null) {
      return clone(array2);
    } else if (array2 == null) {
      return clone(array1);
    }
    short[] joinedArray = new short[array1.length + array2.length];
    System.arraycopy(array1, 0, joinedArray, 0, array1.length);
    System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
    return joinedArray;
  }

  /**
   * <p>Adds all the elements of the given arrays into a new array.</p>
   * <p>The new array contains all of the element of <code>array1</code> followed
   * by all of the elements <code>array2</code>. When an array is returned, it is always
   * a new array.</p>
   * <p/>
   * <pre>
   * ArrayUtils.addAll(array1, null)   = cloned copy of array1
   * ArrayUtils.addAll(null, array2)   = cloned copy of array2
   * ArrayUtils.addAll([], [])         = []
   * </pre>
   *
   * @param array1 the first array whose elements are added to the new array.
   * @param array2 the second array whose elements are added to the new array.
   * @return The new int[] array.
   * @since 2.1
   */
  public static int[] addAll(int[] array1, int[] array2) {
    if (array1 == null) {
      return clone(array2);
    } else if (array2 == null) {
      return clone(array1);
    }
    int[] joinedArray = new int[array1.length + array2.length];
    System.arraycopy(array1, 0, joinedArray, 0, array1.length);
    System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
    return joinedArray;
  }

  /**
   * <p>Adds all the elements of the given arrays into a new array.</p>
   * <p>The new array contains all of the element of <code>array1</code> followed
   * by all of the elements <code>array2</code>. When an array is returned, it is always
   * a new array.</p>
   * <p/>
   * <pre>
   * ArrayUtils.addAll(array1, null)   = cloned copy of array1
   * ArrayUtils.addAll(null, array2)   = cloned copy of array2
   * ArrayUtils.addAll([], [])         = []
   * </pre>
   *
   * @param array1 the first array whose elements are added to the new array.
   * @param array2 the second array whose elements are added to the new array.
   * @return The new long[] array.
   * @since 2.1
   */
  public static long[] addAll(long[] array1, long[] array2) {
    if (array1 == null) {
      return clone(array2);
    } else if (array2 == null) {
      return clone(array1);
    }
    long[] joinedArray = new long[array1.length + array2.length];
    System.arraycopy(array1, 0, joinedArray, 0, array1.length);
    System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
    return joinedArray;
  }

  /**
   * <p>Adds all the elements of the given arrays into a new array.</p>
   * <p>The new array contains all of the element of <code>array1</code> followed
   * by all of the elements <code>array2</code>. When an array is returned, it is always
   * a new array.</p>
   * <p/>
   * <pre>
   * ArrayUtils.addAll(array1, null)   = cloned copy of array1
   * ArrayUtils.addAll(null, array2)   = cloned copy of array2
   * ArrayUtils.addAll([], [])         = []
   * </pre>
   *
   * @param array1 the first array whose elements are added to the new array.
   * @param array2 the second array whose elements are added to the new array.
   * @return The new float[] array.
   * @since 2.1
   */
  public static float[] addAll(float[] array1, float[] array2) {
    if (array1 == null) {
      return clone(array2);
    } else if (array2 == null) {
      return clone(array1);
    }
    float[] joinedArray = new float[array1.length + array2.length];
    System.arraycopy(array1, 0, joinedArray, 0, array1.length);
    System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
    return joinedArray;
  }

  /**
   * <p>Adds all the elements of the given arrays into a new array.</p>
   * <p>The new array contains all of the element of <code>array1</code> followed
   * by all of the elements <code>array2</code>. When an array is returned, it is always
   * a new array.</p>
   * <p/>
   * <pre>
   * ArrayUtils.addAll(array1, null)   = cloned copy of array1
   * ArrayUtils.addAll(null, array2)   = cloned copy of array2
   * ArrayUtils.addAll([], [])         = []
   * </pre>
   *
   * @param array1 the first array whose elements are added to the new array.
   * @param array2 the second array whose elements are added to the new array.
   * @return The new double[] array.
   * @since 2.1
   */
  public static double[] addAll(double[] array1, double[] array2) {
    if (array1 == null) {
      return clone(array2);
    } else if (array2 == null) {
      return clone(array1);
    }
    double[] joinedArray = new double[array1.length + array2.length];
    System.arraycopy(array1, 0, joinedArray, 0, array1.length);
    System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
    return joinedArray;
  }

  /**
   * <p>Copies the given array and adds the given element at the end of the new array.</p>
   * <p/>
   * <p>The new array contains the same elements of the input
   * array plus the given element in the last position. The component type of
   * the new array is the same as that of the input array.</p>
   * <p/>
   * <p>If the input array is <code>null</code>, a new one element array is returned
   * whose component type is the same as the element.</p>
   * <p/>
   * <pre>
   * ArrayUtils.add(null, null)      = [null]
   * ArrayUtils.add(null, "a")       = ["a"]
   * ArrayUtils.add(["a"], null)     = ["a", null]
   * ArrayUtils.add(["a"], "b")      = ["a", "b"]
   * ArrayUtils.add(["a", "b"], "c") = ["a", "b", "c"]
   * </pre>
   *
   * @param array   the array to "add" the element to, may be <code>null</code>
   * @param element the object to add
   * @return A new array containing the existing elements plus the new element
   * @since 2.1
   */
  public static Object[] add(Object[] array, Object element) {
    Class type = array != null ? array.getClass() : (element != null ? element.getClass() : Object.class);
    Object[] newArray = (Object[]) copyArrayGrow1(array, type);
    newArray[newArray.length - 1] = element;
    return newArray;
  }

  /**
   * <p>Copies the given array and adds the given element at the end of the new array.</p>
   * <p/>
   * <p>The new array contains the same elements of the input
   * array plus the given element in the last position. The component type of
   * the new array is the same as that of the input array.</p>
   * <p/>
   * <p>If the input array is <code>null</code>, a new one element array is returned
   * whose component type is the same as the element.</p>
   * <p/>
   * <pre>
   * ArrayUtils.add(null, true)          = [true]
   * ArrayUtils.add([true], false)       = [true, false]
   * ArrayUtils.add([true, false], true) = [true, false, true]
   * </pre>
   *
   * @param array   the array to copy and add the element to, may be <code>null</code>
   * @param element the object to add at the last index of the new array
   * @return A new array containing the existing elements plus the new element
   * @since 2.1
   */
  public static boolean[] add(boolean[] array, boolean element) {
    boolean[] newArray = (boolean[]) copyArrayGrow1(array, Boolean.TYPE);
    newArray[newArray.length - 1] = element;
    return newArray;
  }

  /**
   * <p>Copies the given array and adds the given element at the end of the new array.</p>
   * <p/>
   * <p>The new array contains the same elements of the input
   * array plus the given element in the last position. The component type of
   * the new array is the same as that of the input array.</p>
   * <p/>
   * <p>If the input array is <code>null</code>, a new one element array is returned
   * whose component type is the same as the element.</p>
   * <p/>
   * <pre>
   * ArrayUtils.add(null, 0)   = [0]
   * ArrayUtils.add([1], 0)    = [1, 0]
   * ArrayUtils.add([1, 0], 1) = [1, 0, 1]
   * </pre>
   *
   * @param array   the array to copy and add the element to, may be <code>null</code>
   * @param element the object to add at the last index of the new array
   * @return A new array containing the existing elements plus the new element
   * @since 2.1
   */
  public static byte[] add(byte[] array, byte element) {
    byte[] newArray = (byte[]) copyArrayGrow1(array, Byte.TYPE);
    newArray[newArray.length - 1] = element;
    return newArray;
  }

  /**
   * <p>Copies the given array and adds the given element at the end of the new array.</p>
   * <p/>
   * <p>The new array contains the same elements of the input
   * array plus the given element in the last position. The component type of
   * the new array is the same as that of the input array.</p>
   * <p/>
   * <p>If the input array is <code>null</code>, a new one element array is returned
   * whose component type is the same as the element.</p>
   * <p/>
   * <pre>
   * ArrayUtils.add(null, '0')       = ['0']
   * ArrayUtils.add(['1'], '0')      = ['1', '0']
   * ArrayUtils.add(['1', '0'], '1') = ['1', '0', '1']
   * </pre>
   *
   * @param array   the array to copy and add the element to, may be <code>null</code>
   * @param element the object to add at the last index of the new array
   * @return A new array containing the existing elements plus the new element
   * @since 2.1
   */
  public static char[] add(char[] array, char element) {
    char[] newArray = (char[]) copyArrayGrow1(array, Character.TYPE);
    newArray[newArray.length - 1] = element;
    return newArray;
  }

  /**
   * <p>Copies the given array and adds the given element at the end of the new array.</p>
   * <p/>
   * <p>The new array contains the same elements of the input
   * array plus the given element in the last position. The component type of
   * the new array is the same as that of the input array.</p>
   * <p/>
   * <p>If the input array is <code>null</code>, a new one element array is returned
   * whose component type is the same as the element.</p>
   * <p/>
   * <pre>
   * ArrayUtils.add(null, 0)   = [0]
   * ArrayUtils.add([1], 0)    = [1, 0]
   * ArrayUtils.add([1, 0], 1) = [1, 0, 1]
   * </pre>
   *
   * @param array   the array to copy and add the element to, may be <code>null</code>
   * @param element the object to add at the last index of the new array
   * @return A new array containing the existing elements plus the new element
   * @since 2.1
   */
  public static double[] add(double[] array, double element) {
    double[] newArray = (double[]) copyArrayGrow1(array, Double.TYPE);
    newArray[newArray.length - 1] = element;
    return newArray;
  }

  /**
   * <p>Copies the given array and adds the given element at the end of the new array.</p>
   * <p/>
   * <p>The new array contains the same elements of the input
   * array plus the given element in the last position. The component type of
   * the new array is the same as that of the input array.</p>
   * <p/>
   * <p>If the input array is <code>null</code>, a new one element array is returned
   * whose component type is the same as the element.</p>
   * <p/>
   * <pre>
   * ArrayUtils.add(null, 0)   = [0]
   * ArrayUtils.add([1], 0)    = [1, 0]
   * ArrayUtils.add([1, 0], 1) = [1, 0, 1]
   * </pre>
   *
   * @param array   the array to copy and add the element to, may be <code>null</code>
   * @param element the object to add at the last index of the new array
   * @return A new array containing the existing elements plus the new element
   * @since 2.1
   */
  public static float[] add(float[] array, float element) {
    float[] newArray = (float[]) copyArrayGrow1(array, Float.TYPE);
    newArray[newArray.length - 1] = element;
    return newArray;
  }

  /**
   * <p>Copies the given array and adds the given element at the end of the new array.</p>
   * <p/>
   * <p>The new array contains the same elements of the input
   * array plus the given element in the last position. The component type of
   * the new array is the same as that of the input array.</p>
   * <p/>
   * <p>If the input array is <code>null</code>, a new one element array is returned
   * whose component type is the same as the element.</p>
   * <p/>
   * <pre>
   * ArrayUtils.add(null, 0)   = [0]
   * ArrayUtils.add([1], 0)    = [1, 0]
   * ArrayUtils.add([1, 0], 1) = [1, 0, 1]
   * </pre>
   *
   * @param array   the array to copy and add the element to, may be <code>null</code>
   * @param element the object to add at the last index of the new array
   * @return A new array containing the existing elements plus the new element
   * @since 2.1
   */
  public static int[] add(int[] array, int element) {
    int[] newArray = (int[]) copyArrayGrow1(array, Integer.TYPE);
    newArray[newArray.length - 1] = element;
    return newArray;
  }

  /**
   * <p>Copies the given array and adds the given element at the end of the new array.</p>
   * <p/>
   * <p>The new array contains the same elements of the input
   * array plus the given element in the last position. The component type of
   * the new array is the same as that of the input array.</p>
   * <p/>
   * <p>If the input array is <code>null</code>, a new one element array is returned
   * whose component type is the same as the element.</p>
   * <p/>
   * <pre>
   * ArrayUtils.add(null, 0)   = [0]
   * ArrayUtils.add([1], 0)    = [1, 0]
   * ArrayUtils.add([1, 0], 1) = [1, 0, 1]
   * </pre>
   *
   * @param array   the array to copy and add the element to, may be <code>null</code>
   * @param element the object to add at the last index of the new array
   * @return A new array containing the existing elements plus the new element
   * @since 2.1
   */
  public static long[] add(long[] array, long element) {
    long[] newArray = (long[]) copyArrayGrow1(array, Long.TYPE);
    newArray[newArray.length - 1] = element;
    return newArray;
  }

  /**
   * <p>Copies the given array and adds the given element at the end of the new array.</p>
   * <p/>
   * <p>The new array contains the same elements of the input
   * array plus the given element in the last position. The component type of
   * the new array is the same as that of the input array.</p>
   * <p/>
   * <p>If the input array is <code>null</code>, a new one element array is returned
   * whose component type is the same as the element.</p>
   * <p/>
   * <pre>
   * ArrayUtils.add(null, 0)   = [0]
   * ArrayUtils.add([1], 0)    = [1, 0]
   * ArrayUtils.add([1, 0], 1) = [1, 0, 1]
   * </pre>
   *
   * @param array   the array to copy and add the element to, may be <code>null</code>
   * @param element the object to add at the last index of the new array
   * @return A new array containing the existing elements plus the new element
   * @since 2.1
   */
  public static short[] add(short[] array, short element) {
    short[] newArray = (short[]) copyArrayGrow1(array, Short.TYPE);
    newArray[newArray.length - 1] = element;
    return newArray;
  }

  /**
   * Returns a copy of the given array of size 1 greater than the argument.
   * The last value of the array is left to the default value.
   *
   * @param array                 The array to copy, must not be <code>null</code>.
   * @param newArrayComponentType If <code>array</code> is <code>null</code>, create a
   *                              size 1 array of this type.
   * @return A new copy of the array of size 1 greater than the input.
   */
  private static Object copyArrayGrow1(Object array, Class newArrayComponentType) {
    if (array != null) {
      int arrayLength = Array.getLength(array);
      Object newArray = Array.newInstance(array.getClass().getComponentType(), arrayLength + 1);
      System.arraycopy(array, 0, newArray, 0, arrayLength);
      return newArray;
    }
    return Array.newInstance(newArrayComponentType, 1);
  }

  /**
   * <p>Inserts the specified element at the specified position in the array.
   * Shifts the element currently at that position (if any) and any subsequent
   * elements to the right (adds one to their indices).</p>
   * <p/>
   * <p>This method returns a new array with the same elements of the input
   * array plus the given element on the specified position. The component
   * type of the returned array is always the same as that of the input
   * array.</p>
   * <p/>
   * <p>If the input array is <code>null</code>, a new one element array is returned
   * whose component type is the same as the element.</p>
   * <p/>
   * <pre>
   * ArrayUtils.add(null, 0, null)      = [null]
   * ArrayUtils.add(null, 0, "a")       = ["a"]
   * ArrayUtils.add(["a"], 1, null)     = ["a", null]
   * ArrayUtils.add(["a"], 1, "b")      = ["a", "b"]
   * ArrayUtils.add(["a", "b"], 3, "c") = ["a", "b", "c"]
   * </pre>
   *
   * @param array   the array to add the element to, may be <code>null</code>
   * @param index   the position of the new object
   * @param element the object to add
   * @return A new array containing the existing elements and the new element
   * @throws IndexOutOfBoundsException if the index is out of range
   *                                   (index < 0 || index > array.length).
   */
  public static Object[] add(Object[] array, int index, Object element) {
    Class clss = null;
    if (array != null) {
      clss = array.getClass().getComponentType();
    } else if (element != null) {
      clss = element.getClass();
    } else {
      return new Object[]{null};
    }
    return (Object[]) add(array, index, element, clss);
  }

  /**
   * <p>Inserts the specified element at the specified position in the array.
   * Shifts the element currently at that position (if any) and any subsequent
   * elements to the right (adds one to their indices).</p>
   * <p/>
   * <p>This method returns a new array with the same elements of the input
   * array plus the given element on the specified position. The component
   * type of the returned array is always the same as that of the input
   * array.</p>
   * <p/>
   * <p>If the input array is <code>null</code>, a new one element array is returned
   * whose component type is the same as the element.</p>
   * <p/>
   * <pre>
   * ArrayUtils.add(null, 0, true)          = [true]
   * ArrayUtils.add([true], 0, false)       = [false, true]
   * ArrayUtils.add([false], 1, true)       = [false, true]
   * ArrayUtils.add([true, false], 1, true) = [true, true, false]
   * </pre>
   *
   * @param array   the array to add the element to, may be <code>null</code>
   * @param index   the position of the new object
   * @param element the object to add
   * @return A new array containing the existing elements and the new element
   * @throws IndexOutOfBoundsException if the index is out of range
   *                                   (index < 0 || index > array.length).
   */
  public static boolean[] add(boolean[] array, int index, boolean element) {
    return (boolean[]) add(array, index, BooleanUtils.toBooleanObject(element), Boolean.TYPE);
  }

  /**
   * <p>Inserts the specified element at the specified position in the array.
   * Shifts the element currently at that position (if any) and any subsequent
   * elements to the right (adds one to their indices).</p>
   * <p/>
   * <p>This method returns a new array with the same elements of the input
   * array plus the given element on the specified position. The component
   * type of the returned array is always the same as that of the input
   * array.</p>
   * <p/>
   * <p>If the input array is <code>null</code>, a new one element array is returned
   * whose component type is the same as the element.</p>
   * <p/>
   * <pre>
   * ArrayUtils.add(null, 0, 'a')            = ['a']
   * ArrayUtils.add(['a'], 0, 'b')           = ['b', 'a']
   * ArrayUtils.add(['a', 'b'], 0, 'c')      = ['c', 'a', 'b']
   * ArrayUtils.add(['a', 'b'], 1, 'k')      = ['a', 'k', 'b']
   * ArrayUtils.add(['a', 'b', 'c'], 1, 't') = ['a', 't', 'b', 'c']
   * </pre>
   *
   * @param array   the array to add the element to, may be <code>null</code>
   * @param index   the position of the new object
   * @param element the object to add
   * @return A new array containing the existing elements and the new element
   * @throws IndexOutOfBoundsException if the index is out of range
   *                                   (index < 0 || index > array.length).
   */
  public static char[] add(char[] array, int index, char element) {
    return (char[]) add(array, index, new Character(element), Character.TYPE);
  }

  /**
   * <p>Inserts the specified element at the specified position in the array.
   * Shifts the element currently at that position (if any) and any subsequent
   * elements to the right (adds one to their indices).</p>
   * <p/>
   * <p>This method returns a new array with the same elements of the input
   * array plus the given element on the specified position. The component
   * type of the returned array is always the same as that of the input
   * array.</p>
   * <p/>
   * <p>If the input array is <code>null</code>, a new one element array is returned
   * whose component type is the same as the element.</p>
   * <p/>
   * <pre>
   * ArrayUtils.add([1], 0, 2)         = [2, 1]
   * ArrayUtils.add([2, 6], 2, 3)      = [2, 6, 3]
   * ArrayUtils.add([2, 6], 0, 1)      = [1, 2, 6]
   * ArrayUtils.add([2, 6, 3], 2, 1)   = [2, 6, 1, 3]
   * </pre>
   *
   * @param array   the array to add the element to, may be <code>null</code>
   * @param index   the position of the new object
   * @param element the object to add
   * @return A new array containing the existing elements and the new element
   * @throws IndexOutOfBoundsException if the index is out of range
   *                                   (index < 0 || index > array.length).
   */
  public static byte[] add(byte[] array, int index, byte element) {
    return (byte[]) add(array, index, new Byte(element), Byte.TYPE);
  }

  /**
   * <p>Inserts the specified element at the specified position in the array.
   * Shifts the element currently at that position (if any) and any subsequent
   * elements to the right (adds one to their indices).</p>
   * <p/>
   * <p>This method returns a new array with the same elements of the input
   * array plus the given element on the specified position. The component
   * type of the returned array is always the same as that of the input
   * array.</p>
   * <p/>
   * <p>If the input array is <code>null</code>, a new one element array is returned
   * whose component type is the same as the element.</p>
   * <p/>
   * <pre>
   * ArrayUtils.add([1], 0, 2)         = [2, 1]
   * ArrayUtils.add([2, 6], 2, 10)     = [2, 6, 10]
   * ArrayUtils.add([2, 6], 0, -4)     = [-4, 2, 6]
   * ArrayUtils.add([2, 6, 3], 2, 1)   = [2, 6, 1, 3]
   * </pre>
   *
   * @param array   the array to add the element to, may be <code>null</code>
   * @param index   the position of the new object
   * @param element the object to add
   * @return A new array containing the existing elements and the new element
   * @throws IndexOutOfBoundsException if the index is out of range
   *                                   (index < 0 || index > array.length).
   */
  public static short[] add(short[] array, int index, short element) {
    return (short[]) add(array, index, new Short(element), Short.TYPE);
  }

  /**
   * <p>Inserts the specified element at the specified position in the array.
   * Shifts the element currently at that position (if any) and any subsequent
   * elements to the right (adds one to their indices).</p>
   * <p/>
   * <p>This method returns a new array with the same elements of the input
   * array plus the given element on the specified position. The component
   * type of the returned array is always the same as that of the input
   * array.</p>
   * <p/>
   * <p>If the input array is <code>null</code>, a new one element array is returned
   * whose component type is the same as the element.</p>
   * <p/>
   * <pre>
   * ArrayUtils.add([1], 0, 2)         = [2, 1]
   * ArrayUtils.add([2, 6], 2, 10)     = [2, 6, 10]
   * ArrayUtils.add([2, 6], 0, -4)     = [-4, 2, 6]
   * ArrayUtils.add([2, 6, 3], 2, 1)   = [2, 6, 1, 3]
   * </pre>
   *
   * @param array   the array to add the element to, may be <code>null</code>
   * @param index   the position of the new object
   * @param element the object to add
   * @return A new array containing the existing elements and the new element
   * @throws IndexOutOfBoundsException if the index is out of range
   *                                   (index < 0 || index > array.length).
   */
  public static int[] add(int[] array, int index, int element) {
    return (int[]) add(array, index, new Integer(element), Integer.TYPE);
  }

  /**
   * <p>Inserts the specified element at the specified position in the array.
   * Shifts the element currently at that position (if any) and any subsequent
   * elements to the right (adds one to their indices).</p>
   * <p/>
   * <p>This method returns a new array with the same elements of the input
   * array plus the given element on the specified position. The component
   * type of the returned array is always the same as that of the input
   * array.</p>
   * <p/>
   * <p>If the input array is <code>null</code>, a new one element array is returned
   * whose component type is the same as the element.</p>
   * <p/>
   * <pre>
   * ArrayUtils.add([1L], 0, 2L)           = [2L, 1L]
   * ArrayUtils.add([2L, 6L], 2, 10L)      = [2L, 6L, 10L]
   * ArrayUtils.add([2L, 6L], 0, -4L)      = [-4L, 2L, 6L]
   * ArrayUtils.add([2L, 6L, 3L], 2, 1L)   = [2L, 6L, 1L, 3L]
   * </pre>
   *
   * @param array   the array to add the element to, may be <code>null</code>
   * @param index   the position of the new object
   * @param element the object to add
   * @return A new array containing the existing elements and the new element
   * @throws IndexOutOfBoundsException if the index is out of range
   *                                   (index < 0 || index > array.length).
   */
  public static long[] add(long[] array, int index, long element) {
    return (long[]) add(array, index, new Long(element), Long.TYPE);
  }

  /**
   * <p>Inserts the specified element at the specified position in the array.
   * Shifts the element currently at that position (if any) and any subsequent
   * elements to the right (adds one to their indices).</p>
   * <p/>
   * <p>This method returns a new array with the same elements of the input
   * array plus the given element on the specified position. The component
   * type of the returned array is always the same as that of the input
   * array.</p>
   * <p/>
   * <p>If the input array is <code>null</code>, a new one element array is returned
   * whose component type is the same as the element.</p>
   * <p/>
   * <pre>
   * ArrayUtils.add([1.1f], 0, 2.2f)               = [2.2f, 1.1f]
   * ArrayUtils.add([2.3f, 6.4f], 2, 10.5f)        = [2.3f, 6.4f, 10.5f]
   * ArrayUtils.add([2.6f, 6.7f], 0, -4.8f)        = [-4.8f, 2.6f, 6.7f]
   * ArrayUtils.add([2.9f, 6.0f, 0.3f], 2, 1.0f)   = [2.9f, 6.0f, 1.0f, 0.3f]
   * </pre>
   *
   * @param array   the array to add the element to, may be <code>null</code>
   * @param index   the position of the new object
   * @param element the object to add
   * @return A new array containing the existing elements and the new element
   * @throws IndexOutOfBoundsException if the index is out of range
   *                                   (index < 0 || index > array.length).
   */
  public static float[] add(float[] array, int index, float element) {
    return (float[]) add(array, index, new Float(element), Float.TYPE);
  }

  /**
   * <p>Inserts the specified element at the specified position in the array.
   * Shifts the element currently at that position (if any) and any subsequent
   * elements to the right (adds one to their indices).</p>
   * <p/>
   * <p>This method returns a new array with the same elements of the input
   * array plus the given element on the specified position. The component
   * type of the returned array is always the same as that of the input
   * array.</p>
   * <p/>
   * <p>If the input array is <code>null</code>, a new one element array is returned
   * whose component type is the same as the element.</p>
   * <p/>
   * <pre>
   * ArrayUtils.add([1.1], 0, 2.2)              = [2.2, 1.1]
   * ArrayUtils.add([2.3, 6.4], 2, 10.5)        = [2.3, 6.4, 10.5]
   * ArrayUtils.add([2.6, 6.7], 0, -4.8)        = [-4.8, 2.6, 6.7]
   * ArrayUtils.add([2.9, 6.0, 0.3], 2, 1.0)    = [2.9, 6.0, 1.0, 0.3]
   * </pre>
   *
   * @param array   the array to add the element to, may be <code>null</code>
   * @param index   the position of the new object
   * @param element the object to add
   * @return A new array containing the existing elements and the new element
   * @throws IndexOutOfBoundsException if the index is out of range
   *                                   (index < 0 || index > array.length).
   */
  public static double[] add(double[] array, int index, double element) {
    return (double[]) add(array, index, new Double(element), Double.TYPE);
  }

  /**
   * Underlying implementation of add(array, index, element) methods.
   * The last parameter is the class, which may not equal element.getClass
   * for primitives.
   *
   * @param array   the array to add the element to, may be <code>null</code>
   * @param index   the position of the new object
   * @param element the object to add
   * @param clss    the type of the element being added
   * @return A new array containing the existing elements and the new element
   */
  private static Object add(Object array, int index, Object element, Class clss) {
    if (array == null) {
      if (index != 0) {
        throw new IndexOutOfBoundsException("Index: " + index + ", Length: 0");
      }
      Object joinedArray = Array.newInstance(clss, 1);
      Array.set(joinedArray, 0, element);
      return joinedArray;
    }
    int length = Array.getLength(array);
    if (index > length || index < 0) {
      throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + length);
    }
    Object result = Array.newInstance(clss, length + 1);
    System.arraycopy(array, 0, result, 0, index);
    Array.set(result, index, element);
    if (index < length) {
      System.arraycopy(array, index, result, index + 1, length - index);
    }
    return result;
  }

  /**
   * <p>Removes the element at the specified position from the specified array.
   * All subsequent elements are shifted to the left (substracts one from
   * their indices).</p>
   * <p/>
   * <p>This method returns a new array with the same elements of the input
   * array except the element on the specified position. The component
   * type of the returned array is always the same as that of the input
   * array.</p>
   * <p/>
   * <p>If the input array is <code>null</code>, an IndexOutOfBoundsException
   * will be thrown, because in that case no valid index can be specified.</p>
   * <p/>
   * <pre>
   * ArrayUtils.remove(["a"], 0)           = []
   * ArrayUtils.remove(["a", "b"], 0)      = ["b"]
   * ArrayUtils.remove(["a", "b"], 1)      = ["a"]
   * ArrayUtils.remove(["a", "b", "c"], 1) = ["a", "c"]
   * </pre>
   *
   * @param array the array to remove the element from, may not be <code>null</code>
   * @param index the position of the element to be removed
   * @return A new array containing the existing elements except the element
   *         at the specified position.
   * @throws IndexOutOfBoundsException if the index is out of range
   *                                   (index < 0 || index >= array.length), or if the array is <code>null</code>.
   * @since 2.1
   */
  public static Object[] remove(Object[] array, int index) {
    return (Object[]) remove((Object) array, index);
  }


  /**
   * <p>Removes the element at the specified position from the specified array.
   * All subsequent elements are shifted to the left (substracts one from
   * their indices).</p>
   * <p/>
   * <p>This method returns a new array with the same elements of the input
   * array except the element on the specified position. The component
   * type of the returned array is always the same as that of the input
   * array.</p>
   * <p/>
   * <p>If the input array is <code>null</code>, an IndexOutOfBoundsException
   * will be thrown, because in that case no valid index can be specified.</p>
   * <p/>
   * <pre>
   * ArrayUtils.remove([true], 0)              = []
   * ArrayUtils.remove([true, false], 0)       = [false]
   * ArrayUtils.remove([true, false], 1)       = [true]
   * ArrayUtils.remove([true, true, false], 1) = [true, false]
   * </pre>
   *
   * @param array the array to remove the element from, may not be <code>null</code>
   * @param index the position of the element to be removed
   * @return A new array containing the existing elements except the element
   *         at the specified position.
   * @throws IndexOutOfBoundsException if the index is out of range
   *                                   (index < 0 || index >= array.length), or if the array is <code>null</code>.
   * @since 2.1
   */
  public static boolean[] remove(boolean[] array, int index) {
    return (boolean[]) remove((Object) array, index);
  }


  /**
   * <p>Removes the element at the specified position from the specified array.
   * All subsequent elements are shifted to the left (substracts one from
   * their indices).</p>
   * <p/>
   * <p>This method returns a new array with the same elements of the input
   * array except the element on the specified position. The component
   * type of the returned array is always the same as that of the input
   * array.</p>
   * <p/>
   * <p>If the input array is <code>null</code>, an IndexOutOfBoundsException
   * will be thrown, because in that case no valid index can be specified.</p>
   * <p/>
   * <pre>
   * ArrayUtils.remove([1], 0)          = []
   * ArrayUtils.remove([1, 0], 0)       = [0]
   * ArrayUtils.remove([1, 0], 1)       = [1]
   * ArrayUtils.remove([1, 0, 1], 1)    = [1, 1]
   * </pre>
   *
   * @param array the array to remove the element from, may not be <code>null</code>
   * @param index the position of the element to be removed
   * @return A new array containing the existing elements except the element
   *         at the specified position.
   * @throws IndexOutOfBoundsException if the index is out of range
   *                                   (index < 0 || index >= array.length), or if the array is <code>null</code>.
   * @since 2.1
   */
  public static byte[] remove(byte[] array, int index) {
    return (byte[]) remove((Object) array, index);
  }


  /**
   * <p>Removes the element at the specified position from the specified array.
   * All subsequent elements are shifted to the left (substracts one from
   * their indices).</p>
   * <p/>
   * <p>This method returns a new array with the same elements of the input
   * array except the element on the specified position. The component
   * type of the returned array is always the same as that of the input
   * array.</p>
   * <p/>
   * <p>If the input array is <code>null</code>, an IndexOutOfBoundsException
   * will be thrown, because in that case no valid index can be specified.</p>
   * <p/>
   * <pre>
   * ArrayUtils.remove(['a'], 0)           = []
   * ArrayUtils.remove(['a', 'b'], 0)      = ['b']
   * ArrayUtils.remove(['a', 'b'], 1)      = ['a']
   * ArrayUtils.remove(['a', 'b', 'c'], 1) = ['a', 'c']
   * </pre>
   *
   * @param array the array to remove the element from, may not be <code>null</code>
   * @param index the position of the element to be removed
   * @return A new array containing the existing elements except the element
   *         at the specified position.
   * @throws IndexOutOfBoundsException if the index is out of range
   *                                   (index < 0 || index >= array.length), or if the array is <code>null</code>.
   * @since 2.1
   */
  public static char[] remove(char[] array, int index) {
    return (char[]) remove((Object) array, index);
  }


  /**
   * <p>Removes the element at the specified position from the specified array.
   * All subsequent elements are shifted to the left (substracts one from
   * their indices).</p>
   * <p/>
   * <p>This method returns a new array with the same elements of the input
   * array except the element on the specified position. The component
   * type of the returned array is always the same as that of the input
   * array.</p>
   * <p/>
   * <p>If the input array is <code>null</code>, an IndexOutOfBoundsException
   * will be thrown, because in that case no valid index can be specified.</p>
   * <p/>
   * <pre>
   * ArrayUtils.remove([1.1], 0)           = []
   * ArrayUtils.remove([2.5, 6.0], 0)      = [6.0]
   * ArrayUtils.remove([2.5, 6.0], 1)      = [2.5]
   * ArrayUtils.remove([2.5, 6.0, 3.8], 1) = [2.5, 3.8]
   * </pre>
   *
   * @param array the array to remove the element from, may not be <code>null</code>
   * @param index the position of the element to be removed
   * @return A new array containing the existing elements except the element
   *         at the specified position.
   * @throws IndexOutOfBoundsException if the index is out of range
   *                                   (index < 0 || index >= array.length), or if the array is <code>null</code>.
   * @since 2.1
   */
  public static double[] remove(double[] array, int index) {
    return (double[]) remove((Object) array, index);
  }


  /**
   * <p>Removes the element at the specified position from the specified array.
   * All subsequent elements are shifted to the left (substracts one from
   * their indices).</p>
   * <p/>
   * <p>This method returns a new array with the same elements of the input
   * array except the element on the specified position. The component
   * type of the returned array is always the same as that of the input
   * array.</p>
   * <p/>
   * <p>If the input array is <code>null</code>, an IndexOutOfBoundsException
   * will be thrown, because in that case no valid index can be specified.</p>
   * <p/>
   * <pre>
   * ArrayUtils.remove([1.1], 0)           = []
   * ArrayUtils.remove([2.5, 6.0], 0)      = [6.0]
   * ArrayUtils.remove([2.5, 6.0], 1)      = [2.5]
   * ArrayUtils.remove([2.5, 6.0, 3.8], 1) = [2.5, 3.8]
   * </pre>
   *
   * @param array the array to remove the element from, may not be <code>null</code>
   * @param index the position of the element to be removed
   * @return A new array containing the existing elements except the element
   *         at the specified position.
   * @throws IndexOutOfBoundsException if the index is out of range
   *                                   (index < 0 || index >= array.length), or if the array is <code>null</code>.
   * @since 2.1
   */
  public static float[] remove(float[] array, int index) {
    return (float[]) remove((Object) array, index);
  }


  /**
   * <p>Removes the element at the specified position from the specified array.
   * All subsequent elements are shifted to the left (substracts one from
   * their indices).</p>
   * <p/>
   * <p>This method returns a new array with the same elements of the input
   * array except the element on the specified position. The component
   * type of the returned array is always the same as that of the input
   * array.</p>
   * <p/>
   * <p>If the input array is <code>null</code>, an IndexOutOfBoundsException
   * will be thrown, because in that case no valid index can be specified.</p>
   * <p/>
   * <pre>
   * ArrayUtils.remove([1], 0)         = []
   * ArrayUtils.remove([2, 6], 0)      = [6]
   * ArrayUtils.remove([2, 6], 1)      = [2]
   * ArrayUtils.remove([2, 6, 3], 1)   = [2, 3]
   * </pre>
   *
   * @param array the array to remove the element from, may not be <code>null</code>
   * @param index the position of the element to be removed
   * @return A new array containing the existing elements except the element
   *         at the specified position.
   * @throws IndexOutOfBoundsException if the index is out of range
   *                                   (index < 0 || index >= array.length), or if the array is <code>null</code>.
   * @since 2.1
   */
  public static int[] remove(int[] array, int index) {
    return (int[]) remove((Object) array, index);
  }


  /**
   * <p>Removes the element at the specified position from the specified array.
   * All subsequent elements are shifted to the left (substracts one from
   * their indices).</p>
   * <p/>
   * <p>This method returns a new array with the same elements of the input
   * array except the element on the specified position. The component
   * type of the returned array is always the same as that of the input
   * array.</p>
   * <p/>
   * <p>If the input array is <code>null</code>, an IndexOutOfBoundsException
   * will be thrown, because in that case no valid index can be specified.</p>
   * <p/>
   * <pre>
   * ArrayUtils.remove([1], 0)         = []
   * ArrayUtils.remove([2, 6], 0)      = [6]
   * ArrayUtils.remove([2, 6], 1)      = [2]
   * ArrayUtils.remove([2, 6, 3], 1)   = [2, 3]
   * </pre>
   *
   * @param array the array to remove the element from, may not be <code>null</code>
   * @param index the position of the element to be removed
   * @return A new array containing the existing elements except the element
   *         at the specified position.
   * @throws IndexOutOfBoundsException if the index is out of range
   *                                   (index < 0 || index >= array.length), or if the array is <code>null</code>.
   * @since 2.1
   */
  public static long[] remove(long[] array, int index) {
    return (long[]) remove((Object) array, index);
  }


  /**
   * <p>Removes the element at the specified position from the specified array.
   * All subsequent elements are shifted to the left (substracts one from
   * their indices).</p>
   * <p/>
   * <p>This method returns a new array with the same elements of the input
   * array except the element on the specified position. The component
   * type of the returned array is always the same as that of the input
   * array.</p>
   * <p/>
   * <p>If the input array is <code>null</code>, an IndexOutOfBoundsException
   * will be thrown, because in that case no valid index can be specified.</p>
   * <p/>
   * <pre>
   * ArrayUtils.remove([1], 0)         = []
   * ArrayUtils.remove([2, 6], 0)      = [6]
   * ArrayUtils.remove([2, 6], 1)      = [2]
   * ArrayUtils.remove([2, 6, 3], 1)   = [2, 3]
   * </pre>
   *
   * @param array the array to remove the element from, may not be <code>null</code>
   * @param index the position of the element to be removed
   * @return A new array containing the existing elements except the element
   *         at the specified position.
   * @throws IndexOutOfBoundsException if the index is out of range
   *                                   (index < 0 || index >= array.length), or if the array is <code>null</code>.
   * @since 2.1
   */
  public static short[] remove(short[] array, int index) {
    return (short[]) remove((Object) array, index);
  }


  /**
   * <p>Removes the element at the specified position from the specified array.
   * All subsequent elements are shifted to the left (substracts one from
   * their indices).</p>
   * <p/>
   * <p>This method returns a new array with the same elements of the input
   * array except the element on the specified position. The component
   * type of the returned array is always the same as that of the input
   * array.</p>
   * <p/>
   * <p>If the input array is <code>null</code>, an IndexOutOfBoundsException
   * will be thrown, because in that case no valid index can be specified.</p>
   *
   * @param array the array to remove the element from, may not be <code>null</code>
   * @param index the position of the element to be removed
   * @return A new array containing the existing elements except the element
   *         at the specified position.
   * @throws IndexOutOfBoundsException if the index is out of range
   *                                   (index < 0 || index >= array.length), or if the array is <code>null</code>.
   * @since 2.1
   */
  private static Object remove(Object array, int index) {
    int length = getLength(array);
    if (index < 0 || index >= length) {
      throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + length);
    }

    Object result = Array.newInstance(array.getClass().getComponentType(), length - 1);
    System.arraycopy(array, 0, result, 0, index);
    if (index < length - 1) {
      System.arraycopy(array, index + 1, result, index, length - index - 1);
    }

    return result;
  }

}
