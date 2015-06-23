/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2015, MPL CodeInside http://codeinside.ru
 */
package ru.codeinside.log;

import ru.codeinside.adm.AdminServiceProvider;
import ru.codeinside.gses.activiti.forms.Signatures;
import ru.codeinside.gses.webui.form.SignatureType;

import java.io.File;
import java.io.IOException;

/**
 * Логирует подписи СП и ОВ
 */
public class SignatureLogger {
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(SignatureLogger.class.getName());
    private static final String basePath = "logs";

    private final Long bidId;
    private final String taskId;

    public SignatureLogger(Long bidId, String taskId) {
        this.taskId = taskId;
        if (bidId == null) {
            this.bidId = getBidId();
        } else {
            this.bidId = bidId;
        }
    }

    public void log(String data, Signatures signatures, SignatureType signatureType) {
        File logFile;
        try {
            logFile = prepareToLog(signatureType);
        } catch (IOException e) {
            logger.throwing(SignatureLogger.class.getName(), "log", e);
            return;
        }

        if (logFile == null) {
            logger.warning("Log file already exist: bidId = " + bidId + " signatureType = " + signatureType.name()
                    + ". WHY!?");
            return;
        }
    }

    private File prepareToLog(SignatureType signatureType) throws IOException {
        File rootFile = new File(basePath);
        if (!rootFile.exists()) {
            rootFile.mkdirs();
        }

        File bidFile = new File(rootFile.getAbsolutePath() + File.separator + String.valueOf(bidId));
        if (bidFile.exists()) {
            bidFile.mkdir();
        }

        String suffix = (taskId == null ? "" : taskId) + ".log";
        File signFile = new File(bidFile.getAbsolutePath() + File.separator + signatureType.name() + "-" + suffix);
        if (!signFile.createNewFile()) {
            return null;
        }
        return signFile;
    }

    private Long getBidId() {
        return AdminServiceProvider.get().getBidByTask(taskId).getId();
    }
}
