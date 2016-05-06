package servlets.users;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.User;
import tools.Converters;
import daos.UserDao;

@WebServlet("/user/delete")
public class DeleteUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			int id = Converters.stringToInt(request.getParameter("id"));
			
			UserDao userDao = new UserDao();
			User user = userDao.find(id);
			
			if (user != null) {
				userDao.delete(user);
			}
			
			String context = request.getContextPath();
			response.sendRedirect(context + "/role/list");
		} catch(Exception e) {
			throw new ServletException(e);
		}
	}

}
