package ru.codeinside.xml.ws.transport.http.client;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

final class DumpInputStream extends FilterInputStream {

  final OutputStream dump;

  public DumpInputStream(InputStream in, OutputStream dump) {
    super(in);
    this.dump = dump;
  }

  @Override
  public int read() throws IOException {
    int n = in.read();
    if (n >= 0) {
      try {
        dump.write(n);
      } catch (IOException ignore) {
      }
    }
    return n;
  }

  @Override
  public int read(byte[] b, int off, int len) throws IOException {
    int n = in.read(b, off, len);
    if (n > 0) {
      try {
        dump.write(b, off, n);
      } catch (IOException ignore) {
      }
    }
    return n;
  }
}
