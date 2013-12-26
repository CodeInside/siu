/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.vaadin;

import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.ClientWidget;
import com.vaadin.ui.TextField;
import ru.codeinside.gses.vaadin.client.VMaskedTextField;

/**
 * Значения null разрешены, на клиентской стороне представлены как "".
 * В этом случае предполагается, что данные по маске не заполнены.
 */
@ClientWidget(VMaskedTextField.class)
public class MaskedTextField extends TextField {
  private static final long serialVersionUID = -5168618178262041249L;
  private String mask;
  private MaskValidator maskValidator;

  private void init() {
    setNullSettingAllowed(true);
    setNullRepresentation("");
  }

  public MaskedTextField() {
    init();
  }

  public MaskedTextField(String caption) {
    setCaption(caption);
    init();
  }

  public MaskedTextField(String caption, String mask) {
    setCaption(caption);
    setMask(mask);
    init();
  }

  public MaskedTextField(Property dataSource) {
    super(dataSource);
    init();
  }

  public MaskedTextField(String caption, Property dataSource) {
    super(caption, dataSource);
    init();
  }

  public String getMask() {
    return mask;
  }

  /**
   * Пустая строка считается не заполненным значением.
   */
  @Override
  public void setValue(Object newValue) throws ReadOnlyException, ConversionException {
    //Logger.getAnonymousLogger().info("update: " + (newValue == null ? "NULL" : newValue.toString()));
    String str = newValue == null ? null : newValue.toString();
    if (str != null && str.isEmpty()) {
      str = null;
    }
    super.setValue(str);
  }

  public void setMask(String mask) {
    this.mask = mask;
    if (maskValidator != null) {
      removeValidator(maskValidator);
      maskValidator = null;
    }
    if (mask != null) {
      maskValidator = new MaskValidator(mask);
      addValidator(maskValidator);
      setMaxLength(maskValidator.getLength());
    }
    requestRepaint();
  }

  @Override
  public void paintContent(PaintTarget target) throws PaintException {
    target.addAttribute("mask", mask);
    super.paintContent(target);
  }


  final static class MaskValidator implements Validator {

    final static String CFG = "#UL?A*~H";

    final String maskCfg;
    final String maskSym;

    MaskValidator(final String mask) {
      final StringBuilder cfgBuilder = new StringBuilder();
      final StringBuilder symBuilder = new StringBuilder();
      if (mask != null) {
        final int length = mask.length();
        final int last = length - 1;
        for (int i = 0; i < length; i++) {
          final char c = mask.charAt(i);
          if (CFG.indexOf(c) >= 0) {
            cfgBuilder.append(c);
            symBuilder.append('_');
          } else {
            cfgBuilder.append(' ');
            if (c != '\'' || i == last) {
              symBuilder.append(c);
            } else {
              i++;
              symBuilder.append(mask.charAt(i));
            }
          }
        }
      }
      this.maskCfg = cfgBuilder.toString();
      this.maskSym = symBuilder.toString();
    }

    public int getLength() {
      return maskCfg.length();
    }

    @Override
    public void validate(Object value) throws InvalidValueException {
      if (!isValid(value)) {
        throw new InvalidValueException("Заполните данные по маске!");
      }
    }

    @Override
    public boolean isValid(final Object value) {
      return value instanceof String && maskIsValid((String) value);
    }

    private boolean maskIsValid(final String value) {
      if (maskCfg.length() != value.length()) {
        return false;
      }

      final int length = value.length();
      for (int i = 0; i < length; i++) {
        final char c = value.charAt(i);
        switch (maskCfg.charAt(i)) {
          case ' ':
            if (c != maskSym.charAt(i)) {
              return false;
            }
            break;

          case '#':
            if (!Character.isDigit(c)) {
              return false;
            }
            break;

          case 'U':
            if (!Character.isUpperCase(c)) {
              return false;
            }
            break;

          case 'L':
            if (!Character.isLowerCase(c)) {
              return false;
            }
            break;

          case '?':
            if (!Character.isLetter(c)) {
              return false;
            }
            break;

          case 'A':
            if (!Character.isLetterOrDigit(c)) {
              return false;
            }
            break;

          case '*':
            break;

          case '~':
            if (c != '-' && c != '+') {
              return false;
            }
            break;

          case 'H':
            if (!Character.isDigit(c) && !('A' <= c && c <= 'F')) {
              return false;
            }
            break;

          default:
            return false;
        }
      }
      return true;
    }
  }
}

