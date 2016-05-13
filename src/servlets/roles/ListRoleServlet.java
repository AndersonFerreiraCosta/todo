package servlets.roles;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import models.User;
import daos.RoleDao;

@WebServlet("/role/list")
public class ListRoleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		
		User user  = (User)session.getAttribute("loggedUser");
		
		if (user == null) {
			String ctx = request.getContextPath();
			response.sendRedirect(ctx + "/login");
			return;
		}
		
		try {
			RoleDao roleDao = new RoleDao();
			
			request.setAttribute("list", roleDao.list());
			
			RequestDispatcher disp;
			disp = request.getRequestDispatcher("/role/list.jsp");
			disp.forward(request, response);
		} catch(Exception e) {
			throw new ServletException(e);
		}
	}
	
}