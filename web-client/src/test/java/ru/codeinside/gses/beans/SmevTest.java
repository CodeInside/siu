/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gses.beans;

import org.activiti.engine.delegate.BpmnError;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.db.DbSqlSession;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.CommentManager;
import org.activiti.engine.impl.variable.EntityManagerSession;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ru.codeinside.adm.AdminService;
import ru.codeinside.adm.database.Bid;
import ru.codeinside.adm.database.ClientRequestEntity;
import ru.codeinside.adm.database.ExternalGlue;
import ru.codeinside.adm.database.InfoSystem;
import ru.codeinside.adm.database.InfoSystemService;
import ru.codeinside.gses.webui.gws.ClientRefRegistry;
import ru.codeinside.gses.webui.gws.ServiceRefRegistry;
import ru.codeinside.gses.webui.gws.TRef;
import ru.codeinside.gws.api.Client;
import ru.codeinside.gws.api.ClientRequest;
import ru.codeinside.gws.api.Enclosure;
import ru.codeinside.gws.api.ExchangeContext;
import ru.codeinside.gws.api.Packet;
import ru.codeinside.gws.api.Server;
import ru.codeinside.gws.api.Signature;

import javax.persistence.EntityManager;
import java.util.HashSet;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SmevTest {

  private EntityManagerSession entityManagerSession;
  private DelegateExecution execution;
  private AdminService adminService;
  private TRef<Server> tRefServer;
  private Client client;
  private CommandContext commandContext;
  private Smev smev;
  private EntityManager entityManager;

  final static String SERVICE_NAME = "SERVICE_NAME";

  @Test
  public void test() {

    DelegateExecution delegateExecution = mock(DelegateExecution.class);
    when(delegateExecution.getProcessInstanceId()).thenReturn("321");
    when(delegateExecution.getVariableNames()).thenReturn(new HashSet<String>());

    Bid bid = new Bid();
    bid.setId(13L);
    ExternalGlue glue = new ExternalGlue();
    glue.setId(13L);
    glue.setName("xyz");
    bid.setGlue(glue);
    AdminService adminService = mock(AdminService.class);
    when(adminService.getBidByProcessInstanceId("321")).thenReturn(bid);
    when(adminService.countOfServerResponseByBidIdAndStatus(13L, "RESULT")).thenReturn(0);

    Server server = mock(Server.class);

    TRef<Server> tRef = mock(TRef.class);
    when(tRef.getRef()).thenReturn(server);

    ServiceRefRegistry serviceRegistry = mock(ServiceRefRegistry.class);
    when(serviceRegistry.getServerByName("xyz")).thenReturn(tRef);

    try {
      Smev smev = new Smev();
      smev.adminService = adminService;
      smev.serviceRegistry = serviceRegistry;
      smev.completeReceipt(delegateExecution, null);

      fail();
    } catch (BpmnError e) {
      assertEquals("suddenly_bpmn_error", e.getErrorCode());
      assertEquals("Поставщик xyz при вызове метода processResult вернул null (errorCode='suddenly_bpmn_error')",
        e.getMessage());
    }
  }

  @Test
  public void managed_call() {
    Smev smev = new Smev();
    smev.adminService = mock(AdminService.class);
    smev.serviceRegistry = mock(ServiceRefRegistry.class);
    DelegateExecution execution = mock(DelegateExecution.class);
    try {
      smev.managedCall(execution, "xyz");
      fail();
    } catch (BpmnError bpmnError) {
      assertEquals("client_bpmn_error", bpmnError.getErrorCode());
      ArgumentCaptor<String> arg1 = ArgumentCaptor.forClass(String.class);
      ArgumentCaptor<String> arg2 = ArgumentCaptor.forClass(String.class);
      verify(execution).setVariable(arg1.capture(), arg2.capture());
      assertEquals("call_error", arg1.getValue());
      assertTrue(arg2.getValue(), arg2.getValue().startsWith("xyz managedCall error\njava.lang.IllegalStateException: Нет сервиса с именем xyz"));
    }
  }

  @Before
  public void prepareEnvironment() {
    entityManagerSession = mock(EntityManagerSession.class);
    execution = mock(DelegateExecution.class);
    adminService = mock(AdminService.class);
    Server server = mock(Server.class);
    tRefServer = mock(TRef.class);
    when(tRefServer.getRef()).thenReturn(server);

    client = mock(Client.class);
    ServiceRefRegistry serviceRegistry = mock(ServiceRefRegistry.class);
    when(serviceRegistry.getServerByName(null)).thenReturn(tRefServer);
    ClientRefRegistry clientRegistry = mock(ClientRefRegistry.class);
    TRef<Client> clientRef = mock(TRef.class);
    when(clientRegistry.getClientByNameAndVersion(anyString(), anyString())).thenReturn(clientRef);
    when(clientRef.getRef()).thenReturn(client);

    commandContext = mock(CommandContext.class);
    Context.setCommandContext(commandContext);
    entityManager = mock(EntityManager.class);
    when(entityManagerSession.getEntityManager()).thenReturn(entityManager);
    when(commandContext.getDbSqlSession()).thenReturn(mock(DbSqlSession.class));
    when(commandContext.getCommentManager()).thenReturn(mock(CommentManager.class));
    when(commandContext.getSession(any(Class.class))).thenReturn(entityManagerSession);
    when(adminService.getGlueByProcessInstanceId(null)).thenReturn(new ExternalGlue());
    when(adminService.getInfoSystemServiceBySName(eq(SERVICE_NAME))).thenReturn(createInfoSystemService(SERVICE_NAME));
    when(execution.getId()).thenReturn("1");

    smev = new Smev();
    smev.adminService = adminService;
    smev.serviceRegistry = serviceRegistry;
    smev.registry = clientRegistry;
  }

  @Test
  public void testAddOneEnclosureToContext() {
    final String variableName = "variableName";
    when(execution.hasVariable(variableName)).thenReturn(false);
    when(client.createClientRequest(any(ExchangeContext.class))).thenReturn(createClientRequest(1, false));

    smev.prepare(execution, SERVICE_NAME, variableName);

    verify(entityManager, times(1)).persist(any(ClientRequestEntity.class));
    verify(execution, times(1)).setVariable(eq(variableName), any(ClientRequestEntity.class));
    // enclosure должно помещаться в соответствующий attach
    verify(execution, times(1)).setVariable(eq(variableName + "_enclosure_to_sign_0"), Mockito.notNull());
    // 1 на конце это ид execution
    verify(execution, times(1)).setVariable(eq(variableName + "_enclosure_to_sign_vars"), eq(variableName + "_enclosure_to_sign_0"));
  }

  @Test
  public void testAddSeveralEnclosureToContext() {
    final String variableName = "variableName";
    when(execution.hasVariable(variableName)).thenReturn(false);
    when(client.createClientRequest(any(ExchangeContext.class))).thenReturn(createClientRequest(2, false));

    smev.prepare(execution, SERVICE_NAME, variableName);

    verify(entityManager, times(1)).persist(any(ClientRequestEntity.class));
    verify(execution, times(1)).setVariable(eq(variableName), any(ClientRequestEntity.class));
    // enclosure должно помещаться в соответствующий attach
    verify(execution, times(1)).setVariable(eq(variableName + "_enclosure_to_sign_0"), Mockito.notNull());
    verify(execution, times(1)).setVariable(eq(variableName + "_enclosure_to_sign_1"), Mockito.notNull());
    // 1 на конце это ид execution
    verify(execution, times(1)).setVariable(eq(variableName + "_enclosure_to_sign_vars"), eq(variableName + "_enclosure_to_sign_0;" + variableName + "_enclosure_to_sign_1"));
  }

  @Test
  public void testAlreadySignedEnclosureDoNotNeedAddToSign() {
    final String variableName = "variableName";
    when(execution.hasVariable(variableName)).thenReturn(false);
    when(client.createClientRequest(any(ExchangeContext.class))).thenReturn(createClientRequest(1, true));

    smev.prepare(execution, SERVICE_NAME, variableName);

    verify(entityManager, times(1)).persist(any(ClientRequestEntity.class));
    verify(execution, times(1)).setVariable(eq(variableName), any(ClientRequestEntity.class));
    // enclosure должно помещаться в соответствующий attach
    verify(execution, times(0)).setVariable(eq(variableName + "_enclosure_to_sign_0"), Mockito.notNull());
    verify(execution, times(0)).setVariable(eq(variableName + "_enclosure_to_sign_vars"), any());  // 1 на конце это ид execution
  }

  @Test
  public void testAttemptAddEnclosureButVariableIsExist() {
    final String variableName = "variableName";
    when(execution.hasVariable(variableName)).thenReturn(false);
    when(execution.hasVariable(variableName + "_enclosure_to_sign_0")).thenReturn(false);
    when(execution.hasVariable(variableName + "_enclosure_to_sign_1")).thenReturn(true);
    when(client.createClientRequest(any(ExchangeContext.class))).thenReturn(createClientRequest(2, false));
    try {
      smev.prepare(execution, SERVICE_NAME, variableName);
      fail("IllegaStateException expected");
    } catch (IllegalStateException err) {
    }

    verify(entityManager, times(1)).persist(any(ClientRequestEntity.class));
    verify(execution, times(1)).setVariable(eq(variableName), any(ClientRequestEntity.class));
    verify(execution, times(1)).setVariable(eq(variableName + "_enclosure_to_sign_0"), Mockito.notNull());
    verify(execution, times(1)).removeVariable(variableName + "_enclosure_to_sign_0");
    verify(execution, times(0)).setVariable(eq(variableName + "_enclosure_to_sign_vars"), any()); // 1 на конце это ид execution
  }


  @Test
  public void testPrepareRequestNoEnclosure() {
    final String variableName = "variableName";
    when(execution.hasVariable(variableName)).thenReturn(false);
    when(client.createClientRequest(any(ExchangeContext.class))).thenReturn(createClientRequest(0, false));

    smev.prepare(execution, SERVICE_NAME, variableName);

    verify(entityManager, times(1)).persist(any(ClientRequestEntity.class));
    verify(execution, times(1)).setVariable(eq(variableName), any(ClientRequestEntity.class));
    // enclosure должно помещаться в соответствующий attach
    verify(execution, times(0)).setVariable(eq(variableName + "_enclosure_to_sign_0"), Mockito.notNull());
    verify(execution, times(0)).setVariable(eq(variableName + "_enclosure_to_sign_vars"), any()); // 1 на конце это ид execution

  }

  private ClientRequest createClientRequest(int countEnclosure, boolean createSignedEnclosure) {
    final ClientRequest clientRequest = new ClientRequest();
    final Packet packet = new Packet();
    packet.typeCode = Packet.Type.SERVICE;
    packet.status = Packet.Status.REQUEST;
    clientRequest.packet = packet;
    if (countEnclosure > 0) {
      clientRequest.enclosures = new Enclosure[countEnclosure];
      for (int idx = 0; idx < countEnclosure; idx++) {
        final Enclosure enclosure = new Enclosure("zipName.xml", new byte[]{0x00});
        if (createSignedEnclosure) {
          enclosure.signature = new Signature(null, null, null, true);
        }
        clientRequest.enclosures[idx] = enclosure;
      }
    }
    clientRequest.enclosureDescriptor = "описание вложения";
    return clientRequest;
  }

  private List<InfoSystemService> createInfoSystemService(String serviceName) {
    InfoSystemService service = new InfoSystemService();
    service.setAddress("testAddr");
    service.setId(1l);
    service.setSversion("1");
    service.setRevision("0");
    service.setAvailable(true);
    service.setName(serviceName);
    service.setSname(serviceName);
    service.setInfoSystem(new InfoSystem("testCode", "testName"));
    return asList(service);
  }
}
