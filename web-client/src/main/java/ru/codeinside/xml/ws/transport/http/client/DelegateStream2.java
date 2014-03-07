package ru.codeinside.xml.ws.transport.http.client;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

final class DelegateStream2 extends FilterInputStream {
  private final OutputStream dump;

  public DelegateStream2(InputStream in, OutputStream dump) {
    super(in);
    this.dump = dump;
    System.out.println("create delegate stream2!");
  }

  @Override
  public int read() throws IOException {
    int n = in.read();
    if (n >= 0) {
      dump.write(n);
    }
    return n;
  }

  @Override
  public int read(byte[] b, int off, int len) throws IOException {
    int n = super.read(b, off, len);
    if (n > 0) {
      dump.write(b, off, n);
    }
    return n;
  }
}
