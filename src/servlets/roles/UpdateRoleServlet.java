package servlets.roles;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tools.Converters;
import daos.RoleDao;
import models.Role;

@WebServlet("/role/update")
public class UpdateRoleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private RoleDao roleDao = null;
       
	@Override
	public void init(ServletConfig config) throws ServletException {
		try {
			roleDao = new RoleDao();
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			int id = Converters.stringToInt(request.getParameter("id"));
			
			Role role = roleDao.find(id);
			
			if (role == null) {
				response.setStatus(404);
				response.getWriter().print("Role not found with id " + id);
				return;
			}
			
			request.setAttribute("role", role);
			
			RequestDispatcher disp;
			disp = request.getRequestDispatcher("/role/form.jsp");
			disp.forward(request, response);
		} catch(Exception e) {
			throw new ServletException(e);
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			int id = Converters.stringToInt(request.getParameter("id"));
			
			Role role = roleDao.find(id);
			
			if (role == null) {
				response.setStatus(404);
				response.getWriter().print("Role not found with id " + id);
				return;
			}
			
			role.setName(request.getParameter("name"));
			role.setManager("on".equalsIgnoreCase(request.getParameter("manager")));
		
			roleDao.update(role);
			
			String context = request.getContextPath();
			
			response.sendRedirect(context + "/role/list");
		} catch(Exception e) {
			throw new ServletException(e);
		}
	}
}