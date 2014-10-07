/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.components;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.vaadin.Application;
import com.vaadin.terminal.StreamResource;
import com.vaadin.terminal.StreamResource.StreamSource;
import com.vaadin.ui.Embedded;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.diagram.ProcessDiagramGenerator;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.runtime.Execution;
import ru.codeinside.gses.service.F1;
import ru.codeinside.gses.service.Fn;
import ru.codeinside.gses.service.Functions;
import ru.codeinside.gses.webui.Flash;

import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

//TODO: хранить изображение в во временом файле, кеширование по названию между сессиями.
final public class TaskGraph extends Embedded {

  final static Joiner ID_JOINER = Joiner.on('/').skipNulls();

  final static Joiner FILE_JOINER = Joiner.on('_').skipNulls();

  final public boolean hasBlocks;
  public TaskGraph(final String processDefinitionId, final String executionId) {
    final List<String> activeActivityIds;
    final String baseName;
    final String appendix;
    if (executionId != null) {
      baseName = executionId;
      activeActivityIds = Fn.withEngine(new ActiveActivityIds(), executionId);
      if (activeActivityIds.isEmpty()) {
        appendix = null;
        hasBlocks = false;
      } else {
        hasBlocks = true;
        appendix = getSHA(ID_JOINER.join(activeActivityIds));
      }
    } else {
      activeActivityIds = Collections.emptyList();
      baseName = processDefinitionId.replace(':', '_');
      appendix = null;
      hasBlocks = false;
    }
    final String filename = FILE_JOINER.join("ex", baseName, appendix) + ".png";

    setMimeType("image/png");
    setSource(
      new AppCachedStreamSource(
        new GraphStreamSource(new GraphProducer(processDefinitionId, activeActivityIds)),
        filename,
        Flash.app()
      )
    );
  }

  final static class AppCachedStreamSource extends StreamResource {

    public AppCachedStreamSource(final StreamSource streamSource, final String filename, final Application application) {
      super(streamSource, filename, application);
    }

    @Override
    public int hashCode() {
      return getFilename().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (!(obj instanceof AppCachedStreamSource)) {
        return false;
      }
      return getFilename().equals(((AppCachedStreamSource) obj).getFilename());
    }
  }

  static String getSHA(final String string) {
    try {
      final MessageDigest md = MessageDigest.getInstance("SHA");
      md.update(string.getBytes("UTF8"));
      return new BigInteger(1, md.digest()).toString(16);
    } catch (NoSuchAlgorithmException e) {
      return "" + string.hashCode();
    } catch (UnsupportedEncodingException e) {
      return "" + string.hashCode();
    }
  }

  final static class GraphStreamSource implements StreamSource {

    final private GraphProducer graphProducer;

    public GraphStreamSource(final GraphProducer graphProducer) {
      this.graphProducer = graphProducer;
    }

    public InputStream getStream() {
      return Functions.withRepository(Flash.login(), graphProducer);
    }
  }

  final static class ActiveActivityIds implements F1<List<String>, String> {
    @Override
    public List<String> apply(ProcessEngine engine, final String executionId) {
      // может уже выполниться!
      final Execution execution = engine.getRuntimeService().createExecutionQuery().executionId(executionId).singleResult();
      if (execution == null) {
        return Collections.emptyList();
      }
      List<String> ids = engine.getRuntimeService().getActiveActivityIds(executionId);
      if (!ids.isEmpty()) {
        return ids;
      }
      return Arrays.asList(((ExecutionEntity) execution).getCurrentActivityId());
    }
  }

  final static class GraphProducer implements Function<RepositoryService, InputStream>, Serializable {
    private final String processDefinitionId;
    private final List<String> activeActivityIds;

    public GraphProducer(final String processDefinitionId, final List<String> activeActivityIds) {
      this.processDefinitionId = processDefinitionId;
      this.activeActivityIds = activeActivityIds;
    }

    final public InputStream apply(final RepositoryService repositoryService) {
      final RepositoryServiceImpl impl = (RepositoryServiceImpl) repositoryService;
      final ProcessDefinitionEntity def = (ProcessDefinitionEntity) impl.getDeployedProcessDefinition(processDefinitionId);
      return ProcessDiagramGenerator.generateDiagram(def, "png", activeActivityIds);
    }
  }
}

