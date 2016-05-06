package servlets.tasks;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tools.Converters;
import models.Project;
import models.Task;
import daos.ProjectDao;
import daos.TaskDao;

@WebServlet("/task/list")
public class ListTaskServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private ProjectDao projectDao;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		try {
			projectDao = new ProjectDao();
		} catch(Exception e) {
			throw new ServletException(e);
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {

			String context = request.getContextPath();
			
			int projectId = Converters.stringToInt(request.getParameter("project_id"));

			Project project = projectDao.find(projectId);
			
			String html = "<html>";
			html += "<head>";
			html += "<title>List of Tasks</title>";
			html += "</head>";
			
			html += "<body>";
			html += "<h1>List of Task</h1>";
			html += "<form method=\"get\">";
			html += "<p>Project: " + createProjectSelectField(project);
			html += "&nbsp;<input type=\"submit\" value=\"Ok\"></p>";
			html += "</form>";
			html += "<p><a href=\"" + context + "/task/create\">Add New</a></p>";
		
			TaskDao taskDao = new TaskDao(projectDao);
			
			if (project != null) {
				List<Task> list = taskDao.listByProject(project);
				
				html += "<table>";
				html += "<tr>";
				html += "<th>Id</th>";
				html += "<th>Name</th>";
				html += "<th>Priority</th>";
				html += "<th>Status</th>";
				html += "<th>Assigned To</th>";
				html += "<th>Created By</th>";
				html += "</tr>";
				
				for (Task task : list) {
					html += "<tr>";
					html += "<td>" + task.getId() + "</td>";
					html += "<td>" + task.getName() + "</td>";
					html += "<td>" + task.getPriority() + "</td>";
					html += "<td>" + task.getStatus() + "</td>";
					html += "<td>" + task.getAssignedTo().getName() + "</td>";
					html += "<td>" + task.getCreatedBy().getName() + "</td>";
					html += "<td>" + createEditLink(context, task) + "</td>";
					html += "<td>" + createDeleteLink(context, task) + "</td>";
					html += "</tr>";
				}
	
				html += "</table>";
			}
			
			html += "<p><a href=\"" + context + "/index.jsp\"><< Back to menu</a></p>";

			html += "</body>";
			html += "</html>";
			
			response.setContentType("text/html");
			response.getWriter().print(html);

		} catch(Exception e) {
			throw new ServletException(e);
		}
	}
	
	private String createProjectSelectField(Project selected) throws Exception {
		String html = "<select name=\"project_id\">";
		html += "<option value=\"-1\"></option>";
		
		for (Project project : projectDao.list()) {
			html += "<option value=\"" + project.getId() + "\"";
			
			if (selected != null 
				&& selected.getId() == project.getId()) {
				html += " selected";
			}
					
			html += ">";
			html += project.getName() + "</option>";
		}
		
		html += "</select>";
		
		return html;
	}
	
	private String createEditLink(String context, Task task) {
		String html = "<a href=\"" + context 
				+ "/task/update?id=" + task.getId()
				+ "\">Edit</a>";
		
		return html;
	}
	
	private String createDeleteLink(String context, Task task) {
		String html = "<a href=\"" + context
				+ "/task/delete?id=" + task.getId()
				+ "\">Delete</a>";
		
		return html;
	}
}