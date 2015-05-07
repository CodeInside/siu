<%@ page import="java.util.logging.Logger" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%--
  ~ This Source Code Form is subject to the terms of the Mozilla Public
  ~ License, v. 2.0. If a copy of the MPL was not distributed with this
  ~ file, You can obtain one at http://mozilla.org/MPL/2.0/.
  ~ Copyright (c) 2013, MPL CodeInside http://codeinside.ru
  --%>

<html xmlns="[http://www.w3.org/1999/xhtml]" xml:lang="en">
<%@ page isErrorPage="true"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Отказано в доступе</title>
</head>
<body>
	<h1>Отказано в доступе</h1>
	<p>
		Ресурс <em>${pageContext.request.requestURI}</em> не доступен для <em>${pageContext.request.userPrincipal.name}</em>.
	</p>
	<ul>
		<li><a href="${pageContext.request.contextPath}/ui">Раздел исполнения</a></li>
		<%
			if (request.isUserInRole("Administrator")) {
		%>
		<li><a href="${pageContext.request.contextPath}/admin">Раздел
				управления</a></li>
		<%
			}
		%>
		<li><a href="${pageContext.request.contextPath}/logout.jsp">Выход</a>
		</li>
	</ul>
</body>
</html>