package servlets.roles;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import daos.RoleDao;
import models.Role;

@WebServlet("/role/create")
public class CreateRoleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Role role = new Role();
		request.setAttribute("role", role);
		
		RequestDispatcher disp;
		disp = request.getRequestDispatcher("/role/form.jsp");
		disp.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Role role = new Role();
		role.setName(request.getParameter("name"));
		role.setManager("on".equalsIgnoreCase(
				request.getParameter("manager")));
		
		try {
			RoleDao roleDao = new RoleDao();
			roleDao.create(role);
			
			String context = request.getContextPath();
			response.sendRedirect(context + "/role/list"); 
		} catch(Exception e) {
			throw new ServletException(e);
		}
	}
}