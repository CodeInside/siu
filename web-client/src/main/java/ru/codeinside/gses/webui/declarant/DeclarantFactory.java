/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.declarant;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Select;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import org.activiti.engine.task.IdentityLink;
import ru.codeinside.adm.database.ProcedureProcessDefinition;
import ru.codeinside.adm.database.ProcedureType;
import ru.codeinside.gses.activiti.forms.FormID;
import ru.codeinside.gses.lazyquerycontainer.LazyQueryContainer;
import ru.codeinside.gses.service.ExecutorService;
import ru.codeinside.gses.service.Functions;
import ru.codeinside.gses.webui.Flash;
import ru.codeinside.gses.webui.form.DataAccumulator;
import ru.codeinside.gses.webui.form.FormDescription;
import ru.codeinside.gses.webui.form.FormDescriptionBuilder;
import ru.codeinside.gses.webui.form.TaskForm;

import java.io.Serializable;
import java.util.List;

//TODO: перенести в компонент
public class DeclarantFactory {

  public interface ProcedureSelectListener extends Serializable {
    void selectProcedure(long id);
  }

  public static Component create() {

    // TODO: избавится от кучи классов!
    final ServiceQueryDefinition amSQ = new ServiceQueryDefinition(ProcedureType.Administrative);
    final LazyQueryContainer amSC = new LazyQueryContainer(amSQ, new ServiceQueryFactory(false));
    final ProcedureQueryDefinition amPQ = new ProcedureQueryDefinition(ProcedureType.Administrative);
    final LazyQueryContainer amPC = new LazyQueryContainer(amPQ, new ProcedureQueryFactory(Flash.login(), false));

    final ProcedureQueryDefinition mmQ = new ProcedureQueryDefinition(ProcedureType.Interdepartmental);
    final LazyQueryContainer mmC = new LazyQueryContainer(mmQ, new ProcedureQueryFactory(Flash.login(), false));

    final VerticalLayout layout = new VerticalLayout();
    layout.setSizeFull();
    layout.setMargin(true);
    final Label header = new Label("Подача заявки в систему исполнения услуг");
    header.addStyleName("h1");
    layout.addComponent(header);

    final Select amS = new Select("Процедура", amPC);
    String selectWidth = "400px";
    amS.setWidth(selectWidth);
    amS.setItemCaptionMode(AbstractSelect.ITEM_CAPTION_MODE_PROPERTY);
    amS.setItemCaptionPropertyId("name");
    amS.setFilteringMode(AbstractSelect.Filtering.FILTERINGMODE_CONTAINS);
    amS.setNullSelectionAllowed(true);
    amS.setNewItemsAllowed(false);
    amS.setImmediate(true);

    final Select amSS = new Select("Услуга", amSC);
    amSS.setWidth(selectWidth);
    amSS.setItemCaptionMode(AbstractSelect.ITEM_CAPTION_MODE_PROPERTY);
    amSS.setItemCaptionPropertyId("name");
    amSS.setFilteringMode(AbstractSelect.Filtering.FILTERINGMODE_CONTAINS);
    amSS.setNullSelectionAllowed(true);
    amSS.setNewItemsAllowed(false);
    amSS.setImmediate(true);

    final FormLayout amLayout = new FormLayout();

    final Panel amPanel = new Panel();
    amLayout.addComponent(amSS);
    amLayout.addComponent(amS);
    amPanel.addComponent(amLayout);

    final Select mmS = new Select("Процедура", mmC);
    mmS.setFilteringMode(Select.FILTERINGMODE_OFF);
    mmS.setWidth(selectWidth);
    mmS.setItemCaptionPropertyId("name");
    mmS.setFilteringMode(AbstractSelect.Filtering.FILTERINGMODE_CONTAINS);
    mmS.setNullSelectionAllowed(true);
    mmS.setNewItemsAllowed(false);
    mmS.setImmediate(true);

    final FormLayout mmLayout = new FormLayout();
    final Panel mmPanel = new Panel();
//    mmLayout.addComponent(mmSS);
    mmLayout.addComponent(mmS);
    mmPanel.addComponent(mmLayout);

    final VerticalLayout amWrapper = new VerticalLayout();
    amWrapper.setSizeFull();
    amWrapper.addComponent(amPanel);

    final VerticalLayout imWrapper = new VerticalLayout();
    imWrapper.setSizeFull();
    imWrapper.addComponent(mmPanel);

    final TabSheet typeSheet = new TabSheet();
    typeSheet.setSizeFull();
    typeSheet.addTab(amWrapper, "Административные процедуры");
    typeSheet.addTab(imWrapper, "Межведомственные процедуры");
    layout.addComponent(typeSheet);
    layout.setExpandRatio(typeSheet, 1);

    // поведение

    amSS.addListener(new Property.ValueChangeListener() {
      private static final long serialVersionUID = 1L;

      @Override
      public void valueChange(ValueChangeEvent event) {
        final Property prop = event.getProperty();
        if (prop.getValue() == null) {
          amPQ.serviceId = -1;
        } else {
          amPQ.serviceId = (Long) amSC.getItem(prop.getValue()).getItemProperty("id").getValue();
        }
        amS.select(null);
        amPC.refresh();
      }
    });

    final ProcedureSelectListener administrativeProcedureSelectListener = new ProcedureSelectListener() {
      private static final long serialVersionUID = 1L;

      @Override
      public void selectProcedure(long id) {
        if (amWrapper.getComponentCount() > 1) {
          amWrapper.removeComponent(amWrapper.getComponent(1));
        }
        if (id > 0) {
          final Component cmp = createStartEventForm(id, this, layout);
          if (cmp != null) {
            amWrapper.addComponent(cmp);
            amWrapper.setExpandRatio(cmp, 1f);
          } else {
            amS.select(null);
            amPC.refresh();
            amSC.refresh();
          }
        }
      }
    };
    final ProcedureSelectListener interdepartamentalProcedureSelectListener = new ProcedureSelectListener() {
      private static final long serialVersionUID = 1L;

      @Override
      public void selectProcedure(long id) {
        if (imWrapper.getComponentCount() > 1) {
          imWrapper.removeComponent(imWrapper.getComponent(1));
        }
        if (id > 0) {
          final Component cmp = createStartEventForm(id, this, layout);
          if (cmp != null) {
            imWrapper.addComponent(cmp);
            imWrapper.setExpandRatio(cmp, 1f);
          }
        }
      }
    };
    amS.addListener(new Property.ValueChangeListener() {
      private static final long serialVersionUID = 1L;

      @Override
      public void valueChange(ValueChangeEvent event) {
        final Object itemId = event.getProperty().getValue();
        final long procedureId = itemId == null ? -1 : (Long) amPC.getItem(itemId).getItemProperty("id")
            .getValue();
        administrativeProcedureSelectListener.selectProcedure(procedureId);
      }
    });
    mmS.addListener(new Property.ValueChangeListener() {
      private static final long serialVersionUID = 1L;

      @Override
      public void valueChange(ValueChangeEvent event) {
        final Object itemId = event.getProperty().getValue();
        final long procedureId = itemId == null ? -1 : (Long) mmC.getItem(itemId).getItemProperty("id")
            .getValue();
        interdepartamentalProcedureSelectListener.selectProcedure(procedureId);
      }
    });

    return layout;
  }

  // TODO: в одной транзакции!!!
  static Component createStartEventForm(final long procedureId, final ProcedureSelectListener listener, VerticalLayout layout) {
    final ProcedureProcessDefinition def = Flash.flash().getDeclarantService().selectActive(procedureId);
    if (def == null) {
      layout.getWindow().showNotification("Процедура не найдена");
      return null;
    }
    final String processDefinitionId = def.getProcessDefinitionId();
    String login = Flash.flash().getLogin();
    List<IdentityLink> identityLinksForProcessDefinition = Flash.flash().getProcessEngine().getRepositoryService().getIdentityLinksForProcessDefinition(processDefinitionId);
    boolean ok = identityLinksForProcessDefinition.isEmpty();
    for (IdentityLink identityLink : identityLinksForProcessDefinition) {
      if (identityLink.getGroupId() != null) {
        if (Flash.flash().getAdminService().getUserItem(login).getGroups().contains(identityLink.getGroupId())) {
          ok = true;
          break;
        }
      }
      if (identityLink.getUserId() != null && identityLink.getUserId().equals(login)) {
        ok = true;
        break;
      }
    }
    if (!ok) {
      layout.getWindow().showNotification("У вас недостаточно привилегий");
      return null;
    }

    DataAccumulator accumulator = new DataAccumulator();
    ExecutorService executorService = Flash.flash().getExecutorService();
    final FormDescription formDescription = Functions.withEngine(new FormDescriptionBuilder(
            FormID.byProcessDefinitionId(processDefinitionId), executorService, accumulator));
    if (formDescription == null) {
      layout.getWindow().showNotification("Процедура не найдена");
      return null;
    }
    return new TaskForm(formDescription, new TaskForm.CloseListener() {
      @Override
      public void onFormClose(final TaskForm form) {
        final ComponentContainer container = (ComponentContainer) form.getParent();
        container.removeComponent(form);
        final HorizontalLayout layout = new HorizontalLayout();
        layout.setSizeFull();
        final Button next = new Button("Cледующая заявка...", new Button.ClickListener() {
          @Override
          public void buttonClick(Button.ClickEvent event) {
            listener.selectProcedure(procedureId);
          }
        });
        layout.addComponent(next);
        layout.setComponentAlignment(next, Alignment.BOTTOM_RIGHT);
        container.addComponent(layout);
      }
    }, accumulator);
  }

}
