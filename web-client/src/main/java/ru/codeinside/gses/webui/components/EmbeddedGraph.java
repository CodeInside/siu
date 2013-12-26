/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.components;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;

import javax.imageio.ImageIO;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.diagram.ProcessDiagramGenerator;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;

import ru.codeinside.gses.service.Functions;
import ru.codeinside.gses.webui.Flash;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.vaadin.Application;
import com.vaadin.event.MouseEvents.ClickEvent;
import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.terminal.StreamResource;
import com.vaadin.terminal.StreamResource.StreamSource;
import com.vaadin.ui.Embedded;

public class EmbeddedGraph extends Embedded {
	private static final long serialVersionUID = 1L;

	private int deltaX = 0;
	private int deltaY = 0;
	
	public EmbeddedGraph(ProcessDefinitionEntity entity, final List<String> activeActivityIds, final Application application) {
		this(entity, activeActivityIds, application, null, 0, 0);
	}
	
	public EmbeddedGraph(ProcessDefinitionEntity entity, final List<String> activeActivityIds, final Application application, final EmbeddedGraph graph, int newDeltaX, int newDeltaY) {
		super();
		if(graph != null){
			if(graph.deltaX + newDeltaX > 0){
				deltaX = graph.deltaX + newDeltaX;	
			}
			if(graph.deltaY + newDeltaY > 0){
				deltaY = graph.deltaY + newDeltaY;	
			}
			Collection<?> listeners = graph.getListeners(ClickEvent.class);
			for(Object o :listeners){
				addListener((ClickListener)o);
			}
		}
		final String id = entity.getId();
		setMimeType("image/png");
		StreamSource streamSource = new StreamSource() {
			private static final long serialVersionUID = 1L;

			public InputStream getStream() {
				ProcessDefinitionEntity entity = Functions.withRepository(Flash.login(),
				        new Function<RepositoryService, ProcessDefinitionEntity>() {
					        public ProcessDefinitionEntity apply(final RepositoryService srv) {
						        RepositoryServiceImpl impl = (RepositoryServiceImpl) srv;
						        return (ProcessDefinitionEntity) impl.getDeployedProcessDefinition(id);
					        }
				        });				
				InputStream streamDiagram = ProcessDiagramGenerator.generateDiagram(entity, "png", (activeActivityIds == null ? Lists.<String>newArrayList() : activeActivityIds));
				
				if(deltaX != 0 && deltaY != 0){					
					try {
						return changedInputStream(streamDiagram);
						
					} catch (IOException e) {
						e.printStackTrace();
					}
				}				
				
				return streamDiagram;
			}

			private InputStream changedInputStream(InputStream streamDiagram) throws IOException {
				BufferedImage originalImage = ImageIO.read(streamDiagram);
				int width = originalImage.getWidth() + deltaX;
				int height = originalImage.getHeight() + deltaY;

				BufferedImage bi = new BufferedImage(width, height, originalImage.getType());
				Graphics2D g = (Graphics2D)bi.getGraphics();
				g.drawImage(originalImage, 0, 0, width, height, null);
												
				g.dispose();
				
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				ImageIO.write(bi, "png", os);
				return new ByteArrayInputStream(os.toByteArray());
			}
		};
		setSource(new StreamResource(streamSource, id + ".png", application));
	}
}