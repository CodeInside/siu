/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.xml.ws.transport.http.client;


import com.sun.istack.NotNull;
import com.sun.xml.ws.api.PropertySet;

import javax.xml.ws.handler.MessageContext;
import java.util.List;
import java.util.Map;

/**
 * Properties exposed from {@link com.sun.xml.ws.transport.http.client.HttpTransportPipe} for {@link com.sun.xml.ws.client.ResponseContext}.
 *
 * @author Kohsuke Kawaguchi
 */
final class HttpResponseProperties extends PropertySet {

    private final OepHttpClientTransport deferedCon;

    public HttpResponseProperties(@NotNull OepHttpClientTransport con) {
        this.deferedCon = con;
    }

    @Property(MessageContext.HTTP_RESPONSE_HEADERS)
    public Map<String, List<String>> getResponseHeaders() {
        return deferedCon.getHeaders();
    }

    @Property(MessageContext.HTTP_RESPONSE_CODE)
    public int getResponseCode() {
        return deferedCon.statusCode;
    }

    @Override
    protected PropertyMap getPropertyMap() {
        return model;
    }

    private static final PropertyMap model;

    static {
        model = parse(HttpResponseProperties.class);
    }
}