<%@page import="models.Role"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<% String ctx = request.getContextPath(); %>
<% Role role = (Role)request.getAttribute("role"); %>
<% String action = role.getId() > 0 ? "update" : "create" ; %>

<title><%= role.getId() > 0 ? "Update" : "Create"%> Role</title>
</head>
<body>

<h1><%= role.getId() > 0 ? "Update" : "Create"%> Role</h1>

<form action="<%=ctx%>/role/<%=action%>" method="POST">
  <input type="hidden" name="id" value="<%=role.getId()%>">
  <p>Name: <input type="text" name="name" value="<%=role.getName()%>"></p>
  <p>Manager? 
   <input type="checkbox" name="manager" <%=role.isManager() ? "checked" : "" %>>
  </p>
  <input type="submit" value="Save">
</form>

<p>
 <a href="<%=ctx%>/role/list"><< Back to list</a>
</p>
</body>
</html>