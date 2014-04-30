package ru.codeinside.gws.api.impl;

import ru.codeinside.gws.api.Packet;
import ru.codeinside.gws.api.Packet.Status;
import ru.codeinside.gws.log.format.Metadata;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.util.UUID;
import java.util.logging.Level;

class FileLog {

  final Metadata metadata = new Metadata();
  final String dirName = UUID.randomUUID().toString().replace("-", "");

  private final boolean isLogEnabled;
  private final boolean logErrors;
  private final String status;

  private OutputStream httpOut;
  private OutputStream httpIn;


  FileLog(boolean isLogEnabled, boolean logErrors, String status) {
    this.isLogEnabled = isLogEnabled;
    this.logErrors = logErrors;
    if (status != null && status.contains(Status.RESULT.name())) {
      // считаем отклонение результатом:
      this.status = Status.REJECT.name() + status;
    } else {
      this.status = status;
    }
  }

  public final String getDirName() {
    return dirName;
  }

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

    boolean hit = false;

    if (logErrors) {
      hit = metadata.error != null;
      if (!hit) {
        // учтём ошибки идентификации статуса:
        hit = metadata.send == null
          || metadata.send.status == null
          || metadata.receive == null
          || metadata.receive.status == null;
      }
      if (!hit) {
        // ошибки исполнения:
        String invalid = Status.INVALID.name();
        String failure = Status.FAILURE.name();
        hit = invalid.equals(metadata.send.status)
          || failure.equals(metadata.send.status)
          || invalid.equals(metadata.receive.status)
          || failure.equals(metadata.receive.status);
      }
    }

    if (!hit && isLogEnabled) {
      hit = status == null;
      if (!hit) {
        hit = (metadata.send != null && metadata.send.status != null && status.contains(metadata.send.status))
          || (metadata.receive != null && metadata.receive.status != null && status.contains(metadata.receive.status));
      }
    }

    if (hit) {
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
