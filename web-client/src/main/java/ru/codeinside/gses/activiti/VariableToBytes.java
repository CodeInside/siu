/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti;

import ru.codeinside.adm.database.ClientRequestEntity;
import ru.codeinside.gses.webui.Flash;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Конверсия типа в байты (для подписи).
 */
final public class VariableToBytes {

  public static final byte[] EMPTY_BLOCK = new byte[0];

  final private static Charset UTF8 = Charset.forName("UTF8");

  /**
   * TODO: в данных обязательно должен быть снимок времени!
   * <p/>
   * Без снимка времени можно подменить данные и поставить старую подпись!
   */
  static public byte[] toBytes(final Object o) {
    if (o == null) {
      return EMPTY_BLOCK;
    }
    if (o instanceof byte[]) {
      return (byte[]) o;
    }
    if (o instanceof Boolean) {
      return new byte[]{(byte) ((Boolean) o ? 1 : 0)};
    }
    if (o instanceof Short) {
      final ByteBuffer buffer = ByteBuffer.allocate(2);
      buffer.order(ByteOrder.LITTLE_ENDIAN);
      buffer.putShort((Short) o);
      return buffer.array();
    }
    if (o instanceof Integer) {
      final ByteBuffer buffer = ByteBuffer.allocate(4);
      buffer.order(ByteOrder.LITTLE_ENDIAN);
      buffer.putInt((Integer) o);
      return buffer.array();
    }
    if (o instanceof Long) {
      final long value = (Long) o;
      return toBytes(value);
    }
    if (o instanceof String) {
      return ((String) o).getBytes(UTF8);
    }
    if (o instanceof Float) {
      final ByteBuffer buffer = ByteBuffer.allocate(4);
      buffer.order(ByteOrder.LITTLE_ENDIAN);
      buffer.putFloat((Float) o);
      return buffer.array();
    }
    if (o instanceof Double) {
      final ByteBuffer buffer = ByteBuffer.allocate(8);
      buffer.order(ByteOrder.LITTLE_ENDIAN);
      buffer.putDouble((Double) o);
      return buffer.array();
    }
    if (o instanceof Date) {
      return toBytes(((Date) o).getTime());
    }
    if (o instanceof FileValue) {
      return fileToBytes((FileValue) o);
    }
    if (o instanceof SmevRequestValue) {
      // Простейшая реализация - возвратить нормализованный блок AppData.
      final SmevRequestValue value = (SmevRequestValue) o;
      final ClientRequestEntity entity = Flash.flash().getAdminService().getClientRequestEntity(value.id);
      if (entity.appData == null) {
        return new byte[0];
      }
      return entity.appData.getBytes(UTF8);
    }
    if (false) {
      Logger.getLogger(VariableToBytes.class.getName()).log(Level.SEVERE, "Unknown type " + o.getClass() + ", signature not supported!");
      return EMPTY_BLOCK;
    }
    throw new UnsupportedOperationException("type: " + o.getClass());
  }

  private static byte[] fileToBytes(final FileValue o) {
    final FileValue value = (FileValue) o;
    if (value.getFileName() == null) {
      return EMPTY_BLOCK;
    }
    // Подписываем лишь содержимое без названия файла.    
    return value.getContent();
  }

  private static byte[] toBytes(long value) {
    final ByteBuffer buffer = ByteBuffer.allocate(8);
    buffer.order(ByteOrder.LITTLE_ENDIAN);
    buffer.putLong(value);
    return buffer.array();
  }

}
