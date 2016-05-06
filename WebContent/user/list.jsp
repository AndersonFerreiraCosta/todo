<%@page import="models.User"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Lisf of Users</title>
</head>
<body>
  <h1>List of Users</h1>
  <table>
  	<tr>
  		<th>Id</th>
  		<th>Name</th>
  		<th>Role</th>
  	</tr>
  	<%
  	String ctx = request.getContextPath(); 
  	List<User> list = (List<User>)request.getAttribute("list");
  	for (User user: list) {%>
  	<tr>
  	  <td> <%= user.getId() %> </td>
  	  <td> <%= user.getName() %> </td>
  	  <td> <%= user.getRole().getName() %> </td>
  	  <td>
  	    <a href="<%=ctx %>/user/update?id=<%=user.getId()%>">Edit</a>
  	  </td>
  	  <td>
  	    <a href="<%=ctx %>/user/delete?id=<%=user.getId()%>">Delete</a>
  	  </td>
  	</tr>
  	<% } %>
  </table>
  <a href="<%=ctx%>/user/create">Add New</a><br>
  <a href="<%=ctx%>/"><< Back to menu</a>
</body>
</html>

