<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="ru.codeinside.adm.AdminServiceProvider" %>
<%@ page import="ru.codeinside.adm.database.News" %>
<%@ page import="ru.codeinside.gses.API" %>
<%@ page import="java.util.Iterator" %>
<%--
  ~ This Source Code Form is subject to the terms of the Mozilla Public
  ~ License, v. 2.0. If a copy of the MPL was not distributed with this
  ~ file, You can obtain one at http://mozilla.org/MPL/2.0/.
  ~ Copyright (c) 2013, MPL CodeInside http://codeinside.ru
  --%>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <script src="/web-client/jquery-2.1.4.min.js" type="text/javascript"></script>
    <script src="/web-client/jquery.maskedinput.min.js" type="text/javascript"></script>
    <title>Вход</title>
</head>
<body>
<center>
    <script>
        jQuery(function($){
            $("#snils").mask("999-999-999 99");
        });
    </script>

    <jsp:useBean id="news" scope="session" class="java.util.Vector"/>

    <%
        String allowEsia = AdminServiceProvider.get().getSystemProperty(API.ALLOW_ESIA_LOGIN);
        if ("true".equals(allowEsia)) {
    %>

    <form method="post" action="authServlet">
        <table style="margin: 15% 0 5% 0;">
            <tr valign="middle" align="center">
                <td>
                    <table>
                        <tr>
                            <td></td>
                            <th>Система исполнения услуг</th>
                        </tr>
                        <tr>
                            <td align="right">СНИЛС</td>
                            <td><input id="snils" type="text" name="j_username"/></td>
                        </tr>
                        <tr>
                            <td align="right">Пароль</td>
                            <td><input type="password" name="j_password"/></td>
                        </tr>
                        <tr>
                            <td></td>
                            <td><input type="submit" value="Вход"/></td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </form>

    <%
        } else {
    %>

    <form method="post" action="authServlet">
        <table style="margin: 15% 0 5% 0;">
            <tr valign="middle" align="center">
                <td>
                    <table>
                        <tr>
                            <td></td>
                            <th>Система исполнения услуг</th>
                        </tr>
                        <tr>
                            <td align="right">Пользователь</td>
                            <td><input type="text" name="j_username"/></td>
                        </tr>
                        <tr>
                            <td align="right">Пароль</td>
                            <td><input type="password" name="j_password"/></td>
                        </tr>
                        <tr>
                            <td></td>
                            <td><input type="submit" value="Вход"/></td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </form>

    <%
        }
    %>
    <FONT color="#C0C0C0"><b>НОВОСТИ</b></FONT>

    <table style="margin: 2% 0 0 0;">
        <tr>
            <td> <%
                    for (Iterator i = news.iterator(); i.hasNext(); ) {
                        News currentNews = (News) i.next();
                %>
            <td width="300" style="vertical-align: top;">
                    <b style="color: #FFFFFF; background-color: #C0C0C0; border-radius: 3px"><%=currentNews.getDateCreated2()%>
                </b>
                <br><b><%=currentNews.getTitle()%>
            </b>
                <br>
                <small><%=currentNews.getText()%>
                </small>

            </td>
            <% } %>
            </td>
        </tr>
    </table>
</center>
</body>
</html>