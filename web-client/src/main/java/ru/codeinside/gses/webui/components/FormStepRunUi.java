/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.components;

import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.form.FormData;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricDetailQuery;
import org.activiti.engine.impl.persistence.entity.HistoricFormPropertyEntity;
import org.apache.commons.lang.StringUtils;

import ru.codeinside.gses.service.Functions;
import ru.codeinside.gses.service.PF;
import ru.codeinside.gses.vaadin.FieldFormType;
import ru.codeinside.gses.webui.Configurator;

import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.TextField;

public class FormStepRunUi extends CustomComponent {

	private static final long serialVersionUID = -1137978686087508536L;

	public FormStepRunUi(final String user, final String taskId, final String processInsId) {
		setWidth(800, Sizeable.UNITS_PIXELS);
		setHeight(600, Sizeable.UNITS_PIXELS);
		setCompositionRoot(Functions.withEngine(new FormStepProducer(taskId, user)));
	}

	public static Form createForm(FormData data, final String processInstanceId) {
		final Form form = new Form();
		form.setSizeFull();
		if (data == null) {
			return form;
		}

		form.getLayout().setStyleName("liquid1");

		for (FormProperty formProperty : data.getFormProperties()) {
			String name = formProperty.getName();
			if (formProperty.getType() != null) {
				if (formProperty.getId().equals("repeat")) {// TODO: WTF?
					repeatProcessFields(processInstanceId, form, formProperty);
					continue;
				}

				if (formProperty.getType().getName().equals("date")) {
					name += "дд/мм/гггг";
				}
				if (formProperty.getType().getName().equals("long")) {
					name += "число ";
				}
			}
			String value = StringUtils.isEmpty(formProperty.getValue()) ? "" : formProperty.getValue();
			Field field = createFieldByType(form, formProperty, name, value);
			form.addField(formProperty.getId(), field);
		}
		return form;
	}

	private static void repeatProcessFields(final String processInstId, final Form form, FormProperty formProperty) {
		final Map<String, String> information = (Map<String, String>) formProperty.getType().getInformation("values");
		Functions.withEngine(new PF<String>() {

			private static final long serialVersionUID = 1335145069347620746L;

			public String apply(ProcessEngine engine) {
				HistoricDetailQuery histQuery = engine.getHistoryService().createHistoricDetailQuery();
				for (HistoricDetail details : histQuery.processInstanceId(processInstId).formProperties().list()) {
					HistoricFormPropertyEntity hp = (HistoricFormPropertyEntity) details;
					if (information != null && !information.isEmpty()) {
						String value = information.get(hp.getPropertyId());
						if (StringUtils.isNotEmpty(value)) {
							String[] split = value.split(":");
							AbstractField field = new TextField(split[0], hp.getPropertyValue());
							field.setReadOnly(true);
							form.addField(hp.getPropertyId(), field);
						}
					} else {
						TextField field = new TextField(hp.getPropertyId(), hp.getPropertyValue());
						field.setReadOnly(true);
						form.addField(hp.getPropertyId(), field);
					}
				}
				return "";

			}
		});
	}

	private static Field createFieldByType(Form form, FormProperty formProperty, String name, String value) {
		if (formProperty.getType() == null) {
			return new TextField(name, value);
		}
		String typeName = formProperty.getType().getName();
		FieldFormType fieldFactory = Configurator.getFieldFormType(typeName);
		Field field = fieldFactory.createConstructorOfField().createField(name, value, form.getLayout(), formProperty.isWritable(),
                                                                      formProperty.isRequired());
		field.setCaption(name);
		field.setReadOnly(!formProperty.isWritable());
		field.setRequired(formProperty.isRequired());
		return field;
	}

}
