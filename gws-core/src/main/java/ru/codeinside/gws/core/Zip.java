/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.core;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import ru.codeinside.gws.api.CryptoProvider;
import ru.codeinside.gws.api.Enclosure;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

final public class Zip {

    final static Logger LOG = Logger.getLogger(Zip.class.getName());

    static Map<String, Enclosure> collectAttachments(final InputStream in) {
        final Map<String, Enclosure> attachments = new LinkedHashMap<String, Enclosure>();
        ZipArchiveInputStream zip = null; 
        try {
        	zip = new ZipArchiveInputStream(in);
            while (true) {
                final ZipArchiveEntry zipEntry = zip.getNextZipEntry();
                if (zipEntry == null) {
                    break;
                }
                if (zipEntry.isDirectory()) {
                    continue;
                }
                final long total = zipEntry.getSize();
                zipEntry.getExtra();
                final byte[] content;
                if (total >= 0) {
                    content = readWithTOC(zip, (int) total);
                } else {
                    content = readWithDynamicSize(zip);
                }
                String zipPath = zipEntry.getName();                
                int slash = zipPath.lastIndexOf('/');
                if (slash < 0) {
                    slash = 0;
                }
                attachments.put(zipPath, new Enclosure(zipPath, zipPath.substring(slash), content));
            }
        } catch (IOException e) {
            LOG.log(Level.WARNING, "unzip fail", e);
        } finally {
            if (zip != null) {
                try {
                    zip.close();
                } catch (IOException e) {
                    //
                }
            }
        }
        return attachments;
    }

    public static String toBinaryData(final Enclosure[] enclosures, CryptoProvider cryptoProvider) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final ZipOutputStream zos = new ZipOutputStream(baos);
        zos.setLevel(9);
        for (final Enclosure enclosure : enclosures) {
            {
                final ZipEntry entry = new ZipEntry(enclosure.zipPath);
                entry.setSize(enclosure.content.length);
                entry.setTime(System.currentTimeMillis());
                zos.putNextEntry(entry);
                zos.write(enclosure.content);
            }
            if (enclosure.signature != null && enclosure.signature.valid) {
                final byte[] content = cryptoProvider.toPkcs7(enclosure.signature);
                final ZipEntry sig = new ZipEntry(enclosure.zipPath + ".sig");
                sig.setSize(content.length);
                sig.setTime(System.currentTimeMillis());
                zos.putNextEntry(sig);
                zos.write(content);
            }
        }
        zos.close();
        return DatatypeConverter.printBase64Binary(baos.toByteArray());
    }

    private static byte[] readWithTOC(final ZipArchiveInputStream zis, final int total) throws IOException {
        final byte[] content = new byte[total];
        int offset = 0;
        int read;
        while (offset < total && (read = zis.read(content, offset, total - offset)) != -1) {
            offset += read;
        }
        return content;
    }

    private static byte[] readWithDynamicSize(final ZipArchiveInputStream zis) throws IOException {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int c;
        while ((c = zis.read()) != -1) {
            bos.write(c);
        }
        return bos.toByteArray();
    }
   
}
