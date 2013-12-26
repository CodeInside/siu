/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.vaadin.client;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.ButtonElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.IFrameElement;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.dom.client.TextAreaElement;
import com.google.gwt.event.dom.client.DomEvent.Type;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;
import com.vaadin.terminal.gwt.client.Util;
import com.vaadin.terminal.gwt.client.ui.ClickEventHandler;

public class VJsonFormIntegration extends HTML implements Paintable {

  public static final String CLICK_EVENT_IDENTIFIER = "click";
  private static String CLASSNAME = "v-embedded";
  private Element browserElement;
  private Frame frame;
  private String src = "about:blank";
  private boolean fixRequired;
  private boolean validationMode;

  private ApplicationConnection client;

  private final ClickEventHandler clickEventHandler = new ClickEventHandler(this, CLICK_EVENT_IDENTIFIER) {

    @Override
    protected <H extends EventHandler> HandlerRegistration registerHandler(H handler, Type<H> type) {
      return addDomHandler(handler, type);
    }

  };

  public VJsonFormIntegration() {
    setStyleName(CLASSNAME);
    setHTML("Загрузка...");
  }

  public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {

    if (client.updateComponent(this, uidl, true)) {
      return;
    }

    this.client = client;

    clickEventHandler.handleEventHandlerRegistration(client);

    if (uidl.hasAttribute("src")) {
      if (browserElement == null) {
        addStyleName(CLASSNAME + "-browser");
        setHTML("<iframe width=\"100%\" height=\"100%\" frameborder=\"0\""
          + " allowTransparency=\"true\" src=\"\""
          + " name=\"" + uidl.getId() + "\"></iframe>");
        browserElement = DOM.getFirstChild(getElement());
      }
      String newSrc = getSrc(uidl, client);
      if (!newSrc.equals(src)) {
        if (uidl.hasAttribute("fixArchiveSupport")) {
          fixRequired = true;
          frame = createFrameWrap();
        }
        src = newSrc;
        DOM.setElementAttribute(browserElement, "src", src);
      }
    }

    if (uidl.hasAttribute("validationMode")) {
      validationMode = uidl.getBooleanAttribute("validationMode");
    }

    if (uidl.hasAttribute("go")) {
      Scheduler.get().scheduleDeferred(new JsonValueGetter(uidl.getId()));
    }
  }

  /**
   * Helper to return translated src-attribute from embedded's UIDL
   *
   * @param uidl
   * @param client
   * @return
   */
  private String getSrc(UIDL uidl, ApplicationConnection client) {
    String url = client.translateVaadinUri(uidl.getStringAttribute("src"));
    if (url == null) {
      return "";
    }
    return url;
  }

  @Override
  protected void onDetach() {
    if (frame != null) {
      RootPanel.detachNow(frame);
    }
    if (browserElement != null) {
      browserElement.setAttribute("src", "about:blank");
    }
    super.onDetach();
  }

  @Override
  protected void onAttach() {
    super.onAttach();
    if (fixRequired) {
      frame = createFrameWrap();
    }
    if (browserElement != null && src != null && !src.equals(browserElement.getAttribute("src"))) {
      browserElement.setAttribute("src", src);
    }
  }

  @Override
  public void onBrowserEvent(Event event) {
    super.onBrowserEvent(event);
    if (DOM.eventGetType(event) == Event.ONLOAD) {
      Util.notifyParentOfSizeChange(this, true);
    }
    client.handleTooltipEvent(event, this);
  }

  final class JsonValueGetter implements Command {
    private final String id;

    public JsonValueGetter(String id) {
      this.id = id;
    }

    public void execute() {
      try {
        String value = validationMode ? isFormPosted(browserElement) : getJsonValue(browserElement);
        client.updateVariable(id, "jsonValue", value, true);
      } catch (Exception e) {
        client.updateVariable(id, "jsonError", e.getMessage(), true);
      }
    }

    native String getJsonValue(Element element) /*-{
      var json = element.contentWindow.getOepJsonString();
      return json == null ? "" : ("" + json);
    }-*/;

    native String isFormPosted(Element element) /*-{
      var json = element.contentWindow.isFormPosted();
      return json == null ? "" : ("" + json);
    }-*/;

  }


  private Frame createFrameWrap() {
    if (browserElement == null) {
      return null;
    }
    Frame frameWrap = Frame.wrap(browserElement);
    frameWrap.addLoadHandler(new LoadHandler() {
      @Override
      public void onLoad(LoadEvent event) {
        Scheduler.get().scheduleFixedDelay(new Scheduler.RepeatingCommand() {
          @Override
          public boolean execute() {
            IFrameElement formFrame = IFrameElement.as(browserElement);
            Document formDoc = formFrame.getContentDocument();
            if (formDoc == null) {
              return true;
            }
            NodeList<com.google.gwt.dom.client.Element> elements;
            elements = formDoc.getElementsByTagName("input");
            for (int i = 0; i < elements.getLength(); i++) {
              InputElement input = InputElement.as(elements.getItem(i));
              if ("button".equalsIgnoreCase(input.getType())) {
                input.setDisabled(true);
              } else {
                input.setReadOnly(true);
              }
            }
            elements = formDoc.getElementsByTagName("button");
            for (int i = 0; i < elements.getLength(); i++) {
              ButtonElement button = ButtonElement.as(elements.getItem(i));
              button.setDisabled(true);
            }
            elements = formDoc.getElementsByTagName("select");
            for (int i = 0; i < elements.getLength(); i++) {
              SelectElement select = SelectElement.as(elements.getItem(i));
              select.setDisabled(true);
            }
            elements = formDoc.getElementsByTagName("textarea");
            for (int i = 0; i < elements.getLength(); i++) {
              TextAreaElement textArea = TextAreaElement.as(elements.getItem(i));
              textArea.setReadOnly(true);
            }
            return false;
          }
        }, 99);
      }
    });
    return frameWrap;
  }
}