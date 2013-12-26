/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.components;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import ru.codeinside.adm.database.Procedure;
import ru.codeinside.adm.ui.LazyLoadingContainer2;
import ru.codeinside.gses.manager.processdefeniton.ProcessDefenitionTable;
import ru.codeinside.gses.webui.actions.deployment.DeploymentStartListener;
import ru.codeinside.gses.webui.actions.deployment.DeploymentSucceededListener;
import ru.codeinside.gses.webui.actions.deployment.DeploymentUploadReceiver;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

public class ApInfo extends VerticalLayout {

  private static final long serialVersionUID = 1L;
  private String name;
  private String procDescription;
  private String service;
  private String id;
  private String code;

  private VerticalLayout mainLayout;


  public ApInfo() {
    mainLayout = new VerticalLayout();
  }

  public void render(Procedure procedure, LazyLoadingContainer2 dependentContainer) {
    this.name = procedure.getName();
    this.procDescription = procedure.getDescription();
    this.service = procedure.getService() != null ? procedure.getService().getName() : "";
    this.id = procedure.getId();
    this.code = formatCode(procedure.getRegisterCode());
    buildInfoLayout(dependentContainer);
  }

  private void buildInfoLayout(LazyLoadingContainer2 dependentContainer) {
    if (this.components.contains(mainLayout)) {
      mainLayout.removeAllComponents();
      this.removeComponent(mainLayout);
    }
    mainLayout = new VerticalLayout();
    mainLayout.setWidth("100%");
    HorizontalLayout hl = new HorizontalLayout();
    Label nameTitle = new Label("Процедура: ");
    Label nameValueLabel = new Label(name);
    hl.addComponent(nameTitle);
    hl.addComponent(nameValueLabel);
    hl.setWidth("100%");
    hl.setMargin(false);
    hl.setExpandRatio(nameTitle, 0.1f);
    hl.setExpandRatio(nameValueLabel, 0.9f);

    HorizontalLayout hl1 = new HorizontalLayout();
    Label serviceTitle = new Label("Услуга: ");
    Label serviceValueLabel = new Label(service);
    hl1.addComponent(serviceTitle);
    hl1.addComponent(serviceValueLabel);
    hl1.setWidth("100%");
    hl1.setMargin(false);
    hl1.setExpandRatio(serviceTitle, 0.1f);
    hl1.setExpandRatio(serviceValueLabel, 0.9f);

    HorizontalLayout hl2 = new HorizontalLayout();
    Label codeTitle = new Label("Код: ");
    Label idValueLabel = new Label(id);
    hl2.addComponent(codeTitle);
    hl2.addComponent(idValueLabel);
    hl2.setWidth("100%");
    hl2.setMargin(false);
    hl2.setExpandRatio(codeTitle, 0.1f);
    hl2.setExpandRatio(idValueLabel, 0.9f);

    HorizontalLayout hl3 = new HorizontalLayout();
    Label descriptionTitle = new Label("Описание: ");
    Label descriptionValueLabel = new Label(procDescription);
    hl3.addComponent(descriptionTitle);
    hl3.addComponent(descriptionValueLabel);
    hl3.setWidth("100%");
    hl3.setMargin(false);
    hl3.setExpandRatio(descriptionTitle, 0.1f);
    hl3.setExpandRatio(descriptionValueLabel, 0.9f);

    HorizontalLayout hl4 = new HorizontalLayout();
    Label registerCodeTitle = new Label("Код в реестре: ");
    Label registerCodeValueLabel = new Label(code);
    hl4.addComponent(registerCodeTitle);
    hl4.addComponent(registerCodeValueLabel);
    hl4.setWidth("100%");
    hl4.setMargin(false);
    hl4.setExpandRatio(registerCodeTitle, 0.1f);
    hl4.setExpandRatio(registerCodeValueLabel, 0.9f);

    mainLayout.addComponent(hl2);
    mainLayout.addComponent(hl);
    mainLayout.addComponent(hl1);
    mainLayout.addComponent(hl3);
    mainLayout.addComponent(hl4);
    mainLayout.setWidth("100%");
    hl2.setMargin(true, false, false, false);
    hl3.setMargin(false, false, false, false);
    hl4.setMargin(false, false, true, false);
    addComponent(mainLayout);

    VerticalLayout routesLayout = new VerticalLayout();
    DeploymentUploadReceiver receiver = new DeploymentUploadReceiver();
    DeploymentSucceededListener succeededListener = new DeploymentSucceededListener(receiver, id, null);
    DeploymentAddUi addUi = new DeploymentAddUi(new DeploymentStartListener(), receiver, succeededListener);
    addUi.setMargin(false, false, true, false);
    addUi.setSizeFull();
    routesLayout.addComponent(addUi);
    ProcessDefenitionTable c = new ProcessDefenitionTable(id, dependentContainer);
    routesLayout.addComponent(c);
    succeededListener.addLoadingContainer(c.getTableContainer());
    succeededListener.addLoadingContainer(dependentContainer);
    mainLayout.addComponent(routesLayout);
  }

  public static NumberFormat createCodeFormat() {
    final DecimalFormatSymbols fs = new DecimalFormatSymbols();
    fs.setGroupingSeparator(' ');
    return new DecimalFormat("0,000,000,000,000,000,000", fs);
  }

  public static String formatCode(final Long code) {
    if (code == null) {
      return "";
    }
    return createCodeFormat().format(code.longValue());
  }

}
