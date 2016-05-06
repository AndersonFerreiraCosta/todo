package servlets.users;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tools.Converters;
import models.Role;
import models.User;
import daos.RoleDao;
import daos.UserDao;

@WebServlet("/user/update")
public class UpdateUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private RoleDao roleDao = null;
	private UserDao userDao = null;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		try { 
			roleDao = new RoleDao();
			userDao = new UserDao();
		} catch(Exception e) {
			throw new ServletException(e);
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			
			int id = Converters.stringToInt(request.getParameter("id"));
			
			User user = userDao.find(id);
			
			if (user == null) {
				response.setStatus(404);
				response.getWriter().print("User not found with id " + id);
				return;
			}
			
			request.setAttribute("user", user);
			
			RoleDao roleDao = new RoleDao();
			request.setAttribute("roles", roleDao.list());
			
			RequestDispatcher disp;
			disp = request.getRequestDispatcher("/user/form.jsp");
			disp.forward(request, response);
		} catch(Exception e) {
			throw new ServletException(e);
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			
			int id = Converters.stringToInt(request.getParameter("id"));
			
			User user = userDao.find(id);
			
			if (user != null) {
				user.setName(request.getParameter("name"));
				user.setEmail(request.getParameter("email"));
				user.setPassword(request.getParameter("password"));
				
				int roleId = Converters.stringToInt(request.getParameter("role_id"));
				user.setRole(roleDao.find(roleId));
	
				userDao.update(user);
			}
			
			String context = request.getContextPath();
			
			response.sendRedirect(context + "/user/list"); 
		} catch(Exception e) {
			throw new ServletException(e);
		}
	}

}
