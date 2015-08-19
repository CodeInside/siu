/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.core.cproto;

import com.sun.xml.ws.transport.http.client.HttpTransportPipe;
import org.junit.Ignore;
import ru.codeinside.gws.api.ClientRequest;
import ru.codeinside.gws.api.ClientResponse;
import ru.codeinside.gws.api.InfoSystem;
import ru.codeinside.gws.stubs.DummyContext;
import ru.codeinside.gws.stubs.DummyProvider;
import ru.codeinside.gws.wsdl.ServiceDefinitionParser;
import ru.codeinside.gws.xml.normalizer.XmlNormalizerImpl;
import ru.codeinside.gws3970c.UniversalClient;

/*
* re #501. Try to kill DbIdGenerator
* */
public class ActivitiDosTest {
  @Ignore
  public void testActivitiDbGeneratorKill() throws InterruptedException {
    for(int i=0; i<50; i++){
      Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
          InfoSystem pnzr01581 = new InfoSystem("PNZR01581", "Комплексная система предоставления государственных и муниципальных услуг Пензенской области");
          String ADDRESS = "http://192.168.0.93:8888/smev/mvvact";
          final UniversalClient universalClient = new UniversalClient();
          final ClientRev120315 rev120315 = new ClientRev120315(new ServiceDefinitionParser(), new DummyProvider(),
                  new XmlNormalizerImpl(), null);
          final DummyContext ctx = new DummyContext();
          ctx.setVariable("smevTest", "Первичный запрос");
          ctx.setVariable("appData_var1", "Первичный запрос");
          ctx.setVariable("appData_var2", "Первичный запрос");
          ctx.setVariable("appData_var3", "Первичный запрос");
          ctx.setVariable("appData_var4", "Первичный запрос");
          ctx.setVariable("appData_var5", "Первичный запрос");
          ctx.setVariable("appData_var6", "Первичный запрос");
          ctx.setVariable("appData_var7", "Первичный запрос");
          ctx.setVariable("appData_var8", "Первичный запрос");
          ctx.setVariable("appData_var9", "Первичный запрос");
          ctx.setVariable("appData_var10", "Первичный запрос");
          ctx.setVariable("appData_var11", "Первичный запрос");
          ctx.setVariable("appData_var12", "Первичный запрос");
          ctx.setVariable("appData_var13", "Первичный запрос");
          ctx.setVariable("appData_var14", "Первичный запрос");
          ctx.setVariable("appData_var15", "Первичный запрос");
          ctx.setVariable("appData_var16", "Первичный запрос");
          ctx.setVariable("appData_var17", "Первичный запрос");
          ctx.setVariable("appData_var18", "Первичный запрос");
          ctx.setVariable("appData_var19", "Первичный запрос");
          ctx.setVariable("appData_var20", "Первичный запрос");
          ctx.setVariable("appData_var21", "Первичный запрос");
          ctx.setVariable("appData_var22", "Первичный запрос");
          ctx.setVariable("appData_var23", "Первичный запрос");
          ctx.setVariable("appData_var24", "Первичный запрос");
          ctx.setVariable("appData_var25", "Первичный запрос");
          ctx.setVariable("appData_var26", "Первичный запрос");
          ctx.setVariable("appData_var27", "Первичный запрос");
          ctx.setVariable("procedureCode", 666);
          HttpTransportPipe.dump = true;
          while (true) {
            ClientRequest request = universalClient.createClientRequest(ctx);
            request.portAddress = ADDRESS;
            request.packet.sender = request.packet.originator = pnzr01581;
            ClientResponse response = rev120315.send(universalClient.getWsdlUrl(), request, null);
            universalClient.processClientResponse(response, ctx);
            if (Boolean.TRUE != ctx.getVariable("smevPool")) {
              break;
            }
            System.out.println("Ждём 5 секунд...");
            try {
              Thread.sleep(5000);
            } catch (InterruptedException e) {
              e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            ctx.setVariable("smevTest", "Повторный запрос");
          }
        }
      });
      thread.start();
    }
    while(true){
    }
  }
}
