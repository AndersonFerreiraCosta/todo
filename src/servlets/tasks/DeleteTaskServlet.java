package servlets.tasks;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Task;
import tools.Converters;
import daos.ProjectDao;
import daos.TaskDao;

@WebServlet("/task/delete")
public class DeleteTaskServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			int id = Converters.stringToInt(request.getParameter("id"));
			
			ProjectDao projectDao = new ProjectDao();
			TaskDao taskDao = new TaskDao(projectDao);

			Task task = taskDao.find(id);
			
			if (task != null) {
				taskDao.delete(task);
			}
			
			String context = request.getContextPath();
			response.sendRedirect(context + "/task/list");
		} catch(Exception e) {
			throw new ServletException(e);
		}
	}
}