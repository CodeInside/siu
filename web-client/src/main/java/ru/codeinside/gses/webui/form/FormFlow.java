/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.form;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.vaadin.data.Validator;
import com.vaadin.ui.Form;
import com.vaadin.ui.Window;
import org.apache.commons.lang.StringUtils;
import ru.codeinside.gses.activiti.forms.FormID;
import ru.codeinside.gses.webui.wizard.Wizard;

import java.io.Serializable;
import java.util.LinkedList;

final public class FormFlow implements Serializable {

  final private LinkedList<Form> forms = Lists.newLinkedList();
  final private LinkedList<FormSeq> pages = Lists.newLinkedList();
  final private FormID id;

  public FormFlow(final FormID id) {
    this.id = id;
  }

  public ImmutableList<Form> getForms() {
    return ImmutableList.copyOf(forms);
  }

  public Form getFrom(final FormSeq seq) {
    assert seq != null;
    final FormSeq previous = pages.peekLast();
    final Form form = seq.getForm(id, previous);
    assert form != null;
    forms.addLast(form);
    pages.addLast(seq);
    return form;
  }

  public FormSeq getPrevious() {
    return pages.peekLast();
  }

  public boolean forward(final FormSeq seq, final Wizard wizard) {
    assert seq != null;
    final FormSeq previous = pages.peekLast();
    assert previous == seq;
    final Form form = forms.getLast();
    if (form instanceof AsyncCompletable) {
      boolean asyncRequired = ((AsyncCompletable) form).isAsyncRequiredForComplete(new FlowAsyncCompleter(form, wizard));
      if (asyncRequired) {
        return false;
      }
    }
    try {
      form.commit();
      return true;
    } catch (Validator.InvalidValueException e) {
      String msg = e.getMessage();
      if (StringUtils.isBlank(msg)) {
        if (e instanceof Validator.EmptyValueException) {
          msg = "Заполните все поля, отмеченные красной звёздочкой!";
        } else {
          msg = "Необходимо исправить ошибки!";
        }
      }
      boolean html = msg.contains("<br/>");
      form.getWindow().showNotification(null, msg, Window.Notification.TYPE_HUMANIZED_MESSAGE, html);
    }
    return false;
  }

  public void backward(final FormSeq seq) {
    assert seq != null;
    final FormSeq previous = pages.removeLast();
    assert previous == seq;
    Form form = forms.removeLast();
    assert form != null;
    form = forms.removeLast();
    assert form != null;
  }

  final class FlowAsyncCompleter implements AsyncCompleter {

    final Form form;
    final Wizard wizard;

    public FlowAsyncCompleter(Form form, Wizard wizard) {
      this.form = form;
      this.wizard = wizard;
    }

    @Override
    public void onComplete(boolean success) {
      if (success) {
        if (forms.getLast() != form) {
          form.getWindow().showNotification("разные формы?", forms.getLast() + " vs " + form);
        }
        wizard.next();
      }
    }
  }
}

