/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.eventbus;

import com.vaadin.Application;
import com.vaadin.event.EventRouter;
import com.vaadin.service.ApplicationContext;
import ru.codeinside.gses.webui.Flash;

import java.io.NotSerializableException;

final public class SizePublisher implements ApplicationContext.TransactionListener {
  private static final long serialVersionUID = 1L;
  final private EventRouter router;

  public SizePublisher(final EventRouter router) {
    this.router = router;
  }

  @Override
  public void transactionStart(final Application application, final Object transactionData) {
  }

  @Override
  public void transactionEnd(final Application application, final Object transactionData) {
    if (router != null && Flash.app() == application) {
      router.fireEvent(detect(application));
    }
  }

  private SizeEvent detect(final Application application) {
    CountOutputStream counter = new CountOutputStream();
    String error = null;
    DebuggingObjectOutputStream oos = null;
    try {
      oos = new DebuggingObjectOutputStream(counter);
      oos.writeObject(application.getContext());
      oos.close();
    } catch (NotSerializableException e) {
      String message = "";
      for (Object o : oos.getStack()) {
        message += o.getClass() + "\n";
      }
      error = "Проблема с кластером. Ошибка сериализации:\n" + message;
    } catch (Exception e) {
      error = e.getClass() + ": " + e.getMessage();
    } catch (NoClassDefFoundError e) {
      error = "Проблема с кластером. Нет определения " + e.getMessage();
    }
    if (error != null) {
      // не глотать критические ошибки, а лучше ещё включить -Dsun.io.serialization.extendedDebugInfo=true
      System.err.print(error);
    }
    return new SizeEvent(this, error, counter.getByteCount());
  }

}