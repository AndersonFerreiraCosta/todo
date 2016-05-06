<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Login</title>
</head>
<body>
 <h1>Login</h1>
 <% String ctx = request.getContextPath(); %>
 
 <% String errorMsg = (String)request.getAttribute("errorMessage"); %>
	
 <% if (errorMsg != null) { %>
    <h3 style="color:red"><%= errorMsg %></h3>
 <% } %>
 	 
 <form action="<%=ctx%>/login" method="POST">
   <p>E-mail: <input type="text" name="email"/></p>
   <p>Password: <input type="password" name="password"/></p>
   <input type="submit" value="Login"/>
 </form>
</body>
</html>