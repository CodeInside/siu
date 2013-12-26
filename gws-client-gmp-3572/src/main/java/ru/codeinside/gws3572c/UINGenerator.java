/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws3572c;


import java.util.HashMap;
import java.util.Map;

public class UINGenerator {
  public static String generateChargeUIN(String registerCode, String ordinalNumber) {
    if (registerCode == null) throw new IllegalArgumentException("Register code is null");
    if (ordinalNumber == null) throw new IllegalArgumentException("Ordinal is null");
    if (registerCode.length() != 6)
      throw new IllegalArgumentException("Registe code length must be equals 6 character");
    if (ordinalNumber.length() != 12)
      throw new IllegalArgumentException("Ordinal number code length must be equals 12 character real " + ordinalNumber.length() );
    String result = "Ъ".concat(registerCode.toUpperCase()).concat(ordinalNumber.toUpperCase());
    return result.concat(calcKey(result));
  }

  private static String calcKey(String stringToCalcKey) {
    String result = getKey(stringToCalcKey, 1);
    if ("10".equals(result)) {
      result = getKey(stringToCalcKey, 3);
      if ("10".equals(result)) {
        result = "0";
      }
    }
    return result;
  }

  private static String getKey(String stringToCalcKey, int startIndex) {
    int digitWeight = startIndex;
    int sumDigits = 0;
    Map<Character, Integer> characterCodeMap = getCharacterCodeMap();
    for (int idx = 0; idx < stringToCalcKey.length(); idx++) {
      if (digitWeight > 10) digitWeight = 1;
      int digitValue = characterCodeMap.get(stringToCalcKey.charAt(idx)) % 10;
      sumDigits += digitWeight * digitValue;
      digitWeight++;
    }
    return Integer.toString(sumDigits % 11);
  }

  private static Map<Character, Integer> getCharacterCodeMap() {
    Map<Character, Integer> result = new HashMap<Character, Integer>();
    result.put('0', 0);
    result.put('1', 1);
    result.put('2', 2);
    result.put('3', 3);
    result.put('4', 4);
    result.put('5', 5);
    result.put('6', 6);
    result.put('7', 7);
    result.put('8', 8);
    result.put('9', 9);
    result.put('А', 1);
    result.put('A', 1);
    result.put('Ц', 22);
    result.put('Б', 2);
    result.put('Ч', 23);
    result.put('В', 3);
    result.put('B', 3);
    result.put('Ш', 24);
    result.put('Г', 4);
    result.put('Щ', 25);
    result.put('Д', 5);
    result.put('Э', 26);
    result.put('Е', 6);
    result.put('E', 6);
    result.put('Ю', 27);
    result.put('Ж', 7);
    result.put('Я', 28);
    result.put('З', 8);
    result.put('D', 29);
    result.put('И', 9);
    result.put('F', 30);
    result.put('К', 10);
    result.put('K', 10);
    result.put('G', 31);
    result.put('Л', 11);
    result.put('I', 32);
    result.put('М', 12);
    result.put('M', 12);
    result.put('Ъ', 33);
    result.put('J', 33);
    result.put('Н', 13);
    result.put('H', 13);
    result.put('L', 34);
    result.put('О', 14);
    result.put('O', 14);
    result.put('N', 35);
    result.put('П', 15);
    result.put('Ы', 36);
    result.put('Q', 36);
    result.put('Р', 16);
    result.put('P', 16);
    result.put('R', 37);
    result.put('С', 17);
    result.put('C', 17);
    result.put('S', 38);
    result.put('Т', 18);
    result.put('T', 18);
    result.put('U', 39);
    result.put('У', 19);
    result.put('Y', 19);
    result.put('V', 40);
    result.put('Ф', 20);
    result.put('W', 41);
    result.put('Х', 21);
    result.put('X', 21);
    result.put('Ь', 42);
    result.put('Z', 42);
    return result;
  }
}
