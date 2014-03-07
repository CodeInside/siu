package ru.codeinside.xml.ws.transport.http.client;

import java.io.IOException;
import java.io.OutputStream;

final class DumpOutputStream extends OutputStream {

  final OutputStream out;
  final OutputStream dump;

  public DumpOutputStream(OutputStream out, OutputStream dump) {
    this.out = out;
    this.dump = dump;
  }

  @Override
  public void write(int b) throws IOException {
    out.write(b);
    try {
      dump.write(b);
    } catch (IOException ignore) {
    }
  }

  @Override
  public void write(byte[] b) throws IOException {
    write(b, 0, b.length);
  }

  @Override
  public void write(byte[] b, int off, int len) throws IOException {
    out.write(b, off, len);
    try {
      dump.write(b, off, len);
    } catch (IOException ignore) {
    }
  }

  @Override
  public void flush() throws IOException {
    out.flush();
  }

  @Override
  public void close() throws IOException {
    out.close();
  }
}
