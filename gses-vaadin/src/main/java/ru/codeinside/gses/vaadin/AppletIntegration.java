/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.vaadin;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.codeinside.gses.vaadin.client.VAppletIntegration;

import com.vaadin.Application;
import com.vaadin.service.ApplicationContext;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.terminal.gwt.server.PortletApplicationContext;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.ui.AbstractComponent;

/**
 * Server side component for the VAppletIntegration widget.
 */
@com.vaadin.ui.ClientWidget(ru.codeinside.gses.vaadin.client.VAppletIntegration.class)
public class AppletIntegration extends AbstractComponent {

    private static final long serialVersionUID = 6061722679712017720L;

    private String appletClass = null;
    private String codebase;
    private String name;
    private List<String> appletArchives = null;
    private Map<String, String> appletParams = null;

    private String command = null;
    private String[] commandParams = null;

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        super.paintContent(target);

        // Applet class
        if (appletClass == null) {
            // Do not paint anything of class is missing
            return;
        }
        target.addAttribute(VAppletIntegration.ATTR_APPLET_CLASS, appletClass);

        // Applet HTTP Session id
        String sid = getHttpSessionId();
        if (sid != null) {
            target.addAttribute(VAppletIntegration.ATTR_APPLET_SESSION, sid);
        }

        // Applet archives
        if (appletArchives != null) {
            target.addAttribute(VAppletIntegration.ATTR_APPLET_ARCHIVES,
                    appletArchives.toArray(new String[appletArchives.size()]));
        }

        // Applet codebase
        if (codebase != null) {
            target.addAttribute(VAppletIntegration.ATTR_APPLET_CODEBASE,
                    codebase);
        }

        // Applet name
        if (name != null) {
            target.addAttribute(VAppletIntegration.ATTR_APPLET_NAME, name);
        }

        // Applet parameters
        if (appletParams != null) {
            target.addAttribute(VAppletIntegration.ATTR_APPLET_PARAM_NAMES,
                    appletParams);
        }

        // Commands
        if (command != null) {
            target.addAttribute(VAppletIntegration.ATTR_CMD, command);
            command = null;
        }

        if (commandParams != null) {
            target.addAttribute(VAppletIntegration.ATTR_CMD_PARAMS,
                    commandParams);
            commandParams = null;
        }
    }

    /**
     * Read the HTTP session id.
     *
     * This method cannot be called if this component has not been attached to
     * the application.
     *
     * @return
     */
    protected String getHttpSessionId() {
        Application app = getApplication();
        if (app != null) {
            ApplicationContext ctx = app.getContext();
            if (ctx instanceof WebApplicationContext) {
                return ((WebApplicationContext)ctx).getHttpSession().getId();
            } else if (ctx instanceof PortletApplicationContext) {
                return ((PortletApplicationContext)ctx).getHttpSession().getId();
            }
        }
        return "";
    }

    /**
     * Execute command in applet.
     *
     * @param command
     */
    public void executeCommand(String command) {
        this.command = command;
        commandParams = null;
        requestRepaint();
    }

    /**
     * Execute command with parameter in applet.
     *
     * @param command
     * @param params
     */
    public void executeCommand(String command, String[] params) {
        this.command = command;
        commandParams = params;
        requestRepaint();
    }

    /**
     * Set the fully qualified class name of the applet.
     *
     * This method is protected so that overriding classes can publish it if
     * needed.
     *
     * @param appletClass
     */
    protected void setAppletClass(String appletClass) {
        this.appletClass = appletClass;
    }

    /**
     * Get the fully qualified class name of the applet.
     *
     * This method is protected so that overriding classes can publish it if
     * needed.
     *
     * @param appletClass
     */
    protected String getAppletClass() {
        return appletClass;
    }

    /**
     * Set list of archives needed to run the applet.
     *
     * This method is protected so that overriding classes can publish it if
     * needed.
     *
     * @param appletClass
     */
    protected void setAppletArchives(List<String> appletArchives) {
        this.appletArchives = appletArchives;
    }

    /**
     * Get list of archives needed to run the applet.
     *
     * This method is protected so that overriding classes can publish it if
     * needed.
     *
     * @param appletClass
     */
    protected List<String> getAppletArchives() {
        return appletArchives;
    }

    /**
     * Get an applet paramter. These are name value pairs passed to the applet
     * element as PARAM& elements.
     *
     * This method is protected so that overriding classes can publish it if
     * needed.
     *
     */
    protected String getAppletParams(String paramName) {
        if (appletParams == null) {
            return null;
        }
        return appletParams.get(paramName);
    }

    /**
     * Set an applet paramter. These are name value pairs passed to the applet
     * element as PARAM elements and should therefore be applied before first
     * the applet integration.
     *
     * This method is protected so that overriding classes can publish it if
     * needed.
     *
     */
    protected void setAppletParams(String paramName, String paramValue) {
        if (appletParams == null) {
            appletParams = new HashMap<String, String>();
        }
        appletParams.put(paramName, paramValue);
    }

    /**
     * Get map (name-value pairs) of parameter passed to the applet.
     *
     * This method is protected so that overriding classes can publish it if
     * needed.
     *
     * @param appletClass
     */
    protected Map<String, String> getAppletParams() {
        return Collections.unmodifiableMap(appletParams);
    }

    /**
     * Set the codebase attribute for the applet.
     *
     * By default the codebase points to GWT modulepath, but this can be
     * overrided by setting it explicitly.
     *
     * @param codebase
     */
    public void setCodebase(String codebase) {
        this.codebase = codebase;
    }

    /**
     * Set the codebase attribute for the applet.
     *
     * By default the codebase points to GWT modulepath, but this can be
     * overrided by setting it explicitly.
     *
     * @see #setCodebase(String)
     * @return codebase
     */
    public String getCodebase() {
        return codebase;
    }

    /**
     * Set the name attribute for the applet.
     *
     * By default the is the same as the autogenerated id, but this can be
     * overridden by setting this explicitly.
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the name attribute for the applet.
     *
     * By default the is the same as the autogenerated id, but this can be
     * overridden by setting this explicitly.
     *
     * @see #setName(String)
     * @return name
     */
    public String getName() {
        return name;
    }

}
