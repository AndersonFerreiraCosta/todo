<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Login</title>
</head>
<body>

<fmt:setLocale value="${param.lang}"/>
<fmt:setBundle basename="mensagens" scope="application"/>


<h1><fmt:message key="login.title"/></h1>
 
 

	
 <c:if test="${ not empty errorMessage }">

    <h3 style="color:red">${ errorMessage }></h3>
</c:if>

<form action="${loginUrl}" method="POST">
   <p><fmt:message key="login.email"/><input type="text" name="email"/></p>
   <p><fmt:message key="login.password"/><input type="password" name="password"/></p>
   <input type="submit" value="<fmt:message key="login.submit"/>"/>
 </form>
</body>
</html>