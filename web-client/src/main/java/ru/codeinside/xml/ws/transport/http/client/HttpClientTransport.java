/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.xml.ws.transport.http.client;

import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import com.sun.xml.ws.api.EndpointAddress;
import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.client.BindingProviderProperties;
import com.sun.xml.ws.client.ClientTransportException;
import com.sun.xml.ws.developer.JAXWSProperties;
import com.sun.xml.ws.resources.ClientMessages;
import com.sun.xml.ws.transport.Headers;
import ru.codeinside.gws.api.ClientLog;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.handler.MessageContext;
import java.io.*;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static com.sun.xml.ws.client.BindingProviderProperties.HOSTNAME_VERIFICATION_PROPERTY;

public class HttpClientTransport {

  private static final byte[] THROW_AWAY_BUFFER = new byte[8192];

  // Need to use JAXB first to register DatatypeConverter
  static {
    try {
      JAXBContext.newInstance().createUnmarshaller();
    } catch (JAXBException je) {
      // Nothing much can be done. Intentionally left empty
    }
  }

  int statusCode;
  String statusMessage;
  int contentLength;
  private final Map<String, List<String>> reqHeaders;
  private Map<String, List<String>> respHeaders = null;

  private OutputStream outputStream;
  private boolean https;
  private HttpURLConnection httpConnection = null;
  private final EndpointAddress endpoint;
  private final Packet context;
  private final Integer chunkSize;
  private final ClientLog clientLog;


  public HttpClientTransport(@NotNull Packet packet, @NotNull Map<String, List<String>> reqHeaders) {
    endpoint = packet.endpointAddress;
    context = packet;
    this.reqHeaders = reqHeaders;
    chunkSize = (Integer) context.invocationProperties.get(JAXWSProperties.HTTP_CLIENT_STREAMING_CHUNK_SIZE);
    clientLog = (ClientLog) packet.invocationProperties.get(ClientLog.class.getName());
  }

  /*
   * Prepare the stream for HTTP request
   */
  OutputStream getOutput() {
    try {
      createHttpConnection();
      OutputStream logger = null;
      if (clientLog != null) {
        logger = clientLog.getHttpOutStream();
        dumpURI(logger);
        dumpHeaders(logger, reqHeaders);
      }
      if (requiresOutputStream()) {
        outputStream = httpConnection.getOutputStream();
        if (logger != null) {
          outputStream = new DumpOutputStream(outputStream, logger);
        }
        if (chunkSize != null) {
          outputStream = new WSChunkedOuputStream(outputStream, chunkSize);
        }
        List<String> contentEncoding = reqHeaders.get("Content-Encoding");
        // TODO need to find out correct encoding based on q value - RFC 2616
        if (contentEncoding != null && contentEncoding.get(0).contains("gzip")) {
          outputStream = new GZIPOutputStream(outputStream);
        }
      }
      httpConnection.connect();
    } catch (Exception ex) {
      throw new ClientTransportException(
        ClientMessages.localizableHTTP_CLIENT_FAILED(ex), ex);
    }
    return outputStream;
  }

  private void dumpURI(OutputStream logger) {
    PrintWriter pw = new PrintWriter(logger, true);
    pw.print(httpConnection.getRequestMethod());
    pw.print(" ");
    pw.print(endpoint);
    pw.println(" HTTP/1.1");
  }

  private void dumpHeaders(OutputStream logger, Map<String, List<String>> headers) {
    PrintWriter pw = new PrintWriter(logger, true);
    for (Map.Entry<String, List<String>> headerSet : headers.entrySet()) {
      String name = headerSet.getKey();
      List<String> values = headerSet.getValue();
      if (values.isEmpty()) {
        pw.print(name);
        pw.print(": ");
        pw.println(values);
      } else {
        for (String value : values) {
          if (name != null) {
            pw.print(name);
            pw.print(": ");
          }
          pw.println(value);
        }
      }
    }
    pw.println();
    pw.flush();
  }

  void closeOutput() throws IOException {
    if (outputStream != null) {
      outputStream.close();
      outputStream = null;
    }
  }

  /*
   * Get the response from HTTP connection and prepare the input stream for response
   */
  @Nullable
  InputStream getInput() {
    InputStream in;
    try {
      in = readResponse();
      if (in != null) {
        String contentEncoding = httpConnection.getContentEncoding();
        if (contentEncoding != null && contentEncoding.contains("gzip")) {
          in = new GZIPInputStream(in);
        }
      }
    } catch (IOException e) {
      throw new ClientTransportException(ClientMessages.localizableHTTP_STATUS_CODE(statusCode, statusMessage), e);
    }
    return in;
  }

  public Map<String, List<String>> getHeaders() {
    if (respHeaders != null) {
      return respHeaders;
    }
    respHeaders = new Headers();
    respHeaders.putAll(httpConnection.getHeaderFields());
    return respHeaders;
  }

  protected
  @Nullable
  InputStream readResponse() {
    InputStream is;
    try {
      is = httpConnection.getInputStream();
    } catch (IOException ioe) {
      is = httpConnection.getErrorStream();
    }

    if (is == null) {
      return is;
    }

    if (clientLog != null) {
      is = new DumpInputStream(is, clientLog.getHttpInStream());
    }

    // Since StreamMessage doesn't read </s:Body></s:Envelope>, there
    // are some bytes left in the InputStream. This confuses JDK and may
    // not reuse underlying sockets. Hopefully JDK fixes it in its code !
    final InputStream temp = is;
    return new FilterInputStream(temp) {
      // Workaround for "SJSXP XMLStreamReader.next() closes stream".
      // So it doesn't read from the closed stream
      boolean closed;

      @Override
      public void close() throws IOException {
        if (!closed) {
          closed = true;
          while (temp.read(THROW_AWAY_BUFFER) != -1) ;
          super.close();
        }
      }
    };
  }

  protected void readResponseCodeAndMessage() {
    try {
      statusCode = httpConnection.getResponseCode();
      statusMessage = httpConnection.getResponseMessage();
      contentLength = httpConnection.getContentLength();
      if (clientLog != null) {
        dumpHeaders(clientLog.getHttpInStream(), getHeaders());
      }
    } catch (IOException ioe) {
      throw new WebServiceException(ioe);
    }
  }

  protected boolean checkHTTPS(HttpURLConnection connection) {
    if (connection instanceof HttpsURLConnection) {

      // does the client want client hostname verification by the service
      String verificationProperty =
        (String) context.invocationProperties.get(HOSTNAME_VERIFICATION_PROPERTY);
      if (verificationProperty != null) {
        if (verificationProperty.equalsIgnoreCase("true")) {
          ((HttpsURLConnection) connection).setHostnameVerifier(new HttpClientVerifier());
        }
      }

      // Set application's HostNameVerifier for this connection
      HostnameVerifier verifier =
        (HostnameVerifier) context.invocationProperties.get(JAXWSProperties.HOSTNAME_VERIFIER);
      if (verifier != null) {
        ((HttpsURLConnection) connection).setHostnameVerifier(verifier);
      }

      // Set application's SocketFactory for this connection
      SSLSocketFactory sslSocketFactory =
        (SSLSocketFactory) context.invocationProperties.get(JAXWSProperties.SSL_SOCKET_FACTORY);
      if (sslSocketFactory != null) {
        ((HttpsURLConnection) connection).setSSLSocketFactory(sslSocketFactory);
      }

      return true;
    }
    return false;
  }

  private void createHttpConnection() throws IOException {
    httpConnection = (HttpURLConnection) endpoint.openConnection();

    String scheme = endpoint.getURI().getScheme();
    if (scheme.equals("https")) {
      https = true;
    }
    if (checkHTTPS(httpConnection))
      https = true;

    // allow interaction with the web page - user may have to supply
    // username, password id web page is accessed from web browser
    httpConnection.setAllowUserInteraction(true);

    // enable input, output streams
    httpConnection.setDoOutput(true);
    httpConnection.setDoInput(true);

    String requestMethod = (String) context.invocationProperties.get(MessageContext.HTTP_REQUEST_METHOD);
    String method = (requestMethod != null) ? requestMethod : "POST";
    httpConnection.setRequestMethod(method);

    Integer reqTimeout = (Integer) context.invocationProperties.get(BindingProviderProperties.REQUEST_TIMEOUT);
    if (reqTimeout != null) {
      httpConnection.setReadTimeout(reqTimeout);
    }

    Integer connectTimeout = (Integer) context.invocationProperties.get(JAXWSProperties.CONNECT_TIMEOUT);
    if (connectTimeout != null) {
      httpConnection.setConnectTimeout(connectTimeout);
    }

    Integer chunkSize = (Integer) context.invocationProperties.get(JAXWSProperties.HTTP_CLIENT_STREAMING_CHUNK_SIZE);
    if (chunkSize != null) {
      httpConnection.setChunkedStreamingMode(chunkSize);
    }

    // set the properties on HttpURLConnection
    for (Map.Entry<String, List<String>> entry : reqHeaders.entrySet()) {
      if ("Content-Length".equals(entry.getKey())) continue;
      for (String value : entry.getValue()) {
        httpConnection.addRequestProperty(entry.getKey(), value);
      }
    }
  }

  boolean isSecure() {
    return https;
  }

  private boolean requiresOutputStream() {
    return !(httpConnection.getRequestMethod().equalsIgnoreCase("GET") ||
      httpConnection.getRequestMethod().equalsIgnoreCase("HEAD") ||
      httpConnection.getRequestMethod().equalsIgnoreCase("DELETE"));
  }

  @Nullable
  String getContentType() {
    return httpConnection.getContentType();
  }

  // overide default SSL HttpClientVerifier to always return true
  // effectively overiding Hostname client verification when using SSL
  private static class HttpClientVerifier implements HostnameVerifier {
    public boolean verify(String s, SSLSession sslSession) {
      return true;
    }
  }

  /**
   * HttpURLConnection.getOuputStream() returns sun.net.www.http.ChunkedOuputStream in chunked
   * streaming mode. If you call ChunkedOuputStream.write(byte[20MB], int, int), then the whole data
   * is kept in memory. This wraps the ChunkedOuputStream so that it writes only small
   * chunks.
   */
  private static final class WSChunkedOuputStream extends FilterOutputStream {
    final int chunkSize;

    WSChunkedOuputStream(OutputStream actual, int chunkSize) {
      super(actual);
      this.chunkSize = chunkSize;
    }

    @Override
    public void write(byte b[], int off, int len) throws IOException {
      while (len > 0) {
        int sent = (len > chunkSize) ? chunkSize : len;
        out.write(b, off, sent);        // don't use super.write() as it writes byte-by-byte
        len -= sent;
        off += sent;
      }
    }

  }

}

