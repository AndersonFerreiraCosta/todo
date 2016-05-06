package servlets.login;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import models.User;
import daos.UserDao;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher disp;
		disp = request.getRequestDispatcher("/login/form.jsp");
		disp.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			
			UserDao userDao = new UserDao();
			User user = userDao.findByEmail(email);
			
			if (user != null && user.authenticate(password)) {
				HttpSession session = request.getSession();
				session.setAttribute("loggedUser", user);
				
				String ctx = request.getContextPath();
				
				response.sendRedirect(ctx + "/index.jsp");
			} else {
				request.setAttribute("errorMessage", "Invalid login");
				RequestDispatcher disp;
				disp = request.getRequestDispatcher("/login/form.jsp");
				disp.forward(request, response);
			}
		} catch(Exception e) {
			throw new ServletException(e);
		}
	}

}
