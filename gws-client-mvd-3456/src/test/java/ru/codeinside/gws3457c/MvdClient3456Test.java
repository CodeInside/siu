/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws3457c;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


import org.junit.Test;
import ru.codeinside.gws.api.Client;
import ru.codeinside.gws.api.ClientRequest;
import ru.codeinside.gws3456c.MvdClient3456;

import static junit.framework.Assert.assertNotNull;

/**
 * Тестируем взаимодействия с сервисом МВД (услуга 3456)
 */
public class MvdClient3456Test {
    @Test
    public void testCreateClientRequest() throws Exception {
        DummyContext ctx = new DummyContext();
        Client client = new MvdClient3456();

        ctx.setVariable("smevTest", "Первичный запрос");

        ctx.setVariable("SecName", "Иванов");
        ctx.setVariable("FirstName", "Иван");
        ctx.setVariable("FathersName", "Иванович");
        ctx.setVariable("SNILS", "00000000000");
        ctx.setVariable("DateOfBirth", parseDate("07.10.1917", "dd.MM.yyyy"));

        ctx.setVariable("Region", "058"); // брать из справочника
        ctx.setVariable("RegistrationPlace", "г. Пенза ул. Попова 32 кв 1");
        ctx.setVariable("TypeRegistration", "MЖ"); //для места пребывания – МП,  для места проживания - МЖ

        /**
         * conviction_doc – в случае предоставления сведений о наличии судимости
         * conviction_info – в случае предоставления сведений о непогашенной судимости
         * investigation – в случае предоставления сведений о нахождении в розыске
         */
        ctx.setVariable("MsgVid", "conviction_doc");  // в зависимости от типа запроса нужно выставлять разные параметры

        ctx.setVariable("OriginatorFio", "Ковалевская И.А., тел. (351) 232-3456");
        ctx.setVariable("OriginatorName", "Комплексная система предоставления государственных и муниципальных услуг Пензенской области");
        ctx.setVariable("OriginatorRegion", "058");
        ctx.setVariable("OriginatorCode", "PNZR01581");

        ctx.setVariable("PlaceOfBirth", "Пенза");
        ctx.setVariable("BirthRegionCode", "058"); // брать из справочникаПри месте рождения вне перечня регионов РФ – должно принимать значение "077"
        ctx.setVariable("Reason", "Тестирование системы") ; // правовые основания предоставления услуги

        ClientRequest request = client.createClientRequest(ctx);
        assertNotNull(request);
    }

	private Date parseDate(String stringDate, String format) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat(format);
		return df.parse(stringDate);
	}
}
