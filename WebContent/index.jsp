<%@page import="models.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<% User user = (User)session.getAttribute("loggedUser"); %>

<% if (user == null) { 
	String ctx = request.getContextPath();
	response.sendRedirect(ctx + "/login");
	return;
} %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>TODO Project</title>
</head>
<body>
  <h1>TODO Project</h1>
	
  <h4>Welcome <%= user.getName() %></h4>	  
  <ul>
    <li><a href="./role/list">Roles</a></li>
    <li><a href="./user/list">Users</a></li>
    <li><a href="./project/list">Projects</a></li>
    <li><a href="./task/list">Tasks</a></li>
  </ul>
</body>
</html>
