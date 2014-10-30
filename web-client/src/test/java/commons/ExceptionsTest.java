/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package commons;

import org.junit.Test;
import ru.codeinside.EJBMonkey;
import ru.codeinside.gws.api.ServerException;

import javax.ejb.EJBException;
import java.util.concurrent.Callable;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

public class ExceptionsTest {

  @Test
  public void testConvertToApi() {

    ServerException serverException = null;
    try {
      EJBMonkey.npe();
      fail();
    } catch (EJBException e) {
      serverException = Exceptions.convertToApi(e);
    }
    String expected = "ru.codeinside.gws.api.ServerException: java.lang.NullPointerException\n" +
      "\tat ru.codeinside.EJBMonkey.npe(EJBMonkey.java:16)\n";
    assertEquals(expected, Exceptions.toString(serverException));
  }

  @Test
  public void trimToCauseString() {
    Exception exception = null;
    try {
      EJBMonkey.npe();
      fail();
    } catch (Exception e) {
      exception = e;
    }
    assertEquals("java.lang.NullPointerException\n", Exceptions.trimToCauseString(exception));
  }

  @Test
  public void trimToCauseString2() {
    Exception exception = null;
    try {
      EJBMonkey.npe(new Callable<Exception>() {
        @Override
        public Exception call() throws Exception {
          return new NullPointerException();
        }
      });
      fail();
    } catch (Exception e) {
      exception = e;
    }
    String expected = "java.lang.NullPointerException\n" +
      "\tat commons.ExceptionsTest$1.call(ExceptionsTest.java:56)\n" +
      "\tat commons.ExceptionsTest$1.call(ExceptionsTest.java:53)\n";
    assertEquals(expected, Exceptions.trimToCauseString(exception));
  }

}