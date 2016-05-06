<%@page import="models.Role"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>List of Roles</title>
</head>
<body>
	<h1>List of Roles</h1>
	
	<% String ctx = request.getContextPath(); %>
	
	<p>
	<a href="<%=ctx%>/role/create">Add new</a>
	</p>
	
	<table>
		<tr>
			<th>Id</th>
			<th>Name</th>
			<th>Manager?</th>
		</tr>
		<% List<Role> list = (List<Role>)request.getAttribute("list");%>
		
		<% for (Role role : list) {%>
		<tr>
			<td><%= role.getId() %></td>
			<td><%= role.getName() %></td>
			<td><%= role.isManager() ? "Yes" : "No" %></td>
			<td>
			<a href="<%=ctx%>/role/update?id=<%=role.getId()%>">
				Edit
			</a>
			</td>
			<td>
			<a href="<%=ctx%>/role/delete?id=<%=role.getId()%>">
				Delete
			</a>
			</td>
		</tr>
		<% } %>
	</table>
	<p>
	<a href="<%=ctx%>/index.jsp"> << Back to menu</a>
	</p>
</body>
</html>