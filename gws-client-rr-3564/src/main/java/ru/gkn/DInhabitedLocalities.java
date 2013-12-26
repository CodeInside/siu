/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */


package ru.gkn;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


@XmlType(name = "dInhabitedLocalities")
@XmlEnum
public enum DInhabitedLocalities {


    /**
     * Аал
     * 
     */
    @XmlEnumValue("\u0430\u0430\u043b")
    ААЛ("\u0430\u0430\u043b"),

    /**
     * Аул
     * 
     */
    @XmlEnumValue("\u0430\u0443\u043b")
    АУЛ("\u0430\u0443\u043b"),

    /**
     * Волость
     * 
     */
    @XmlEnumValue("\u0432\u043e\u043b\u043e\u0441\u0442\u044c")
    ВОЛОСТЬ("\u0432\u043e\u043b\u043e\u0441\u0442\u044c"),

    /**
     * Выселки(ок)
     * 
     */
    @XmlEnumValue("\u0432\u044b\u0441\u0435\u043b")
    ВЫСЕЛ("\u0432\u044b\u0441\u0435\u043b"),

    /**
     * Город
     * 
     */
    @XmlEnumValue("\u0433")
    Г("\u0433"),

    /**
     * Деревня
     * 
     */
    @XmlEnumValue("\u0434")
    Д("\u0434"),

    /**
     * Дачный поселок
     * 
     */
    @XmlEnumValue("\u0434\u043f")
    ДП("\u0434\u043f"),

    /**
     * Железнодорожная будка
     * 
     */
    @XmlEnumValue("\u0436/\u0434_\u0431\u0443\u0434\u043a\u0430")
    Ж_Д_БУДКА("\u0436/\u0434_\u0431\u0443\u0434\u043a\u0430"),

    /**
     * Железнодорожная казарма
     * 
     */
    @XmlEnumValue("\u0436/\u0434_\u043a\u0430\u0437\u0430\u0440\u043c")
    Ж_Д_КАЗАРМ("\u0436/\u0434_\u043a\u0430\u0437\u0430\u0440\u043c"),

    /**
     * ж/д остановочный (обгонный) пункт
     * 
     */
    @XmlEnumValue("\u0436/\u0434_\u043e\u043f")
    Ж_Д_ОП("\u0436/\u0434_\u043e\u043f"),

    /**
     * Железнодорожный пост
     * 
     */
    @XmlEnumValue("\u0436/\u0434_\u043f\u043e\u0441\u0442")
    Ж_Д_ПОСТ("\u0436/\u0434_\u043f\u043e\u0441\u0442"),

    /**
     * Железнодорожный разъезд
     * 
     */
    @XmlEnumValue("\u0436/\u0434_\u0440\u0437\u0434")
    Ж_Д_РЗД("\u0436/\u0434_\u0440\u0437\u0434"),

    /**
     * Железнодорожная станция
     * 
     */
    @XmlEnumValue("\u0436/\u0434_\u0441\u0442")
    Ж_Д_СТ("\u0436/\u0434_\u0441\u0442"),

    /**
     * Заимка
     * 
     */
    @XmlEnumValue("\u0437\u0430\u0438\u043c\u043a\u0430")
    ЗАИМКА("\u0437\u0430\u0438\u043c\u043a\u0430"),

    /**
     * Казарма
     * 
     */
    @XmlEnumValue("\u043a\u0430\u0437\u0430\u0440\u043c\u0430")
    КАЗАРМА("\u043a\u0430\u0437\u0430\u0440\u043c\u0430"),

    /**
     * Курортный поселок
     * 
     */
    @XmlEnumValue("\u043a\u043f")
    КП("\u043a\u043f"),

    /**
     * Местечко
     * 
     */
    @XmlEnumValue("\u043c")
    М("\u043c"),

    /**
     * Микрорайон
     * 
     */
    @XmlEnumValue("\u043c\u043a\u0440")
    МКР("\u043c\u043a\u0440"),

    /**
     * Населенный пункт
     * 
     */
    @XmlEnumValue("\u043d\u043f")
    НП("\u043d\u043f"),

    /**
     * Остров
     * 
     */
    @XmlEnumValue("\u043e\u0441\u0442\u0440\u043e\u0432")
    ОСТРОВ("\u043e\u0441\u0442\u0440\u043e\u0432"),

    /**
     * Поселок
     * 
     */
    @XmlEnumValue("\u043f")
    П("\u043f"),

    /**
     * Планировочный район
     * 
     */
    @XmlEnumValue("\u043f/\u0440")
    П_Р("\u043f/\u0440"),

    /**
     * Поселок и(при) станция(и)
     * 
     */
    @XmlEnumValue("\u043f/\u0441\u0442")
    П_СТ("\u043f/\u0441\u0442"),

    /**
     * Поселок городского типа
     * 
     */
    @XmlEnumValue("\u043f\u0433\u0442")
    ПГТ("\u043f\u0433\u0442"),

    /**
     * Починок
     * 
     */
    @XmlEnumValue("\u043f\u043e\u0447\u0438\u043d\u043e\u043a")
    ПОЧИНОК("\u043f\u043e\u0447\u0438\u043d\u043e\u043a"),

    /**
     * Почтовое отделение
     * 
     */
    @XmlEnumValue("\u043f/\u043e")
    П_О("\u043f/\u043e"),

    /**
     * Промышленная зона
     * 
     */
    @XmlEnumValue("\u043f\u0440\u043e\u043c\u0437\u043e\u043d\u0430")
    ПРОМЗОНА("\u043f\u0440\u043e\u043c\u0437\u043e\u043d\u0430"),

    /**
     * Разъезд
     * 
     */
    @XmlEnumValue("\u0440\u0437\u0434")
    РЗД("\u0440\u0437\u0434"),

    /**
     * Рабочий поселок
     * 
     */
    @XmlEnumValue("\u0440\u043f")
    РП("\u0440\u043f"),

    /**
     * Село
     * 
     */
    @XmlEnumValue("\u0441")
    С("\u0441"),

    /**
     * Слобода
     * 
     */
    @XmlEnumValue("\u0441\u043b")
    СЛ("\u0441\u043b"),

    /**
     * Станция
     * 
     */
    @XmlEnumValue("\u0441\u0442")
    СТ("\u0441\u0442"),

    /**
     * Станица
     * 
     */
    @XmlEnumValue("\u0441\u0442-\u0446\u0430")
    СТ_ЦА("\u0441\u0442-\u0446\u0430"),

    /**
     * Улус
     * 
     */
    @XmlEnumValue("\u0443")
    У("\u0443"),

    /**
     * Хутор
     * 
     */
    @XmlEnumValue("\u0445")
    Х("\u0445"),

    /**
     * Городок
     * 
     */
    @XmlEnumValue("\u0433\u043e\u0440\u043e\u0434\u043e\u043a")
    ГОРОДОК("\u0433\u043e\u0440\u043e\u0434\u043e\u043a"),

    /**
     * Территория
     * 
     */
    @XmlEnumValue("\u0442\u0435\u0440")
    ТЕР("\u0442\u0435\u0440"),

    /**
     * Железнодорожная платформа
     * 
     */
    @XmlEnumValue("\u0436/\u0434_\u043f\u043b\u0430\u0442\u0444")
    Ж_Д_ПЛАТФ("\u0436/\u0434_\u043f\u043b\u0430\u0442\u0444"),

    /**
     * Квартал
     * 
     */
    @XmlEnumValue("\u043a\u0432-\u043b")
    КВ_Л("\u043a\u0432-\u043b"),

    /**
     * Арбан
     * 
     */
    @XmlEnumValue("\u0430\u0440\u0431\u0430\u043d")
    АРБАН("\u0430\u0440\u0431\u0430\u043d"),

    /**
     * Садовое некоммерческое товарищество
     * 
     */
    @XmlEnumValue("\u0441\u043d\u0442")
    СНТ("\u0441\u043d\u0442"),

    /**
     * Леспромхоз
     * 
     */
    @XmlEnumValue("\u043b\u043f\u0445")
    ЛПХ("\u043b\u043f\u0445"),

    /**
     * Погост
     * 
     */
    @XmlEnumValue("\u043f\u043e\u0433\u043e\u0441\u0442")
    ПОГОСТ("\u043f\u043e\u0433\u043e\u0441\u0442"),

    /**
     * Кордон
     * 
     */
    @XmlEnumValue("\u043a\u043e\u0440\u0434\u043e\u043d")
    КОРДОН("\u043a\u043e\u0440\u0434\u043e\u043d"),

    /**
     * Автодорога
     * 
     */
    @XmlEnumValue("\u0430\u0432\u0442\u043e\u0434\u043e\u0440\u043e\u0433\u0430")
    АВТОДОРОГА("\u0430\u0432\u0442\u043e\u0434\u043e\u0440\u043e\u0433\u0430"),

    /**
     * Жилой район
     * 
     */
    @XmlEnumValue("\u0436\u0438\u043b\u0440\u0430\u0439\u043e\u043d")
    ЖИЛРАЙОН("\u0436\u0438\u043b\u0440\u0430\u0439\u043e\u043d"),

    /**
     * Жилая зона
     * 
     */
    @XmlEnumValue("\u0436\u0438\u043b\u0437\u043e\u043d\u0430")
    ЖИЛЗОНА("\u0436\u0438\u043b\u0437\u043e\u043d\u0430"),

    /**
     * Массив
     * 
     */
    @XmlEnumValue("\u043c\u0430\u0441\u0441\u0438\u0432")
    МАССИВ("\u043c\u0430\u0441\u0441\u0438\u0432");
    private final String value;

    DInhabitedLocalities(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static DInhabitedLocalities fromValue(String v) {
        for (DInhabitedLocalities c: DInhabitedLocalities.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
