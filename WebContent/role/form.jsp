<%@page import="models.Role"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>


<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">


<title>${role.id > 0 ? "Update" : "Create"} Role</title>
</head>
<body>

<fmt:setLocale value="${param.lang}"/>
<fmt:setBundle basename="mensagens" scope="application"/>

	<h1>${role.id > 0 ? "Update" : "Create"} Role</h1>

	<c:url value="/role/create" var="saveUrl" />

	<c:if test="${role.id > 0}">
		<c:url value="/role/update" var="saveUrl" />
	</c:if>


	<form action="${saveUrl}" method="POST">
		<input type="hidden" name="id" value="${role.id}">
		<p>
			<fmt:message key="role.name"/> <input type="text" name="name" value="${role.name}">
		</p>
		<p>
			<fmt:message key="role.manager"/> <input type="checkbox" name="manager"
				${role.manager ? 'checked' : ''}>
		</p>
		<input type="submit" value="Save">
	</form>

	<p>
	<c:url value="/role/list" var="backUrl"/>
		<a href="${backUrl}"><< Back to list</a>
	</p>
</body>
</html>