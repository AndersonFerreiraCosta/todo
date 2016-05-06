package servlets.projects;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Project;
import tools.Converters;
import daos.ProjectDao;

@WebServlet("/project/delete")
public class DeleteProjectServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			int id = Converters.stringToInt(request.getParameter("id"));
			
			ProjectDao projectDao = new ProjectDao();
			Project project = projectDao.find(id);
			
			if (project != null) {
				projectDao.delete(project);
			}
			
			String context = request.getContextPath();
			response.sendRedirect(context + "/project/list");
		} catch(Exception e) {
			throw new ServletException(e);
		}
	}
}