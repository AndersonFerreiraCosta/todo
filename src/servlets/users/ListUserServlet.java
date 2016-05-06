package servlets.users;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.User;
import daos.UserDao;

@WebServlet("/user/list")
public class ListUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			
			UserDao userDao = new UserDao();
			
			List<User> list = userDao.list();
			
			//esse atributo estará disponível na view
			request.setAttribute("list", list);
			
			RequestDispatcher disp;
			disp = request.getRequestDispatcher("/user/list.jsp");
			disp.forward(request, response);
			
		} catch(Exception e) {
			throw new ServletException(e);
		}
	}

}
