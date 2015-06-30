/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.gws.p.router;

import com.sun.xml.ws.transport.http.HttpAdapter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;
import org.ops4j.pax.exam.util.Filter;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import ru.codeinside.gws.api.CryptoProvider;
import ru.codeinside.gws.api.Declarant;
import ru.codeinside.gws.api.InfoSystem;
import ru.codeinside.gws.api.Packet;
import ru.codeinside.gws.api.ReceiptContext;
import ru.codeinside.gws.api.RequestContext;
import ru.codeinside.gws.api.Revision;
import ru.codeinside.gws.api.Server;
import ru.codeinside.gws.api.ServerRequest;
import ru.codeinside.gws.api.ServerResponse;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.xml.namespace.QName;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Hashtable;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.ops4j.pax.exam.CoreOptions.junitBundles;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.options;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class) // зависимые тесты
public class GlassfishIntegrationTest {


    // на сборочном дикие тормоза
    @Inject
    @Filter(value = "osgi.web.contextpath=/smev", timeout = 180000L)
    private ServletContext servletContext;

    @Inject
    private CryptoProvider cryptoProvider;

    @Inject
    @Filter("component.name=mvvact")
    private Server server;

    @Inject
    private BundleContext bundleContext;

    ServiceRegistration<Declarant> declarantServiceRegistration;
    ServiceRegistration<Server> serverRegistration;

    {
        HttpAdapter.dump = true;
    }

    @Configuration
    public Option[] config() {

        Killer.killPortOwner(18080);

        return options(
                mavenBundle("ru.codeinside", "gws-p-registry-api", "1.0.1"),
                mavenBundle("ru.codeinside", "gws-p-registry-hc", "1.0.1"),
                mavenBundle("ru.codeinside", "gws-api", "1.0.11"),
                mavenBundle("ru.codeinside", "gws-wsdl", "1.1.0"),
                mavenBundle("ru.codeinside", "gws-core", "1.1.2"),
                mavenBundle("ru.codeinside", "gws-crypto-cryptopro", "1.0.4"),
                mavenBundle("ru.codeinside", "gws-s-oep-declarer", "1.0.5"),
                mavenBundle("ru.codeinside", "gws-p-adapter", "1.0.4"),
                mavenBundle("ru.codeinside", "gws-p-router", "1.0.6"),
                mavenBundle("ru.codeinside", "gws-xml-normalizer", "1.0.0"),
                mavenBundle("ru.codeinside", "gws-xml-signature-injector", "1.0.0"),

                junitBundles()
        );
    }

    @Before
    public void before() {
        assertNotNull(cryptoProvider);
        assertNotNull(server);
        assertNotNull(servletContext);

        Declarant declarant = new Declarant() {
            @Override
            public ServerResponse processRequest(ServerRequest serverRequest, String name) {
                ServerResponse sresponse = new ServerResponse();
                sresponse.action = new QName("http://mvv.oep.com/", "putData");
                Packet p = new Packet();
                sresponse.packet = p;
                p.exchangeType = "Test";
                p.serviceCode = "111111111111";
                p.requestIdRef = "111111111111";
                p.originRequestIdRef = "111111111111";
                p.caseNumber = "111111111111";
                p.typeCode = Packet.Type.SERVICE;
                p.status = Packet.Status.PROCESS;
                p.recipient = p.sender = new InfoSystem("PNZR01581", "111111111");
                p.date = new Date();
                return sresponse;
            }
        };

        Server server1 = new Server() {
            @Override
            public Revision getRevision() {
                return Revision.rev120315;
            }

            @Override
            public URL getWsdlUrl() {
                return null;
            }

            @Override
            public ServerResponse processRequest(RequestContext requestContext) {
                return null;
            }

            @Override
            public ServerResponse processStatus(String statusMessage, ReceiptContext exchangeContext) {
                return null;
            }

            @Override
            public ServerResponse processResult(String resultMessage, ReceiptContext exchangeContext) {
                return null;

            }
        };
        declarantServiceRegistration = bundleContext.registerService(Declarant.class, declarant, null);
        Hashtable<String, Object> p = new Hashtable<String, Object>();
        p.put("component.name", "xyz");
        serverRegistration = bundleContext.registerService(Server.class, server1, p);
    }

    @After
    public void after() {
        if (declarantServiceRegistration != null) {
            declarantServiceRegistration.unregister();
            declarantServiceRegistration = null;
        }
        if (serverRegistration != null) {
            serverRegistration.unregister();
            serverRegistration = null;
        }
    }

    @Test
    public void wsdlInfo() throws InterruptedException, IOException {
        // 1. Общий индекс
        HttpURLConnection con = (HttpURLConnection) (new URL("http://localhost:18080/smev").openConnection());
        con.setDoInput(true);
        assert200(con);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        copy(con.getInputStream(), bos);
        String response = bos.toString("UTF8");
        assertTrue(response, response.contains("mvvact"));
        assertTrue(response, response.contains("xyz")); // саморегистрация!

        // 2.Сервис
        con = (HttpURLConnection) (new URL("http://localhost:18080/smev/mvvact").openConnection());
        con.setDoInput(true);
        assert200(con);
        bos = new ByteArrayOutputStream();
        copy(con.getInputStream(), bos);
        response = bos.toString("UTF8");
        assertTrue(response, response.contains("?wsdl"));

        // 3. Схема
        con = (HttpURLConnection) (new URL("http://localhost:18080/smev/mvvact/mvvact_schema1.xsd").openConnection());
        con.setDoInput(true);
        assert200(con);
        bos = new ByteArrayOutputStream();
        copy(con.getInputStream(), bos);
        response = bos.toString("UTF8");
        assertTrue(response, response.contains("Include"));
    }

    @Test
    public void soap() throws InterruptedException, IOException {
        HttpURLConnection con = (HttpURLConnection) (new URL("http://localhost:18080/smev/mvvact").openConnection());
        con.setConnectTimeout(30 * 1000);
        con.setReadTimeout(30 * 1000);
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-type", "text/xml; charset=utf-8");
        con.setDoOutput(true);
        con.setDoInput(true);
        copy(getClass().getClassLoader().getResourceAsStream("soap/request-1.xml"), con.getOutputStream());
        assert200(con);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        copy(con.getInputStream(), bos);
        String response = bos.toString("UTF8");
        assertTrue(response, response.contains("http://smev.gosuslugi.ru/actors/smev"));
    }

    private void assert200(HttpURLConnection con) throws IOException {
        if (con.getResponseCode() != 200) {
            String response;
            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                copy(con.getErrorStream(), bos);
                response = bos.toString("UTF8");
            } catch (IOException e) {
                response = "NESTED ERROR:" + e.getMessage();
            }
            fail("Code:" + con.getResponseCode() + ", error: " + response);
        }
    }


    private void copy(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[1024];
        int n;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
        input.close();
        output.close();
    }
}
