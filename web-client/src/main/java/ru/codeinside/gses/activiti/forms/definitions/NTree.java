/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.activiti.forms.definitions;

import com.google.common.collect.ImmutableMap;
import ru.codeinside.gses.activiti.forms.api.definitions.PropertyNode;
import ru.codeinside.gses.activiti.forms.api.definitions.PropertyTree;
import ru.codeinside.gses.activiti.forms.api.duration.DurationPreference;

import java.util.Arrays;
import java.util.Map;

final class NTree implements PropertyTree {

  final PropertyNode[] nodes;
  final ImmutableMap<String, PropertyNode> index;
  final DurationPreference durationPreference;
  final String formKey;
  private final boolean signatureRequired;
  private final boolean isDataFlow;
  private final String consumerName;
  private final ImmutableMap<String, Boolean> dataFlowParameters;
  boolean isResultDataFlow;
  private final String requestType;
  private final String responseMessage;
  Map<String, Boolean> resultDataFlowParameters;

  public NTree(PropertyNode[] nodes,
               Map<String, PropertyNode> index,
               DurationPreference durationPreference,
               String formKey,
               boolean signatureRequired,
               boolean isDataFlow,
               String consumerName,
               Map<String, Boolean> dataFlowParameters,
               boolean isResultDataFlow,
               String requestType,
               String responseMessage,
               Map<String, Boolean> resultDataFlowParameters
               ) {
    this.nodes = nodes;
    this.signatureRequired = signatureRequired;
    this.isDataFlow = isDataFlow;
    this.consumerName = consumerName;
    this.dataFlowParameters = ImmutableMap.copyOf(dataFlowParameters);
    this.index = ImmutableMap.copyOf(index);
    this.durationPreference = durationPreference;
    this.formKey = formKey;
    this.isResultDataFlow = isResultDataFlow;
    this.requestType = requestType;
    this.responseMessage = responseMessage;
    this.resultDataFlowParameters = resultDataFlowParameters;
  }

  @Override
  public PropertyNode[] getNodes() {
    return nodes;
  }

  @Override
  public ImmutableMap<String, PropertyNode> getIndex() {
    return index;
  }

  @Override
  public DurationPreference getDurationPreference() {
    return durationPreference;
  }

  @Override
  public String getFormKey() {
    return formKey;
  }

  @Override
  public boolean isSignatureRequired() {
    return signatureRequired;
  }

  @Override
  public boolean isDataFlow() {
    return isDataFlow;
  }

  @Override
  public String getConsumerName() {
    return consumerName;
  }

  @Override
  public boolean needSp() {
    if (dataFlowParameters != null && dataFlowParameters.containsKey("needSp")) {
      return dataFlowParameters.get("needSp");
    }
    return false;
  }

  @Override
  public boolean needOv() {
    if (dataFlowParameters != null && dataFlowParameters.containsKey("needOv")) {
      return dataFlowParameters.get("needOv");
    }
    return false;
  }

  @Override
  public boolean needTep() {
    if (dataFlowParameters != null && dataFlowParameters.containsKey("needTep")) {
      return dataFlowParameters.get("needTep");
    }
    return false;
  }

  @Override
  public boolean needSend() {
    if (dataFlowParameters != null && dataFlowParameters.containsKey("needSend")) {
      return dataFlowParameters.get("needSend");
    }
    return false;
  }

  @Override
  public boolean isLazyWriter() {
    if (dataFlowParameters != null && dataFlowParameters.containsKey("isLazyWriter")) {
      return dataFlowParameters.get("isLazyWriter");
    }
    return false;
  }

  @Override
  public boolean isAppDataSignatureBlockLast() {
    if (dataFlowParameters != null && dataFlowParameters.containsKey("isAppDataSignatureBlockLast")) {
      return dataFlowParameters.get("isAppDataSignatureBlockLast");
    }
    return false;
  }

  @Override
  public boolean isResultDataFlow() {
    return isResultDataFlow;
  }

  @Override
  public String getRequestType() {
    return requestType;
  }

  @Override
  public String getResponseMessage() {
    return responseMessage;
  }

  @Override
  public boolean resultNeedSp() {
    if (resultDataFlowParameters != null && resultDataFlowParameters.containsKey("needSp")) {
      return resultDataFlowParameters.get("needSp");
    }
    return false;
  }

  @Override
  public boolean resultNeedOv() {
    if (resultDataFlowParameters != null && resultDataFlowParameters.containsKey("needOv")) {
      return resultDataFlowParameters.get("needOv");
    }
    return false;
  }


  @Override
  public String toString() {
    return "{nodes=" + Arrays.toString(nodes).replaceAll("\\},", "},\n").replaceAll("\\[\\{", "\n[{") +
      ", durationPreference=" + durationPreference +
      ", formKey='" + formKey + '\'' +
      '}';
  }
}
