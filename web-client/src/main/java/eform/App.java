/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package eform;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/eform")
final public class App extends Application {

  public Set<Class<?>> getClasses() {
    Set<Class<?>> set = new HashSet<Class<?>>();
    set.add(org.codehaus.jackson.jaxrs.JacksonJsonProvider.class);
    set.add(Api.class);
    return set;
  }
}
