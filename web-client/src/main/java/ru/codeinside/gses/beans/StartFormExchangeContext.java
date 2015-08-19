package ru.codeinside.gses.beans;

import org.activiti.engine.delegate.VariableScope;
import org.apache.commons.lang.StringUtils;
import org.apache.tika.mime.MimeTypes;
import ru.codeinside.gses.activiti.ftarchive.AttachmentFFT;
import ru.codeinside.gses.webui.form.DataAccumulator;
import ru.codeinside.gses.webui.form.ProtocolUtils;
import ru.codeinside.gses.webui.form.StartEventAttachmentConverter;
import ru.codeinside.gses.webui.form.TmpAttachment;
import ru.codeinside.gws.api.Enclosure;
import ru.codeinside.gws.api.ExchangeContext;

import java.io.ByteArrayInputStream;
import java.util.Set;

public class StartFormExchangeContext implements ExchangeContext {
  private final MimeTypes mimeTypes = new MimeTypes();
  private final VariableScope variableScope;
  private final DataAccumulator dataAccumulator;
  private Object local;

  public StartFormExchangeContext(VariableScope variableScope, DataAccumulator dataAccumulator) {
    this.variableScope = variableScope;
    this.dataAccumulator = dataAccumulator;
  }

  @Override
  public Object getLocal() {
    return local;
  }

  @Override
  public void setLocal(Object value) {
    this.local = value;
  }

  @Override
  public Set<String> getVariableNames() {
    return variableScope.getVariableNames();
  }

  @Override
  public Object getVariable(String name) {
    return variableScope.getVariable(name);
  }

  @Override
  public boolean isEnclosure(String name) {
    final Object value = variableScope.getVariable(name);
    return AttachmentFFT.isAttachmentValue(value);
  }

  @Override
  public void setVariable(String name, Object value) {
    variableScope.setVariable(name, value);
  }

  @Override
  public Enclosure getEnclosure(String name) {
    final Object value = variableScope.getVariable(name);
    String attId = AttachmentFFT.getAttachmentIdByValue(value);
    if (StringUtils.isEmpty(attId)) {
      return null;
    }

    TmpAttachment attachment = dataAccumulator.getAttachment(attId);
    if (attachment == null) {
      return null;
    }

    return ProtocolUtils.createEnclosureInStartEventContext(attachment, name);
  }

  @Override
  public void addEnclosure(String name, Enclosure enclosure) {
    ByteArrayInputStream content = new ByteArrayInputStream(enclosure.content);
    String mimeType = StringUtils.isNotEmpty(enclosure.mimeType) ? enclosure.mimeType : mimeTypes.getMimeType(enclosure.content).getName();
    TmpAttachment attachment = StartEventAttachmentConverter.createTmpAttachment(mimeType, enclosure.fileName, content);
    setVariable(name, AttachmentFFT.stringValue(attachment));
    dataAccumulator.setAttachment(attachment);
  }
}
