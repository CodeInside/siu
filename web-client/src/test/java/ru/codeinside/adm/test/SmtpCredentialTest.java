/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.test;

import junit.framework.Assert;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;
import ru.codeinside.gses.activiti.mail.SmtpConfig;
import ru.codeinside.gses.activiti.mail.SmtpConfigReader;

/**
 * Можно запустить вручную, например:
 * <pre></pre>mvn test -Dtest=SmtpCredentialTest -Darquillian.launch=xeodon</pre>
 */
@RunWith(Arquillian.class)
public class SmtpCredentialTest {

  @Test
  /**
   * Тестируем чтение настроек для доступа к smtp через jndi контейнера
   */
  public void testReadSmtpCredentials() throws Exception {
    SmtpConfig credential = SmtpConfigReader.readSmtpConnectionParams();
    Assert.assertEquals("smtp.mail.ru", credential.getHost());
    Assert.assertEquals("smtp.user.name", credential.getUserName());
    Assert.assertEquals("smtp.password", credential.getPassword());
    Assert.assertEquals("smtp.default.from", credential.getDefaultFrom());
    Assert.assertEquals(Boolean.TRUE, credential.getUseTLS());
    Assert.assertEquals(1001, credential.getPort().intValue());

    Assert.assertNotNull(credential);
  }
}
