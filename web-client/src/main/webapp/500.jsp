<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%--
  ~ This Source Code Form is subject to the terms of the Mozilla Public
  ~ License, v. 2.0. If a copy of the MPL was not distributed with this
  ~ file, You can obtain one at http://mozilla.org/MPL/2.0/.
  ~ Copyright (c) 2013, MPL CodeInside http://codeinside.ru
  --%>

<html xmlns="[http://www.w3.org/1999/xhtml" ] xml:lang="en">
<%@ page isErrorPage="true"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>500</title>
</head>
<body>
	<h1>500</h1>
	<p>Адрес: ${pageContext.errorData.requestURI}</p>
	<p>Код: ${pageContext.errorData.statusCode}</p>
	<p>Ошибка: ${pageContext.errorData.throwable}</p>
	<p>Компонент: ${pageContext.errorData.servletName}</p>
</body>
</html>