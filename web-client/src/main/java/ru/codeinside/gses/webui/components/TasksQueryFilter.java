/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.webui.components;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public interface TasksQueryFilter {

  void setFromDate(Date fromDate);

  void setToDate(Date toDate);

  void setRequester(String requester);

  void setBidId(String bidId);

  void setProcedureType(String procedureType);

  void setServiceId(String serviceId);

  void setProcedureId(String procedureId);

  void setTaskKey(String taskKey);

  void setDeclarantTypeName(String declarantTypeName);

  void setDeclarantTypeValue(String declarantTypeValue);

  void setControlledOrgGroups(List groups);

  void setControlledEmpGroups(List groups);

  void setOverdue(boolean value);
}
