/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.test;

import java.util.Set;

import javax.annotation.Resource;
import javax.inject.Singleton;

import org.glassfish.embeddable.CommandResult;
import org.glassfish.embeddable.CommandRunner;

import com.google.common.base.Joiner;
import com.sun.appserv.security.ProgrammaticLogin;

@Singleton
public class GlassfihSecurity {

	@Resource(mappedName = "org.glassfish.embeddable.CommandRunner")
	private CommandRunner commandRunner;

	public void create(String user, Set<String> groups) {
		final CommandResult result = commandRunner.run("create-file-user", "--passwordfile",
		        "src/test/resources/glassfish-password.txt", "--groups", Joiner.on(':').join(groups), user);
		if (result.getFailureCause() != null) {
			throw new RuntimeException(result.getFailureCause());
		}
	}

	public void login(final String user, final String password) throws Exception {
		System.setProperty("java.security.auth.login.config", "src/test/resources/glassfish-auth.conf");
		final ProgrammaticLogin login = new ProgrammaticLogin();
		login.login(user, password.toCharArray(), "fileRealm", true);
	}

}
