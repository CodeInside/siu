package ru.codeinside.xml.ws.transport.http.client;

import ru.codeinside.adm.database.SystemProperty;

import java.io.IOException;
import java.io.OutputStream;

final class DelegateStream extends OutputStream {
  private final OutputStream delegate;
  private final OutputStream dump;

  public DelegateStream(OutputStream delegate, OutputStream dump) {
    System.out.println("create delegate stream!");
    this.delegate = delegate;
    this.dump = dump;
  }

  @Override
  public void write(int b) throws IOException {
    delegate.write(b);
    dump.write(b);
  }

  @Override
  public void write(byte[] b) throws IOException {
    delegate.write(b);
    dump.write(b);
  }

  @Override
  public void write(byte[] b, int off, int len) throws IOException {
    delegate.write(b, off, len);
    dump.write(b, off, len);
  }

  @Override
  public void flush() throws IOException {
    delegate.flush();
    dump.flush();
  }

  @Override
  public void close() throws IOException {
    delegate.close();
  }
}
