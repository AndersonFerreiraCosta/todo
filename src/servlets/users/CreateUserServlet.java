package servlets.users;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Role;
import models.User;
import daos.RoleDao;
import daos.UserDao;

@WebServlet("/user/create")
public class CreateUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private RoleDao roleDao = null;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		try { 
			roleDao = new RoleDao();
		} catch(Exception e) {
			throw new ServletException(e);
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			User user = new User();
			
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
		User user = new User();
		user.setName(request.getParameter("name"));
		user.setEmail(request.getParameter("email"));
		user.setPassword(request.getParameter("password"));
		
		int roleId = Integer.parseInt(request.getParameter("role_id"));
		
		try {
			user.setRole(roleDao.find(roleId));
			
			UserDao userDao = new UserDao();
			userDao.create(user);

			String context = request.getContextPath();
			
			response.sendRedirect(context + "/user/list"); 
		} catch(Exception e) {
			throw new ServletException(e);
		}
	}
}