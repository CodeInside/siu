/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.manager.processdefeniton;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;

import ru.codeinside.adm.database.DefinitionStatus;
import ru.codeinside.adm.database.Employee;
import ru.codeinside.adm.database.ProcedureProcessDefinition;
import ru.codeinside.adm.ui.LazyLoadingContainer2;
import ru.codeinside.gses.manager.ManagerService;
import ru.codeinside.gses.service.Functions;
import ru.codeinside.gses.service.PF;
import ru.codeinside.gses.webui.Flash;
import ru.codeinside.gses.webui.actions.deployment.DeploymentStartListener;
import ru.codeinside.gses.webui.actions.deployment.DeploymentSucceededListener;
import ru.codeinside.gses.webui.actions.deployment.DeploymentUploadReceiver;
import ru.codeinside.gses.webui.components.ContentWindowChanger;
import ru.codeinside.gses.webui.components.DeploymentAddUi;
import ru.codeinside.gses.webui.components.ProcessDefinitionShowUi;
import ru.codeinside.gses.webui.containers.LazyLoadingContainer;
import ru.codeinside.gses.webui.containers.LazyLoadingQuery;
import ru.codeinside.gses.webui.utils.Components;

import com.google.common.base.Function;
import com.vaadin.Application;
import com.vaadin.data.Item;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.terminal.DownloadStream;
import com.vaadin.terminal.StreamResource;
import com.vaadin.terminal.StreamResource.StreamSource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Window;

public class ProcessDefenitionQuery implements LazyLoadingQuery {

	private static final long serialVersionUID = 1L;

	final SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy hh:mm");
	DecimalFormat df = new DecimalFormat("##.00");

	String[] sortProps = {};
	boolean[] sortAsc = {};

	private final String procedureId;
	private final LazyLoadingContainer2 proceduresContainer;

	public ProcessDefenitionQuery(String procedureId, LazyLoadingContainer2 proceduresContainer) {
		this.procedureId = procedureId;
		this.proceduresContainer = proceduresContainer;
	}

	@Override
	public int size() {
		return ManagerService.get().getProcessDefenitionCountByProcedureId(procedureId);
	}

	@Override
	public List<Item> loadItems(int start, int count) {
		ArrayList<Item> items = new ArrayList<Item>();
		for (ProcedureProcessDefinition procedure : ManagerService.get().getProcessDefenitionsByProcedureId(
				procedureId, start, count, sortProps, sortAsc)) {
			items.add(createItem(procedure));
		}
		return items;
	}

	@Override
	public Item loadSingleResult(String id) {
		final ProcedureProcessDefinition procedure = ManagerService.get().getProcessDefenition(id);
		return createItem(procedure);
	}

	PropertysetItem createItem(final ProcedureProcessDefinition p) {
		PropertysetItem item = new PropertysetItem();

		ClickListener listener = new ClickListener() {

			private static final long serialVersionUID = -8900212370037948964L;

			@Override
			public void buttonClick(ClickEvent event) {
				Window mainWin = event.getButton().getApplication().getMainWindow();

				ProcessDefinition processDefinition = Functions.withRepository(Flash.login(),
						new Function<RepositoryService, ProcessDefinition>() {
							public ProcessDefinition apply(RepositoryService srv) {
								return srv.createProcessDefinitionQuery()
										.processDefinitionId(p.getProcessDefinitionId()).singleResult();
							}
						});

				String caption = "Версия " + df.format(p.getVersion());
				Window win = Components.createWindow(mainWin, caption);
				win.center();
				ContentWindowChanger changer = new ContentWindowChanger(win);
				ProcessDefinitionShowUi putComponent = new ProcessDefinitionShowUi(mainWin.getApplication(), processDefinition, changer);
				changer.set(putComponent, caption);
			}

		};
		ObjectProperty<Component> versionProperty = Components.buttonProperty(df.format(p.getVersion()), listener);
		item.addItemProperty("version", versionProperty);

		HorizontalLayout ll = new HorizontalLayout();
		ll.setSpacing(true);
		DefinitionStatus status = p.getStatus();
		final Label label = new Label(status.getLabelName());
		label.setWidth("100px");
		ll.addComponent(label);

		final ComboBox comboBox = new ComboBox();
		comboBox.setWidth("100px");
		comboBox.setNullSelectionAllowed(false);
		for (DefinitionStatus s : status.getAvailableStatus()) {
			comboBox.addItem(s.getLabelName());
			comboBox.setValue(s.getLabelName());
		}
        if(!status.equals(DefinitionStatus.PathToArchive)&& !status.getAvailableStatus().isEmpty()) {
			ll.addComponent(comboBox);
			Button c = new Button("ok");
			c.addListener(new ClickListener() {

				private static final long serialVersionUID = 2966059295049064338L;

				@Override
				public void buttonClick(ClickEvent event) {
					Object value = comboBox.getValue();
					final String newValue = value.toString();
					final DefinitionStatus newStatus = DefinitionStatus.getStatusByLabelName(newValue);

					if (DefinitionStatus.Work.equals(newStatus)) {
						final List<ProcedureProcessDefinition> works = ManagerService.get()
								.getProcessDefenitionWithStatus(p, DefinitionStatus.Work);
						if (!works.isEmpty()) {
							final Window thisWindow = event.getButton().getWindow();
							comfirmAction(thisWindow, p, label, newValue, newStatus, works);
							return;

						}
					}

					ManagerService.get().updateProcessDefinationStatus(p.getProcessDefinitionId(), newStatus);

					label.setValue(null);
					label.setCaption(newValue);
					paramLazyLoadingContainer.fireItemSetChange();
					proceduresContainer.fireItemSetChange();
				}

				private void comfirmAction(final Window thisWindow, final ProcedureProcessDefinition p,
						final Label label, final String newValue, final DefinitionStatus newStatus,
						final List<ProcedureProcessDefinition> works) {

					final Window window = new Window();
					window.setModal(true);
					window.setContent(new HorizontalLayout());
					window.setCaption("Маршрут версии " + df.format(works.get(0).getVersion())
							+ " будет отправлен в архив и новые заявки по нему приниматься не будут");
					Button save = new Button("Да");
					save.addListener(new ClickListener() {

						private static final long serialVersionUID = 3229924940535642819L;

						@Override
						public void buttonClick(ClickEvent event) {
							ManagerService.get().updateProcessDefinationStatus(p.getProcessDefinitionId(), newStatus);
							label.setValue(null);
							label.setCaption(newValue);
							paramLazyLoadingContainer.fireItemSetChange();
							proceduresContainer.fireItemSetChange();
							closeWindow(thisWindow, window);
						}

					});

					window.addComponent(save);
					Button c2 = new Button("Нет");
					c2.addListener(new ClickListener() {

						private static final long serialVersionUID = 4502614143261892063L;

						@Override
						public void buttonClick(ClickEvent event) {
							closeWindow(thisWindow, window);
						}
					});
					window.addComponent(c2);
					thisWindow.addWindow(window);
				}
			});
			ll.addComponent(c);
		}
		item.addItemProperty("status", new ObjectProperty<Component>(ll));
		item.addItemProperty("date", Components.stringProperty(formatter.format(p.getDateCreated())));
        Employee creator = p.getCreator();
        item.addItemProperty("user", Components.stringProperty(creator==null? null: creator.getLogin()));

		Button b = new Button("Выгрузить");

		b.addListener(new ClickListener() {

			private static final long serialVersionUID = 1362078893385574138L;

			@Override
			public void buttonClick(ClickEvent event) {
				StreamSource streamSource = new StreamSource() {

					private static final long serialVersionUID = 456334952891567271L;

					public InputStream getStream() {
						return Functions.withEngine(new PF<InputStream>() {
							private static final long serialVersionUID = 1L;

							public InputStream apply(ProcessEngine s) {
								return s.getRepositoryService().getProcessModel(p.getProcessDefinitionId());
							}
						});
					}
				};
				final Application application = event.getButton().getApplication();
				StreamResource resource = new StreamResource(streamSource, "test" + ".xml", application) {
					private static final long serialVersionUID = -3869546661105572851L;
					public DownloadStream getStream() {
						final StreamSource ss = getStreamSource();
						if (ss == null) {
							return null;
						}
						final DownloadStream ds = new DownloadStream(ss.getStream(), getMIMEType(), getFilename());
						ds.setBufferSize(getBufferSize());
						ds.setCacheTime(getCacheTime());
						ds.setParameter("Content-Disposition", "attachment; filename=" + getFilename());
						return ds;
					}
				};
				Window window = event.getButton().getWindow();
				window.open(resource);
			}
		});
		item.addItemProperty("getRoute", new ObjectProperty<Component>(b));

		ObjectProperty<Component> buttonProperty = null;

		if (status.equals(DefinitionStatus.Debugging)) {
			DeploymentUploadReceiver receiver = new DeploymentUploadReceiver();

			DeploymentSucceededListener succeededListener = new DeploymentSucceededListener(receiver, procedureId,
					p.getProcessDefinitionId());
			succeededListener.addLoadingContainer(paramLazyLoadingContainer);
			succeededListener.addLoadingContainer(proceduresContainer);
			DeploymentAddUi addUi = new DeploymentAddUi(new DeploymentStartListener(), receiver, succeededListener);

			addUi.setSizeFull();
			buttonProperty = new ObjectProperty<Component>(addUi);
		} else {
			ClickListener l = new ClickListener() {

				private static final long serialVersionUID = 1362078893385574138L;

				@Override
				public void buttonClick(ClickEvent event) {

				}
			};
			buttonProperty = Components.buttonProperty("", l);
		}

		item.addItemProperty("download", buttonProperty);
		return item;
	}

	private static void closeWindow(final Window thisWindow, final Window window) {
		thisWindow.removeWindow(window);
		if (thisWindow.getParent() != null) {
			thisWindow.getParent().removeWindow(window);
		}
	}

	private LazyLoadingContainer2 paramLazyLoadingContainer;

	@Override
	public void setLazyLoadingContainer(LazyLoadingContainer paramLazyLoadingContainer) {
		this.paramLazyLoadingContainer = (LazyLoadingContainer2) paramLazyLoadingContainer;
	}

	@Override
	public void setSorting(Object[] propertyIds, boolean[] ascending) {
		String[] props = new String[propertyIds.length];
		for (int i = 0; i < propertyIds.length; i++) {
			props[i] = propertyIds[i].toString();
		}
		sortProps = props;
		sortAsc = ascending;
	}

}
