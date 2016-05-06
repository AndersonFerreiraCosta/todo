<%@page import="models.Role"%>
<%@page import="java.util.List"%>
<%@page import="models.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<% User user = (User)request.getAttribute("user"); %>
<% String action = (user.getId() > 0) ? "update" : "create"; %>
<% String ctx = request.getContextPath(); %>

<title><%= (user.getId() > 0) ? "Edit" : "Add New" %> User</title>
</head>
<body>

  <h1><%= (user.getId() > 0) ? "Edit" : "Add New" %> User</h1>
	
  <form action="<%=ctx %>/user/<%=action%>" method="post">
    <input type="hidden" name="id" value="<%=user.getId()%>"/>
    <p>
     Name: 
     <input type="text" name="name" value="<%=user.getName()%>"/>
    </p>
    <p>
     E-mail: 
     <input type="text" name="email" value="<%=user.getEmail()%>"/>
    </p>
    <p>
     Password: 
     <input type="password" name="password"/>
    </p>
    <p>
     Role:
     <select name="role_id">
       <option value="-1"></option>
       <%
       List<Role> roles = (List<Role>)request.getAttribute("roles");
       for (Role role: roles) {%>
         <% String selected = (user.getRole() != null 
             && user.getRole().getId() == role.getId()) 
             ? "selected" : ""; 
         %>
         <option value="<%=role.getId()%>" <%=selected%>>
           <%=role.getName()%>
         </option>
       <% } %>
     </select>
    </p>
    <input type="submit" value="Save"/>  
   </form>
</body>
</html>