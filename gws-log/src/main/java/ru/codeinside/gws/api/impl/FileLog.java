package ru.codeinside.gws.api.impl;

import ru.codeinside.gws.api.Packet;
import ru.codeinside.gws.log.format.Metadata;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.util.UUID;
import java.util.logging.Level;

import static ru.codeinside.gws.api.Packet.Status.*;

class FileLog {

  FileLog(boolean isLogEnabled, boolean logErrors, String status) {
    this.logErrors = logErrors;
    this.isLogEnabled = isLogEnabled;
    if (status != null && (status.contains(REQUEST.toString()) || status.contains(RESULT.toString()))) {
      this.status = status;
    }
  }

  final Metadata metadata = new Metadata();
  final String dirName = UUID.randomUUID().toString().replace("-", "");

  private OutputStream httpOut;
  private OutputStream httpIn;
  boolean isLogEnabled;
  boolean logErrors;
  String status = null;

  public final void log(Throwable e) {
    Files.logFailure(metadata, e, dirName);
  }

  public final OutputStream getHttpOutStream() {
    if (httpOut == null) {
      try {
        File file = Files.createSpoolFile(Metadata.HTTP_SEND, dirName);
        httpOut = Files.fileOut(file);
      } catch (FileNotFoundException e) {
        LogServiceFileImpl.LOGGER.log(Level.WARNING, "create spool file fail", e);
        httpOut = new NullOutputStream();
      }
    }
    return httpOut;
  }

  public final OutputStream getHttpInStream() {
    if (httpIn == null) {
      try {
        File file = Files.createSpoolFile(Metadata.HTTP_RECEIVE, dirName);
        httpIn = Files.fileOut(file);
      } catch (FileNotFoundException e) {
        LogServiceFileImpl.LOGGER.log(Level.WARNING, "create spool file fail", e);
        httpIn = new NullOutputStream();
      }
    }
    return httpIn;
  }

  public final void close() {
    Files.close(httpOut, httpIn);
    boolean errorCase = metadata.error != null && logErrors;
    boolean matchedStatus;
    matchedStatus = status == null ||
      (metadata.send != null && status.contains(metadata.send.status)) ||
      (metadata.receive != null && status.contains(metadata.receive.status));
    if (errorCase  || (isLogEnabled && matchedStatus)) {
      Files.moveFromSpool(dirName);
    } else {
      Files.deleteFromSpool(dirName);
    }
  }


  final void logSendPacket(Packet packet) {
    metadata.send = Files.getPack(packet);
    Files.writeMetadataToSpool(metadata, dirName);
  }

  final void logReceivePacket(Packet packet) {
    metadata.receive = Files.getPack(packet);
    Files.writeMetadataToSpool(metadata, dirName);
  }
}
