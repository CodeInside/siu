/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2014, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.database;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(InfoSystemService.class)
public class InfoSystemService_ {

  public static volatile SingularAttribute<InfoSystemService, String> id;
  public static volatile SingularAttribute<InfoSystemService, String> address;
  public static volatile SingularAttribute<InfoSystemService, String> revision;
  public static volatile SingularAttribute<InfoSystemService, String> sname;
  public static volatile SingularAttribute<InfoSystemService, String> sversion;
  public static volatile SingularAttribute<InfoSystemService, String> name;
  public static volatile SingularAttribute<InfoSystemService, Boolean> available;
  public static volatile SingularAttribute<InfoSystemService, Boolean> logEnabled;
  public static volatile SingularAttribute<InfoSystemService, InfoSystem> infoSystem;
  public static volatile SingularAttribute<InfoSystemService, InfoSystem> source;
}
