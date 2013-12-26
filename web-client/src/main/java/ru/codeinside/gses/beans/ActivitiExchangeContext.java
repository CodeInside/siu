/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.beans;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.impl.cmd.CreateAttachmentCmd;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.task.Attachment;
import org.apache.commons.lang.StringUtils;
import org.apache.tika.mime.MimeTypes;
import ru.codeinside.gses.activiti.Activiti;
import ru.codeinside.gses.activiti.ftarchive.AttachmentFFT;
import ru.codeinside.gses.activiti.history.ExecutionId;
import ru.codeinside.gses.activiti.history.HistoricDbSqlSession;
import ru.codeinside.gws.api.Enclosure;
import ru.codeinside.gws.api.ExchangeContext;
import ru.codeinside.gws.api.Signature;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateEncodingException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

//TODO переместить в другой пакет
public class ActivitiExchangeContext implements ExchangeContext {

    private final MimeTypes mimeTypes = new MimeTypes();
    private final Logger logger = Logger.getLogger(getClass().getName());
    private final DelegateExecution execution;
    private Object local;

    public ActivitiExchangeContext(DelegateExecution execution) {
        this.execution = execution;
    }

    @Override
    public void setVariable(String name, Object value) {
        logger.fine("Set  " + name + " = '" + value + "'");
        execution.setVariable(name, value);
    }

    @Override
    public Set<String> getVariableNames() {
        return execution.getVariableNames();
    }

    @Override
    public Object getVariable(String name) {
        final Object value = execution.getVariable(name);
        logger.fine("Get  " + name + " = '" + value + "'");
        return value;
    }

    @Override
    public Object getLocal() {
        return local;
    }

    @Override
    public void setLocal(Object value) {
        local = value;
    }

    @Override
    public boolean isEnclosure(String name) {
        final Object value = execution.getVariable(name);
        return AttachmentFFT.isAttachmentValue(value);
    }

    @Override
    public Enclosure getEnclosure(String name) {
        final Object value = execution.getVariable(name);
        String attId = AttachmentFFT.getAttachmentIdByValue(value);
        if (StringUtils.isEmpty(attId)) {
            return null;
        }
        return Activiti.createEnclosureInCommandContext(attId, execution.getId(), name);
    }

    //TODO переделать Cmd на сервисы
    @Override
    public void addEnclosure(String name, Enclosure enclosure) {
        ByteArrayInputStream content = new ByteArrayInputStream(enclosure.content);
        String mimeType = StringUtils.isNotEmpty(enclosure.mimeType) ? enclosure.mimeType : mimeTypes.getMimeType(enclosure.content).getName();
        Attachment attachment = new CreateAttachmentCmd(mimeType, execution.getId(), execution.getProcessInstanceId(), enclosure.fileName, name, content, null).execute(Context.getCommandContext());
        setVariable(name, AttachmentFFT.stringValue(attachment));

        final Signature signature = enclosure.signature;
        if (signature != null && signature.certificate != null && signature.sign != null && signature.valid) {
            try {
                final byte[] certificate = signature.certificate.getEncoded();
                final byte[] sign = signature.sign;
                final CommandContext ctx = Context.getCommandContext();
                final HistoricDbSqlSession session = (HistoricDbSqlSession) ctx.getDbSqlSession();
                // TODO: является ли execution.getId()==taskId ?
                final ExecutionId executionId = new ExecutionId(execution.getProcessInstanceId(), execution.getId(), execution.getId());
                session.addSignature(executionId, name, certificate, sign, true);
            } catch (CertificateEncodingException e) {
                logger.log(Level.WARNING, "encode certificate fail", e);
            }
        }
    }
}