/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm;

import org.apache.commons.lang.StringUtils;
import ru.codeinside.gses.webui.osgi.LogCustomizer;
import ru.codeinside.gws.api.LogService;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.DependsOn;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

@Singleton
@Startup
@DependsOn("BaseBean")
public class AdminServiceProvider {

    @Inject
    AdminService adminService;

    transient static AdminService instance;

    Object ticket;

    @PostConstruct
    void initialize() {
        synchronized (AdminServiceImpl.class) {
            if (instance == null) {
                instance = adminService;
            }
        }
        ticket = adminService.afterCreate();
        adminService.init();

        LogService logService = LogCustomizer.getLogger();
        logService.setShouldWriteClientLog(AdminServiceProvider.getBoolProperty(LogService.httpTransportPipeDump));
        logService.setShouldWriteServerLog(AdminServiceProvider.getBoolProperty(LogService.httpAdapterDump));
    }

    @PreDestroy
    void shutdown() {
        adminService.preDestroy(ticket);
        synchronized (AdminServiceImpl.class) {
            if (instance == adminService) {
                instance = null;
            }
        }
    }

    public static AdminService tryGet() {
        return instance;
    }

    public static AdminService get() {
        if (instance == null) {
            throw new IllegalStateException("Сервис не зарегистрирован!");
        }
        return instance;
    }

    public static boolean getBoolProperty(String key){
        String value = get().getSystemProperty(key);
        if(!StringUtils.isEmpty(value) && Boolean.valueOf(value)){
            return true;
        }
        return false;
    }
}
