package servlets.roles;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tools.Converters;
import models.Role;
import daos.RoleDao;

@WebServlet("/role/delete")
public class DeleteRoleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			int id = Converters.stringToInt(request.getParameter("id"));
			
			RoleDao roleDao = new RoleDao();
			Role role = roleDao.find(id);
			
			if (role != null) {
				roleDao.delete(role);
			}
			
			String context = request.getContextPath();
			response.sendRedirect(context + "/role/list");
		} catch(Exception e) {
			throw new ServletException(e);
		}
	}

}
