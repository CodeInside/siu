/**
 * PETALS - PETALS Services Platform.
 * Copyright (c) 2008 OW2 Consortium, http://www.ow2.org/
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * -------------------------------------------------------------------------
 * $Id$
 * -------------------------------------------------------------------------
 */

package org.ow2.easywsdl.wsdl.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Initial developer(s): Christophe DENEUX
 * 
 * @author cdeneux - Capgemini Sud
 * 
 */
public class InputStreamForker {

    private InputStream originalInputStream;

    private final ForkedInputStream is1;

    private final ForkedInputStream is2;

    /**
     * true if the close() method has been called on two forked input
     * streams
     */
    protected boolean originalInputStreamClosed = false;

    public InputStreamForker(final InputStream originalInputStream) {
        this.originalInputStream = originalInputStream;

        this.is1 = new ForkedInputStream();
        this.is2 = new ForkedInputStream();
        
        this.is1.setOtherForkedInputStream(this.is2);
        this.is2.setOtherForkedInputStream(this.is1);

    }
    
    public InputStream getInputStreamOne() {
        return this.is1;
    }

    public InputStream getInputStreamTwo() {
        return this.is2;
    }

    protected class ForkedInputStream extends InputStream {

        final private LinkedList<byte[]> bufferList;
        
        private boolean isClosed = false;
        
        /**
         * The other forked input stream.
         */
        private ForkedInputStream otherForkedInputStream;

        /**
         * A reference on the other branch of inputstream to improve
         * performances (avoid to call {@link ForkedInputStream#getBufferList()}
         * on {@link #otherForkedInputStream}).
         */
        private LinkedList<byte[]> otherBufferList;

        public ForkedInputStream() {
            this.bufferList = new LinkedList<byte[]>();;
        }
        
        public void setOtherForkedInputStream(final ForkedInputStream otherForkedInputStream) {
            this.otherForkedInputStream = otherForkedInputStream;
            this.otherBufferList = this.otherForkedInputStream.getBufferList();
        }
        
        public LinkedList<byte[]> getBufferList() {
            return this.bufferList;
        }
        public boolean isClosed() {
            return this.isClosed;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int available() throws IOException {
            synchronized (InputStreamForker.this) {

                int available = 0;

                final Iterator<byte[]> iterator = this.bufferList.iterator();
                while (iterator.hasNext()) {
                    available += iterator.next().length;
                }

                if (!InputStreamForker.this.originalInputStreamClosed && !this.isClosed) {
                    available += InputStreamForker.this.originalInputStream
                            .available();
                }

                return available;
            }
        }

        /**
         * <p>{@inheritDoc}</p>
         * <p>If both forked input streams are closed, the original input stream is closed.</p>
         * <p>If the forked stream has already been closed, this method has no effect.</p>
         */
        @Override
        public void close() throws IOException {
            if (!this.isClosed) {
                synchronized (InputStreamForker.this) {
                    this.isClosed = true;
                    
                    // Note: It's not needed to check if the original input
                    // stream is closed because it is closed when closing one of
                    // two forked input streams. So, as the current forked input
                    // stream is not closed, the original input stream is not
                    // closed.
                    
                    if (this.otherForkedInputStream.isClosed) {
                        // The other forked input stream is closed, we close the
                        // original input stream
                        InputStreamForker.this.originalInputStream.close();
                        InputStreamForker.this.originalInputStreamClosed = true;
                    }
                }
            }
        }

        @Override
        public boolean markSupported() {
            return false;
        }

        /**
         * <p>
         * Reads the next byte of data from the input stream. The value byte is
         * returned as an <code>int</code> in the range <code>0</code> to
         * <code>255</code>. If no byte is available because the end of the
         * stream has been reached, the value <code>-1</code> is returned.
         * This method blocks until input data is available, the end of the
         * stream is detected, or an exception is thrown.
         * </p>
         * <p>
         * The byte is read in the internal buffer. If no data is available in
         * the internal buffer, the byte is read from the original input stream.
         * </p>
         * 
         * @return the next byte of data, or <code>-1</code> if the end of the
         *         stream is reached.
         * @exception IOException
         *                if an I/O error occurs.
         */
        @Override
        public int read() throws IOException {
            byte[] buffer = new byte[1];
            int nbByteRead = this.read(buffer);
            if (nbByteRead != 1) {
                throw new IOException("No byte read.");
            }
            return buffer[0];
        }

        @Override
        public int read(final byte[] cbuf) throws IOException {
            return this.read(cbuf, 0, cbuf.length);
        }

        @Override
        public int read(final byte[] cbuf, final int off, final int len)
                throws IOException {
            return this.localRead(cbuf, off, len, false);
        }

        @Override
        public long skip(long n) throws IOException, IllegalArgumentException {
            if (n > Long.MAX_VALUE) {
                throw new IllegalArgumentException("Only value lesser "
                        + Integer.MAX_VALUE + "are accepted.");
            }
            return this.localRead(null, 0, (int) n, true);
        }

        private int localRead(final byte[] cbuf, final int off, final int len,
                final boolean skip) throws IOException {
            int remainingLen = len;
            int offset = off;
            int byteRead;

            // Original input stream reading and buffer list management must
            // be synchronized. Otherwise, inversions can occur.
            synchronized (InputStreamForker.this) {
                // First we read from the internal buffers
                int nbBuffers = this.bufferList.size();
                while (nbBuffers > 0 && remainingLen != 0) {
                    final byte[] buffer = this.bufferList.getFirst();

                    if (remainingLen >= buffer.length) {
                        // Full copy needed
                        if (!skip) {
                            System.arraycopy(buffer, 0, cbuf, offset,
                                    buffer.length);
                        }

                        this.bufferList.removeFirst();
                        
                        remainingLen -= buffer.length;
                        if (remainingLen == 0) {
                            // No more bytes must be read
                            return len;
                        }
                        offset += buffer.length;
                        
                        nbBuffers--;
                    } else {
                        // Partial copy needed because the buffer len to read is
                        // reached
                        if (!skip) {
                            System.arraycopy(buffer, 0, cbuf, offset,
                                    remainingLen);
                        }

                        // Replace byte buffer by a buffer containing remaining
                        // bytes
                        final byte[] newBuffer = new byte[buffer.length
                                - remainingLen];
                        System.arraycopy(buffer, remainingLen, newBuffer, 0,
                                buffer.length - remainingLen);
                        this.bufferList.removeFirst();
                        this.bufferList.addFirst(newBuffer);

                        return len;
                    }
                }

                // Next, we read missing bytes from the original input stream
                if (InputStreamForker.this.originalInputStreamClosed) {
                    throw new IOException(
                            "The original InputStream has been closed; cannot read from a closed InputStream.");
                }

                byte[] buffer = new byte[remainingLen];
                byteRead = InputStreamForker.this.originalInputStream.read(
                        buffer, 0, remainingLen);
                
                if (byteRead != -1) {

                    // We add the read bytes into the other branch, only if it is not closed.
                    if (!this.otherForkedInputStream.isClosed()) {
                        if (byteRead == remainingLen) {
                            // We can add the buffer as is because of its length is
                            // right.
                            this.otherBufferList.addLast(buffer);
                        } else {
                            final byte[] newBuffer = new byte[byteRead];
                            System.arraycopy(buffer, 0, newBuffer, 0, byteRead);
                            this.otherBufferList.addLast(newBuffer);
                        }
                    }
    
                    if (!skip) {
                        System.arraycopy(buffer, 0, cbuf, offset, remainingLen);
                    }
                    
                    return len - remainingLen + byteRead;
                } else {
                    // EOF of the originial input stream is reached, we must
                    // return -1 (EOF) if no bytes have been read from the
                    // buffer list.
                    if (len == remainingLen) {
                        // no bytes have been read from the buffer list.
                        return -1;
                    }
                    else {
                        // bytes have been read from the buffer list.
                        return len - remainingLen;
                    }
                }
            }
        }
    }
}
