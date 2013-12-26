/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.apservice;

import com.jensjansson.pagedtable.PagedTable;
import com.jensjansson.pagedtable.PagedTableContainer;
import com.vaadin.data.Container;
import com.vaadin.data.Container.ItemSetChangeEvent;
import com.vaadin.data.Property;
import com.vaadin.data.validator.IntegerValidator;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Reindeer;
import ru.codeinside.adm.database.Service;
import ru.codeinside.adm.ui.LazyLoadingContainer2;
import ru.codeinside.gses.manager.ManagerService;

public class ApServiceTable extends VerticalLayout {

  private static final long serialVersionUID = -3060552897820352215L;

  private static final String[] NAMES = new String[]{"Код", "Наименование", "Дата"};

  private final ApServiceForm form;
  final PagedTable listAp;

  public ApServiceTable(final ApServiceForm serviceForm) {
    this.form = serviceForm;
    listAp = new PagedTable();
    listAp.setStyleName("clickable-item-table");

    LazyLoadingContainer2 newDataSource = new LazyLoadingContainer2(new ApServiceQuery(serviceForm));
    serviceForm.addDependentContainer(newDataSource);
    listAp.setContainerDataSource(newDataSource);
    listAp.addContainerProperty("id", Component.class, "");
    listAp.addContainerProperty("name", String.class, "");
    listAp.addContainerProperty("dateCreated", String.class, "");
    listAp.setColumnHeaders(NAMES);

    addComponent(listAp);
    addComponent(createMyControls(listAp));
    listAp.setPageLength(10);
    listAp.setColumnExpandRatio("id", 0.3f);
    listAp.setColumnExpandRatio("name", 3);
    listAp.setColumnExpandRatio("dateCreated", 0.3f);

    listAp.addListener(new ItemClickEvent.ItemClickListener() {
      @Override
      public void itemClick(ItemClickEvent event) {
        if (event.getItem().getItemProperty("id") != null && event.getItem().getItemProperty("id").getType() == Button.class) {
          String id = ((Button) event.getItem().getItemProperty("id").getValue()).getCaption();
          Service s = ManagerService.get().getApService(id);
          if (s != null) {
            form.showForm(s);
          }
        }
      }
    });

    Container.ItemSetChangeListener listener = new Container.ItemSetChangeListener() {

      private static final long serialVersionUID = 4042381260704014883L;

      @Override
      public void containerItemSetChange(ItemSetChangeEvent event) {
        refreshTable();
      }
    };

    newDataSource.addListener(listener);
    listAp.setSizeFull();
    setSizeFull();

  }

  public void refreshTable() {
    listAp.getContainerDataSource().removeAllItems();
    listAp.refreshRowCache();
    int pageLength = listAp.getPageLength();
    listAp.setPageLength(pageLength - 1);
    listAp.setPageLength(pageLength);
  }


  public HorizontalLayout createMyControls(final PagedTable listAp) {
    final PagedTableContainer container = (PagedTableContainer) listAp.getContainerDataSource();

    Label itemsPerPageLabel = new Label("Строк на странице:");
    final ComboBox itemsPerPageSelect = new ComboBox();
    itemsPerPageSelect.addItem("5");
    itemsPerPageSelect.addItem("10");
    itemsPerPageSelect.addItem("25");
    itemsPerPageSelect.addItem("50");
    itemsPerPageSelect.addItem("100");
    itemsPerPageSelect.addItem("600");
    itemsPerPageSelect.setImmediate(true);
    itemsPerPageSelect.setNullSelectionAllowed(false);
    itemsPerPageSelect.setWidth("50px");
    itemsPerPageSelect.addListener(new Property.ValueChangeListener() {
      private static final long serialVersionUID = -2255853716069800092L;

      public void valueChange(
          com.vaadin.data.Property.ValueChangeEvent event) {
        Integer newValue = Integer.valueOf(String.valueOf(event.getProperty().getValue()));
        if (container.getRealSize() < newValue) {
          listAp.setPageLength(container.getRealSize());
        } else {
          listAp.setPageLength(newValue);
        }
      }
    });
    itemsPerPageSelect.select("25");
    Label pageLabel = new Label("Страница:&nbsp;", Label.CONTENT_XHTML);
    final TextField currentPageTextField = new TextField();
    currentPageTextField.setValue(String.valueOf(listAp.getCurrentPage()));
    currentPageTextField.addValidator(new IntegerValidator(null));
    Label separatorLabel = new Label("&nbsp;/&nbsp;", Label.CONTENT_XHTML);
    final Label totalPagesLabel = new Label(
        String.valueOf(listAp.getTotalAmountOfPages()), Label.CONTENT_XHTML);
    currentPageTextField.setStyleName(Reindeer.TEXTFIELD_SMALL);
    currentPageTextField.setImmediate(true);
    currentPageTextField.addListener(new Property.ValueChangeListener() {
      private static final long serialVersionUID = -2255853716069800092L;

      public void valueChange(
          com.vaadin.data.Property.ValueChangeEvent event) {
        if (currentPageTextField.isValid()
            && currentPageTextField.getValue() != null) {
          int page = Integer.valueOf(String
              .valueOf(currentPageTextField.getValue()));
          listAp.setCurrentPage(page);
        }
      }
    });
    pageLabel.setWidth(null);
    currentPageTextField.setWidth("20px");
    separatorLabel.setWidth(null);
    totalPagesLabel.setWidth(null);

    HorizontalLayout controlBar = new HorizontalLayout();
    HorizontalLayout pageSize = new HorizontalLayout();
    HorizontalLayout pageManagement = new HorizontalLayout();
    final Button first = new Button("<<", new Button.ClickListener() {
      private static final long serialVersionUID = -355520120491283992L;

      public void buttonClick(Button.ClickEvent event) {
        listAp.setCurrentPage(0);
      }
    });
    final Button previous = new Button("<", new Button.ClickListener() {
      private static final long serialVersionUID = -355520120491283992L;

      public void buttonClick(Button.ClickEvent event) {
        listAp.previousPage();
      }
    });
    final Button next = new Button(">", new Button.ClickListener() {
      private static final long serialVersionUID = -1927138212640638452L;

      public void buttonClick(Button.ClickEvent event) {
        listAp.nextPage();
      }
    });
    final Button last = new Button(">>", new Button.ClickListener() {
      private static final long serialVersionUID = -355520120491283992L;

      public void buttonClick(Button.ClickEvent event) {
        listAp.setCurrentPage(listAp.getTotalAmountOfPages());
      }
    });
    first.setStyleName(Reindeer.BUTTON_LINK);
    previous.setStyleName(Reindeer.BUTTON_LINK);
    next.setStyleName(Reindeer.BUTTON_LINK);
    last.setStyleName(Reindeer.BUTTON_LINK);

    itemsPerPageLabel.addStyleName("pagedtable-itemsperpagecaption");
    itemsPerPageSelect.addStyleName("pagedtable-itemsperpagecombobox");
    pageLabel.addStyleName("pagedtable-pagecaption");
    currentPageTextField.addStyleName("pagedtable-pagefield");
    separatorLabel.addStyleName("pagedtable-separator");
    totalPagesLabel.addStyleName("pagedtable-total");
    first.addStyleName("pagedtable-first");
    previous.addStyleName("pagedtable-previous");
    next.addStyleName("pagedtable-next");
    last.addStyleName("pagedtable-last");

    itemsPerPageLabel.addStyleName("pagedtable-label");
    itemsPerPageSelect.addStyleName("pagedtable-combobox");
    pageLabel.addStyleName("pagedtable-label");
    currentPageTextField.addStyleName("pagedtable-label");
    separatorLabel.addStyleName("pagedtable-label");
    totalPagesLabel.addStyleName("pagedtable-label");
    first.addStyleName("pagedtable-button");
    previous.addStyleName("pagedtable-button");
    next.addStyleName("pagedtable-button");
    last.addStyleName("pagedtable-button");

    pageSize.addComponent(itemsPerPageLabel);
    pageSize.addComponent(itemsPerPageSelect);
    pageSize.setComponentAlignment(itemsPerPageLabel, Alignment.MIDDLE_LEFT);
    pageSize.setComponentAlignment(itemsPerPageSelect,
        Alignment.MIDDLE_LEFT);
    pageSize.setSpacing(true);
    pageManagement.addComponent(first);
    pageManagement.addComponent(previous);
    pageManagement.addComponent(pageLabel);
    pageManagement.addComponent(currentPageTextField);
    pageManagement.addComponent(separatorLabel);
    pageManagement.addComponent(totalPagesLabel);
    pageManagement.addComponent(next);
    pageManagement.addComponent(last);
    pageManagement.setComponentAlignment(first, Alignment.MIDDLE_LEFT);
    pageManagement.setComponentAlignment(previous, Alignment.MIDDLE_LEFT);
    pageManagement.setComponentAlignment(pageLabel, Alignment.MIDDLE_LEFT);
    pageManagement.setComponentAlignment(currentPageTextField,
        Alignment.MIDDLE_LEFT);
    pageManagement.setComponentAlignment(separatorLabel,
        Alignment.MIDDLE_LEFT);
    pageManagement.setComponentAlignment(totalPagesLabel,
        Alignment.MIDDLE_LEFT);
    pageManagement.setComponentAlignment(next, Alignment.MIDDLE_LEFT);
    pageManagement.setComponentAlignment(last, Alignment.MIDDLE_LEFT);
    pageManagement.setWidth(null);
    pageManagement.setSpacing(true);

    controlBar.addComponent(pageSize);
    controlBar.addComponent(pageManagement);
    controlBar.setComponentAlignment(pageManagement,
        Alignment.MIDDLE_CENTER);
    controlBar.setWidth("100%");
    controlBar.setExpandRatio(pageSize, 1);

    listAp.addListener(new PagedTable.PageChangeListener() {
      private static final long serialVersionUID = 5174955717170669637L;

      public void pageChanged(PagedTable.PagedTableChangeEvent event) {
        first.setEnabled(container.getStartIndex() > 0);
        previous.setEnabled(container.getStartIndex() > 0);
        next.setEnabled(container.getStartIndex() < container
            .getRealSize() - listAp.getPageLength());
        last.setEnabled(container.getStartIndex() < container
            .getRealSize() - listAp.getPageLength());
        currentPageTextField.setValue(String.valueOf(listAp.getCurrentPage()));
        totalPagesLabel.setValue(listAp.getTotalAmountOfPages());
        itemsPerPageSelect.setValue(String.valueOf(listAp.getPageLength()));
      }
    });
    return controlBar;
  }

}
