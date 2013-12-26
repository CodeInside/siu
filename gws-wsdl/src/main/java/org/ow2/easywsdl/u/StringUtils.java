package org.ow2.easywsdl.u;

public class StringUtils {

  /**
   * The empty String <code>""</code>.
   *
   * @since 2.0
   */
  public static final String EMPTY = "";

  /**
   * <p>The maximum size to which the padding constant(s) can expand.</p>
   */
  private static final int PAD_LIMIT = 8192;


  // Empty checks
  //-----------------------------------------------------------------------

  /**
   * <p>Checks if a String is empty ("") or null.</p>
   * <p/>
   * <pre>
   * StringUtils.isEmpty(null)      = true
   * StringUtils.isEmpty("")        = true
   * StringUtils.isEmpty(" ")       = false
   * StringUtils.isEmpty("bob")     = false
   * StringUtils.isEmpty("  bob  ") = false
   * </pre>
   * <p/>
   * <p>NOTE: This method changed in Lang version 2.0.
   * It no longer trims the String.
   * That functionality is available in isBlank().</p>
   *
   * @param str the String to check, may be null
   * @return <code>true</code> if the String is empty or null
   */
  public static boolean isEmpty(String str) {
    return str == null || str.length() == 0;
  }


  /**
   * <p>Checks if a String is whitespace, empty ("") or null.</p>
   * <p/>
   * <pre>
   * StringUtils.isBlank(null)      = true
   * StringUtils.isBlank("")        = true
   * StringUtils.isBlank(" ")       = true
   * StringUtils.isBlank("bob")     = false
   * StringUtils.isBlank("  bob  ") = false
   * </pre>
   *
   * @param str the String to check, may be null
   * @return <code>true</code> if the String is null, empty or whitespace
   * @since 2.0
   */
  public static boolean isBlank(String str) {
    int strLen;
    if (str == null || (strLen = str.length()) == 0) {
      return true;
    }
    for (int i = 0; i < strLen; i++) {
      if ((Character.isWhitespace(str.charAt(i)) == false)) {
        return false;
      }
    }
    return true;
  }


  // Equals
  //-----------------------------------------------------------------------

  /**
   * <p>Compares two Strings, returning <code>true</code> if they are equal.</p>
   * <p/>
   * <p><code>null</code>s are handled without exceptions. Two <code>null</code>
   * references are considered to be equal. The comparison is case sensitive.</p>
   * <p/>
   * <pre>
   * StringUtils.equals(null, null)   = true
   * StringUtils.equals(null, "abc")  = false
   * StringUtils.equals("abc", null)  = false
   * StringUtils.equals("abc", "abc") = true
   * StringUtils.equals("abc", "ABC") = false
   * </pre>
   *
   * @param str1 the first String, may be null
   * @param str2 the second String, may be null
   * @return <code>true</code> if the Strings are equal, case sensitive, or
   *         both <code>null</code>
   * @see java.lang.String#equals(Object)
   */
  public static boolean equals(String str1, String str2) {
    return str1 == null ? str2 == null : str1.equals(str2);
  }


  // IndexOf
  //-----------------------------------------------------------------------

  /**
   * <p>Finds the first index within a String, handling <code>null</code>.
   * This method uses {@link String#indexOf(int)}.</p>
   * <p/>
   * <p>A <code>null</code> or empty ("") String will return <code>-1</code>.</p>
   * <p/>
   * <pre>
   * StringUtils.indexOf(null, *)         = -1
   * StringUtils.indexOf("", *)           = -1
   * StringUtils.indexOf("aabaabaa", 'a') = 0
   * StringUtils.indexOf("aabaabaa", 'b') = 2
   * </pre>
   *
   * @param str        the String to check, may be null
   * @param searchChar the character to find
   * @return the first index of the search character,
   *         -1 if no match or <code>null</code> string input
   * @since 2.0
   */
  public static int indexOf(String str, char searchChar) {
    if (isEmpty(str)) {
      return -1;
    }
    return str.indexOf(searchChar);
  }

  /**
   * <p>Finds the first index within a String from a start position,
   * handling <code>null</code>.
   * This method uses {@link String#indexOf(int, int)}.</p>
   * <p/>
   * <p>A <code>null</code> or empty ("") String will return <code>-1</code>.
   * A negative start position is treated as zero.
   * A start position greater than the string length returns <code>-1</code>.</p>
   * <p/>
   * <pre>
   * StringUtils.indexOf(null, *, *)          = -1
   * StringUtils.indexOf("", *, *)            = -1
   * StringUtils.indexOf("aabaabaa", 'b', 0)  = 2
   * StringUtils.indexOf("aabaabaa", 'b', 3)  = 5
   * StringUtils.indexOf("aabaabaa", 'b', 9)  = -1
   * StringUtils.indexOf("aabaabaa", 'b', -1) = 2
   * </pre>
   *
   * @param str        the String to check, may be null
   * @param searchChar the character to find
   * @param startPos   the start position, negative treated as zero
   * @return the first index of the search character,
   *         -1 if no match or <code>null</code> string input
   * @since 2.0
   */
  public static int indexOf(String str, char searchChar, int startPos) {
    if (isEmpty(str)) {
      return -1;
    }
    return str.indexOf(searchChar, startPos);
  }

  /**
   * <p>Finds the first index within a String, handling <code>null</code>.
   * This method uses {@link String#indexOf(String)}.</p>
   * <p/>
   * <p>A <code>null</code> String will return <code>-1</code>.</p>
   * <p/>
   * <pre>
   * StringUtils.indexOf(null, *)          = -1
   * StringUtils.indexOf(*, null)          = -1
   * StringUtils.indexOf("", "")           = 0
   * StringUtils.indexOf("aabaabaa", "a")  = 0
   * StringUtils.indexOf("aabaabaa", "b")  = 2
   * StringUtils.indexOf("aabaabaa", "ab") = 1
   * StringUtils.indexOf("aabaabaa", "")   = 0
   * </pre>
   *
   * @param str       the String to check, may be null
   * @param searchStr the String to find, may be null
   * @return the first index of the search String,
   *         -1 if no match or <code>null</code> string input
   * @since 2.0
   */
  public static int indexOf(String str, String searchStr) {
    if (str == null || searchStr == null) {
      return -1;
    }
    return str.indexOf(searchStr);
  }


  /**
   * <p>Finds the first index within a String, handling <code>null</code>.
   * This method uses {@link String#indexOf(String, int)}.</p>
   * <p/>
   * <p>A <code>null</code> String will return <code>-1</code>.
   * A negative start position is treated as zero.
   * An empty ("") search String always matches.
   * A start position greater than the string length only matches
   * an empty search String.</p>
   * <p/>
   * <pre>
   * StringUtils.indexOf(null, *, *)          = -1
   * StringUtils.indexOf(*, null, *)          = -1
   * StringUtils.indexOf("", "", 0)           = 0
   * StringUtils.indexOf("aabaabaa", "a", 0)  = 0
   * StringUtils.indexOf("aabaabaa", "b", 0)  = 2
   * StringUtils.indexOf("aabaabaa", "ab", 0) = 1
   * StringUtils.indexOf("aabaabaa", "b", 3)  = 5
   * StringUtils.indexOf("aabaabaa", "b", 9)  = -1
   * StringUtils.indexOf("aabaabaa", "b", -1) = 2
   * StringUtils.indexOf("aabaabaa", "", 2)   = 2
   * StringUtils.indexOf("abc", "", 9)        = 3
   * </pre>
   *
   * @param str       the String to check, may be null
   * @param searchStr the String to find, may be null
   * @param startPos  the start position, negative treated as zero
   * @return the first index of the search String,
   *         -1 if no match or <code>null</code> string input
   * @since 2.0
   */
  public static int indexOf(String str, String searchStr, int startPos) {
    if (str == null || searchStr == null) {
      return -1;
    }
    // JDK1.2/JDK1.3 have a bug, when startPos > str.length for "", hence
    if (searchStr.length() == 0 && startPos >= str.length()) {
      return str.length();
    }
    return str.indexOf(searchStr, startPos);
  }


  // Contains
  //-----------------------------------------------------------------------

  /**
   * <p>Checks if String contains a search character, handling <code>null</code>.
   * This method uses {@link String#indexOf(int)}.</p>
   * <p/>
   * <p>A <code>null</code> or empty ("") String will return <code>false</code>.</p>
   * <p/>
   * <pre>
   * StringUtils.contains(null, *)    = false
   * StringUtils.contains("", *)      = false
   * StringUtils.contains("abc", 'a') = true
   * StringUtils.contains("abc", 'z') = false
   * </pre>
   *
   * @param str        the String to check, may be null
   * @param searchChar the character to find
   * @return true if the String contains the search character,
   *         false if not or <code>null</code> string input
   * @since 2.0
   */
  public static boolean contains(String str, char searchChar) {
    if (isEmpty(str)) {
      return false;
    }
    return str.indexOf(searchChar) >= 0;
  }

  /**
   * <p>Checks if String contains a search String, handling <code>null</code>.
   * This method uses {@link String#indexOf(String)}.</p>
   * <p/>
   * <p>A <code>null</code> String will return <code>false</code>.</p>
   * <p/>
   * <pre>
   * StringUtils.contains(null, *)     = false
   * StringUtils.contains(*, null)     = false
   * StringUtils.contains("", "")      = true
   * StringUtils.contains("abc", "")   = true
   * StringUtils.contains("abc", "a")  = true
   * StringUtils.contains("abc", "z")  = false
   * </pre>
   *
   * @param str       the String to check, may be null
   * @param searchStr the String to find, may be null
   * @return true if the String contains the search String,
   *         false if not or <code>null</code> string input
   * @since 2.0
   */
  public static boolean contains(String str, String searchStr) {
    if (str == null || searchStr == null) {
      return false;
    }
    return str.indexOf(searchStr) >= 0;
  }


  /**
   * <p>Deletes all whitespaces from a String as defined by
   * {@link Character#isWhitespace(char)}.</p>
   * <p/>
   * <pre>
   * StringUtils.deleteWhitespace(null)         = null
   * StringUtils.deleteWhitespace("")           = ""
   * StringUtils.deleteWhitespace("abc")        = "abc"
   * StringUtils.deleteWhitespace("   ab  c  ") = "abc"
   * </pre>
   *
   * @param str the String to delete whitespace from, may be null
   * @return the String without whitespaces, <code>null</code> if null String input
   */
  public static String deleteWhitespace(String str) {
    if (isEmpty(str)) {
      return str;
    }
    int sz = str.length();
    char[] chs = new char[sz];
    int count = 0;
    for (int i = 0; i < sz; i++) {
      if (!Character.isWhitespace(str.charAt(i))) {
        chs[count++] = str.charAt(i);
      }
    }
    if (count == sz) {
      return str;
    }
    return new String(chs, 0, count);
  }


  /**
   * <p>Removes all occurrences of a substring from within the source string.</p>
   * <p/>
   * <p>A <code>null</code> source string will return <code>null</code>.
   * An empty ("") source string will return the empty string.
   * A <code>null</code> remove string will return the source string.
   * An empty ("") remove string will return the source string.</p>
   * <p/>
   * <pre>
   * StringUtils.remove(null, *)        = null
   * StringUtils.remove("", *)          = ""
   * StringUtils.remove(*, null)        = *
   * StringUtils.remove(*, "")          = *
   * StringUtils.remove("queued", "ue") = "qd"
   * StringUtils.remove("queued", "zz") = "queued"
   * </pre>
   *
   * @param str    the source String to search, may be null
   * @param remove the String to search for and remove, may be null
   * @return the substring with the string removed if found,
   *         <code>null</code> if null String input
   * @since 2.1
   */
  public static String remove(String str, String remove) {
    if (isEmpty(str) || isEmpty(remove)) {
      return str;
    }
    return replace(str, remove, EMPTY, -1);
  }

  /**
   * <p>Removes all occurrences of a character from within the source string.</p>
   * <p/>
   * <p>A <code>null</code> source string will return <code>null</code>.
   * An empty ("") source string will return the empty string.</p>
   * <p/>
   * <pre>
   * StringUtils.remove(null, *)       = null
   * StringUtils.remove("", *)         = ""
   * StringUtils.remove("queued", 'u') = "qeed"
   * StringUtils.remove("queued", 'z') = "queued"
   * </pre>
   *
   * @param str    the source String to search, may be null
   * @param remove the char to search for and remove, may be null
   * @return the substring with the char removed if found,
   *         <code>null</code> if null String input
   * @since 2.1
   */
  public static String remove(String str, char remove) {
    if (isEmpty(str) || str.indexOf(remove) == -1) {
      return str;
    }
    char[] chars = str.toCharArray();
    int pos = 0;
    for (int i = 0; i < chars.length; i++) {
      if (chars[i] != remove) {
        chars[pos++] = chars[i];
      }
    }
    return new String(chars, 0, pos);
  }


  /**
   * <p>Replaces all occurrences of a String within another String.</p>
   * <p/>
   * <p>A <code>null</code> reference passed to this method is a no-op.</p>
   * <p/>
   * <pre>
   * StringUtils.replace(null, *, *)        = null
   * StringUtils.replace("", *, *)          = ""
   * StringUtils.replace("any", null, *)    = "any"
   * StringUtils.replace("any", *, null)    = "any"
   * StringUtils.replace("any", "", *)      = "any"
   * StringUtils.replace("aba", "a", null)  = "aba"
   * StringUtils.replace("aba", "a", "")    = "b"
   * StringUtils.replace("aba", "a", "z")   = "zbz"
   * </pre>
   *
   * @param text         text to search and replace in, may be null
   * @param searchString the String to search for, may be null
   * @param replacement  the String to replace it with, may be null
   * @return the text with any replacements processed,
   *         <code>null</code> if null String input
   * @see #replace(String text, String searchString, String replacement, int max)
   */
  public static String replace(String text, String searchString, String replacement) {
    return replace(text, searchString, replacement, -1);
  }

  /**
   * <p>Replaces a String with another String inside a larger String,
   * for the first <code>max</code> values of the search String.</p>
   * <p/>
   * <p>A <code>null</code> reference passed to this method is a no-op.</p>
   * <p/>
   * <pre>
   * StringUtils.replace(null, *, *, *)         = null
   * StringUtils.replace("", *, *, *)           = ""
   * StringUtils.replace("any", null, *, *)     = "any"
   * StringUtils.replace("any", *, null, *)     = "any"
   * StringUtils.replace("any", "", *, *)       = "any"
   * StringUtils.replace("any", *, *, 0)        = "any"
   * StringUtils.replace("abaa", "a", null, -1) = "abaa"
   * StringUtils.replace("abaa", "a", "", -1)   = "b"
   * StringUtils.replace("abaa", "a", "z", 0)   = "abaa"
   * StringUtils.replace("abaa", "a", "z", 1)   = "zbaa"
   * StringUtils.replace("abaa", "a", "z", 2)   = "zbza"
   * StringUtils.replace("abaa", "a", "z", -1)  = "zbzz"
   * </pre>
   *
   * @param text         text to search and replace in, may be null
   * @param searchString the String to search for, may be null
   * @param replacement  the String to replace it with, may be null
   * @param max          maximum number of values to replace, or <code>-1</code> if no maximum
   * @return the text with any replacements processed,
   *         <code>null</code> if null String input
   */
  public static String replace(String text, String searchString, String replacement, int max) {
    if (isEmpty(text) || isEmpty(searchString) || replacement == null || max == 0) {
      return text;
    }
    int start = 0;
    int end = text.indexOf(searchString, start);
    if (end == -1) {
      return text;
    }
    int replLength = searchString.length();
    int increase = replacement.length() - replLength;
    increase = (increase < 0 ? 0 : increase);
    increase *= (max < 0 ? 16 : (max > 64 ? 64 : max));
    StringBuffer buf = new StringBuffer(text.length() + increase);
    while (end != -1) {
      buf.append(text.substring(start, end)).append(replacement);
      start = end + replLength;
      if (--max == 0) {
        break;
      }
      end = text.indexOf(searchString, start);
    }
    buf.append(text.substring(start));
    return buf.toString();
  }


  // Padding
  //-----------------------------------------------------------------------

  /**
   * <p>Repeat a String <code>repeat</code> times to form a
   * new String.</p>
   * <p/>
   * <pre>
   * StringUtils.repeat(null, 2) = null
   * StringUtils.repeat("", 0)   = ""
   * StringUtils.repeat("", 2)   = ""
   * StringUtils.repeat("a", 3)  = "aaa"
   * StringUtils.repeat("ab", 2) = "abab"
   * StringUtils.repeat("a", -2) = ""
   * </pre>
   *
   * @param str    the String to repeat, may be null
   * @param repeat number of times to repeat str, negative treated as zero
   * @return a new String consisting of the original String repeated,
   *         <code>null</code> if null String input
   */
  public static String repeat(String str, int repeat) {
    // Performance tuned for 2.0 (JDK1.4)

    if (str == null) {
      return null;
    }
    if (repeat <= 0) {
      return EMPTY;
    }
    int inputLength = str.length();
    if (repeat == 1 || inputLength == 0) {
      return str;
    }
    if (inputLength == 1 && repeat <= PAD_LIMIT) {
      return padding(repeat, str.charAt(0));
    }

    int outputLength = inputLength * repeat;
    switch (inputLength) {
      case 1:
        char ch = str.charAt(0);
        char[] output1 = new char[outputLength];
        for (int i = repeat - 1; i >= 0; i--) {
          output1[i] = ch;
        }
        return new String(output1);
      case 2:
        char ch0 = str.charAt(0);
        char ch1 = str.charAt(1);
        char[] output2 = new char[outputLength];
        for (int i = repeat * 2 - 2; i >= 0; i--, i--) {
          output2[i] = ch0;
          output2[i + 1] = ch1;
        }
        return new String(output2);
      default:
        StringBuffer buf = new StringBuffer(outputLength);
        for (int i = 0; i < repeat; i++) {
          buf.append(str);
        }
        return buf.toString();
    }
  }

  /**
   * <p>Returns padding using the specified delimiter repeated
   * to a given length.</p>
   * <p/>
   * <pre>
   * StringUtils.padding(0, 'e')  = ""
   * StringUtils.padding(3, 'e')  = "eee"
   * StringUtils.padding(-2, 'e') = IndexOutOfBoundsException
   * </pre>
   * <p/>
   * <p>Note: this method doesn't not support padding with
   * <a href="http://www.unicode.org/glossary/#supplementary_character">Unicode Supplementary Characters</a>
   * as they require a pair of <code>char</code>s to be represented.
   * If you are needing to support full I18N of your applications
   * consider using {@link #repeat(String, int)} instead.
   * </p>
   *
   * @param repeat  number of times to repeat delim
   * @param padChar character to repeat
   * @return String with repeated character
   * @throws IndexOutOfBoundsException if <code>repeat &lt; 0</code>
   * @see #repeat(String, int)
   */
  private static String padding(int repeat, char padChar) throws IndexOutOfBoundsException {
    if (repeat < 0) {
      throw new IndexOutOfBoundsException("Cannot pad a negative amount: " + repeat);
    }
    final char[] buf = new char[repeat];
    for (int i = 0; i < buf.length; i++) {
      buf[i] = padChar;
    }
    return new String(buf);
  }


  /**
   * <p>Right pad a String with a specified character.</p>
   * <p/>
   * <p>The String is padded to the size of <code>size</code>.</p>
   * <p/>
   * <pre>
   * StringUtils.rightPad(null, *, *)     = null
   * StringUtils.rightPad("", 3, 'z')     = "zzz"
   * StringUtils.rightPad("bat", 3, 'z')  = "bat"
   * StringUtils.rightPad("bat", 5, 'z')  = "batzz"
   * StringUtils.rightPad("bat", 1, 'z')  = "bat"
   * StringUtils.rightPad("bat", -1, 'z') = "bat"
   * </pre>
   *
   * @param str     the String to pad out, may be null
   * @param size    the size to pad to
   * @param padChar the character to pad with
   * @return right padded String or original String if no padding is necessary,
   *         <code>null</code> if null String input
   * @since 2.0
   */
  public static String rightPad(String str, int size, char padChar) {
    if (str == null) {
      return null;
    }
    int pads = size - str.length();
    if (pads <= 0) {
      return str; // returns original String when possible
    }
    if (pads > PAD_LIMIT) {
      return rightPad(str, size, String.valueOf(padChar));
    }
    return str.concat(padding(pads, padChar));
  }

  /**
   * <p>Right pad a String with a specified String.</p>
   * <p/>
   * <p>The String is padded to the size of <code>size</code>.</p>
   * <p/>
   * <pre>
   * StringUtils.rightPad(null, *, *)      = null
   * StringUtils.rightPad("", 3, "z")      = "zzz"
   * StringUtils.rightPad("bat", 3, "yz")  = "bat"
   * StringUtils.rightPad("bat", 5, "yz")  = "batyz"
   * StringUtils.rightPad("bat", 8, "yz")  = "batyzyzy"
   * StringUtils.rightPad("bat", 1, "yz")  = "bat"
   * StringUtils.rightPad("bat", -1, "yz") = "bat"
   * StringUtils.rightPad("bat", 5, null)  = "bat  "
   * StringUtils.rightPad("bat", 5, "")    = "bat  "
   * </pre>
   *
   * @param str    the String to pad out, may be null
   * @param size   the size to pad to
   * @param padStr the String to pad with, null or empty treated as single space
   * @return right padded String or original String if no padding is necessary,
   *         <code>null</code> if null String input
   */
  public static String rightPad(String str, int size, String padStr) {
    if (str == null) {
      return null;
    }
    if (isEmpty(padStr)) {
      padStr = " ";
    }
    int padLen = padStr.length();
    int strLen = str.length();
    int pads = size - strLen;
    if (pads <= 0) {
      return str; // returns original String when possible
    }
    if (padLen == 1 && pads <= PAD_LIMIT) {
      return rightPad(str, size, padStr.charAt(0));
    }

    if (pads == padLen) {
      return str.concat(padStr);
    } else if (pads < padLen) {
      return str.concat(padStr.substring(0, pads));
    } else {
      char[] padding = new char[pads];
      char[] padChars = padStr.toCharArray();
      for (int i = 0; i < pads; i++) {
        padding[i] = padChars[i % padLen];
      }
      return str.concat(new String(padding));
    }
  }


  /**
   * <p>Left pad a String with a specified character.</p>
   * <p/>
   * <p>Pad to a size of <code>size</code>.</p>
   * <p/>
   * <pre>
   * StringUtils.leftPad(null, *, *)     = null
   * StringUtils.leftPad("", 3, 'z')     = "zzz"
   * StringUtils.leftPad("bat", 3, 'z')  = "bat"
   * StringUtils.leftPad("bat", 5, 'z')  = "zzbat"
   * StringUtils.leftPad("bat", 1, 'z')  = "bat"
   * StringUtils.leftPad("bat", -1, 'z') = "bat"
   * </pre>
   *
   * @param str     the String to pad out, may be null
   * @param size    the size to pad to
   * @param padChar the character to pad with
   * @return left padded String or original String if no padding is necessary,
   *         <code>null</code> if null String input
   * @since 2.0
   */
  public static String leftPad(String str, int size, char padChar) {
    if (str == null) {
      return null;
    }
    int pads = size - str.length();
    if (pads <= 0) {
      return str; // returns original String when possible
    }
    if (pads > PAD_LIMIT) {
      return leftPad(str, size, String.valueOf(padChar));
    }
    return padding(pads, padChar).concat(str);
  }

  /**
   * <p>Left pad a String with a specified String.</p>
   * <p/>
   * <p>Pad to a size of <code>size</code>.</p>
   * <p/>
   * <pre>
   * StringUtils.leftPad(null, *, *)      = null
   * StringUtils.leftPad("", 3, "z")      = "zzz"
   * StringUtils.leftPad("bat", 3, "yz")  = "bat"
   * StringUtils.leftPad("bat", 5, "yz")  = "yzbat"
   * StringUtils.leftPad("bat", 8, "yz")  = "yzyzybat"
   * StringUtils.leftPad("bat", 1, "yz")  = "bat"
   * StringUtils.leftPad("bat", -1, "yz") = "bat"
   * StringUtils.leftPad("bat", 5, null)  = "  bat"
   * StringUtils.leftPad("bat", 5, "")    = "  bat"
   * </pre>
   *
   * @param str    the String to pad out, may be null
   * @param size   the size to pad to
   * @param padStr the String to pad with, null or empty treated as single space
   * @return left padded String or original String if no padding is necessary,
   *         <code>null</code> if null String input
   */
  public static String leftPad(String str, int size, String padStr) {
    if (str == null) {
      return null;
    }
    if (isEmpty(padStr)) {
      padStr = " ";
    }
    int padLen = padStr.length();
    int strLen = str.length();
    int pads = size - strLen;
    if (pads <= 0) {
      return str; // returns original String when possible
    }
    if (padLen == 1 && pads <= PAD_LIMIT) {
      return leftPad(str, size, padStr.charAt(0));
    }

    if (pads == padLen) {
      return padStr.concat(str);
    } else if (pads < padLen) {
      return padStr.substring(0, pads).concat(str);
    } else {
      char[] padding = new char[pads];
      char[] padChars = padStr.toCharArray();
      for (int i = 0; i < pads; i++) {
        padding[i] = padChars[i % padLen];
      }
      return new String(padding).concat(str);
    }
  }

  /**
   * Gets a String's length or <code>0</code> if the String is <code>null</code>.
   *
   * @param str a String or <code>null</code>
   * @return String length or <code>0</code> if the String is <code>null</code>.
   * @since 2.4
   */
  public static int length(String str) {
    return str == null ? 0 : str.length();
  }


  /**
   * <p>Capitalizes a String changing the first letter to title case as
   * per {@link Character#toTitleCase(char)}. No other letters are changed.</p>
   * <p/>
   * <p>For a word based algorithm, see {@link WordUtils#capitalize(String)}.
   * A <code>null</code> input String returns <code>null</code>.</p>
   * <p/>
   * <pre>
   * StringUtils.capitalize(null)  = null
   * StringUtils.capitalize("")    = ""
   * StringUtils.capitalize("cat") = "Cat"
   * StringUtils.capitalize("cAt") = "CAt"
   * </pre>
   *
   * @param str the String to capitalize, may be null
   * @return the capitalized String, <code>null</code> if null String input
   * @see WordUtils#capitalize(String)
   * @see #uncapitalize(String)
   * @since 2.0
   */
  public static String capitalize(String str) {
    int strLen;
    if (str == null || (strLen = str.length()) == 0) {
      return str;
    }
    return new StringBuffer(strLen)
      .append(Character.toTitleCase(str.charAt(0)))
      .append(str.substring(1))
      .toString();
  }


  /**
   * <p>Uncapitalizes a String changing the first letter to title case as
   * per {@link Character#toLowerCase(char)}. No other letters are changed.</p>
   * <p/>
   * <p>For a word based algorithm, see {@link WordUtils#uncapitalize(String)}.
   * A <code>null</code> input String returns <code>null</code>.</p>
   * <p/>
   * <pre>
   * StringUtils.uncapitalize(null)  = null
   * StringUtils.uncapitalize("")    = ""
   * StringUtils.uncapitalize("Cat") = "cat"
   * StringUtils.uncapitalize("CAT") = "cAT"
   * </pre>
   *
   * @param str the String to uncapitalize, may be null
   * @return the uncapitalized String, <code>null</code> if null String input
   * @see WordUtils#uncapitalize(String)
   * @see #capitalize(String)
   * @since 2.0
   */
  public static String uncapitalize(String str) {
    int strLen;
    if (str == null || (strLen = str.length()) == 0) {
      return str;
    }
    return new StringBuffer(strLen)
      .append(Character.toLowerCase(str.charAt(0)))
      .append(str.substring(1))
      .toString();
  }


  // Defaults
  //-----------------------------------------------------------------------

  /**
   * <p>Returns either the passed in String,
   * or if the String is <code>null</code>, an empty String ("").</p>
   * <p/>
   * <pre>
   * StringUtils.defaultString(null)  = ""
   * StringUtils.defaultString("")    = ""
   * StringUtils.defaultString("bat") = "bat"
   * </pre>
   *
   * @param str the String to check, may be null
   * @return the passed in String, or the empty String if it
   *         was <code>null</code>
   * @see ObjectUtils#toString(Object)
   * @see String#valueOf(Object)
   */
  public static String defaultString(String str) {
    return str == null ? EMPTY : str;
  }

  /**
   * <p>Returns either the passed in String, or if the String is
   * <code>null</code>, the value of <code>defaultStr</code>.</p>
   * <p/>
   * <pre>
   * StringUtils.defaultString(null, "NULL")  = "NULL"
   * StringUtils.defaultString("", "NULL")    = ""
   * StringUtils.defaultString("bat", "NULL") = "bat"
   * </pre>
   *
   * @param str        the String to check, may be null
   * @param defaultStr the default String to return
   *                   if the input is <code>null</code>, may be null
   * @return the passed in String, or the default if it was <code>null</code>
   * @see ObjectUtils#toString(Object, String)
   * @see String#valueOf(Object)
   */
  public static String defaultString(String str, String defaultStr) {
    return str == null ? defaultStr : str;
  }


}
