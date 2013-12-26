/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.vaadin.client;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.Command;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.UIDL;
import com.vaadin.terminal.gwt.client.ui.VTextField;

import java.util.ArrayList;
import java.util.List;

import static com.google.gwt.event.dom.client.KeyCodes.KEY_BACKSPACE;
import static com.google.gwt.event.dom.client.KeyCodes.KEY_DELETE;
import static com.google.gwt.event.dom.client.KeyCodes.KEY_DOWN;
import static com.google.gwt.event.dom.client.KeyCodes.KEY_END;
import static com.google.gwt.event.dom.client.KeyCodes.KEY_HOME;
import static com.google.gwt.event.dom.client.KeyCodes.KEY_LEFT;
import static com.google.gwt.event.dom.client.KeyCodes.KEY_RIGHT;
import static com.google.gwt.event.dom.client.KeyCodes.KEY_UP;

public class VMaskedTextField extends VTextField {


  public void onFocus(FocusEvent event) {
    super.onFocus(event);
    // для FF этого достаточно!
    event.preventDefault();
    // немедленное позиционирование не работает в хроме!
    if (getValue().isEmpty()) {
      setValue(blank);
      Scheduler.get().scheduleDeferred(new Command() {
        public void execute() {
          updateCursor(-1);
        }
      });
    } else {
      final int pos = string.indexOf("_");
      if (pos >= 0) {
        Scheduler.get().scheduleDeferred(new Command() {
          public void execute() {
            updateCursor(pos - 1);
          }
        });
      }
    }
  }

  public void onBlur(BlurEvent event) {
    if (getValue().equals(blank)) {
      setText("");
      setValue("");
    }
    super.onBlur(event);
  }

  final static char PLACEHOLDER = '_';

  List<Mask> maskTest = new ArrayList<Mask>();
  StringBuilder string = new StringBuilder();
  String blank = "";

  public VMaskedTextField() {
    addKeyPressHandler(new KeyPressHandler() {
      @Override
      public void onKeyPress(final KeyPressEvent e) {
        final int key = e.getNativeEvent().getKeyCode();
        final int char_ = e.getNativeEvent().getCharCode();
        //VConsole.log("press " + key + ", char " + char_);
        if (key == KEY_DELETE) {
          e.preventDefault();
        }
        if (e.isAltKeyDown() || e.isControlKeyDown() || e.isMetaKeyDown() || char_ == 0) { // клавиши управления без символов!
          return;
        }
        if (getCursorPos() < maskTest.size()) {
          final int cursorPos = getCursorPos();
          final Mask symbol = maskTest.get(cursorPos);
          if (symbol != null) {
            final char charCode = e.getCharCode();
            if (symbol.isValid(charCode)) {
              string.setCharAt(cursorPos, symbol.getChar(charCode));
              setValue(string.toString());
              updateCursor(cursorPos);
            }
          } else {
            updateCursor(cursorPos);
          }
        }
        e.preventDefault();
      }
    });
    addKeyDownHandler(new KeyDownHandler() {
      @Override
      public void onKeyDown(KeyDownEvent event) {
        final int keyCode = event.getNativeKeyCode();
        final int charCode = event.getNativeEvent().getCharCode();
        //VConsole.log("down " + keyCode + ", char " + charCode);
        if (keyCode == KEY_BACKSPACE) {
          int pos = getPreviousPos(getCursorPos());
          Mask m = maskTest.get(pos);
          if (m != null) {
            string.setCharAt(pos, PLACEHOLDER);
            setValue(string.toString());
          }
          setCursorPos(pos);
          event.preventDefault();
        } else if (keyCode == KEY_DELETE || (keyCode == 0 && charCode == 0)) {
          final int pos = getCursorPos();
          if (pos < maskTest.size()) {
            final Mask m = maskTest.get(pos);
            if (m != null) {
              string.setCharAt(pos, PLACEHOLDER);
              setValue(string.toString());
            }
            updateCursor(pos);
          }
          event.preventDefault();
        } else if (keyCode == KEY_RIGHT) {
          setCursorPos(getNextPos(getCursorPos()));
          event.preventDefault();
        } else if (keyCode == KEY_LEFT) {
          setCursorPos(getPreviousPos(getCursorPos()));
          event.preventDefault();
        } else if (keyCode == KEY_HOME || keyCode == KEY_UP) {
          setCursorPos(getPreviousPos(0));
          event.preventDefault();
        } else if (keyCode == KEY_END || keyCode == KEY_DOWN) {
          setCursorPos(getPreviousPos(getValue().length()) + 1);
          event.preventDefault();
        }
      }
    });
  }


  public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
    if (uidl.hasAttribute("mask")) {
      setMask(uidl.getStringAttribute("mask"));
    }
    super.updateFromUIDL(uidl, client);
  }

  //TODO: канонизация значения по маске!
  public void setText(final String value) {
    string = new StringBuilder(value);
    super.setText(value);
  }

  private void setMask(final String mask) {
    final StringBuilder sb = new StringBuilder();
    maskTest = new ArrayList<Mask>();
    if (mask != null) {
      for (int i = 0; i < mask.length(); i++) {
        char c = mask.charAt(i);
        if (c == '\'') {
          maskTest.add(null);
          sb.append(mask.charAt(++i));
        } else if (c == '#') {
          maskTest.add(new NumericMask());
          sb.append(PLACEHOLDER);
        } else if (c == 'U') {
          maskTest.add(new UpperCaseMask());
          sb.append(PLACEHOLDER);
        } else if (c == 'L') {
          maskTest.add(new LowerCaseMask());
          sb.append(PLACEHOLDER);
        } else if (c == '?') {
          maskTest.add(new LetterMask());
          sb.append(PLACEHOLDER);
        } else if (c == 'A') {
          maskTest.add(new AlphanumericMask());
          sb.append(PLACEHOLDER);
        } else if (c == '*') {
          maskTest.add(new WildcardMask());
          sb.append(PLACEHOLDER);
        } else if (c == 'H') {
          maskTest.add(new HexMask());
          sb.append(PLACEHOLDER);
        } else if (c == '~') {
          maskTest.add(new SignMask());
          sb.append(PLACEHOLDER);
        } else {
          maskTest.add(null);
          sb.append(c);
        }
      }
    }
    blank = sb.toString();
  }

  private void updateCursor(int pos) {
    setCursorPos(getNextPos(pos));
  }

  private int getNextPos(int pos) {
    while (++pos < maskTest.size() && maskTest.get(pos) == null) {
      //
    }
    if (pos > maskTest.size()) {
      pos = maskTest.size();
    }
    return pos;
  }

  int getPreviousPos(int pos) {
    while (--pos >= 0 && maskTest.get(pos) == null) {
      //
    }
    if (pos < 0)
      return getNextPos(pos);
    return pos;
  }

  static boolean isCyrillic(final char c) {
    return (0x400 <= c && c <= 0x4ff) ||  //Cyrillic
      (0x500 <= c && c <= 0x52f) ||  //Cyrillic Supplement
      (0x2de0 <= c && c <= 0x2dff) ||  //Cyrillic Extended-A
      (0xa640 <= c && c <= 0xa69f) ||  //Cyrillic Extended-B
      (0x1d2b <= c && c <= 0x1d78);     //Phonetic Extensions
  }

  abstract static class Mask {

    abstract boolean isValid(char c);

    char getChar(char c) {
      return c;
    }

  }

  final static class NumericMask extends Mask {
    @Override
    boolean isValid(char c) {
      return Character.isDigit(c);
    }
  }

  final static class LetterMask extends Mask {
    @Override
    boolean isValid(char c) {
      return Character.isLetter(c) || isCyrillic(c);
    }
  }

  final static class LowerCaseMask extends Mask {
    @Override
    boolean isValid(char c) {
      return Character.isLetter(c) || isCyrillic(c);
    }

    @Override
    char getChar(char c) {
      return Character.toLowerCase(c);
    }
  }

  final static class UpperCaseMask extends Mask {
    @Override
    boolean isValid(char c) {
      return Character.isLetter(c) || isCyrillic(c);
    }

    @Override
    char getChar(char c) {
      return Character.toUpperCase(c);
    }
  }

  final static class AlphanumericMask extends Mask {
    @Override
    boolean isValid(char c) {
      return Character.isLetter(c) || Character.isDigit(c) || isCyrillic(c);
    }
  }

  final static class WildcardMask extends Mask {
    @Override
    boolean isValid(char c) {
      return true;
    }
  }

  final static class SignMask extends Mask {
    @Override
    boolean isValid(char c) {
      return c == '-' || c == '+';
    }
  }

  final static class HexMask extends Mask {
    @Override
    boolean isValid(char c) {
      return ((c == '0' || c == '1' ||
        c == '2' || c == '3' ||
        c == '4' || c == '5' ||
        c == '6' || c == '7' ||
        c == '8' || c == '9' ||
        c == 'a' || c == 'A' ||
        c == 'b' || c == 'B' ||
        c == 'c' || c == 'C' ||
        c == 'd' || c == 'D' ||
        c == 'e' || c == 'E' ||
        c == 'f' || c == 'F'));
    }

    @Override
    char getChar(char c) {
      if (Character.isDigit(c)) {
        return c;
      }
      return Character.toUpperCase(c);
    }
  }
}
