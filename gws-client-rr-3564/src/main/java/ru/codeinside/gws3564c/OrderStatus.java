/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws3564c;

public enum OrderStatus {

    OK("000", "Создана", "Заявка зарегистрирована в ИС Росреестра"),

    VERIFICATION("001", "На проверке",
            "Осуществляется проверка ФЛК и проверка ЭЦП"),

    VERIFICATION_FAIL("0010", "Проверка не пройдена",
            "ФЛК, либо проверка ЭЦП не пройдена"),

    PAYMENT_PENDING("002", "Ожидает оплату",
            "Не используется при межведомственном взаимодействии"),

    PAYMENT("0021", "Обработка платежа",
            "Не используется при межведомственном взаимодействии"),

    PROCESSING("003", "В работе",
            "Заявка прошла проверку и находится на исполнении."),

    DONE(
            "004",
            "Завершена",
            "Заявление завершено с положительным результатом. Одновременно с данным статусом возвращается результат выполнения заявления."),

    DENIED(
            "0040",
            "Завершена отказом",
            "Принято решение об отказе в предоставлении сведений/проведении учетных действий."),

    NOT_FOUND("0041", "Сведения отсутствуют",
            "Учетная Система не нашла объектов  по указанным в запросе параметрам."),

    REJECTED(
            "0043",
            "Отказ в обработке",
            "Учетная Система отказала в обработке заявления (например, не все необходимые документы приложены).");

    final public String code;
    final public String message;
    final public String reason;

    private OrderStatus(String code, String message, String reason) {
        this.code = code;
        this.message = message;
        this.reason = reason;
    }

    public static OrderStatus findByCode(final String code) {
        for (OrderStatus item : values()) {
            if (item.code.equals(code)) {
                return item;
            }
        }
        return null;
    }
}
