/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.mail;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Читает настройки SMTP из контекста контейнера
 */
public class SmtpConfigReader {
	final static Logger LOG = Logger.getLogger(SmtpConfigReader.class.getName());
	public static final String JNDI_SMTP_HOST = "oep_smtp_host";
	private static final String JNDI_SMTP_USER = "oep_smtp_user";
	private static final String JNDI_SMTP_USER_PASSWORD = "oep_smtp_user_password";
	private static final String JNDI_SMTP_USE_TLS = "oep_smtp_use_tls";
	private static final String JNDI_SMTP_PORT = "oep_smtp_port";
	private static final String JNDI_SMTP_DEFAULT_FROM = "oep_smtp_default_from";

	public static SmtpConfig readSmtpConnectionParams() {
		SmtpConfig smtpConfig = new SmtpConfig();
		smtpConfig.setHost(readString(JNDI_SMTP_HOST));
		smtpConfig.setUserName(readString(JNDI_SMTP_USER));
		smtpConfig.setPassword(readString(JNDI_SMTP_USER_PASSWORD));
		smtpConfig.setDefaultFrom(readString(JNDI_SMTP_DEFAULT_FROM));
		smtpConfig.setUseTLS(readBoolean(JNDI_SMTP_USE_TLS));
		smtpConfig.setPort(readInteger(JNDI_SMTP_PORT));
		return smtpConfig;
	}

	private static Integer readInteger(String jndiName) {
		Integer result = null;
		try {
			result = InitialContext.doLookup(jndiName);
		} catch (NamingException e) {
			LOG.log(Level.WARNING, e.getMessage());//, e);
		}
		return result;
	}

	private static Boolean readBoolean(String jndiName) {
		Boolean result = null;
		try {
			result = InitialContext.doLookup(jndiName);
		} catch (NamingException e) {
			LOG.log(Level.WARNING, e.getMessage());//, e);
		}
		return result;
	}

	private static String readString(String jndiName) {
		String value = null;
		try {
			value = InitialContext.doLookup(jndiName);
		} catch (NamingException e) {
			LOG.log(Level.WARNING, e.getMessage());//, e);
		}
		return value;
	}
}
