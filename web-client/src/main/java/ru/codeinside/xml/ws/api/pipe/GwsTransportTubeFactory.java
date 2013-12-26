/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.xml.ws.api.pipe;

import com.sun.xml.ws.api.pipe.ClientTubeAssemblerContext;
import com.sun.xml.ws.api.pipe.TransportTubeFactory;
import com.sun.xml.ws.api.pipe.Tube;
import ru.codeinside.xml.ws.transport.http.client.OepHttpTransportPipe;

import javax.xml.ws.WebServiceException;

public class GwsTransportTubeFactory extends TransportTubeFactory {

    @Override
    public Tube doCreate(ClientTubeAssemblerContext context) {
        return createDefault(context);
    }

    protected Tube createDefault(ClientTubeAssemblerContext context) {
        // default built-in transports
        String scheme = context.getAddress().getURI().getScheme();
        if (scheme != null) {
            if(scheme.equalsIgnoreCase("http") || scheme.equalsIgnoreCase("https"))
                return createHttpTransport(context);
        }
        throw new WebServiceException("Unsupported endpoint address: "+context.getAddress());    // TODO: i18n
    }

    protected Tube createHttpTransport(ClientTubeAssemblerContext context) {
        return new OepHttpTransportPipe(context.getCodec(), context.getBinding());
    }
}