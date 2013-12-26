<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%--
  ~ This Source Code Form is subject to the terms of the Mozilla Public
  ~ License, v. 2.0. If a copy of the MPL was not distributed with this
  ~ file, You can obtain one at http://mozilla.org/MPL/2.0/.
  ~ Copyright (c) 2013, MPL CodeInside http://codeinside.ru
  --%>

<html xmlns="[http://www.w3.org/1999/xhtml]" xml:lang="ru">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
<title>Ошибка входа</title>
</head>
<body>
	<table style="width: 90%; height: 90%">
		<tr valign="middle" align="center">
			<td>
				<table>
					<tr>
						<td><strong>Неправильный логин или пароль!</strong></td>
					</tr>
					<tr>
						<td><em>Провертье раскладку клавиатуры и режим CAPS LOCK</em></td>
					</tr>
					<tr>
						<td><a href="${pageContext.request.contextPath}/login">Повторить попытку</a>.</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</body>
</html>