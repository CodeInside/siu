/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.manager;

import com.vaadin.ui.Button;
import com.vaadin.ui.Table;
import org.glassfish.embeddable.GlassFishException;
import ru.codeinside.gses.webui.Configurator;
import ru.codeinside.gses.webui.gws.TRef;
import ru.codeinside.gws.api.Server;

public class UndeployButtonListener implements Button.ClickListener{

    private static final long serialVersionUID = 5843493321L;

    final String name;
    final Table table;

    public UndeployButtonListener(TRef<Server> ref, Table table) {
        String[] split = ref.getLocation().split("/");
        this.name = split[split.length - 1];
        this.table = table;
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        try {
            Configurator.getDeployer().undeploy(name);
            ManagerWorkplace.fillServerTable(table);
        } catch (GlassFishException e) {
            e.printStackTrace();
        }
    }
}