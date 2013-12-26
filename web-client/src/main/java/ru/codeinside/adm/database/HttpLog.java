/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.database;

import javax.persistence.*;
import java.io.UnsupportedEncodingException;

@Embeddable
public class HttpLog {

    public HttpLog(){

    }

    public HttpLog(String msg){
        try {
            data = msg.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            data = msg.getBytes();
        }
    }

    @Column
    protected byte[] data;

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
