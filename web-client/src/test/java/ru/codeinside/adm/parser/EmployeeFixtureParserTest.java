/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.parser;


import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import ru.codeinside.adm.database.Role;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import static org.mockito.Matchers.anySet;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.isNotNull;
import static org.mockito.Mockito.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EmployeeFixtureParserTest {

    private EmployeeFixtureParser.PersistenceCallback callback;
    private EmployeeFixtureParser parser;

    @Before
    public void setUp() throws Exception {
        callback = mock(EmployeeFixtureParser.PersistenceCallback.class);
        parser = new EmployeeFixtureParser();
    }

    /**
     * Должны распознать одну организацию без пользователей
     */
    @Test
    public void testParseFixtureSingleOrg() throws IOException {
        parser.loadFixtures(getInputStream("/fixtures/single_org.txt"), callback);
        // ни одного пользователя не должны найти
        verify(callback, never()).onUserComplete(anyString(), anyString(), anyString(), anyString(), anyLong(), anySet(), anySet());
        // должны найти только одну организацию
        verify(callback, times(1)).onOrganizationComplete(anyString(), anySet(), (Long) isNull());
        verify(callback, times(1)).onOrganizationComplete(eq("Оператор Электронного Правительства"), anySet(), (Long) isNull());
    }

    /**
     * Должны распознать иерархию организаций с тремя уровнями
     *
     * @throws IOException
     */
    @Test
    public void testParseMultiLevelOrg() throws IOException {
        long MFC_ORG_ID = 1L;
        when(callback.onOrganizationComplete(eq("Многофункциональные центры"), anySet(), (Long) isNull())).thenReturn(MFC_ORG_ID);
        Long MFC_PENZA_ORG_ID = 2L;
        when(callback.onOrganizationComplete(eq("МФЦ г.Пенза"), anySet(), eq(MFC_ORG_ID))).thenReturn(MFC_PENZA_ORG_ID);
        parser.loadFixtures(getInputStream("/fixtures/twolevel_org.txt"), callback);
        // ни одного пользователя не должны найти
        verify(callback, never()).onUserComplete(anyString(), anyString(), anyString(), anyString(), anyLong(), anySet(), anySet());
        // должны найти только одну организацию
        verify(callback, times(2)).onOrganizationComplete(anyString(), anySet(), (Long) isNull());
        verify(callback, times(2)).onOrganizationComplete(anyString(), anySet(), (Long) isNotNull());
        verify(callback, times(1)).onOrganizationComplete(eq("Оператор Электронного Правительства"), anySet(), (Long) isNull());
        verify(callback, times(1)).onOrganizationComplete(eq("Многофункциональные центры"), anySet(), (Long) isNull());
        verify(callback, times(1)).onOrganizationComplete(eq("МФЦ г.Пенза"), anySet(), eq(MFC_ORG_ID));
        verify(callback, times(1)).onOrganizationComplete(eq("МФЦ г.Заречный"), anySet(), eq(MFC_PENZA_ORG_ID));
    }

    /**
     * Должны распознать организацию у которой указана одна группа
     */
    @Test
    public void testParseSingleOrganizationWithSingleGroup() throws IOException {
        parser.loadFixtures(getInputStream("/fixtures/org_single_group.txt"), callback);
        verify(callback, times(1)).onOrganizationComplete(anyString(), anySet(), (Long) isNull());
        verify(callback, times(1)).onOrganizationComplete(eq("МФЦ г.Заречный"), anySet(), (Long) isNull());

        ArgumentCaptor<Set> argumentCaptor = ArgumentCaptor.forClass(Set.class);
        verify(callback).onOrganizationComplete(eq("МФЦ г.Заречный"), argumentCaptor.capture(), (Long) isNull());
        Assert.assertNotNull(argumentCaptor.getValue());
        Assert.assertEquals(1, argumentCaptor.getValue().size());
        Assert.assertTrue(argumentCaptor.getValue().contains("mfcZarGor_executors"));
    }

    /**
     * Дожный распознать организацию у которой указано несколько групп
     */
    @Test
    public void testParseSingleOrganizationWithSeveralGroup() throws IOException {
        parser.loadFixtures(getInputStream("/fixtures/org_multiple_group.txt"), callback);
        verify(callback, times(1)).onOrganizationComplete(anyString(), anySet(), (Long) isNull());
        verify(callback, times(1)).onOrganizationComplete(eq("МФЦ г.Заречный"), anySet(), (Long) isNull());

        ArgumentCaptor<Set> argumentCaptor = ArgumentCaptor.forClass(Set.class);
        verify(callback).onOrganizationComplete(eq("МФЦ г.Заречный"), argumentCaptor.capture(), (Long) isNull());
        Assert.assertNotNull(argumentCaptor.getValue());
        Assert.assertEquals(2, argumentCaptor.getValue().size());
        Assert.assertTrue(argumentCaptor.getValue().contains("mfcZarGor_executors"));
        Assert.assertTrue(argumentCaptor.getValue().contains("testGroup"));
    }

    /**
     * Пользователя без организации быть не может
     */
    @Test(expected = IllegalStateException.class)
    public void testUserWithoutOrg() throws IOException {
        parser.loadFixtures(getInputStream("/fixtures/wrong_user.txt"), callback);
    }

    /**
     * У организации на втором уровне есть 1 пользователь с установленными ролями и группой
     */
    @Test
    public void testParseUser() throws IOException {
        long MFC_ORG_ID = 1L;
        when(callback.onOrganizationComplete(eq("Многофункциональные центры"), anySet(), (Long) isNull())).thenReturn(MFC_ORG_ID);
        Long MFC_PENZA_ORG_ID = 2L;
        when(callback.onOrganizationComplete(eq("МФЦ г.Пенза"), anySet(), eq(MFC_ORG_ID))).thenReturn(MFC_PENZA_ORG_ID);
        Long MFC_ZAR_ORG_ID = 3L;
        when(callback.onOrganizationComplete(eq("МФЦ г.Заречный"), anySet(), eq(MFC_PENZA_ORG_ID))).thenReturn(MFC_ZAR_ORG_ID);
        parser.loadFixtures(getInputStream("/fixtures/users.txt"), callback);

        // должны найти только одну организацию
        verify(callback, times(2)).onOrganizationComplete(anyString(), anySet(), (Long) isNull());
        verify(callback, times(2)).onOrganizationComplete(anyString(), anySet(), (Long) isNotNull());
        verify(callback, times(1)).onOrganizationComplete(eq("Оператор Электронного Правительства"), anySet(), (Long) isNull());
        verify(callback, times(1)).onOrganizationComplete(eq("Многофункциональные центры"), anySet(), (Long) isNull());
        verify(callback, times(1)).onOrganizationComplete(eq("МФЦ г.Пенза"), anySet(), eq(MFC_ORG_ID));
        verify(callback, times(1)).onOrganizationComplete(eq("МФЦ г.Заречный"), anySet(), eq(MFC_PENZA_ORG_ID));

        // должны найти 2 пользователя
        verify(callback, times(2)).onUserComplete(anyString(), anyString(), anyString(), anyString(), anyLong(), anySet(), anySet());
        verify(callback, times(1)).onUserComplete(eq("s.shuvalova"), eq("testpassword"), eq("Шувалова Светлана Николаевна"), eq(""), eq(MFC_PENZA_ORG_ID), anySet(), anySet());
        verify(callback, times(1)).onUserComplete(eq("a.myanzelina"), eq("testpassword"), eq("Мянзелина Алсу Нязыфовна"), eq(""), eq(MFC_ZAR_ORG_ID), anySet(), anySet());

        // проверяем разбор ролей и групп
        ArgumentCaptor<Set> rolesCaptor = ArgumentCaptor.forClass(Set.class);
        ArgumentCaptor<Set> groupCaptor = ArgumentCaptor.forClass(Set.class);
        verify(callback).onUserComplete(eq("s.shuvalova"), eq("testpassword"), eq("Шувалова Светлана Николаевна"), eq(""), eq(MFC_PENZA_ORG_ID), rolesCaptor.capture(), groupCaptor.capture());
        Assert.assertNotNull(rolesCaptor.getValue());
        Assert.assertEquals(2, rolesCaptor.getValue().size());
        Assert.assertTrue(rolesCaptor.getValue().contains(Role.Declarant));
        Assert.assertTrue(rolesCaptor.getValue().contains(Role.Executor));

        Assert.assertNotNull(groupCaptor.getValue());
        Assert.assertEquals(2, groupCaptor.getValue().size());
        Assert.assertTrue(groupCaptor.getValue().contains("mfcNevRai_request_MV00002"));
        Assert.assertTrue(groupCaptor.getValue().contains("testGroup"));

        verify(callback).onUserComplete(eq("a.myanzelina"), eq("testpassword"), eq("Мянзелина Алсу Нязыфовна"), eq(""), eq(MFC_ZAR_ORG_ID), rolesCaptor.capture(), groupCaptor.capture());
        Assert.assertNotNull(rolesCaptor.getValue());
        Assert.assertEquals(1, rolesCaptor.getValue().size());
        Assert.assertTrue(rolesCaptor.getValue().contains(Role.Declarant));

        Assert.assertNotNull(groupCaptor.getValue());
        Assert.assertEquals(1, groupCaptor.getValue().size());
        Assert.assertTrue(groupCaptor.getValue().contains("mfcNevRai_request_MV00002"));
    }
    /**
     * Тестируем разбор пользователя с пустыми группами
     */
    @Test
    public void testParseEmptyFields() throws IOException {
        long MFC_ORG_ID = 1L;
        when(callback.onOrganizationComplete(eq("Многофункциональные центры"), anySet(), (Long) isNull())).thenReturn(MFC_ORG_ID);
        Long MFC_PENZA_ORG_ID = 2L;
        when(callback.onOrganizationComplete(eq("МФЦ г.Пенза"), anySet(), eq(MFC_ORG_ID))).thenReturn(MFC_PENZA_ORG_ID);
        Long MFC_ZAR_ORG_ID = 3L;
        when(callback.onOrganizationComplete(eq("МФЦ г.Заречный"), anySet(), eq(MFC_PENZA_ORG_ID))).thenReturn(MFC_ZAR_ORG_ID);
        parser.loadFixtures(getInputStream("/fixtures/user_empty_field.txt"), callback);

        // должны найти только одну организацию
        verify(callback, times(2)).onOrganizationComplete(anyString(), anySet(), (Long) isNull());
        verify(callback, times(2)).onOrganizationComplete(anyString(), anySet(), (Long) isNotNull());
        verify(callback, times(1)).onOrganizationComplete(eq("Оператор Электронного Правительства"), anySet(), (Long) isNull());
        verify(callback, times(1)).onOrganizationComplete(eq("Многофункциональные центры"), anySet(), (Long) isNull());
        verify(callback, times(1)).onOrganizationComplete(eq("МФЦ г.Пенза"), anySet(), eq(MFC_ORG_ID));
        verify(callback, times(1)).onOrganizationComplete(eq("МФЦ г.Заречный"), anySet(), eq(MFC_PENZA_ORG_ID));

        // должны найти 1 пользователь
        verify(callback, times(1)).onUserComplete(anyString(), anyString(), anyString(), anyString(), anyLong(), anySet(), anySet());
        verify(callback, times(1)).onUserComplete(eq("s.shuvalova"), (String)isNull(), eq("Шувалова Светлана Николаевна"), eq(""), eq(MFC_PENZA_ORG_ID), anySet(), anySet());


        // проверяем разбор ролей и групп
        ArgumentCaptor<Set> rolesCaptor = ArgumentCaptor.forClass(Set.class);
        ArgumentCaptor<Set> groupCaptor = ArgumentCaptor.forClass(Set.class);
        verify(callback).onUserComplete(eq("s.shuvalova"), (String)isNull(), eq("Шувалова Светлана Николаевна"), eq(""), eq(MFC_PENZA_ORG_ID), rolesCaptor.capture(), groupCaptor.capture());
        Assert.assertNotNull(rolesCaptor.getValue());
        Assert.assertEquals(0, rolesCaptor.getValue().size());

        Assert.assertNotNull(groupCaptor.getValue());
        Assert.assertEquals(0, groupCaptor.getValue().size());
    }

    /**
     * Берет поток из ресурсов
     *
     * @param resourcePath путь до ресурса
     * @return поток для чтения
     */

    private InputStream getInputStream(String resourcePath) {
        return this.getClass().getResourceAsStream(resourcePath);
    }
}
